package rxjava.existing.imperative.concurrency;

import org.junit.jupiter.api.Test;
import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rxjava.utils.RxTestUtils;
import org.apache.commons.lang3.tuple.Pair;

public class ConcurrencyTestCase {

    class Flight {
        private String flightNo;

        public Flight(String flightNo) {
            this.flightNo = flightNo;
        }
    }

    private class Passenger {
        private long id;

        public Passenger(long id) {
            this.id = id;
        }
    }

    private class Ticket {
        private Flight flight;
        private Passenger passenger;

        public Ticket(Flight flight, Passenger passenger) {
            this.flight = flight;
            this.passenger = passenger;
        }

        @Override
        public String toString() {
            return "Ticket - flight: " + flight.flightNo + ", passenger: " + passenger.id;
        }
    }

    private class SmtpResponse {
        private Ticket ticket;

        public SmtpResponse(Ticket ticket) {
            this.ticket = ticket;
        }
    }

    Flight lookupFlight(String flightNo) {
        RxTestUtils.log("lookupFlight: " + flightNo);
        return new Flight(flightNo);
    }

    Passenger findPassenger(long id) {
        RxTestUtils.log("findPassenger: " + id);
        return new Passenger(id);
    }

    Ticket bookTicket(Flight flight, Passenger passenger) {
        RxTestUtils.log("bookTicket: " + flight.flightNo + " " + passenger.id);
        return new Ticket(flight, passenger);
    }

    SmtpResponse sendEmail(Ticket ticket) {
        RxTestUtils.log("sendEmail: " + ticket);
        return new SmtpResponse(ticket);
    }

    Observable<Flight> rxLookupFlight(String flightNo) {
        return Observable.defer(() -> Observable.just(lookupFlight(flightNo)));
    }

    Observable<Passenger> rxFindPassenger(long id) {
        return Observable.defer(() -> Observable.just(findPassenger(id)));
    }

    Observable<Ticket> rxBookTicket(Flight flight, Passenger passenger) {
        return Observable.defer(() -> Observable.just(bookTicket(flight, passenger)));
    }

    @Test
    public void synchronousTestCase() {
        Observable<Flight> flight = rxLookupFlight("LOT 783");
        Observable<Passenger> passenger = rxFindPassenger(42);
        Observable<Ticket> ticket = flight.zipWith(passenger, (f, p) -> bookTicket(f, p));
        ticket.subscribe(this::sendEmail);
    }

    @Test
    public void semiAsynchronousTestCase() throws InterruptedException {
        Observable<Flight> flight = rxLookupFlight("LOT 783")
                .subscribeOn(Schedulers.io());
        Observable<Passenger> passenger = rxFindPassenger(42)
                .subscribeOn(Schedulers.io());
        Observable<Ticket> ticket = flight.zipWith(passenger, (f, p) -> bookTicket(f, p)); // this is blocking
        ticket.subscribe(this::sendEmail);
        Thread.sleep(10);
    }


    private Observable<Integer> doubleIt(int value) {
        return Observable.just(value * 2);
    }

    @Test
    public void flatMapTest() {
        Observable<Observable<Integer>> map = Observable
                .range(1, 100)
                .map(this::doubleIt);

        Observable<Integer> integerObservable = map
                .flatMap(item -> item);
    }

    @Test
    public void fullyAsynchronousTestCase() throws InterruptedException {
        Observable<Flight> flight = rxLookupFlight("LOT 783")
                .subscribeOn(Schedulers.io());
        Observable<Passenger> passenger = rxFindPassenger(42)
                .subscribeOn(Schedulers.io());

        Observable<Ticket> ticketObservable = flight
                .zipWith(passenger, (Flight f, Passenger p) -> Pair.of(f, p))
                .flatMap(pair -> rxBookTicket(pair.getLeft(), pair.getRight()));

        ticketObservable.subscribe(this::sendEmail);
        Thread.sleep(100);
    }
}
