package lifestylecoach.client.telegram;

import com.google.gson.Gson;
import lifestylecoach.client.models.Goal;
import lifestylecoach.client.models.User;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

/**
 * Created by matteo on 09/05/17.
 */
public class Bot extends TelegramLongPollingBot implements Tags {

    private static final String BOT_NAME = "lifestylecoach_introsde_bot";
    private static final String BOT_TOKEN = "260045306:AAFktJp6e6tubZTqMafPSu4icxi880gNynM";

    // Services URI
    private static final String URI_PROCESS_CENTRIC = "http://localhost:5700/lifestylecoach-process-centric";


    public void onUpdateReceived(Update update) {

        // We check if the update has a message and the message has text
        if ((update.hasMessage() && update.getMessage().hasText()) || update.hasCallbackQuery()) {

            //take important stuffs from the user request

            Message message;
            String reqMessage;
            if (update.hasMessage()) {
                message = update.getMessage();
                reqMessage = message.getText(); // message from the user
            } else {
                message = update.getCallbackQuery().getMessage();
                reqMessage = update.getCallbackQuery().getData();
            }


            Long chatId = message.getChatId(); // id of the chat

            // gen my profile
            User contact = new User(message.getFrom().getId(),
                    message.getFrom().getFirstName(),
                    message.getFrom().getLastName());

            String replyMessage = null;

            if (message.isReply()) {
                // Selection of the reply
                replyMessage = message.getReplyToMessage().getText();
            }

            // Manage the request, and prepare the response
            SendMessage response = prepareResponse(chatId, contact, reqMessage, replyMessage);

            try {
                if (response != null)
                    sendMessage(response); // Call method to send the message
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private SendMessage prepareResponse(Long chatId, User contact, String command, String replyMessage) {
        // TODO manage the response
        SendMessage response;
        BotBusiness botBusiness = new BotBusiness(URI_PROCESS_CENTRIC, BOT_NAME);

        // log and instantiate response
        System.out.println(command);

        response = new SendMessage().setChatId(chatId);
        String res = "";

        if (command.equals(TAG_START)) {
            res = botBusiness.onStart(contact);

            // if the user is not registered
            if (!res.equals(botBusiness.genNotRegisteredResponse(contact)))
                response.setReplyMarkup(CustomKeyboards.getDefaultKeyboard());
            // if something go wrong send default keyboard
            if (res.equals(botBusiness.genErrorMessage(TAG_START)))
                response.setReplyMarkup(CustomKeyboards.getDefaultKeyboard());

            response.setText(res);
        }
        //take only the first part of the string, ex. /surname smith -> /surname
        else if (command.split(" ")[0].equals(TAG_REGSURNAME)) {
            res = botBusiness.registrationSurname(contact, command);

            // if something go wrong send default keyboard
            if (res.equals(botBusiness.genErrorMessage(TAG_REGSURNAME)))
                response.setReplyMarkup(CustomKeyboards.getDefaultKeyboard());

            response.setText(res);

        } else if (command.split(" ")[0].equals(TAG_REGHEIGHT)) {
            res = botBusiness.registrationHeight(contact, command);

            // if something go wrong send default keyboard
            if (res.equals(botBusiness.genErrorMessage(TAG_REGHEIGHT)))
                response.setReplyMarkup(CustomKeyboards.getDefaultKeyboard());

            response.setText(res);
        } else if (command.split(" ")[0].equals(TAG_REGWEIGHT)) {
            res = botBusiness.registrationWeight(contact, command);

            // if something go wrong send default keyboard
            if (res.equals(botBusiness.genErrorMessage(TAG_REGWEIGHT)))
                response.setReplyMarkup(CustomKeyboards.getDefaultKeyboard());

            response.setText(res);
        } else if (command.equals(TAG_SEEPROFILE)) {
            res = botBusiness.seeProfile(contact);

            // if the user is not registered
            if (res.equals(botBusiness.genNotRegisteredResponse(contact)))
                response.setReplyMarkup(CustomKeyboards.getDefaultKeyboard());

            response.setText(res);
        } else if (command.equals(TAG_MEASURES)) {
            res = botBusiness.addMeasure(contact);

            // if the user is not registered
            if (res.equals(botBusiness.genNotRegisteredResponse(contact)))
                response.setReplyMarkup(CustomKeyboards.getDefaultKeyboard());
            else
                response.setReplyMarkup(CustomKeyboards.getInlineKeyboard(LBL_SHOWMEASURES_WEIGHT,
                        TAG_SHOWMEASURES_WEIGHT,
                        LBL_SHOWMEASURES_STEP,
                        TAG_SHOWMEASURES_STEP));

            response.setText(res);
        } else if (command.equals(TAG_SHOWMEASURES_WEIGHT)) {
            res = botBusiness.showMeasures(contact, "weight");

            response.setReplyMarkup(CustomKeyboards.getNewRowKeyboard(TAG_UPDATEWEIGHT, TAG_BACK));

            response.setText(res);
        } else if (command.equals(TAG_SHOWMEASURES_STEP)) {
            res = botBusiness.showMeasures(contact, "step");

            response.setReplyMarkup(CustomKeyboards.getNewRowKeyboard(TAG_UPDATESTEPS, TAG_BACK));

            response.setText(res);
        } else if (command.equals(TAG_UPDATEWEIGHT)) {
            //res = botBusiness.updateMeasure(contact, "weight");
            res = botBusiness.genUpdateMeasure("weight");

            response.setReplyMarkup(CustomKeyboards.getForceReply());

            response.setText(res);
        } else if (command.equals(TAG_UPDATESTEPS)) {
            res = botBusiness.genUpdateMeasure("step");

            response.setReplyMarkup(CustomKeyboards.getForceReply());

            response.setText(res);
        } else if (command.equals(TAG_SHOWGOALS)) {
            res = botBusiness.getGoals(contact);

            System.out.println(res);

            Gson gson = new Gson();
            Goal vGoals[];
            vGoals = gson.fromJson(res, Goal[].class);

            //vGoals.add(new Goal("title1", "description1", false));
            //vGoals.add(new Goal("title2", "description2", false));
            //System.out.println(gson.toJson(vGoals,  vGoals.getClass()));

            String[] goals = new String[vGoals.length + 1];
            res = "Goals : \n\n";
            int i;
            for (i = 0; i < vGoals.length; i++) {
                goals[i] = TAG_GOALS_UPDATE + " " + vGoals[i].title;
                res += vGoals[i].formattedText() + "\n";
            }
            goals[i] = TAG_BACK;

            //response.setReplyMarkup(CustomKeyboards.getInlineVerticalKeyboard(goals));
            response.setReplyMarkup(CustomKeyboards.getNewColumnKeyboard(goals));

            response.setText(res);

        } else if (command.equals(TAG_BACK)) {

            res = botBusiness.back(contact);

            // if the user is not registered
            if (!res.equals(botBusiness.genNotRegisteredResponse(contact)))
                response.setReplyMarkup(CustomKeyboards.getDefaultKeyboard());

            response.setReplyMarkup(CustomKeyboards.getDefaultKeyboard());

            response.setText(res);
        } else if (command.split(" ")[0].equals(TAG_GOALS_UPDATE)) {

            try {
                res = TAG_GOALS_UPDATE + " " + command.split(" ")[1];

                String[] rows = command.split("\n");


                //if(rows.length == 1)
                res += "\nNew Title : ";
                //else if(rows.length == 3)
                //   res += "\nNew Description :";
                //else if(rows.length == 5)
                //   res += "Condition (insert < or >)";

                response.setReplyMarkup(CustomKeyboards.getForceReply());
            } catch (Exception e) {

            }
            response.setText(res);

        }
        // REPLIES
        else if (replyMessage != null) {
            //String[] vReplies = replyMessage.split(" : ");

            if (replyMessage.equals(botBusiness.genUpdateMeasure("weight"))) {
                res = botBusiness.updateMeasure(contact, command, "weight");
                response.setReplyMarkup(CustomKeyboards.getDefaultKeyboard());
                // TODO AGGIUNGERE CONTROLLI
                response.setText(res);
            } else if (replyMessage.equals(botBusiness.genUpdateMeasure("step"))) {
                res = botBusiness.updateMeasure(contact, command, "step");
                response.setReplyMarkup(CustomKeyboards.getDefaultKeyboard());
                // TODO AGGIUNGERE CONTROLLI
                response.setText(res);
            } else if (replyMessage.split(" ")[0].equals(TAG_GOALS_UPDATE)) {

                res = replyMessage + command;

                String[] rows = res.split("\n");

                // if(rows.length == 1)
                //   res += "\nNew Title :";
                //else
                if (rows.length == 2)
                    res += "\nNew Description :";
                else if (rows.length == 3)
                    res += "\nType of measure:";
                else if (rows.length == 4)
                    res += "\nCondition (insert < or >):";
                else if (rows.length == 5)
                    res += "\nQuantity:";

                response.setReplyMarkup(CustomKeyboards.getForceReply());

                if (rows.length == 6) {
                    res = botBusiness.updateGoal(contact, rows);
                    response.setReplyMarkup(CustomKeyboards.getDefaultKeyboard());
                }

                response.setText(res);
            }
        } else {// TODO measures , goals , adaptor
            response = null; //TODO

            //response.setReplyMarkup(CustomKeyboards.getDefaultKeyboard());

        }

        return response;
    }

    public String getBotUsername() {
        return BOT_NAME;
    }

    public String getBotToken() {
        return BOT_TOKEN;
    }

    public void onClosing() {
        //TODO
    }
}