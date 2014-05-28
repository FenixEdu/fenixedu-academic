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
package net.sourceforge.fenixedu.presentationTier.Action.publico.department;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.cms.OldCmsSemanticURLHandler;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.organizationalStructure.DepartmentUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalCategory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.publico.UnitSiteVisualizationDA;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

import com.google.common.base.Strings;

@Mapping(module = "publico", path = "/department/departmentSite", scope = "session", parameter = "method")
@Forwards(value = { @Forward(name = "department-employees", path = "department-employees"),
        @Forward(name = "announcementsAction", path = "/department/announcements.do"),
        @Forward(name = "department-degrees", path = "department-degrees"),
        @Forward(name = "frontPage-INTRO_BANNER", path = "department-site-front-page-intro-banner"),
        @Forward(name = "eventsAction", path = "/department/events.do"),
        @Forward(name = "department-teachers-category", path = "department-teachers-category"),
        @Forward(name = "unit-subunits", path = "department-subunits"),
        @Forward(name = "frontPage-BANNER_INTRO", path = "department-site-front-page-banner-intro"),
        @Forward(name = "site-section-adviseLogin", path = "department-section-adviseLogin"),
        @Forward(name = "department-teachers-area", path = "department-teachers-area"),
        @Forward(name = "frontPage-BANNER_INTRO_COLLAPSED", path = "department-site-front-page-intro-float"),
        @Forward(name = "site-section", path = "department-section"), @Forward(name = "site-item", path = "department-item"),
        @Forward(name = "eventsRSSAction", path = "/department/eventsRSS.do"),
        @Forward(name = "site-item-deny", path = "department-item-deny"),
        @Forward(name = "site-item-adviseLogin", path = "department-item-adviseLogin"),
        @Forward(name = "announcementsRSSAction", path = "/department/announcementsRSS.do"),
        @Forward(name = "unit-organization", path = "department-organization"),
        @Forward(name = "site-section-deny", path = "department-section-deny") })
