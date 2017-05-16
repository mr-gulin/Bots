package BotPackage;

import DBPackage.JavaToMySQL;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.AbstractList;
import java.util.Calendar;
import java.util.Date;
import java.util.*;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.exceptions.TelegramApiValidationException;
import org.telegram.telegrambots.generics.BotSession;
import org.telegram.telegrambots.generics.LongPollingBot;
import org.telegram.telegrambots.generics.UpdatesHandler;
import org.telegram.telegrambots.generics.UpdatesReader;
import org.telegram.telegrambots.logging.BotLogger;
import org.telegram.telegrambots.ApiConstants;
import org.telegram.telegrambots.api.methods.send.SendDocument;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.methods.send.SendVoice;
import org.telegram.telegrambots.api.methods.updates.GetUpdates;
import org.telegram.telegrambots.api.objects.Document;
import static org.telegram.telegrambots.api.objects.EntityType.URL;
import org.telegram.telegrambots.api.objects.PhotoSize;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;
import org.json.*;

public class Bot extends TelegramLongPollingBot {

    private int userID;
    private String flag = "begin";

    private String botToken = "361355602:AAFP2pJ7tsyONPJMQGTkOly0xE5c5J_azJ0";

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            Bot bot = new Bot();
            telegramBotsApi.registerBot(bot);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "bestshedulebot";
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        User user = new User();
        user = message.getFrom();
        userID = user.getId();
        String userName = user.getFirstName();

        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

        Parser parser = new Parser(message, this);
        if (message != null) {
            parser.doAction(userID, userName);
        }
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getFlag() {
        return flag;
    }

    private void setKeyboard(SendMessage sendMessage, String state) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboad(false);
        Keyboard keyboard = new Keyboard();
        replyKeyboardMarkup.setKeyboard(keyboard.getKeyboard(userID, state));
    }

    public void sendMsg(Message message, String text, String state) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        setKeyboard(sendMessage, state);
        sendMessage.setChatId(message.getChatId().toString());
        //sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);
        try {
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    public void sendMessageToAdmin(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        //setKeyboard(sendMessage, state);
        sendMessage.setChatId(String.valueOf(163069540));
        //sendMessage.setReplyToMessageId(message.getMessageId());
        String text = message.getText();
        System.err.println(text);
        sendMessage.setText(text);
        try {
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendPic(Message message, String path) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setNewPhoto(new File(path));
        sendPhoto.setChatId(message.getChatId().toString());

        try {
            sendPhoto(sendPhoto);
        } catch (TelegramApiException ex) {
            Logger.getLogger(Bot.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendDoc(Message message, String path) {
        SendDocument sendDocument = new SendDocument();
        sendDocument.setNewDocument(new File(path));
        sendDocument.setChatId(message.getChatId().toString());
        try {
            sendDocument(sendDocument);
        } catch (TelegramApiException ex) {
            Logger.getLogger(Bot.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendAudio(Message message, String path) {
        SendVoice sendVoice = new SendVoice();
        sendVoice.setNewVoice(new File(path));
        sendVoice.setChatId(message.getChatId().toString());
        try {
            sendVoice(sendVoice);
        } catch (TelegramApiException ex) {
            Logger.getLogger(Bot.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void photoParser(Message message) {
        if (message.hasPhoto()) {
            String s;
            s = getPic(message);
            //sendMsg(message, s);
        }
    }

    private void docParser(Message message) {
        if (message.hasDocument()) {
            //getDoc(message);
        }
    }

    private String getPic(Message message) {
        String path = null;
        try {
            List<PhotoSize> ph = new ArrayList<PhotoSize>();
            StringBuilder sb = new StringBuilder();
            ph = message.getPhoto();
            path = ph.get(1).getFilePath();
            sb.append("https://api.telegram.org/photo/").append(botToken).append("/getFile?file_id=").append(ph.get(ph.size() - 1).getFileId());
            //           sb.append("https://api.telegram.org/" + botToken + "/getFile?file_id=" + ph.get(ph.size()-1).getFileId());
            System.out.println(sb.toString());
            //File in = new File(path);
            File out = new File("C:\\hello\\123.png");

            BufferedImage image = ImageIO.read(out);
            String format = "JPEG";
            ImageIO.write(image, format, out);

        } catch (IOException ex) {
            Logger.getLogger(Bot.class.getName()).log(Level.SEVERE, null, ex);
        }
        return path;
    }

    public String getDoc(Message message, String saveTo) {
        String fileName = null;
        String fileID = null;
        StringBuilder jsonGetter = new StringBuilder();
        StringBuilder fileGetter = new StringBuilder();
        String fileXML = "";

        Document doc = message.getDocument();
        fileID = doc.getFileId();

        jsonGetter.append("https://api.telegram.org/bot").append(botToken).append("/getFile?file_id=").append(fileID);
        InputStream stream = null;
        try {
            stream = new URL(jsonGetter.toString()).openStream();
        } catch (MalformedURLException ex) {
            Logger.getLogger(Bot.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Bot.class.getName()).log(Level.SEVERE, null, ex);
        }
        JSONTokener jstk = new JSONTokener(stream);
        JSONObject obj = new JSONObject(jstk);
        fileName = obj.getJSONObject("result").getString("file_path");

        // fileName = obj.getString("file_path");
        System.out.println(obj.toString(1));
        System.out.println(fileName);
        saveTo += fileID + ".xml";

        fileGetter.append("https://api.telegram.org/file/").append("bot" + botToken).append("/").append(fileName);
        URL url = null;

        File f = new File(saveTo);
        try {
            url = new URL(fileGetter.toString());
        } catch (MalformedURLException ex) {
            Logger.getLogger(Bot.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            FileUtils.copyURLToFile(url, f);
        } catch (IOException ex) {
            Logger.getLogger(Bot.class.getName()).log(Level.SEVERE, null, ex);
        }
        return saveTo;
    }

}
