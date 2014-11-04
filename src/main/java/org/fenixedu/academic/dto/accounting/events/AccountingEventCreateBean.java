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
package org.fenixedu.academic.dto.accounting.events;

import java.io.Serializable;

import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.StudentCurricularPlan;

public class AccountingEventCreateBean implements Serializable {

    static private final long serialVersionUID = 5068902775407522276L;

    private StudentCurricularPlan studentCurricularPlan;

    private ExecutionYear executionYear;

    private ExecutionSemester executionSemester;

    public AccountingEventCreateBean() {
    }

    public AccountingEventCreateBean(final StudentCurricularPlan studentCurricularPlan) {
        setStudentCurricularPlan(studentCurricularPlan);
    }

    public StudentCurricularPlan getStudentCurricularPlan() {
        return this.studentCurricularPlan;
    }

    public void setStudentCurricularPlan(StudentCurricularPlan studentCurricularPlan) {
        this.studentCurricularPlan = studentCurricularPlan;
    }

    public ExecutionYear getExecutionYear() {
        return this.executionYear;
    }

    public void setExecutionYear(ExecutionYear executionYear) {
        this.executionYear = executionYear;
    }

    public ExecutionSemester getExecutionPeriod() {
        return this.executionSemester;
    }

    public void setExecutionPeriod(ExecutionSemester executionSemester) {
        this.executionSemester = executionSemester;
    }

}
