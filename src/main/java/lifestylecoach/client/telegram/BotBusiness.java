package lifestylecoach.client.telegram;

import com.google.gson.Gson;
import lifestylecoach.client.models.Goal;
import lifestylecoach.client.models.Measure;
import lifestylecoach.client.models.User;
import lifestylecoach.client.rest.ClientProcessCentric;

import java.util.HashMap;

/**
 * Created by matteo on 15/06/17.
 */
public class BotBusiness implements Tags {

    private String serviceUri;
    private String botName;

    public BotBusiness(String serviceUri, String botName) {
        this.serviceUri = serviceUri;
        this.botName = botName;
    }

    public String onStart(User contact) {
        ClientProcessCentric cp = new ClientProcessCentric(this.serviceUri);

        // is the userid already registered?
        if (!cp.userExist(contact.uid))
            return genNotRegisteredResponse(contact);

        return this.genOnStartResponse(contact);
    }

    public String back(User contact) {

        ClientProcessCentric cp = new ClientProcessCentric(this.serviceUri);

        // is the userid already registered?
        if (!cp.userExist(contact.uid))
            return genNotRegisteredResponse(contact);

        return new String("Back to main menù");
    }

    // Save the name and the surname of the user into the db
    public String registrationSurname(User contact, String command) {

        ClientProcessCentric cp = new ClientProcessCentric(this.serviceUri);

        // parse command usually in this form : "/command surname"
        String surname = command.split(" ")[1];

        // generating JSON
        Gson gson = new Gson();
        String userJson = gson.toJson(new User(contact.uid, contact.name, surname));

        // is all good? if not report the error
        if (!cp.newNameSurname(userJson))
            return this.genErrorMessage("registrationSurname");

        return this.genRegHeight(contact);
    }

    public String registrationHeight(User contact, String command) {

        ClientProcessCentric cp = new ClientProcessCentric(this.serviceUri);

        // parse command usually in this form : "/command surname"
        String height = command.split(" ")[1];

        // generating JSON
        Gson gson = new Gson();
        String heightjson = gson.toJson(new Measure(contact.uid, "height", height));

        // is all good? if not report the error
        if (!cp.newHeight(heightjson))
            return this.genErrorMessage("registrationHeight");

        return this.genRegWeight(contact);
    }

    public String registrationWeight(User contact, String command) {

        ClientProcessCentric cp = new ClientProcessCentric(this.serviceUri);

        // parse command usually in this form : "/command surname"
        String weight = command.split(" ")[1];

        // generating JSON
        Gson gson = new Gson();
        String weightjson = gson.toJson(new Measure(contact.uid, "weight", weight));

        // is all good? if not report the error
        if (!cp.newWeight(weightjson))
            return this.genErrorMessage("registrationWeight");

        return this.genRegFinish(contact);
    }

    public String seeProfile(User contact) {

        ClientProcessCentric cp = new ClientProcessCentric(this.serviceUri);

        // is the userid already registered?
        if (!cp.userExist(contact.uid))
            return genNotRegisteredResponse(contact);

        String res = cp.seeProfile(contact.uid);

        if (res.equals(""))
            return this.genErrorMessage("seeProfile");

        return this.genProfile(res);
    }

    public String showMeasures(User contact, String type) {

        ClientProcessCentric cp = new ClientProcessCentric(this.serviceUri);

        // is the userid already registered?
        if (!cp.userExist(contact.uid))
            return genNotRegisteredResponse(contact);

        String res = cp.getMeasures(contact.uid, type);

        if (res.equals(""))
            return this.genErrorMessage("seeProfile");

        return this.genMeasuresList(res);
    }

    public String updateMeasure(User contact, String parameter, String type) {

        ClientProcessCentric cp = new ClientProcessCentric(this.serviceUri);

        // is the userid already registered?
        if (!cp.userExist(contact.uid))
            return genNotRegisteredResponse(contact);

        // generating JSON
        Gson gson = new Gson();
        String measure = gson.toJson(new Measure(contact.uid, type, parameter));

        // is all good? if not report the error
        if (!cp.newMeasure(measure))
            return this.genErrorMessage("updateMeasure");

        return this.genUpdateMeasureSucess(type);
    }

