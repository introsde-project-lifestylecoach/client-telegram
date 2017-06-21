package lifestylecoach.client.models;

/**
 * Created by matteo on 16/06/17.
 */
public class Goal {

    public String title;
    public String description;
    public Boolean status;
    public String condition;

    public Goal() {

    }

    public Goal(String title, String description, Boolean status, String condition) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.condition = condition;
    }

    public String formattedText() {

        String displaiedStatus = "";
        if (status == true)
            displaiedStatus = ":white_check_mark:";
        else
            displaiedStatus = ":x:";
        return this.title + "\n" + this.description + "\n" + displaiedStatus + "\n";
    }

}
