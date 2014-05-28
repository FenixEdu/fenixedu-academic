/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
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

    private final Integer state;

    public State(int validation) {
        this.state = new Integer(validation);
    }

    public State(Integer validation) {
        this.state = validation;
    }

    public State(String validation) {
        if (validation.equals(State.ACTIVE_STRING)) {
            this.state = new Integer(State.ACTIVE);
        } else if (validation.equals(State.INACTIVE_STRING)) {
            this.state = new Integer(State.INACTIVE);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
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

    @Override
    public String toString() {
        if (state.intValue() == State.ACTIVE) {
            return State.ACTIVE_STRING;
        }
        if (state.intValue() == State.INACTIVE) {
            return State.INACTIVE_STRING;
        }
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

}
