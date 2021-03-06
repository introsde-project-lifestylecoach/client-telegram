package lifestylecoach.client.telegram;

import com.google.gson.Gson;
import lifestylecoach.client.models.Bmi;
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

        String res = "";
        if (type.equals("weight") || type.equals("height") || type.equals("step"))
            res = this.genMeasuresList(cp.getMeasures(contact.uid, type));
        else if (type.equals("waist") || type.equals("hip")) {
            String person = cp.seeProfile(contact.uid);
            // Format the output
            Gson gson = new Gson();
            HashMap<String, String> profile = new HashMap<String, String>();
            profile = (HashMap<String, String>) gson.fromJson(person, profile.getClass());

            res = "Measure:\n\nActual " + type + " : " + profile.get(type);
        }

        if (res.equals(""))
            return this.genErrorMessage("showMeasures");

        return res;
    }

    public String updateMeasure(User contact, String parameter, String type) {

        ClientProcessCentric cp = new ClientProcessCentric(this.serviceUri);

        // is the userid already registered?
        if (!cp.userExist(contact.uid))
            return genNotRegisteredResponse(contact);

        // Replace , with . for avoid errors
        parameter = parameter.replace(",", ".");

        // generating JSON
        Gson gson = new Gson();

        if (type.equals("weight") || type.equals("step") || type.equals("height")) {

            String measure = gson.toJson(new Measure(contact.uid, type, parameter, ""));
            // is all good? if not report the error
            if (!cp.newMeasure(measure))
                return this.genErrorMessage("updateMeasure");
            return this.genUpdateMeasureSucess(type);
        } else {
            String userjson = cp.seeProfile(contact.uid);

            HashMap<String, String> profile = new HashMap<String, String>();
            profile = (HashMap<String, String>) gson.fromJson(userjson, profile.getClass());
            profile.put(type, parameter);

            userjson = gson.toJson(profile, profile.getClass());
            if (!cp.updatePerson(userjson))
                return this.genErrorMessage("updateMeasure");
            return this.genUpdateMeasureSucess(type);
        }
    }

    public boolean updateGoalCheck_newTitle(String title) {
        return !title.contains("\n");
    }

    public boolean updateGoalCheck_newDescription(String description) {
        return !description.contains("\n");
    }

    public boolean updateGoalCheck_newCondition(String condition) {
        return condition.equals("<") || condition.equals("<=") || condition.equals(">") || condition.equals(">=");
    }

    public boolean updateGoalCheck_type(String type) {

        return type.toLowerCase().equals("weight") || type.toLowerCase().equals("step") ||
                type.toLowerCase().equals("height");
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


        Goal goal = new Goal();

        System.out.println("=============");
        System.out.println(rows[0]);
        String[] vOldTitle;
        String oldTitle = "";
        vOldTitle = rows[0].split(" ");
        for (int i = 1; i < vOldTitle.length; i++) {
            oldTitle += vOldTitle[i];
            if (i < vOldTitle.length - 1) {
                oldTitle += " ";
            }
        }

        goal.title = rows[1].split(":")[1];
        goal.description = rows[2].split(":")[1];
        goal.status = false;
        String typeIncrease = "";
        if (rows[4].split(":")[1].equals(">"))
            typeIncrease = "increase";
        else if (rows[4].split(":")[1].equals("<"))
            typeIncrease = "decrease";

        goal.condition = rows[3].split(":")[1] + " " + typeIncrease + " " + rows[5].split(":")[1];

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

    private String genMeasuresList(String json) {

        Gson gson = new Gson();

        Measure[] measures;
        measures = gson.fromJson(json, Measure[].class);

        String res = "Measures : \n\n";

        for (Measure m : measures) {
            res += m.measureType +
                    " | " + m.measureValue +
                    " | " + m.date + "\n";
        }

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
                "\nBirthdate : " + profile.get("birthdate") +
                "\nActual weight : " + profile.get("weight") +
                "\nActual Waist : " + profile.get("waist") +
                "\nActual Hip : " + profile.get("hip");

        return strOut;
    }

    private String genRegFinish(User contact) {
        return new String("Good! The registration operation is finished! \n") + genInfoMessage();
    }

    public String genRegWeight(String command) {
        //return new String("Wonderful! Just a bit of patience, this is the last step! Insert your actual weight! " +
        //        "(write " + TAG_REGWEIGHT + " followed by your weight)");
        return new String(command + "\nYour weight (kg) : ");
    }

    public String genInfoMessage() {

        return new String("Bot commands : \n\n" +
                "/start : starts a new session of the bot\n\n" +
                "/seeprofile : user can see his actual profile\n\n" +
                "/measures : show the measures history of the user, and allows him to modify the mesaures \n\n" +
                "/goals : show the user goals and allows the user to modify and delete them\n\n" +
                "/bmi : user can see advanced statistics based on his BMI\n\n" +
                "/info : shows this message"
        );
    }

    public String genErrorMessage(String where) {
        return new String("Something went wrong...");
    }

    // Responses
    public String genNotRegisteredResponse(User contact) {
        return new String("Hi " + contact.name + "! Welcome to " + this.botName + ". For using our bot, you have" +
                " to insert a couple of informations about yourself! Proceed with the " + TAG_REGISTRATION);
    }

    public String genRegHeight(String command) {
        //return new String("Well done " + contact.name + ", now insert your height! (write " + TAG_REGHEIGHT + " " +
        //        "followed by your height)");
        return new String(command + "\nYour height (cm) :");
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

    public String getBmi(User contact) {

        String res = "";


        ClientProcessCentric cp = new ClientProcessCentric(this.serviceUri);

        // is the userid already registered?
        if (!cp.userExist(contact.uid))
            return genNotRegisteredResponse(contact);

        res = cp.getBmi(contact.uid);

        if (res.equals(""))
            return this.genErrorMessage("getBmi");

        return genBmi(res);
    }

    private String genBmi(String bmijson) {

        Gson gson = new Gson();
        Bmi bmi = gson.fromJson(bmijson, Bmi.class);

        String res = "";

        res += "\nYour bmi value is ";
        res += bmi.bmiValue + " and your ideal weight is ";
        res += bmi.idealWeight + "\n";

        res += "You are ";
        res += bmi.bmiStatus + "\n";

        res += "\nYou have a ";
        res += bmi.bmiRisk + "\n";

        res += "\nYour risk is ";
        res += bmi.whrStatus + "\n";

        res += "\nYou should eat ";
        res += bmi.bmrValue + " calories per day";

        return res; //TODO
    }

    public String genRegBirthDate(String command) {
        return new String(command + "\nBirthdate (YYYY/MM//DD) : ");
    }

    public String genRegSurname() {
        return new String("Surname : ");
    }

    public String genRegSex(String command) {
        return new String(command + "\nSex (m or f) : ");
    }

    public String genRegWaist(String command) {
        return new String(command + "\nYour waist measure (cm) : ");
    }

    public String genRegHip(String command) {
        return new String(command + "\nYour hip measure (cm) : ");
    }

    public String registration(User contact, String command) {

        ClientProcessCentric cp = new ClientProcessCentric(this.serviceUri);

        if (cp.userExist(contact.uid))
            return this.genErrorMessage("registration - user");

        System.out.println(command);

        String[] rows = command.split("\n");

        // row 1 surname
        String surname = rows[1].split(":")[1];
        surname = surname.replace(" ", "");
        // row 2 sex
        String sex = rows[2].split(":")[1];
        sex = sex.replace(" ", "");
        // row 3 birthdate
        String birthdate = rows[3].split(":")[1];
        birthdate = birthdate.replace(" ", "");
        // row 4 height
        String height = rows[4].split(":")[1];
        height = height.replace(" ", "");
        // row 5 weight
        String weight = rows[5].split(":")[1];
        weight = weight.replace(" ", "");
        // row 6 waist
        String waist = rows[6].split(":")[1];
        waist = waist.replace(" ", "");
        // row 7 hip
        String hip = rows[7].split(":")[1];
        hip = hip.replace(" ", "");

        Gson gson = new Gson();

        /* Registration of the new User */
        // generating JSON
        String userJson = gson.toJson(new User(contact.uid, contact.name, surname, sex, birthdate, waist, hip));


        if (!cp.newUserRegistration(userJson))
            return this.genErrorMessage("registration - user");

        /*Add the measures height and weight*/
        // generating JSON
        String heightjson = gson.toJson(new Measure(contact.uid, "height", height, ""));
        String weightjson = gson.toJson(new Measure(contact.uid, "weight", weight, ""));

        // is all good? if not report the error
        if (!cp.newMeasure(heightjson))
            return this.genErrorMessage("registration - height");
        // is all good? if not report the error
        if (!cp.newMeasure(weightjson))
            return this.genErrorMessage("registration - weight");

        return this.genRegFinish(contact);
    }

    public String deleteGoal(User contact, String title) {

        ClientProcessCentric cp = new ClientProcessCentric(this.serviceUri);

        if (!cp.deleteGoal(contact.uid, title))
            genErrorMessage("deleteGoal");
        return genDeleteGoalMessage(title);
    }

    private String genDeleteGoalMessage(String title) {
        return new String("Goal " + title + " successfully removed");
    }
}
