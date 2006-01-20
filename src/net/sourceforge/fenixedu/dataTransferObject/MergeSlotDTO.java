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

}
