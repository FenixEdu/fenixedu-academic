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
package org.fenixedu.academic.ui.struts.action.internationalRelatOffice.candidacy.erasmus;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.candidacyProcess.CandidacyProcessDocumentUploadBean;
import org.fenixedu.academic.domain.candidacyProcess.IndividualCandidacyProcess;
import org.fenixedu.academic.domain.candidacyProcess.erasmus.ApprovedLearningAgreementDocumentFile;
import org.fenixedu.academic.domain.candidacyProcess.erasmus.ErasmusAlert;
import org.fenixedu.academic.domain.candidacyProcess.mobility.MobilityIndividualApplication;
import org.fenixedu.academic.domain.candidacyProcess.mobility.MobilityIndividualApplicationProcess;
import org.fenixedu.academic.domain.candidacyProcess.mobility.MobilityIndividualApplicationProcessBean;
import org.fenixedu.academic.domain.caseHandling.Activity;
import org.fenixedu.academic.domain.curricularRules.executors.RuleResult;
import org.fenixedu.academic.domain.curricularRules.executors.ruleExecutors.CurricularRuleLevel;
import org.fenixedu.academic.domain.exceptions.EnrollmentDomainException;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DefaultDocumentGenerator;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumModule;
import org.fenixedu.academic.domain.studentCurriculum.NoCourseGroupCurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.NoCourseGroupCurriculumGroupType;
import org.fenixedu.academic.dto.administrativeOffice.studentEnrolment.MobilityExtraEnrolmentBean;
import org.fenixedu.academic.dto.person.PersonBean;
import org.fenixedu.academic.dto.student.enrollment.bolonha.BolonhaStudentEnrollmentBean;
import org.fenixedu.academic.dto.student.enrollment.bolonha.CycleEnrolmentBean;
import org.fenixedu.academic.dto.student.enrollment.bolonha.ErasmusBolonhaStudentEnrollmentBean;
import org.fenixedu.academic.dto.student.enrollment.bolonha.ErasmusBolonhaStudentEnrollmentBean.ErasmusExtraCurricularEnrolmentBean;
import org.fenixedu.academic.predicate.IllegalDataAccessException;
import org.fenixedu.academic.report.candidacy.erasmus.LearningAgreementDocument;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.student.administrativeOfficeServices.CreateExtraEnrolment;
import org.fenixedu.academic.service.services.student.enrolment.bolonha.EnrolBolonhaStudent;
import org.fenixedu.academic.ui.struts.FenixActionForm;
import org.fenixedu.bennu.core.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.joda.time.YearMonthDay;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.FenixFramework;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Mapping(path = "/caseHandlingMobilityIndividualApplicationProcess", module = "internationalRelatOffice",
        formBeanClass = FenixActionForm.class, functionality = ErasmusCandidacyProcessDA.class)
