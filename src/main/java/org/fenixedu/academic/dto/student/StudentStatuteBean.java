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

import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.student.StatuteType;
import org.fenixedu.academic.domain.student.StudentStatute;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
@SuppressWarnings("serial")
public class StudentStatuteBean implements Serializable {

    private StatuteType statuteType;

    private ExecutionInterval executionInterval;

    private StudentStatute studentStatute;

    public StudentStatuteBean(StatuteType statuteType, ExecutionInterval executionInterval) {
        this.statuteType = statuteType;
        this.executionInterval = executionInterval;
    }

    public StudentStatuteBean(StatuteType statuteType) {
        this.statuteType = statuteType;
    }

    public StudentStatuteBean(StudentStatute studentStatute, ExecutionInterval executionInterval) {
        this.executionInterval = executionInterval;
        this.studentStatute = studentStatute;
    }

    public StudentStatuteBean(StudentStatute studentStatute) {
        this.studentStatute = studentStatute;
    }

    public ExecutionInterval getExecutionPeriod() {
        return executionInterval;
    }

    public StatuteType getStatuteType() {
        return statuteType != null ? statuteType : getStudentStatute().getType();
    }

    public StudentStatute getStudentStatute() {
        return studentStatute;
    }

    public String getBeginPeriodFormatted() {
        return getStudentStatute() != null && getStudentStatute().getBeginExecutionInterval() != null ? getStudentStatute()
                .getBeginExecutionInterval().getQualifiedName() : " ... ";
    }

    public String getEndPeriodFormatted() {
        return getStudentStatute() != null && getStudentStatute().getEndExecutionInterval() != null ? getStudentStatute()
                .getEndExecutionInterval().getQualifiedName() : " ... ";
    }

    public String getDescription() {
        return getStatuteType().getName().getContent();
    }
}
