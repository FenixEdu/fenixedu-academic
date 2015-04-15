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
package org.fenixedu.academic.ui.struts.action.phd.coordinator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Coordinator;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.domain.phd.ManageEnrolmentsBean;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.phd.PhdProgram;
import org.fenixedu.academic.domain.phd.SearchPhdIndividualProgramProcessBean;
import org.fenixedu.academic.domain.phd.alert.AlertService;
import org.fenixedu.academic.domain.phd.candidacy.PhdCandidacyReferee;
import org.fenixedu.academic.domain.phd.email.PhdProgramEmail;
import org.fenixedu.academic.domain.phd.email.PhdProgramEmailBean;
import org.fenixedu.academic.domain.phd.individualProcess.activities.AcceptEnrolments;
import org.fenixedu.academic.domain.phd.individualProcess.activities.RejectEnrolments;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.service.services.caseHandling.ExecuteProcessActivity;
import org.fenixedu.academic.ui.struts.FenixActionForm;
import org.fenixedu.academic.ui.struts.action.coordinator.CoordinatorApplication.CoordinatorPhdApp;
import org.fenixedu.academic.ui.struts.action.phd.CommonPhdIndividualProgramProcessDA;
import org.fenixedu.academic.ui.struts.action.phd.PhdCandidacyPredicateContainer;
import org.fenixedu.academic.ui.struts.action.phd.PhdInactivePredicateContainer;
import org.fenixedu.academic.ui.struts.action.phd.PhdSeminarPredicateContainer;
import org.fenixedu.academic.ui.struts.action.phd.PhdThesisPredicateContainer;
import org.fenixedu.academic.util.predicates.PredicateContainer;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = CoordinatorPhdApp.class, path = "phd-processes", titleKey = "label.coordinator.phdProcesses",
        bundle = "ApplicationResources")
@Mapping(path = "/phdIndividualProgramProcess", module = "coordinator",
        formBeanClass = PhdIndividualProgramProcessDA.PhdEmailProgramForm.class)
@Forwards({ @Forward(name = "manageProcesses", path = "/phd/coordinator/manageProcesses.jsp"),
        @Forward(name = "viewProcess", path = "/phd/coordinator/viewProcess.jsp"),
        @Forward(name = "viewInactiveProcesses", path = "/phd/coordinator/viewInactiveProcesses.jsp"),
        @Forward(name = "searchResults", path = "/phd/coordinator/searchResults.jsp"),
        @Forward(name = "viewAlertMessages", path = "/phd/coordinator/viewAlertMessages.jsp"),
        @Forward(name = "viewAlertMessageArchive", path = "/phd/coordinator/viewAlertMessageArchive.jsp"),
        @Forward(name = "viewAlertMessage", path = "/phd/coordinator/viewAlertMessage.jsp"),
        @Forward(name = "viewProcessAlertMessages", path = "/phd/coordinator/viewProcessAlertMessages.jsp"),
        @Forward(name = "viewProcessAlertMessageArchive", path = "/phd/coordinator/viewProcessAlertMessageArchive.jsp"),
        @Forward(name = "viewCurriculum", path = "/phd/coordinator/viewCurriculum.jsp"),
        @Forward(name = "manageEnrolments", path = "/phd/coordinator/enrolments/manageEnrolments.jsp"),
        @Forward(name = "validateEnrolments", path = "/phd/coordinator/enrolments/validateEnrolments.jsp"),
        @Forward(name = "manageGuidanceDocuments", path = "/phd/coordinator/manageGuidanceDocuments.jsp"),
        @Forward(name = "uploadGuidanceDocument", path = "/phd/coordinator/uploadGuidanceDocument.jsp"),
        @Forward(name = "managePhdEmails", path = "/phd/coordinator/email/managePhdEmails.jsp"),
        @Forward(name = "choosePhdEmailRecipients", path = "/phd/coordinator/email/choosePhdEmailRecipients.jsp"),
        @Forward(name = "prepareSendPhdEmail", path = "/phd/coordinator/email/prepareSendPhdEmail.jsp"),
        @Forward(name = "confirmSendPhdEmail", path = "/phd/coordinator/email/confirmSendPhdEmail.jsp"),
        @Forward(name = "viewPhdEmail", path = "/phd/coordinator/email/viewPhdEmail.jsp"),
        @Forward(name = "viewRefereeLetters", path = "/phd/coordinator/referee/viewRefereeLetters.jsp"),
        @Forward(name = "viewLetter", path = "/phd/coordinator/referee/viewLetter.jsp") })
