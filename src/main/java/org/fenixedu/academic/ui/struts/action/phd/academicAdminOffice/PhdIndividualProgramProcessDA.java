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
package org.fenixedu.academic.ui.struts.action.phd.academicAdminOffice;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.*;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicAccessRule;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.phd.*;
import org.fenixedu.academic.domain.phd.alert.PhdAlert;
import org.fenixedu.academic.domain.phd.alert.PhdAlertMessage;
import org.fenixedu.academic.domain.phd.alert.PhdCustomAlertBean;
import org.fenixedu.academic.domain.phd.candidacy.RegistrationFormalizationBean;
import org.fenixedu.academic.domain.phd.email.PhdEmailBean;
import org.fenixedu.academic.domain.phd.email.PhdIndividualProgramProcessEmail;
import org.fenixedu.academic.domain.phd.email.PhdIndividualProgramProcessEmailBean;
import org.fenixedu.academic.domain.phd.email.PhdIndividualProgramProcessEmailBean.PhdEmailParticipantsGroup;
import org.fenixedu.academic.domain.phd.individualProcess.activities.*;
import org.fenixedu.academic.domain.phd.migration.*;
import org.fenixedu.academic.domain.phd.migration.common.exceptions.PhdMigrationException;
import org.fenixedu.academic.domain.phd.reports.EPFLCandidatesReport;
import org.fenixedu.academic.domain.phd.reports.PhdGuidersReport;
import org.fenixedu.academic.domain.phd.reports.PhdIndividualProgramProcessesReport;
import org.fenixedu.academic.domain.phd.reports.RecommendationLetterReport;
import org.fenixedu.academic.domain.phd.thesis.PhdThesisProcessBean;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DefaultDocumentGenerator;
import org.fenixedu.academic.dto.person.PersonBean;
import org.fenixedu.academic.dto.person.PhotographUploadBean;
import org.fenixedu.academic.dto.person.PhotographUploadBean.UnableToProcessTheImage;
import org.fenixedu.academic.report.phd.registration.PhdSchoolRegistrationDeclarationDocument;
import org.fenixedu.academic.service.services.caseHandling.ExecuteProcessActivity;
import org.fenixedu.academic.service.services.fileManager.StorePersonalPhoto;
import org.fenixedu.academic.service.services.phd.CreateEnrolmentPeriods;
import org.fenixedu.academic.ui.struts.action.academicAdministration.AcademicAdministrationApplication.AcademicAdminPhdApp;
import org.fenixedu.academic.ui.struts.action.exceptions.FenixActionException;
import org.fenixedu.academic.ui.struts.action.phd.CommonPhdIndividualProgramProcessDA;
import org.fenixedu.academic.ui.struts.action.phd.PhdInactivePredicateContainer;
import org.fenixedu.academic.ui.struts.action.phd.PhdProcessStateBean;
import org.fenixedu.academic.util.ContentType;
import org.fenixedu.academic.util.predicates.AndPredicate;
import org.fenixedu.academic.util.predicates.PredicateContainer;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;

@StrutsFunctionality(app = AcademicAdminPhdApp.class, path = "phd-processes", titleKey = "label.phd.manageProcesses",
        accessGroup = "academic(MANAGE_PHD_PROCESSES)")
@Mapping(path = "/phdIndividualProgramProcess", module = "academicAdministration")
@Forwards({
        @Forward(name = "manageProcesses", path = "/phd/academicAdminOffice/manageProcesses.jsp"),
        @Forward(name = "viewProcess", path = "/phd/academicAdminOffice/viewProcess.jsp"),
        @Forward(name = "editPersonalInformation", path = "/phd/academicAdminOffice/editPersonalInformation.jsp"),
        @Forward(name = "editQualificationsAndJobsInformation",
                path = "/phd/academicAdminOffice/editQualificationsAndJobsInformation.jsp"),
        @Forward(name = "editPhdIndividualProgramProcessInformation",
                path = "/phd/academicAdminOffice/editPhdIndividualProgramProcessInformation.jsp"),
        @Forward(name = "manageGuidingInformation", path = "/phd/academicAdminOffice/manageGuidingInformation.jsp"),
        @Forward(name = "managePhdIndividualProgramProcessState",
                path = "/phd/academicAdminOffice/managePhdIndividualProgramProcessState.jsp"),
        @Forward(name = "manageAlerts", path = "/phd/academicAdminOffice/manageAlerts.jsp"),
        @Forward(name = "createCustomAlert", path = "/phd/academicAdminOffice/createCustomAlert.jsp"),
        @Forward(name = "viewAlertMessages", path = "/phd/academicAdminOffice/viewAlertMessages.jsp"),
        @Forward(name = "viewAlertMessageArchive", path = "/phd/academicAdminOffice/viewAlertMessageArchive.jsp"),
        @Forward(name = "viewAlertMessage", path = "/phd/academicAdminOffice/viewAlertMessage.jsp"),
        @Forward(name = "viewProcessAlertMessages", path = "/phd/academicAdminOffice/viewProcessAlertMessages.jsp"),
        @Forward(name = "viewProcessAlertMessageArchive", path = "/phd/academicAdminOffice/viewProcessAlertMessageArchive.jsp"),
        @Forward(name = "manageStudyPlan", path = "/phd/academicAdminOffice/manageStudyPlan.jsp"),
        @Forward(name = "createStudyPlan", path = "/phd/academicAdminOffice/createStudyPlan.jsp"),
        @Forward(name = "editStudyPlan", path = "/phd/academicAdminOffice/editStudyPlan.jsp"),
        @Forward(name = "createStudyPlanEntry", path = "/phd/academicAdminOffice/createStudyPlanEntry.jsp"),
        @Forward(name = "editQualificationExams", path = "/phd/academicAdminOffice/editQualificationExams.jsp"),
        @Forward(name = "uploadPhoto", path = "/phd/academicAdminOffice/uploadPhoto.jsp"),
        @Forward(name = "requestPublicPresentationSeminarComission",
                path = "/phd/academicAdminOffice/requestPublicPresentationSeminarComission.jsp"),
        @Forward(name = "exemptPublicPresentationSeminarComission",
                path = "/phd/academicAdminOffice/exemptPublicPresentationSeminarComission.jsp"),
        @Forward(name = "requestPublicThesisPresentation", path = "/phd/academicAdminOffice/requestPublicThesisPresentation.jsp"),
        @Forward(name = "viewCurriculum", path = "/phd/academicAdminOffice/viewCurriculum.jsp"),
        @Forward(name = "manageEnrolmentPeriods", path = "/phd/academicAdminOffice/periods/manageEnrolmentPeriods.jsp"),
        @Forward(name = "createEnrolmentPeriod", path = "/phd/academicAdminOffice/periods/createEnrolmentPeriod.jsp"),
        @Forward(name = "editEnrolmentPeriod", path = "/phd/academicAdminOffice/periods/editEnrolmentPeriod.jsp"),
        @Forward(name = "editWhenStartedStudies", path = "/phd/academicAdminOffice/editWhenStartedStudies.jsp"),
        @Forward(name = "managePhdIndividualProcessConfiguration",
                path = "/phd/academicAdminOffice/configuration/managePhdIndividualProcessConfiguration.jsp"),
        @Forward(name = "managePhdIndividualProcessEmails", path = "/phd/academicAdminOffice/viewProcessEmails.jsp"),
        @Forward(name = "sendPhdIndividualProcessEmail", path = "/phd/academicAdminOffice/sendPhdEmail.jsp"),
        @Forward(name = "viewPhdIndividualProcessEmail", path = "/phd/academicAdminOffice/viewPhdEmail.jsp"),
        @Forward(name = "viewMigrationProcess", path = "/phd/academicAdminOffice/viewMigrationProcess.jsp"),
        @Forward(name = "viewAllMigratedProcesses", path = "/phd/academicAdminOffice/viewAllMigratedProcesses.jsp"),
        @Forward(name = "viewPhdParticipants", path = "/phd/academicAdminOffice/participant/viewPhdParticipants.jsp"),
        @Forward(name = "editPhdParticipant", path = "/phd/academicAdminOffice/participant/editPhdParticipant.jsp"),
        @Forward(name = "chooseProcessToTransfer", path = "/phd/academicAdminOffice/transfer/chooseProcessToTransfer.jsp"),
        @Forward(name = "fillRemarksOnTransfer", path = "/phd/academicAdminOffice/transfer/fillRemarksOnTransfer.jsp"),
        @Forward(name = "editCandidacyProcessData",
                path = "/phd/academicAdminOffice/manualMigration/editCandidacyProcessData.jsp"),
        @Forward(name = "editPersonalData", path = "/phd/academicAdminOffice/manualMigration/editPersonalData.jsp"),
        @Forward(name = "verifyChosenCandidate", path = "/phd/academicAdminOffice/manualMigration/verifyChosenCandidate.jsp"),
        @Forward(name = "createManualMigrationCandidacy",
                path = "/phd/academicAdminOffice/manualMigration/createManualMigrationCandidacy.jsp"),
        @Forward(name = "concludeManualMigration", path = "/phd/academicAdminOffice/manualMigration/concludeManualMigration.jsp"),
        @Forward(name = "dissociateRegistration", path = "/phd/academicAdminOffice/dissociateRegistration.jsp"),
        @Forward(name = "uploadGuidanceAcceptanceDocument",
                path = "/phd/academicAdminOffice/participant/guidance/uploadGuidanceAcceptanceDocument.jsp"),
        @Forward(name = "editPhdProcessState", path = "/phd/academicAdminOffice/editState.jsp"),
        @Forward(name = "viewAllAlertMessages", path = "/phd/academicAdminOffice/alerts/viewAllAlertMessages.jsp"),
        @Forward(name = "viewAlertMessageFromAllAlertMessages", path = "/phd/academicAdminOffice/alerts/viewAlertMessage.jsp"),
        @Forward(name = "viewLogs", path = "/phd/academicAdminOffice/logs/viewLogs.jsp") })
