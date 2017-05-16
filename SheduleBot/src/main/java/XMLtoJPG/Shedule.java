/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package XMLtoJPG;

import java.util.List;
import org.simpleframework.xml.*;

/**
 *
 * @author gulin
 */
@Root(name = "Shedule")
public class Shedule {

    private String university;
    private String group;
    private String dateOfStart;
    private int flag;
    private List<Day> days;

    @Attribute(name = "dateOfStart")
    public String getDateOfStart() {
        return dateOfStart;
    }

    @Attribute(name = "dateOfStart")
    public void setDateOfStart(String dateOfStart) {
        this.dateOfStart = dateOfStart;
    }

    @Attribute(name = "university")
    public String getUniversity() {
        return university;
    }

    @Attribute(name = "university")
    public void setUniversity(String university) {
        this.university = university;
    }

    @Attribute(name = "group")
    public String getGroup() {
        return group;
    }

    @Attribute(name = "group")
    public void setGroup(String group) {
        this.group = group;
    }

    @Attribute(name = "flag")
    public int getFlag() {
        return flag;
    }

    @Attribute(name = "flag")
    public void setFlag(int flag) {
        this.flag = flag;
    }

    @ElementList(inline = true, name = "Day")
    public List<Day> getDays() {
        return days;
    }

    @ElementList(inline = true, name = "Day")
    public void setDays(List<Day> days) {
        this.days = days;
    }

}
