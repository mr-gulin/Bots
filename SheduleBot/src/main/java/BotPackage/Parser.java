/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BotPackage;

import DBPackage.JavaToMySQL;
import java.util.Calendar;
import java.util.Locale;
import org.telegram.telegrambots.api.objects.Message;
import BotPackage.Strings;
import Services.LoggerA;
import XMLtoJPG.ParseXML;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author Андрей Гулин
 */
public class Parser {

    private Bot bot;

    private Message message;

    private int userID;

    private int action = -1;

    private StringBuilder log;

    private JavaToMySQL jvdb;
    private String userName = "";
    private String state = "";
    private boolean flagHelp;

    public Parser(Message message, Bot bot) {
        this.bot = bot;
        this.message = message;
        jvdb = new JavaToMySQL();
        flagHelp = true;
        log = new StringBuilder();

    }

    private void caseAction(int userID, String userName) {
        this.userID = userID;
        this.userName = userName;
        this.state = jvdb.getState(userID);
        log.append("Get message ").append(message.getText()).append(" from ")
                .append(userName).append(", ").append(userID).append("\n");
        authorization();

    }

    private void sendHelp() {
        if (state.equals(Strings.STATE_REGISTER)) {
            bot.sendMsg(message, Strings.HELP_REGISTER, state);
        }
        if (state.equals(Strings.STATE_AUTH)) {
            bot.sendMsg(message, Strings.HELP_AUTH, state);
        }
        if (state.equals(Strings.STATE_BEGIN)) {
            flagHelp = false;
            bot.sendMsg(message, Strings.HELP_BEGIN, state);
        }
        log.append("Sended help ").append(message.getText()).append(" to ")
                .append(userName).append(", ").append(userID).append("\n");

    }

    private void authorization() {
        if (message.getText().equals(Strings.BOT_START)) {
            jvdb.setState(userID, Strings.STATE_BEGIN);
            updateState();
        }
        if (message.getText().equals(Strings.BOT_RESET)) {
            log.append("Bot reset by ").append(userName).append(", ").append(userID)
                    .append("\n");
            jvdb.setState(userID, Strings.STATE_BEGIN);
            updateState();
            bot.sendMsg(message, "Сброшено состояние до начального", state);
        }
        if (message.getText().equals(Strings.BOT_HELP)) {
            sendHelp();
        }
        if (message.getText().equals(Strings.BOT_AUTHORIZATION)) {
            parseBegin();
        }
        
        if (message.getText().contains(Strings.BOT_MESSAGE_TO_ADMIN)) {
            bot.sendMessageToAdmin(message);
            bot.sendMsg(message, Strings.USER_MESSAGE_SENT, state);
        }
        if ((message.getText().equals(Strings.BOT_SEND_LIST)) && (state.equals(Strings.STATE_REGISTER))) {
            jvdb.setState(userID, Strings.STATE_GETLIST);
            updateState();
            bot.sendMsg(message, Strings.USER_CHOOSE_UNIVERSITY, state);
            jvdb.setState(userID, Strings.STATE_REGISTER);
            updateState();
        }

        if (state.equals(Strings.STATE_ADD_SCHEDULE)){
            if (message.getText().equals(Strings.BOT_ADMIN_ADD_CANCEL)){
                jvdb.setState(userID, Strings.STATE_ADMIN);
                updateState();
                bot.sendMsg(message, Strings.ADMIN_ADD_CANCEL, state);
            }
        }
        if (state.equals(Strings.STATE_REGISTER)) {
            registrationGet();
        }
        if (state.equals(Strings.STATE_AUTH)) {
            if ((isUserAdmin(userID)) && (message.getText().equals(Strings.BOT_ADMIN_LOGIN))) { //(userID == 163069540)
                jvdb.setState(userID, Strings.STATE_ADMIN);
                updateState();
                bot.sendMsg(message, Strings.ADMIN_HELLO, state);
            }
            parseAuth();
        }
        if (state.equals(Strings.STATE_ADMIN)) {
            parseAdmin();
            parseAuth();
        }

        if (state.equals(Strings.STATE_BEGIN)) {
            if (flagHelp) {
                bot.sendMsg(message, Strings.USER_HELLO, state);
            }
        }

    }

    private boolean isUserAdmin(int userID) {
        for (int i = 0; i < Strings.ADMINS.size(); i++) {
            if (String.valueOf(userID).equals(Strings.ADMINS.get(i))) {
                return true;
            }
        }
        return false;
    }

    private void parseBegin() {
        if (jvdb.isUserExist(userID)) {
            jvdb.setState(userID, Strings.STATE_AUTH);
            updateState();
            bot.sendMsg(message, Strings.USER_HELLO_AUTH, state);
        } else {
            registrationSend();
        }
    }

    private void parseAuth() {
        if (message.getText().equals(Strings.BOT_SHEDULE)) {
            sendShedule();
        }
    }

