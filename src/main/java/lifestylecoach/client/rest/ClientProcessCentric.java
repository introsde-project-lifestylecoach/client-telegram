package lifestylecoach.client.rest;


import com.google.gson.Gson;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;

public class ClientProcessCentric extends RestClient {

    public static final String NEW_SURNAME = "/user/new";
    public static final String USER_EXIST = "/user/exist";
    public static final String USER_PROFILE = "/user/profile";
    public static final String NEW_MEASURE = "/measure/new";
    public static final String NEW_HEIGHT = "/measure/new";
    public static final String NEW_WEIGHT = "/measure/new";
    public static final String SHOW_MEASURES = "/measure";

    public ClientProcessCentric(String serviceURI) {
        super(serviceURI);
    }

    /*public String getMeasures() {

        return getApi(USER_PROFILE+"/"+uid);

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
    }*/

    public boolean newMeasure(String req) {
        // TODO fare json di req
        return newApi(NEW_MEASURE, req);
    }

    public boolean newNameSurname(String user) {
        // TODO fare json di surname
        return newApi(NEW_SURNAME, user);
    }


    public boolean newHeight(String measure) {
        // TODO fare json di height

        /*
        * something like
        * newheight
        * {
        *   user : 12341531,
        *   measuretype : height,
        *   value : 123
        * }
        *
        * */

        return newApi(NEW_HEIGHT, measure);
    }

    public boolean newWeight(String measure) {
        // TODO fare json di weight
        return newApi(NEW_WEIGHT, measure);
    }

    public String seeProfile(Integer uid) {
        return getApi(USER_PROFILE + "/" + uid);
    }

    public boolean userExist(Integer uid) {

        // This call return a json like this {success : False}
        String getRes = getApi(USER_EXIST + "/" + uid);
        System.out.println(getRes);
        if (getRes != "") {
            Gson gson = new Gson();
            HashMap<String, Boolean> res = new HashMap<String, Boolean>();
            res = (HashMap<String, Boolean>) gson.fromJson(getRes, res.getClass());
            return res.get("success");
        } else
            return false; //TODO se non c'è connessione torna che è falso, che è come dire "l'utente non esiste"
    }

    public boolean newApi(String address, String parameter) {

        Response response;
        int status;
        boolean res = false;

        try {
            response = service.path(address)
                    .request()
                    .accept(MediaType.APPLICATION_JSON)
                    .post(Entity.json(parameter));

            status = response.getStatus();

            System.out.println(status);

            if (status == 202 || status == 200 || status == 201) {
                res = true;
            }
        } catch (javax.ws.rs.ProcessingException e) {
            System.err.println("Error : Connection refused");
        }

        return res;
    }

    public String getApi(String path) {
        Response response;
        int status;
        String res = "";

        try {
            response = service.path(path)
                    .request()
                    .accept(MediaType.APPLICATION_JSON)
                    .get();

            status = response.getStatus();

            if (status == 200) {
                res = response.readEntity(String.class);
            }
        } catch (javax.ws.rs.ProcessingException e) {
            System.err.println("Error : Connection refused");
        }

        return res;
    }
}