@Forwards({
        @Forward(name = "intro",
                path = "/internationalRelatOffice/caseHandlingMobilityApplicationProcess.do?method=listProcessAllowedActivities"),
        @Forward(name = "list-allowed-activities", path = "/candidacy/erasmus/listIndividualCandidacyActivities.jsp"),
        @Forward(name = "prepare-create-new-process", path = "/candidacy/erasmus/selectPersonForCandidacy.jsp"),
        @Forward(name = "fill-personal-information", path = "/candidacy/erasmus/fillPersonalInformation.jsp"),
        @Forward(name = "fill-candidacy-information", path = "/candidacy/erasmus/fillCandidacyInformation.jsp"),
        @Forward(name = "fill-degree-information", path = "/candidacy/erasmus/fillDegreeInformation.jsp"),
        @Forward(name = "fill-courses-information", path = "/candidacy/erasmus/fillCoursesInformation.jsp"),
        @Forward(name = "edit-candidacy-personal-information", path = "/candidacy/erasmus/editPersonalInformation.jsp"),
        @Forward(name = "edit-candidacy-information", path = "/candidacy/erasmus/editCandidacyInformation.jsp"),
        @Forward(name = "edit-degree-courses-information", path = "/candidacy/erasmus/editDegreeAndCoursesInformation.jsp"),
        @Forward(name = "set-gri-validation", path = "/internationalRelatOffice/candidacy/erasmus/setGriValidation.jsp"),
        @Forward(name = "visualize-alerts", path = "/candidacy/erasmus/visualizeAlerts.jsp"),
        @Forward(name = "prepare-edit-candidacy-documents", path = "/candidacy/erasmus/editCandidacyDocuments.jsp"),
        @Forward(name = "create-student-data", path = "/candidacy/erasmus/createStudentData.jsp"),
        @Forward(name = "view-student-data-username", path = "/candidacy/erasmus/viewStudentDataUsername.jsp"),
        @Forward(name = "edit-eidentifier", path = "/candidacy/erasmus/editEidentifier.jsp"),
        @Forward(name = "cancel-candidacy", path = "/candidacy/cancelCandidacy.jsp"),
        @Forward(name = "view-approved-learning-agreements", path = "/candidacy/erasmus/viewApprovedLearningAgreements.jsp"),
        @Forward(name = "upload-learning-agreement", path = "/candidacy/erasmus/uploadLearningAgreement.jsp"),
        @Forward(name = "reject-candidacy", path = "/candidacy/rejectCandidacy.jsp"),
        @Forward(name = "revert-candidacy-to-standby", path = "/candidacy/erasmus/revertCandidacyToStandby.jsp"),
        @Forward(name = "enrol-student", path = "/candidacy/erasmus/enrolStudent.jsp"),
        @Forward(name = "chooseCycleCourseGroupToEnrol", path = "/candidacy/erasmus/chooseCycleCourseGroupToEnrol.jsp") })
