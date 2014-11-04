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
package org.fenixedu.academic.dto.research.result;

import java.io.Serializable;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.dto.commons.ExecutionYearBean;

public class ExecutionYearIntervalBean extends ExecutionYearBean implements Serializable {

    private ExecutionYear finalExecutionYear;

    public ExecutionYearIntervalBean() {
        setFirstExecutionYear(ExecutionYear.readFirstExecutionYear());
        setFinalExecutionYear(ExecutionYear.readLastExecutionYear());
    }

    public ExecutionYearIntervalBean(ExecutionYear firstExecutionYear, ExecutionYear finalExecutionYear) {
        setFirstExecutionYear(firstExecutionYear);
        setFinalExecutionYear(finalExecutionYear);
    }

    public void setFirstExecutionYear(ExecutionYear executionYear) {
        this.setExecutionYear(executionYear);
    }

    public ExecutionYear getFirstExecutionYear() {
        return this.getExecutionYear();
    }

    public void setFinalExecutionYear(ExecutionYear executionYear) {
        this.finalExecutionYear = executionYear;
    }

    public ExecutionYear getFinalExecutionYear() {
        return this.finalExecutionYear;
    }

    public ExecutionYear getFirstNonNullExecutionYear() {
        return getFirstExecutionYear() != null ? getFirstExecutionYear() : ExecutionYear.readFirstExecutionYear();
    }

    public ExecutionYear getFinalNonNullExecutionYear() {
        return getFinalExecutionYear() != null ? getFinalExecutionYear() : ExecutionYear.readLastExecutionYear();
    }
}
