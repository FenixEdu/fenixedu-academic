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
package org.fenixedu.academic.ui.struts.action.messaging;

import java.io.Serializable;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.person.RoleType;

public class FindPersonBean implements Serializable {
    private static final long serialVersionUID = -7868952167229025567L;

    private RoleType roleType;
    private DegreeType degreeType;
    private Degree degree;
    private Department department;
    private String name;
    private Boolean viewPhoto;

    public FindPersonBean() {

    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    public DegreeType getDegreeType() {
        if (!RoleType.STUDENT.equals(roleType)) {
            return null;
        }
        return degreeType;
    }

    public void setDegreeType(DegreeType degreeType) {
        this.degreeType = degreeType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getViewPhoto() {
        return viewPhoto;
    }

    public void setViewPhoto(Boolean viewPhoto) {
        this.viewPhoto = viewPhoto;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Degree getDegree() {
        return degree;
    }

    public void setDegree(Degree degree) {
        this.degree = degree;
    }

    public String getDepartmentExternalId() {
        if (department != null && RoleType.TEACHER.equals(roleType)) {
            return department.getExternalId();
        }
        return null;
    }

    public String getDegreeExternalId() {
        if (degree != null && RoleType.STUDENT.equals(roleType)) {
            return degree.getExternalId();
        }
        return null;
    }

}
