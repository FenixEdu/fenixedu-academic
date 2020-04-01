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
package org.fenixedu.academic.ui.struts.action.administrativeOffice.studentEnrolment;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.academic.domain.ExternalCurricularCourse;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.studentCurriculum.ExternalEnrolment;
import org.fenixedu.academic.dto.administrativeOffice.externalUnits.EditExternalEnrolmentBean;
import org.fenixedu.academic.dto.administrativeOffice.externalUnits.ExternalCurricularCourseResultBean;
import org.fenixedu.academic.dto.administrativeOffice.studentEnrolment.ExternalCurricularCourseEnrolmentBean;
import org.fenixedu.academic.predicate.IllegalDataAccessException;
import org.fenixedu.academic.service.services.administrativeOffice.enrolment.CreateExternalEnrolments;
import org.fenixedu.academic.service.services.administrativeOffice.enrolment.DeleteExternalEnrolments;
import org.fenixedu.academic.service.services.administrativeOffice.externalUnits.EditExternalEnrolment;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.academic.ui.struts.action.administrativeOffice.student.SearchForStudentsDA;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixframework.FenixFramework;

@Mapping(path = "/studentExternalEnrolments", module = "academicAdministration", formBean = "studentExternalEnrolmentsForm",
        functionality = SearchForStudentsDA.class)
@Forwards({
        @Forward(name = "manageExternalEnrolments",
                path = "/academicAdminOffice/student/enrollment/bolonha/manageExternalEnrolments.jsp"),
        @Forward(name = "prepareEditExternalEnrolment",
                path = "/academicAdminOffice/student/enrollment/bolonha/editExternalEnrolment.jsp"),
        @Forward(name = "chooseExternalUnit", path = "/academicAdminOffice/student/enrollment/bolonha/chooseExternalUnit.jsp"),
        @Forward(name = "chooseExternalCurricularCourses",
                path = "/academicAdminOffice/student/enrollment/bolonha/chooseExternalCurricularCourses.jsp"),
        @Forward(name = "createExternalEnrolments",
                path = "/academicAdminOffice/student/enrollment/bolonha/createExternalEnrolments.jsp"),
        @Forward(name = "viewRegistrationDetails",
                path = "/academicAdminOffice/student/registration/viewRegistrationDetails.jsp") })
