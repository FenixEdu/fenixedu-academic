package Util;

/**
 * @author dcs-rjao 25/Mar/2003
 * @author João Mota 05/Apr/2004
 */
public class StudentType extends FenixUtil {

    public static final int NORMAL = 1;

    public static final int WORKING_STUDENT = 2;

    public static final int FOREIGN_STUDENT = 3;

    public static final int EXTERNAL_STUDENT = 4;

    public static final int OTHER = 5;

    private Integer state;

    /** Creates a new instance of StudentType */
    public StudentType() {
    }

    public StudentType(int state) {
        this.state = new Integer(state);
    }

    public StudentType(Integer state) {
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
        if (o instanceof StudentType) {
            StudentType aux = (StudentType) o;
            return this.state.equals(aux.getState());
        }

        return false;

    }

    public String toString() {

        int value = this.state.intValue();
        String valueS = null;

        switch (value) {
        case NORMAL:
            valueS = "FIRST_SEASON";
            break;
        case WORKING_STUDENT:
            valueS = "WORKING_STUDENT";
            break;
        case FOREIGN_STUDENT:
            valueS = "FOREIGN_STUDENT";
            break;
        case EXTERNAL_STUDENT:
            valueS = "EXTERNAL_STUDENT";
            break;
        case OTHER:
            valueS = "OTHER";
            break;
        default:
            break;
        }

        return "[" + this.getClass().getName() + ": " + valueS + "]";
    }
}