package lifestylecoach.client.models;

/**
 * Created by matteo on 09/05/17.
 */
public class MeasureShow extends ProcessCentricMessage {

    public static final String ID_REQ = "REQ_MEASURE_SHOW";

    private String measureType; // type of measure that we want to see (default 'all')
    private int bufferSize = 0; // It show how many measures we want to see (0 means "all")

    public static String getIdReq() {
        return ID_REQ;
    }

    public String getMeasureType() {
        return measureType;
    }

    public void setMeasureType(String measureType) {
        this.measureType = measureType;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }
}
