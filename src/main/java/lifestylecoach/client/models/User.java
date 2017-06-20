package lifestylecoach.client.models;

public class User {

    public Long uid;
    public String name;
    public String surname;
    public String sex;
    public String birthdate;
    public String waist;
    public String hip;

    public User(Long uid, String name, String surname, String sex, String birthdate, String waist, String hip) {
        this.uid = uid;
        this.name = name;
        this.surname = surname;
        this.sex = sex;
        this.birthdate = birthdate;
        this.waist = waist;
        this.hip = hip;
    }

}
