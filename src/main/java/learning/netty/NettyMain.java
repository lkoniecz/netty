package learning.netty;

public class NettyMain {

    public static void main(String[] args) {
        long number = ipToNumber("1.0.0.0");
        System.out.println(number);
        System.out.println(numberToIP(number));

        long number_two = 16777471;
        System.out.println(numberToIP(number_two));

        long number_three = 16777472;
        System.out.println(numberToIP(number_three));
    }

    public static void earnXP(int exp) {
        System.out.println("earn xp " + exp);
    }

    public static void methodOne() {
        System.out.println("method one");
    }
	
    public static void methodTwo() {
        System.out.println("methodTwo");
    }

    public static void branch1() {
        System.out.println("branch1");
    }

    public static void branch2() {
        System.out.println("branch2");
    }

    public static long ipToNumber(String ipstring) {
        String[] ipAddressInArray = ipstring.split("\\.");
        long result = 0;
        long ip = 0;
        for (int x = 3; x >= 0; x--) {
            ip = Long.parseLong(ipAddressInArray[3 - x]);
            result |= ip << (x << 3);
        }
        return result;
    }

    public static String numberToIP(long number) {
        int w = (int) ((number / 16777216) % 256);
        int x = (int) ((number / 65536) % 256);
        int y = (int) ((number / 256) % 256);
        int z = (int) (number % 256);
        return w + "." + x + "." + y + "." + z;
    }

}
