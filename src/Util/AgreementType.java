/*
 * Created on Apr 5, 2004
 *
 */
package Util;

/**
 * @author João Mota
 *  
 */
public class AgreementType extends FenixUtil {

    public static final int NONE = 0;

    public static final int AIR_FORCE_ACADEMY = 1;

    public static final int MILITARY_ACADEMY = 2;

    public static final int NC = 3;

    public static final int ERASMUS = 4;

    public static final int SOCRATES = 5;

    public static final int SOCRATES_ERASMUS = 6;

    public static final int TEMPUS = 7;

    public static final int BILATERAL_AGREEMENT = 8;

    public static final int ALFA2 = 9;

    public static final int UNIFOR = 10;

    private Integer state;

    /** Creates a new instance of AgreementType */
    public AgreementType() {
    }

    public AgreementType(int state) {
        this.state = new Integer(state);
    }

    public AgreementType(Integer state) {
        this.state = state;
    }

    /**
     * Getter for property state.
     * 
     * @return Value of property state.
     */
    public java.lang.Integer getState() {
        return state;
    }

    /**
     * Setter for property state.
     * 
     * @param state
     *            New value of property state.
     */
    public void setState(Integer state) {
        this.state = state;
    }

    public boolean equals(Object o) {
        if (o instanceof AgreementType) {
            AgreementType aux = (AgreementType) o;
            return this.state.equals(aux.getState());
        }

        return false;

    }

    public String toString() {

        int value = this.state.intValue();
        String valueS = null;

        switch (value) {
        case NONE:
            valueS = "NONE";
            break;
        case AIR_FORCE_ACADEMY:
            valueS = "AIR_FORCE_ACADEMY";
            break;
        case MILITARY_ACADEMY:
            valueS = "MILITARY_ACADEMY";
            break;
        case NC:
            valueS = "NC";
            break;
        case ERASMUS:
            valueS = "ERASMUS";
            break;
        case SOCRATES:
            valueS = "SOCRATES";
            break;
        case SOCRATES_ERASMUS:
            valueS = "SOCRATES_ERASMUS";
            break;
        case TEMPUS:
            valueS = "TEMPUS";
            break;
        case BILATERAL_AGREEMENT:
            valueS = "BILATERAL_AGREEMENT";
            break;
        case ALFA2:
            valueS = "ALFA2";
            break;
        case UNIFOR:
            valueS = "UNIFOR";
            break;
        default:
            break;
        }

        return "[" + this.getClass().getName() + ": " + valueS + "]";
    }

}