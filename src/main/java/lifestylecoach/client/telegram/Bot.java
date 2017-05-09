package lifestylecoach.client.telegram;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

/**
 * Created by matteo on 09/05/17.
 */
public class Bot extends TelegramLongPollingBot {

    private static String BOT_NAME = "lifestylecoach_introsde_bot";
    private static String BOT_TOKEN = "260045306:AAFktJp6e6tubZTqMafPSu4icxi880gNynM";

    public void onUpdateReceived(Update update) {
        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {

            //take important stuffs from the user request
            Long chatId = update.getMessage().getChatId(); // id of the chat
            String reqMessage = update.getMessage().getText(); // message from the user

            // Manage the request, and prepare the response
            SendMessage message = prepareResponse(chatId, reqMessage);

            try {
                sendMessage(message); // Call method to send the message
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private SendMessage prepareResponse(Long chatId, String command) {

        // TODO manage the response

        // prepare the response
        SendMessage response = new SendMessage().setChatId(chatId).setText("TODOTODOTODO");

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