public class PhdIndividualProgramProcessDA extends CommonPhdIndividualProgramProcessDA {

    // These methods will not be needed while we're using the old interface that
    // does not require predicate containers
    @Override
    protected PhdInactivePredicateContainer getConcludedContainer() {
        return null;
    }

    @Override
    protected List<PredicateContainer<?>> getThesisCategory() {
        return null;
    }

    @Override
    protected List<PredicateContainer<?>> getSeminarCategory() {
        return null;
    }

    @Override
    protected List<PredicateContainer<?>> getCandidacyCategory() {
        return null;
    }

    @Override
    protected SearchPhdIndividualProgramProcessBean initializeSearchBean(HttpServletRequest request) {
        final SearchPhdIndividualProgramProcessBean searchBean = new SearchPhdIndividualProgramProcessBean();
        searchBean.setExecutionYear(ExecutionYear.readCurrentExecutionYear());
        searchBean.setFilterPhdPrograms(false);
        searchBean.setFilterPhdProcesses(false);

        return searchBean;
    }

    @Override
    @EntryPoint
    public ActionForward manageProcesses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        SearchPhdIndividualProgramProcessBean searchBean =
                (SearchPhdIndividualProgramProcessBean) getObjectFromViewState("searchProcessBean");

        if (searchBean == null) {
            searchBean = initializeSearchBean(request);
        }

        AndPredicate<PhdIndividualProgramProcess> predicate = searchBean.getPredicates();

        predicate.add(new Predicate<PhdIndividualProgramProcess>() {
            @Override
            public boolean test(PhdIndividualProgramProcess process) {
                return process.isAllowedToManageProcess(Authenticate.getUser());
            }
        });

        request.setAttribute("searchProcessBean", searchBean);
        request.setAttribute("processes", PhdIndividualProgramProcess.search(searchBean.getExecutionYear(), predicate));

        RenderUtils.invalidateViewState();