public class ErasmusIndividualCandidacyProcessDA extends
        org.fenixedu.academic.ui.struts.action.candidacy.erasmus.ErasmusIndividualCandidacyProcessDA {

    @Override
    protected List<Activity> getAllowedActivities(final IndividualCandidacyProcess process) {
        List<Activity> activities = process.getAllowedActivities(Authenticate.getUser());
        ArrayList<Activity> resultActivities = new ArrayList<Activity>();

        for (Activity activity : activities) {
            if (activity.isVisibleForGriOffice()) {
                resultActivities.add(activity);
            }
        }

        return resultActivities;
    }

    public ActionForward prepareExecuteSetGriValidation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        final MobilityIndividualApplicationProcessBean bean = new MobilityIndividualApplicationProcessBean(getProcess(request));

        bean.setCreateAlert(true);
        bean.setSendEmail(true);

        request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
        return mapping.findForward("set-gri-validation");

    }

    public ActionForward executeSetGriValidation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        try {
            MobilityIndividualApplicationProcessBean bean = getIndividualCandidacyProcessBean();

            if (bean.getCreateAlert()
                    && (StringUtils.isEmpty(bean.getAlertSubject()) || StringUtils.isEmpty(bean.getAlertBody()))) {
                addActionMessage(request, "error.erasmus.alert.subject.and.body.must.not.be.empty");
            } else {
                executeActivity(getProcess(request), "SetGriValidation", getIndividualCandidacyProcessBean());
                return listProcessAllowedActivities(mapping, actionForm, request, response);
            }
        } catch (final DomainException e) {
            addActionMessage(request, e.getLocalizedMessage(), false);
        }

        request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
        return mapping.findForward("set-gri-validation");
    }

    public ActionForward executeSetGriValidationInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
        return mapping.findForward("set-gri-validation");
    }

    public ActionForward prepareExecuteCreateStudentData(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        final MobilityIndividualApplicationProcessBean bean = new MobilityIndividualApplicationProcessBean(getProcess(request));
        request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);

        return mapping.findForward("create-student-data");
    }

    public ActionForward prepareExecuteCreateStudentDataInvalid(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
        return mapping.findForward("create-student-data");
    }

    public ActionForward executeCreateStudentData(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());

        try {
            executeActivity(getProcess(request), "CreateStudentData", getIndividualCandidacyProcessBean());
            executeActivity(getProcess(request), "ImportToLDAP", getIndividualCandidacyProcessBean());

        } catch (final DomainException e) {
            addActionMessage(request, e.getLocalizedMessage(), false);
            request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
            return mapping.findForward("create-student-data");
        }

        return mapping.findForward("view-student-data-username");
    }

    public ActionForward prepareExecuteSetEIdentifierForTesting(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        final MobilityIndividualApplicationProcessBean bean = new MobilityIndividualApplicationProcessBean(getProcess(request));

        bean.setPersonBean(new PersonBean(getProcess(request).getPersonalDetails()));

        request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);

        return mapping.findForward("edit-eidentifier");
    }

    public ActionForward prepareExecuteSetEIdentifierForTestingInvalid(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        final MobilityIndividualApplicationProcessBean bean = new MobilityIndividualApplicationProcessBean(getProcess(request));
        request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);

        return mapping.findForward("edit-eidentifier");
    }

    public ActionForward executeSetEIdentifierForTesting(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException {
        final MobilityIndividualApplicationProcessBean bean = new MobilityIndividualApplicationProcessBean(getProcess(request));
        request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);

        try {
            executeActivity(getProcess(request), "SetEIdentifierForTesting", getIndividualCandidacyProcessBean());
        } catch (final DomainException e) {
            addActionMessage(request, e.getLocalizedMessage(), false);
            request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
            return mapping.findForward("edit-eidentifier");
        }

        return listProcessAllowedActivities(mapping, actionForm, request, response);
    }

    public ActionForward prepareExecuteViewApprovedLearningAgreements(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        final MobilityIndividualApplicationProcessBean bean = new MobilityIndividualApplicationProcessBean(getProcess(request));
        request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);

        return mapping.findForward("view-approved-learning-agreements");
    }

    public ActionForward markApprovedLearningAgreementAsViewed(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        ApprovedLearningAgreementDocumentFile file =
                FenixFramework.getDomainObject(request.getParameter("approvedLearningAgreementId"));
        file.markLearningAgreementViewed();

        return prepareExecuteViewApprovedLearningAgreements(mapping, actionForm, request, response);
    }

    public ActionForward markApprovedLearningAgreementAsSent(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        ApprovedLearningAgreementDocumentFile file =
                FenixFramework.getDomainObject(request.getParameter("approvedLearningAgreementId"));
        file.markLearningAgreementSent();

        return prepareExecuteViewApprovedLearningAgreements(mapping, actionForm, request, response);
    }

    public ActionForward markAlertAsViewed(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        ErasmusAlert alert = FenixFramework.getDomainObject(request.getParameter("erasmusAlertId"));
        executeActivity(getProcess(request), "MarkAlertAsViewed", alert);

        return prepareExecuteVisualizeAlerts(mapping, actionForm, request, response);
    }

    public ActionForward sendEmailToAcceptedStudent(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        ApprovedLearningAgreementDocumentFile file =
                FenixFramework.getDomainObject(request.getParameter("approvedLearningAgreementId"));
        executeActivity(getProcess(request), "SendEmailToAcceptedStudent", null);

        return prepareExecuteViewApprovedLearningAgreements(mapping, actionForm, request, response);
    }

    public ActionForward retrieveLearningAgreement(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        MobilityIndividualApplicationProcess process = getProcess(request);

        final LearningAgreementDocument document = new LearningAgreementDocument(process);
        byte[] data = DefaultDocumentGenerator.getGenerator().generateReport(Collections.singletonList(document));

        response.setContentLength(data.length);
        response.setContentType("application/pdf");
        response.addHeader("Content-Disposition", "attachment; filename=" + document.getReportFileName() + ".pdf");

        final ServletOutputStream writer = response.getOutputStream();
        writer.write(data);
        writer.flush();
        writer.close();

        response.flushBuffer();
        return mapping.findForward("");
    }

    public ActionForward revokeApprovedLearningAgreement(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        ApprovedLearningAgreementDocumentFile file =
                FenixFramework.getDomainObject(request.getParameter("approvedLearningAgreementId"));
        CandidacyProcessDocumentUploadBean documentBean = new CandidacyProcessDocumentUploadBean();
        documentBean.setDocumentFile(file);

        executeActivity(getProcess(request), "RevokeDocumentFile", documentBean);

        return prepareExecuteViewApprovedLearningAgreements(mapping, form, request, response);
    }

    public ActionForward prepareExecuteRevertCandidacyToStandBy(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());

        return mapping.findForward("revert-candidacy-to-standby");
    }

    public ActionForward executeRevertCandidacyToStandBy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        try {
            executeActivity(getProcess(request), "RevertCandidacyToStandBy", null);
        } catch (DomainException e) {
            addActionMessage(request, e.getLocalizedMessage(), false);
            return mapping.findForward("revert-candidacy-to-standby");
        }
        return listProcessAllowedActivities(mapping, form, request, response);
    }

    public ActionForward enrolStudent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        MobilityIndividualApplicationProcess process = getProcess(request);
        MobilityIndividualApplication candidacy = process.getCandidacy();
        Boolean restrictEnrollment;
        ErasmusBolonhaStudentEnrollmentBean bean;
        if (candidacy.getRegistration().getActiveStudentCurricularPlan() != null) {
            restrictEnrollment = true;
            ExecutionSemester semester = ExecutionSemester.readByYearMonthDay(new YearMonthDay());
            bean =
                    new ErasmusBolonhaStudentEnrollmentBean(candidacy.getRegistration().getActiveStudentCurricularPlan(),
                            semester, null, CurricularRuleLevel.ENROLMENT_NO_RULES, candidacy);
        } else {
            restrictEnrollment = false;
            bean = null;
        }
        return enrolStudent(mapping, request, process, bean);
    }

    private ActionForward enrolStudent(ActionMapping mapping, HttpServletRequest request,
            MobilityIndividualApplicationProcess process, ErasmusBolonhaStudentEnrollmentBean bean) {
        MobilityIndividualApplication candidacy = process.getCandidacy();
        Boolean restrictEnrollment;
        if (candidacy.getRegistration().getActiveStudentCurricularPlan() != null) {
            restrictEnrollment = true;
        } else {
            restrictEnrollment = false;
        }
        request.setAttribute("process", process);
        request.setAttribute("restrictEnrollment", restrictEnrollment);
        request.setAttribute("bolonhaStudentEnrollmentBean", bean);
        request.setAttribute("action", "/caseHandlingMobilityIndividualApplicationProcess.do");
        return mapping.findForward("enrol-student");
    }

    public ActionForward postBack(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixServiceException {
        MobilityIndividualApplicationProcess process = getProcess(request);
        MobilityIndividualApplication candidacy = process.getCandidacy();
        ExecutionSemester semester = ((ErasmusBolonhaStudentEnrollmentBean) getRenderedObject()).getExecutionPeriod();
        RenderUtils.invalidateViewState();
        ErasmusBolonhaStudentEnrollmentBean bean =
                new ErasmusBolonhaStudentEnrollmentBean(candidacy.getRegistration().getActiveStudentCurricularPlan(), semester,
                        null, CurricularRuleLevel.ENROLMENT_NO_RULES, candidacy);
        return enrolStudent(mapping, request, process, bean);
    }

    public ActionForward doEnrol(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixServiceException {
        ErasmusBolonhaStudentEnrollmentBean erasmusBolonhaStudentEnrollmentBean =
                (ErasmusBolonhaStudentEnrollmentBean) getRenderedObject();
        try {
            final RuleResult ruleResults =
                    EnrolBolonhaStudent.run(erasmusBolonhaStudentEnrollmentBean.getStudentCurricularPlan(),
                            erasmusBolonhaStudentEnrollmentBean.getExecutionPeriod(),
                            erasmusBolonhaStudentEnrollmentBean.getDegreeModulesToEvaluate(),
                            erasmusBolonhaStudentEnrollmentBean.getCurriculumModulesToRemove(),
                            erasmusBolonhaStudentEnrollmentBean.getCurricularRuleLevel());

            if (!erasmusBolonhaStudentEnrollmentBean.getDegreeModulesToEvaluate().isEmpty()
                    || !erasmusBolonhaStudentEnrollmentBean.getCurriculumModulesToRemove().isEmpty()) {
                addActionMessage("success", request, "label.save.success");
            }

            if (ruleResults.isWarning()) {
                addRuleResultMessagesToActionMessages("warning", request, ruleResults);
            }

        } catch (EnrollmentDomainException ex) {
            addRuleResultMessagesToActionMessages("error", request, ex.getFalseResult());

            return enrolStudent(mapping, form, request, response);

        } catch (DomainException de) {
            addActionMessage("error", request, de.getLocalizedMessage(), false);
            return enrolStudent(mapping, form, request, response);
        }

        StudentCurricularPlan studentCurricularPlan = erasmusBolonhaStudentEnrollmentBean.getStudentCurricularPlan();
        ExecutionSemester executionSemester = erasmusBolonhaStudentEnrollmentBean.getExecutionPeriod();
        NoCourseGroupCurriculumGroup group =
                studentCurricularPlan.getNoCourseGroupCurriculumGroup(NoCourseGroupCurriculumGroupType.STANDALONE);
        Set<CurricularCourse> remaining = new HashSet<CurricularCourse>();
        HashSet<CurricularCourse> set = new HashSet<CurricularCourse>();
        set.addAll(erasmusBolonhaStudentEnrollmentBean.getCandidacy().getCurricularCoursesSet());
        for (Enrolment enrolment : group.getEnrolments()) {
            set.add(enrolment.getCurricularCourse());
        }

        remaining.addAll(set);

        for (ErasmusExtraCurricularEnrolmentBean bean : erasmusBolonhaStudentEnrollmentBean.getExtraCurricularEnrolments()) {
            remaining.remove(bean.getCurricularCourse());
            if (group.hasEnrolmentWithEnroledState(bean.getCurricularCourse(),
                    erasmusBolonhaStudentEnrollmentBean.getExecutionPeriod())) {
                continue;
            }

            MobilityExtraEnrolmentBean mobilityExtraEnrolmentBean =
                    new MobilityExtraEnrolmentBean(studentCurricularPlan, executionSemester);

            mobilityExtraEnrolmentBean.setCurriculumGroup(studentCurricularPlan
                    .getNoCourseGroupCurriculumGroup(NoCourseGroupCurriculumGroupType.STANDALONE));
            mobilityExtraEnrolmentBean.setDegree(bean.getCurricularCourse().getDegree());
            mobilityExtraEnrolmentBean.setDegreeType(bean.getCurricularCourse().getDegree().getDegreeType());
            mobilityExtraEnrolmentBean.setDegreeCurricularPlan(bean.getCurricularCourse().getDegreeCurricularPlan());
            mobilityExtraEnrolmentBean.setSelectedCurricularCourse(bean.getCurricularCourse());
            mobilityExtraEnrolmentBean.setCurricularRuleLevel(CurricularRuleLevel.EXTRA_ENROLMENT);

            try {
                final RuleResult ruleResult = CreateExtraEnrolment.run(mobilityExtraEnrolmentBean);

                if (ruleResult.isWarning()) {
                    addRuleResultMessagesToActionMessages("warning", request, ruleResult);
                }

            } catch (final IllegalDataAccessException e) {
                addActionMessage("error", request, "error.notAuthorized");
                return enrolStudent(mapping, form, request, response);

            } catch (final EnrollmentDomainException ex) {
                addRuleResultMessagesToActionMessages("enrolmentError", request, ex.getFalseResult());
                return enrolStudent(mapping, form, request, response);

            } catch (final DomainException e) {
                addActionMessage("error", request, e.getLocalizedMessage(), false);
                return enrolStudent(mapping, form, request, response);
            }
        }

        // After adding all that I want to add, the ones that I've not added
        // that were enrolled are to be removed.
        for (Enrolment enrolment : group.getEnrolmentsBy(executionSemester)) {
            if (remaining.contains(enrolment.getCurricularCourse())) {
                studentCurricularPlan.removeCurriculumModulesFromNoCourseGroupCurriculumGroup(
                        Collections.<CurriculumModule> singletonList(enrolment), executionSemester,
                        NoCourseGroupCurriculumGroupType.STANDALONE);
            }
        }
        MobilityIndividualApplicationProcess process = getProcess(request);
        MobilityIndividualApplication candidacy = process.getCandidacy();
        ErasmusBolonhaStudentEnrollmentBean bean =
                new ErasmusBolonhaStudentEnrollmentBean(candidacy.getRegistration().getActiveStudentCurricularPlan(),
                        erasmusBolonhaStudentEnrollmentBean.getExecutionPeriod(), null, CurricularRuleLevel.ENROLMENT_NO_RULES,
                        candidacy);
        RenderUtils.invalidateViewState();
        return enrolStudent(mapping, request, getProcess(request), bean);
    }

    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixServiceException {

        return null;

    }

    protected BolonhaStudentEnrollmentBean getBolonhaStudentEnrollmentBeanFromViewState() {
        return getRenderedObject("bolonhaStudentEnrolments");
    }

    public ActionForward prepareChooseCycleCourseGroupToEnrol(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final ErasmusBolonhaStudentEnrollmentBean studentEnrollmentBean =
                (ErasmusBolonhaStudentEnrollmentBean) getRenderedObject();

        final CycleEnrolmentBean cycleEnrolmentBean =
                new CycleEnrolmentBean(studentEnrollmentBean.getStudentCurricularPlan(),
                        studentEnrollmentBean.getExecutionPeriod(), studentEnrollmentBean.getCycleTypeToEnrol()
                                .getSourceCycleAffinity(), studentEnrollmentBean.getCycleTypeToEnrol());
        request.setAttribute("cycleEnrolmentBean", cycleEnrolmentBean);
        request.setAttribute("withRules", false);
        request.setAttribute("process", studentEnrollmentBean.getCandidacy().getCandidacyProcess());
        return mapping.findForward("chooseCycleCourseGroupToEnrol");
    }

    public ActionForward cancelChooseCycleCourseGroupToEnrol(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        return enrolStudent(mapping, form, request, response);
    }

    public ActionForward enrolInCycleCourseGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        final CycleEnrolmentBean cycleEnrolmentBean = getCycleEnrolmentBeanFromViewState();

        try {
            cycleEnrolmentBean.getStudentCurricularPlan().enrolInAffinityCycle(cycleEnrolmentBean.getCycleCourseGroupToEnrol(),
                    cycleEnrolmentBean.getExecutionPeriod());

        } catch (final IllegalDataAccessException e) {
            addActionMessage(request, "error.NotAuthorized");

            request.setAttribute("withRules", request.getParameter("withRules"));
            request.setAttribute("cycleEnrolmentBean", cycleEnrolmentBean);
            return mapping.findForward("chooseCycleCourseGroupToEnrol");

        } catch (final DomainException e) {
            addActionMessage(request, e.getLocalizedMessage(), false);

            request.setAttribute("withRules", request.getParameter("withRules"));
            request.setAttribute("cycleEnrolmentBean", cycleEnrolmentBean);
            return mapping.findForward("chooseCycleCourseGroupToEnrol");
        }

        return enrolStudent(mapping, form, request, response);
    }

    private CycleEnrolmentBean getCycleEnrolmentBeanFromViewState() {
        return getRenderedObject("cycleEnrolmentBean");
    }
}
