package lifestylecoach.client.telegram;

import org.telegram.telegrambots.api.objects.replykeyboard.ForceReplyKeyboard;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
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
        row.add("/seeprofile");
        row.add("/measures");
        keyboard.add(row);
        row = new KeyboardRow();
        row.add("/goals");
        row.add("/adaptor"); // TODO TMP
        keyboard.add(row);

        keyboardMarkup.setKeyboard(keyboard);

        return keyboardMarkup;
    }

    public static ReplyKeyboardMarkup getNewRowKeyboard(String command1, String command2) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

        List<KeyboardRow> keyboard = new ArrayList();
        KeyboardRow row = new KeyboardRow();
        row.add(command1);
        row.add(command2);
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
    public static InlineKeyboardMarkup getInlineKeyboard(String lbl1, String msg1, String lbl2, String msg2) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();

        ArrayList keyboard = new ArrayList();
        ArrayList row = new ArrayList();

        InlineKeyboardButton btn1 = new InlineKeyboardButton();
        btn1.setText(lbl1);
        btn1.setCallbackData(msg1);
        InlineKeyboardButton btn2 = new InlineKeyboardButton();
        btn2.setText(lbl2);
        btn2.setCallbackData(msg2);

        row.add(btn1);
        row.add(btn2);

        keyboard.add(row);

        keyboardMarkup.setKeyboard(keyboard);

        return keyboardMarkup;
    }
}
