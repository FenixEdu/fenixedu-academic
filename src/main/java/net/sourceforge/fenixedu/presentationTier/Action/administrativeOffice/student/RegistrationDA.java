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
package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.student.RegistrationConclusionProcess;
import net.sourceforge.fenixedu.applicationTier.Servico.enrollment.shift.WriteStudentAttendingCourse;
import net.sourceforge.fenixedu.applicationTier.Servico.registration.DeleteRegistrationRegime;
import net.sourceforge.fenixedu.dataTransferObject.AddAttendsBean;
import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationConclusionBean;
import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationCurriculumBean;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.RegistrationRegime;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.injectionCode.IllegalDataAccessException;
import net.sourceforge.fenixedu.predicates.RegistrationPredicates;

import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@Mapping(path = "/registration", module = "academicAdministration", functionality = SearchForStudentsDA.class)
@Forwards({
        @Forward(name = "chooseCycleForViewRegistrationCurriculum",
                path = "/academicAdminOffice/student/registration/chooseCycleForViewRegistrationCurriculum.jsp"),
        @Forward(name = "chooseCycleForRegistrationConclusion",
                path = "/academicAdminOffice/student/registration/chooseCycleForRegistrationConclusion.jsp"),
        @Forward(name = "view-registration-curriculum",
                path = "/academicAdminOffice/student/registration/viewRegistrationCurriculum.jsp"),
        @Forward(name = "registrationConclusion", path = "/academicAdminOffice/student/registration/registrationConclusion.jsp"),
        @Forward(name = "registrationConclusionDocument",
                path = "/academicAdminOffice/student/registration/registrationConclusionDocument.jsp"),
        @Forward(name = "viewAttends", path = "/academicAdminOffice/student/registration/viewAttends.jsp"),
        @Forward(name = "addAttends", path = "/academicAdminOffice/student/registration/addAttends.jsp"),
        @Forward(name = "showRegimes", path = "/academicAdminOffice/student/registration/showRegimes.jsp"),
        @Forward(name = "createRegime", path = "/academicAdminOffice/student/registration/createRegime.jsp"),
        @Forward(name = "view-application", path = "/academicAdminOffice/student/registration/application.jsp") })
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

        if (!registrationCurriculumBean.hasCycleCurriculumGroup()) {
            final List<CycleCurriculumGroup> internalCycleCurriculumGroups =
                    registration.getLastStudentCurricularPlan().getInternalCycleCurriculumGrops();
            if (internalCycleCurriculumGroups.size() > 1) {
                return mapping.findForward("chooseCycleForViewRegistrationCurriculum");
            }
        }

        return mapping.findForward("view-registration-curriculum");
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

        final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
        if (registrationCurriculumBean.getExecutionYear() == null
                && registrationCurriculumBean.getRegistration().hasAnyEnrolmentsIn(currentExecutionYear)) {
            registrationCurriculumBean.setExecutionYear(currentExecutionYear);
        }

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

        if (registration.isBolonha()) {

            if (registration.getLastStudentCurricularPlan().getInternalCycleCurriculumGrops().size() > 1) {
                request.setAttribute("registrationConclusionBean", new RegistrationConclusionBean(registration));
                return mapping.findForward("chooseCycleForRegistrationConclusion");

            } else if (registration.getInternalCycleCurriculumGrops().size() == 1) {
                final RegistrationConclusionBean bean = buildRegistrationConclusionBean(registration);
                bean.setCycleCurriculumGroup(registration.getInternalCycleCurriculumGrops().iterator().next());
                request.setAttribute("registrationConclusionBean", bean);
                return mapping.findForward("registrationConclusion");

            } else {
                return mapping.findForward("chooseCycleForRegistrationConclusion");
            }
        }

        request.setAttribute("registrationConclusionBean", buildRegistrationConclusionBean(registration));
        return mapping.findForward("registrationConclusion");
    }

    private RegistrationConclusionBean buildRegistrationConclusionBean(final Registration registration) {
        final RegistrationConclusionBean bean = new RegistrationConclusionBean(registration);
        bean.setHasAccessToRegistrationConclusionProcess(RegistrationPredicates.MANAGE_CONCLUSION_PROCESS.evaluate(registration));
        return bean;
    }

    public ActionForward prepareRegistrationConclusionProcessInvalid(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("registrationConclusionBean", getRegistrationConclusionBeanFromViewState());

        return mapping.findForward("chooseCycleForRegistrationConclusion");

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
            addActionMessage(request, e.getKey(), e.getArgs());
            request.setAttribute("registrationConclusionBean", registrationConclusionBean);
            return mapping.findForward("registrationConclusion");
        }

        request.setAttribute("registrationId", registrationConclusionBean.getRegistration().getExternalId());
        return visualizeRegistration(mapping, form, request, response);

    }

    public ActionForward chooseCycleCurriculumGroupForConclusion(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        final RegistrationConclusionBean registrationConclusionBean = getRegistrationConclusionBeanFromViewState();
        request.setAttribute("registrationConclusionBean", registrationConclusionBean);
        request.setAttribute("registration", registrationConclusionBean.getRegistration());

        return mapping.findForward("registrationConclusion");
    }

    public ActionForward prepareRegistrationConclusionDocument(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        RenderUtils.invalidateViewState();

        final Registration registration = getAndSetRegistration(request);
        request.setAttribute("registration", registration);

        final Object cycleCurriculumGroupId = getFromRequest(request, "cycleCurriculumGroupId");
        final CycleCurriculumGroup cycleCurriculumGroup = getDomainObject(request, "cycleCurriculumGroupId");
        final RegistrationConclusionBean registrationConclusionBean;
        if (cycleCurriculumGroupId == null) {
            registrationConclusionBean = new RegistrationConclusionBean(registration);
        } else {
            registrationConclusionBean = new RegistrationConclusionBean(registration, cycleCurriculumGroup);
        }
        request.setAttribute("registrationConclusionBean", registrationConclusionBean);

        String degreePresentationName =
                registration.getDegree().getPresentationName(registrationConclusionBean.getConclusionYear());
        request.setAttribute("degreePresentationName", degreePresentationName);

        return mapping.findForward("registrationConclusionDocument");
    }

    public ActionForward viewAttends(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        RenderUtils.invalidateViewState();

        final Registration registration = getAndSetRegistration(request);
        request.setAttribute("registration", registration);

        if (registration != null) {
            final SortedMap<ExecutionSemester, SortedSet<Attends>> attendsMap =
                    new TreeMap<ExecutionSemester, SortedSet<Attends>>();
            for (final Attends attends : registration.getAssociatedAttendsSet()) {
                final ExecutionSemester executionSemester = attends.getExecutionPeriod();
                SortedSet<Attends> attendsSet = attendsMap.get(executionSemester);
                if (attendsSet == null) {
                    attendsSet = new TreeSet<Attends>(Attends.ATTENDS_COMPARATOR);
                    attendsMap.put(executionSemester, attendsSet);
                }
                attendsSet.add(attends);
            }
            request.setAttribute("attendsMap", attendsMap);
        }

        return mapping.findForward("viewAttends");
    }

    public ActionForward prepareAddAttends(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        final Registration registration = getAndSetRegistration(request);
        request.setAttribute("registration", registration);

        AddAttendsBean addAttendsBean = (AddAttendsBean) getObjectFromViewState("addAttendsBean");
        if (addAttendsBean == null) {
            addAttendsBean = new AddAttendsBean();
            final ExecutionSemester executionPeriod = ExecutionSemester.readActualExecutionSemester();
            final ExecutionYear executionYear = executionPeriod.getExecutionYear();
            final Degree degree = registration.getDegree();
            final ExecutionDegree executionDegree = getExecutionDegree(executionYear, degree);

            addAttendsBean.setExecutionPeriod(executionPeriod);
            addAttendsBean.setExecutionYear(executionYear);
            addAttendsBean.setExecutionDegree(executionDegree);
        }
        request.setAttribute("addAttendsBean", addAttendsBean);

        RenderUtils.invalidateViewState();

        return mapping.findForward("addAttends");
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

    public ActionForward addAttends(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final Registration registration = getAndSetRegistration(request);
        request.setAttribute("registration", registration);

        final AddAttendsBean addAttendsBean = (AddAttendsBean) getObjectFromViewState("addAttendsBean");
        final ExecutionCourse executionCourse = addAttendsBean.getExecutionCourse();

        WriteStudentAttendingCourse.runWriteStudentAttendingCourse(registration, executionCourse.getExternalId());

        return viewAttends(mapping, actionForm, request, response);
    }

    public ActionForward deleteAttends(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final Registration registration = getAndSetRegistration(request);
        request.setAttribute("registration", registration);

        final String attendsIdString = request.getParameter("attendsId");
        final Attends attends = FenixFramework.getDomainObject(attendsIdString);

        try {
            registration.removeAttendFor(attends.getExecutionCourse());
        } catch (final DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
        }

        return viewAttends(mapping, actionForm, request, response);
    }

    public ActionForward deleteShiftEnrolments(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final Registration registration = getAndSetRegistration(request);
        request.setAttribute("registration", registration);

        final String attendsIdString = request.getParameter("attendsId");
        final Attends attends = FenixFramework.getDomainObject(attendsIdString);

        if (attends != null) {
            attends.deleteShiftEnrolments();
        }

        return viewAttends(mapping, actionForm, request, response);
    }

    public ActionForward showRegimes(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final Registration registration = getAndSetRegistration(request);
        final List<RegistrationRegime> regimes = new ArrayList<RegistrationRegime>(registration.getRegistrationRegimes());

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

    public ActionForward viewApplication(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final Registration registration = getAndSetRegistration(request);
        request.setAttribute("registration", registration);

        return mapping.findForward("view-application");
    }

}