    private void parseAdmin() {
        if (message.getText().contains(Strings.BOT_ADMIN_NEW)) {
            jvdb.setState(userID, Strings.STATE_ADD_SCHEDULE);
            updateState();
            bot.sendMsg(message, "Пришли теперь xml", state);
        }
        if (message.getText().equals(Strings.BOT_ADMIN_EXIT)) {
            jvdb.setState(userID, Strings.STATE_AUTH);
            updateState();
            bot.sendMsg(message, Strings.ADMIN_BYE, state);
        }

    }

    private void sendShedule() {
        int univID = jvdb.getUniversityID(userID);
        int groupID = jvdb.getGroupID(userID, univID);
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        int week = getWeek(univID);
        String path = jvdb.getPathJPG(groupID, day, week);
        bot.sendMsg(message, Strings.USER_TODAY_SCHED, state);
        bot.sendPic(message, path);
        log.append("Sended shedule to ").append(userName).append(", ")
                .append(userID).append("\n");
    }

    private int getWeek(int univID) {
        int flag = jvdb.getUniversityFlag(univID);

        if (flag == 2) {
            return getTypeOfWeek(univID);
        } else {
            return 1;
        }
    }

    private int getTypeOfWeek(int univID) {
        int diff = getDifference(univID);
        int mod = diff % 2;
        if (mod == 0) {
            return 2;
        } else {
            return 1;
        }

    }

    private int getDifference(int univID) {

        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(jvdb.getDayOfStart(univID));

        Calendar calendar = Calendar.getInstance();

        // Находим разницу между двумя календарями в милисекундах
        long diff = calendar.getTimeInMillis() - startCalendar.getTimeInMillis();
        // в секундах
        long seconds = diff / 1000;
        // в минутах
        long minutes = seconds / 60;
        // в часах
        long hours = minutes / 60;
        // в днях
        long days = hours / 24;

        double weeks = days / 7;

        int week = (int) weeks + 1;

        return week;
    }

    private void registrationGet() {
        String reg = message.getText();
        String register[] = reg.split(" ");
        String university;
        String group;
        if (register.length == 2) {
            university = register[0];
            group = register[1];
            if (jvdb.isUniversityExist(university)) {
                int univID = jvdb.getUniversityID(university);
                if (jvdb.isGroupExist(group, univID)) {
                    int groupID = jvdb.getGroupID(group, univID);
                    int success = jvdb.registerNewUser(userID, userName, univID, groupID);
                    if (success == 0) {
                        jvdb.setState(userID, Strings.STATE_AUTH);
                        updateState();
                        bot.sendMsg(message, (Strings.USER_SUCC_REG + userName), state);
                        log.append("Registration success, ").append(userName).append(", ")
                                .append(userID).append("\n");
                    } else {
                        jvdb.setState(userID, Strings.STATE_BEGIN);
                        updateState();
                        bot.sendMsg(message, (Strings.USER_FAIL_REG + userName), state);
                        log.append("Registration failed, ").append(userName).append(", ")
                                .append(userID).append("\n");
                    }

                } else {
                    bot.sendMsg(message, Strings.USER_FAIL_REG_GROUP, state);
                    jvdb.setState(userID, Strings.STATE_BEGIN);
                    updateState();
                }
            } else {
                bot.sendMsg(message, Strings.USER_FAIL_REG_UNIV, state);
                jvdb.setState(userID, Strings.STATE_BEGIN);
                updateState();
            }
        } else {
            if (reg.equals(Strings.BOT_RESET)) {
                jvdb.setState(userID, Strings.STATE_BEGIN);
                updateState();
            }
        }
    }

    private void registrationSend() {
        jvdb.setState(userID, Strings.STATE_REGISTER);
        updateState();
        bot.sendMsg(message, Strings.USER_HELLO_NEW, state);
        log.append("Registration request by ").append(userName).append(", ")
                .append(userID).append("\n");
    }

    private void updateState() {
        state = jvdb.getState(userID);
    }

    private boolean parseDoc() {
        String saveTo = "C:/DataBot/src/xml/";
        String pathXML = bot.getDoc(message, saveTo);
        ParseXML parse = new ParseXML(pathXML);
        try {
            parse.doParse();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void doAction(int userID, String userName) {

        if (message.hasText()) {
            caseAction(userID, userName);
        }
        if (message.hasDocument()) {
            this.userID = userID;
            updateState();
            if (jvdb.getState(userID).equals(Strings.STATE_ADD_SCHEDULE)) {
                boolean f = false;
                f = parseDoc();
                if (f) {
                    bot.sendMsg(message, Strings.ADMIN_ADD_SUCCESS, state);
                } else {
                    bot.sendMsg(message, Strings.ADMIN_ADD_FAIL, state);
                }

                jvdb.setState(userID, Strings.STATE_ADMIN);
                updateState();
            }
        }
        LoggerA.log(log);
        jvdb.closeCon();
    }

}
