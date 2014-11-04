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
package org.fenixedu.academic.ui.struts.action.academicAdministration.executionCourseManagement;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionForm;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Shift;

import pt.ist.fenixframework.FenixFramework;

public class ExecutionCourseManagementForm extends ActionForm {

    private static final long serialVersionUID = 1L;

    private String[] curricularCoursesToTransferIds;
    private String[] shiftsToTransferIds;

    public ExecutionCourseManagementForm() {
    }

    public String[] getCurricularCoursesToTransferIds() {
        return curricularCoursesToTransferIds;
    }

    public void setCurricularCoursesToTransferIds(String[] curricularCoursesToTransferIds) {
        this.curricularCoursesToTransferIds = curricularCoursesToTransferIds;
    }

    public String[] getShiftsToTransferIds() {
        return shiftsToTransferIds;
    }

    public void setShiftsToTransferIds(String[] shiftsToTransferIds) {
        this.shiftsToTransferIds = shiftsToTransferIds;
    }

    public List<CurricularCourse> readCurricularCoursesToTransfer() {
        List<CurricularCourse> result = new ArrayList<CurricularCourse>();

        for (String id : curricularCoursesToTransferIds) {
            result.add((CurricularCourse) FenixFramework.getDomainObject(id));
        }

        return result;
    }

    public List<Shift> readShifts() {
        List<Shift> result = new ArrayList<Shift>();

        for (String id : shiftsToTransferIds) {
            result.add((Shift) FenixFramework.getDomainObject(id));
        }

        return result;
    }

}
