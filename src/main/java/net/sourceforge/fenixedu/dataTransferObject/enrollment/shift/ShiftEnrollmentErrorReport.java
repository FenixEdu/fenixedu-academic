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
 * Created on 13/Fev/2004
 */
package net.sourceforge.fenixedu.dataTransferObject.enrollment.shift;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;

/**
 * @author jmota
 * 
 */
public class ShiftEnrollmentErrorReport extends DataTranferObject {

    private List unExistingShifts;

    private List unAvailableShifts;

    /**
     *  
     */
    public ShiftEnrollmentErrorReport() {
        setUnAvailableShifts(new ArrayList());
        setUnExistingShifts(new ArrayList());
    }

    /**
     * @return Returns the unAvailableShifts.
     */
    public List getUnAvailableShifts() {
        return unAvailableShifts;
    }

    /**
     * @param unAvailableShifts
     *            The unAvailableShifts to set.
     */
    public void setUnAvailableShifts(List unAvailableShifts) {
        this.unAvailableShifts = unAvailableShifts;
    }

    /**
     * @return Returns the unExistingShifts.
     */
    public List getUnExistingShifts() {
        return unExistingShifts;
    }

    /**
     * @param unExistingShifts
     *            The unExistingShifts to set.
     */
    public void setUnExistingShifts(List unExistingShifts) {
        this.unExistingShifts = unExistingShifts;
    }
}