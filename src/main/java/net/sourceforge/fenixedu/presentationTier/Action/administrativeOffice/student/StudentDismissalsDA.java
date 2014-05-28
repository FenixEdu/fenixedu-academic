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
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.dismissal.DeleteCredits;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal.DismissalBean;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal.DismissalBean.DismissalType;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal.DismissalBean.SelectedCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal.DismissalBean.SelectedEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal.DismissalBean.SelectedExternalEnrolment;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.ExternalEnrolment;
import net.sourceforge.fenixedu.injectionCode.IllegalDataAccessException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.bennu.portal.EntryPoint;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/studentDismissals", module = "academicAdministration", formBean = "studentDismissalForm",
        functionality = SearchForStudentsDA.class)
@Forwards({ @Forward(name = "manage", path = "/academicAdminOffice/dismissal/managementDismissals.jsp"),
        @Forward(name = "chooseEquivalents", path = "/academicAdminOffice/dismissal/chooseEquivalents.jsp"),
        @Forward(name = "visualizeRegistration", path = "/academicAdministration/student.do?method=visualizeRegistration"),
        @Forward(name = "chooseDismissalEnrolments", path = "/academicAdminOffice/dismissal/chooseDismissalEnrolments.jsp"),
        @Forward(name = "confirmCreateDismissals", path = "/academicAdminOffice/dismissal/confirmCreateDismissals.jsp") })
public class StudentDismissalsDA extends FenixDispatchAction {

    private StudentCurricularPlan getSCP(final HttpServletRequest request) {
        return getDomainObject(request, "scpID");
    }

    @EntryPoint
    public ActionForward manage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("studentCurricularPlan", getSCP(request));
        return mapping.findForward("manage");
    }

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

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

    protected Collection<SelectedEnrolment> buildStudentEnrolmentsInformation(final DismissalBean dismissalBean) {
        final Collection<SelectedEnrolment> enrolments = new HashSet<SelectedEnrolment>();

        for (final StudentCurricularPlan scp : dismissalBean.getStudent().getAllStudentCurricularPlans()) {

            if (scp.equals(dismissalBean.getStudentCurricularPlan())) {
                continue;
            }

            final List<Enrolment> approvedEnrolments = new ArrayList<Enrolment>(scp.getDismissalApprovedEnrolments());
            Collections.sort(approvedEnrolments, Enrolment.COMPARATOR_BY_EXECUTION_YEAR_AND_NAME_AND_ID);

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
        final ExecutionSemester actualEP = ExecutionSemester.readActualExecutionSemester();

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
            Collection<? extends CurriculumGroup> curricularCoursePossibleGroups =
                    dismissalBean.getStudentCurricularPlan().getCurricularCoursePossibleGroups(
                            selectedCurricularCourse.getCurricularCourse());
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

    public ActionForward stepOne(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("dismissalBean", getRenderedObject());
        return mapping.findForward("chooseDismissalEnrolments");
    }

    public ActionForward stepTwo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
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
        }

        return manage(mapping, form, request, response);
    }

    protected void executeCreateDismissalService(DismissalBean dismissalBean) throws FenixServiceException {
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
            addActionMessage(request, e.getMessage());
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

}
