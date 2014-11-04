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
/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.student;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.StudentStatuteType;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
@SuppressWarnings("serial")
public class ManageStudentStatuteBean implements Serializable {

    private StudentStatuteType statuteType;

    private ExecutionSemester beginExecutionPeriod;

    private ExecutionSemester executionPeriod;

    private ExecutionSemester endExecutionPeriod;

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

    public StudentStatuteType getStatuteType() {
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

    public void setStatuteType(StudentStatuteType statuteType) {
        this.statuteType = statuteType;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setRegistration(Registration registration) {
        this.registration = registration;
    }

}
