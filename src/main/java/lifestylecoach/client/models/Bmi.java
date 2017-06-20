package lifestylecoach.client.models;

/**
 * Created by matteo on 20/06/17.
 */
public class Bmi {

    public String bmiRisk;
    public String bmiStatus;
    public double bmiValue;
    public double bmrValue;
    public String idealWeight;
    public String whrStatus;

    public Bmi(String bmiRisk, String bmiStatus, double bmiValue, double bmrValue, String idealWeight, String whrStatus) {
        this.bmiRisk = bmiRisk;
        this.bmiStatus = bmiStatus;
        this.bmiValue = bmiValue;
        this.bmrValue = bmrValue;
        this.idealWeight = idealWeight;
        this.whrStatus = whrStatus;
    }
}
