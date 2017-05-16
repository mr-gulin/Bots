/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Services;

import BotPackage.Bot;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

/**
 *
 * @author Андрей Гулин
 */
public class LoggerA {

    public static void log(StringBuilder sb) {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        sb.append(" at ").append(df.format(new Date()))
                .append("\n__________________________\n");
        FileWriter writer = null;
        try {
            writer = new FileWriter("C:\\DataBot\\log\\logactions.txt", true);
            writer.write(sb.toString());
                    
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Bot.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(Bot.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