public class PhdIndividualProgramProcessDA extends CommonPhdIndividualProgramProcessDA {

    private static final PredicateContainer<?>[] CANDIDACY_CATEGORY = { PhdCandidacyPredicateContainer.DELIVERED,
            PhdCandidacyPredicateContainer.PENDING, PhdCandidacyPredicateContainer.APPROVED,
            PhdCandidacyPredicateContainer.CONCLUDED };

    private static final PredicateContainer<?>[] SEMINAR_CATEGORY = { PhdSeminarPredicateContainer.SEMINAR_PROCESS_STARTED,
            PhdSeminarPredicateContainer.AFTER_FIRST_SEMINAR_REUNION };

    private static final PredicateContainer<?>[] THESIS_CATEGORY = { PhdThesisPredicateContainer.PROVISIONAL_THESIS_DELIVERED,
            PhdThesisPredicateContainer.DISCUSSION_SCHEDULED };

    static public class PhdEmailProgramForm extends FenixActionForm {
        private String[] selectedProcesses;

        public String[] getSelectedProcesses() {
            return selectedProcesses;
        }

        public void setSelectedProcesses(String[] selectedProcesses) {
            this.selectedProcesses = selectedProcesses;
        }
    }

    @Override
    @EntryPoint
    public ActionForward manageProcesses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return super.manageProcesses(mapping, form, request, response);
    }

    @Override
    protected SearchPhdIndividualProgramProcessBean initializeSearchBean(HttpServletRequest request) {
        SearchPhdIndividualProgramProcessBean searchBean = new SearchPhdIndividualProgramProcessBean();
        searchBean.setPhdPrograms(getManagedPhdPrograms(request));
        searchBean.setFilterPhdProcesses(false);
        return searchBean;
    }

    private Set<PhdProgram> getManagedPhdPrograms(HttpServletRequest request) {
        final Set<PhdProgram> result = new HashSet<PhdProgram>();
        final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();

        for (final Coordinator coordinator : getLoggedPerson(request).getCoordinatorsSet()) {
            if (coordinator.getExecutionDegree().getDegree().getPhdProgram() != null
                    && coordinator.getExecutionDegree().getExecutionYear() == currentExecutionYear) {
                result.add(coordinator.getExecutionDegree().getDegree().getPhdProgram());
            }
        }

        return result;
    }

    @Override
    protected PhdInactivePredicateContainer getConcludedContainer() {
        return PhdInactivePredicateContainer.CONCLUDED_THIS_YEAR;
    }

    @Override
    protected List<PredicateContainer<?>> getThesisCategory() {
        return Arrays.asList(THESIS_CATEGORY);
    }

    @Override
    protected List<PredicateContainer<?>> getSeminarCategory() {
        return Arrays.asList(SEMINAR_CATEGORY);
    }

    @Override
    protected List<PredicateContainer<?>> getCandidacyCategory() {
        return Arrays.asList(CANDIDACY_CATEGORY);
    }

    // Manage enrolments

    public ActionForward manageEnrolments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final PhdIndividualProgramProcess process = getProcess(request);
        ManageEnrolmentsBean bean = getRenderedObject("manageEnrolmentsBean");

        if (bean == null) {
            bean = new ManageEnrolmentsBean();
            bean.setProcess(process);
            bean.setSemester(ExecutionSemester.readActualExecutionSemester());
        }

        filterEnrolments(bean, process, false);

        request.setAttribute("manageEnrolmentsBean", bean);
        return mapping.findForward("manageEnrolments");
    }

    private void filterEnrolments(ManageEnrolmentsBean bean, PhdIndividualProgramProcess process, boolean filterByTemporary) {
        final StudentCurricularPlan scp = process.getRegistration().getLastStudentCurricularPlan();

        final Collection<Enrolment> enrolmentsPerformedByStudent = new HashSet<Enrolment>();
        final Collection<Enrolment> enrolmentsPerformedByAdminOffice = new HashSet<Enrolment>();

        for (final Enrolment enrolment : scp.getEnrolmentsByExecutionPeriod(bean.getSemester())) {

            if (filterByTemporary && !enrolment.isTemporary()) {
                continue;
            }

            if (isPerformedByStudent(enrolment)) {
                enrolmentsPerformedByStudent.add(enrolment);
            } else {
                enrolmentsPerformedByAdminOffice.add(enrolment);
            }
        }

        bean.setEnrolmentsPerformedByStudent(enrolmentsPerformedByStudent);
        bean.setRemainingEnrolments(enrolmentsPerformedByAdminOffice);
    }

    private boolean isPerformedByStudent(Enrolment enrolment) {
        final Person person = Person.readPersonByUsername(enrolment.getCreatedBy());
        return RoleType.STUDENT.isMember(person.getUser()) && enrolment.getStudent().equals(person.getStudent());
    }

    public ActionForward prepareValidateEnrolments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final PhdIndividualProgramProcess process = getProcess(request);
        ManageEnrolmentsBean bean = getRenderedObject("manageEnrolmentsBean");

        if (bean == null) {
            bean = new ManageEnrolmentsBean();
            bean.setProcess(process);
            setExecutionSemester(request, bean);
        }

        filterEnrolments(bean, process, true);
        request.setAttribute("manageEnrolmentsBean", bean);

        setDefaultMailInformation(bean, getProcess(request));
        return mapping.findForward("validateEnrolments");
    }

    private void setExecutionSemester(HttpServletRequest request, ManageEnrolmentsBean bean) {
        final ExecutionSemester semester = getDomainObject(request, "executionSemesterId");
        if (semester != null) {
            bean.setSemester(semester);
        } else {
            bean.setSemester(ExecutionSemester.readActualExecutionSemester());
        }
    }

    private void setDefaultMailInformation(ManageEnrolmentsBean bean, PhdIndividualProgramProcess process) {
        bean.setMailSubject(AlertService.getSubjectPrefixed(process, "message.phd.enrolments.validation.default.subject"));
        bean.setMailBody(AlertService.getBodyText(process, "message.phd.enrolments.validation.default.body"));
    }

    public ActionForward acceptEnrolments(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final ManageEnrolmentsBean bean = getRenderedObject("manageEnrolmentsBean");
        request.setAttribute("manageEnrolmentsBean", bean);

        try {

            ExecuteProcessActivity.run(getProcess(request), AcceptEnrolments.class, bean);

        } catch (final DomainException e) {
            addErrorMessage(request, e.getMessage(), e.getArgs());
            return mapping.findForward("validateEnrolments");
        }

        RenderUtils.invalidateViewState();
        return redirect(String.format("/phdIndividualProgramProcess.do?method=manageEnrolments&processId=%s", getProcess(request)
                .getExternalId()), request);
    }

    public ActionForward rejectEnrolments(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final ManageEnrolmentsBean bean = getRenderedObject("manageEnrolmentsBean");
        request.setAttribute("manageEnrolmentsBean", bean);

        try {

            ExecuteProcessActivity.run(getProcess(request), RejectEnrolments.class, bean);

        } catch (final DomainException e) {
            addErrorMessage(request, e.getMessage(), e.getArgs());
            return mapping.findForward("validateEnrolments");
        }

        RenderUtils.invalidateViewState();

        return redirect(String.format("/phdIndividualProgramProcess.do?method=manageEnrolments&processId=%s", getProcess(request)
                .getExternalId()), request);
    }

    // end of manage enrolments

    // manage emails

    public ActionForward managePhdEmails(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final PhdProgramEmailBean bean = new PhdProgramEmailBean();
        List<PhdProgram> coordinatedPrograms = getCoordinatedPhdPrograms();
        if (coordinatedPrograms.size() == 1) {
            bean.setPhdProgram(coordinatedPrograms.iterator().next());
            bean.setShowProgramsChoice(false);
        }

        request.setAttribute("phdEmailBean", bean);
        return mapping.findForward("managePhdEmails");
    }

    public ActionForward manageEmailBeanPostback(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("phdEmailBean", getRenderedObject("phdEmailBean"));
        return mapping.findForward("managePhdEmails");
    }

    public ActionForward choosePhdEmailRecipients(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PhdProgramEmailBean bean = getRenderedObject("phdEmailBean");

        if (bean == null) {
            bean = new PhdProgramEmailBean((PhdProgram) getDomainObject(request, "phdProgramId"));
        } else {
            setSelectedIndividualProcesses((PhdEmailProgramForm) form, bean);
        }

        request.setAttribute("phdEmailBean", bean);
        request.setAttribute("candidacyCategory", getCandidacyCategory());
        request.setAttribute("seminarCategory", getSeminarCategory());
        request.setAttribute("thesisCategory", getThesisCategory());
        request.setAttribute("concludedThisYearContainer", getConcludedContainer());

        return mapping.findForward("choosePhdEmailRecipients");
    }

    public ActionForward prepareSendPhdEmail(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final PhdProgramEmailBean bean = getRenderedObject("phdEmailBean");

        List<PhdIndividualProgramProcess> selectedIndividual = retrieveSelectedProcesses((PhdEmailProgramForm) form);
        bean.setSelectedElements(selectedIndividual);

        request.setAttribute("phdEmailBean", bean);

        return mapping.findForward("prepareSendPhdEmail");
    }

    public ActionForward confirmSendPhdEmail(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final PhdProgramEmailBean bean = getRenderedObject("phdEmailBean");

        try {

            PhdProgramEmail.validateEmailBean(bean);

        } catch (final DomainException e) {
            addErrorMessage(request, e.getMessage(), e.getArgs());
            request.setAttribute("phdEmailBean", bean);
            return mapping.findForward("prepareSendPhdEmail");
        }

        bean.updateBean();
        request.setAttribute("phdEmailBean", getRenderedObject("phdEmailBean"));

        return mapping.findForward("confirmSendPhdEmail");
    }

    public ActionForward sendPhdEmail(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        PhdProgramEmailBean bean = getRenderedObject("phdEmailBean");
        PhdProgramEmail.createEmail(bean);

        RenderUtils.invalidateViewState("phdEmailBean");

        request.setAttribute("phdEmailBean", new PhdProgramEmailBean());
        return mapping.findForward("managePhdEmails");
    }

    public ActionForward viewPhdEmail(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        PhdProgramEmailBean bean = new PhdProgramEmailBean(getPhdEmail(request));
        request.setAttribute("phdEmailBean", bean);

        return mapping.findForward("viewPhdEmail");
    }

    private PhdProgramEmail getPhdEmail(HttpServletRequest request) {
        return getDomainObject(request, "phdEmailId");
    }

    private void setSelectedIndividualProcesses(PhdEmailProgramForm actionForm, PhdProgramEmailBean bean) {
        List<String> externalIdList = new ArrayList<String>();

        for (PhdIndividualProgramProcess individualProcess : bean.getSelectedElements()) {
            externalIdList.add(individualProcess.getExternalId());
        }

        actionForm.setSelectedProcesses(externalIdList.toArray(new String[0]));
    }

    private List<PhdIndividualProgramProcess> retrieveSelectedProcesses(PhdEmailProgramForm actionForm) {
        List<PhdIndividualProgramProcess> processList = new ArrayList<PhdIndividualProgramProcess>();

        if (actionForm.getSelectedProcesses() == null) {
            return processList;
        }

        for (String externalId : actionForm.getSelectedProcesses()) {
            processList.add((PhdIndividualProgramProcess) FenixFramework.getDomainObject(externalId));
        }

        return processList;
    }

    private List<PhdProgram> getCoordinatedPhdPrograms() {
        List<PhdProgram> programs = new ArrayList<PhdProgram>();

        for (PhdProgram program : Bennu.getInstance().getPhdProgramsSet()) {
            if (program.isCoordinatorFor(AccessControl.getPerson(), ExecutionYear.readCurrentExecutionYear())) {
                programs.add(program);
            }
        }

        return programs;
    }

    /* Referee letters */

    public ActionForward viewRefereeLetters(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        PhdIndividualProgramProcess process = getProcess(request);
        request.setAttribute("process", process);
        return mapping.findForward("viewRefereeLetters");
    }

    public ActionForward viewLetter(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            HttpServletResponse response) {
        PhdIndividualProgramProcess process = getProcess(request);
        PhdCandidacyReferee referee = getDomainObject(request, "refereeId");
        request.setAttribute("process", process);
        request.setAttribute("referee", referee);
        return mapping.findForward("viewLetter");
    }
}