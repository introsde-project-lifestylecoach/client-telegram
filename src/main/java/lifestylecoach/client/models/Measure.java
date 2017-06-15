package lifestylecoach.client.models;

public class Measure {

    public Integer uid;
    public String measureType;
    public String measureValue;

    public Measure(Integer uid, String measureType, String measureValue) {
        this.uid = uid;
        this.measureType = measureType;
        this.measureValue = measureValue;
    }

}
