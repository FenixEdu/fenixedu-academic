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
package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.net.MalformedURLException;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.Site.SiteMapper;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.cms.OldCmsSemanticURLHandler;
import net.sourceforge.fenixedu.domain.contacts.EmailAddress;
import net.sourceforge.fenixedu.domain.homepage.Homepage;
import net.sourceforge.fenixedu.domain.organizationalStructure.Contract;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;
import net.sourceforge.fenixedu.presentationTier.Action.manager.SiteVisualizationDA;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.RequestUtils;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.image.TextPngCreator;

@Mapping(module = "publico", path = "/viewHomepage")
@Forwards({ @Forward(name = "view-homepage", path = "view-homepage"), @Forward(path = "view-homepage", name = "view-homepage"),
        @Forward(path = "list-homepages-teachers", name = "list-homepages-teachers"),
        @Forward(path = "list-homepages-employees", name = "list-homepages-employees"),
        @Forward(path = "list-homepages-students", name = "list-homepages-students"),
        @Forward(path = "list-homepages-alumni", name = "list-homepages-alumni"),
        @Forward(path = "view-homepage-section", name = "site-section"),
        @Forward(path = "view-homepage-section-deny", name = "site-section-deny"),
        @Forward(path = "view-homepage-section-adviseLogin", name = "site-section-adviseLogin"),
        @Forward(path = "view-homepage-item", name = "site-item"),
        @Forward(path = "view-homepage-item-deny", name = "site-item-deny"),
        @Forward(path = "view-homepage-item-adviseLogin", name = "site-item-adviseLogin"),
        @Forward(path = "homepage-stats", name = "homepage-stats"),
        @Forward(path = "not-found-homepage", name = "not-found-homepage") })
public class ViewHomepageDA extends SiteVisualizationDA {

    @Override
    protected String getDirectLinkContext(HttpServletRequest request) {
        Homepage homepage = (Homepage) request.getAttribute("homepage");
        if (homepage == null) {
            return null;
        }

        try {
            return RequestUtils.absoluteURL(request, "/homepage/" + homepage.getPerson().getUser().getUsername()).toString();
        } catch (MalformedURLException e) {
            return null;
        }
    }

