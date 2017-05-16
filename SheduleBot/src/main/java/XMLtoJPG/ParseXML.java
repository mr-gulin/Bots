/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package XMLtoJPG;

import DBPackage.JavaToMySQL;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import XMLtoJPG.Day;
import com.sun.imageio.plugins.jpeg.JPEGImageWriter;
import com.sun.imageio.plugins.png.PNGImageWriter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.Reader;
import java.io.StringReader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.simpleframework.xml.*;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import gui.ava.html.*;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import static java.lang.System.out;
import java.util.Iterator;
import java.util.Locale;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.bmp.BMPImageWriteParam;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.JEditorPane;
import javax.swing.text.BadLocationException;

/**
 *
 * @author gulin
 */
public class ParseXML {

    private String pathXML;
    private String pathToSave;
    private Shedule shedule;
    private JavaToMySQL jvdb;

    public ParseXML(String pathXML) {
        this.pathXML = pathXML;
        jvdb = new JavaToMySQL();
    }

    public void doParse() {
        shedule = fromXmlToObject();
        for (int i = 0; i < shedule.getDays().size(); i++) {
            System.out.println(shedule.getDays().get(i).getName());
        }
        createXLS();
        jvdb.closeCon();
    }

    private Shedule fromXmlToObject() {
        File source;
        source = new File(pathXML);
        Shedule shed = null;
        Persister serializer = new Persister();
        try {
            shed = serializer.read(Shedule.class, source, false);
        } catch (Exception ex) {
            Logger.getLogger(ParseXML.class.getName()).log(Level.SEVERE, null, ex);
        }
        return shed;
    }

