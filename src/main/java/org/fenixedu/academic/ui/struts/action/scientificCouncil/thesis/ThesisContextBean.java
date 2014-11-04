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
package org.fenixedu.academic.ui.struts.action.scientificCouncil.thesis;

import java.io.Serializable;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.ExecutionYear;

public class ThesisContextBean implements Serializable {

    /**
     * Serial version id.
     */
    private static final long serialVersionUID = 1L;

    private Degree degree;
    private ExecutionYear executionYear;

    public ThesisContextBean(Degree degree, ExecutionYear executionYear) {
        setDegree(degree);
        setExecutionYear(executionYear);
    }

    public Degree getDegree() {
        return this.degree;
    }

    public void setDegree(Degree degree) {
        this.degree = degree;
    }

    public ExecutionYear getExecutionYear() {
        return this.executionYear;
    }

    public void setExecutionYear(ExecutionYear executionYear) {
        this.executionYear = executionYear;
    }

}