    public ActionForward show(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        Homepage homepage = getHomepage(request);

        if (homepage == null || !homepage.getActivated().booleanValue()) {
            final ActionMessages actionMessages = new ActionMessages();
            actionMessages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("homepage.not.found"));
            saveMessages(request, actionMessages);

            return notFound(mapping, actionForm, request, response);
        } else {
            SortedSet<Attends> personAttendsSortedByExecutionCourseName =
                    new TreeSet<Attends>(Attends.ATTENDS_COMPARATOR_BY_EXECUTION_COURSE_NAME);
            personAttendsSortedByExecutionCourseName.addAll(homepage.getPerson().getCurrentAttends());

            request.setAttribute("personAttends", personAttendsSortedByExecutionCourseName);
            request.setAttribute("homepage", homepage);

            return mapping.findForward("view-homepage");
        }
    }

    public ActionForward notFound(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        return mapping.findForward("not-found-homepage");
    }

    public ActionForward listTeachers(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final SortedMap<Unit, SortedSet<Homepage>> homepages =
                new TreeMap<Unit, SortedSet<Homepage>>(Unit.COMPARATOR_BY_NAME_AND_ID);
        for (final Teacher teacher : rootDomainObject.getTeachersSet()) {
            final Person person = teacher.getPerson();
            final Employee employee = person.getEmployee();
            if (employee != null) {
                final Contract contract = employee.getCurrentWorkingContract();
                if (contract != null) {
                    final Unit unit = contract.getWorkingUnit();
                    final SortedSet<Homepage> unitHomepages;
                    if (homepages.containsKey(unit)) {
                        unitHomepages = homepages.get(unit);
                    } else {
                        unitHomepages = new TreeSet<Homepage>(Homepage.HOMEPAGE_COMPARATOR_BY_NAME);
                        homepages.put(unit, unitHomepages);
                    }
                    final Homepage homepage = person.getHomepage();
                    if (homepage != null && homepage.getActivated().booleanValue()) {
                        unitHomepages.add(homepage);
                    }
                }
            }
        }
        request.setAttribute("homepages", homepages);

        final String selectedPage = request.getParameter("selectedPage");
        if (selectedPage != null) {
            request.setAttribute("selectedPage", selectedPage);
        }

        return mapping.findForward("list-homepages-teachers");
    }

    public ActionForward listEmployees(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final SortedMap<Unit, SortedSet<Homepage>> homepages =
                new TreeMap<Unit, SortedSet<Homepage>>(Unit.COMPARATOR_BY_NAME_AND_ID);
        for (final Employee employee : rootDomainObject.getEmployeesSet()) {
            final Person person = employee.getPerson();
            if (person != null) {
                final Teacher teacher = person.getTeacher();
                if (teacher == null) {
                    final Contract contract = employee.getCurrentWorkingContract();
                    if (contract != null) {
                        final Unit unit = contract.getWorkingUnit();
                        final SortedSet<Homepage> unitHomepages;
                        if (homepages.containsKey(unit)) {
                            unitHomepages = homepages.get(unit);
                        } else {
                            unitHomepages = new TreeSet<Homepage>(Homepage.HOMEPAGE_COMPARATOR_BY_NAME);
                            homepages.put(unit, unitHomepages);
                        }
                        final Homepage homepage = person.getHomepage();
                        if (homepage != null && homepage.getActivated().booleanValue()) {
                            unitHomepages.add(homepage);
                        }
                    }
                }
            }
        }
        request.setAttribute("homepages", homepages);

        final String selectedPage = request.getParameter("selectedPage");
        if (selectedPage != null) {
            request.setAttribute("selectedPage", selectedPage);
        }

        return mapping.findForward("list-homepages-employees");
    }

    public ActionForward listStudents(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final SortedMap<Degree, SortedSet<Homepage>> homepages =
                new TreeMap<Degree, SortedSet<Homepage>>(Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID);
        for (final Registration registration : rootDomainObject.getRegistrationsSet()) {
            final StudentCurricularPlan studentCurricularPlan = registration.getActiveStudentCurricularPlan();
            if (studentCurricularPlan != null) {
                final DegreeCurricularPlan degreeCurricularPlan = studentCurricularPlan.getDegreeCurricularPlan();
                final Degree degree = degreeCurricularPlan.getDegree();
                final Person person = registration.getPerson();
                final SortedSet<Homepage> degreeHomepages;
                if (homepages.containsKey(degree)) {
                    degreeHomepages = homepages.get(degree);
                } else {
                    degreeHomepages = new TreeSet<Homepage>(Homepage.HOMEPAGE_COMPARATOR_BY_NAME);
                    homepages.put(degree, degreeHomepages);
                }
                final Homepage homepage = person.getHomepage();
                if (homepage != null && homepage.getActivated().booleanValue()) {
                    degreeHomepages.add(homepage);
                }
            }
        }
        request.setAttribute("homepages", homepages);

        final String selectedPage = request.getParameter("selectedPage");
        if (selectedPage != null) {
            request.setAttribute("selectedPage", selectedPage);
        }

        return mapping.findForward("list-homepages-students");
    }

    public ActionForward listAlumni(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final SortedMap<Degree, SortedSet<Homepage>> homepages =
                new TreeMap<Degree, SortedSet<Homepage>>(Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID);
        for (final Registration registration : rootDomainObject.getRegistrationsSet()) {

            if (registration.getActiveState().getStateType().equals(RegistrationStateType.CONCLUDED)) {

                final Degree degree = registration.getActiveStudentCurricularPlan().getDegreeCurricularPlan().getDegree();

                final SortedSet<Homepage> degreeHomepages;
                if (homepages.containsKey(degree)) {
                    degreeHomepages = homepages.get(degree);
                } else {
                    degreeHomepages = new TreeSet<Homepage>(Homepage.HOMEPAGE_COMPARATOR_BY_NAME);
                    homepages.put(degree, degreeHomepages);
                }

                final Homepage homepage = registration.getPerson().getHomepage();
                if (homepage != null && homepage.getActivated()) {
                    degreeHomepages.add(homepage);
                }
            }

        }

        request.setAttribute("homepages", homepages);

        final String selectedPage = request.getParameter("selectedPage");
        if (selectedPage != null) {
            request.setAttribute("selectedPage", selectedPage);
        }

        return mapping.findForward("list-homepages-alumni");
    }

    public ActionForward emailPng(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String emailId = request.getParameter("email");
        EmailAddress email = (EmailAddress) FenixFramework.getDomainObject(emailId);
        if (email != null && StringUtils.isNotEmpty(email.getValue())) {
            final byte[] pngFile = TextPngCreator.createPng("Arial", 12, "000000", email.getValue());
            response.setContentType("image/png");
            response.getOutputStream().write(pngFile);
            response.getOutputStream().close();
        }
        return null;
    }

    public ActionForward stats(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("homepages", Homepage.getAllHomepages());
        return mapping.findForward("homepage-stats");
    }

    @Override
    protected ActionForward getSiteDefaultView(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return show(mapping, form, request, response);
    }

    protected Homepage getHomepage(HttpServletRequest request) {
        Site site = SiteMapper.getSite(request);
        if (site instanceof Homepage) {
            return (Homepage) site;
        } else {
            String homepageID = request.getParameter("homepageID");
            if (homepageID != null) {
                site = FenixFramework.getDomainObject(homepageID);
            } else {
                site = getDomainObject(request, "siteID");
            }
            OldCmsSemanticURLHandler.selectSite(request, site);
            return (Homepage) site;
        }

    }

}