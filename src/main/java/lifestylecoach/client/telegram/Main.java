package lifestylecoach.client.telegram;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

/**
 * Created by matteo on 09/05/17.
 * The client Main file of LifeStyleCoach project
 */
public class Main {

    public static void main(String[] args) {

        // Initialize API context
        ApiContextInitializer.init();

        // Instantiate Telegram Bots API
        TelegramBotsApi botsApi = new TelegramBotsApi();

        //botsApi = new TelegramBotsApi("https://127.0.0.1:" + (System.getenv("PORT") != null ? System.getenv("PORT") : "5000"));

        // Register the bot
        try {
            botsApi.registerBot(new Bot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
