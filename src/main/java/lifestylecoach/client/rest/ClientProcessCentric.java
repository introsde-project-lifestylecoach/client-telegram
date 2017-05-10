package lifestylecoach.client.rest;


import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by matteo on 10/05/17.
 */
public class ClientProcessCentric extends RestClient {

    public static final String SHOW_MEASURES = "/measure";
    public static final String NEW_MEASURE = "/measure/new";

    public ClientProcessCentric(String serviceURI) {
        super(serviceURI);
    }

    public String getMeasures() {
        Response response;
        int status;
        String res = "";

        response = service.path(SHOW_MEASURES)
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get();

        status = response.getStatus();

        if (status == 200) {
            res = response.readEntity(String.class);
        }

        return res;
    }

    public boolean newMeasure(String req) {
        Response response;
        int status;
        boolean res = false;

        response = service.path(NEW_MEASURE)
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.json(req));

        status = response.getStatus();

        if (status == 202 || status == 200 || status == 201) {
            res = true;
        }

        return res;
    }

}