    public boolean updateGoalCheck_newTitle(String title) {
        return !title.contains("\n");
    }

    public boolean updateGoalCheck_newDescription(String description) {
        return !description.contains("\n");
    }

    public boolean updateGoalCheck_newCondition(String condition) {
        System.out.println("?????????????????????");
        System.out.println(condition);

        return condition.equals("<") || condition.equals("<=") || condition.equals(">") || condition.equals(">=");
    }

    public boolean updateGoalCheck_type(String type) {

        return type.toLowerCase().equals("weight") || type.toLowerCase().equals("step");
    }

    public boolean updateGoalCheck_number(String number) {
        try {
            return !Double.valueOf(number).isNaN();
        } catch (java.lang.NumberFormatException e) {
            return false;
        }
    }

    public String updateGoal(User contact, String[] rows) {

        // old title
        // new title
        // description
        // higher or lower
        // Quantity

        ClientProcessCentric cp = new ClientProcessCentric(serviceUri);

        // is the userid already registered?
        if (!cp.userExist(contact.uid))
            return genNotRegisteredResponse(contact);

        String oldTitle = "";
        Goal goal = new Goal();

        oldTitle = rows[0].split(" ")[1];

        goal.title = rows[1].split(":")[1];
        goal.description = rows[2].split(":")[1];
        goal.status = false;
        goal.condition = rows[3].split(":")[1] + " " + rows[4].split(":")[1] + " " + rows[5].split(":")[1];

        Gson gson = new Gson();
        String goalJson = gson.toJson(goal, Goal.class);

        cp.updateGoal(contact.uid, oldTitle, goalJson);

        return genUpdateGoalSuccess(oldTitle);

    }

    private String genUpdateGoalSuccess(String oldTitle) {
        return new String("Goal " + oldTitle + " updated");
    }

    public String getGoals(User contact) {
        ClientProcessCentric cp = new ClientProcessCentric(this.serviceUri);

        String res = cp.getGoals(contact.uid);

        if (res.equals(""))
            return this.genErrorMessage("getGoals");

        return res;

    }

    private String genUpdateMeasureSucess(String type) {
        return new String("Measure of " + type + " updated");
    }

    private String genMeasuresList(String res) {

        // TODO
        return res;

    }

    private String genProfile(String res) {

        // Format the output
        Gson gson = new Gson();
        HashMap<String, String> profile = new HashMap<String, String>();
        profile = (HashMap<String, String>) gson.fromJson(res, profile.getClass());

        String strOut = "Name : " + profile.get("name") +
                "\nSurname : " + profile.get("surname") +
                "\nHeight : " + profile.get("height") +
                "\nActual weight : " + profile.get("weight");

        return strOut;
    }

    private String genRegFinish(User contact) {
        return new String("Good! The registration operation is finished! \n") + genInfoMessage();
    }

    private String genRegWeight(User contact) {
        return new String("Wonderful! Just a bit of patience, this is the last step! Insert your actual weight! " +
                "(write " + TAG_REGWEIGHT + " followed by your weight)");
    }

    public String genInfoMessage() {

        return new String("Bot commands : balblablabsdasjd"); //TODO
    }

    public String genErrorMessage(String where) {
        return new String("Something went wrong...");
    }

    // Responses
    public String genNotRegisteredResponse(User contact) {
        return new String("Hi " + contact.name + "! Welcome to " + this.botName + ". For using our bot, you have" +
                " to insert a couple of informations about yourself! First, what's your surname? (write " + TAG_REGSURNAME +
                " followed by your surname)");
    }

    public String genRegHeight(User contact) {
        return new String("Well done " + contact.name + ", now insert your height! (write " + TAG_REGHEIGHT + " " +
                "followed by your height)");
    }

    private String genOnStartResponse(User contact) {
        return new String("Hi again " + contact.name + "!");
    }

    public String addMeasure(User contact) {
        return new String("Measure types :"); //TODO
    }

    public String genUpdateMeasure(String type) {
        return new String("Insert new " + type + " value:");

    }
}
