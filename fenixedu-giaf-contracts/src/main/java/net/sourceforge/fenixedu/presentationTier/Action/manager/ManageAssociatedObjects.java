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
package org.fenixedu.academic.ui.struts.action.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.EmptyDegree;
import org.fenixedu.academic.domain.EmptyDegreeCurricularPlan;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.serviceAgreementTemplates.AdministrativeOfficeServiceAgreementTemplate;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOfficeType;
import org.fenixedu.academic.domain.organizationalStructure.AccountabilityTypeEnum;
import org.fenixedu.academic.domain.organizationalStructure.EmployeeContract;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.manager.ManagerApplications.ManagerSystemManagementApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

@StrutsFunctionality(app = ManagerSystemManagementApp.class, path = "manage-associated-objects",
        titleKey = "title.manage.associated.objects")
@Mapping(path = "/manageAssociatedObjects", module = "manager")
@Forwards({ @Forward(name = "show", path = "/manager/listAssociatedObjects.jsp"),
        @Forward(name = "list", path = "/manager/listAssociatedObjects.jsp"),
        @Forward(name = "createDepartment", path = "/manager/createDepartment.jsp"),
        @Forward(name = "createEmptyDegree", path = "/manager/createEmptyDegree.jsp"),
        @Forward(name = "associatePersonUnit", path = "/manager/associatePersonUnit.jsp"),
        @Forward(name = "createAcademicOffice", path = "/manager/createAcademicOffice.jsp") })
public class ManageAssociatedObjects extends FenixDispatchAction {
    public static class AssociatedObjectsBean implements Serializable {
        private boolean active;
        private String code;
        private String name;
        private String realName;
        private String realNameEn;
        private YearMonthDay start;
        private AccountabilityTypeEnum accTypeEnum;
        private AdministrativeOfficeType type;
        private AdministrativeOffice office;
        private List<Unit> units = new ArrayList<>();
        private Unit unit;
        private String username;
        private boolean teacher;
        private final List<AdministrativeOffice> offices = new ArrayList<>();

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getRealNameEn() {
            return realNameEn;
        }

        public void setRealNameEn(String realNameEn) {
            this.realNameEn = realNameEn;
        }

        public AdministrativeOfficeType getType() {
            return type;
        }

        public void setType(AdministrativeOfficeType type) {
            this.type = type;
        }

        public Unit getUnit() {
            return unit;
        }

        public void setUnit(Unit unit) {
            this.unit = unit;
        }

        public List<Unit> getUnits() {
            return units;
        }

        public void setUnits(List<Unit> units) {
            this.units = units;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public YearMonthDay getStart() {
            return start;
        }

        public void setStart(YearMonthDay start) {
            this.start = start;
        }

        public AccountabilityTypeEnum getAccTypeEnum() {
            return accTypeEnum;
        }

        public void setAccTypeEnum(AccountabilityTypeEnum accTypeEnum) {
            this.accTypeEnum = accTypeEnum;
        }

        public boolean isTeacher() {
            return teacher;
        }

        public void setTeacher(boolean teacher) {
            this.teacher = teacher;
        }

        public AdministrativeOffice getOffice() {
            return office;
        }

        public void setOffice(AdministrativeOffice office) {
            this.office = office;
        }

        public List<AdministrativeOffice> getOffices() {
            return offices;
        }

        public void setOffices(Set<AdministrativeOffice> officesSet) {
            this.offices.addAll(officesSet);
        }
    }

    @EntryPoint
    public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Set<Department> departments = Bennu.getInstance().getDepartmentsSet();
        Set<AdministrativeOffice> offices = Bennu.getInstance().getAdministrativeOfficesSet();

        request.setAttribute("departments", departments);
        request.setAttribute("offices", offices);
        request.setAttribute("emptyDegree", EmptyDegree.getInstance());

        return mapping.findForward("list");
    }

    public ActionForward prepareCreateDepartment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("bean", new AssociatedObjectsBean());
        return mapping.findForward("createDepartment");
    }

    public ActionForward createDepartment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        AssociatedObjectsBean bean = getRenderedObject("department");
        createDepartment(bean);
        return list(mapping, form, request, response);
    }

    @Atomic
    private void createDepartment(AssociatedObjectsBean bean) {
        Department department = new Department();
        department.setCompetenceCourseMembersGroup(RoleType.MANAGER.actualGroup());
        department.setActive(bean.isActive());
        department.setCode(bean.getCode());
        department.setName(bean.getName());
        department.setRealName(bean.getRealName());
        department.setName(bean.getName());
        department.setRealNameEn(bean.getRealNameEn());
        department.setRootDomainObject(Bennu.getInstance());
    }

    public ActionForward prepareAcademicOffice(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("bean", new AssociatedObjectsBean());
        return mapping.findForward("createAcademicOffice");
    }

    public ActionForward createAcademicOffice(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        AssociatedObjectsBean bean = getRenderedObject("office");
        createAcademicOffice(bean);
        return list(mapping, form, request, response);
    }

    @Atomic
    private void createAcademicOffice(AssociatedObjectsBean bean) {
        AdministrativeOffice office = new AdministrativeOffice();
        office.setAdministrativeOfficeType(bean.getType());
        new AdministrativeOfficeServiceAgreementTemplate(office);
        office.setRootDomainObject(Bennu.getInstance());
    }

    public ActionForward prepareEmptyDegree(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        AssociatedObjectsBean associatedObjectsBean = new AssociatedObjectsBean();
        associatedObjectsBean.setOffices(Bennu.getInstance().getAdministrativeOfficesSet());
        request.setAttribute("bean", associatedObjectsBean);

        return mapping.findForward("createEmptyDegree");
    }

    public ActionForward createEmptyDegree(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        AssociatedObjectsBean bean = getRenderedObject("admOffice");

        createEmptyDegree(bean);
        return list(mapping, form, request, response);
    }

    @Atomic(mode = TxMode.WRITE)
    private void createEmptyDegree(AssociatedObjectsBean bean) {
        AdministrativeOffice adminOffice = bean.getOffice();

        EmptyDegree emptyDegree = EmptyDegree.getInstance();
        if (emptyDegree == null) {
            EmptyDegree.init();
        }
        EmptyDegree.getInstance().setAdministrativeOffice(adminOffice);

        EmptyDegreeCurricularPlan emptyDCP = EmptyDegreeCurricularPlan.getInstance();
        if (emptyDCP == null) {
            EmptyDegreeCurricularPlan.init();
        }
    }

    public ActionForward prepareAssociatePersonUnit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        AssociatedObjectsBean associatedObjectsBean = new AssociatedObjectsBean();
        associatedObjectsBean.setUnits(Unit.readAllUnits());
        request.setAttribute("bean", associatedObjectsBean);
        return mapping.findForward("associatePersonUnit");
    }

    public ActionForward associatePersonUnit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        AssociatedObjectsBean bean = getRenderedObject("office");

        createAssociationToUnit(bean);

        return list(mapping, form, request, response);
    }

    @Atomic(mode = TxMode.WRITE)
    private void createAssociationToUnit(AssociatedObjectsBean bean) {
        Person person = Person.readPersonByUsername(bean.getUsername());
        EmployeeContract ec =
                new EmployeeContract(person, bean.getStart(), null, bean.getUnit(), bean.getAccTypeEnum(), bean.isTeacher());

        person.getEmployee().getCurrentDepartmentWorkingPlace();
    }
}
