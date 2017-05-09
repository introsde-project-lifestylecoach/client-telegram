package lifestylecoach.client.telegram.lifestylecoach.models;

/**
 * Created by matteo on 09/05/17.
 */
public class MeasureUpdate extends ProcessCentricMessage {

    public static final String ID_REQ = "REQ_MEASURE_UPDATE";

    private String measureName;
    private String measureValue;
    private String data;
}
