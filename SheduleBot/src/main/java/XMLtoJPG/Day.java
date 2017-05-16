/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package XMLtoJPG;

import java.util.List;
import javax.xml.bind.annotation.*;
import org.simpleframework.xml.*;

/**
 *
 * @author gulin
 */
@Root(name="Day")
public class Day {

    private String name;
    private int typeOfWeek;
    private List<Pair> pairs;

    @Attribute(name = "name")
    public String getName() {
        return name;
    }

    @Attribute(name = "name")
    public void setName(String name) {
        this.name = name;
    }

    @Attribute(required=false, name = "week")
    public int getTypeOfWeek() {
        return typeOfWeek;
    }

    @Attribute(required=false, name = "week")
    public void setTypeOfWeek(int typeOfWeek) {
        this.typeOfWeek = typeOfWeek;
    }

    @ElementList(inline = true, name = "Pair")
    public List<Pair> getPairs() {
        return pairs;
    }

    @ElementList(inline = true, name = "Pair")
    public void setPairs(List<Pair> pairs) {
        this.pairs = pairs;
    }

}
