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
package org.fenixedu.academic.ui.struts.action.phd.candidacy.publicProgram.institution;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.PublicCandidacyHashCode;
import org.fenixedu.academic.domain.QualificationBean;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramDocumentType;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess.PublicPhdIndividualProgramProcess;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcessBean;
import org.fenixedu.academic.domain.phd.PhdParticipantBean;
import org.fenixedu.academic.domain.phd.PhdParticipantBean.PhdParticipantType;
import org.fenixedu.academic.domain.phd.PhdProgramCandidacyProcessState;
import org.fenixedu.academic.domain.phd.PhdProgramDocumentUploadBean;
import org.fenixedu.academic.domain.phd.PhdProgramProcessDocument;
import org.fenixedu.academic.domain.phd.candidacy.InstitutionPhdCandidacyPeriod;
import org.fenixedu.academic.domain.phd.candidacy.PhdCandidacyPeriod;
import org.fenixedu.academic.domain.phd.candidacy.PhdCandidacyReferee;
import org.fenixedu.academic.domain.phd.candidacy.PhdCandidacyRefereeBean;
import org.fenixedu.academic.domain.phd.candidacy.PhdCandidacyRefereeLetter;
import org.fenixedu.academic.domain.phd.candidacy.PhdCandidacyRefereeLetterBean;
import org.fenixedu.academic.domain.phd.candidacy.PhdProgramCandidacyProcess;
import org.fenixedu.academic.domain.phd.candidacy.PhdProgramCandidacyProcessBean;
import org.fenixedu.academic.domain.phd.candidacy.PhdProgramPublicCandidacyHashCode;
import org.fenixedu.academic.domain.phd.individualProcess.activities.AddAssistantGuidingInformation;
import org.fenixedu.academic.domain.phd.individualProcess.activities.AddCandidacyReferees;
import org.fenixedu.academic.domain.phd.individualProcess.activities.AddGuidingsInformation;
import org.fenixedu.academic.domain.phd.individualProcess.activities.AddQualification;
import org.fenixedu.academic.domain.phd.individualProcess.activities.DeleteAssistantGuiding;
import org.fenixedu.academic.domain.phd.individualProcess.activities.DeleteGuiding;
import org.fenixedu.academic.domain.phd.individualProcess.activities.EditIndividualProcessInformation;
import org.fenixedu.academic.domain.phd.individualProcess.activities.EditPersonalInformation;
import org.fenixedu.academic.domain.phd.individualProcess.activities.RemoveCandidacyReferee;
import org.fenixedu.academic.domain.phd.individualProcess.activities.UploadDocuments;
import org.fenixedu.academic.domain.phd.individualProcess.activities.ValidatedByCandidate;
import org.fenixedu.academic.dto.person.PersonBean;
import org.fenixedu.academic.dto.person.PhotographUploadBean;
import org.fenixedu.academic.dto.person.PhotographUploadBean.UnableToProcessTheImage;
import org.fenixedu.academic.service.services.caseHandling.CreateNewProcess;
import org.fenixedu.academic.service.services.caseHandling.ExecuteProcessActivity;
import org.fenixedu.academic.service.services.fileManager.UploadOwnPhoto;
import org.fenixedu.academic.service.services.person.qualification.DeleteQualification;
import org.fenixedu.academic.ui.struts.action.phd.candidacy.publicProgram.PublicPhdProgramCandidacyProcessDA;
import org.fenixedu.academic.ui.struts.action.publico.PublicApplication.PublicPhdApp;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.ContentType;
import org.fenixedu.academic.util.phd.InstitutionPhdCandidacyProcessProperties;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;
import org.fenixedu.commons.i18n.I18N;
import org.fenixedu.messaging.core.domain.Message;
import org.fenixedu.messaging.core.template.DeclareMessageTemplate;
import org.fenixedu.messaging.core.template.TemplateParameter;
import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.FenixFramework;

@DeclareMessageTemplate(id = "phd.submission.email.for.candidacy.message.template",
        description = "phd.submission.email.for.candidacy.message.description",
        subject = "phd.submission.email.for.candidacy.message.subject",
        text = "phd.submission.email.for.candidacy.message.body",
        parameters = {
                @TemplateParameter(id = "institutionAcronym", description = "phd.submission.email.for.candidacy.message.parameter.institutionAcronym"),
                @TemplateParameter(id = "candidacyLink", description = "phd.submission.email.for.candidacy.message.parameter.candidacyLink"),
                @TemplateParameter(id = "hashCodeValue", description = "phd.submission.email.for.candidacy.message.parameter.hashCodeValue")
        },
        bundle = Bundle.PHD
)
@DeclareMessageTemplate(id = "phd.application.submission.success.email.message.template",
        description = "phd.application.submission.success.email.message.description",
        subject = "phd.application.submission.success.email.message.subject",
        text = "phd.application.submission.success.email.message.body",
        parameters = {
                @TemplateParameter(id = "institutionAcronym", description = "phd.application.submission.success.email.message.parameter.institutionAcronym"),
                @TemplateParameter(id = "candidacyLink", description = "phd.application.submission.success.email.message.parameter.candidacyLink"),
                @TemplateParameter(id = "hashCodeValue", description = "phd.application.submission.success.email.message.parameter.hashCodeValue"),
                @TemplateParameter(id = "processNumber", description = "phd.application.submission.success.email.message.parameter.processNumber"),
        },
        bundle = Bundle.PHD
)

