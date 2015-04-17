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
package org.fenixedu.academic.ui.struts.action.publico.candidacies.erasmus;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Country;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Installation;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.PublicCandidacyHashCode;
import org.fenixedu.academic.domain.candidacyProcess.CandidacyProcess;
import org.fenixedu.academic.domain.candidacyProcess.DegreeOfficePublicCandidacyHashCode;
import org.fenixedu.academic.domain.candidacyProcess.DegreeOfficePublicCandidacyHashCodeOperations;
import org.fenixedu.academic.domain.candidacyProcess.IndividualCandidacyDocumentFile;
import org.fenixedu.academic.domain.candidacyProcess.IndividualCandidacyProcessBean;
import org.fenixedu.academic.domain.candidacyProcess.erasmus.ApprovedLearningAgreementDocumentFile;
import org.fenixedu.academic.domain.candidacyProcess.erasmus.ErasmusApplyForSemesterType;
import org.fenixedu.academic.domain.candidacyProcess.exceptions.HashCodeForEmailAndProcessAlreadyBounded;
import org.fenixedu.academic.domain.candidacyProcess.mobility.MobilityApplicationProcess;
import org.fenixedu.academic.domain.candidacyProcess.mobility.MobilityIndividualApplicationProcess;
import org.fenixedu.academic.domain.candidacyProcess.mobility.MobilityIndividualApplicationProcessBean;
import org.fenixedu.academic.domain.candidacyProcess.mobility.MobilityProgram;
import org.fenixedu.academic.domain.candidacyProcess.mobility.MobilityQuota;
import org.fenixedu.academic.domain.candidacyProcess.mobility.MobilityStudentDataBean;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.organizationalStructure.UniversityUnit;
import org.fenixedu.academic.domain.person.IDDocumentType;
import org.fenixedu.academic.domain.util.email.EmailBean;
import org.fenixedu.academic.domain.util.email.Recipient;
import org.fenixedu.academic.domain.util.email.SystemSender;
import org.fenixedu.academic.dto.person.PersonBean;
import org.fenixedu.academic.report.candidacy.erasmus.LearningAgreementDocument;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.ui.struts.action.candidacy.erasmus.DegreeCourseInformationBean;
import org.fenixedu.academic.ui.struts.action.commons.FenixActionForward;
import org.fenixedu.academic.ui.struts.action.publico.PublicApplication.PublicCandidaciesApp;
import org.fenixedu.academic.ui.struts.action.publico.candidacies.RefactoredIndividualCandidacyProcessPublicDA;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.report.ReportsUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;
import org.fenixedu.commons.i18n.I18N;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixWebFramework.rendererExtensions.util.IPresentableEnum;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

@StrutsFunctionality(app = PublicCandidaciesApp.class, path = "mobility", titleKey = "title.application.name.mobility")
@Mapping(path = "/candidacies/caseHandlingMobilityApplicationIndividualProcess", module = "publico")
@Forwards({
        @Forward(name = "show-pre-creation-candidacy-form", path = "/publico/candidacy/mobility/preCreationCandidacyForm.jsp"),
        @Forward(name = "show-email-message-sent", path = "/publico/candidacy/mobility/showEmailSent.jsp"),
        @Forward(name = "show-application-submission-conditions",
                path = "/publico/candidacy/mobility/applicationSubmissionConditions.jsp"),
        @Forward(name = "open-candidacy-processes-not-found",
                path = "/publico/candidacy/mobility/individualCandidacyNotFound.jsp"),
        @Forward(name = "show-candidacy-creation-page", path = "/publico/candidacy/mobility/createCandidacyPartOne.jsp"),
        @Forward(name = "candidacy-continue-creation", path = "/publico/candidacy/mobility/createCandidacyPartTwo.jsp"),
        @Forward(name = "choose-mobility-program", path = "/publico/candidacy/mobility/chooseMobilityProgram.jsp"),
        @Forward(name = "fill-degree-and-courses-information",
                path = "/publico/candidacy/mobility/fillDegreeAndCoursesInformation.jsp"),
        @Forward(name = "accept-honour-declaration", path = "/publico/candidacy/mobility/acceptHonourDeclaration.jsp"),
        @Forward(name = "inform-submited-candidacy", path = "/publico/candidacy/mobility/candidacySubmited.jsp"),
        @Forward(name = "show-candidacy-details", path = "/publico/candidacy/mobility/viewCandidacy.jsp"),
        @Forward(name = "edit-candidacy", path = "/publico/candidacy/mobility/editCandidacy.jsp"),
        @Forward(name = "edit-candidacy-information", path = "/publico/candidacy/mobility/editCandidacyInformation.jsp"),
        @Forward(name = "edit-candidacy-degree-and-courses",
                path = "/publico/candidacy/mobility/editCandidacyDegreeAndCourses.jsp"),
        @Forward(name = "edit-candidacy-documents", path = "/publico/candidacy/mobility/editCandidacyDocuments.jsp"),
        @Forward(name = "edit-candidacy-degree-and-courses",
                path = "/publico/candidacy/mobility/editCandidacyDegreeAndCourses.jsp"),
        @Forward(name = "redirect-to-peps", path = "/publico/candidacy/mobility/peps.jsp"),
        @Forward(name = "show-application-submission-conditions-for-stork",
                path = "/publico/candidacy/mobility/applicationSubmissionConditionsForStork.jsp"),
        @Forward(name = "show-stork-error-page", path = "/publico/candidacy/mobility/showStorkErrorPage.jsp"),
        @Forward(name = "stork-candidacy-already-bounded", path = "/publico/candidacy/mobility/candidacyAlreadyBounded.jsp"),
        @Forward(name = "redirect-to-peps-to-access-application", path = "/publico/candidacy/mobility/pepsAccessAplication.jsp"),
        @Forward(name = "stork-error-authentication-failed", path = "/publico/candidacy/mobility/storkAuthenticationFailed.jsp"),
        @Forward(name = "show-recover-access-link-form", path = "/publico/candidacy/mobility/recoverAccess.jsp"),
        @Forward(name = "show-recovery-email-sent", path = "/publico/candidacy/mobility/recoveryEmailSent.jsp"),
        @Forward(name = "stork-attr-list-test", path = "/publico/candidacy/mobility/storkAttrListTest.jsp"),
        @Forward(name = "error-on-application-submission", path = "/publico/candidacy/mobility/errorOnSubmission.jsp"),
        @Forward(name = "bind-link-submited-individual-candidacy-with-stork",
                path = "/publico/candidacy/mobility/bindSubmitedIndividualCandidacyWithStork.jsp"),
        @Forward(name = "show-bind-process-success", path = "/publico/candidacy/mobility/showBindProcessSuccess.jsp"),
        @Forward(name = "open-candidacy-process-closed", path = "/publico/candidacy/mobility/candidacyProcessClosed.jsp") })
