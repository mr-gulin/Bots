/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBPackage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import Services.LoggerA;
import XMLtoJPG.ParseXML;
import java.util.ArrayList;

/**
 *
 * @author Андрей Гулин
 */
public class JavaToMySQL {

    // JDBC URL, username and password of MySQL server
    private static final String url = "jdbc:mysql://localhost:3306/botdb";
    private static final String user = "root";
    private static final String password = "Protoss123";
    private static final String baseDir = "C:/DataBot/";

    // JDBC variables for opening and managing connection
    private static Connection con;
    private static Statement stmt;
    private static ResultSet rs;

    private StringBuilder log;

    public JavaToMySQL() {
        log = new StringBuilder();
    }

    private ResultSet getResult(String query) {

        try {
            // opening database connection to MySQL server
            con = DriverManager.getConnection(url, user, password);

            // getting Statement object to execute query
            stmt = con.createStatement();
            // executing SELECT query
            rs = stmt.executeQuery(query);

            return rs;

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {

        }
        return null;
    }

    private void setNew(String query) {
        try {
            // opening database connection to MySQL server
            con = DriverManager.getConnection(url, user, password);

            // getting Statement object to execute query
            stmt = con.createStatement();
            // executing SELECT query
            stmt.executeUpdate(query);

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {

        }
    }

    public void closeCon() {
        try {
            con.close();
        } catch (SQLException se) {
            /*can't do anything */ }
        try {
            stmt.close();
        } catch (SQLException se) {
            /*can't do anything */ }
        try {
            rs.close();
        } catch (SQLException se) {
            /*can't do anything */ }
    }

    private void mkDir(String name) {
        String path = baseDir + "groups/" + name;
        File folder = new File(path);
        String html = baseDir + "groups/" + name + "/html";
        File htmlF = new File(html);
        String xml = baseDir + "groups/" + name + "/xml";
        File xmlF = new File(xml);
        folder.mkdirs();
        htmlF.mkdirs();
        xmlF.mkdirs();
    }

    public int registerNewUser(int userID, String name, int universityID, int groupID) {
        int ret = -1;
        try {
            String query1 = "select userID from user";
            getResult(query1);
            while (rs.next()) {
                int usID = rs.getInt(1);
                if (usID == userID) {
                    return ret;
                }
            }
            String query = "insert into user values (" + userID + ", '" + name + "', " + groupID + ", " + universityID + ")";
            setNew(query);
            ret = 0;
        } catch (SQLException ex) {
            Logger.getLogger(JavaToMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

  
    public int registerNewGroup(int universityID, String name) {
        int ret = -1;
        String path = "/";
        int groupID = 0;
        try {

            String query = "select g.groupID, g.name, u.universityID from groupp g, university u where (u.universityID=g.university_universityID);";
            getResult(query);
            while (rs.next()) {
                String group = rs.getString(2);
                int univID = rs.getInt(3);
                if (group.matches(name) && (univID == universityID)) {
                    return ret;
                }
                groupID = rs.getInt(1);
            }
            groupID++;
            path = baseDir + "groups/" + String.valueOf(groupID) + "/";
            mkDir(String.valueOf(groupID));
            String query2 = "insert into groupp values (" + groupID + ", '" + name + "', '" + path + "','" + universityID + "')";
            setNew(query2);
            ret = 0;

        } catch (SQLException ex) {
            Logger.getLogger(JavaToMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
        }
        return ret;
    }

    public int registerNewUniversity(String name, String dateOfStart, int flag) {
        int ret = -1;
        int universityID = 0;
        try {

            String query = "select * from university";
            getResult(query);
            while (rs.next()) {
                String university = rs.getString(2);
                if (university.matches(name)) {
                    return ret;
                }
                universityID = rs.getInt(1);
            }
            universityID++;

            String query2 = "insert into university values (" + universityID + ",'" + name + "','" + dateOfStart + "', '" + flag + "');";
            setNew(query2);

            while (rs.next()) {
                universityID = rs.getInt(1);
            }
            ret = 0;
        } catch (SQLException ex) {
            Logger.getLogger(JavaToMySQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
        }

        return ret;
    }

    public String getGroupPath(int groupID) {
        String path = "";
        try {

            String query = "select path from groupp where groupID=" + groupID;
            getResult(query);
            while (rs.next()) {
                path = rs.getString(1);
            }

        } catch (SQLException ex) {
            Logger.getLogger(JavaToMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return path;
    }

    public int getUniversityFlag(int universityID) {
        int flag = -1;
        String query = "select flag from university where universityID=" + universityID;
        getResult(query);
        try {
            while (rs.next()) {
                flag = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(JavaToMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return flag;
    }

    public Date getDayOfStart(int universityID) {
        Date date = null;
        String query = "select dateOfStart from university where universityID=" + universityID;
        getResult(query);
        try {
            while (rs.next()) {
                date = rs.getDate(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(JavaToMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return date;
    }

    public int getGroupID(String name, int universityID) {
        int id = -1;
        String query = "select groupID from groupp where (university_universityID=" + universityID + " AND name='" + name + "')";
        getResult(query);
        try {
            while (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(JavaToMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    public int getUniversityID(String name) {
        int id = -1;
        String query = "select universityID from university where name='" + name + "'";
        getResult(query);
        try {
            while (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(JavaToMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    public int getGroupID(int userID, int universityID) {
        int id = -1;
        String query = "select group_groupID from user where university_universityID=" + universityID + " AND userID=" + userID;
        getResult(query);
        try {
            while (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(JavaToMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    public int getUniversityID(int userID) {
        int id = -1;
        String query = "select university_universityID from user where userID=" + userID;
        getResult(query);
        try {
            while (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(JavaToMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    public List<String> getListOfExist() {
        String query = "select u.name, g.name from university u, groupp g where (u.universityID = g.university_universityID)";
        List<String> res = new ArrayList<>();
        getResult(query);
        try {
            while (rs.next()) {
                res.add(rs.getString(1) + " " + rs.getString(2));
            }
        } catch (SQLException ex) {
            Logger.getLogger(JavaToMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }
    public int getLastAddedGroup(){
        String query = "select groupID from groupp";
        getResult(query);
        int res = 0;
        try {
            while (rs.next()) {
                res = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(JavaToMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }

    public boolean isGroupExist(String name, int universityID) {
        int id = getGroupID(name, universityID);
        return id != -1;
    }

    public boolean isUniversityExist(String name) {
        int id = getUniversityID(name);
        return id != -1;
    }

    public boolean isUserExist(int userID) {
        int id = -1;
        String query = "select userID from user where userID = " + userID;
        getResult(query);
        try {
            while (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(JavaToMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id != -1;
    }

    public String getPathJPG(int GroupID, int day, int week) {
        String pathDay = "";
        if (week == 2) {
            switch (day) {
                case Calendar.MONDAY:
                    pathDay = "8.png";
                    break;
                case Calendar.TUESDAY:
                    pathDay = "9.png";
                    break;
                case Calendar.WEDNESDAY:
                    pathDay = "10.png";
                    break;
                case Calendar.THURSDAY:
                    pathDay = "11.png";
                    break;
                case Calendar.FRIDAY:
                    pathDay = "12.png";
                    break;
                case Calendar.SATURDAY:
                    pathDay = "13.png";
                    break;
                case Calendar.SUNDAY:
                    pathDay = "14.png";
                    break;
            }
        } else {
            switch (day) {
                case Calendar.MONDAY:
                    pathDay = "1.png";
                    break;
                case Calendar.TUESDAY:
                    pathDay = "2.png";
                    break;
                case Calendar.WEDNESDAY:
                    pathDay = "3.png";
                    break;
                case Calendar.THURSDAY:
                    pathDay = "4.png";
                    break;
                case Calendar.FRIDAY:
                    pathDay = "5.png";
                    break;
                case Calendar.SATURDAY:
                    pathDay = "6.png";
                    break;
                case Calendar.SUNDAY:
                    pathDay = "7.png";
                    break;
            }
        }
        String path = getGroupPath(GroupID) + pathDay;
        return path;

    }

    public void setState(int userID, String state) {
        String query = "select stateID from states where StateID = " + userID;
        String query2 = "";
        boolean f = false;
        getResult(query);
        try {
            while (rs.next()) {
                int usrid = rs.getInt(1);
                if (usrid == userID) {
                    query2 = "update states set state = '" + state + "' where stateID=" + userID;
                    System.out.println(query2);
                    f = true;
                }

            }
        } catch (SQLException ex) {
            Logger.getLogger(JavaToMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (!f) {
            query2 = "insert into states values (" + userID + ", '" + state + "')";

        }
        setNew(query2);
    }

    public String getState(int userID) {
        String query = "select state from states where stateID = " + userID;
        String res = "";
        getResult(query);
        try {
            while (rs.next()) {
                res = rs.getString(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(JavaToMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (res.equals("")) {
            setState(userID, "begin");
            res = "begin";
        }
        return res;
    }
}
