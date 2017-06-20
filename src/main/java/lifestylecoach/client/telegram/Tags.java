package lifestylecoach.client.telegram;

public interface Tags {

    // COMMAND TAGS FOR THE TELEGRAM BOT
    //private static final String TAG_MEASURES = "/measures"; // show tag measures
    String TAG_MEASURES_UPDATE = "/newmeasure";
    String TAG_REPLY_MEASURES_UPDATE1 = "1. Insert measure type";
    String TAG_REPLY_MEASURES_UPDATE2 = "2. Now insert the value for the type";
    String TAG_REPLY_MEASURES_UPDATE3 = "3. Measure saved";
    //private static final String TAG_GOALS = "/goals";
    String TAG_GOALS_UPDATE = "/modify";
    String TAG_REPLY_GOALS_UPDATE1 = "1. Insert goal title";
    String TAG_REPLY_GOALS_UPDATE2 = "2. Now insert the description for the goal ";
    String TAG_REPLY_GOALS_UPDATE3 = "3. Goal saved";
    String TAG_BACK = "/back";

    // COMMAND TAGS FOR THE TELEGRAM BOT
    String TAG_START = "/start";
    String TAG_REGSURNAME = "/regsurname";
    String TAG_REGHEIGHT = "/regheight";
    String TAG_REGWEIGHT = "/regweight";
    String TAG_SEEPROFILE = "/seeprofile";
    String TAG_MEASURES = "/measures";
    String LBL_SHOWMEASURES_WEIGHT = "weight";
    String TAG_SHOWMEASURES_WEIGHT = "/showwheights";
    String LBL_SHOWMEASURES_STEP = "step";
    String TAG_SHOWMEASURES_STEP = "/showsteps";
    String TAG_SHOWGOALS = "/goals";
    String TAG_GETBMI = "/bmi";
    String TAG_ADDMEASURE = "/update";

    String TAG_MEASURETYPE = "/measuretype";
    String TAG_UPDATEMEASURE = "/update";
    String TAG_UPDATEWEIGHT = "/update weight";
    String TAG_UPDATESTEPS = "/update steps";

    String TAG_GOALS = "/goals";
    String TAG_GOALSUPDATE = "/goalsupdate";
    String TAG_GOALTYPE = "/goaltype";
}