public class ErasmusIndividualCandidacyProcessPublicDA extends RefactoredIndividualCandidacyProcessPublicDA {

    private static final Logger logger = LoggerFactory.getLogger(ErasmusIndividualCandidacyProcessPublicDA.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        setLocale(request, Locale.ENGLISH);
        I18N.setLocale(request.getSession(), Locale.ENGLISH);

        return super.execute(mapping, actionForm, request, response);
    }

    @Override
    protected String getCandidacyInformationLinkDefaultLanguage() {
        return getStringFromDefaultBundle("link.candidacy.information.default.erasmus");
    }

    @Override
    protected String getCandidacyInformationLinkEnglish() {
        return getStringFromDefaultBundle("link.candidacy.information.english.erasmus");
    }

    @Override
    protected String getCandidacyNameKey() {
        return "title.application.name.mobility";
    }

    @Override
    public ActionForward viewCandidacy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        MobilityIndividualApplicationProcess individualCandidacyProcess =
                (MobilityIndividualApplicationProcess) request.getAttribute("individualCandidacyProcess");

        if (individualCandidacyProcess == null) {
            individualCandidacyProcess = (MobilityIndividualApplicationProcess) getProcess(request);
        }

        if (request.getAttribute("individualCandidacyProcessBean") == null) {
            MobilityIndividualApplicationProcessBean bean =
                    new MobilityIndividualApplicationProcessBean(individualCandidacyProcess);
            bean.setPersonBean(new PersonBean(individualCandidacyProcess.getPersonalDetails()));
            request.setAttribute("individualCandidacyProcessBean", bean);
        }

