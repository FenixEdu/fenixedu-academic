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
package org.fenixedu.academic.ui.struts.action.coordinator.thesis;

import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.*;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DefaultDocumentGenerator;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.domain.thesis.Thesis;
import org.fenixedu.academic.domain.thesis.ThesisEvaluationParticipant;
import org.fenixedu.academic.domain.thesis.ThesisParticipationType;
import org.fenixedu.academic.report.thesis.ApproveJuryDocument;
import org.fenixedu.academic.report.thesis.StudentThesisIdentificationDocument;
import org.fenixedu.academic.service.services.scientificCouncil.thesis.ApproveThesisProposal;
import org.fenixedu.academic.service.services.thesis.*;
import org.fenixedu.academic.service.services.thesis.ChangeThesisPerson.PersonChange;
import org.fenixedu.academic.service.services.thesis.ChangeThesisPerson.PersonTarget;
import org.fenixedu.academic.ui.struts.action.commons.AbstractManageThesisDA;
import org.fenixedu.academic.ui.struts.action.coordinator.DegreeCoordinatorIndex;
import org.fenixedu.academic.ui.struts.action.coordinator.ThesisSummaryBean;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.FenixFramework;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Mapping(module = "coordinator", path = "/manageThesis", functionality = DegreeCoordinatorIndex.class)
@Forwards({ @Forward(name = "search-student", path = "/coordinator/thesis/searchStudent.jsp"),
        @Forward(name = "review-proposal", path = "/coordinator/thesis/reviewProposal.jsp"),
        @Forward(name = "change-information", path = "/coordinator/thesis/changeInformation.jsp"),
        @Forward(name = "editParticipant", path = "/coordinator/thesis/editParticipant.jsp"),
        @Forward(name = "view-submitted", path = "/coordinator/thesis/viewSubmitted.jsp"),
        @Forward(name = "change-information-with-docs", path = "/coordinator/thesis/changeInformationWithDocs.jsp"),
        @Forward(name = "viewOperationsThesis", path = "/coordinator/thesis/viewOperationsThesis.jsp"),
        @Forward(name = "select-unit", path = "/coordinator/thesis/selectUnit.jsp"),
        @Forward(name = "view-confirmed", path = "/coordinator/thesis/viewConfirmed.jsp"),
        @Forward(name = "list-thesis", path = "/coordinator/thesis/listThesis.jsp"),
        @Forward(name = "select-person", path = "/coordinator/thesis/selectPerson.jsp"),
        @Forward(name = "select-externalPerson", path = "/coordinator/thesis/selectExternalPerson.jsp"),
        @Forward(name = "view-approved", path = "/coordinator/thesis/viewApproved.jsp"),
        @Forward(name = "view-evaluated", path = "/coordinator/thesis/viewEvaluated.jsp"),
        @Forward(name = "collect-basic-information", path = "/coordinator/thesis/collectBasicInformation.jsp"),
        @Forward(name = "edit-thesis", path = "/coordinator/thesis/editThesis.jsp") })
