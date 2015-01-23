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
