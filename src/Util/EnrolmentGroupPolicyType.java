/*
 * Created on 23/Jul/2003
 *
 */
package Util;

/**
 * @author asnr and scpo
 *  
 */
public class EnrolmentGroupPolicyType extends FenixUtil {

    public static final int ATOMIC = 1;

    public static final int INDIVIDUAL = 2;

    private Integer type;

    /** Creates a new instance of EnrolmentPolicyType */
    public EnrolmentGroupPolicyType() {
    }

    public EnrolmentGroupPolicyType(int type) {
        this.type = new Integer(type);
    }

    public EnrolmentGroupPolicyType(Integer type) {
        this.type = type;
    }

    /**
     * Getter for property type.
     * 
     * @return Value of property type.
     *  
     */

    public java.lang.Integer getType() {
        return type;
    }

    /**
     * Setter for property type.
     * 
     * @param type
     *            New value of property type.
     *  
     */

    public void setType(Integer type) {
        this.type = type;
    }

    
    public String getTypeFullName() {

        int value = this.type.intValue();
        String stringValue = null;

        switch (value) {
        case ATOMIC:
            stringValue = "ATOMICA";
            break;
        case INDIVIDUAL:
            stringValue = "INDIVIDUAL";
            break;
        default:
            break;
        }

        return stringValue;
    }
    
    
    
    
    public boolean equals(Object o) {
        if (o instanceof EnrolmentGroupPolicyType) {
            EnrolmentGroupPolicyType aux = (EnrolmentGroupPolicyType) o;
            return this.type.equals(aux.getType());
        }
        return false;

    }

    public String toString() {

        int value = this.type.intValue();
        String stringValue = null;

        switch (value) {
        case ATOMIC:
            stringValue = "ATOMIC";
            break;
        case INDIVIDUAL:
            stringValue = "INDIVIDUAL";
            break;
        default:
            break;
        }

        return "[" + this.getClass().getName() + ": " + stringValue + "]";
    }
}