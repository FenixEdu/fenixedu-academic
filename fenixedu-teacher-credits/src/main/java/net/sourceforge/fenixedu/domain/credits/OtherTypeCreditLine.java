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
package org.fenixedu.academic.domain.credits;

import java.util.ArrayList;
import java.util.List;

import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.credits.event.CreditsEvent;

public class OtherTypeCreditLine extends OtherTypeCreditLine_Base {

    public OtherTypeCreditLine() {
        super();
    }

    @Override
    protected CreditsEvent getCreditEventGenerated() {
        return CreditsEvent.OTHER_CREDIT;
    }

    public boolean belongsToExecutionPeriod(ExecutionSemester executionSemester) {
        return this.getExecutionPeriod().equals(executionSemester);
    }

    public void delete() {
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    public static List<OtherTypeCreditLine> readByTeacherAndExecutionPeriod(Teacher teacher, ExecutionSemester executionSemester) {
        List<OtherTypeCreditLine> result = new ArrayList<OtherTypeCreditLine>();

        for (OtherTypeCreditLine otherTypeCreditLine : teacher.getOtherTypeCreditLinesSet()) {
            if (otherTypeCreditLine.getExecutionPeriod().equals(executionSemester)) {
                result.add(otherTypeCreditLine);
            }
        }

        return result;
    }

}
