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
package org.fenixedu.academic.ui.struts.action.coordinator;

import java.io.Serializable;

import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.service.services.commons.FactoryExecutor;

public class StudentSearchBean implements FactoryExecutor, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Integer studentNumber;

    private DegreeCurricularPlan degreeCurricularPlan;

    private DegreeCurricularPlan oldDegreeCurricularPlan;

    @Override
    public Object execute() {
        return search();
    }

    public Student search() {
        return Student.readStudentByNumber(getStudentNumber());
    }

    public Integer getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(final Integer studentNumber) {
        this.studentNumber = studentNumber;
    }

    public DegreeCurricularPlan getDegreeCurricularPlan() {
        return degreeCurricularPlan;
    }

    public void setDegreeCurricularPlan(DegreeCurricularPlan degreeCurricularPlan) {
        this.degreeCurricularPlan = degreeCurricularPlan;
    }

    public DegreeCurricularPlan getOldDegreeCurricularPlan() {
        return this.oldDegreeCurricularPlan;
    }

    public void setOldDegreeCurricularPlan(DegreeCurricularPlan oldDegreeCurricularPlan) {
        this.oldDegreeCurricularPlan = oldDegreeCurricularPlan;
    }

}
