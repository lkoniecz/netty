package learning.netty.model;

import java.io.Serializable;

public class User implements Serializable {
    private String firstName;

    public User(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

}
