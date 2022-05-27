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
package org.fenixedu.academic.ui.struts.action.administrativeOffice.student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.degreeStructure.ProgramConclusion;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.RegistrationRegime;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumGroup;
import org.fenixedu.academic.dto.student.RegistrationConclusionBean;
import org.fenixedu.academic.dto.student.RegistrationCurriculumBean;
import org.fenixedu.academic.predicate.IllegalDataAccessException;
import org.fenixedu.academic.service.services.administrativeOffice.student.RegistrationConclusionProcess;
import org.fenixedu.academic.service.services.registration.DeleteRegistrationRegime;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

@Mapping(path = "/registration", module = "academicAdministration", functionality = SearchForStudentsDA.class)
@Forwards({
        @Forward(name = "chooseCycleForViewRegistrationCurriculum",
                path = "/academicAdminOffice/student/registration/chooseCycleForViewRegistrationCurriculum.jsp"),
        @Forward(name = "chooseProgramConclusion",
                path = "/academicAdminOffice/student/registration/chooseProgramConclusion.jsp"),
        @Forward(name = "view-registration-curriculum",
                path = "/academicAdminOffice/student/registration/viewRegistrationCurriculum.jsp"),
        @Forward(name = "registrationConclusion", path = "/academicAdminOffice/student/registration/registrationConclusion.jsp"),
        @Forward(name = "registrationConclusionDocument",
                path = "/academicAdminOffice/student/registration/registrationConclusionDocument.jsp"),
        @Forward(name = "showRegimes", path = "/academicAdminOffice/student/registration/showRegimes.jsp"),
        @Forward(name = "createRegime", path = "/academicAdminOffice/student/registration/createRegime.jsp") })
public class RegistrationDA extends StudentRegistrationDA {

    public ActionForward prepareViewRegistrationCurriculum(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        RenderUtils.invalidateViewState();

        final Registration registration = getAndSetRegistration(request);
        final RegistrationCurriculumBean registrationCurriculumBean = new RegistrationCurriculumBean(registration);
        request.setAttribute("registrationCurriculumBean", registrationCurriculumBean);

        final String degreeCurricularPlanID = getStringFromRequest(request, "degreeCurricularPlanID");
        if (degreeCurricularPlanID != null) {
            request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
        }

        return mapping.findForward("chooseCycleForViewRegistrationCurriculum");
    }

    public ActionForward prepareViewRegistrationCurriculumInvalid(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        final Registration registration = getAndSetRegistration(request);

        request.setAttribute("registrationCurriculumBean", getRegistrationCurriculumBeanFromViewState());
        request.setAttribute("degreeCurricularPlanID", registration.getLastDegreeCurricularPlan().getExternalId());

        return mapping.findForward("chooseCycleForViewRegistrationCurriculum");
    }

    private RegistrationCurriculumBean getRegistrationCurriculumBeanFromViewState() {
        return (RegistrationCurriculumBean) getObjectFromViewState("registrationCurriculumBean");
    }

    public ActionForward chooseCycleForViewRegistrationCurriculum(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        final RegistrationCurriculumBean registrationCurriculumBean = getRegistrationCurriculumBeanFromViewState();
        request.setAttribute("registrationCurriculumBean", registrationCurriculumBean);
        request.setAttribute("registration", registrationCurriculumBean.getRegistration());

        final Person studentPerson = registrationCurriculumBean.getStudent().getPerson();
        request.setAttribute("studentPerson", studentPerson);

        return mapping.findForward("view-registration-curriculum");
    }

    public ActionForward viewRegistrationCurriculum(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final RegistrationCurriculumBean registrationCurriculumBean = getRegistrationCurriculumBeanFromViewState();

        final String degreeCurricularPlanID = getStringFromRequest(request, "degreeCurricularPlanID");
        if (degreeCurricularPlanID != null) {
            request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
        }

        request.setAttribute("registrationCurriculumBean", registrationCurriculumBean);

        return mapping.findForward("view-registration-curriculum");
    }

    public ActionForward prepareRegistrationConclusionProcess(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        RenderUtils.invalidateViewState();

        final Registration registration = getAndSetRegistration(request);

        request.setAttribute("registrationConclusionBean",
                new RegistrationConclusionBean(registration.getLastStudentCurricularPlan(), null));
        return mapping.findForward("chooseProgramConclusion");
    }

    public ActionForward prepareRegistrationConclusionProcessInvalid(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        getAndSetRegistration(request);
        request.setAttribute("registrationConclusionBean", getRegistrationConclusionBeanFromViewState());

        return mapping.findForward("chooseProgramConclusion");

    }

    private RegistrationConclusionBean getRegistrationConclusionBeanFromViewState() {
        return (RegistrationConclusionBean) getObjectFromViewState("registrationConclusionBean");
    }