        return mapping.findForward("show-candidacy-details");
    }

    @Override
    protected Class getParentProcessType() {
        return MobilityApplicationProcess.class;
    }

    @Override
    protected Class getProcessType() {
        return MobilityIndividualApplicationProcess.class;
    }

    public ActionForward intro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return new FenixActionForward(request, new ActionForward("http://nmci.ist.utl.pt/en/ist/erasmus/", true));
    }

    public ActionForward chooseSubmissionType(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return new FenixActionForward(request, new ActionForward("http://nmci.ist.utl.pt/en/ist/erasmus/", true));
    }

    public ActionForward prepareCandidacyCreation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        DegreeOfficePublicCandidacyHashCode candidacyHashCode =
                (DegreeOfficePublicCandidacyHashCode) PublicCandidacyHashCode.getPublicCandidacyCodeByHash(request
                        .getParameter("hash"));

        if (candidacyHashCode.getIndividualCandidacyProcess() != null) {
            request.setAttribute("individualCandidacyProcess", candidacyHashCode.getIndividualCandidacyProcess());
            return viewCandidacy(mapping, form, request, response);
        }

        ActionForward actionForwardError = verifySubmissionPreconditions(mapping);
        if (actionForwardError != null) {
            return actionForwardError;
        }

        CandidacyProcess candidacyProcess = getCurrentOpenParentProcess();

        MobilityIndividualApplicationProcessBean bean = new MobilityIndividualApplicationProcessBean(candidacyProcess);
        bean.setPersonBean(new PersonBean());
        bean.getPersonBean().setIdDocumentType(IDDocumentType.FOREIGNER_IDENTITY_CARD);
        bean.setPublicCandidacyHashCode(candidacyHashCode);

        request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
        bean.getPersonBean().setEmail(candidacyHashCode.getEmail());
        return mapping.findForward("show-candidacy-creation-page");
    }

    @Override
    public ActionForward fillExternalPrecedentInformation(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        ActionForward actionForwardError = verifySubmissionPreconditions(mapping);
        if (actionForwardError != null) {
            return actionForwardError;
        }

        request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
        return mapping.findForward("candidacy-continue-creation");
    }

    public ActionForward chooseMobilityProgram(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        ActionForward actionForwardError = verifySubmissionPreconditions(mapping);
        if (actionForwardError != null) {
            return actionForwardError;
        }

        MobilityIndividualApplicationProcessBean bean =
                (MobilityIndividualApplicationProcessBean) getIndividualCandidacyProcessBean();
        request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());

        if (bean.getMobilityStudentDataBean().getDateOfDeparture().isBefore(bean.getMobilityStudentDataBean().getDateOfArrival())) {
            addActionMessage("error", request, "mobility.error.date.of.departure.before.date.of.arrival");
            return mapping.findForward("candidacy-continue-creation");
        }
        if (!bean.getMobilityStudentDataBean().isSchoolLevelDefined()) {
            addActionMessage("error", request, "mobility.error.schoolLevel.not.defined");
            return mapping.findForward("candidacy-continue-creation");
        }

        if (bean.getMobilityStudentDataBean().getApplyFor() != ErasmusApplyForSemesterType.SECOND_SEMESTER
                && ExecutionYear.readCurrentExecutionYear().getNextExecutionYear() != null
                && bean.getMobilityStudentDataBean()
                        .getDateOfArrival()
                        .isAfter(
                                ExecutionYear.readCurrentExecutionYear().getNextExecutionYear().getLastExecutionPeriod()
                                        .getBeginDateYearMonthDay())) {
            addActionMessage("error", request, "mobility.error.wrong.period.for.spring.term.applications");
            return mapping.findForward("candidacy-continue-creation");
        }

        return mapping.findForward("choose-mobility-program");
    }

    public ActionForward fillDegreeInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        ActionForward actionForwardError = verifySubmissionPreconditions(mapping);
        if (actionForwardError != null) {
            return actionForwardError;
        }

        MobilityIndividualApplicationProcessBean bean =
                (MobilityIndividualApplicationProcessBean) getIndividualCandidacyProcessBean();
        request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());

        if (bean.getMobilityStudentDataBean().getSelectedMobilityProgram() == null) {
            addActionMessage("error", request, "mobility.error.mobility.cant.be.null");
            return mapping.findForward("choose-mobility-program");
        }
        request.setAttribute("mobilityIndividualApplicationProcessBean", bean);
        request.setAttribute("degreeCourseInformationBean",
                new DegreeCourseInformationBean((ExecutionYear) getCurrentOpenParentProcess().getCandidacyExecutionInterval(),
                        (MobilityApplicationProcess) bean.getCandidacyProcess()));

        return mapping.findForward("fill-degree-and-courses-information");
    }

    private DegreeCourseInformationBean readDegreeCourseInformationBean(HttpServletRequest request) {
        return getRenderedObject("degree.course.information.bean");
    }

    private MobilityIndividualApplicationProcessBean readMobilityDegreeInformation(HttpServletRequest request) {
        return getRenderedObject("mobility.individual.application");
    }

    public ActionForward chooseDegree(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        ActionForward actionForwardError = verifySubmissionPreconditions(mapping);
        if (actionForwardError != null) {
            return actionForwardError;
        }

        MobilityIndividualApplicationProcessBean bean = readMobilityDegreeInformation(request);
        request.setAttribute("mobilityIndividualApplicationProcessBean", bean);
        request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
        request.setAttribute("degreeCourseInformationBean", readDegreeCourseInformationBean(request));

        RenderUtils.invalidateViewState();

        if ("editCandidacy".equals(request.getParameter("userAction"))) {
            return mapping.findForward("edit-candidacy-degree-and-courses");
        }

        return mapping.findForward("fill-degree-and-courses-information");
    }

    public ActionForward chooseDegreeForMobility(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        ActionForward actionForwardError = verifySubmissionPreconditions(mapping);
        if (actionForwardError != null) {
            return actionForwardError;
        }

        MobilityIndividualApplicationProcessBean bean = readMobilityDegreeInformation(request);

        request.setAttribute("selectDegreeView", true);
        request.setAttribute("mobilityIndividualApplicationProcessBean", bean);
        request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
        request.setAttribute("degreeCourseInformationBean", readDegreeCourseInformationBean(request));

        RenderUtils.invalidateViewState();

        if ("editCandidacy".equals(request.getParameter("userAction"))) {
            return mapping.findForward("edit-candidacy-degree-and-courses");
        }

        return mapping.findForward("fill-degree-and-courses-information");
    }

    public ActionForward addCourse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        ActionForward actionForwardError = verifySubmissionPreconditions(mapping);
        if (actionForwardError != null) {
            return actionForwardError;
        }

        request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());

        MobilityIndividualApplicationProcessBean bean =
                (MobilityIndividualApplicationProcessBean) getIndividualCandidacyProcessBean();
        DegreeCourseInformationBean degreeCourseBean = readDegreeCourseInformationBean(request);

        if (degreeCourseBean.getChosenCourse() != null) {
            bean.addCurricularCourse(degreeCourseBean.getChosenCourse());
        }

        request.setAttribute("degreeCourseInformationBean", readDegreeCourseInformationBean(request));
        request.setAttribute("mobilityIndividualApplicationProcessBean", bean);
        RenderUtils.invalidateViewState();

        if ("editCandidacy".equals(request.getParameter("userAction"))) {
            return mapping.findForward("edit-candidacy-degree-and-courses");
        }

        return mapping.findForward("fill-degree-and-courses-information");
    }

    public ActionForward removeCourse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        ActionForward actionForwardError = verifySubmissionPreconditions(mapping);
        if (actionForwardError != null) {
            return actionForwardError;
        }

        request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());

        MobilityIndividualApplicationProcessBean bean =
                (MobilityIndividualApplicationProcessBean) getIndividualCandidacyProcessBean();
        DegreeCourseInformationBean degreeCourseBean = readDegreeCourseInformationBean(request);

        CurricularCourse courseToRemove = getDomainObject(request, "removeCourseId");
        bean.removeCurricularCourse(courseToRemove);

        request.setAttribute("degreeCourseInformationBean", readDegreeCourseInformationBean(request));
        request.setAttribute("mobilityIndividualApplicationProcessBean", bean);
        RenderUtils.invalidateViewState();

        if ("editCandidacy".equals(request.getParameter("userAction"))) {
            return mapping.findForward("edit-candidacy-degree-and-courses");
        }

        return mapping.findForward("fill-degree-and-courses-information");
    }

    public ActionForward acceptHonourDeclaration(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        ActionForward actionForwardError = verifySubmissionPreconditions(mapping);
        if (actionForwardError != null) {
            return actionForwardError;
        }

        MobilityIndividualApplicationProcessBean bean =
                (MobilityIndividualApplicationProcessBean) getIndividualCandidacyProcessBean();
        request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);

        try {
            MobilityQuota quota = bean.determineMobilityQuota();
        } catch (DomainException e) {
            addActionMessage("error", request, e.getMessage());
            request.setAttribute("degreeCourseInformationBean", readDegreeCourseInformationBean(request));
            request.setAttribute("mobilityIndividualApplicationProcessBean", bean);
            RenderUtils.invalidateViewState();

            return mapping.findForward("fill-degree-and-courses-information");
        }

        return mapping.findForward("accept-honour-declaration");
    }

    public ActionForward submitCandidacy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException, FenixServiceException {
        try {
            ActionForward actionForwardError = verifySubmissionPreconditions(mapping);
            if (actionForwardError != null) {
                return actionForwardError;
            }

            MobilityIndividualApplicationProcessBean bean =
                    (MobilityIndividualApplicationProcessBean) getIndividualCandidacyProcessBean();
            bean.setInternalPersonCandidacy(Boolean.TRUE);

            boolean isValid = hasInvalidViewState();
            if (!isValid) {
                invalidateDocumentFileRelatedViewStates();
                request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
                return mapping.findForward("accept-honour-declaration");
            }

            if (candidacyIndividualProcessExistsForThisEmail(bean.getPersonBean().getEmail())) {
                addActionMessage("error", request, "error.candidacy.hash.code.already.bounded");
                invalidateDocumentFileRelatedViewStates();
                request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
                return mapping.findForward("candidacy-continue-creation");
            }

            if (!bean.getHonorAgreement()) {
                addActionMessage("error", request, "error.must.agree.on.declaration.of.honor");
                invalidateDocumentFileRelatedViewStates();
                request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
                return mapping.findForward("accept-honour-declaration");
            }

            if (bean.isToAccessFenix() && bean.getPublicCandidacyHashCode() == null) {
                DegreeOfficePublicCandidacyHashCode candidacyHashCode = null;
                try {
                    candidacyHashCode =
                            DegreeOfficePublicCandidacyHashCodeOperations
                                    .getUnusedOrCreateNewHashCodeAndSendEmailForApplicationSubmissionToCandidate(
                                            getProcessType(), getCurrentOpenParentProcess(), bean.getPersonBean().getEmail());
                    bean.setPublicCandidacyHashCode(candidacyHashCode);
                } catch (HashCodeForEmailAndProcessAlreadyBounded e) {
                    addActionMessage(request, "error.candidacy.hash.code.already.bounded");
                    return mapping.findForward("show-pre-creation-candidacy-form");
                }
            }

            MobilityIndividualApplicationProcess process = (MobilityIndividualApplicationProcess) createNewPublicProcess(bean);

            request.setAttribute("process", process);
            request.setAttribute("mappingPath", mapping.getPath());
            request.setAttribute("individualCandidacyProcess", process);
            request.setAttribute("endSubmissionDate", getFormattedApplicationSubmissionEndDate());

            return mapping.findForward("inform-submited-candidacy");
        } catch (DomainException e) {
            addActionMessage("error", request, e.getMessage(), e.getArgs());
            logger.error(e.getMessage(), e);
            getIndividualCandidacyProcessBean().getPersonBean().setPerson(null);
            request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
            sendSubmissionErrorReportMail(getIndividualCandidacyProcessBean(), e);
            return mapping.findForward("error-on-application-submission");
        }
    }

    public ActionForward editCandidacyProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        MobilityIndividualApplicationProcessBean bean =
                (MobilityIndividualApplicationProcessBean) getIndividualCandidacyProcessBean();
        try {
            ActionForward actionForwardError = verifySubmissionPreconditions(mapping);
            if (actionForwardError != null) {
                return actionForwardError;
            }

            PersonBean personBean = bean.getPersonBean();
            final Set<Person> persons = new HashSet<Person>(Person.readByDocumentIdNumber(personBean.getDocumentIdNumber()));

            if (persons.size() > 1) {
                addActionMessage("individualCandidacyMessages", request, "mobility.error.person.with.same.identifier.exists");
                return prepareEditCandidacyProcess(mapping, form, request, response);
            } else if (persons.size() == 1
                    && persons.iterator().next() != bean.getIndividualCandidacyProcess().getPersonalDetails().getPerson()) {
                addActionMessage("individualCandidacyMessages", request, "mobility.error.person.with.same.identifier.exists");
                return prepareEditCandidacyProcess(mapping, form, request, response);
            }

            executeActivity(bean.getIndividualCandidacyProcess(), "EditPublicCandidacyPersonalInformation",
                    getIndividualCandidacyProcessBean());
        } catch (final DomainException e) {
            if (e.getMessage().equals("error.IndividualCandidacyEvent.invalid.payment.code")) {
                throw e;
            }

            addActionMessage(request, e.getMessage(), e.getArgs());
            request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
            return mapping.findForward("edit-candidacy");
        }

        request.setAttribute("individualCandidacyProcess", bean.getIndividualCandidacyProcess());
        return backToViewCandidacyInternal(mapping, form, request, response);
    }

    public ActionForward submitWithNationalCitizenCard(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        ActionForward actionForwardError = verifySubmissionPreconditions(mapping);
        if (actionForwardError != null) {
            return actionForwardError;
        }

        return mapping.findForward("redirect-to-peps");
    }

    public ActionForward accessApplicationWithNationalCitizenCard(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("redirect-to-peps-to-access-application");
    }

    public ActionForward prepareCandidacyCreationForStork(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        ActionForward actionForwardError = verifySubmissionPreconditions(mapping);
        if (actionForwardError != null) {
            return actionForwardError;
        }

        request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
        return mapping.findForward("show-candidacy-creation-page");

    }

    public ActionForward prepareEditCandidacyInformation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        ActionForward actionForwardError = verifySubmissionPreconditions(mapping);
        if (actionForwardError != null) {
            return actionForwardError;
        }

        request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
        return mapping.findForward("edit-candidacy-information");
    }

    @Override
    public ActionForward editCandidacyDocuments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException, IOException {
        ActionForward actionForwardError = verifySubmissionPreconditions(mapping);
        if (actionForwardError != null) {
            return actionForwardError;
        }
        return super.editCandidacyDocuments(mapping, form, request, response);
    }

    @Override
    public ActionForward editCandidacyProcessInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        ActionForward actionForwardError = verifySubmissionPreconditions(mapping);
        if (actionForwardError != null) {
            return actionForwardError;
        }
        return super.editCandidacyProcessInvalid(mapping, form, request, response);
    }

    @Override
    public ActionForward prepareEditCandidacyDocuments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        ActionForward actionForwardError = verifySubmissionPreconditions(mapping);
        if (actionForwardError != null) {
            return actionForwardError;
        }
        return super.prepareEditCandidacyDocuments(mapping, form, request, response);
    }

    public ActionForward editCandidacyInformationInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        ActionForward actionForwardError = verifySubmissionPreconditions(mapping);
        if (actionForwardError != null) {
            return actionForwardError;
        }

        request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
        return mapping.findForward("edit-candidacy-information");
    }

    public ActionForward editCandidacyInformation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        MobilityIndividualApplicationProcessBean bean =
                (MobilityIndividualApplicationProcessBean) getIndividualCandidacyProcessBean();
        try {
            ActionForward actionForwardError = verifySubmissionPreconditions(mapping);
            if (actionForwardError != null) {
                return actionForwardError;
            }

            if (bean.getMobilityStudentDataBean().getDateOfDeparture()
                    .isBefore(bean.getMobilityStudentDataBean().getDateOfArrival())) {
                addActionMessage("error", request, "mobility.error.date.of.departure.before.date.of.arrival");
                request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
                return mapping.findForward("edit-candidacy-information");
            }

            executeActivity(bean.getIndividualCandidacyProcess(), "EditPublicCandidacyInformation",
                    getIndividualCandidacyProcessBean());
        } catch (final DomainException e) {
            if (e.getMessage().equals("error.IndividualCandidacyEvent.invalid.payment.code")) {
                throw e;
            }

            addActionMessage(request, e.getMessage(), e.getArgs());
            request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
            return mapping.findForward("edit-candidacy-information");
        }

        request.setAttribute("individualCandidacyProcess", bean.getIndividualCandidacyProcess());
        return backToViewCandidacyInternal(mapping, form, request, response);
    }

    public ActionForward prepareEditDegreeAndCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        ActionForward actionForwardError = verifySubmissionPreconditions(mapping);
        if (actionForwardError != null) {
            return actionForwardError;
        }

        MobilityIndividualApplicationProcessBean bean =
                (MobilityIndividualApplicationProcessBean) getIndividualCandidacyProcessBean();
        request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
        request.setAttribute("degreeCourseInformationBean", new DegreeCourseInformationBean(
                (ExecutionYear) getCurrentOpenParentProcess().getCandidacyExecutionInterval(),
                (MobilityApplicationProcess) getIndividualCandidacyProcessBean().getCandidacyProcess()));
        request.setAttribute("mobilityIndividualApplicationProcessBean", bean);
        return mapping.findForward("edit-candidacy-degree-and-courses");
    }

    public ActionForward prepareEditDegreeAndCoursesInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        ActionForward actionForwardError = verifySubmissionPreconditions(mapping);
        if (actionForwardError != null) {
            return actionForwardError;
        }

        request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
        return mapping.findForward("edit-candidacy-degree-and-courses");
    }

    public ActionForward editDegreeAndCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        MobilityIndividualApplicationProcessBean bean =
                (MobilityIndividualApplicationProcessBean) getIndividualCandidacyProcessBean();
        try {
            ActionForward actionForwardError = verifySubmissionPreconditions(mapping);
            if (actionForwardError != null) {
                return actionForwardError;
            }

            MobilityQuota quota = bean.determineMobilityQuota();
            executeActivity(bean.getIndividualCandidacyProcess(), "EditPublicDegreeAndCoursesInformation",
                    getIndividualCandidacyProcessBean());
        } catch (final DomainException e) {
            request.setAttribute("mobilityIndividualApplicationProcessBean", bean);
            request.setAttribute("degreeCourseInformationBean", readDegreeCourseInformationBean(request));
            request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
            addActionMessage("error", request, e.getMessage());
            RenderUtils.invalidateViewState();
            return mapping.findForward("edit-candidacy-degree-and-courses");
        }

        request.setAttribute("individualCandidacyProcess", bean.getIndividualCandidacyProcess());
        return backToViewCandidacyInternal(mapping, form, request, response);
    }

    private static final String f(String format, Object... args) {
        return String.format(format, args);
    }

    public ActionForward chooseCountry(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        ActionForward actionForwardError = verifySubmissionPreconditions(mapping);
        if (actionForwardError != null) {
            return actionForwardError;
        }

        MobilityIndividualApplicationProcessBean bean =
                (MobilityIndividualApplicationProcessBean) getIndividualCandidacyProcessBean();
        request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
        request.setAttribute("degreeCourseInformationBean", readDegreeCourseInformationBean(request));

        RenderUtils.invalidateViewState();

        if ("editCandidacy".equals(request.getParameter("userAction"))) {
            bean.getMobilityStudentDataBean().setSelectedUniversity(null);

            return mapping.findForward("edit-candidacy-degree-and-courses");
        }

        return mapping.findForward("candidacy-continue-creation");
    }

    public ActionForward chooseUniversity(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        ActionForward actionForwardError = verifySubmissionPreconditions(mapping);
        if (actionForwardError != null) {
            return actionForwardError;
        }

        MobilityIndividualApplicationProcessBean bean =
                (MobilityIndividualApplicationProcessBean) getIndividualCandidacyProcessBean();
        request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
        request.setAttribute("degreeCourseInformationBean", readDegreeCourseInformationBean(request));

        RenderUtils.invalidateViewState();

        return mapping.findForward("edit-candidacy-degree-and-courses");
    }

    public ActionForward retrieveLearningAgreement(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        MobilityIndividualApplicationProcess process = (MobilityIndividualApplicationProcess) getProcess(request);

        final LearningAgreementDocument document = new LearningAgreementDocument(process);
        byte[] data = ReportsUtils.generateReport(document).getData();

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

    public ActionForward retrieveApprovedLearningAgreement(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final ApprovedLearningAgreementDocumentFile file = getDomainObject(request, "agreementId");
        final String hash = request.getParameter("hash");

        final MobilityIndividualApplicationProcess process = file.getProcess();
        final DegreeOfficePublicCandidacyHashCode candidacyHashCode = process.getCandidacyHashCode();
        if (candidacyHashCode.getValue().equals(hash)) {
            final byte[] content = file.getContent();
            response.setContentLength(content.length);
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", "attachment; filename=" + file.getFilename());

            final ServletOutputStream writer = response.getOutputStream();
            writer.write(content);
            writer.flush();
            writer.close();

            response.flushBuffer();
        }

        return null;
    }

    @Override
    public ActionForward continueCandidacyCreation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        MobilityIndividualApplicationProcessBean bean =
                (MobilityIndividualApplicationProcessBean) getIndividualCandidacyProcessBean();

        final PersonBean personBean = bean.getPersonBean();

        if (existsIndividualCandidacyProcessForDocumentId(request, personBean.getIdDocumentType(),
                personBean.getDocumentIdNumber())) {
            addActionMessage("individualCandidacyMessages", request, "mobility.error.candidacy.for.person.already.exists");
            return executeCreateCandidacyPersonalInformationInvalid(mapping, form, request, response);
        }

        final Set<Person> persons = new HashSet<Person>(Person.readByDocumentIdNumber(personBean.getDocumentIdNumber()));

        if (persons.size() > 1) {
            addActionMessage("individualCandidacyMessages", request, "mobility.error.person.with.same.identifier.exists.multiple");
            return executeCreateCandidacyPersonalInformationInvalid(mapping, form, request, response);

        } else if (persons.size() == 1) {
            Person person = persons.iterator().next();

            if (person.getStudent() != null && person.getStudent().hasActiveRegistrations()) {
                addActionMessage("individualCandidacyMessages", request,
                        "mobility.error.person.with.same.identifier.exists.active.registration");
                return executeCreateCandidacyPersonalInformationInvalid(mapping, form, request, response);
            }

            if (person.getStudent() != null && !person.getStudent().getNumber().toString().equals(bean.getPersonNumber())) {
                addActionMessage("individualCandidacyMessages", request,
                        "mobility.error.person.with.same.identifier.exists.different.student", Unit.getInstitutionAcronym());
                return executeCreateCandidacyPersonalInformationInvalid(mapping, form, request, response);
            }

            personBean.setPerson(person);
        }

        IndividualCandidacyDocumentFile photoDocumentFile =
                createIndividualCandidacyDocumentFile(bean.getPhotoDocument(), bean.getPersonBean().getDocumentIdNumber());
        bean.getPhotoDocument().setDocumentFile(photoDocumentFile);
        request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());

        if (bean.isToAccessFenix() && !(personBean.getEmail().equals(personBean.getEmailConfirmation()))) {
            addActionMessage("individualCandidacyMessages", request, "mobility.error.emails.are.not.equals");
            return executeCreateCandidacyPersonalInformationInvalid(mapping, form, request, response);
        }

        if (bean.isToAccessFenix() && bean.getPublicCandidacyHashCode() == null) {
            DegreeOfficePublicCandidacyHashCode candidacyHashCode = null;
            candidacyHashCode =
                    DegreeOfficePublicCandidacyHashCode.getPublicCandidacyHashCodeByEmailAndCandidacyProcessType(bean
                            .getPersonBean().getEmail(), getProcessType(), getCurrentOpenParentProcess());

            if (candidacyHashCode != null && candidacyHashCode.getIndividualCandidacyProcess() != null) {
                addActionMessage("individualCandidacyMessages", request, "mobility.error.email.is.bounded.to.candidacy");
                return executeCreateCandidacyPersonalInformationInvalid(mapping, form, request, response);
            }

        }

        return mapping.findForward("candidacy-continue-creation");
    }

    public ActionForward prepareTestStorkAttrString(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("attrStringTestBean", new StorkAttrStringTestBean());

        return mapping.findForward("stork-attr-list-test");
    }

    public ActionForward prepareBindLinkSubmitedIndividualCandidacyWithStork(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        MobilityIndividualApplicationProcessBean bean =
                (MobilityIndividualApplicationProcessBean) getIndividualCandidacyProcessBean();
        request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);

        return mapping.findForward("bind-link-submited-individual-candidacy-with-stork");
    }

    public ActionForward answerNationalIdCardAvoidanceQuestion(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException {
        MobilityIndividualApplicationProcessBean bean =
                (MobilityIndividualApplicationProcessBean) getIndividualCandidacyProcessBean();

        try {
            executeActivity(bean.getIndividualCandidacyProcess(), "AnswerNationalIdCardAvoidanceOnSubmissionQuestion",
                    getIndividualCandidacyProcessBean());
        } catch (final DomainException e) {
            request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
            addActionMessage("error", request, e.getMessage());
            RenderUtils.invalidateViewState();
            return viewCandidacy(mapping, form, request, response);
        }

        return viewCandidacy(mapping, form, request, response);
    }

    public ActionForward answerNationalIdCardAvoidanceQuestionInvalid(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException {
        MobilityIndividualApplicationProcessBean bean =
                (MobilityIndividualApplicationProcessBean) getIndividualCandidacyProcessBean();
        request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);

        return viewCandidacy(mapping, form, request, response);
    }

    public ActionForward answerNationalIdCardAvoidanceQuestionPostback(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException {
        MobilityIndividualApplicationProcessBean bean =
                (MobilityIndividualApplicationProcessBean) getIndividualCandidacyProcessBean();
        request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);

        RenderUtils.invalidateViewState();

        return viewCandidacy(mapping, form, request, response);
    }

    public static class StorkAttrStringTestBean implements java.io.Serializable {
        /**
	 * 
	 */
        private static final long serialVersionUID = 1L;

        private String attrList;

        public String getAttrList() {
            return this.attrList;
        }

        public void setAttrList(String value) {
            this.attrList = value;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    // avmc: Methods for creating the mail to send to NMCI in case the submission goes wrong. //
    //       To avoid code replication, several functions were agregated on the same methods. //
    ////////////////////////////////////////////////////////////////////////////////////////////
    //avmc: common usage for filling empty strings.
    private String fillEmptyString(String value) {
        return StringUtils.isEmpty(value) ? "«« empty »»" : value;
    }

    //return string to append from a value String
    private String reportAppenderAuxString(String field, String value) {
        return field + "........: " + fillEmptyString(value) + "\n";
    }

    //return string to append from a value Object that can be turned to a simple String with toString
    private String reportAppenderAuxToStringable(String field, Object obj) {
        String value = null;
        if (obj != null) {
            value = obj.toString();
        }
        return reportAppenderAuxString(field, value);
    }

    //return string to append from a value Enum that implements getLocalizedName
    private String reportAppenderAuxEnum(String field, IPresentableEnum obj) {
        String value = null;
        if (obj != null) {
            value = obj.getLocalizedName();
        }
        return reportAppenderAuxString(field, value);
    }

    //return string to append from a value date (which can be a YearMonthDay or LocalDate)
    private String reportAppenderAuxDate(String field, Object obj) {
        final String DATE_FORMAT_STRING = "dd/MM/yyyy";
        String value = null;
        if (obj != null) {
            if (obj instanceof YearMonthDay) {
                value = ((YearMonthDay) obj).toString(DATE_FORMAT_STRING);
            } else if (obj instanceof LocalDate) {
                value = ((LocalDate) obj).toString(DATE_FORMAT_STRING);
            }
        }
        return reportAppenderAuxString(field, value);
    }

    //return string to append from a value Country (uses simple name)
    private String reportAppenderAuxCountry(String field, Country obj) {
        String value = null;
        if (obj != null) {
            value = obj.getLocalizedName().getContent();
        }
        return reportAppenderAuxString(field, value);
    }

    //return string to append from a value MobilityProgram (uses MultiLanguageString)
    private String reportAppenderAuxProgram(String field, MobilityProgram obj) {
        String value = null;
        if (obj != null) {
            value = obj.getName().getContent();
        }
        return reportAppenderAuxString(field, value);
    }

    //return string to append from a value Object that uses a presentation name string (UniversityUnit or Degree)
    private String reportAppenderAuxUnivUnitDegree(String field, Object obj) {
        String value = null;
        if (obj != null) {
            if (obj instanceof UniversityUnit) {
                value = ((UniversityUnit) obj).getPresentationName();
            } else if (obj instanceof Degree) {
                value = ((Degree) obj).getPresentationName();
            }
        }
        return reportAppenderAuxString(field, value);
    }

    //return string to append from a value List of Courses (I18N multilanguage string)
    private String reportAppenderAuxCourses(String field, List<CurricularCourse> obj) {
        StringBuilder value = new StringBuilder();
        if (obj != null && !obj.isEmpty()) {
            for (CurricularCourse curricularCourse : obj) {
                value.append("[");
                value.append(curricularCourse.getDegree().getSigla());
                value.append("] ");
                value.append(StringUtils.isEmpty(curricularCourse.getNameI18N().getContent()) ? curricularCourse.getName() : curricularCourse
                        .getNameI18N().getContent());
                value.append("; ");
            }
            if (value.length() > 0) {
                value.setLength(value.length() - 2);
            }
        }
        return reportAppenderAuxString(field, value.toString());
    }

    private void sendSubmissionErrorReportMail(IndividualCandidacyProcessBean individualCandidacyProcessBean,
            DomainException exception) {

        StringBuilder sb = new StringBuilder();
        MobilityIndividualApplicationProcessBean mobilityBean =
                (MobilityIndividualApplicationProcessBean) individualCandidacyProcessBean;

        // Email intro and error message
        sb.append(BundleUtil.getString(Bundle.CANDIDATE, "error.mobility.report.mail.intro"));
        sb.append("\n");
        sb.append("\nError message: ");
        sb.append(BundleUtil.getString(Bundle.APPLICATION, exception.getKey(), exception.getArgs()));
        sb.append("\n");

        // Data input from candidate
        PersonBean personBean = mobilityBean.getPersonBean();
        sb.append("\nPersonal data entered:\n");
        sb.append(reportAppenderAuxString("Name", personBean.getName()));
        sb.append(reportAppenderAuxEnum("Gender", personBean.getGender()));
        sb.append(reportAppenderAuxDate("Date of Birth", personBean.getDateOfBirth()));
        sb.append(reportAppenderAuxString("Documentation Number", personBean.getDocumentIdNumber()));
        sb.append(reportAppenderAuxCountry("Nationality", personBean.getNationality()));
        sb.append(reportAppenderAuxString("Address", personBean.getAddress()));
        sb.append(reportAppenderAuxString("Zip-Code", personBean.getAreaCode()));
        sb.append(reportAppenderAuxString("City.", personBean.getArea()));
        sb.append(reportAppenderAuxCountry("Country of Residence", personBean.getCountryOfResidence()));
        sb.append(reportAppenderAuxString("Phone", personBean.getPhone()));
        sb.append(reportAppenderAuxString("Email", personBean.getEmail()));
        sb.append(reportAppenderAuxString("Email Confirmation", personBean.getEmailConfirmation()));
        sb.append(reportAppenderAuxString("Student Number", mobilityBean.getPersonNumber()));

        MobilityStudentDataBean mobilityStudentDataBean = mobilityBean.getMobilityStudentDataBean();
        sb.append("\nMobility Data Entered:\n");
        sb.append(reportAppenderAuxCountry("Selected Country", mobilityStudentDataBean.getSelectedCountry()));
        sb.append(reportAppenderAuxUnivUnitDegree("Selected Univ", mobilityStudentDataBean.getSelectedUniversity()));
        sb.append(reportAppenderAuxEnum("School Level", mobilityStudentDataBean.getSchoolLevel()));
        sb.append(reportAppenderAuxString("Other School Level", mobilityStudentDataBean.getOtherSchoolLevel()));
        sb.append(reportAppenderAuxString("Exchange Coord Name",
                mobilityStudentDataBean.getHomeInstitutionExchangeCoordinatorName()));

        sb.append(reportAppenderAuxToStringable("Has Diploma/Degree", mobilityStudentDataBean.getHasDiplomaOrDegree()));
        sb.append(reportAppenderAuxString("Diploma Name", mobilityStudentDataBean.getDiplomaName()));
        sb.append(reportAppenderAuxToStringable("Diploma Year", mobilityStudentDataBean.getDiplomaConclusionYear()));
        sb.append(reportAppenderAuxToStringable("Experience Research", mobilityStudentDataBean.getExperienceCarryingOutProject()));

        sb.append(reportAppenderAuxDate("Date of Arrival", mobilityStudentDataBean.getDateOfArrival()));
        sb.append(reportAppenderAuxDate("Date of Departure", mobilityStudentDataBean.getDateOfDeparture()));
        sb.append(reportAppenderAuxToStringable("Types of Programme", mobilityStudentDataBean.getTypeOfProgrammeList()));

        sb.append(reportAppenderAuxString("Thesis Main Subject", mobilityStudentDataBean.getMainSubjectThesis()));
        sb.append(reportAppenderAuxToStringable("Has contacted Staff", mobilityStudentDataBean.getHasContactedOtherStaff()));
        sb.append(reportAppenderAuxString("Staff Name", mobilityStudentDataBean.getNameOfContact()));

        sb.append(reportAppenderAuxEnum("Applying for period", mobilityStudentDataBean.getApplyFor()));
        sb.append(reportAppenderAuxString("Observations", individualCandidacyProcessBean.getObservations()));

        sb.append("\nMobility Program, Degree and Courses:\n");
        sb.append(reportAppenderAuxProgram("Selected Program", mobilityStudentDataBean.getSelectedMobilityProgram()));
        sb.append(reportAppenderAuxUnivUnitDegree("Chosen Degree", mobilityBean.getDegree()));
        sb.append(reportAppenderAuxCourses("Chosen Courses", mobilityBean.getSortedSelectedCurricularCourses()));

        // Exception details
        sb.append("\n");
        sb.append(BundleUtil.getString(Bundle.CANDIDATE, "error.mobility.report.mail.stacktrace"));
        sb.append("\n");
        sb.append("\nException key: ");
        sb.append(exception.getKey());
        sb.append("\nException args:");
        if (exception.getArgs().length == 0) {
            sb.append("«« no args »»");
        } else {
            for (String arg : exception.getArgs()) {
                sb.append(" " + arg + ",");
            }
        }
        sb.deleteCharAt(sb.length() - 1); //remove surplus ','
        sb.append("\nException stacktrace:\n");
        sb.append(org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(exception)); // stacktrace to string
        sb.append("\n");

        // Email construction and sending
        String errorReportAddress = Installation.getInstance().getInstituitionalEmailAddress("nmci");
        String errorReportSubject =
                BundleUtil.getString(Bundle.CANDIDATE, "error.mobility.report.mail.subject", Unit.getInstitutionAcronym());
        String errorReportBody = sb.toString();

        SystemSender systemSender = Bennu.getInstance().getSystemSender();
        EmailBean emailBean = new EmailBean();
        emailBean.setSender(systemSender);
        emailBean.setReplyTos(systemSender.getConcreteReplyTos());
        emailBean.setRecipients(Collections.<Recipient> emptyList());
        emailBean.setSubject(errorReportSubject);
        emailBean.setMessage(errorReportBody);
        emailBean.setBccs(errorReportAddress);
        emailBean.send();
    }
}