        return mapping.findForward("manageProcesses");
    }

    // Edit Personal Information
    public ActionForward prepareEditPersonalInformation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        Person person = getProcess(request).getPerson();
        final PersonBean personBean = new PersonBean(person);

        /* TODO: UGLY HACK DUE TO PENDING VALIDATION DATA FOR PERSON */
        initPersonBeanUglyHack(personBean, person);

        request.setAttribute("editPersonalInformationBean", personBean);
        return mapping.findForward("editPersonalInformation");
    }

    public ActionForward prepareEditPersonalInformationInvalid(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute("editPersonalInformationBean", getEditPersonalInformationBean());
        return mapping.findForward("editPersonalInformation");
    }

    private PersonBean getEditPersonalInformationBean() {
        return getRenderedObject("editPersonalInformationBean");
    }

    public ActionForward editPersonalInformation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!validateAreaCodeAndAreaOfAreaCode(request, getEditPersonalInformationBean().getPerson(),
                getEditPersonalInformationBean().getCountryOfResidence(), getEditPersonalInformationBean().getAreaCode(),
                getEditPersonalInformationBean().getAreaOfAreaCode())) {

            request.setAttribute("editPersonalInformationBean", getEditPersonalInformationBean());

            return mapping.findForward("editPersonalInformation");
        }

        request.setAttribute("editPersonalInformationBean", getEditPersonalInformationBean());
        return executeActivity(EditPersonalInformation.class, getEditPersonalInformationBean(), request, mapping,
                "editPersonalInformation", "viewProcess", "message.personal.data.edited.with.success");
    }

    public ActionForward cancelEditPersonalInformation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return mapping.findForward("viewProcess");
    }

    // End of Edit Personal Information

    // Edit Qualifications and Jobs information
    private void addQualificationsAndJobsContextInformation(HttpServletRequest request) {
        final Person person = getProcess(request).getPerson();
        request.setAttribute("qualifications", person.getAssociatedQualificationsSet());
        request.setAttribute("jobs", person.getJobsSet());
    }

    public ActionForward prepareEditQualificationsAndJobsInformation(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        addQualificationsAndJobsContextInformation(request);
        return mapping.findForward("editQualificationsAndJobsInformation");
    }

    // Qualifications
    public ActionForward prepareAddQualification(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        addQualificationsAndJobsContextInformation(request);
        request.setAttribute("qualification", new QualificationBean());
        return mapping.findForward("editQualificationsAndJobsInformation");
    }

    public ActionForward addQualification(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        addQualificationsAndJobsContextInformation(request);
        final Object bean = getRenderedObject("qualification");

        try {
            ExecuteProcessActivity.run(getProcess(request), AddQualification.class.getSimpleName(), bean);
            addSuccessMessage(request, "message.qualification.information.create.success");

        } catch (DomainException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());
            request.setAttribute("qualification", bean);
        }
        return mapping.findForward("editQualificationsAndJobsInformation");
    }

    public ActionForward addQualificationInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        addQualificationsAndJobsContextInformation(request);
        request.setAttribute("qualification", getRenderedObject("qualification"));
        return mapping.findForward("editQualificationsAndJobsInformation");
    }

    public ActionForward deleteQualification(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        addQualificationsAndJobsContextInformation(request);
        return executeActivity(DeleteQualification.class, getDomainObject(request, "qualificationId"), request, mapping,
                "editQualificationsAndJobsInformation", "editQualificationsAndJobsInformation",
                "message.qualification.information.delete.success");
    }

    // Jobs
    public ActionForward prepareAddJobInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        addQualificationsAndJobsContextInformation(request);
        final JobBean bean = new JobBean();
        bean.setCountry(Country.readDefault());
        request.setAttribute("job", bean);
        return mapping.findForward("editQualificationsAndJobsInformation");
    }

    public ActionForward addJobInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        addQualificationsAndJobsContextInformation(request);
        final Object bean = getRenderedObject("job");

        try {
            ExecuteProcessActivity.run(getProcess(request), AddJobInformation.class.getSimpleName(), bean);
            addSuccessMessage(request, "message.job.information.create.success");

        } catch (DomainException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());
            request.setAttribute("job", bean);
        }
        return mapping.findForward("editQualificationsAndJobsInformation");
    }

    public ActionForward addJobInformationInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        addQualificationsAndJobsContextInformation(request);
        request.setAttribute("job", getRenderedObject("job"));
        return mapping.findForward("editQualificationsAndJobsInformation");
    }

    public ActionForward addJobInformationPostback(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        addQualificationsAndJobsContextInformation(request);
        request.setAttribute("job", getRenderedObject("job"));
        RenderUtils.invalidateViewState();
        return mapping.findForward("editQualificationsAndJobsInformation");
    }

    public ActionForward deleteJobInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        addQualificationsAndJobsContextInformation(request);
        return executeActivity(DeleteJobInformation.class, getDomainObject(request, "jobId"), request, mapping,
                "editQualificationsAndJobsInformation", "editQualificationsAndJobsInformation",
                "message.job.information.delete.success");
    }

    // End of Qualifications and Jobs information

    // Phd individual program process information
    public ActionForward prepareEditPhdIndividualProgramProcessInformation(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("phdIndividualProgramProcessBean", new PhdIndividualProgramProcessBean(getProcess(request)));
        return mapping.findForward("editPhdIndividualProgramProcessInformation");
    }

    public ActionForward editPhdIndividualProgramProcessInformationInvalid(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("phdIndividualProgramProcessBean", getRenderedObject("phdIndividualProgramProcessBean"));
        return mapping.findForward("editPhdIndividualProgramProcessInformation");
    }

    public ActionForward editPhdIndividualProgramProcessInformationPostback(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("phdIndividualProgramProcessBean", getRenderedObject("phdIndividualProgramProcessBean"));
        RenderUtils.invalidateViewState();
        return mapping.findForward("editPhdIndividualProgramProcessInformation");
    }

    public ActionForward editPhdIndividualProgramProcessInformation(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        final PhdIndividualProgramProcessBean bean = getRenderedObject("phdIndividualProgramProcessBean");
        request.setAttribute("phdIndividualProgramProcessBean", bean);

        if (!bean.isCollaborationInformationCorrect()) {
            addErrorMessage(request, "message.phdIndividualProgramProcessInformation.invalid.collaboration");
            return mapping.findForward("editPhdIndividualProgramProcessInformation");
        }

        try {
            ExecuteProcessActivity.run(getProcess(request), EditIndividualProcessInformation.class.getSimpleName(), bean);
            addSuccessMessage(request, "message.phdIndividualProgramProcessInformation.edit.success");
            return viewProcess(mapping, actionForm, request, response);

        } catch (DomainException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());
            return mapping.findForward("editPhdIndividualProgramProcessInformation");
        }
    }

    // End of Phd individual program process information

    // Phd guiding information
    private void addGuidingsContextInformation(ActionMapping mapping, HttpServletRequest request) {
        request.setAttribute("guidings", getProcess(request).getGuidingsSet());
        request.setAttribute("assistantGuidings", getProcess(request).getAssistantGuidingsSet());
    }

    public ActionForward prepareManageGuidingInformation(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        addGuidingsContextInformation(mapping, request);
        return mapping.findForward("manageGuidingInformation");
    }

    public ActionForward prepareAddGuidingInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        addGuidingsContextInformation(mapping, request);
        request.setAttribute("guidingBean", new PhdParticipantBean(getProcess(request)));
        return mapping.findForward("manageGuidingInformation");
    }

    public ActionForward prepareAddGuidingInformationInvalid(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        addGuidingsContextInformation(mapping, request);
        request.setAttribute("guidingBean", getRenderedObject("guidingBean"));
        return mapping.findForward("manageGuidingInformation");
    }

    public ActionForward prepareAddGuidingInformationSelectType(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        addGuidingsContextInformation(mapping, request);
        request.setAttribute("guidingBean", getRenderedObject("guidingBean"));
        RenderUtils.invalidateViewState();
        return mapping.findForward("manageGuidingInformation");
    }

    public ActionForward addGuidingInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final PhdParticipantBean bean = getRenderedObject("guidingBean");
        try {
            ExecuteProcessActivity.run(getProcess(request), AddGuidingInformation.class.getSimpleName(), bean);
            addSuccessMessage(request, "message.guiding.created.with.success");

        } catch (DomainException e) {
            request.setAttribute("guidingBean", bean);
            addErrorMessage(request, e.getKey(), e.getArgs());
        }

        addGuidingsContextInformation(mapping, request);
        return mapping.findForward("manageGuidingInformation");
    }

    public ActionForward deleteGuiding(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        try {
            ExecuteProcessActivity.run(getProcess(request), DeleteGuiding.class.getSimpleName(),
                    getDomainObject(request, "guidingId"));
            addSuccessMessage(request, "message.guiding.deleted.with.success");

        } catch (DomainException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());
        }

        addGuidingsContextInformation(mapping, request);
        return mapping.findForward("manageGuidingInformation");
    }

    public ActionForward prepareAddAssistantGuidingInformation(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        addGuidingsContextInformation(mapping, request);
        request.setAttribute("assistantGuidingBean", new PhdParticipantBean(getProcess(request)));
        return mapping.findForward("manageGuidingInformation");
    }

    public ActionForward prepareAddAssistantGuidingInformationInvalid(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        addGuidingsContextInformation(mapping, request);
        request.setAttribute("assistantGuidingBean", getRenderedObject("assistantGuidingBean"));
        return mapping.findForward("manageGuidingInformation");
    }

    public ActionForward prepareAddAssistantGuidingInformationSelectType(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        addGuidingsContextInformation(mapping, request);
        request.setAttribute("assistantGuidingBean", getRenderedObject("assistantGuidingBean"));
        RenderUtils.invalidateViewState();
        return mapping.findForward("manageGuidingInformation");
    }

    public ActionForward addAssistantGuidingInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final PhdParticipantBean bean = getRenderedObject("assistantGuidingBean");
        try {
            ExecuteProcessActivity.run(getProcess(request), AddAssistantGuidingInformation.class.getSimpleName(), bean);
            addSuccessMessage(request, "message.assistant.guiding.created.with.success");

        } catch (DomainException e) {
            request.setAttribute("assistantGuidingBean", bean);
            addErrorMessage(request, e.getKey(), e.getArgs());
        }

        addGuidingsContextInformation(mapping, request);
        return mapping.findForward("manageGuidingInformation");
    }

    public ActionForward deleteAssistantGuiding(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        try {
            ExecuteProcessActivity.run(getProcess(request), DeleteAssistantGuiding.class.getSimpleName(),
                    getDomainObject(request, "assistantGuidingId"));
            addSuccessMessage(request, "message.assistant.guiding.deleted.with.success");

        } catch (DomainException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());
        }

        addGuidingsContextInformation(mapping, request);
        return mapping.findForward("manageGuidingInformation");
    }

    // End of Phd guiding information

    // change phd individual program process state

    public ActionForward managePhdIndividualProgramProcessState(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        final PhdIndividualProgramProcessBean bean = new PhdIndividualProgramProcessBean();
        bean.setIndividualProgramProcess(getProcess(request));
        request.setAttribute("processBean", bean);
        return mapping.findForward("managePhdIndividualProgramProcessState");
    }

    public ActionForward managePhdIndividualProgramProcessStateInvalid(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("processBean", getRenderedObject("processBean"));
        return mapping.findForward("managePhdIndividualProgramProcessState");
    }

    public ActionForward changePhdIndividualProgramProcessState(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {

        final PhdIndividualProgramProcessBean bean = getRenderedObject("processBean");

        try {
            switch (bean.getProcessState()) {
            case CANCELLED:
                ExecuteProcessActivity.run(getProcess(request), CancelPhdProgramProcess.class.getSimpleName(), bean);
                break;

            case FLUNKED:
                ExecuteProcessActivity.run(getProcess(request), FlunkedPhdProgramProcess.class.getSimpleName(), bean);
                break;

            case NOT_ADMITTED:
                ExecuteProcessActivity.run(getProcess(request), NotAdmittedPhdProgramProcess.class.getSimpleName(), bean);
                break;

            case SUSPENDED:
                ExecuteProcessActivity.run(getProcess(request), SuspendPhdProgramProcess.class.getSimpleName(), bean);
                break;

            case CANDIDACY:
                ExecuteProcessActivity.run(getProcess(request), ActivatePhdProgramProcessInCandidacyState.class.getSimpleName(),
                        bean);
                break;

            case THESIS_DISCUSSION:
                ExecuteProcessActivity.run(getProcess(request),
                        ActivatePhdProgramProcessInThesisDiscussionState.class.getSimpleName(), bean);
                break;

            case WORK_DEVELOPMENT:
                ExecuteProcessActivity.run(getProcess(request),
                        ActivatePhdProgramProcessInWorkDevelopmentState.class.getSimpleName(), bean);
                break;
            case CONCLUDED:
                ExecuteProcessActivity.run(getProcess(request), ConcludeIndividualProgramProcess.class.getSimpleName(), bean);
                break;
            case ABANDON:
                ExecuteProcessActivity.run(getProcess(request), AbandonIndividualProgramProcess.class.getSimpleName(), bean);
                break;
            default:
                throw new FenixActionException();
            }
        } catch (final DomainException e) {
            addErrorMessage(request, e.getMessage(), e.getArgs());
            return managePhdIndividualProgramProcessState(mapping, actionForm, request, response);
        }

        RenderUtils.invalidateViewState();

        return managePhdIndividualProgramProcessState(mapping, actionForm, request, response);
    }

    // End of change phd individual program process state

    // Alerts Management

    public ActionForward manageAlerts(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("alerts", getAlertsToShow(request));

        return mapping.findForward("manageAlerts");
    }

    public ActionForward prepareCreateCustomAlert(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("createCustomAlertBean", new PhdCustomAlertBean(getProcess(request)));

        return mapping.findForward("createCustomAlert");
    }

    public ActionForward prepareCreateCustomAlertInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("createCustomAlert", getCreateCustomAlertBean());

        return mapping.findForward("createCustomAlert");
    }

    public ActionForward createCustomAlert(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final PhdCustomAlertBean bean = getCreateCustomAlertBean();
        request.setAttribute("createCustomAlertBean", bean);

        final ActionForward result =
                executeActivity(AddCustomAlert.class, bean, request, mapping, "createCustomAlert", "manageAlerts",
                        "message.alert.create.with.success");

        request.setAttribute("alerts", getAlertsToShow(request));

        return result;
    }

    private Set<PhdAlert> getAlertsToShow(HttpServletRequest request) {
        return getProcess(request).getActiveAlerts();
    }

    public ActionForward deleteCustomAlert(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final ActionForward result =
                executeActivity(DeleteCustomAlert.class, getAlert(request), request, mapping, "manageAlerts", "manageAlerts",
                        "message.alert.deleted.with.success");

        request.setAttribute("alerts", getAlertsToShow(request));

        return result;

    }

    private Alert getAlert(HttpServletRequest request) {
        return getDomainObject(request, "alertId");
    }

    private PhdCustomAlertBean getCreateCustomAlertBean() {
        return getRenderedObject("createCustomAlertBean");
    }

    // Study plan management

    public ActionForward manageStudyPlan(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        return mapping.findForward("manageStudyPlan");
    }

    public ActionForward prepareCreateStudyPlan(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("studyPlanBean", new PhdStudyPlanBean(getProcess(request)));

        return mapping.findForward("createStudyPlan");
    }

    public ActionForward prepareCreateStudyPlanInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("studyPlanBean", getRenderedObject("studyPlanBean"));
        return mapping.findForward("createStudyPlan");
    }

    public ActionForward prepareCreateStudyPlanPostback(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("studyPlanBean", getRenderedObject("studyPlanBean"));
        RenderUtils.invalidateViewState("studyPlanBean");

        return mapping.findForward("createStudyPlan");
    }

    public ActionForward createStudyPlan(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        return executeActivity(AddStudyPlan.class, getRenderedObject("studyPlanBean"), request, mapping, "createStudyPlan",
                "manageStudyPlan", "message.study.plan.created.with.success");
    }

    public ActionForward prepareEditStudyPlan(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("studyPlanBean", new PhdStudyPlanBean(getProcess(request).getStudyPlan()));
        return mapping.findForward("editStudyPlan");
    }

    public ActionForward prepareEditStudyPlanInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("studyPlanBean", getRenderedObject("studyPlanBean"));
        return mapping.findForward("editStudyPlan");
    }

    public ActionForward prepareEditStudyPlanPostback(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("studyPlanBean", getRenderedObject("studyPlanBean"));
        RenderUtils.invalidateViewState("studyPlanBean");

        return mapping.findForward("editStudyPlan");
    }

    public ActionForward editStudyPlan(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        return executeActivity(EditStudyPlan.class, getRenderedObject("studyPlanBean"), request, mapping, "editStudyPlan",
                "manageStudyPlan", "message.study.plan.edited.with.success");
    }

    public ActionForward prepareCreateStudyPlanEntry(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("studyPlanEntryBean", new PhdStudyPlanEntryBean(getProcess(request).getStudyPlan()));

        return mapping.findForward("createStudyPlanEntry");

    }

    public ActionForward prepareCreateStudyPlanEntryInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("studyPlanEntryBean", getStudyPlanEntryBean());

        return mapping.findForward("createStudyPlanEntry");
    }

    public ActionForward createStudyPlanEntry(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("studyPlanEntryBean", getStudyPlanEntryBean());

        return executeActivity(AddStudyPlanEntry.class, getStudyPlanEntryBean(), request, mapping, "createStudyPlanEntry",
                "manageStudyPlan", "message.study.plan.entry.created.with.success");

    }

    private Object getStudyPlanEntryBean() {
        return getRenderedObject("studyPlanEntryBean");
    }

    public ActionForward studyPlanEntryPostBack(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("studyPlanEntryBean", getStudyPlanEntryBean());

        RenderUtils.invalidateViewState("studyPlanEntryBean");

        return mapping.findForward("createStudyPlanEntry");
    }

    public ActionForward deleteStudyPlanEntry(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        return executeActivity(DeleteStudyPlanEntry.class, getStudyPlanEntry(request), request, mapping, "manageStudyPlan",
                "manageStudyPlan", "message.study.plan.entry.deleted.successfuly");

    }

    private PhdStudyPlanEntry getStudyPlanEntry(HttpServletRequest request) {
        return getDomainObject(request, "studyPlanEntryId");

    }

    public ActionForward deleteStudyPlan(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        return executeActivity(DeleteStudyPlan.class, getProcess(request).getStudyPlan(), request, mapping, "manageStudyPlan",
                "manageStudyPlan", "message.study.plan.deleted.successfuly");

    }

    public ActionForward prepareEditQualificationExams(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("editQualificationExamsBean", new PhdIndividualProgramProcessBean(getProcess(request)));

        return mapping.findForward("editQualificationExams");
    }

    public ActionForward prepareEditQualificationExamsInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("editQualificationExamsBean", getRenderedObject("editQualificationExamsBean"));

        return mapping.findForward("editQualificationExams");
    }

    public ActionForward editQualificationExams(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("editQualificationExamsBean", getRenderedObject("editQualificationExamsBean"));

        return executeActivity(EditQualificationExams.class, getRenderedObject("editQualificationExamsBean"), request, mapping,
                "editQualificationExams", "manageStudyPlan");

    }

    // End of study plan management

    // Photo Upload

    public ActionForward prepareUploadPhoto(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("uploadPhotoBean", new PhotographUploadBean());
        return mapping.findForward("uploadPhoto");
    }

    public ActionForward uploadPhoto(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        final PhotographUploadBean photo = getRenderedObject("uploadPhotoBean");

        if (!RenderUtils.getViewState("uploadPhotoBean").isValid()) {
            addErrorMessage(request, "error.photo.upload.invalid.information");
            return uploadPhotoInvalid(mapping, actionForm, request, response);
        }

        if (ContentType.getContentType(photo.getContentType()) == null) {
            addErrorMessage(request, "error.photo.upload.unsupported.file");
            return uploadPhotoInvalid(mapping, actionForm, request, response);
        }

        try {

            photo.processImage();

            StorePersonalPhoto.uploadPhoto(photo, getProcess(request).getPerson());

            addSuccessMessage(request, "message.photo.updated.with.success");

        } catch (final UnableToProcessTheImage e) {
            addErrorMessage(request, "error.photo.upload.unable.to.process.image");
            photo.deleteTemporaryFiles();
            return uploadPhotoInvalid(mapping, actionForm, request, response);

        } catch (final DomainException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());
            photo.deleteTemporaryFiles();
            return uploadPhotoInvalid(mapping, actionForm, request, response);
        }

        return viewProcess(mapping, actionForm, request, response);
    }

    public ActionForward uploadPhotoInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("uploadPhotoBean", getRenderedObject("uploadPhotoBean"));
        RenderUtils.invalidateViewState("uploadPhotoBean");
        return mapping.findForward("uploadPhoto");
    }

    // End of Photo Upload

    // Request Public Thesis Presentation

    public ActionForward prepareRequestPublicThesisPresentation(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        final PhdThesisProcessBean bean = new PhdThesisProcessBean(getProcess(request));

        final PhdIndividualProgramProcess individualProcess = getProcess(request);

        if (individualProcess.isMigratedProcess()) {
            bean.addDocument(new PhdProgramDocumentUploadBean(PhdIndividualProgramDocumentType.PROVISIONAL_THESIS));
            bean.addDocument(new PhdProgramDocumentUploadBean(PhdIndividualProgramDocumentType.THESIS_ABSTRACT));
        } else {
            bean.addDocument(new PhdProgramDocumentUploadBean(PhdIndividualProgramDocumentType.PROVISIONAL_THESIS).required());
            bean.addDocument(new PhdProgramDocumentUploadBean(PhdIndividualProgramDocumentType.THESIS_ABSTRACT).required());
        }

        bean.addDocument(new PhdProgramDocumentUploadBean(PhdIndividualProgramDocumentType.THESIS_REQUIREMENT));
        bean.addDocument(new PhdProgramDocumentUploadBean(PhdIndividualProgramDocumentType.CV));
        bean.addDocument(new PhdProgramDocumentUploadBean(PhdIndividualProgramDocumentType.CANDIDACY_REVIEW));
        bean.addDocument(new PhdProgramDocumentUploadBean(PhdIndividualProgramDocumentType.OTHER));

        request.setAttribute("requestPublicThesisPresentation", bean);

        addThesisPreConditionsInformation(request, individualProcess);

        return mapping.findForward("requestPublicThesisPresentation");
    }

    private void addThesisPreConditionsInformation(HttpServletRequest request, final PhdIndividualProgramProcess process) {

        request.setAttribute("hasPublicPresentationSeminar", process.getSeminarProcess() != null);

        if (process.getSeminarProcess() != null && !process.getSeminarProcess().isExempted()) {
            request.setAttribute("hasPublicPresentationSeminarReport", process.hasSeminarReportDocument());
        }

        request.setAttribute("hasSchoolPartConcluded", process.hasSchoolPartConcluded());
        request.setAttribute("hasQualificationExamsToPerform", process.hasQualificationExamsToPerform());

        if (process.getRegistration() != null && process.getStudyPlan() != null
                && process.getStudyPlan().hasAnyPropaeudeuticsOrExtraEntries()) {
            request.setAttribute("hasPropaeudeuticsOrExtraEntriesApproved", process.hasPropaeudeuticsOrExtraEntriesApproved());
        }
    }

    public ActionForward prepareRequestPublicThesisPresentationInvalid(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute("requestPublicThesisPresentation", getRenderedObject("requestPublicThesisPresentation"));
        addThesisPreConditionsInformation(request, getProcess(request));

        RenderUtils.invalidateViewState("requestPublicThesisPresentation.edit.documents");
        return mapping.findForward("requestPublicThesisPresentation");
    }

    public ActionForward requestPublicThesisPresentation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final PhdThesisProcessBean bean = getRenderedObject("requestPublicThesisPresentation");
        request.setAttribute("requestPublicThesisPresentation", bean);

        RenderUtils.invalidateViewState("requestPublicThesisPresentation.edit.documents");
        addThesisPreConditionsInformation(request, getProcess(request));

        return executeActivity(RequestPublicThesisPresentation.class, bean, request, mapping, "requestPublicThesisPresentation",
                "viewProcess");
    }

    // End of Request Public Thesis Presentation

    public ActionForward manageEnrolmentPeriods(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        ManageEnrolmentsBean bean = getRenderedObject("manageEnrolmentsBean");

        if (bean == null) {
            bean = new ManageEnrolmentsBean();
            bean.setSemester(ExecutionSemester.readActualExecutionSemester());
        }

        filterEnrolmentPeriods(bean);

        request.setAttribute("manageEnrolmentsBean", bean);

        return mapping.findForward("manageEnrolmentPeriods");
    }

    private void filterEnrolmentPeriods(final ManageEnrolmentsBean bean) {
        bean.setEnrolmentPeriods(getPhdEnrolmentPeriods(bean));
    }

    private List<EnrolmentPeriod> getPhdEnrolmentPeriods(ManageEnrolmentsBean bean) {
        final List<EnrolmentPeriod> result = new ArrayList<EnrolmentPeriod>();

        for (final EnrolmentPeriod period : bean.getSemester().getEnrolmentPeriodSet()) {
            if (period.getClass().equals(EnrolmentPeriodInCurricularCourses.class) && period.getDegree().isDEA()) {
                result.add(period);
            }
        }

        return result;
    }

    public ActionForward prepareCreateEnrolmentPeriod(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final ManageEnrolmentsBean bean = new ManageEnrolmentsBean();
        bean.setSemester((ExecutionSemester) getDomainObject(request, "executionIntervalId"));

        request.setAttribute("createBean", bean);
        return mapping.findForward("createEnrolmentPeriod");
    }

    public ActionForward createEnrolmentPeriodInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("createBean", getRenderedObject("createBean"));
        return mapping.findForward("createEnrolmentPeriod");
    }

    public ActionForward createEnrolmentPeriod(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        try {

            final ManageEnrolmentsBean bean = getRenderedObject("createBean");
            CreateEnrolmentPeriods.create(bean.getDegreeCurricularPlans(), bean.getSemester(), bean.getStartDate(),
                    bean.getEndDate());

        } catch (final DomainException e) {
            addErrorMessage(request, e.getMessage(), e.getArgs());
            return createEnrolmentPeriodInvalid(mapping, actionForm, request, response);
        }

        return redirect("/phdIndividualProgramProcess.do?method=manageEnrolmentPeriods", request);
    }

    public ActionForward prepareEditEnrolmentPeriod(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("enrolmentPeriod", getDomainObject(request, "periodId"));
        return mapping.findForward("editEnrolmentPeriod");
    }

    public ActionForward deleteEnrolmentPeriod(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        try {
            ((EnrolmentPeriod) getDomainObject(request, "periodId")).delete();
            return redirect("/phdIndividualProgramProcess.do?method=manageEnrolmentPeriods", request);

        } catch (final DomainException e) {
            addErrorMessage(request, e.getMessage(), e.getArgs());
            return manageEnrolmentPeriods(mapping, actionForm, request, response);
        }

    }

    // End of edit Phd Enrolment Periods

    // Edit when started studies
    public ActionForward prepareEditWhenStartedStudies(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final RegistrationFormalizationBean bean = new RegistrationFormalizationBean();
        bean.setWhenStartedStudies(getProcess(request).getWhenStartedStudies());

        request.setAttribute("registrationFormalizationBean", bean);
        return mapping.findForward("editWhenStartedStudies");
    }

    public ActionForward editWhenStartedStudies(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final PhdIndividualProgramProcess process = getProcess(request);
        final RegistrationFormalizationBean bean = getRenderedObject("registrationFormalizationBean");

        try {

            ExecuteProcessActivity.run(process, EditWhenStartedStudies.class.getSimpleName(), bean);

        } catch (final DomainException e) {
            addErrorMessage(request, e.getMessage(), e.getArgs());
            return mapping.findForward("editWhenStartedStudies");
        }

        return redirect(
                String.format("/phdIndividualProgramProcess.do?method=viewProcess&processId=%s", process.getExternalId()),
                request);
    }

    // End of edit when started studies

    // Print school registration declaration

    public ActionForward printSchoolRegistrationDeclaration(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        final PhdSchoolRegistrationDeclarationDocument report = new PhdSchoolRegistrationDeclarationDocument(getProcess(request));
        writeFile(response, report.getReportFileName() + ".pdf", "application/pdf", DefaultDocumentGenerator
                .getGenerator().generateReport(Collections.singletonList(report)));
        return null;
    }

    // End of Print school registration declaration

    // Phd Configuration Management
    public ActionForward preparePhdConfigurationManagement(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final PhdIndividualProgramProcess process = getProcess(request);
        PhdConfigurationIndividualProgramProcessBean bean = new PhdConfigurationIndividualProgramProcessBean(process);
        request.setAttribute("phdConfigurationIndividualProgramProcessBean", bean);
        return mapping.findForward("managePhdIndividualProcessConfiguration");
    }

    public ActionForward savePhdConfiguration(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final PhdIndividualProgramProcess process = getProcess(request);
        PhdConfigurationIndividualProgramProcessBean bean =
                (PhdConfigurationIndividualProgramProcessBean) getObjectFromViewState("phdConfigurationIndividualProgramProcessBean");

        ExecuteProcessActivity.run(process, ConfigurePhdIndividualProgramProcess.class, bean);
        return viewProcess(mapping, form, request, response);
    }

    public ActionForward savePhdConfigurationInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final PhdIndividualProgramProcess process = getProcess(request);
        PhdConfigurationIndividualProgramProcessBean bean = new PhdConfigurationIndividualProgramProcessBean(process);

        request.setAttribute("phdConfigurationIndividualProgramProcessBean", bean);
        return mapping.findForward("managePhdIndividualProcessConfiguration");
    }

    // End of Phd Configuration Management

    // Phd Emails Management
    public ActionForward preparePhdEmailsManagement(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        return mapping.findForward("managePhdIndividualProcessEmails");
    }

    public ActionForward prepareSendPhdEmail(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final PhdIndividualProgramProcess process = getProcess(request);
        final PhdIndividualProgramProcessEmailBean emailBean = new PhdIndividualProgramProcessEmailBean();

        emailBean.setProcess(process);

        request.setAttribute("emailBean", emailBean);

        return mapping.findForward("sendPhdIndividualProcessEmail");
    }

    public ActionForward sendPhdEmail(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final PhdIndividualProgramProcess process = getProcess(request);
        final PhdEmailBean bean = getRenderedObject("emailBean");

        try {

            ExecuteProcessActivity.run(process, SendPhdEmail.class, bean);

        } catch (final DomainException e) {
            addErrorMessage(request, e.getMessage(), e.getArgs());
            request.setAttribute("emailBean", bean);
            return mapping.findForward("sendPhdIndividualProcessEmail");
        }

        return preparePhdEmailsManagement(mapping, form, request, response);
    }

    public ActionForward sendEmailPostback(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final PhdIndividualProgramProcessEmailBean emailBean = getRenderedObject("emailBean");
        emailBean.refreshTemplateInUse();
        request.setAttribute("emailBean", emailBean);

        RenderUtils.invalidateViewState("emailBean.create");
        for (PhdEmailParticipantsGroup group : emailBean.getPossibleParticipantsGroups()) {
            RenderUtils.invalidateViewState("emailBean.groups.edit." + group.toString());
        }
        RenderUtils.invalidateViewState("emailBean.individuals");
        RenderUtils.invalidateViewState("emailBean.template");

        return mapping.findForward("sendPhdIndividualProcessEmail");
    }

    public ActionForward viewPhdEmail(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final PhdIndividualProgramProcess process = getProcess(request);
        PhdIndividualProgramProcessEmailBean bean = new PhdIndividualProgramProcessEmailBean(getPhdEmail(request));
        bean.setProcess(process);

        request.setAttribute("emailBean", bean);

        return mapping.findForward("viewPhdIndividualProcessEmail");
    }

    private PhdIndividualProgramProcessEmail getPhdEmail(HttpServletRequest request) {
        return getDomainObject(request, "phdEmailId");
    }

    // End of Phd Emails Management

    public ActionForward removeLastState(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final PhdIndividualProgramProcess process = getProcess(request);

        try {
            ExecuteProcessActivity.run(process, RemoveLastStateOnPhdIndividualProgramProcess.class, null);
        } catch (DomainException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());
        }
        return managePhdIndividualProgramProcessState(mapping, form, request, response);
    }

    // Start of Individual Migration Process Visualization

    public ActionForward viewAssociatedMigrationProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final PhdIndividualProgramProcess process = getProcess(request);
        final PhdMigrationIndividualProcessData processData = process.getAssociatedMigrationProcess();

        if (processData != null) {
            request.setAttribute("processDataBean", processData.getProcessBean());

            if (processData.getPhdMigrationIndividualPersonalData() != null) {
                request.setAttribute("personalDataBean", processData.getPhdMigrationIndividualPersonalData().getPersonalBean());
            }

        }

        final PhdMigrationGuiding guidingData = process.getAssociatedMigrationGuiding();
        final PhdMigrationGuiding assistantGuidingData = process.getAssociatedMigrationAssistantGuiding();

        if (guidingData != null) {
            request.setAttribute("migrationGuidingBean", guidingData.getGuidingBean());
        }

        if (assistantGuidingData != null) {
            request.setAttribute("migrationAssistantGuidingBean", assistantGuidingData.getGuidingBean());
        }

        return mapping.findForward("viewMigrationProcess");
    }

    public ActionForward viewMigrationProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        fetchMigrationBeans(request);

        return mapping.findForward("viewMigrationProcess");
    }

    private PhdMigrationIndividualProcessData fetchMigrationBeans(HttpServletRequest request) {
        final Integer migrationId = getMigrationProcessId(request);
        final PhdMigrationIndividualProcessData processData = getMigrationProcessData(migrationId);

        if (processData != null) {
            request.setAttribute("processDataBean", processData.getProcessBean());

            if (processData.getPhdMigrationIndividualPersonalData() != null) {
                request.setAttribute("personalDataBean", processData.getPhdMigrationIndividualPersonalData().getPersonalBean());
            }

            final String guiderCode = processData.getProcessBean().getGuiderId();
            final String assistantGuiderCode = processData.getProcessBean().getAssistantGuiderId();

            if (guiderCode != null) {
                final PhdMigrationGuiding guiderData = getMigrationGuidingData(guiderCode);

                if (guiderData != null) {
                    request.setAttribute("migrationGuidingBean", guiderData.getGuidingBean());
                }
            }

            if (assistantGuiderCode != null) {
                final PhdMigrationGuiding assistantGuiderData = getMigrationGuidingData(assistantGuiderCode);

                if (assistantGuiderData != null) {
                    request.setAttribute("migrationAssistantGuidingBean", assistantGuiderData.getGuidingBean());
                }
            }
        }

        return processData;
    }

    private Integer getMigrationProcessId(HttpServletRequest request) {
        final String attribute = (String) request.getAttribute("migrationProcessId");
        final String parameter = request.getParameter("migrationProcessId");

        if (attribute != null) {
            return Integer.valueOf(attribute);
        } else {
            return Integer.valueOf(parameter);
        }
    }

    private PhdMigrationIndividualProcessData getMigrationProcessData(Integer migrationId) {
        for (final PhdMigrationProcess migrationProcess : Bennu.getInstance().getPhdMigrationProcessesSet()) {
            for (final PhdMigrationIndividualProcessData processData : migrationProcess.getPhdMigrationIndividualProcessDataSet()) {
                if (processData.getNumber().equals(migrationId)) {
                    return processData;
                }
            }
        }

        return null;
    }

    private PhdMigrationGuiding getMigrationGuidingData(String teacherId) {
        for (final PhdMigrationProcess migrationProcess : Bennu.getInstance().getPhdMigrationProcessesSet()) {
            for (final PhdMigrationGuiding guidingData : migrationProcess.getPhdMigrationGuidingSet()) {
                if (guidingData.getTeacherId().equals(teacherId)) {
                    return guidingData;
                }
            }
        }

        return null;
    }

    // End of Individual Migration Process Visualization

    // Start of Migration Processes Visualization

    public ActionForward viewMigratedProcesses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        SearchPhdMigrationProcessBean searchBean =
                (SearchPhdMigrationProcessBean) getObjectFromViewState("searchMigrationProcessBean");

        if (searchBean == null) {
            searchBean = initializeMigrationSearchBean(request);
        }

        AndPredicate<PhdMigrationIndividualProcessData> predicate = searchBean.getPredicates();

        predicate.add(new Predicate<PhdMigrationIndividualProcessData>() {
            @Override
            public boolean test(PhdMigrationIndividualProcessData process) {
            	final PhdProgram program = process.getProcessBean().getPhdProgram();
                return AcademicAccessRule
                        .getPhdProgramsAccessibleToFunction(AcademicOperationType.MANAGE_PHD_PROCESSES, Authenticate.getUser())
                        .anyMatch(p -> p == program);
            }
        });

        request.setAttribute("searchMigrationProcessBean", searchBean);
        request.setAttribute("migrationProcesses",
                PhdIndividualProgramProcess.searchMigrationProcesses(searchBean.getExecutionYear(), predicate));

        return mapping.findForward("viewAllMigratedProcesses");
    }

    protected SearchPhdMigrationProcessBean initializeMigrationSearchBean(HttpServletRequest request) {
        final SearchPhdMigrationProcessBean searchBean = new SearchPhdMigrationProcessBean();
        searchBean.setFilterPhdPrograms(false);
        searchBean.setFilterPhdProcesses(false);

        searchBean.setProcessState(PhdMigrationProcessStateType.CONCLUDED);

        return searchBean;
    }

    // End of Migration Processes Visualization

    // Start of Manual Migration

    public ActionForward prepareManualMigration(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final Integer migrationId = getMigrationProcessId(request);
        final PhdMigrationIndividualProcessData processData = getMigrationProcessData(migrationId);
        request.setAttribute("processData", processData);

        return mapping.findForward("editCandidacyProcessData");
    }

    public ActionForward editMigrationPersonalData(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final Integer migrationId = getMigrationProcessId(request);
        final PhdMigrationIndividualProcessData processData = getMigrationProcessData(migrationId);

        PhdMigrationIndividualProcessDataBean processDataBean = getRenderedObject("processDataBean");

        request.setAttribute("processData", processData);
        request.setAttribute("processDataBean", processDataBean);

        return mapping.findForward("editPersonalData");
    }

    public ActionForward verifyChosenCandidate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final Integer migrationId = getMigrationProcessId(request);
        final PhdMigrationIndividualProcessData processData = getMigrationProcessData(migrationId);

        PhdMigrationIndividualProcessDataBean processDataBean = getRenderedObject("processDataBean");
        PhdMigrationIndividualPersonalDataBean personalDataBean = getRenderedObject("personalDataBean");

        request.setAttribute("processData", processData);
        request.setAttribute("processDataBean", processDataBean);
        request.setAttribute("personalDataBean", personalDataBean);

        return mapping.findForward("verifyChosenCandidate");
    }

    public ActionForward createCandidacyManualMigration(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final Integer migrationId = getMigrationProcessId(request);
        final PhdMigrationIndividualProcessData processData = getMigrationProcessData(migrationId);

        PhdMigrationIndividualProcessDataBean processDataBean = getRenderedObject("processDataBean");
        PhdMigrationIndividualPersonalDataBean personalDataBean = getRenderedObject("personalDataBean");

        try {
            processData.proceedWithMigration(Authenticate.getUser());
            addSuccessMessage(request, "message.migration.manual.candidacy.success");
        } catch (PhdMigrationException e) {
            addErrorMessage(request, e.getKey());
            request.setAttribute("processData", processData);
            request.setAttribute("processDataBean", processDataBean);
            request.setAttribute("personalDataBean", processData.getPhdMigrationIndividualPersonalData().getPersonalBean());
            return mapping.findForward("editPersonalData");
        } catch (DomainException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());
            request.setAttribute("processData", processData);
            request.setAttribute("processDataBean", processDataBean);
            request.setAttribute("personalDataBean", personalDataBean);
            return mapping.findForward("editPersonalData");
        }

        request.setAttribute("migratedProcess", processData.getPhdIndividualProgramProcess());

        return mapping.findForward("concludeManualMigration");
    }

    // End of Manual Migration

    // Edition of Phd Participants

    public ActionForward viewPhdParticipants(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return mapping.findForward("viewPhdParticipants");
    }

    public ActionForward prepareEditPhdParticipant(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PhdParticipant phdParticipant = getDomainObject(request, "phdParticipantId");
        PhdParticipantBean bean = new PhdParticipantBean(phdParticipant);
        request.setAttribute("phdParticipantBean", bean);

        return mapping.findForward("editPhdParticipant");
    }

    public ActionForward editPhdParticipant(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PhdParticipantBean bean = getRenderedObject("phdParticipantBean");
        ExecuteProcessActivity.run(getProcess(request), EditPhdParticipant.class, bean);

        return viewPhdParticipants(mapping, form, request, response);
    }

    public ActionForward editPhdParticipantInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PhdParticipantBean bean = getRenderedObject("phdParticipantBean");
        request.setAttribute("phdParticipantBean", bean);

        return mapping.findForward("editPhdParticipant");
    }

    // End of Edition of Phd Participants

    // Process Transfer

    public ActionForward prepareChooseProcessToTransfer(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PhdIndividualProgramProcess process = getProcess(request);

        request.setAttribute("hasEnrolmentsInCurrentYear", hasEnrolmentsInCurrentYear(process));

        request.setAttribute("enrolmentsInCurrentYear",
                !process.getStudyPlan().isExempted() ? hasEnrolmentsInCurrentYear(process) : false);

        request.setAttribute("studentProcesses", getStudentOtherProcesses(process));

        return mapping.findForward("chooseProcessToTransfer");
    }

    private Boolean hasEnrolmentsInCurrentYear(PhdIndividualProgramProcess process) {
        if (process.getStudyPlan().isExempted()) {
            return false;
        }

        return process.getRegistration().getLastStudentCurricularPlan().hasAnyEnrolmentForCurrentExecutionYear();
    }

    private List<PhdIndividualProgramProcess> getStudentOtherProcesses(final PhdIndividualProgramProcess process) {
        List<PhdIndividualProgramProcess> result = new ArrayList<PhdIndividualProgramProcess>();
        result.addAll(CollectionUtils.disjunction(process.getPerson().getPhdIndividualProgramProcessesSet(),
                Collections.singletonList(process)));

        return result;
    }

    public ActionForward prepareFillRemarksOnTransfer(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PhdIndividualProgramProcessBean bean = new PhdIndividualProgramProcessBean(getProcess(request));

        bean.setDestiny((PhdIndividualProgramProcess) getDomainObject(request, "destinyId"));
        request.setAttribute("phdIndividualProgramProcessBean", bean);

        return mapping.findForward("fillRemarksOnTransfer");
    }

    public ActionForward transferProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        try {
            PhdIndividualProgramProcessBean bean = getRenderedObject("phdIndividualProgramProcessBean");
            ExecuteProcessActivity.run(getProcess(request), TransferToAnotherProcess.class, bean);
        } catch (DomainException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());
            return transferProcessInvalid(mapping, form, request, response);
        }

        return viewProcess(mapping, form, request, response);
    }

    public ActionForward transferProcessInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("phdIndividualProgramProcessBean", getRenderedObject("phdIndividualProgramProcessBean"));

        return mapping.findForward("fillRemarksOnTransfer");
    }

    public ActionForward prepareDissociateRegistration(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PhdIndividualProgramProcess process = getProcess(request);

        request.setAttribute("process", process);
        request.setAttribute("something", Boolean.TRUE);

        return mapping.findForward("dissociateRegistration");
    }

    public ActionForward dissociateRegistration(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        ExecuteProcessActivity.run(getProcess(request), DissociateRegistration.class, null);

        return viewProcess(mapping, form, request, response);
    }

    public ActionForward generateReport(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        SearchPhdIndividualProgramProcessBean searchBean =
                (SearchPhdIndividualProgramProcessBean) getObjectFromViewState("searchProcessBean");

        HSSFWorkbook workbook = new HSSFWorkbook();
        PhdIndividualProgramProcessesReport report = new PhdIndividualProgramProcessesReport(workbook);
        report.build(searchBean);

        PhdGuidersReport guidersReport = new PhdGuidersReport(workbook);
        guidersReport.build(searchBean);

        EPFLCandidatesReport epflReport = new EPFLCandidatesReport(workbook);
        epflReport.build(searchBean);

        RecommendationLetterReport recommendationLetterReport = new RecommendationLetterReport(workbook);
        recommendationLetterReport.build(searchBean);

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=phd.xls");
        workbook.write(response.getOutputStream());

        response.getOutputStream().flush();
        response.flushBuffer();

        return null;
    }

    // End of Process transfer

    // Upload guidance acceptance document

    public ActionForward prepareUploadGuidanceAcceptanceLetter(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        PhdIndividualProgramProcess process = getDomainObject(request, "processId");
        PhdParticipant guider = getDomainObject(request, "guidingId");

        PhdParticipantBean guidingBean = new PhdParticipantBean(guider);
        PhdProgramDocumentUploadBean guidingAcceptanceLetter = new PhdProgramDocumentUploadBean();
        guidingAcceptanceLetter.setType(PhdIndividualProgramDocumentType.GUIDER_ACCEPTANCE_LETTER);
        guidingBean.setGuidingAcceptanceLetter(guidingAcceptanceLetter);

        request.setAttribute("process", process);
        request.setAttribute("guidingBean", guidingBean);

        return mapping.findForward("uploadGuidanceAcceptanceDocument");
    }

    public ActionForward uploadGuidanceAcceptanceLetterInvalid(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        return prepareUploadGuidanceAcceptanceLetter(mapping, form, request, response);
    }

    public ActionForward uploadGuidanceAcceptanceLetter(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PhdIndividualProgramProcess process = getDomainObject(request, "processId");
        PhdParticipantBean guidingBean = getRenderedObject("guidingBean");

        ExecuteProcessActivity.run(process, UploadGuidanceAcceptanceLetter.class, guidingBean);

        return prepareManageGuidingInformation(mapping, form, request, response);
    }

    // End of upload guidance acceptance document

    /* EDIT PHD STATES */

    public ActionForward prepareEditState(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PhdProcessState state = getDomainObject(request, "stateId");
        PhdProcessStateBean bean = new PhdProcessStateBean(state);

        request.setAttribute("bean", bean);

        return mapping.findForward("editPhdProcessState");
    }

    public ActionForward editState(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PhdProcessStateBean bean = getRenderedObject("bean");
        bean.getState().editStateDate(bean);

        return managePhdIndividualProgramProcessState(mapping, form, request, response);
    }

    public ActionForward editStateInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PhdProcessStateBean bean = getRenderedObject("bean");
        request.setAttribute("bean", bean);

        return mapping.findForward("editPhdProcessState");
    }

    /* EDIT PHD STATES */

    // Alerts Management
    public ActionForward viewAllAlertMessages(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PhdIndividualProgramProcess process = getProcess(request);

        TreeSet<PhdAlertMessage> orderedMessages =
                new TreeSet<PhdAlertMessage>(Collections.reverseOrder(PhdAlertMessage.COMPARATOR_BY_WHEN_CREATED_AND_ID));
        orderedMessages.addAll(process.getAlertMessagesSet());

        request.setAttribute("alertMessages", orderedMessages);
        return mapping.findForward("viewAllAlertMessages");
    }

    public ActionForward viewAlertMessageFromAllAlertMessages(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PhdAlertMessage alertMessage = getDomainObject(request, "alertMessageId");

        request.setAttribute("alertMessage", alertMessage);

        return mapping.findForward("viewAlertMessageFromAllAlertMessages");
    }

    public ActionForward viewLogs(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        return mapping.findForward("viewLogs");
    }

}
