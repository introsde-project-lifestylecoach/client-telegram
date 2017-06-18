package lifestylecoach.client.models;

public class User {

    public Long uid;
    public String name;
    public String surname;

    public User(Long uid, String name, String surname) {
        this.uid = uid;
        this.name = name;
        this.surname = surname;
    }

}