    private void createJPG(String html, String name) {

        File file2 = new File(pathToSave + "/" + name + ".png");
        Html2Image imageGenerator = Html2Image.fromHtml(html);

        JEditorPane jep = new JEditorPane("text/html", html);
        jep.setSize(jep.getPreferredSize().width, jep.getPreferredSize().height);
        int height = jep.getSize().height;
        int width = jep.getSize().width;
        BufferedImage image = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice().getDefaultConfiguration()
                .createCompatibleImage(width, height);
        Graphics graphics = image.createGraphics();
        jep.print(graphics);
        
        ImageTypeSpecifier type = ImageTypeSpecifier.createFromRenderedImage(image);
        Iterator iter = ImageIO.getImageWriters(type, "PNG");
        // берем первый попавшийся для JPEG
        ImageWriter writer = (ImageWriter) iter.next();

        ImageOutputStream ios = null;
        try {
            ios = ImageIO.createImageOutputStream(file2);
        } catch (IOException ex) {
            Logger.getLogger(ParseXML.class.getName()).log(Level.SEVERE, null, ex);
        }
        writer.setOutput(ios);

        
        JPEGImageWriteParam iwparam = new JPEGImageWriteParam(Locale.getDefault());
        iwparam.setCompressionMode(JPEGImageWriteParam.MODE_EXPLICIT);
        // Вот здесь выкручиваем качество на максимум!
        iwparam.setCompressionQuality(1.0F);
        IIOImage iioimage = new IIOImage(image, null, null);
        try {
            writer.write(null, iioimage, iwparam);
        } catch (IOException ex) {
            Logger.getLogger(ParseXML.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            ios.flush();
        } catch (IOException ex) {
            Logger.getLogger(ParseXML.class.getName()).log(Level.SEVERE, null, ex);
        }
        writer.dispose();
        try {
            ios.close();
        } catch (IOException ex) {
            Logger.getLogger(ParseXML.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String getName(Day day, int typeOfWeek) {
        String path = null;
        if (typeOfWeek == 2) {
            switch (day.getName()) {
                case "Понедельник":
                    path = "8";
                    break;
                case "Вторник":
                    path = "9";
                    break;
                case "Среда":
                    path = "10";
                    break;
                case "Четверг":
                    path = "11";
                    break;
                case "Пятница":
                    path = "12";
                    break;
                case "Суббота":
                    path = "13";
                    break;
                case "Воскресенье":
                    path = "14";
                    break;
            }
        } else {
            switch (day.getName()) {
                case "Понедельник":
                    path = "1";
                    break;
                case "Вторник":
                    path = "2";
                    break;
                case "Среда":
                    path = "3";
                    break;
                case "Четверг":
                    path = "4";
                    break;
                case "Пятница":
                    path = "5";
                    break;
                case "Суббота":
                    path = "6";
                    break;
                case "Воскресенье":
                    path = "7";
                    break;
            }
        }
        return path;
    }

    private void createXLS() {

        String univName = shedule.getUniversity();
        if (!jvdb.isUniversityExist(univName)) {
            jvdb.registerNewUniversity(univName, shedule.getDateOfStart(), shedule.getFlag());
        }
        String groupName = shedule.getGroup();
        if (!jvdb.isGroupExist(groupName, jvdb.getUniversityID(univName))) {
            jvdb.registerNewGroup(jvdb.getUniversityID(univName), groupName);
        }
        pathToSave = jvdb.getGroupPath(jvdb.getGroupID(groupName, jvdb.getUniversityID(univName)));

        //получаем расписание для понедельника, например, по номеру дня (0)
        for (int j = 0; j < shedule.getDays().size(); j++) {
            StringBuilder sb = new StringBuilder();
            sb.append("<!DOCTYPE html>\n"
                    + "<html>");
//                    + "     <meta charset=\"utf-8\">");

            sb.append("<head>"
                    + "  <meta content=\"width=600px\">"
                    + "  <style type=\"text/css\">"
                    + "     @font-face{"
                    + "         font-family: Arial;"
                    + "         font-style: bold;"
                    + "         src: url(C:/Windows/Fonts/Arial.ttf);"
                    + "     }"
                    + "     body{"
                    + "         display: flex;"
                    + "         background-color: white;"
                    + "         box-sizing: content-box;"
                    + "         align-items: center;"
                    + "         width: 800px;"
                    + "     }"
                    + "     table, th, td {"
                    + "         border: 2px solid black;"
                    + "         border-collapse: collapse;"
                    + "         font-family: Arial;"
                    + "         font-weight: 600;"
                    + "         font-size: 20pt;"
                    + "     }"
                    + "     th, td {"
                    + "         padding: 5px;"
                    + "         text-align: center;"
                    + "     }"
                    + "     th {"
                    + "         text-align: left;\n"
                    + "     }"
                    + "     .theader{\n"
                    + "         text-align: center;\n"
                    + "     }"
                    + "     .tname{"
                    + "         text-align: left !important;"
                    + "     }"
                    + "     table{"
                    + "         table-layout: auto;"
                    + "         text-align: center;"
                    + "         position:relative;"
                    + "         border-spacing: 0px;"
                    + "         width: 100%"
                    + "     }"
                    + "  </style>"
                    + "  <title>Заголовок</title>"
                    + "</head>");

            sb.append("<body>"
                    + " <table>");

            sb.append("<th colspan=\"6\" class=\"theader\">" + shedule.getDays().get(j).getName() + "</th>");

            int number = 1;
            for (int i = 0; i < shedule.getDays().get(j).getPairs().size(); i++) {

                if (number != (shedule.getDays().get(j).getPairs().get(i).getNumber())) {
                    sb.append("<tr>");
                    sb.append("<td>" + number + "</td>");
                    sb.append("<td>" + "" + "</td>");
                    sb.append("<td>" + "" + "</td>");
                    sb.append("<td>" + "" + "</td>");
                    sb.append("<td>" + "" + "</td>");
                    sb.append("<td>" + "" + "</td>");
                    sb.append("</tr>");
                }
                if (shedule.getDays().get(j).getPairs().get(i).getColor() != null) {
                    sb.append("<tr style=\"background-color: "
                            + shedule.getDays().get(j).getPairs().get(i).getColor()
                            + ";\">");
                } else {
                    sb.append("<tr>");
                }
                sb.append("<td>" + String.valueOf(shedule.getDays().get(j).getPairs().get(i).getNumber()) + "</td>");
                sb.append("<td>" + shedule.getDays().get(j).getPairs().get(i).getType() + "</td>");
                sb.append("<td class=\"tname\">" + shedule.getDays().get(j).getPairs().get(i).getName() + "</td>");
//                if (shedule.getDays().get(j).getPairs().get(i).getTimeStart() != null) {
//                    sb.append("<td>" + shedule.getDays().get(j).getPairs().get(i).getTimeStart() + "</td>");
//                } else {
//                    sb.append("<td>" + "-" + "</td>");
//                }
//                if (shedule.getDays().get(j).getPairs().get(i).getTimeEnd() != null) {
//                    sb.append("<td>" + shedule.getDays().get(j).getPairs().get(i).getTimeEnd() + "</td>");
//                } else {
//                    sb.append("<td>" + "-" + "</td>");
//                }
               
                if ((shedule.getDays().get(j).getPairs().get(i).getTimeStart() != null) && (shedule.getDays().get(j).getPairs().get(i).getTimeEnd() != null)) {
                    sb.append("<td>" + shedule.getDays().get(j).getPairs().get(i).getTimeStart() + "  -  " + shedule.getDays().get(j).getPairs().get(i).getTimeEnd() + "</td>");
                } else {
                    sb.append("<td>" + "-" + "</td>");
                }
                sb.append("<td>" + shedule.getDays().get(j).getPairs().get(i).getRoom() + "</td>");
                sb.append("<td>" + shedule.getDays().get(j).getPairs().get(i).getTeacher() + "</td>");
                sb.append("</tr>");

                number = shedule.getDays().get(j).getPairs().get(i).getNumber() + 1;

            }
            sb.append("</table>"
                    + "</body>");
            sb.append("</html>");

            String name = getName(shedule.getDays().get(j), shedule.getDays().get(j).getTypeOfWeek());

            File file = new File(pathToSave + "html/" + name + ".html");
            createJPG(sb.toString(), name);
            FileWriter fr = null;
            BufferedWriter br = null;
            try {
                fr = new FileWriter(file);

            } catch (IOException ex) {
                Logger.getLogger(ParseXML.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
            br = new BufferedWriter(fr);

            try {
                br.write(sb.toString());

            } catch (IOException ex) {
                Logger.getLogger(ParseXML.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
            try {
                br.close();

            } catch (IOException ex) {
                Logger.getLogger(ParseXML.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
            try {
                fr.close();

            } catch (IOException ex) {
                Logger.getLogger(ParseXML.class
                        .getName()).log(Level.SEVERE, null, ex);
            }

        }
        

    }
}
