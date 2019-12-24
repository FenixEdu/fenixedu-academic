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
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumLine;
import org.fenixedu.academic.domain.studentCurriculum.ExternalEnrolment;
import org.fenixedu.academic.domain.studentCurriculum.NoCourseGroupCurriculumGroup;
import org.fenixedu.academic.dto.administrativeOffice.dismissal.CreditsBean;
import org.fenixedu.academic.dto.administrativeOffice.dismissal.DismissalBean;
import org.fenixedu.academic.dto.administrativeOffice.dismissal.DismissalBean.DismissalType;
import org.fenixedu.academic.dto.administrativeOffice.dismissal.DismissalBean.SelectedCurricularCourse;
import org.fenixedu.academic.dto.administrativeOffice.dismissal.DismissalBean.SelectedEnrolment;
import org.fenixedu.academic.dto.administrativeOffice.dismissal.DismissalBean.SelectedExternalEnrolment;
import org.fenixedu.academic.predicate.IllegalDataAccessException;
import org.fenixedu.academic.service.services.administrativeOffice.dismissal.DeleteCredits;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.exceptions.FenixActionException;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

@Mapping(path = "/studentDismissals", module = "academicAdministration", formBean = "studentDismissalForm",
        functionality = SearchForStudentsDA.class)
@Forwards({ @Forward(name = "manage", path = "/academicAdminOffice/dismissal/managementDismissals.jsp"),
        @Forward(name = "chooseEquivalents", path = "/academicAdminOffice/dismissal/chooseEquivalents.jsp"),
        @Forward(name = "visualizeRegistration", path = "/academicAdministration/student.do?method=visualizeRegistration"),
        @Forward(name = "chooseDismissalEnrolments", path = "/academicAdminOffice/dismissal/chooseDismissalEnrolments.jsp"),
        @Forward(name = "confirmCreateDismissals", path = "/academicAdminOffice/dismissal/confirmCreateDismissals.jsp"),
        @Forward(name = "editCredits", path = "/academicAdminOffice/dismissal/editCredits.jsp") })
public class StudentDismissalsDA extends FenixDispatchAction {

    private StudentCurricularPlan getSCP(final HttpServletRequest request) {
        return getDomainObject(request, "scpID");
    }

    @EntryPoint
    public ActionForward manage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        StudentCurricularPlan scp = getSCP(request);
        request.setAttribute("studentCurricularPlan", scp);