public class ManageThesisDA extends AbstractManageThesisDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DegreeCoordinatorIndex.setCoordinatorContext(request);
        request.setAttribute("degreeCurricularPlan", getDegreeCurricularPlan(request));
        request.setAttribute("thesis", getThesis(request));
        request.setAttribute("student", getStudent(request));

        final ExecutionYear executionYear;
        final ThesisContextBean bean = getRenderedObject("contextBean");
        if (bean == null) {
            executionYear = getExecutionYear(request);
        } else {
            executionYear = bean.getExecutionYear();
        }
        setFilterContext(request, executionYear);

        return super.execute(mapping, actionForm, request, response);
    }

    private void setFilterContext(HttpServletRequest request, ExecutionYear executionYear) {
        request.setAttribute("executionYear", executionYear);
        request.setAttribute("executionYearId", executionYear == null ? "" : executionYear.getExternalId());
    }

    @Override
    protected Thesis getThesis(HttpServletRequest request) {
        Thesis thesis = (Thesis) request.getAttribute("thesis");

        if (thesis != null) {
            return thesis;
        } else {
            return FenixFramework.getDomainObject(request.getParameter("thesisID"));
        }
    }

    private Enrolment getEnrolment(HttpServletRequest request) {
        return FenixFramework.getDomainObject(request.getParameter("enrolmentOID"));
    }

    private Student getStudent(HttpServletRequest request) {
        Student student = (Student) request.getAttribute("student");

        if (student != null) {
            return student;
        } else {
            return FenixFramework.getDomainObject(request.getParameter("studentID"));
        }
    }

    private ExecutionYear getExecutionYear(HttpServletRequest request) {
        String id = request.getParameter("executionYearId");
        if (id == null) {
            id = request.getParameter("executionYear");
        }
        if (id == null) {
            TreeSet<ExecutionYear> executionYears = new TreeSet<ExecutionYear>(new ReverseComparator());
            executionYears.addAll(getDegreeCurricularPlan(request).getExecutionYears());

            if (executionYears.isEmpty()) {
                return ExecutionYear.readCurrentExecutionYear();
            } else {
                return executionYears.first();
            }
        } else {
            return FenixFramework.getDomainObject(id);
        }
    }

    private ThesisContextBean getContextBean(HttpServletRequest request) {
        ThesisContextBean bean = getRenderedObject("contextBean");
        RenderUtils.invalidateViewState("contextBean");

        if (bean != null) {
            return bean;
        } else {
            ExecutionYear executionYear = getExecutionYear(request);

            if (executionYear == null) {
                executionYear = ExecutionYear.readCurrentExecutionYear();
            }

            TreeSet<ExecutionYear> executionYears = new TreeSet<ExecutionYear>(new ReverseComparator());
            executionYears.addAll(getDegreeCurricularPlan(request).getExecutionYears());

            return new ThesisContextBean(executionYears, executionYear);
        }
    }

    public ActionForward selectStudent(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ThesisBean bean = getRenderedObject("student");
        if (bean == null) {
            return searchStudent(mapping, actionForm, request, response);
        } else {
            request.setAttribute("bean", bean);
        }

        Student student = bean.getStudent();

        if (student == null) {
            addActionMessage(request, "thesis.selectStudent.notFound");
            return mapping.findForward("search-student");
        } else {
            DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);

            if (student.isCurrentlyEnroled(degreeCurricularPlan)) {
                Enrolment enrolment = student.getDissertationEnrolment(degreeCurricularPlan);

                if (enrolment != null) {
                    Thesis thesis = enrolment.getThesis();

                    if (thesis == null) {
                        request.setAttribute("proposeStartProcess", true);
                        return mapping.findForward("search-student");
                    } else {
                        request.setAttribute("hasThesis", true);
                        request.setAttribute("thesis", thesis);
                        return mapping.findForward("search-student");
                    }
                } else {
                    addActionMessage(request, "thesis.selectStudent.dissertation.notEnroled");
                    return mapping.findForward("search-student");
                }
            } else {
                addActionMessage(request, "thesis.selectStudent.degreeCurricularPlan.notEnroled", degreeCurricularPlan.getName());
                return mapping.findForward("search-student");
            }

        }
    }

    public ActionForward viewThesis(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Thesis thesis = getThesis(request);

        if (thesis.isDraft()) {
            return editProposal(mapping, actionForm, request, response);
        }

        if (thesis.isSubmitted()) {
            return viewSubmitted(mapping, actionForm, request, response);
        }

        if (thesis.isWaitingConfirmation()) {
            return viewApproved(mapping, actionForm, request, response);
        }

        if (thesis.isConfirmed()) {
            return viewConfirmed(mapping, actionForm, request, response);
        }

        if (thesis.isEvaluated()) {
            return viewEvaluated(mapping, actionForm, request, response);
        }

        return searchStudent(mapping, actionForm, request, response);
    }

    public ThesisPresentationState getFilterFromRequest(HttpServletRequest request) {
        String filter = request.getParameter("filter");
        return filter != null && !filter.isEmpty() && !filter.equals("null") ? ThesisPresentationState.valueOf(filter) : null;
    }

    public ActionForward listThesis(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
        ThesisContextBean bean = getContextBean(request);
        ThesisPresentationState filter = bean.getPresentationState();
        if (filter == null) {
            filter = getFilterFromRequest(request);
            bean.setPresentationState(filter);
        }
        request.setAttribute("filter", (filter != null) ? filter : "null");

        List<StudentThesisInfo> result = new ArrayList<StudentThesisInfo>();
        for (CurricularCourse curricularCourse : degreeCurricularPlan.getDissertationCurricularCourses(bean.getExecutionYear())) {
            for (Enrolment enrolment : curricularCourse.getEnrolmentsByExecutionYear(bean.getExecutionYear())) {
                StudentCurricularPlan studentCurricularPlan = enrolment.getStudentCurricularPlan();

                if (studentCurricularPlan.getDegreeCurricularPlan() != degreeCurricularPlan) {
                    continue;
                }
                final Thesis thesis = enrolment.getThesis();
                if (filter != null) {
                    final ThesisPresentationState state = ThesisPresentationState.getThesisPresentationState(thesis);
                    if (!state.equals(filter)) {
                        continue;
                    }
                }
                result.add(new StudentThesisInfo(enrolment));
            }
        }

        request.setAttribute("theses", result);
        request.setAttribute("contextBean", bean);

        ExecutionDegree executionDegree =
                degreeCurricularPlan.getExecutionDegreeByYear(bean.getExecutionYear().getPreviousExecutionYear());

        if (executionDegree != null) {
            request.setAttribute("summary", new ThesisSummaryBean(executionDegree, degreeCurricularPlan));
        }

        return mapping.findForward("list-thesis");
    }

    public ActionForward prepareCreateProposal(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Student student = getStudent(request);

        if (student == null) {
            return listThesis(mapping, actionForm, request, response);
        }

        // Enrolment enrolment = student.getDissertationEnrolment();
        Enrolment enrolment = getEnrolment(request);
        Thesis thesis = getThesis(request);

        if (thesis == null) {
            thesis = enrolment.getPossibleThesis();
        }
        ThesisBean bean = new ThesisBean();
        bean.setStudent(student);
        fillLastThesisInfo(bean, student, enrolment);
        request.setAttribute("bean", bean);
        return mapping.findForward("collect-basic-information");
    }

    private void fillLastThesisInfo(final ThesisBean bean, final Student student, final Enrolment enrolment) {
        final SortedSet<Enrolment> dissertationEnrolments = student.getDissertationEnrolments(null);
        dissertationEnrolments.remove(enrolment);
        if (!dissertationEnrolments.isEmpty()) {
            final Thesis previous = findPreviousThesis(dissertationEnrolments);
            if (previous != null) {
                bean.setTitle(previous.getTitle());
                return;
            }
        }
    }

    private Thesis findPreviousThesis(final SortedSet<Enrolment> dissertationEnrolments) {
        if (dissertationEnrolments.isEmpty()) {
            return null;
        }
        final Enrolment previous = dissertationEnrolments.last();
        if (previous != null) {
            if (!previous.getThesesSet().isEmpty()) {
                return previous.getThesis();
            } else {
                dissertationEnrolments.remove(previous);
                return findPreviousThesis(dissertationEnrolments);
            }
        }
        return null;
    }

    public ActionForward createProposal(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);

        ThesisBean bean = getRenderedObject("bean");
        RenderUtils.invalidateViewState("bean");

        if (bean == null) {
            return selectStudent(mapping, actionForm, request, response);
        }

        try {
            Thesis thesis = CreateThesisProposal.run(degreeCurricularPlan, bean.getStudent(), bean.getTitle(), bean.getComment());
            request.setAttribute("thesis", thesis);
        } catch (DomainException e) {
            addActionMessage("error", request, e.getKey(), e.getArgs());
            return listThesis(mapping, actionForm, request, response);
        }

        return editProposal(mapping, actionForm, request, response);
    }

    // Draft

    @Override
    public ActionForward editProposal(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Thesis thesis = getThesis(request);

        if (thesis == null) {
            return listThesis(mapping, actionForm, request, response);
        }

        request.setAttribute("conditions", thesis.getConditions());

        return mapping.findForward("edit-thesis");
    }

    public ActionForward editProposalWithDocs(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Thesis thesis = getThesis(request);

        if (thesis == null) {
            return listThesis(mapping, actionForm, request, response);
        }

        return viewApproved(mapping, actionForm, request, response);
    }

    public ActionForward editProposalDiscussion(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("changeDiscussion", true);
        return editProposal(mapping, actionForm, request, response);
    }

    public ActionForward changeInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return mapping.findForward("change-information");
    }

    public ActionForward changeInformationWithDocs(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return mapping.findForward("change-information-with-docs");
    }

    public ActionForward changeCredits(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String target = request.getParameter("target");

        if (target == null) {
            return editProposal(mapping, actionForm, request, response);
        }

        request.setAttribute("editOrientatorCreditsDistribution", target);

        return editProposal(mapping, actionForm, request, response);
    }

    public ActionForward changeParticipationInfo(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String target = request.getParameter("target");

        if (target == null) {
            return editProposal(mapping, actionForm, request, response);
        }

        ThesisEvaluationParticipant participant = FenixFramework.getDomainObject(target);

        PersonTarget targetType = getPersonTarget(participant.getType());

        request.setAttribute("targetType", targetType);
        request.setAttribute("participant", participant);
        return mapping.findForward("editParticipant");
    }

    private PersonTarget getPersonTarget(ThesisParticipationType type) {
        if (type.equals(ThesisParticipationType.ORIENTATOR)) {
            return PersonTarget.orientator;
        }

        if (type.equals(ThesisParticipationType.COORIENTATOR)) {
            return PersonTarget.coorientator;
        }

        if (type.equals(ThesisParticipationType.PRESIDENT)) {
            return PersonTarget.president;
        }

        if (type.equals(ThesisParticipationType.VOWEL)) {
            return PersonTarget.vowel;
        }

        return null;
    }

    public ActionForward changePerson(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String target = request.getParameter("target");
        boolean remove = request.getParameter("remove") != null;

        if (target == null) {
            return editProposal(mapping, actionForm, request, response);
        }

        Thesis thesis = getThesis(request);
        ThesisBean bean = new ThesisBean(thesis);

        Degree degree = getDegreeCurricularPlan(request).getDegree();
        bean.setDegree(degree);

        PersonTarget targetType = PersonTarget.valueOf(target);
        bean.setTargetType(targetType);

        if (targetType.equals(PersonTarget.vowel)) {
            ThesisEvaluationParticipant targetVowel = getVowel(request);

            if (targetVowel != null) {
                bean.setTarget(targetVowel);
            } else {
                bean.setTarget(null);
            }
        }

        if (remove) {
            DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
            ChangeThesisPerson.run(degreeCurricularPlan, thesis, new PersonChange(bean.getTargetType(), null, bean.getTarget()));

            return editProposal(mapping, actionForm, request, response);
        } else {
            request.setAttribute("bean", bean);
            return mapping.findForward("select-person");
        }
    }

    public ActionForward addExternal(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String target = request.getParameter("target");
        boolean remove = request.getParameter("remove") != null;

        if (target == null) {
            return editProposal(mapping, actionForm, request, response);
        }

        Thesis thesis = getThesis(request);
        ThesisBean bean = new ThesisBean(thesis);

        Degree degree = getDegreeCurricularPlan(request).getDegree();
        bean.setDegree(degree);

        PersonTarget targetType = PersonTarget.valueOf(target);
        bean.setTargetType(targetType);

        if (targetType.equals(PersonTarget.vowel)) {
            ThesisEvaluationParticipant targetVowel = getVowel(request);

            if (targetVowel != null) {
                bean.setTarget(targetVowel);
            } else {
                bean.setTarget(null);
            }
        }

        if (remove) {
            DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
            ChangeThesisPerson.run(degreeCurricularPlan, thesis, new PersonChange(bean.getTargetType(), null, bean.getTarget()));

            return editProposal(mapping, actionForm, request, response);
        } else {
            request.setAttribute("bean", bean);
            return mapping.findForward("select-externalPerson");
        }
    }

    public ActionForward deleteParticipant(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String target = request.getParameter("target");

        ThesisEvaluationParticipant participant = FenixFramework.getDomainObject(target);

        ChangeThesisPerson.remove(participant);

        return editProposal(mapping, actionForm, request, response);
    }

    private ThesisEvaluationParticipant getVowel(HttpServletRequest request) {
        String parameter = request.getParameter("vowelID");
        if (parameter == null) {
            return null;
        }

        String id = parameter;

        Thesis thesis = getThesis(request);
        for (ThesisEvaluationParticipant participant : thesis.getVowels()) {
            if (participant.getExternalId().equals(id)) {
                return participant;
            }
        }

        return null;
    }

    public ActionForward changePersonInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ThesisBean bean = getRenderedObject("bean");

        if (bean == null) {
            return editProposal(mapping, actionForm, request, response);
        }

        request.setAttribute("bean", bean);
        return mapping.findForward("select-person");
    }

    public ActionForward submitProposal(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Thesis thesis = getThesis(request);

        if (thesis == null) {
            return listThesis(mapping, actionForm, request, response);
        }

        try {
            DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
            SubmitThesis.run(degreeCurricularPlan, thesis);
        } catch (DomainException e) {
            addActionMessage("error", request, e.getKey(), e.getArgs());
            return editProposal(mapping, actionForm, request, response);
        }

        return listThesis(mapping, actionForm, request, response);
    }

    public ActionForward confirmDeleteProposal(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("confirmDelete", true);
        return editProposal(mapping, actionForm, request, response);
    }

    public ActionForward deleteProposal(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Thesis thesis = getThesis(request);

        if (thesis == null) {
            return listThesis(mapping, actionForm, request, response);
        }

        try {
            DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
            DeleteThesis.run(degreeCurricularPlan, thesis);
        } catch (DomainException e) {
            addActionMessage("error", request, e.getKey(), e.getArgs());
        }

        return listThesis(mapping, actionForm, request, response);
    }

    // Submitted

    public ActionForward viewSubmitted(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Thesis thesis = getThesis(request);

        if (thesis == null) {
            return listThesis(mapping, actionForm, request, response);
        }

        return mapping.findForward("view-submitted");
    }

    public ActionForward cancelApprovalRequest(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Thesis thesis = getThesis(request);

        if (thesis == null) {
            return listThesis(mapping, actionForm, request, response);
        }

        try {
            DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
            CancelSubmitThesis.run(degreeCurricularPlan, thesis);
        } catch (DomainException e) {
            addActionMessage("error", request, e.getKey(), e.getArgs());
            return viewSubmitted(mapping, actionForm, request, response);
        }

        return listThesis(mapping, actionForm, request, response);
    }

    // Approved

    public ActionForward viewApproved(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return mapping.findForward("view-approved");
    }

    public ActionForward printApprovalDocument(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Thesis thesis = getThesis(request);

        try {
            ApproveJuryDocument document = new ApproveJuryDocument(thesis);
            byte[] data = DefaultDocumentGenerator.getGenerator().generateReport(Collections.singletonList(document));
            response.setContentLength(data.length);
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", String.format("attachment; filename=%s.pdf", document.getReportFileName()));
            response.getOutputStream().write(data);

            return null;
        } catch (Exception e) {
            addActionMessage("error", request, "coordinator.thesis.approved.print.failed");
            return viewSubmitted(mapping, actionForm, request, response);
        }
    }

    // Confirmed

    public ActionForward viewConfirmed(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return mapping.findForward("view-confirmed");
    }

    public ActionForward confirmRevision(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("confirmRevision", true);
        return viewConfirmed(mapping, actionForm, request, response);
    }

    public ActionForward enterRevision(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Thesis thesis = getThesis(request);

        if (thesis == null) {
            return listThesis(mapping, actionForm, request, response);
        }

        try {
            ReviseThesis.run(thesis);
        } catch (DomainException e) {
            addActionMessage("error", request, e.getKey(), e.getArgs());
        }

        return listThesis(mapping, actionForm, request, response);
    }

    // Evaluated

    public ActionForward viewEvaluated(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return mapping.findForward("view-evaluated");
    }

    public ActionForward approveProposal(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Thesis thesis = getThesis(request);

        if (thesis != null) {
            ApproveThesisProposal.runApproveThesisProposal(thesis);
            addActionMessage("mail", request, "thesis.approved.mail.sent");
            addActionMessage("nextAction", request, "thesis.approved.next.action");
        }

        return listThesis(mapping, actionForm, request, response);
    }

    public ActionForward confirmRejectProposal(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("confirmReject", true);
        return reviewProposal(mapping, actionForm, request, response);
    }

    public ActionForward reviewProposal(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return mapping.findForward("review-proposal");
    }

    public ActionForward downloadIdentificationSheet(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Thesis thesis = getThesis(request);

        try {
            StudentThesisIdentificationDocument document = new StudentThesisIdentificationDocument(thesis);
            byte[] data = DefaultDocumentGenerator.getGenerator().generateReport(Collections.singletonList(document));
            response.setContentLength(data.length);
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", String.format("attachment; filename=%s.pdf", document.getReportFileName()));

            response.getOutputStream().write(data);

            return null;
        } catch (Exception e) {
            addActionMessage("error", request, "student.thesis.generate.identification.failed");
            e.printStackTrace();//FIXME remove
            return listThesis(mapping, actionForm, request, response);
        }
    }

}