@StrutsFunctionality(app = PublicPhdApp.class, path = "candidacy", titleKey = "title.public.phd.program.candidacy")
@Mapping(path = "/applications/phd/phdProgramApplicationProcess", module = "publico")
@Forwards({
        @Forward(name = "outOfCandidacyPeriod", path = "/phd/candidacy/publicProgram/institution/outOfCandidacyPeriod.jsp"),
        @Forward(name = "createIdentification", path = "/phd/candidacy/publicProgram/institution/createIdentification.jsp"),
        @Forward(name = "createIdentificationSuccess",
                path = "/phd/candidacy/publicProgram/institution/createIdentificationSuccess.jsp"),
        @Forward(name = "applicationSubmissionGuide",
                path = "/phd/candidacy/publicProgram/institution/applicationSubmissionGuide.jsp"),
        @Forward(name = "fillPersonalData", path = "/phd/candidacy/publicProgram/institution/fillPersonalData.jsp"),
        @Forward(name = "fillPhdProgramData", path = "/phd/candidacy/publicProgram/institution/fillPhdProgramData.jsp"),
        @Forward(name = "applicationCreationReport",
                path = "/phd/candidacy/publicProgram/institution/applicationCreationReport.jsp"),
        @Forward(name = "view", path = "/phd/candidacy/publicProgram/institution/view.jsp"),
        @Forward(name = "editPersonalData", path = "/phd/candidacy/publicProgram/institution/editPersonalData.jsp"),
        @Forward(name = "editPhdInformationData", path = "/phd/candidacy/publicProgram/institution/editPhdInformationData.jsp"),
        @Forward(name = "editQualifications", path = "/phd/candidacy/publicProgram/institution/editQualifications.jsp"),
        @Forward(name = "uploadDocuments", path = "/phd/candidacy/publicProgram/institution/uploadDocuments.jsp"),
        @Forward(name = "editReferees", path = "/phd/candidacy/publicProgram/institution/editReferees.jsp"),
        @Forward(name = "createRefereeLetter", path = "/phd/candidacy/publicProgram/institution/createRefereeLetter.jsp"),
        @Forward(name = "createRefereeLetterSuccess",
                path = "/phd/candidacy/publicProgram/institution/createRefereeLetterSuccess.jsp"),
        @Forward(name = "editGuidings", path = "/phd/candidacy/publicProgram/institution/editGuidings.jsp"),
        @Forward(name = "validateApplication", path = "/phd/candidacy/publicProgram/institution/validateApplication.jsp"),
        @Forward(name = "uploadPhoto", path = "/phd/candidacy/publicProgram/institution/uploadPhoto.jsp"),
        @Forward(name = "identificationRecovery", path = "/phd/candidacy/publicProgram/institution/identificationRecovery.jsp"),
        @Forward(name = "emailSentForIdentificationRecovery",
                path = "/phd/candidacy/publicProgram/institution/emailSentForIdentificationRecovery.jsp") })
public class PublicInstitutionPhdProgramsCandidacyProcessDA extends PublicPhdProgramCandidacyProcessDA {

    static private final List<String> DO_NOT_VALIDATE_CANDIDACY_PERIOD_IN_METHODS = Arrays.asList(

    "viewCandidacy",

    "backToViewCandidacy",

    "prepareCreateRefereeLetter",

    "createRefereeLetterInvalid",

    "createRefereeLetter");

    @Override
    protected ActionForward filterDispatchMethod(final PhdProgramCandidacyProcessBean bean, ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        final PhdProgramPublicCandidacyHashCode hashCode = (bean != null ? bean.getCandidacyHashCode() : null);
        final String methodName = getMethodName(mapping, actionForm, request, response, mapping.getParameter());

        if (methodName == null || !DO_NOT_VALIDATE_CANDIDACY_PERIOD_IN_METHODS.contains(methodName)) {
            if (isOutOfCandidacyPeriod(hashCode)) {
                InstitutionPhdCandidacyPeriod nextCandidacyPeriod = InstitutionPhdCandidacyPeriod.readNextCandidacyPeriod();
                request.setAttribute("candidacyPeriod", getPhdCandidacyPeriod(hashCode));
                request.setAttribute("nextCandidacyPeriod", nextCandidacyPeriod);
                return mapping.findForward("outOfCandidacyPeriod");
            }
        }

        return null;
    }

    private boolean isOutOfCandidacyPeriod(final PhdProgramPublicCandidacyHashCode hashCode) {
        PhdCandidacyPeriod phdCandidacyPeriod = getPhdCandidacyPeriod(hashCode);
        return phdCandidacyPeriod == null || !phdCandidacyPeriod.contains(new DateTime());
    }

    private PhdCandidacyPeriod getPhdCandidacyPeriod(final PhdProgramPublicCandidacyHashCode hashCode) {
        final DateTime date =
                (hashCode != null && hashCode.hasCandidacyProcess()) ? hashCode.getPhdProgramCandidacyProcess()
                        .getCandidacyDate().toDateTimeAtCurrentTime() : new DateTime();

        return InstitutionPhdCandidacyPeriod.readInstitutionPhdCandidacyPeriodForDate(date);
    }

    /*
     * Create application identification for submission with email
     */
    @EntryPoint
    public ActionForward prepareCreateIdentification(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final String hash = request.getParameter("hash");
        final PhdProgramPublicCandidacyHashCode hashCode =
                (PhdProgramPublicCandidacyHashCode) PublicCandidacyHashCode.getPublicCandidacyCodeByHash(hash);
        if (hashCode != null) {
            return viewCandidacy(mapping, form, request, response, hashCode);
        }

        request.setAttribute("candidacyBean", new PhdProgramCandidacyProcessBean());

        return mapping.findForward("createIdentification");
    }

