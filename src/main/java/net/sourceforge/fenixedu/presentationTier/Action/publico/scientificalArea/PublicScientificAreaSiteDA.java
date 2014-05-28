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
package net.sourceforge.fenixedu.presentationTier.Action.publico.scientificalArea;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.ScientificAreaUnit;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalCategory;
import net.sourceforge.fenixedu.presentationTier.Action.publico.UnitSiteVisualizationDA;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "publico", path = "/scientificArea/viewSite", scope = "session", parameter = "method")
@Forwards(value = { @Forward(name = "announcementsAction", path = "/scientificArea/announcements.do"),
        @Forward(name = "view-teachers", path = "scientific-area-site-teachers"),
        @Forward(name = "view-courses", path = "scientific-area-site-courses"),
        @Forward(name = "frontPage-INTRO_BANNER", path = "basicUnit-site-front-page-intro-banner"),
        @Forward(name = "eventsAction", path = "/scientificArea/events.do"),
        @Forward(name = "frontPage-BANNER_INTRO", path = "basicUnit-site-front-page-banner-intro"),
        @Forward(name = "unit-subunits", path = "basicUnit-subunits"),
        @Forward(name = "view-employees", path = "scientific-area-site-employees"),
        @Forward(name = "site-section-adviseLogin", path = "basicUnit-section-adviseLogin"),
        @Forward(name = "frontPage-BANNER_INTRO_COLLAPSED", path = "basicUnit-site-front-page-intro-float"),
        @Forward(name = "site-section", path = "basicUnit-section"), @Forward(name = "site-item", path = "basicUnit-item"),
        @Forward(name = "eventsRSSAction", path = "/scientificArea/eventsRSS.do"),
        @Forward(name = "site-item-deny", path = "basicUnit-item-deny"),
        @Forward(name = "site-item-adviseLogin", path = "basicUnit-item-adviseLogin"),
        @Forward(name = "announcementsRSSAction", path = "/scientificArea/announcementsRSS.do"),
        @Forward(name = "unit-organization", path = "scientific-area-organization"),
        @Forward(name = "site-section-deny", path = "basicUnit-section-deny") })
public class PublicScientificAreaSiteDA extends UnitSiteVisualizationDA {

    @Override
    protected ActionForward getSiteDefaultView(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return presentation(mapping, form, request, response);
    }

    public ActionForward viewTeachers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        ScientificAreaUnit scientificArea = (ScientificAreaUnit) getUnit(request);

        YearMonthDay today = new YearMonthDay();
        YearMonthDay tomorrow = today.plusDays(1);

        SortedSet<ProfessionalCategory> categories = new TreeSet<ProfessionalCategory>();
        Map<String, SortedSet<Person>> teachers = new Hashtable<String, SortedSet<Person>>();

        for (Teacher teacher : scientificArea.getDepartmentUnit().getDepartment().getAllTeachers(today, tomorrow)) {
            if (teacher.getCurrentSectionOrScientificArea() == scientificArea) {
                ProfessionalCategory professionalCategory = teacher.getCategory();
                if (professionalCategory != null) {
                    String category = professionalCategory.getExternalId();
                    categories.add(professionalCategory);
                    addListTeacher(teachers, category, teacher);
                }
            }
        }

        request.setAttribute("categories", categories);
        request.setAttribute("teachers", teachers);

        return mapping.findForward("view-teachers");
    }

    public ActionForward viewEmployees(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        ScientificAreaUnit scientificArea = (ScientificAreaUnit) getUnit(request);

        List<Person> employees = new ArrayList<Person>();
        for (Employee employee : scientificArea.getAllCurrentNonTeacherEmployees()) {
            employees.add(employee.getPerson());
        }

        Collections.sort(employees, Party.COMPARATOR_BY_NAME_AND_ID);
        request.setAttribute("employees", employees);
        return mapping.findForward("view-employees");

    }

    public ActionForward viewCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        ScientificAreaUnit scientificArea = (ScientificAreaUnit) getUnit(request);

        request.setAttribute("courseGroupUnits", scientificArea.getCompetenceCourseGroupUnits());

        return mapping.findForward("view-courses");
    }

    private void addListTeacher(Map<String, SortedSet<Person>> teachersMap, String key, Teacher teacher) {
        SortedSet<Person> teachers = teachersMap.get(key);

        if (teachers == null) {
            teachers = new TreeSet<Person>(new BeanComparator("teacher", Teacher.TEACHER_COMPARATOR_BY_CATEGORY_AND_NUMBER));
            teachersMap.put(key, teachers);
        }

        teachers.add(teacher.getPerson());
    }

}
