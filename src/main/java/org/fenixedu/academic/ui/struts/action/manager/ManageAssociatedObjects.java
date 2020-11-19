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
package org.fenixedu.academic.ui.struts.action.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOfficeType;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.AccountabilityType;
import org.fenixedu.academic.domain.organizationalStructure.AccountabilityTypeEnum;
import org.fenixedu.academic.domain.organizationalStructure.PartyType;
import org.fenixedu.academic.domain.organizationalStructure.PartyTypeEnum;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.manager.ManagerApplications.ManagerSystemManagementApp;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.spaces.domain.Space;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = ManagerSystemManagementApp.class, path = "manage-associated-objects",
        titleKey = "title.manage.associated.objects")
@Mapping(path = "/manageAssociatedObjects", module = "manager")
@Forwards({ @Forward(name = "list", path = "/manager/listAssociatedObjects.jsp"),
        @Forward(name = "createDepartment", path = "/manager/createDepartment.jsp"),
        @Forward(name = "createScientificArea", path = "/manager/createScientificArea.jsp"),
        @Forward(name = "editDepartment", path = "/manager/editDepartment.jsp"),
        @Forward(name = "createCompetenceCourseGroup", path = "/manager/createCompetenceCourseGroup.jsp"),
        @Forward(name = "associatePersonUnit", path = "/manager/associatePersonUnit.jsp"),
        @Forward(name = "createAcademicOffice", path = "/manager/createAcademicOffice.jsp"),
        @Forward(name = "createDegreeType", path = "/manager/createDegreeType.jsp") })
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
        private String membersGroupExpression;
        private LocalizedString nameLS;
        private boolean teacher;

        private Space building;
        private List<Space> buildings;

        public Department getDepartment() {
            return department;
        }

        public void setDepartment(Department department) {
            this.department = department;
        }

        private Department department;

        public List<Department> getDepartments() {
            return departments;
        }

        public void setDepartments(List<Department> departments) {
            this.departments = departments;
        }

        private List<Department> departments;

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

        public String getMembersGroupExpression() {
            return membersGroupExpression;
        }

        public void setMembersGroupExpression(String membersGroupExpression) {
            this.membersGroupExpression = membersGroupExpression;
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

        public LocalizedString getNameLS() {
            return nameLS;
        }

        public void setNameLS(LocalizedString nameLS) {
            this.nameLS = nameLS;
        }

        public Space getBuilding() {
            return building;
        }

        public void setBuilding(Space building) {
            this.building = building;
        }

        public List<Space> getBuildings() {
            return buildings;
        }

        public void setBuildings(List<Space> buildings) {
            this.buildings = buildings;
        }

        public Unit getScientificAreaUnit() {
            return scientificAreaUnit;
        }

        public void setScientificAreaUnit(Unit scientificAreaUnit) {
            this.scientificAreaUnit = scientificAreaUnit;
        }

        public Unit scientificAreaUnit;
    }

    public static class DegreeTypeBean implements Serializable {

        private static final long serialVersionUID = 387599179531038042L;

        private LocalizedString name;
        private DegreeType selected;
        private boolean empty;
        private boolean degree;
        private boolean masterDegree;
        private boolean dea;
        private boolean dfa;
        private List<CycleType> cycleTypes;
        private List<CycleType> cyclesToEnrol;

        public DegreeTypeBean() {
        }

        public DegreeTypeBean(DegreeType type) {
            this.selected = type;
            this.name = type.getName();
            this.empty = type.getEmpty();
            this.degree = type.getDegreeType();
            this.masterDegree = type.getMasterDegree();
            this.dea = type.getDea();
            this.dfa = type.getDfa();
            this.cycleTypes = new ArrayList<>(type.getCycleTypes());
            this.cyclesToEnrol = new ArrayList<>(type.getSupportedCyclesToEnrol());
        }

        public LocalizedString getName() {
            return name;
        }

        public void setName(LocalizedString name) {
            this.name = name;
        }

        public DegreeType getSelected() {
            return selected;
        }

        public void setSelected(DegreeType selected) {
            this.selected = selected;
        }

        public boolean isEmpty() {
            return empty;
        }

        public void setEmpty(boolean empty) {
            this.empty = empty;
        }

        public boolean isDegree() {
            return degree;
        }

        public void setDegree(boolean degree) {
            this.degree = degree;
        }

        public boolean isMasterDegree() {
            return masterDegree;
        }

        public void setMasterDegree(boolean masterDegree) {
            this.masterDegree = masterDegree;
        }

        public boolean isDea() {
            return dea;
        }

        public void setDea(boolean dea) {
            this.dea = dea;
        }

        public boolean isDfa() {
            return dfa;
        }

        public void setDfa(boolean dfa) {
            this.dfa = dfa;
        }

        public List<CycleType> getCycleTypes() {
            return cycleTypes;
        }

        public void setCycleTypes(List<CycleType> cycleTypes) {
            this.cycleTypes = cycleTypes;
        }

        public List<CycleType> getCyclesToEnrol() {
            return cyclesToEnrol;
        }

        public void setCyclesToEnrol(List<CycleType> cyclesToEnrol) {
            this.cyclesToEnrol = cyclesToEnrol;
        }

        public Collection<CycleType> getPossibleCycleTypes() {
            return Arrays.asList(CycleType.values());
        }

    }

    @EntryPoint
    public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Set<Department> departments = Bennu.getInstance().getDepartmentsSet();
        Set<AdministrativeOffice> offices = Bennu.getInstance().getAdministrativeOfficesSet();

        request.setAttribute("departments", departments);
        request.setAttribute("offices", offices);
        request.setAttribute("degreeTypes", Bennu.getInstance().getDegreeTypeSet());

        return mapping.findForward("list");
    }

    public ActionForward prepareCreateDegreeType(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DegreeType currentType = getDomainObject(request, "degreeTypeId");
        request.setAttribute("bean", currentType == null ? new DegreeTypeBean() : new DegreeTypeBean(currentType));
        return mapping.findForward("createDegreeType");
    }

    public ActionForward createDegreeType(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DegreeTypeBean bean = getRenderedObject("bean");
        if (bean != null) {
            atomic(() -> {
                DegreeType type = bean.getSelected() == null ? new DegreeType(bean.getName()) : bean.getSelected();
                type.setName(bean.getName());
                type.setEmpty(bean.isEmpty());
                type.setDegreeType(bean.isDegree());
                type.setMasterDegree(bean.isMasterDegree());
                type.setDea(bean.isDea());
                type.setDfa(bean.isDfa());
                type.setCycleTypes(bean.getCycleTypes());
                type.setCycleTypesToEnrol(bean.getCyclesToEnrol());
            });
        }
        return list(mapping, form, request, response);
    }

    public ActionForward prepareCreateDepartment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("bean", new AssociatedObjectsBean());
        return mapping.findForward("createDepartment");
    }

    public ActionForward createDepartment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        AssociatedObjectsBean bean = getRenderedObject("department");
        try {
            createDepartment(bean);
        } catch (final DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
            request.setAttribute("bean", bean);
            return mapping.findForward("createDepartment");
        }
        return list(mapping, form, request, response);
    }

    @Atomic
    private void createDepartment(AssociatedObjectsBean bean) {
        Department department = new Department();
        department.setCompetenceCourseMembersGroup(Group.managers());
        department.setActive(bean.isActive());
        department.setCode(bean.getCode());
        department.setName(bean.getName());
        department.setRealName(bean.getRealName());
        department.setRealNameEn(bean.getRealNameEn());
        department.setRootDomainObject(Bennu.getInstance());
        Unit departmentParent = Bennu.getInstance().getInstitutionUnit().getSubUnits().stream().filter(x -> x.isAggregateUnit())
                .filter(x -> x.getName().equals("Departments")).findAny().orElse(Bennu.getInstance().getInstitutionUnit());
        Unit departmentUnit = Unit.createNewUnit(PartyType.of(PartyTypeEnum.DEPARTMENT), department.getNameI18n(),
                department.getCode(), departmentParent, AccountabilityType.readByType(AccountabilityTypeEnum.ACADEMIC_STRUCTURE));
        departmentUnit.setDepartment(department);
    }

    public ActionForward prepareAcademicOffice(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        AssociatedObjectsBean bean = new AssociatedObjectsBean();
        bean.setBuildings(Space.getAllSpaces().sorted().collect(Collectors.toList()));

        request.setAttribute("bean", bean);
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

        office.setName(bean.getNameLS());
        office.setCoordinator(User.findByUsername(bean.getUsername()));
        office.setRootDomainObject(Bennu.getInstance());
        Unit servicesParent = Bennu.getInstance().getInstitutionUnit().getSubUnits().stream()
                .filter(x -> x.getName().equals("Services")).findAny().orElse(Bennu.getInstance().getInstitutionUnit());
        Unit.createNewUnit(PartyType.of(PartyTypeEnum.ADMINISTRATIVE_OFFICE_UNIT), office.getName(), null, servicesParent,
                AccountabilityType.readByType(AccountabilityTypeEnum.ADMINISTRATIVE_STRUCTURE));
    }

    public ActionForward prepareAssociatePersonUnit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        AssociatedObjectsBean associatedObjectsBean = new AssociatedObjectsBean();
        associatedObjectsBean.setUnits(Unit.readAllUnits());
        request.setAttribute("bean", associatedObjectsBean);
        return mapping.findForward("associatePersonUnit");
    }

