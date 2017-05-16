/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BotPackage;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Андрей Гулин
 */
public class Strings {

    public static final String STATE_REGISTER = "register";
    public static final String STATE_BEGIN = "begin";
    public static final String STATE_AUTH = "authorized";
    public static final String STATE_ADMIN = "admin";
    public static final String STATE_GETLIST = "getlist";
    public static final String STATE_ADD_SCHEDULE = "addschedule";
    
    
    public static final String HELP_REGISTER = "Для регистрации вы можете получить список доступных университетов, нажав на кнопку снизу, а также ввести "
            + "университет и группу вручную, в формате 'УНИВЕРСИТЕТ ГРУППА'";
    public static final String HELP_BEGIN = "Значение кнопок:\n "
            + "Авторизация - пройти авторизацию, чтобы получать персонализированное расписание\n"
            + "Сбросить - сбросить состояние до начального\n";
    public static final String HELP_AUTH = "Значения кнопок:\n "
            + "Расписание - получить расписание на сегодня\n"
            + "Помощь - получить help\n"
            + "Сбросить - сбросить состояние до начального\n";
    
    public static final String BOT_RESET = "Выход";
    public static final String BOT_SHEDULE = "Расписание";
    public static final String BOT_HELP = "Помощь";
    public static final String BOT_SEND_LIST = "Список университетов и групп";
    public static final String BOT_AUTHORIZATION = "Авторизация";
    public static final String BOT_ADMIN_EXIT = "Выйти из админки";
    public static final String BOT_ADMIN_NEW = "#new";
    public static final String BOT_ADMIN_LOGIN = "#root";
    public static final String BOT_ADMIN_ADD_CANCEL = "Отмена";
    public static final String BOT_MESSAGE_TO_ADMIN = "#admin";
    public static final String BOT_START = "/start";
    
    
    public static final String USER_HELLO_AUTH = "Ты уже авторизован у нас, так что смело спрашивай "
            + "у меня расписание на сегодня!\n";
    public static final String USER_HELLO_NEW = "Ты не зарегистрирован (-а)."
            + "Для регистрации введи, пожалуйста, название своего университета "
            + " и название своей группы в формате 'Университет Группа'";
    public static final String USER_HELLO = "Привет! Выбери действие снизу!";
    public static final String USER_FAIL_REG_GROUP = "Кажется, вашей группы ещё не существует :с";
    public static final String USER_FAIL_REG_UNIV = "Кажется, вашего университета ещё нет в нашем списке :с";
    public static final String USER_FAIL_REG = "Не удалось :с Попробуйте ещё... ";
    public static final String USER_SUCC_REG = "Добро пожаловать, ";
    public static final String USER_MESSAGE_SENT = "Ваше сообщение отправлено!";
    public static final String USER_CHOOSE_UNIVERSITY = "Выберите группу и университет из списка:";
    public static final String USER_TODAY_SCHED = "Расписание на сегодня:";
    
    
    
    public static final String ADMIN_HELLO = "Здравствуйте, профессор!";
    public static final String ADMIN_BYE = "До свидания, профессор!";
    public static final String ADMIN_ADD_SUCCESS = "Расписание добавлено!)";
    public static final String ADMIN_ADD_FAIL = "Что-то пошло не так :с";
    public static final String ADMIN_ADD_CANCEL = "Отмена добавления";
    
    
    
    
    
   public static final List<String> ADMINS =  Collections.unmodifiableList(Arrays.asList("163069540", "126950238"));
}
