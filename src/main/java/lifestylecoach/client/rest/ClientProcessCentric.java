package lifestylecoach.client.rest;


import com.google.gson.Gson;
import lifestylecoach.client.models.Success;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;

public class ClientProcessCentric extends RestClient {

    public static final String NEW_USER = "/user/new";
    public static final String USER_EXIST = "/user/exist";
    public static final String USER_PROFILE = "/user/profile";
    public static final String NEW_MEASURE = "/measure/new";
    public static final String NEW_HEIGHT = "/measure/new";
    public static final String NEW_WEIGHT = "/measure/new";
    public static final String SHOW_MEASURES = "/measure/show";
    public static final String SHOW_GOALS = "/goal/show";
    public static final String UPDATE_GOAL = "/goal/new";
    public static final String DELETE_GOAL = "goal/delete";
    public static final String BMI = "user/bmi";

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

    public boolean newMeasure(String measure) {
        return newApi(NEW_MEASURE, measure);
    }

    public boolean newNameSurname(String user) {
        return newApi(NEW_USER, user);
    }


    public boolean newHeight(String measure) {

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


    public String getGoals(Long uid) {
        return getApi(SHOW_GOALS + "/" + uid);
    }

    public boolean newWeight(String measure) {
        // TODO fare json di weight
        return newApi(NEW_WEIGHT, measure);
    }

    public String seeProfile(Long uid) {
        return getApi(USER_PROFILE + "/" + uid);
    }

    public String getMeasures(Long uid, String type) {
        return getApi(SHOW_MEASURES + "/" + uid + "/" + type);
    }

    public boolean userExist(Long uid) {

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

    public boolean newApi(String path, String parameter) {

        Response response;
        int status;
        boolean res = false;

        // TODO CECK OF THE RESPONSE

        try {
            response = service.path(path)
                    .request()
                    .accept(MediaType.APPLICATION_JSON)
                    .post(Entity.json(parameter));

            status = response.getStatus();

            System.out.println(":: POST -> status \"" + path + "\" : " + status);

            if (status == 202 || status == 200 || status == 201) {
                res = true;
            }
        } catch (javax.ws.rs.ProcessingException e) {
            System.err.println("Error : Connection refused in \'" + path + "\'");
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

            System.out.println(":: GET -> status \"" + path + "\" : " + status);

            if (status == 200) {
                res = response.readEntity(String.class);
            }
        } catch (javax.ws.rs.ProcessingException e) {
            System.err.println("Error : Connection refused in \'" + path + "\'");
        }

        return res;
    }

    public Boolean updateGoal(Long uid, String oldTitle, String goalJson) {
        return newApi(UPDATE_GOAL + "/" + uid + "/" + oldTitle, goalJson);
    }

    public boolean updatePerson(String user) {
        return newUserRegistration(user);
    }

    public String getBmi(Long uid) {
        return getApi(BMI + "/" + uid);
    }

    public boolean newUserRegistration(String user) {
        return newApi(NEW_USER, user);
    }

    public boolean deleteGoal(Long uid, String title) {

        Gson gson = new Gson();
        //if (gson.fromJson(getApi(DELETE_GOAL+"/"+uid+"/"+title.replace(" ","_")), Success.class).success)
        //    return true;

        String resjson = getApi(DELETE_GOAL + "/" + uid + "/" + title.replace(" ", "_"));
        return gson.fromJson(resjson, Success.class).success;
    }

}