public class PublicDepartmentSiteDA extends UnitSiteVisualizationDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Department department = getDepartment(request);
        if (department != null) {
            request.setAttribute("department", department);
            request.setAttribute("unit", department.getDepartmentUnit());
            OldCmsSemanticURLHandler.selectSite(request, department.getDepartmentUnit().getSite());
        }
        return super.execute(mapping, actionForm, request, response);
    }

    @Override
    protected ActionForward getSiteDefaultView(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return presentation(mapping, form, request, response);
    }

    @Override
    protected String getContextParamName(HttpServletRequest request) {
        return "selectedDepartmentUnitID";
    }

    private Department getDepartment(HttpServletRequest request) {
        String selectedDepartmentUnitId = FenixContextDispatchAction.getFromRequest("selectedDepartmentUnitID", request);
        if (!Strings.isNullOrEmpty(selectedDepartmentUnitId)) {
            DepartmentUnit departmentUnit = FenixFramework.getDomainObject(selectedDepartmentUnitId);
            return departmentUnit != null ? departmentUnit.getDepartment() : null;
        } else {
            Unit unit = getUnit(request);
            return unit != null ? unit.getDepartment() : null;
        }
    }

    public ActionForward employees(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        Unit unit = getUnit(request);

        BeanComparator employeeComparator = new BeanComparator("person", Party.COMPARATOR_BY_NAME_AND_ID);

        SortedSet<Unit> workingUnits = new TreeSet<Unit>(Unit.COMPARATOR_BY_NAME_AND_ID);
        SortedSet<Employee> noUnitAvailable = new TreeSet<Employee>(employeeComparator);
        Map<String, SortedSet<Employee>> employeesMap = new Hashtable<String, SortedSet<Employee>>();

        for (Employee employee : unit.getAllCurrentNonTeacherEmployees()) {

            if (employee.getPerson().hasRole(RoleType.TEACHER)) {
                continue;
            }

            Unit workingUnit = employee.getCurrentWorkingPlace();

            if (workingUnit != null) {
                workingUnits.add(workingUnit);

                String areaKey = workingUnit.getExternalId().toString();
                SortedSet<Employee> employees = employeesMap.get(areaKey);
                if (employees == null) {
                    employees = new TreeSet<Employee>(employeeComparator);
                    employeesMap.put(areaKey, employees);
                }

                employees.add(employee);
            } else {
                noUnitAvailable.add(employee);
            }
        }

        if (workingUnits.isEmpty()) {
            request.setAttribute("ignoreAreas", true);
        }

        request.setAttribute("areas", workingUnits);
        request.setAttribute("employees", employeesMap);
        request.setAttribute("employeesNoArea", noUnitAvailable);

        return mapping.findForward("department-employees");
    }

    public ActionForward degrees(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        Unit unit = getUnit(request);

        Department department = unit.getDepartment();
        if (department == null) {
            return presentation(mapping, actionForm, request, response);
        }

        Map<DegreeType, SortedSet<Degree>> degreeAndTypes = new HashMap<DegreeType, SortedSet<Degree>>();
        SortedSet<DegreeType> activeTypes = new TreeSet<DegreeType>();
        SortedSet<DegreeType> inactiveTypes = new TreeSet<DegreeType>();

        for (Degree degree : department.getDegrees()) {
            DegreeType type = degree.getDegreeType();
            if (degree.isActive()) {
                activeTypes.add(type);
            } else {
                inactiveTypes.add(type);
            }

            SortedSet<Degree> current = degreeAndTypes.get(type);
            if (current == null) {
                current = new TreeSet<Degree>(Degree.COMPARATOR_BY_NAME_AND_ID);
                degreeAndTypes.put(type, current);
            }

            current.add(degree);
        }

        request.setAttribute("inactive-types", inactiveTypes);
        request.setAttribute("active-types", activeTypes);

        for (DegreeType type : inactiveTypes) {
            request.setAttribute(type.getName(), degreeAndTypes.get(type));
        }

        for (DegreeType type : activeTypes) {
            request.setAttribute(type.getName(), degreeAndTypes.get(type));
        }

        return mapping.findForward("department-degrees");
    }

    public ActionForward teachers(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        Unit unit = getUnit(request);

        Department department = unit.getDepartment();
        if (department == null) {
            return presentation(mapping, actionForm, request, response);
        }

        String mode = getTeachersMode(request.getParameter("viewBy"));

        if (mode.equals("category")) {
            setupTeachersCategories(request, department);
        } else if (mode.equals("area")) {
            setupTeachersAreas(request, department);
        }

        return mapping.findForward("department-teachers-" + mode);
    }

    private String getTeachersMode(String parameter) {
        if (parameter == null) {
            return "category";
        }

        if (parameter.equals("category") || parameter.equals("area")) {
            return parameter;
        }

        return "category";
    }

    private void setupTeachersCategories(HttpServletRequest request, Department department) {

        SortedSet<ProfessionalCategory> categories = new TreeSet<ProfessionalCategory>();
        Map<String, SortedSet<Teacher>> teachers = new Hashtable<String, SortedSet<Teacher>>();

        for (Teacher teacher : department.getAllCurrentTeachers()) {
            ProfessionalCategory professionalCategory = teacher.getCategory();
            if (professionalCategory != null) {
                String category = professionalCategory.getExternalId();
                categories.add(professionalCategory);
                addListTeacher(teachers, category, teacher);
            }
        }

        request.setAttribute("categories", categories);
        request.setAttribute("teachers", teachers);
    }

    private void setupTeachersAreas(HttpServletRequest request, Department department) {

        SortedSet<Unit> areas = new TreeSet<Unit>(Unit.COMPARATOR_BY_NAME_AND_ID);
        SortedSet<Teacher> teachersNoArea = new TreeSet<Teacher>(Teacher.TEACHER_COMPARATOR_BY_CATEGORY_AND_NUMBER);
        Map<String, SortedSet<Teacher>> teachers = new Hashtable<String, SortedSet<Teacher>>();

        for (Teacher teacher : department.getAllCurrentTeachers()) {
            Unit area = teacher.getCurrentSectionOrScientificArea();

            if (area != null) {
                areas.add(area);
                addListTeacher(teachers, area.getExternalId().toString(), teacher);
            } else {
                teachersNoArea.add(teacher);
            }
        }

        if (areas.isEmpty()) {
            request.setAttribute("ignoreAreas", true);
        }

        request.setAttribute("areas", areas);
        request.setAttribute("teachers", teachers);
        request.setAttribute("teachersNoArea", teachersNoArea);
    }

    private void addListTeacher(Map<String, SortedSet<Teacher>> teachersMap, String key, Teacher teacher) {
        SortedSet<Teacher> teachers = teachersMap.get(key);

        if (teachers == null) {
            teachers = new TreeSet<Teacher>(Teacher.TEACHER_COMPARATOR_BY_CATEGORY_AND_NUMBER);
            teachersMap.put(key, teachers);
        }

        teachers.add(teacher);
    }
}