//
//    public ActionForward associatePersonUnit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
//            HttpServletResponse response) throws Exception {
//        AssociatedObjectsBean bean = getRenderedObject("office");
//
//        createAssociationToUnit(bean);
//
//        return list(mapping, form, request, response);
//    }
//
//    @Atomic(mode = TxMode.WRITE)
//    private void createAssociationToUnit(AssociatedObjectsBean bean) {
//        Person person = Person.readPersonByUsername(bean.getUsername());
//        EmployeeContract ec =
//                new EmployeeContract(person, bean.getStart(), null, bean.getUnit(), bean.getAccTypeEnum(), bean.isTeacher());
//
//        person.getEmployee().getCurrentDepartmentWorkingPlace();
//    }

    public ActionForward prepareCreateScientificArea(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        AssociatedObjectsBean associatedObjectsBean = new AssociatedObjectsBean();
        request.setAttribute("bean", associatedObjectsBean);
        associatedObjectsBean.setDepartments(Department.readActiveDepartments().stream()
                .filter(x -> x.getDepartmentUnit() != null).collect(Collectors.toList()));

        return mapping.findForward("createScientificArea");
    }

    public ActionForward createScientificArea(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        AssociatedObjectsBean bean = getRenderedObject("admOffice");

        try {
            createScientificArea(bean);
        } catch (final DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
            request.setAttribute("bean", bean);

            return mapping.findForward("createScientificArea");
        }

        return list(mapping, form, request, response);
    }

    @Atomic(mode = TxMode.WRITE)
    private void createScientificArea(AssociatedObjectsBean bean) {
        Unit.createNewUnit(PartyType.of(PartyTypeEnum.SCIENTIFIC_AREA), bean.getNameLS(), bean.getCode(),
                bean.getDepartment().getDepartmentUnit(),
                AccountabilityType.readByType(AccountabilityTypeEnum.ORGANIZATIONAL_STRUCTURE));
    }

    public ActionForward prepareCreateCompetenceCourseGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        AssociatedObjectsBean associatedObjectsBean = new AssociatedObjectsBean();
        associatedObjectsBean.setScientificAreaUnit(FenixFramework.getDomainObject(request.getParameter("oid")));
        request.setAttribute("bean", associatedObjectsBean);

        return mapping.findForward("createCompetenceCourseGroup");
    }

    public ActionForward createCompetenceCourseGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        AssociatedObjectsBean bean = getRenderedObject("admOffice");

        try {
            createCompetenceCourseGroup(bean);
        } catch (final DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
            request.setAttribute("bean", bean);
            return mapping.findForward("createCompetenceCourseGroup");
        }

        return list(mapping, form, request, response);
    }

    @Atomic(mode = TxMode.WRITE)
    private void createCompetenceCourseGroup(AssociatedObjectsBean bean) {
        Unit.createNewUnit(PartyType.of(PartyTypeEnum.COMPETENCE_COURSE_GROUP), bean.getNameLS(), bean.getCode(),
                bean.getScientificAreaUnit(), AccountabilityType.readByType(AccountabilityTypeEnum.ORGANIZATIONAL_STRUCTURE));
    }

    public ActionForward prepareEditDepartment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        AssociatedObjectsBean associatedObjectsBean = new AssociatedObjectsBean();
        Department d = FenixFramework.getDomainObject(request.getParameter("oid"));
        associatedObjectsBean.setDepartment(d);
        associatedObjectsBean.setCode(d.getCode());
        associatedObjectsBean.setName(d.getName());
        associatedObjectsBean.setRealName(d.getRealName());
        associatedObjectsBean.setRealNameEn(d.getRealNameEn());
        associatedObjectsBean.setMembersGroupExpression(d.getCompetenceCourseMembersGroup().getExpression());

        request.setAttribute("bean", associatedObjectsBean);

        return mapping.findForward("editDepartment");
    }

    public ActionForward editDepartment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        AssociatedObjectsBean bean = getRenderedObject();

        atomic(() -> {
            Department d = bean.getDepartment();
            d.setCode(bean.getCode());
            d.setName(bean.getName());
            d.setRealName(bean.getRealName());
            d.setRealNameEn(bean.getRealNameEn());
            d.setCompetenceCourseMembersGroup(Group.parse(bean.getMembersGroupExpression()));
        });

        return list(mapping, form, request, response);
    }
}
