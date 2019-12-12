/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 5/Abr/2003 by jpvl
 *
 */
package org.fenixedu.academic.util;

import java.io.Serializable;

/**
 * @author jpvl
 */
public class PeriodState implements Serializable {

    public static final PeriodState CLOSED = new PeriodState(PeriodState.CLOSED_CODE);
    public static final PeriodState OPEN = new PeriodState(PeriodState.OPEN_CODE);
    public static final PeriodState NOT_OPEN = new PeriodState(PeriodState.NOT_OPEN_CODE);
    public static final PeriodState CURRENT = new PeriodState(PeriodState.CURRENT_CODE);

    public static final String CLOSED_CODE = "CL";
    public static final String CURRENT_CODE = "C";
    public static final String OPEN_CODE = "O";
    public static final String NOT_OPEN_CODE = "NO";

    private final String stateCode;

    @Deprecated
    public PeriodState(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getStateCode() {
        return stateCode;
    }

    @Deprecated
    public PeriodState(PeriodState executionPeriodState) {
        this.stateCode = executionPeriodState.getStateCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PeriodState) {
            final PeriodState executionPeriodState = (PeriodState) obj;
            return executionPeriodState.getStateCode().equals(stateCode);
        }
        return false;
    }

    @Override
    public String toString() {
        String result = "";

        if (getStateCode().equals(CLOSED_CODE)) {
            result = "CLOSED";
        } else if (getStateCode().equals(CURRENT_CODE)) {
            result = "CURRENT";
        } else if (getStateCode().equals(OPEN_CODE)) {
            result = "OPEN";
        } else if (getStateCode().equals(NOT_OPEN_CODE)) {
            result = "NOT_OPEN";
        }

        return result;
    }

    public static PeriodState valueOf(String code) {
        if (code == null) {
            return null;
        } else if (code.equals(PeriodState.CURRENT_CODE)) {
            return PeriodState.CURRENT;
        } else if (code.equals(PeriodState.OPEN_CODE)) {
            return PeriodState.OPEN;
        } else if (code.equals(PeriodState.NOT_OPEN_CODE)) {
            return PeriodState.NOT_OPEN;
        } else if (code.equals(PeriodState.CLOSED_CODE)) {
            return PeriodState.CLOSED;
        }

        return null;
    }
}