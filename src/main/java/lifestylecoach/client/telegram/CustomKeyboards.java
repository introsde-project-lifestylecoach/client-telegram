package lifestylecoach.client.telegram;

import org.telegram.telegrambots.api.objects.replykeyboard.ForceReplyKeyboard;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matteo on 09/05/17.
 */
public class CustomKeyboards {

    // DEFAULT keyboard
    public static ReplyKeyboardMarkup getDefaultKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

        List<KeyboardRow> keyboard = new ArrayList();
        KeyboardRow row = new KeyboardRow();
        row.add("/measures");
        row.add("/goals");
        keyboard.add(row);
        row = new KeyboardRow();
        row.add("/activities");
        keyboard.add(row);

        keyboardMarkup.setKeyboard(keyboard);

        return keyboardMarkup;
    }


    public static ForceReplyKeyboard getForceReply() {
        ForceReplyKeyboard keyboardMarkup = new ForceReplyKeyboard();
        keyboardMarkup.setSelective(true);
        return keyboardMarkup;
    }


    // DEFAULT keyboard
    public static InlineKeyboardMarkup getInlineKeyboard() {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();

       /* List<KeyboardRow> keyboard = new ArrayList();
        KeyboardRow row = new KeyboardRow();
        row.add("/measures");
        row.add("/goals");
        keyboard.add(row);
        row = new KeyboardRow();
        row.add("/activities");
        keyboard.add(row);

        keyboardMarkup.setKeyboard(keyboard);*/

        return keyboardMarkup;
    }


}
