package lifestylecoach.client.telegram;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

/**
 * Created by matteo on 09/05/17.
 */
public class Bot extends TelegramLongPollingBot {

    private static final String BOT_NAME = "lifestylecoach_introsde_bot";
    private static final String BOT_TOKEN = "260045306:AAFktJp6e6tubZTqMafPSu4icxi880gNynM";

    // COMMAND TAGS FOR THE TELEGRAM BOT
    private static final String TAG_MEASURES = "/measures"; // show tag measures
    private static final String TAG_MEASURES_UPDATE = "/newmeasure";
    private static final String TAG_REPLY_MEASURES_UPDATE1 = "1. Insert measure type";
    private static final String TAG_REPLY_MEASURES_UPDATE2 = "2. Now insert the value for the type";
    private static final String TAG_REPLY_MEASURES_UPDATE3 = "3. Measure saved";
    private static final String TAG_GOALS = "/goals";
    private static final String TAG_GOALS_UPDATE = "/newgoal";
    private static final String TAG_REPLY_GOALS_UPDATE1 = "1. Insert goal title";
    private static final String TAG_REPLY_GOALS_UPDATE2 = "2. Now insert the description for the goal ";
    private static final String TAG_REPLY_GOALS_UPDATE3 = "3. Goal saved";
    private static final String TAG_BACK = "/back";

    public void onUpdateReceived(Update update) {
        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {

            //take important stuffs from the user request
            Long chatId = update.getMessage().getChatId(); // id of the chat
            String reqMessage = update.getMessage().getText(); // message from the user
            String replyMessage = null;

            if (update.getMessage().isReply()) {
                // Selection of the reply
                replyMessage = update.getMessage().getReplyToMessage().getText();
            }

            // Manage the request, and prepare the response
            SendMessage message = prepareResponse(chatId, reqMessage, replyMessage);

            try {
                sendMessage(message); // Call method to send the message
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private SendMessage prepareResponse(Long chatId, String command, String replyMessage) {

        // TODO manage the response
        SendMessage response;

        if (command.equals(TAG_MEASURES)) {
            System.out.println(command); // TODO Send request to process-centric
            response = new SendMessage().setChatId(chatId);
            response.setReplyMarkup(CustomKeyboards.getNewRowKeyboard(TAG_MEASURES_UPDATE, TAG_BACK));
            response.setText("Here are your measures ..."); // TODO REQUEST TO PROCESS SENTRIC
        } else if (command.equals(TAG_MEASURES_UPDATE)) {
            System.out.println(command);
            response = new SendMessage().setChatId(chatId);
            response.setReplyMarkup(CustomKeyboards.getForceReply());
            response.setText(TAG_REPLY_MEASURES_UPDATE1);
        } else if (command.equals(TAG_GOALS)) {
            System.out.println(command); // TODO Send request to process-centric
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

                // TODO SAVE RESPONSE INTO THE DB

                response.setText(TAG_REPLY_MEASURES_UPDATE3);
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