package lifestylecoach.client.models;

public class Measure {

    public Long uid;
    public String measureType;
    public String measureValue;
    public String date;

    public Measure(Long uid, String measureType, String measureValue, String date) {
        this.uid = uid;
        this.measureType = measureType;
        this.measureValue = measureValue;
        this.date = date;
    }

}
