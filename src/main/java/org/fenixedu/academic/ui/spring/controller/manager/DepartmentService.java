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
package org.fenixedu.academic.ui.spring.controller.manager;

import java.util.Set;

import org.fenixedu.academic.domain.Department;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.groups.DynamicGroup;
import org.springframework.stereotype.Service;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

@Service
public class DepartmentService {

    public static Set<Department> getAllDepartments() {
        return Bennu.getInstance().getDepartmentsSet();
    }

    @Atomic
    public void addDepartment(DepartmentBean bean) {
        createDepartment(bean);
    }

    @Atomic
    public void editDepartment(DepartmentBean bean) {
        Department department = ((Department) FenixFramework.getDomainObject(bean.getExternalId()));
        department.setActive(bean.isActive());
        department.setCode(bean.getCode());
        department.setName(bean.getName());
        department.setRealName(bean.getRealName());
        department.setRealNameEn(bean.getRealNameEn());
    }

    @Atomic
    public void deleteDepartment(DepartmentBean bean) {
        ((Department) FenixFramework.getDomainObject(bean.getExternalId())).delete();
    }

    @Atomic
    private Department createDepartment(DepartmentBean bean) {
        Department department = new Department();
        department.setCompetenceCourseMembersGroup(DynamicGroup.get("managers"));
        department.setActive(bean.isActive());
        department.setCode(bean.getCode());
        department.setName(bean.getName());
        department.setRealName(bean.getRealName());
        department.setRealNameEn(bean.getRealNameEn());
        department.setRootDomainObject(Bennu.getInstance());
        return department;
    }
}
