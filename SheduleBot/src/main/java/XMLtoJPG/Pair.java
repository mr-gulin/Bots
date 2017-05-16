/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package XMLtoJPG;

import org.simpleframework.xml.*;

/**
 *
 * @author gulin
 */
@Root(name = "Pair")
public class Pair {

    private int number;
    private String name;
    private String type;
    private String teacher;
    private String room;
    private String color;
    private String timeStart;
    private String timeEnd;

    @Attribute(required = false, name = "timeStart")
    public String getTimeStart() {
        return timeStart;
    }

    @Attribute(required = false, name = "timeStart")
    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    @Attribute(required = false, name = "timeEnd")
    public String getTimeEnd() {
        return timeEnd;
    }

    @Attribute(required = false, name = "timeEnd")
    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    @Element(name = "Number")
    public int getNumber() {
        return number;
    }

    @Element(name = "Number")
    public void setNumber(int number) {
        this.number = number;
    }

    @Element(name = "Name")
    public String getName() {
        return name;
    }

    @Element(name = "Name")
    public void setName(String name) {
        this.name = name;
    }

    @Element(name = "Type")
    public String getType() {
        return type;
    }

    @Element(name = "Type")
    public void setType(String type) {
        this.type = type;
    }

    @Element(name = "Teacher")
    public String getTeacher() {
        return teacher;
    }

    @Element(name = "Teacher")
    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    @Element(name = "Room")
    public String getRoom() {
        return room;
    }

    @Element(name = "Room")
    public void setRoom(String room) {
        this.room = room;
    }

    @Element(required = false, name = "Color")
    public String getColor() {
        return color;
    }

    @Element(required = false, name = "Color")
    public void setColor(String color) {
        this.color = color;
    }

}