    public ActionForward createIdentificationInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("candidacyBean", getRenderedObject("candidacyBean"));
        return mapping.findForward("createIdentification");
    }

    public ActionForward createIdentification(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final PhdProgramCandidacyProcessBean bean = getCandidacyBean();
        if (bean == null) {
            return prepareCreateIdentification(mapping, form, request, response);
        }
        final PhdProgramPublicCandidacyHashCode hashCode =
                PhdProgramPublicCandidacyHashCode.getOrCreatePhdProgramCandidacyHashCode(bean.getEmail());

        if (hashCode.hasCandidacyProcess() && (bean.getProgram() == null || hashCode.getPhdProgramCandidacyProcess().getPhdProgram().equals(bean.getProgram()))) {
            addErrorMessage(request, "error.PhdProgramPublicCandidacyHashCode.already.has.candidacy");
            return prepareCreateIdentification(mapping, form, request, response);
        }

        sendSubmissionEmailForCandidacy(hashCode, request);

        String url =
                String.format("%s?hash=%s",
                        InstitutionPhdCandidacyProcessProperties.getPublicCandidacySubmissionLink(I18N.getLocale()),
                        hashCode.getValue());

        request.setAttribute("processLink", url);

        return mapping.findForward("createIdentificationSuccess");
    }

    private void sendSubmissionEmailForCandidacy(final PublicCandidacyHashCode hashCode, final HttpServletRequest request) {
        Message.fromSystem().replyToSender()
                .singleBcc(hashCode.getEmail())
                .template("phd.submission.email.for.candidacy.message.template")
                    .parameter("institutionAcronym", Unit.getInstitutionAcronym())
                    .parameter("candidacyLink", InstitutionPhdCandidacyProcessProperties.getPublicCandidacySubmissionLink(I18N.getLocale()))
                    .parameter("hashCodeValue", hashCode.getValue())
                    .and()
                .wrapped().send();
    }

    /*
     * Identification recovery
     */
    public ActionForward prepareIdentificationRecovery(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("candidacyBean", new PhdProgramCandidacyProcessBean());
        return mapping.findForward("identificationRecovery");
    }

    public ActionForward identificationRecoveryInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("candidacyBean", getCandidacyBean());

        return mapping.findForward("identificationRecovery");
    }

    public ActionForward identificationRecovery(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final PhdProgramCandidacyProcessBean bean = getCandidacyBean();
        final PhdProgramPublicCandidacyHashCode hashCode =
                PhdProgramPublicCandidacyHashCode.getPhdProgramCandidacyHashCode(bean.getEmail(), bean.getProgram(), bean.getCandidacyDate());

        if (hashCode != null) {
            if (hashCode.hasCandidacyProcess()) {
                sendRecoveryEmailForCandidate(hashCode, request);
            } else {
                sendSubmissionEmailForCandidacy(hashCode, request);
            }
        }

        return mapping.findForward("emailSentForIdentificationRecovery");
    }

    private void sendRecoveryEmailForCandidate(PhdProgramPublicCandidacyHashCode candidacyHashCode, HttpServletRequest request) {
        final String subject =
                BundleUtil.getString(Bundle.PHD, "message.phd.email.subject.recovery.access", Unit.getInstitutionAcronym());
        final String body =
                BundleUtil.getString(Bundle.PHD, "message.phd.institution.email.body.recovery.access",
                        Unit.getInstitutionAcronym());
        candidacyHashCode.sendEmail(subject, String.format(body,
                InstitutionPhdCandidacyProcessProperties.getPublicCandidacyAccessLink(I18N.getLocale()),
                candidacyHashCode.getValue()));
    }

    /*
     * Submission forms
     */
    public ActionForward beginSubmission(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return mapping.findForward("applicationSubmissionGuide");
    }

    public ActionForward prepareFillPersonalData(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final PhdProgramPublicCandidacyHashCode hashCode =
                (PhdProgramPublicCandidacyHashCode) PublicCandidacyHashCode.getPublicCandidacyCodeByHash(request
                        .getParameter("hash"));

        if (hashCode == null) {
            return prepareCreateIdentification(mapping, form, request, response);
        }

        if (hashCode.hasCandidacyProcess()) {
            return viewCandidacy(mapping, form, request, response, hashCode);
        }

        final PhdProgramCandidacyProcessBean bean = new PhdProgramCandidacyProcessBean();
        PhdCandidacyPeriod phdCandidacyPeriod = getPhdCandidacyPeriod(hashCode);

        bean.setPersonBean(new PersonBean());
        bean.getPersonBean().setEmail(hashCode.getEmail());
        bean.setCandidacyHashCode(hashCode);
        bean.setExecutionYear(phdCandidacyPeriod.getExecutionInterval());
        bean.setState(PhdProgramCandidacyProcessState.PRE_CANDIDATE);
        bean.setMigratedProcess(Boolean.FALSE);
        bean.setPhdCandidacyPeriod(phdCandidacyPeriod);

        request.setAttribute("candidacyBean", bean);

        return mapping.findForward("fillPersonalData");
    }

    @Override
    public ActionForward fillPersonalDataInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("candidacyBean", getCandidacyBean());

        return mapping.findForward("fillPersonalData");
    }

    public ActionForward createApplication(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        ActionForward checkPersonalDataForward = checkPersonalData(mapping, form, request, response);

        if (checkPersonalDataForward != null) {
            return checkPersonalDataForward;
        }

        final PhdProgramCandidacyProcessBean bean = getCandidacyBean();
        if (PhdProgramPublicCandidacyHashCode.getPhdProgramCandidacyHashCode(bean.getCandidacyHashCode().getEmail(),
                bean.getProgram(), bean.getCandidacyDate()) != null) {
            addErrorMessage(request, "error.PhdProgramPublicCandidacyHashCode.already.has.candidacy");
            return fillPersonalDataInvalid(mapping, form, request, response);
        }

        try {

            final String processLink = FenixFramework.atomic(() -> {
                PhdIndividualProgramProcess process =
                        (PhdIndividualProgramProcess) CreateNewProcess.run(PublicPhdIndividualProgramProcess.class, bean);
                sendApplicationSuccessfullySubmitedEmail(bean.getCandidacyHashCode(), request);

                request.setAttribute("phdIndividualProgramProcess", process);
                request.setAttribute("candidacyHashCode", bean.getCandidacyHashCode());

                PhdProgramPublicCandidacyHashCode candidacyProcessHashCode = process.getCandidacyProcessHashCode();
                return InstitutionPhdCandidacyProcessProperties
                        .getPublicCandidacyAccessLink(candidacyProcessHashCode, I18N.getLocale());
            });

            request.setAttribute("processLink", processLink);
            return mapping.findForward("applicationCreationReport");
        } catch (Exception e) {
            if (e instanceof DomainException) {
                addErrorMessage(request, e);
                return fillPersonalDataInvalid(mapping, form, request, response);
            }
            throw new Error(e);
        }
    }

    private void sendApplicationSuccessfullySubmitedEmail(final PhdProgramPublicCandidacyHashCode hashCode,
            final HttpServletRequest request) {

        // TODO: if candidacy period exists, then change body message to send
        // candidacy limit end date

        Message.fromSystem().replyToSender()
                .singleBcc(hashCode.getEmail())
                .template("phd.application.submission.success.email.message.template")
                    .parameter("institutionAcronym", Unit.getInstitutionAcronym())
                    .parameter("candidacyLink", InstitutionPhdCandidacyProcessProperties.getPublicCandidacySubmissionLink(I18N.getLocale()))
                    .parameter("hashCodeValue", hashCode.getValue())
                    .parameter("processNumber", hashCode.getPhdProgramCandidacyProcess().getProcessNumber())
                    .and()
                .wrapped().send();
    }

    /*
     * View application
     */

    public ActionForward viewApplication(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return viewCandidacy(mapping, form, request, response,
                (PhdProgramPublicCandidacyHashCode) PublicCandidacyHashCode.getPublicCandidacyCodeByHash(request
                        .getParameter("hash")));
    }

    private ActionForward viewCandidacy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response, PhdProgramPublicCandidacyHashCode hashCode) {

        if (hashCode == null || !hashCode.hasCandidacyProcess()) {
            return prepareFillPersonalData(mapping, form, request, response);
        }

        PhdIndividualProgramProcess individualProgramProcess = hashCode.getIndividualProgramProcess();
        request.setAttribute("process", individualProgramProcess.getCandidacyProcess());

        canEditCandidacy(request, hashCode);
        canEditPersonalInformation(request, hashCode.getPerson());

        PersonBean personBean = new PersonBean(individualProgramProcess.getPerson());
        initPersonBeanUglyHack(personBean, individualProgramProcess.getPerson());
        request.setAttribute("personBean", personBean);

        validateProcess(request, individualProgramProcess);

        return mapping.findForward("view");
    }

    private boolean validateProcess(final HttpServletRequest request, final PhdIndividualProgramProcess process) {
        return validateProcessDocuments(request, process);
    }

    private boolean validateProcessDocuments(final HttpServletRequest request, final PhdIndividualProgramProcess process) {
        boolean result = true;

        boolean hasPaymentFees = process.getCandidacyProcess().hasPaymentCodeToPay();
        request.setAttribute("hasPaymentFees", hasPaymentFees);
        int totalDocuments = 5; //with payment fees
        if (!hasPaymentFees) {
            totalDocuments = 4;
        }
        BigDecimal numberOfDocumentsToSubmit = new BigDecimal(totalDocuments + process.getQualifications().size());
        BigDecimal numberOfDocumentsSubmitted = new BigDecimal(0);

        if (!process.hasCandidacyProcessDocument(PhdIndividualProgramDocumentType.ID_DOCUMENT)) {
            addValidationMessage(request, "message.validation.missing.id.document");
            result &= false;

        } else {
            numberOfDocumentsSubmitted = numberOfDocumentsSubmitted.add(new BigDecimal(1));
        }

        if (hasPaymentFees) {
            if (!process.hasCandidacyProcessDocument(PhdIndividualProgramDocumentType.PAYMENT_DOCUMENT)) {
                addValidationMessage(request, "message.validation.missing.payment.document");
                result &= false;
            } else {
                numberOfDocumentsSubmitted = numberOfDocumentsSubmitted.add(new BigDecimal(1));
            }
        }

        if (!process.hasCandidacyProcessDocument(PhdIndividualProgramDocumentType.SOCIAL_SECURITY)) {
            addValidationMessage(request, "message.validation.missing.social.security.document");
            result &= false;
        } else {
            numberOfDocumentsSubmitted = numberOfDocumentsSubmitted.add(new BigDecimal(1));
        }

        if (!process.hasCandidacyProcessDocument(PhdIndividualProgramDocumentType.CV)) {
            addValidationMessage(request, "message.validation.missing.cv");
            result &= false;
        } else {
            numberOfDocumentsSubmitted = numberOfDocumentsSubmitted.add(new BigDecimal(1));
        }

        if (process.getCandidacyProcessDocumentsCount(PhdIndividualProgramDocumentType.HABILITATION_CERTIFICATE_DOCUMENT) < process
                .getQualifications().size()) {
            addValidationMessage(request, "message.validation.missing.qualification.documents",
                    String.valueOf(process.getQualifications().size()));
            result &= false;
        } else {
            numberOfDocumentsSubmitted =
                    numberOfDocumentsSubmitted
                            .add(new BigDecimal(
                                    process.getCandidacyProcessDocumentsCount(PhdIndividualProgramDocumentType.HABILITATION_CERTIFICATE_DOCUMENT)));
        }

        if (!process.hasCandidacyProcessDocument(PhdIndividualProgramDocumentType.MOTIVATION_LETTER)) {
            addValidationMessage(request, "message.validation.missing.motivation.letter");
            result &= false;
        } else {
            numberOfDocumentsSubmitted = numberOfDocumentsSubmitted.add(new BigDecimal(1));
        }

        request.setAttribute(
                "documentsSubmittedPercentage",
                numberOfDocumentsSubmitted.divide(numberOfDocumentsToSubmit, 2, RoundingMode.HALF_EVEN)
                        .multiply(new BigDecimal(100)).intValue());
        request.setAttribute("numberOfDocumentsToSubmit", numberOfDocumentsToSubmit.intValue());
        request.setAttribute("numberOfDocumentsSubmitted", numberOfDocumentsSubmitted.intValue());

        return result;
    }

    /*
     * Edit personal data
     */

    public ActionForward prepareEditPersonalData(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final PhdProgramCandidacyProcess process = getProcess(request);
        final PhdProgramCandidacyProcessBean bean = new PhdProgramCandidacyProcessBean(process);
        Person person = process.getPerson();
        PersonBean personBean = new PersonBean(person);
        bean.setPersonBean(personBean);

        /* TODO: UGLY HACK DUE TO PENDING VALIDATION DATA FOR PERSON */
        initPersonBeanUglyHack(personBean, person);

        canEditCandidacy(request, process.getCandidacyHashCode());
        canEditPersonalInformation(request, person);

        request.setAttribute("candidacyBean", bean);
        return mapping.findForward("editPersonalData");
    }

    public ActionForward editPersonalDataInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final PhdProgramCandidacyProcess process = getProcess(request);
        PhdProgramCandidacyProcessBean candidacyBean = getCandidacyBean();
        request.setAttribute("candidacyBean", candidacyBean);

        canEditCandidacy(request, process.getCandidacyHashCode());
        canEditPersonalInformation(request, process.getPerson());

        return mapping.findForward("editPersonalData");
    }

    public ActionForward editPersonalData(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final PhdProgramCandidacyProcessBean bean = getCandidacyBean();
        PhdProgramCandidacyProcess process = getProcess(request);

        canEditCandidacy(request, process.getCandidacyHashCode());
        canEditPersonalInformation(request, process.getPerson());

        try {
            ExecuteProcessActivity
                    .run(process.getIndividualProgramProcess(), EditPersonalInformation.class, bean.getPersonBean());
        } catch (final DomainException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());
            request.setAttribute("candidacyBean", bean);
            return mapping.findForward("editPersonalData");
        }

        return viewCandidacy(mapping, form, request, response, process.getCandidacyHashCode());
    }

    /*
     * Edit phd information data
     */
    public ActionForward prepareEditPhdInformationData(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final PhdProgramCandidacyProcess process = getProcess(request);
        final PhdProgramCandidacyProcessBean candidacyBean = new PhdProgramCandidacyProcessBean(process);

        request.setAttribute("candidacyBean", candidacyBean);
        request.setAttribute("individualProcessBean", new PhdIndividualProgramProcessBean(process.getIndividualProgramProcess()));

        canEditCandidacy(request, process.getCandidacyHashCode());

        return mapping.findForward("editPhdInformationData");
    }

    public ActionForward prepareEditPhdInformationDataFocusAreaPostback(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        final PhdProgramCandidacyProcess process = getProcess(request);

        PhdProgramCandidacyProcessBean candidacyBean = getCandidacyBean();
        final PhdIndividualProgramProcessBean bean = getIndividualProcessBean();

        request.setAttribute("candidacyBean", candidacyBean);
        request.setAttribute("individualProcessBean", bean);

        canEditCandidacy(request, process.getCandidacyHashCode());

        return mapping.findForward("editPhdInformationData");
    }

    public ActionForward editPhdInformationDataInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final PhdProgramCandidacyProcess process = getProcess(request);
        PhdProgramCandidacyProcessBean candidacyBean = getCandidacyBean();
        final PhdIndividualProgramProcessBean bean = getIndividualProcessBean();

        request.setAttribute("candidacyBean", candidacyBean);
        request.setAttribute("individualProcessBean", bean);

        canEditCandidacy(request, process.getCandidacyHashCode());

        return mapping.findForward("editPhdInformationData");
    }

    public ActionForward editPhdInformationData(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final PhdIndividualProgramProcessBean bean = getIndividualProcessBean();
        PhdProgramCandidacyProcess process = getProcess(request);

        canEditCandidacy(request, process.getCandidacyHashCode());

        try {
            ExecuteProcessActivity.run(process.getIndividualProgramProcess(), EditIndividualProcessInformation.class, bean);
            addSuccessMessage(request, "message.phdIndividualProgramProcessInformation.edit.success");

        } catch (final DomainException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());
            request.setAttribute("candidacyBean", getCandidacyBean());
            request.setAttribute("individualProcessBean", bean);

            return mapping.findForward("editPhdInformationData");
        }

        return viewCandidacy(mapping, form, request, response, process.getCandidacyHashCode());
    }

    /*
     * Qualifications
     */

    public ActionForward prepareEditQualifications(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PhdProgramCandidacyProcess process = getProcess(request);
        canEditCandidacy(request, process.getCandidacyHashCode());

        PhdProgramCandidacyProcessBean bean = new PhdProgramCandidacyProcessBean(process);
        QualificationBean qualificationBean = new QualificationBean();

        request.setAttribute("candidacyBean", bean);
        request.setAttribute("qualificationBean", qualificationBean);

        return mapping.findForward("editQualifications");
    }

    public ActionForward editQualificationsInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PhdProgramCandidacyProcessBean bean = getCandidacyBean();
        QualificationBean qualificationBean = getQualificationBean();

        request.setAttribute("candidacyBean", bean);
        request.setAttribute("qualificationBean", qualificationBean);

        return prepareEditQualifications(mapping, form, request, response);
    }

    public ActionForward addQualification(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PhdProgramCandidacyProcess process = getProcess(request);
        canEditCandidacy(request, process.getCandidacyHashCode());

        try {
            ExecuteProcessActivity.run(process.getIndividualProgramProcess(), AddQualification.class, getQualificationBean());
            addSuccessMessage(request, "message.qualification.information.create.success");
        } catch (final DomainException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());
            return editQualificationsInvalid(mapping, form, request, response);
        }

        RenderUtils.invalidateViewState();
        return prepareEditQualifications(mapping, form, request, response);
    }

    public ActionForward removeQualification(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PhdProgramCandidacyProcess process = getProcess(request);
        canEditCandidacy(request, process.getCandidacyHashCode());

        try {
            ExecuteProcessActivity.run(process.getIndividualProgramProcess(), DeleteQualification.class,
                    getDomainObject(request, "qualificationId"));
        } catch (final DomainException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());
            return editQualificationsInvalid(mapping, form, request, response);
        }

        RenderUtils.invalidateViewState();
        return prepareEditQualifications(mapping, form, request, response);
    }

    /*
     * Upload documents
     */

    public ActionForward prepareUploadDocuments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final PhdProgramCandidacyProcess process = getProcess(request);
        final PhdProgramCandidacyProcessBean bean = new PhdProgramCandidacyProcessBean(process);

        canEditCandidacy(request, process.getCandidacyHashCode());

        RenderUtils.invalidateViewState();

        request.setAttribute("candidacyBean", bean);
        request.setAttribute("candidacyProcessDocuments", process.getLatestDocumentVersions());
        request.setAttribute("hasPaymentFees", process.hasPaymentCodeToPay());

        final PhdProgramDocumentUploadBean uploadBean = new PhdProgramDocumentUploadBean();
        uploadBean.setIndividualProgramProcess(process.getIndividualProgramProcess());
        request.setAttribute("documentByType", uploadBean);

        validateProcessDocuments(request, process.getIndividualProgramProcess());

        return mapping.findForward("uploadDocuments");

    }

    @Override
    public ActionForward uploadDocumentsInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final PhdProgramCandidacyProcess process = getProcess(request);
        PhdProgramCandidacyProcessBean bean = getCandidacyBean();
        PhdProgramDocumentUploadBean uploadBean = getUploadBean();

        canEditCandidacy(request, process.getCandidacyHashCode());

        request.setAttribute("candidacyProcessDocuments", process.getLatestDocumentVersions());

        request.setAttribute("candidacyBean", bean);
        request.setAttribute("documentByType", uploadBean);

        validateProcessDocuments(request, process.getIndividualProgramProcess());

        return mapping.findForward("uploadDocuments");
    }

    @Override
    public ActionForward uploadDocuments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final PhdProgramCandidacyProcess process = getProcess(request);

        if (!RenderUtils.getViewState("documentByType").isValid()) {
            return uploadDocumentsInvalid(mapping, form, request, response);
        }

        final PhdProgramDocumentUploadBean uploadBean = getUploadBean();

        if (!uploadBean.hasAnyInformation()) {
            addErrorMessage(request, "message.no.documents.to.upload");
            return uploadDocumentsInvalid(mapping, form, request, response);

        }
        try {
            ExecuteProcessActivity.run(process.getIndividualProgramProcess(), UploadDocuments.class,
                    Collections.singletonList(uploadBean));
            addSuccessMessage(request, "message.documents.uploaded.with.success");

        } catch (final DomainException e) {
            addErrorMessage(request, "message.no.documents.to.upload");
            return uploadDocumentsInvalid(mapping, form, request, response);
        }

        return prepareUploadDocuments(mapping, form, request, response);
    }

    public ActionForward removeDocument(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final PhdProgramCandidacyProcess process = getProcess(request);
        final PhdProgramProcessDocument document = getDomainObject(request, "documentId");

        try {
            ExecuteProcessActivity.run(process,
                    org.fenixedu.academic.domain.phd.candidacy.activities.RemoveCandidacyDocument.class, document);
            addSuccessMessage(request, "message.documents.uploaded.with.success");

        } catch (final DomainException e) {
            addErrorMessage(request, "message.no.documents.to.upload");
            return uploadDocumentsInvalid(mapping, form, request, response);
        }

        return prepareUploadDocuments(mapping, form, request, response);
    }

    /*
     * Edit phd referees
     */

    public ActionForward prepareEditReferees(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final PhdProgramCandidacyProcess process = getProcess(request);
        final PhdProgramCandidacyProcessBean bean = new PhdProgramCandidacyProcessBean(process);

        canEditCandidacy(request, process.getCandidacyHashCode());

        request.setAttribute("candidacyBean", bean);
        request.setAttribute("refereeBean", new PhdCandidacyRefereeBean());

        return mapping.findForward("editReferees");
    }

    public ActionForward addReferee(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final PhdProgramCandidacyProcess process = getProcess(request);

        try {
            ExecuteProcessActivity.run(process.getIndividualProgramProcess(), AddCandidacyReferees.class,
                    Collections.singletonList(getRenderedObject("refereeBean")));

            addSuccessMessage(request, "message.referee.information.create.success");
        } catch (final DomainException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());
            return editRefereesInvalid(mapping, form, request, response);
        }

        RenderUtils.invalidateViewState();

        return prepareEditReferees(mapping, form, request, response);
    }

    public ActionForward editRefereesInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final PhdProgramCandidacyProcess process = getProcess(request);

        request.setAttribute("candidacyBean", getCandidacyBean());
        request.setAttribute("refereeBean", getPhdCandidacyReferee());

        canEditCandidacy(request, process.getCandidacyHashCode());

        return mapping.findForward("editReferees");
    }

    public ActionForward sendCandidacyRefereeEmail(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final PhdCandidacyReferee referee = getDomainObject(request, "candidacyRefereeId");
        referee.sendEmail();
        addSuccessMessage(request, "message.candidacy.referee.email.sent.with.success", referee.getName());

        return prepareEditReferees(mapping, form, request, response);
    }

    public ActionForward prepareCreateRefereeLetter(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final PhdCandidacyReferee hashCode =
                (PhdCandidacyReferee) PublicCandidacyHashCode.getPublicCandidacyCodeByHash(request.getParameter("hash"));

        request.setAttribute("refereeLetterHash", hashCode);

        if (hashCode == null) {
            request.setAttribute("no-information", Boolean.TRUE);
            return mapping.findForward("createRefereeLetterSuccess");
        }

        if (hashCode.getLetter() != null) {
            request.setAttribute("has-letter", Boolean.TRUE);
            request.setAttribute("letter", hashCode.getLetter());
            return mapping.findForward("createRefereeLetterSuccess");
        }

        final PhdCandidacyRefereeLetterBean bean = new PhdCandidacyRefereeLetterBean();
        bean.setCandidacyReferee(hashCode);
        bean.setRefereeName(hashCode.getName());
        request.setAttribute("createRefereeLetterBean", bean);
        return mapping.findForward("createRefereeLetter");
    }

    public ActionForward createRefereeLetterInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final PhdCandidacyReferee hashCode =
                (PhdCandidacyReferee) PublicCandidacyHashCode.getPublicCandidacyCodeByHash(request.getParameter("hash"));

        request.setAttribute("refereeLetterHash", hashCode);

        final PhdCandidacyRefereeLetterBean bean = new PhdCandidacyRefereeLetterBean();

        bean.setCandidacyReferee(hashCode);
        bean.setRefereeName(hashCode.getName());
        request.setAttribute("createRefereeLetterBean", bean);
        return mapping.findForward("createRefereeLetter");
    }

    public ActionForward createRefereeLetter(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        final PhdCandidacyReferee hashCode =
                (PhdCandidacyReferee) PublicCandidacyHashCode.getPublicCandidacyCodeByHash(request.getParameter("hash"));

        request.setAttribute("refereeLetterHash", hashCode);

        final PhdCandidacyRefereeLetterBean bean = getRenderedObject("createRefereeLetterBean");

        if (hasAnyRefereeLetterViewStateInvalid()) {
            return createRefereeLetterInvalid(mapping, actionForm, request, response);
        }

        try {
            PhdCandidacyRefereeLetter.create(bean);

        } catch (final DomainException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());
            request.setAttribute("createRefereeLetterBean", bean);
            return mapping.findForward("createRefereeLetter");
        }

        request.setAttribute("created-with-success", Boolean.TRUE);
        return mapping.findForward("createRefereeLetterSuccess");
    }

    public ActionForward removeReferee(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final PhdProgramCandidacyProcess process = getProcess(request);
        final PhdCandidacyReferee referee = getDomainObject(request, "candidacyRefereeId");

        try {
            ExecuteProcessActivity.run(process.getIndividualProgramProcess(), RemoveCandidacyReferee.class, referee);

            addSuccessMessage(request, "message.referee.information.remove.success");
        } catch (final DomainException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());
            return editRefereesInvalid(mapping, form, request, response);
        }

        RenderUtils.invalidateViewState();

        return prepareEditReferees(mapping, form, request, response);
    }

    /*
     * Edit Phd Guidings
     */

    public ActionForward prepareEditCandidacyGuidings(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final PhdProgramCandidacyProcess process = getProcess(request);
        final PhdProgramCandidacyProcessBean bean = new PhdProgramCandidacyProcessBean(process);

        canEditCandidacy(request, process.getCandidacyHashCode());

        request.setAttribute("candidacyBean", bean);
        PhdParticipantBean guidingBean = new PhdParticipantBean();
        guidingBean.setParticipantType(PhdParticipantType.EXTERNAL);

        PhdProgramDocumentUploadBean guidingAcceptanceLetter = new PhdProgramDocumentUploadBean();
        guidingAcceptanceLetter.setType(PhdIndividualProgramDocumentType.GUIDER_ACCEPTANCE_LETTER);
        guidingBean.setGuidingAcceptanceLetter(guidingAcceptanceLetter);

        request.setAttribute("guidingBean", guidingBean);

        PhdParticipantBean assistantGuidingBean = new PhdParticipantBean();
        assistantGuidingBean.setParticipantType(PhdParticipantType.EXTERNAL);

        PhdProgramDocumentUploadBean assistantGuidingAcceptanceLetter = new PhdProgramDocumentUploadBean();
        assistantGuidingAcceptanceLetter.setType(PhdIndividualProgramDocumentType.ASSISTENT_GUIDER_ACCEPTANCE_LETTER);
        assistantGuidingBean.setGuidingAcceptanceLetter(assistantGuidingAcceptanceLetter);

        request.setAttribute("assistantGuidingBean", assistantGuidingBean);

        return mapping.findForward("editGuidings");
    }

    public ActionForward addGuiding(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final PhdProgramCandidacyProcess process = getProcess(request);
        PhdParticipantBean bean = getGuidingBean();

        try {
            ExecuteProcessActivity.run(process.getIndividualProgramProcess(), AddGuidingsInformation.class,
                    Collections.singletonList(bean));

            addSuccessMessage(request, "message.guiding.created.with.success");
        } catch (final DomainException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());
            return addGuidingInvalid(mapping, form, request, response);
        }

        RenderUtils.invalidateViewState();

        return prepareEditCandidacyGuidings(mapping, form, request, response);
    }

    public ActionForward addGuidingInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final PhdProgramCandidacyProcess process = getProcess(request);

        request.setAttribute("candidacyBean", getCandidacyBean());
        request.setAttribute("guidingBean", getGuidingBean());
        request.setAttribute("assistantGuidingBean", getAssistantGuidingBean());

        canEditCandidacy(request, process.getCandidacyHashCode());

        return mapping.findForward("editGuidings");
    }

    public ActionForward addAssistantGuiding(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final PhdProgramCandidacyProcess process = getProcess(request);
        PhdParticipantBean bean = getAssistantGuidingBean();

        try {
            ExecuteProcessActivity.run(process.getIndividualProgramProcess(), AddAssistantGuidingInformation.class, bean);

            addSuccessMessage(request, "message.assistant.guiding.created.with.success");
        } catch (final DomainException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());
            return addGuidingInvalid(mapping, form, request, response);
        }

        RenderUtils.invalidateViewState();

        return prepareEditCandidacyGuidings(mapping, form, request, response);
    }

    public ActionForward removeGuiding(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final PhdProgramCandidacyProcess process = getProcess(request);

        try {
            ExecuteProcessActivity.run(process.getIndividualProgramProcess(), DeleteGuiding.class,
                    getDomainObject(request, "guidingId"));
            addSuccessMessage(request, "message.guiding.deleted.with.success");
        } catch (final DomainException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());
        }

        return prepareEditCandidacyGuidings(mapping, form, request, response);
    }

    public ActionForward removeAssistantGuiding(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final PhdProgramCandidacyProcess process = getProcess(request);

        try {
            ExecuteProcessActivity.run(process.getIndividualProgramProcess(), DeleteAssistantGuiding.class,
                    getDomainObject(request, "guidingId"));
            addSuccessMessage(request, "message.guiding.deleted.with.success");
        } catch (final DomainException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());
        }

        return prepareEditCandidacyGuidings(mapping, form, request, response);
    }

    private boolean hasAnyRefereeLetterViewStateInvalid() {
        for (final IViewState viewState : getViewStatesWithPrefixId("createRefereeLetterBean.")) {
            if (!viewState.isValid()) {
                return true;
            }
        }
        return false;
    }

    /*
     * Validate application
     */

    public ActionForward prepareValidateApplication(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final PhdProgramCandidacyProcess process = getProcess(request);
        final PhdProgramCandidacyProcessBean bean = new PhdProgramCandidacyProcessBean(process);

        canEditCandidacy(request, process.getCandidacyHashCode());
        validateProcess(request, process.getIndividualProgramProcess());

        request.setAttribute("candidacyBean", bean);

        return mapping.findForward("validateApplication");
    }

    public ActionForward validateApplication(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final PhdProgramCandidacyProcess process = getProcess(request);

        if (!validateProcess(request, process.getIndividualProgramProcess())) {
            return prepareValidateApplication(mapping, form, request, response);
        }

        try {
            ExecuteProcessActivity.run(process.getIndividualProgramProcess(), ValidatedByCandidate.class, null);
            addSuccessMessage(request, "message.validation.with.success");

        } catch (final DomainException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());
            return prepareValidateApplication(mapping, form, request, response);
        }

        return viewCandidacy(mapping, form, request, response, process.getCandidacyHashCode());

    }

    /*
     * 
     * Upload photo
     */
    public ActionForward prepareUploadPhoto(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        final PhdProgramCandidacyProcess process = getProcess(request);
        final PhdProgramCandidacyProcessBean bean = new PhdProgramCandidacyProcessBean(process);

        request.setAttribute("candidacyBean", bean);
        request.setAttribute("uploadPhotoBean", new PhotographUploadBean());
        return mapping.findForward("uploadPhoto");
    }

    public ActionForward uploadPhotoInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("candidacyBean", getCandidacyBean());
        request.setAttribute("uploadPhotoBean", getRenderedObject("uploadPhotoBean"));

        RenderUtils.invalidateViewState("uploadPhotoBean");
        return mapping.findForward("uploadPhoto");
    }

    public ActionForward uploadPhoto(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        final PhdProgramCandidacyProcessBean bean = getCandidacyBean();
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
            UploadOwnPhoto.upload(photo, bean.getIndividualProgramProcess().getPerson());
        } catch (final UnableToProcessTheImage e) {
            addErrorMessage(request, "error.photo.upload.unable.to.process.image");
            photo.deleteTemporaryFiles();
            return uploadPhotoInvalid(mapping, actionForm, request, response);

        } catch (final DomainException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());
            photo.deleteTemporaryFiles();
            return uploadPhotoInvalid(mapping, actionForm, request, response);
        }

        return viewCandidacy(mapping, actionForm, request, response, bean.getProcess().getCandidacyHashCode());
    }

    private QualificationBean getQualificationBean() {
        return getRenderedObject("qualificationBean");
    }

    private PhdIndividualProgramProcessBean getIndividualProcessBean() {
        return getRenderedObject("individualProcessBean");
    }

    private PhdProgramDocumentUploadBean getUploadBean() {
        return getRenderedObject("documentByType");

    }

    private PhdCandidacyRefereeBean getPhdCandidacyReferee() {
        return getRenderedObject("refereeBean");
    }

    private PhdParticipantBean getGuidingBean() {
        return getRenderedObject("guidingBean");
    }

    private PhdParticipantBean getAssistantGuidingBean() {
        return getRenderedObject("assistantGuidingBean");
    }
}