        if (!scp.isAllowedToManageEquivalencies()) {
            addActionMessage(request, "error.notAuthorized");
        }
        return mapping.findForward("manage");
    }

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final DismissalBean dismissalBean = createDismissalBean();
        dismissalBean.setStudentCurricularPlan(getSCP(request));

        request.setAttribute("dismissalBean", dismissalBean);
        dismissalBean.setEnrolments(buildStudentEnrolmentsInformation(dismissalBean));
        dismissalBean.setExternalEnrolments(buildStudentExternalEnrolmentsInformation(dismissalBean));

        return mapping.findForward("chooseDismissalEnrolments");
    }

    protected DismissalBean createDismissalBean() {
        return new DismissalBean();
    }

    protected Collection<SelectedExternalEnrolment> buildStudentExternalEnrolmentsInformation(final DismissalBean dismissalBean) {
        final Collection<SelectedExternalEnrolment> externalEnrolments = new HashSet<SelectedExternalEnrolment>();
        for (final ExternalEnrolment externalEnrolment : dismissalBean.getStudentCurricularPlan().getRegistration().getStudent()
                .getSortedExternalEnrolments()) {
            externalEnrolments.add(new DismissalBean.SelectedExternalEnrolment(externalEnrolment));
        }
        return externalEnrolments;
    }

    public boolean isAllowedEnrolmentsFromSameCurricularPlan() {
        return false;
    }

    protected Collection<SelectedEnrolment> buildStudentEnrolmentsInformation(final DismissalBean dismissalBean) {
        final Collection<SelectedEnrolment> enrolments = new HashSet<SelectedEnrolment>();

        final Collection<StudentCurricularPlan> scps = dismissalBean.getStudent().getRegistrationsSet().stream()
                .flatMap(r -> r.getStudentCurricularPlansSet().stream()).collect(Collectors.toList());
        for (final StudentCurricularPlan scp : scps) {

            final List<Enrolment> approvedEnrolments = new ArrayList<Enrolment>(scp.getDismissalApprovedEnrolments());
            Collections.sort(approvedEnrolments, Enrolment.COMPARATOR_BY_EXECUTION_YEAR_AND_NAME_AND_ID);

            if (scp.equals(dismissalBean.getStudentCurricularPlan())) {

                // include no course group enrolment of the same plan to insert creditation
                if (isAllowedEnrolmentsFromSameCurricularPlan()) {

                    final Collection<NoCourseGroupCurriculumGroup> groups = scp.getNoCourseGroupCurriculumGroups();

                    for (final NoCourseGroupCurriculumGroup group : groups) {
                        for (final CurriculumLine curriculumLine : group.getApprovedCurriculumLines()) {
                            if (!curriculumLine.isEnrolment()) {
                                continue;
                            }

                            enrolments.add(new DismissalBean.SelectedEnrolment((Enrolment) curriculumLine));
                        }
                    }

                }

                continue;
            }

            for (final Enrolment enrolment : approvedEnrolments) {
                enrolments.add(new DismissalBean.SelectedEnrolment(enrolment));
            }
        }
        return enrolments;
    }

    public ActionForward chooseEquivalents(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final DismissalBean dismissalBean = getRenderedObject("dismissalBean");
        dismissalBean.setDismissalType(DismissalType.CURRICULAR_COURSE_CREDITS);

        final DegreeCurricularPlan dcp = dismissalBean.getStudentCurricularPlan().getDegreeCurricularPlan();
        final ExecutionInterval actualEP = ExecutionInterval.findFirstCurrentChild(dcp.getDegree().getCalendar());

        dismissalBean.setExecutionPeriod(dcp.hasExecutionDegreeFor(actualEP.getExecutionYear()) ? actualEP : dcp
                .getMostRecentExecutionYear().getFirstExecutionPeriod());

        request.setAttribute("dismissalBean", dismissalBean);

        return mapping.findForward("chooseEquivalents");
    }

    public ActionForward dismissalTypePostBack(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final DismissalBean dismissalBean = getRenderedObject();
        dismissalBean.setCredits(null);
        dismissalBean.setCourseGroup(null);
        dismissalBean.setDismissals(null);
        dismissalBean.setCurriculumGroup(null);

        RenderUtils.invalidateViewState();
        request.setAttribute("dismissalBean", dismissalBean);

        return mapping.findForward("chooseEquivalents");
    }

    private void checkArguments(HttpServletRequest request, DismissalBean dismissalBean) throws FenixActionException {
        if (dismissalBean.getDismissalType() == DismissalType.CURRICULAR_COURSE_CREDITS) {
            if (!dismissalBean.hasAnyDismissals() && !dismissalBean.hasAnyOptionalDismissals()) {
                addActionMessage("error", request, "error.studentDismissal.curricularCourse.required");
                throw new FenixActionException();
            }
        } else if (dismissalBean.getDismissalType() == DismissalType.CURRICULUM_GROUP_CREDITS) {
            if (dismissalBean.getCourseGroup() == null) {
                addActionMessage("error", request, "error.studentDismissal.curriculumGroup.required");
                throw new FenixActionException();
            }
        } else {
            if (dismissalBean.getCurriculumGroup() == null) {
                addActionMessage("error", request, "error.studentDismissal.curriculumGroup.required");
                throw new FenixActionException();
            }
        }
    }

    public ActionForward confirmCreateDismissals(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final DismissalBean dismissalBean = getRenderedObject();
        request.setAttribute("dismissalBean", dismissalBean);

        try {
            checkArguments(request, dismissalBean);

        } catch (FenixActionException e) {
            return stepTwo(mapping, actionForm, request, response);
        }

        if (dismissalBean.getDismissalType() == DismissalType.CURRICULAR_COURSE_CREDITS) {
            setCurriculumGroups(dismissalBean);
        }

        return mapping.findForward("confirmCreateDismissals");
    }

    private void setCurriculumGroups(DismissalBean dismissalBean) {
        for (SelectedCurricularCourse selectedCurricularCourse : dismissalBean.getDismissals()) {
            Collection<? extends CurriculumGroup> curricularCoursePossibleGroups = dismissalBean.getStudentCurricularPlan()
                    .getCurricularCoursePossibleGroups(selectedCurricularCourse.getCurricularCourse());
            if (!curricularCoursePossibleGroups.isEmpty()) {
                if (curricularCoursePossibleGroups.size() == 1) {
                    selectedCurricularCourse.setCurriculumGroup(curricularCoursePossibleGroups.iterator().next());
                } else {
                    for (CurriculumGroup curriculumGroup : curricularCoursePossibleGroups) {
                        if (!curriculumGroup.isNoCourseGroupCurriculumGroup()) {
                            selectedCurricularCourse.setCurriculumGroup(curriculumGroup);
                            break;
                        }
                    }
                }
            }
        }
    }

    public ActionForward stepOne(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("dismissalBean", getRenderedObject());
        return mapping.findForward("chooseDismissalEnrolments");
    }

    public ActionForward stepTwo(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("dismissalBean", getRenderedObject());
        return mapping.findForward("chooseEquivalents");
    }

    public ActionForward stepThree(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("dismissalBean", getRenderedObject());
        return mapping.findForward("confirmCreateDismissals");
    }

    public ActionForward createDismissals(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        final DismissalBean dismissalBean = getRenderedObject();

        try {
            checkArguments(request, dismissalBean);
        } catch (FenixActionException e) {
            return stepTwo(mapping, form, request, response);
        }

        try {
            executeCreateDismissalService(dismissalBean);
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
            return confirmCreateDismissals(mapping, form, request, response);
        } catch (NotAuthorizedException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
            return confirmCreateDismissals(mapping, form, request, response);
        }

        return manage(mapping, form, request, response);
    }

    protected void executeCreateDismissalService(DismissalBean dismissalBean)
            throws FenixServiceException, NotAuthorizedException {
        // Do nothing, the dismissals MUST be created in the concrete classes
    }

    public ActionForward deleteCredits(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        final String[] creditsIDs = ((DynaActionForm) form).getStrings("creditsToDelete");
        final StudentCurricularPlan studentCurricularPlan = getSCP(request);

        try {
            DeleteCredits.run(studentCurricularPlan, creditsIDs);
        } catch (final IllegalDataAccessException e) {
            addActionMessage(request, "error.notAuthorized");

        } catch (final DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
        }

        request.setAttribute("studentCurricularPlan", studentCurricularPlan);
        return mapping.findForward("manage");
    }

    public ActionForward backViewRegistration(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("registrationId", getSCP(request).getRegistration().getExternalId().toString());
        return mapping.findForward("visualizeRegistration");
    }

    public ActionForward prepareChooseNotNeedToEnrol(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("dismissalBean", getRenderedObject());
        return mapping.findForward("chooseNotNeedToEnrol");
    }

    public ActionForward prepareEditCredits(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        request.setAttribute("creditsBean", new CreditsBean(getDomainObject(request, "creditsId")));
        return mapping.findForward("editCredits");
    }

    public ActionForward prepareEditCreditsInvalid(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        request.setAttribute("creditsBean", getRenderedObject("creditsBean"));
        return mapping.findForward("editCredits");
    }

    public ActionForward editCredits(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        final CreditsBean bean = getRenderedObject("creditsBean");
        try {
            atomic(() -> {
                bean.getCredits().setOfficialDate(bean.getOfficialDate());
            });

        } catch (DomainException e) {
            addActionMessage("error", request, e.getKey(), e.getArgs());
            request.setAttribute("creditsBean", bean);

            return mapping.findForward("edit");
        }

        request.setAttribute("scpID", bean.getCredits().getStudentCurricularPlan().getExternalId());

        return manage(mapping, form, request, response);

    }

}
