/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class MergeSlotDTO extends DataTranferObject {

    public static final String VALUE2 = "value2";

    public static final String VALUE1 = "value1";

    private String type;

    private String name;

    private String value1;

    private String value2;
    
    private String value1Link = "";

    private String value2Link = "";

    public MergeSlotDTO(String name, String type, String value1, String value2) {
        super();
        this.name = name;
        this.type = type;
        this.value1 = value1;
        this.value2 = value2;
    }

    public MergeSlotDTO(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getValue1() {
        return value1;
    }

    public String getValue2() {
        return value2;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public void setValueProperty(String property, String value) {
        if (property.equals(VALUE1)) {
            setValue1(value);
        } else if (property.equals(VALUE2)) {
            setValue2(value);
        }
    }
    
    public void setValueLinkProperty(String property, String link) {
        if (property.equals(VALUE1)) {
            setValue1Link(link);
        } else if (property.equals(VALUE2)) {
            setValue2Link(link);
        }
    }

    public String getValue1Link() {
        return value1Link;
    }

    public void setValue1Link(String value1Link) {
        this.value1Link = value1Link;
    }

    public String getValue2Link() {
        return value2Link;
    }

    public void setValue2Link(String value2Link) {
        this.value2Link = value2Link;
    }

}
