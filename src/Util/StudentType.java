package Util;

/**
 * @author dcs-rjao 25/Mar/2003
 */
public class StudentType extends FenixUtil
{

    public static final int NORMAL = 1;

    public static final int WORKING_STUDENT = 2;

    private Integer state;

    /** Creates a new instance of StudentType */
    public StudentType()
    {
    }

    public StudentType(int state)
    {
        this.state = new Integer(state);
    }

    public StudentType(Integer state)
    {
        this.state = state;
    }

    /**
     * Getter for property state.
     * 
     * @return Value of property state.
     */
    public java.lang.Integer getState()
    {
        return state;
    }

    /**
     * Setter for property state.
     * 
     * @param state
     *            New value of property state.
     */
    public void setState(Integer state)
    {
        this.state = state;
    }

    public boolean equals(Object o)
    {
        if (o instanceof StudentType)
        {
            StudentType aux = (StudentType) o;
            return this.state.equals(aux.getState());
        }
        else
        {
            return false;
        }
    }

    public String toString()
    {

        int value = this.state.intValue();
        String valueS = null;

        switch (value)
        {
            case NORMAL:
                valueS = "FIRST_SEASON";
                break;
            case WORKING_STUDENT:
                valueS = "WORKING_STUDENT";
                break;
            default:
                break;
        }

        return "[" + this.getClass().getName() + ": " + valueS + "]";
    }
}
