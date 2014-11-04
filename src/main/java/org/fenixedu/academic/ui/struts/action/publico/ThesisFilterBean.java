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
package org.fenixedu.academic.ui.struts.action.publico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.thesis.ThesisState;

public class ThesisFilterBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Department department;
    private ExecutionYear year;
    private Collection<Degree> options;

    private ThesisState state;

    private Degree degree;

    public ThesisFilterBean() {
        super();

        this.department = null;
        this.year = null;
        this.degree = null;

        this.options = new ArrayList<Degree>();
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Collection<Degree> getDegreeOptions() {
        return this.options;
    }

    public void setDegreeOptions(Collection<Degree> degrees) {
        this.options = degrees;
    }

    public Degree getDegree() {
        return degree;
    }

    public void setDegree(Degree degree) {
        this.degree = degree;
    }

    public ThesisState getState() {
        return state;
    }

    public void setState(ThesisState state) {
        this.state = state;
    }

    public ExecutionYear getYear() {
        return year;
    }

    public void setYear(ExecutionYear year) {
        this.year = year;
    }

}
