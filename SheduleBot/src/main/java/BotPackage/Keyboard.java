/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BotPackage;

import DBPackage.JavaToMySQL;
import java.util.ArrayList;
import java.util.List;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

/**
 *
 * @author Андрей Гулин
 */
public class Keyboard {

    public List<KeyboardRow> getKeyboard(int userID, String state) {
        List<KeyboardRow> keyboard = null;

        System.out.println(state);
        if (state.equals(Strings.STATE_BEGIN)) {
            keyboard = setBeginLayout();
        }
        if (state.equals(Strings.STATE_AUTH)) {
            keyboard = setAuthLayout();
        }
        if (state.equals(Strings.STATE_REGISTER)) {
            keyboard = setRegisterLayout();
        }
        if (state.equals(Strings.STATE_ADMIN)) {
            keyboard = setRootLayout();
        }
        if (state.equals(Strings.STATE_GETLIST)) {
            keyboard = setListLayout();
        }
        if (state.equals(Strings.STATE_ADD_SCHEDULE)) {
            keyboard = setAddSheduleLayout();
        }
        return keyboard;
    }

    private List<KeyboardRow> setBeginLayout() {
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(Strings.BOT_AUTHORIZATION);
        keyboardFirstRow.add(Strings.BOT_HELP);

        keyboard.add(keyboardFirstRow);

        return keyboard;
    }

    private List<KeyboardRow> setRegisterLayout() {
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardFirstRow = new KeyboardRow();
        KeyboardRow keyboardSecondRow = new KeyboardRow();

        keyboardFirstRow.add(Strings.BOT_HELP);
        keyboardFirstRow.add(Strings.BOT_RESET);

        keyboardSecondRow.add(Strings.BOT_SEND_LIST);

        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);

        return keyboard;
    }

    private List<KeyboardRow> setAuthLayout() {
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardFirstRow = new KeyboardRow();
        KeyboardRow keyboardSecondRow = new KeyboardRow();

        keyboardFirstRow.add(Strings.BOT_SHEDULE);

        keyboardSecondRow.add(Strings.BOT_HELP);
        keyboardSecondRow.add(Strings.BOT_RESET);

        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);

        return keyboard;
    }

    private List<KeyboardRow> setRootLayout() {
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardFirstRow = new KeyboardRow();
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        KeyboardRow keyboardThirdRow = new KeyboardRow();

        keyboardFirstRow.add(Strings.BOT_SHEDULE);

        keyboardSecondRow.add(Strings.BOT_HELP);
        keyboardSecondRow.add(Strings.BOT_RESET);

        keyboardThirdRow.add(Strings.BOT_ADMIN_EXIT);

        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        keyboard.add(keyboardThirdRow);

        return keyboard;
    }

    private List<KeyboardRow> setAddSheduleLayout() {
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardFirstRow = new KeyboardRow();
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        KeyboardRow keyboardThirdRow = new KeyboardRow();
        KeyboardRow keyboardFourthRow = new KeyboardRow();

        keyboardFirstRow.add(Strings.BOT_SHEDULE);

        keyboardSecondRow.add(Strings.BOT_HELP);
        keyboardSecondRow.add(Strings.BOT_RESET);

        keyboardThirdRow.add(Strings.BOT_ADMIN_EXIT);

        keyboardFourthRow.add(Strings.BOT_ADMIN_ADD_CANCEL);

        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        keyboard.add(keyboardThirdRow);
        keyboard.add(keyboardFourthRow);

        return keyboard;
    }

    private List<KeyboardRow> setListLayout() {
        JavaToMySQL jvdb = new JavaToMySQL();
        List<String> list = jvdb.getListOfExist();
        List<KeyboardRow> keyboard = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            KeyboardRow row = new KeyboardRow();
            row.add(list.get(i));
            keyboard.add(row);
        }
        jvdb.closeCon();
        return keyboard;
    }

    private List<KeyboardRow> setSheduleLayout() {
        List<KeyboardRow> keyboard = new ArrayList<>();

        // Первая строчка клавиатуры
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        // Добавляем кнопки в первую строчку клавиатуры
        keyboardFirstRow.add("На сегодня");
        keyboardFirstRow.add("На завтра");

        // Вторая строчка клавиатуры
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        // Добавляем кнопки во вторую строчку клавиатуры
        keyboardSecondRow.add("Полностью расписание");

        KeyboardRow keyboardThirdRow = new KeyboardRow();
        keyboardThirdRow.add("Отмена");
        // Добавляем все строчки клавиатуры в список
        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        keyboard.add(keyboardThirdRow);

        return keyboard;
    }

    private List<KeyboardRow> setSpecialLayout() {
        List<KeyboardRow> keyboard = new ArrayList<>();

        // Вторая строчка клавиатуры
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        // Добавляем кнопки во вторую строчку клавиатуры
        keyboardSecondRow.add("Расписание");

        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add("Карта");

        KeyboardRow keyboardThirdRow = new KeyboardRow();
        keyboardThirdRow.add("Кожевников");
        keyboardThirdRow.add("Кто лох?");

        KeyboardRow keyboardFourthRow = new KeyboardRow();
        keyboardFourthRow.add("Помощь");

        // Добавляем все строчки клавиатуры в список
        keyboard.add(keyboardSecondRow);
        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardThirdRow);
        keyboard.add(keyboardFourthRow);

        return keyboard;
    }

    private List<KeyboardRow> setRegularLayout() {
        List<KeyboardRow> keyboard = new ArrayList<>();
        // Вторая строчка клавиатуры
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        // Добавляем кнопки во вторую строчку клавиатуры
        keyboardSecondRow.add("Расписание");

        KeyboardRow keyboardThirdRow = new KeyboardRow();
        keyboardThirdRow.add("Кожевников");
        keyboardThirdRow.add("Кто лох?");

        KeyboardRow keyboardFourthRow = new KeyboardRow();
        keyboardFourthRow.add("Помощь");

        // Добавляем все строчки клавиатуры в список
        keyboard.add(keyboardSecondRow);
        keyboard.add(keyboardThirdRow);
        keyboard.add(keyboardFourthRow);

        return keyboard;
    }

}
