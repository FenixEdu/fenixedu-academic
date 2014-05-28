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
package net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission;

import java.util.Date;

import net.sourceforge.fenixedu.domain.MarkSheetState;
import net.sourceforge.fenixedu.domain.MarkSheetType;

public class MarkSheetManagementSearchBean extends MarkSheetManagementBaseBean {

    private String teacherId;
    private Date evaluationDate;

    private MarkSheetState markSheetState;
    private MarkSheetType markSheetType;

    public MarkSheetType getMarkSheetType() {
        return markSheetType;
    }

    public void setMarkSheetType(MarkSheetType markSheetType) {
        this.markSheetType = markSheetType;
    }

    public Date getEvaluationDate() {
        return evaluationDate;
    }

    public void setEvaluationDate(Date evaluationDate) {
        this.evaluationDate = evaluationDate;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public MarkSheetState getMarkSheetState() {
        return markSheetState;
    }

    public void setMarkSheetState(MarkSheetState markSheetState) {
        this.markSheetState = markSheetState;
    }
}