    public ActionForward doRegistrationConclusion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final RegistrationConclusionBean registrationConclusionBean = getRegistrationConclusionBeanFromViewState();

        try {
            RegistrationConclusionProcess.run(registrationConclusionBean);
        } catch (final IllegalDataAccessException e) {
            addActionMessage("illegal.access", request, "error.not.authorized.to.registration.conclusion.process");
            request.setAttribute("registrationConclusionBean", registrationConclusionBean);
            return mapping.findForward("registrationConclusion");

        } catch (final DomainException e) {
            addActionMessage(request, e.getLocalizedMessage(), false);
            request.setAttribute("registrationConclusionBean", registrationConclusionBean);
            return mapping.findForward("registrationConclusion");
        }

        request.setAttribute("registrationId", registrationConclusionBean.getRegistration().getExternalId());
        return visualizeRegistration(mapping, form, request, response);

    }

    public ActionForward revertRegistrationConclusionLastVersion(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        final RegistrationConclusionBean registrationConclusionBean = getRegistrationConclusionBeanFromViewState();
        try {
            RegistrationConclusionProcess.revert(registrationConclusionBean);
        } catch (DomainException e) {
            addActionMessage(request, e.getLocalizedMessage(), false);
            request.setAttribute("registrationConclusionBean", registrationConclusionBean);
            return mapping.findForward("registrationConclusion");
        }

        request.setAttribute("registrationId", registrationConclusionBean.getRegistration().getExternalId());
        return visualizeRegistration(mapping, form, request, response);
    }

    public ActionForward chooseProgramConclusion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final RegistrationConclusionBean registrationConclusionBean = getRegistrationConclusionBeanFromViewState();
        request.setAttribute("registrationConclusionBean", registrationConclusionBean);
        request.setAttribute("registration", registrationConclusionBean.getRegistration());

        return mapping.findForward("registrationConclusion");
    }

    public ActionForward selectProgramConclusion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        Registration registration = getDomainObject(request, "registration");
        ProgramConclusion programConclusion = getDomainObject(request, "programConclusion");
        RegistrationConclusionBean registrationConclusionBean =
                new RegistrationConclusionBean(registration.getLastStudentCurricularPlan(), programConclusion);
        request.setAttribute("registrationConclusionBean", registrationConclusionBean);
        request.setAttribute("registration", registrationConclusionBean.getRegistration());
        return mapping.findForward("registrationConclusion");
    }

    public ActionForward prepareRegistrationConclusionDocument(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        RenderUtils.invalidateViewState();

        final Registration registration = getAndSetRegistration(request);
        request.setAttribute("registration", registration);

        final CurriculumGroup curriculumGroup = getDomainObject(request, "curriculumGroupId");
        final RegistrationConclusionBean registrationConclusionBean;

        if (curriculumGroup == null || curriculumGroup.getDegreeModule().getProgramConclusion() == null) {
            addActionMessage(request, BundleUtil.getString(Bundle.APPLICATION, "error.program.conclusion.empty"), false);
        }

        registrationConclusionBean = new RegistrationConclusionBean(curriculumGroup.getStudentCurricularPlan(),
                curriculumGroup.getDegreeModule().getProgramConclusion());
        request.setAttribute("registrationConclusionBean", registrationConclusionBean);

        String degreePresentationName =
                registration.getDegree().getPresentationName(registrationConclusionBean.getConclusionYear());
        request.setAttribute("degreePresentationName", degreePresentationName);

        return mapping.findForward("registrationConclusionDocument");
    }

    protected ExecutionDegree getExecutionDegree(final ExecutionYear executionYear, final Degree degree) {
        for (final DegreeCurricularPlan degreeCurricularPlan : degree.getDegreeCurricularPlansSet()) {
            for (final ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegreesSet()) {
                if (executionDegree.getExecutionYear() == executionYear) {
                    return executionDegree;
                }
            }
        }
        return null;
    }

    public ActionForward showRegimes(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final Registration registration = getAndSetRegistration(request);
        final List<RegistrationRegime> regimes = new ArrayList<RegistrationRegime>(registration.getRegistrationRegimesSet());

        Collections.sort(regimes, new ReverseComparator(RegistrationRegime.COMPARATOR_BY_EXECUTION_YEAR));
        request.setAttribute("registrationRegimes", regimes);

        return mapping.findForward("showRegimes");
    }

    public ActionForward prepareCreateRegime(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        getAndSetRegistration(request);
        return mapping.findForward("createRegime");
    }

    public ActionForward deleteRegime(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        try {
            final RegistrationRegime regime = getRegistrationRegime(request);
            DeleteRegistrationRegime.run(regime);
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
        }
        return showRegimes(mapping, actionForm, request, response);
    }

    private RegistrationRegime getRegistrationRegime(HttpServletRequest request) {
        return getDomainObject(request, "registrationRegimeId");
    }

}
