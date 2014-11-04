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
package org.fenixedu.academic.ui.struts.action.pedagogicalCouncil;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.Employee;
import org.fenixedu.academic.domain.Teacher;

import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class TutorSearchBean implements Serializable {
    public static class DepartmentTeachersProvider implements DataProvider {
        @Override
        public Converter getConverter() {
            return new DomainObjectKeyConverter();
        }

        @Override
        public Object provide(Object source, Object current) {
            TutorSearchBean bean = (TutorSearchBean) source;
            Set<Teacher> teachers = new HashSet<Teacher>();
            if (bean.getDepartment() != null) {
                for (Employee employee : Employee.getAllCurrentActiveWorkingEmployees(bean.getDepartment().getDepartmentUnit())) {
                    if (employee.getPerson().getTeacher() != null) {
                        if (!bean.getOnlyTutors() || !employee.getPerson().getTeacher().getTutorshipsSet().isEmpty()) {
                            teachers.add(employee.getPerson().getTeacher());
                        }
                    }
                }
            } else {

                for (Teacher teacher : Bennu.getInstance().getTeachersSet()) {
                    if (!bean.getOnlyTutors() || !teacher.getTutorshipsSet().isEmpty()) {
                        teachers.add(teacher);
                    }
                }

            }

            return teachers;
        }
    }

    private static final long serialVersionUID = 161580136110904806L;

    private boolean searchType;

    private Department department;

    private Teacher teacher;

    private Boolean onlyTutors = Boolean.TRUE;

    /**
     * @return true if search is for department/teacher or false if for degree
     */
    public boolean isSearchType() {
        return searchType;
    }

    public void setSearchType(boolean searchType) {
        this.searchType = searchType;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Boolean getOnlyTutors() {
        return onlyTutors;
    }

    public void setOnlyTutors(Boolean onlyTutors) {
        this.onlyTutors = onlyTutors;
    }

    public void setOnlyTutors(String onlyTutors) {
        this.onlyTutors = Boolean.valueOf(onlyTutors);
    }
}