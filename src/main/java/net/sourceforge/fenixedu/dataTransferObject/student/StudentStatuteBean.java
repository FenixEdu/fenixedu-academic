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
import net.sourceforge.fenixedu.domain.student.SeniorStatute;
import net.sourceforge.fenixedu.domain.student.StudentStatute;
import net.sourceforge.fenixedu.domain.student.StudentStatuteType;
import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.i18n.BundleUtil;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
@SuppressWarnings("serial")
public class StudentStatuteBean implements Serializable {

    private StudentStatuteType statuteType;

    private ExecutionSemester executionSemester;

    private StudentStatute studentStatute;

    public StudentStatuteBean(StudentStatuteType statuteType, ExecutionSemester executionSemester) {
        this.statuteType = statuteType;
        this.executionSemester = executionSemester;
    }

    public StudentStatuteBean(StudentStatuteType statuteType) {
        this.statuteType = statuteType;
    }

    public StudentStatuteBean(StudentStatute studentStatute, ExecutionSemester executionSemester) {
        this.executionSemester = executionSemester;
        this.studentStatute = studentStatute;
    }

    public StudentStatuteBean(StudentStatute studentStatute) {
        this.studentStatute = studentStatute;
    }

    public ExecutionSemester getExecutionPeriod() {
        return executionSemester;
    }

    public StudentStatuteType getStatuteType() {
        return statuteType != null ? statuteType : getStudentStatute().getStatuteType();
    }

    public StudentStatute getStudentStatute() {
        return studentStatute;
    }

    public String getBeginPeriodFormatted() {
        return getStudentStatute() != null && getStudentStatute().hasBeginExecutionPeriod() ? getStudentStatute()
                .getBeginExecutionPeriod().getQualifiedName() : " ... ";
    }

    public String getEndPeriodFormatted() {
        return getStudentStatute() != null && getStudentStatute().hasEndExecutionPeriod() ? getStudentStatute()
                .getEndExecutionPeriod().getQualifiedName() : " ... ";
    }

    public String getDescription() {
        return BundleUtil.getString(Bundle.ENUMERATION, getStatuteType().getDeclaringClass().getSimpleName() + "."
                + getStatuteType().name())
                + (studentStatute instanceof SeniorStatute ? (" ("
                        + ((SeniorStatute) studentStatute).getRegistration().getDegree().getPresentationName() + ") ") : "");
    }

}
