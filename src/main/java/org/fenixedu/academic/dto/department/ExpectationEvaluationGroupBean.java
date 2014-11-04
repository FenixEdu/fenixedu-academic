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
package net.sourceforge.fenixedu.dataTransferObject.department;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Teacher;

public class ExpectationEvaluationGroupBean implements Serializable {

    private ExecutionYear executionYearReference;

    private Teacher appraiserReference;

    private Teacher evaluatedReference;

    public ExpectationEvaluationGroupBean(Teacher teacher, ExecutionYear executionYear) {
        setAppraiser(teacher);
        setExecutionYear(executionYear);
    }

    public ExecutionYear getExecutionYear() {
        return this.executionYearReference;
    }

    public void setExecutionYear(ExecutionYear executionYear) {
        this.executionYearReference = executionYear;
    }

    public Teacher getAppraiser() {
        return this.appraiserReference;
    }

    public void setAppraiser(Teacher teacher) {
        this.appraiserReference = teacher;
    }

    public Teacher getEvaluated() {
        return this.evaluatedReference;
    }

    public void setEvaluated(Teacher teacher) {
        this.evaluatedReference = teacher;
    }
}
