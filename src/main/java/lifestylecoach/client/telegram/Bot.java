package lifestylecoach.client.telegram;

import lifestylecoach.client.models.User;
import org.telegram.telegrambots.api.methods.send.SendMessage;
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
        if (update.hasMessage() && update.getMessage().hasText()) {

            //take important stuffs from the user request
            Long chatId = update.getMessage().getChatId(); // id of the chat
            String reqMessage = update.getMessage().getText(); // message from the user

            // gen my profile
            User contact = new User(update.getMessage().getFrom().getId(),
                    update.getMessage().getFrom().getFirstName(),
                    update.getMessage().getFrom().getLastName());

            String replyMessage = null;

            if (update.getMessage().isReply()) {
                // Selection of the reply
                replyMessage = update.getMessage().getReplyToMessage().getText();
            }

            // Manage the request, and prepare the response
            SendMessage message = prepareResponse(chatId, contact, reqMessage, replyMessage);

            try {
                if (message != null)
                    sendMessage(message); // Call method to send the message
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
            if (!res.equals(botBusiness.genNotRegisteredResponse(contact)))
                response.setReplyMarkup(CustomKeyboards.getDefaultKeyboard());

            response.setText(res);
        }
        //else if(command.equals(TAG_MEASURES)) {


        //}
        else {// TODO measures , goals , adaptor
            response = null; //TODO
        }

        return response;
    }

    // TODO REMOVE THIS METHOD
    /*
    private SendMessage prepareResponse_deprecated(Long chatId, String command, String replyMessage) {

        // TODO manage the response
        SendMessage response;

        if (command.equals(TAG_MEASURES)) {
            System.out.println(command);
            response = new SendMessage().setChatId(chatId);
            response.setReplyMarkup(CustomKeyboards.getNewRowKeyboard(TAG_MEASURES_UPDATE, TAG_BACK));

            // make http request to the process centric service
            ClientProcessCentric clientProcessCentric = new ClientProcessCentric(URI_PROCESS_CENTRIC);
            String res = clientProcessCentric.getMeasures();

            response.setText(res);
        } else if (command.equals(TAG_MEASURES_UPDATE)) {
            System.out.println(command);
            response = new SendMessage().setChatId(chatId);
            response.setReplyMarkup(CustomKeyboards.getForceReply());
            response.setText(TAG_REPLY_MEASURES_UPDATE1);
        } else if (command.equals(TAG_GOALS)) {
            System.out.println(command);
            response = new SendMessage().setChatId(chatId);
            response.setReplyMarkup(CustomKeyboards.getNewRowKeyboard(TAG_GOALS_UPDATE, TAG_BACK));
            response.setText("Here are your goals ..."); // TODO REQUEST TO PROCESS SENTRIC
        } else if (command.equals(TAG_GOALS_UPDATE)) {
            System.out.println(command);
            response = new SendMessage().setChatId(chatId);
            response.setReplyMarkup(CustomKeyboards.getForceReply());
            response.setText(TAG_REPLY_GOALS_UPDATE1);
        }
        // REPLIES
        else if (replyMessage != null) {
            String[] vReplies = replyMessage.split(" : ");

            if (replyMessage.equals(TAG_REPLY_MEASURES_UPDATE1)) {
                System.out.println(command);
                response = new SendMessage().setChatId(chatId);
                response.setReplyMarkup(CustomKeyboards.getForceReply());

                response.setText(TAG_REPLY_MEASURES_UPDATE2 + " : " + command);
            } else if (vReplies[0].equals(TAG_REPLY_MEASURES_UPDATE2)) {
                System.out.println(command);
                response = new SendMessage().setChatId(chatId);
                response.setReplyMarkup(CustomKeyboards.getDefaultKeyboard());

                // TODO marshal parse command
                System.out.println("");
                System.out.println(vReplies[1]);
                System.out.println(command);


                // TODO send request to process centric
                //make post request to process centric in order to create new
                // make http request to the process centric service
                ClientProcessCentric clientProcessCentric = new ClientProcessCentric(URI_PROCESS_CENTRIC);
                boolean res = clientProcessCentric.newMeasure("\"{ \\\"hello\\\" : \\\"TODO show measure\\\" }\"");

                if (res == true)
                    response.setText(TAG_REPLY_MEASURES_UPDATE3);
                else
                    response.setText("Error during the save of the new measure");
            } else if (replyMessage.equals(TAG_REPLY_GOALS_UPDATE1)) {
                System.out.println(command); // TODO Send new keyboard
                response = new SendMessage().setChatId(chatId);
                response.setReplyMarkup(CustomKeyboards.getForceReply());

                response.setText(TAG_REPLY_GOALS_UPDATE2 + " : " + command);

            } else if (vReplies[0].equals(TAG_REPLY_GOALS_UPDATE2)) {
                System.out.println(command);
                response = new SendMessage().setChatId(chatId);
                response.setReplyMarkup(CustomKeyboards.getDefaultKeyboard());

                // TODO SAVE RESPONSE INTO THE DB

                response.setText(TAG_REPLY_GOALS_UPDATE3);
            } else // default response
            {
                System.out.println(command);
                response = new SendMessage().setChatId(chatId);
                response.setReplyMarkup(CustomKeyboards.getDefaultKeyboard());
                response.setText("What? I go back to the main menu ...");
            }
        } else if (command.equals(TAG_BACK)) {
            System.out.println(command);
            response = new SendMessage().setChatId(chatId);
            response.setReplyMarkup(CustomKeyboards.getDefaultKeyboard());
            response.setText("Back to main menu ...");
        } else // default response
        {
            System.out.println(command);
            response = new SendMessage().setChatId(chatId);
            response.setReplyMarkup(CustomKeyboards.getDefaultKeyboard());
            response.setText("What? I go back to the main menu ...");
        }

        return response;
    }
    */

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