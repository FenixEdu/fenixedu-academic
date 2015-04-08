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
/**
 * 
 */
package org.fenixedu.academic.dto.student;

import java.io.Serializable;

import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.StatuteType;
import org.fenixedu.academic.domain.student.Student;
import org.joda.time.LocalDate;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
@SuppressWarnings("serial")
public class ManageStudentStatuteBean implements Serializable {

    private StatuteType statuteType;

    private ExecutionSemester beginExecutionPeriod;

    private ExecutionSemester executionPeriod;

    private ExecutionSemester endExecutionPeriod;

    private LocalDate beginDate;

    private LocalDate endDate;

    private Student student;

    private Registration registration;

    public ManageStudentStatuteBean(Student student) {
        super();
        this.student = student;
        this.executionPeriod = ExecutionSemester.readActualExecutionSemester();
    }

    public ExecutionSemester getExecutionPeriod() {
        return executionPeriod;
    }

    public void setExecutionPeriod(ExecutionSemester executionSemester) {
        executionPeriod = executionSemester;
    }

    public ExecutionSemester getBeginExecutionPeriod() {
        return beginExecutionPeriod;
    }

    public ExecutionSemester getEndExecutionPeriod() {
        return endExecutionPeriod;
    }

    public LocalDate getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(LocalDate beginDate) {
        this.beginDate = beginDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public StatuteType getStatuteType() {
        return statuteType;
    }

    public Student getStudent() {
        return student;
    }

    public Registration getRegistration() {
        return registration;
    }

    public void setBeginExecutionPeriod(ExecutionSemester beginExecutionPeriod) {
        this.beginExecutionPeriod = beginExecutionPeriod;
    }

    public void setEndExecutionPeriod(ExecutionSemester endExecutionPeriod) {
        this.endExecutionPeriod = endExecutionPeriod;
    }

    public void setStatuteType(StatuteType statuteType) {
        this.statuteType = statuteType;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setRegistration(Registration registration) {
        this.registration = registration;
    }

}