public class StudentExternalEnrolmentsDA extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        request.setAttribute("contextInformation", getContextInformation());
        request.setAttribute("parameters", getParameters(request));
        return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward manageExternalEnrolments(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("registration", getRegistration(request, actionForm));
        return mapping.findForward("manageExternalEnrolments");
    }

    public ActionForward chooseExternalUnit(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("registration", getRegistration(request, actionForm));

        final String unitId = (String) getFromRequest(request, "unitId");
        if (StringUtils.isNotBlank(unitId)) {
            request.setAttribute("unit", FenixFramework.getDomainObject(unitId));
        }
        //request.setAttribute("unit", UnitUtils.readEarthUnit());

        return mapping.findForward("chooseExternalUnit");
    }

    public ActionForward chooseExternalCurricularCourses(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final Unit externalUnit = getExternalUnit(request, actionForm);

        request.setAttribute("externalUnit", externalUnit);
        request.setAttribute("externalCurricularCourseBeans", buildExternalCurricularCourseResultBeans(externalUnit));
        request.setAttribute("registration", getRegistration(request, actionForm));

        return mapping.findForward("chooseExternalCurricularCourses");
    }

    private Set<ExternalCurricularCourseResultBean> buildExternalCurricularCourseResultBeans(final Unit unit) {
        final Set<ExternalCurricularCourseResultBean> result =
                new TreeSet<ExternalCurricularCourseResultBean>(new BeanComparator("fullName"));

        ExternalCurricularCourse.readAllExternalCurricularCourses(unit)
                .forEach(ecc -> result.add(new ExternalCurricularCourseResultBean(ecc)));
        return result;
    }

    public ActionForward prepareCreateExternalEnrolments(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final String[] externalCurricularCourseIDs =
                ((DynaActionForm) actionForm).getStrings("selectedExternalCurricularCourses");
        if (externalCurricularCourseIDs == null || externalCurricularCourseIDs.length == 0) {
            addActionMessage(request, "error.StudentEnrolmentDA.must.choose.externalCurricularCourses");
            return chooseExternalCurricularCourses(mapping, actionForm, request, response);
        }

        request.setAttribute("externalCurricularCourseEnrolmentBeans",
                buildExternalCurricularCourseEnrolmentBeans(externalCurricularCourseIDs));
        request.setAttribute("registration", getRegistration(request, actionForm));
        request.setAttribute("externalUnit", getExternalUnit(request, actionForm));

        return mapping.findForward("createExternalEnrolments");
    }

    private List<ExternalCurricularCourseEnrolmentBean> buildExternalCurricularCourseEnrolmentBeans(
            final String[] externalCurricularCourseIDs) {
        final List<ExternalCurricularCourseEnrolmentBean> result =
                new ArrayList<ExternalCurricularCourseEnrolmentBean>(externalCurricularCourseIDs.length);
        for (final String externalCurricularCourseID : externalCurricularCourseIDs) {
            result.add(new ExternalCurricularCourseEnrolmentBean(getExternalCurricularCourseByID(externalCurricularCourseID)));
        }
        return result;
    }

    public ActionForward createExternalEnrolmentsInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("externalCurricularCourseEnrolmentBeans", getRenderedObject());
        request.setAttribute("registration", getRegistration(request, actionForm));
        request.setAttribute("externalUnit", getExternalUnit(request, actionForm));

        return mapping.findForward("createExternalEnrolments");
    }

    public ActionForward createExternalEnrolments(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final List<ExternalCurricularCourseEnrolmentBean> externalCurricularCourseEnrolmentBeans =
                getRenderedObject("externalCurricularCourseEnrolmentBeans");
        final Registration registration = getRegistration(request, actionForm);

        try {
            CreateExternalEnrolments.run(registration, externalCurricularCourseEnrolmentBeans);
        } catch (DomainException e) {
            addActionMessage("error", request, e.getMessage(), e.getArgs());
            request.setAttribute("registration", getRegistration(request, actionForm));
            request.setAttribute("externalCurricularCourseEnrolmentBeans", externalCurricularCourseEnrolmentBeans);
            request.setAttribute("externalUnit", getExternalUnit(request, actionForm));
            return mapping.findForward("createExternalEnrolments");
        }

        return backToMainPage(mapping, actionForm, request, response);
    }

    public ActionForward deleteExternalEnrolments(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        final String[] externalEnrolmentIDs = ((DynaActionForm) actionForm).getStrings("externalEnrolmentsToDelete");
        final Registration registration = getRegistration(request, actionForm);
        request.setAttribute("registration", registration);

        try {
            DeleteExternalEnrolments.run(registration, externalEnrolmentIDs);
        } catch (NotAuthorizedException e) {
            addActionMessage("error", request, "error.notAuthorized");
        } catch (DomainException e) {
            addActionMessage("error", request, e.getMessage(), e.getArgs());
        }

        return mapping.findForward("manageExternalEnrolments");
    }

    public ActionForward prepareEditExternalEnrolment(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final ExternalEnrolment externalEnrolment = getExternalEnrolment(request, actionForm);
        request.setAttribute("registration", externalEnrolment.getRegistration());
        request.setAttribute("externalEnrolmentBean", new EditExternalEnrolmentBean(externalEnrolment));
        return mapping.findForward("prepareEditExternalEnrolment");
    }

    public ActionForward editExternalEnrolment(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final EditExternalEnrolmentBean externalEnrolmentBean = getRenderedObject();
        final ExternalEnrolment externalEnrolment = externalEnrolmentBean.getExternalEnrolment();
        try {
            EditExternalEnrolment.run(externalEnrolmentBean, externalEnrolment.getRegistration());
            return manageExternalEnrolments(mapping, actionForm, request, response);

        } catch (final IllegalDataAccessException e) {
            addActionMessage("error", request, "error.notAuthorized");
        } catch (final DomainException e) {
            addActionMessage("error", request, e.getMessage());
        }

        request.setAttribute("externalEnrolmentBean", externalEnrolmentBean);
        return mapping.findForward("prepareEditExternalEnrolment");
    }

    public ActionForward backToMainPage(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        return manageExternalEnrolments(mapping, actionForm, request, response);
    }

    public ActionForward cancelExternalEnrolment(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("registration", getRegistration(request, actionForm));
        return mapping.findForward("viewRegistrationDetails");
    }

    protected String getContextInformation() {
        return "/studentExternalEnrolments.do?";
    }

    protected String getParameters(final HttpServletRequest request) {
        return StringUtils.EMPTY;
    }

    protected Registration getRegistration(final HttpServletRequest request, ActionForm form) {
        return FenixFramework.getDomainObject(getStringFromRequestOrForm(request, (DynaActionForm) form, "registrationId"));
    }

    protected Unit getExternalUnit(final HttpServletRequest request, ActionForm actionForm) {
        return FenixFramework.getDomainObject(getStringFromRequestOrForm(request, (DynaActionForm) actionForm, "externalUnitId"));
    }

    protected ExternalCurricularCourse getExternalCurricularCourseByID(final String externalCurricularCourseID) {
        return FenixFramework.getDomainObject(externalCurricularCourseID);
    }

    protected ExternalEnrolment getExternalEnrolment(final HttpServletRequest request, ActionForm actionForm) {
        return FenixFramework
                .getDomainObject(getStringFromRequestOrForm(request, (DynaActionForm) actionForm, "externalEnrolmentId"));
    }

    protected String getStringFromRequestOrForm(final HttpServletRequest request, final DynaActionForm form, final String name) {
        final String value = getStringFromRequest(request, name);
        return (value != null) ? value : form.getString(name);
    }
}