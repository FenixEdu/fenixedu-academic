/*
 * State.java
 *
 * Created on 08 of February of 2003, 11:09
 */

/**
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */

package net.sourceforge.fenixedu.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

public class State extends FenixUtil {

    public static final int INACTIVE = 0;

    public static final int ACTIVE = 1;

    public static final String INACTIVE_STRING = "Inactiva";

    public static final String ACTIVE_STRING = "Activa";

    private Integer state;

    /** Creates a new instance of State */
    public State() {
    }

    public State(int validation) {
        this.state = new Integer(validation);
    }

    public State(Integer validation) {
        this.state = validation;
    }

    public State(String validation) {
        if (validation.equals(State.ACTIVE_STRING))
            this.state = new Integer(State.ACTIVE);
        if (validation.equals(State.INACTIVE_STRING))
            this.state = new Integer(State.INACTIVE);
    }

    public boolean equals(Object o) {
        if (o instanceof State) {
            State aux = (State) o;
            return this.state.equals(aux.getState());
        }

        return false;

    }

    public List toArrayList() {
        List result = new ArrayList();
        result.add(new LabelValueBean(State.ACTIVE_STRING, State.ACTIVE_STRING));
        result.add(new LabelValueBean(State.INACTIVE_STRING, State.INACTIVE_STRING));
        return result;
    }

    public String toString() {
        if (state.intValue() == State.ACTIVE)
            return State.ACTIVE_STRING;
        if (state.intValue() == State.INACTIVE)
            return State.INACTIVE_STRING;
        return "ERROR!";
    }

    /**
     * Returns the state.
     * 
     * @return Integer
     */
    public Integer getState() {
        return state;
    }

    /**
     * Sets the state.
     * 
     * @param state
     *            The state to set
     */
    public void setState(Integer candidateSituationValidation) {
        this.state = candidateSituationValidation;
    }

}