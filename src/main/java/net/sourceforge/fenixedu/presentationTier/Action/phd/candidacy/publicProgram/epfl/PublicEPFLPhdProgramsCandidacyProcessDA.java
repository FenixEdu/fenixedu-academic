package net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy.publicProgram.epfl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.caseHandling.CreateNewProcess;
import net.sourceforge.fenixedu.applicationTier.Servico.caseHandling.ExecuteProcessActivity;
import net.sourceforge.fenixedu.applicationTier.Servico.fileManager.UploadOwnPhoto;
import net.sourceforge.fenixedu.dataTransferObject.contacts.PendingPartyContactBean;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.dataTransferObject.person.PhotographUploadBean;
import net.sourceforge.fenixedu.dataTransferObject.person.PhotographUploadBean.UnableToProcessTheImage;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PublicCandidacyHashCode;
import net.sourceforge.fenixedu.domain.Qualification;
import net.sourceforge.fenixedu.domain.QualificationBean;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddress;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramCollaborationType;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramDocumentType;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.PublicPhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessBean;
import net.sourceforge.fenixedu.domain.phd.PhdParticipant;
import net.sourceforge.fenixedu.domain.phd.PhdParticipantBean;
import net.sourceforge.fenixedu.domain.phd.PhdParticipantBean.PhdParticipantType;
import net.sourceforge.fenixedu.domain.phd.PhdProgramCandidacyProcessState;
import net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.EPFLPhdCandidacyPeriod;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyPeriod;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyReferee;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyRefereeBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyRefereeLetter;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyRefereeLetterBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramPublicCandidacyHashCode;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdThesisSubjectOrderBean;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.AddCandidacyReferees;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.AddGuidingsInformation;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.AddQualification;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.DeleteGuiding;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.DeleteQualification;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.EditIndividualProcessInformation;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.EditPersonalInformation;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.UploadDocuments;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.ValidatedByCandidate;
import net.sourceforge.fenixedu.presentationTier.Action.commons.FenixActionForward;
import net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy.publicProgram.PublicPhdProgramCandidacyProcessDA;
import net.sourceforge.fenixedu.util.ContentType;
import net.sourceforge.fenixedu.util.phd.EPFLPhdCandidacyProcessProperties;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.servlets.filters.I18NFilter;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.utl.ist.fenix.tools.util.i18n.Language;

@Mapping(path = "/applications/epfl/phdProgramCandidacyProcess", module = "publico")
@Forwards(tileProperties = @Tile(extend = "definition.candidacy.process"), value = {

        @Forward(name = "createCandidacyIdentification",
                path = "/phd/candidacy/publicProgram/epfl/createCandidacyIdentification.jsp", tileProperties = @Tile(
                        hideLanguage = "true")),

        @Forward(name = "createCandidacyIdentificationSuccess",
                path = "/phd/candidacy/publicProgram/epfl/createCandidacyIdentificationSuccess.jsp", tileProperties = @Tile(
                        hideLanguage = "true")),

        @Forward(name = "candidacyIdentificationRecovery",
                path = "/phd/candidacy/publicProgram/epfl/candidacyIdentificationRecovery.jsp", tileProperties = @Tile(
                        hideLanguage = "true")),

        @Forward(name = "createCandidacyStepOne", path = "/phd/candidacy/publicProgram/epfl/createCandidacyStepOne.jsp",
                tileProperties = @Tile(hideLanguage = "true")),

        @Forward(name = "createCandidacyStepTwo", path = "/phd/candidacy/publicProgram/epfl/createCandidacyStepTwo.jsp",
                tileProperties = @Tile(hideLanguage = "true")),

        @Forward(name = "createCandidacyStepThree", path = "/phd/candidacy/publicProgram/epfl/createCandidacyStepThree.jsp",
                tileProperties = @Tile(hideLanguage = "true")),

        @Forward(name = "showCandidacySuccess", path = "/phd/candidacy/publicProgram/epfl/candidacySubmited.jsp",
                tileProperties = @Tile(hideLanguage = "true")),

        @Forward(name = "viewCandidacy", path = "/phd/candidacy/publicProgram/epfl/viewCandidacy.jsp", tileProperties = @Tile(
                hideLanguage = "true")),

        @Forward(name = "editPersonalInformation", path = "/phd/candidacy/publicProgram/epfl/editPersonalInformation.jsp",
                tileProperties = @Tile(hideLanguage = "true")),

        @Forward(name = "uploadCandidacyDocuments", path = "/phd/candidacy/publicProgram/epfl/uploadCandidacyDocuments.jsp",
                tileProperties = @Tile(hideLanguage = "true")),

        @Forward(name = "editPhdIndividualProgramProcessInformation",
                path = "/phd/candidacy/publicProgram/epfl/editPhdIndividualProgramProcessInformation.jsp",
                tileProperties = @Tile(hideLanguage = "true")),

        @Forward(name = "editCandidacyGuidings", path = "/phd/candidacy/publicProgram/epfl/editCandidacyGuidings.jsp",
                tileProperties = @Tile(hideLanguage = "true")),

        @Forward(name = "editQualifications", path = "/phd/candidacy/publicProgram/epfl/editQualifications.jsp",
                tileProperties = @Tile(hideLanguage = "true")),

        @Forward(name = "createRefereeLetter", path = "/phd/candidacy/publicProgram/epfl/createRefereeLetter.jsp",
                tileProperties = @Tile(hideLanguage = "true")),

        @Forward(name = "createRefereeLetterSuccess", path = "/phd/candidacy/publicProgram/epfl/createRefereeLetterSuccess.jsp",
                tileProperties = @Tile(hideLanguage = "true")),

        @Forward(name = "editCandidacyReferees", path = "/phd/candidacy/publicProgram/epfl/editCandidacyReferees.jsp",
                tileProperties = @Tile(hideLanguage = "true")),

        @Forward(name = "uploadPhoto", path = "/phd/candidacy/publicProgram/epfl/uploadPhoto.jsp", tileProperties = @Tile(
                hideLanguage = "true")),

        @Forward(name = "out.of.candidacy.period", path = "/phd/candidacy/publicProgram/epfl/outOfCandidacyPeriod.jsp",
                tileProperties = @Tile(hideLanguage = "true")),

        @Forward(name = "validateCandidacy", path = "/phd/candidacy/publicProgram/epfl/validateCandidacy.jsp",
                tileProperties = @Tile(hideLanguage = "true")),

        @Forward(name = "emailSentForIdentificationRecovery",
                path = "/phd/candidacy/publicProgram/epfl/emailSentForIdentificationRecovery.jsp", tileProperties = @Tile(
                        hideLanguage = "true"))

})
public class PublicEPFLPhdProgramsCandidacyProcessDA extends PublicPhdProgramCandidacyProcessDA {

    static private final List<String> DO_NOT_VALIDATE_CANDIDACY_PERIOD_IN_METHODS = Arrays.asList(

    "showCandidacySuccess",

    "viewCandidacy",

    "backToViewCandidacy",

    "prepareCreateRefereeLetter",

    "createRefereeLetterInvalid",

    "createRefereeLetter");

    static private final int MINIMUM_HABILITATIONS_AND_CERTIFICATES = 2;
    static private final int MINIMUM_CANDIDACY_REFEREES = 3;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        I18NFilter.setLocale(request, request.getSession(true), Locale.ENGLISH);
        return super.execute(mapping, actionForm, request, response);
    }

    @Override
    protected ActionForward filterDispatchMethod(final PhdProgramCandidacyProcessBean bean, ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        final PhdProgramPublicCandidacyHashCode hashCode = (bean != null ? bean.getCandidacyHashCode() : null);
        final String methodName = getMethodName(mapping, actionForm, request, response, mapping.getParameter());

        if (methodName == null || !DO_NOT_VALIDATE_CANDIDACY_PERIOD_IN_METHODS.contains(methodName)) {
            if (isOutOfCandidacyPeriod(hashCode)) {
                request.setAttribute("candidacyPeriod", getPhdCandidacyPeriod(hashCode));
                return mapping.findForward("out.of.candidacy.period");
            }
        }

        return null;
    }

    private boolean isOutOfCandidacyPeriod(final PhdProgramPublicCandidacyHashCode hashCode) {
        final PhdCandidacyPeriod period = getPhdCandidacyPeriod(hashCode);
        return period == null || !period.contains(new DateTime());
    }

    private PhdCandidacyPeriod getPhdCandidacyPeriod(final PhdProgramPublicCandidacyHashCode hashCode) {
        if (hashCode == null) {
            return EPFLPhdCandidacyPeriod.readEPFLPhdCandidacyPeriodForDateTime(new DateTime());
        }

        if (!hashCode.hasCandidacyProcess()) {
            return EPFLPhdCandidacyPeriod.readEPFLPhdCandidacyPeriodForDateTime(new DateTime());
        }

        if (!hashCode.getPhdProgramCandidacyProcess().isPublicCandidacy()) {
            return EPFLPhdCandidacyPeriod.readEPFLPhdCandidacyPeriodForDateTime(new DateTime());
        }

        if (!hashCode.getPhdProgramCandidacyProcess().getPublicPhdCandidacyPeriod().isEpflCandidacyPeriod()) {
            return EPFLPhdCandidacyPeriod.readEPFLPhdCandidacyPeriodForDateTime(new DateTime());
        }

        return hashCode.getPhdProgramCandidacyProcess().getPublicPhdCandidacyPeriod();
    }

    public ActionForward prepareCreateCandidacyIdentification(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        final String hash = request.getParameter("hash");
        final PhdProgramPublicCandidacyHashCode hashCode =
                (PhdProgramPublicCandidacyHashCode) PublicCandidacyHashCode.getPublicCandidacyCodeByHash(hash);
        if (hashCode != null) {
            return viewCandidacy(mapping, request, hashCode);
        }

        request.setAttribute("candidacyBean", new PhdProgramCandidacyProcessBean());
        return mapping.findForward("createCandidacyIdentification");
    }

    public ActionForward createCandidacyIdentificationInvalid(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("candidacyBean", getRenderedObject("candidacyBean"));
        return mapping.findForward("createCandidacyIdentification");
    }

    public ActionForward createCandidacyIdentification(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final PhdProgramCandidacyProcessBean bean = getCandidacyBean();
        final PhdProgramPublicCandidacyHashCode hashCode =
                PhdProgramPublicCandidacyHashCode.getOrCreatePhdProgramCandidacyHashCode(bean.getEmail());

        if (hashCode.hasCandidacyProcess()) {
            addErrorMessage(request, "error.PhdProgramPublicCandidacyHashCode.already.has.candidacy");
            return prepareCreateCandidacyIdentification(mapping, actionForm, request, response);
        }

        sendSubmissionEmailForCandidacy(hashCode, request);

        String url =
                String.format("%s?hash=%s", EPFLPhdCandidacyProcessProperties.getPublicCandidacySubmissionLink(),
                        hashCode.getValue());

        request.setAttribute("processLink", url);

        return mapping.findForward("createCandidacyIdentificationSuccess");
    }

    private void sendSubmissionEmailForCandidacy(final PublicCandidacyHashCode hashCode, final HttpServletRequest request) {
        final ResourceBundle bundle = ResourceBundle.getBundle("resources.PhdResources", Language.getLocale());
        final String subject = bundle.getString("message.phd.epfl.application.email.subject.send.link.to.submission");
        final String body = bundle.getString("message.phd.epfl.email.body.send.link.to.submission");
        hashCode.sendEmail(subject,
                String.format(body, EPFLPhdCandidacyProcessProperties.getPublicCandidacySubmissionLink(), hashCode.getValue()));
    }

    public ActionForward prepareCandidacyIdentificationRecovery(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("candidacyBean", new PhdProgramCandidacyProcessBean());
        return mapping.findForward("candidacyIdentificationRecovery");
    }

    public ActionForward candidacyIdentificationRecoveryInvalid(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("candidacyBean", getRenderedObject("candidacyBean"));
        return mapping.findForward("candidacyIdentificationRecovery");
    }

    public ActionForward candidacyIdentificationRecovery(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        final PhdProgramCandidacyProcessBean bean = getCandidacyBean();
        final PhdProgramPublicCandidacyHashCode hashCode =
                PhdProgramPublicCandidacyHashCode.getPhdProgramCandidacyHashCode(bean.getEmail());

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
        final ResourceBundle bundle = ResourceBundle.getBundle("resources.PhdResources", Language.getLocale());
        final String subject = bundle.getString("message.phd.email.subject.recovery.access");
        final String body = bundle.getString("message.phd.epfl.email.body.recovery.access");
        candidacyHashCode.sendEmail(
                subject,
                String.format(body, EPFLPhdCandidacyProcessProperties.getPublicCandidacyAccessLink(),
                        candidacyHashCode.getValue()));
    }

    public ActionForward prepareCreateCandidacy(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        return createCandidacyStepOne(mapping, actionForm, request, response);
    }

    public ActionForward createCandidacyStepOne(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final PhdProgramPublicCandidacyHashCode hashCode =
                (PhdProgramPublicCandidacyHashCode) PublicCandidacyHashCode.getPublicCandidacyCodeByHash(request
                        .getParameter("hash"));

        if (hashCode == null) {
            return mapping.findForward("createCandidacyStepOne");
        }

        if (hashCode.hasCandidacyProcess()) {
            return viewCandidacy(mapping, request, hashCode);
        }

        final PhdProgramCandidacyProcessBean bean = new PhdProgramCandidacyProcessBean();
        bean.setPersonBean(new PersonBean());
        bean.getPersonBean().setEmail(hashCode.getEmail());
        bean.getPersonBean().setCreateLoginIdentificationAndUserIfNecessary(false);
        bean.setCandidacyHashCode(hashCode);
        bean.setExecutionYear(ExecutionYear.readCurrentExecutionYear());
        bean.setCollaborationType(PhdIndividualProgramCollaborationType.EPFL);
        bean.setState(PhdProgramCandidacyProcessState.PRE_CANDIDATE);
        bean.setMigratedProcess(Boolean.FALSE);
        bean.setPhdCandidacyPeriod(getPhdCandidacyPeriod(hashCode));

        request.setAttribute("candidacyBean", bean);
        return mapping.findForward("createCandidacyStepOne");
    }

    public ActionForward createCandidacyStepOneInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("candidacyBean", getRenderedObject("candidacyBean"));
        return mapping.findForward("createCandidacyStepOne");
    }

    public ActionForward returnCreateCandidacyStepOne(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("candidacyBean", getCandidacyBean());
        RenderUtils.invalidateViewState();
        return mapping.findForward("createCandidacyStepOne");
    }

    @Override
    public ActionForward fillPersonalDataInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return createCandidacyStepOneInvalid(mapping, form, request, response);
    }

    public ActionForward createCandidacyStepTwo(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        ActionForward checkPersonalDataForward = checkPersonalData(mapping, actionForm, request, response);

        if (checkPersonalDataForward != null) {
            return checkPersonalDataForward;
        }
        PersonBean personBean = getCandidacyBean().getPersonBean();

        final String familyName = personBean.getFamilyNames();
        final String composedName =
                familyName == null || familyName.isEmpty() ? personBean.getGivenNames() : personBean.getGivenNames() + " "
                        + familyName;
        personBean.setName(composedName);

        request.setAttribute("candidacyBean", getCandidacyBean());
        RenderUtils.invalidateViewState();

        return prepareCreateCandidacyStepTwo(mapping, actionForm, request, response);
    }

    public ActionForward prepareCreateCandidacyStepTwo(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        return mapping.findForward("createCandidacyStepTwo");
    }

    public ActionForward moveUpThesisSubjectForEditPhdInformation(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        int order = getIntegerFromRequest(request, "order");
        PhdIndividualProgramProcessBean phdBean = getRenderedObject("individualProcessBean");
        PhdThesisSubjectOrderBean beanToMoveUp = phdBean.getThesisSubjectBean(order);
        PhdThesisSubjectOrderBean beanToMoveDown = phdBean.getThesisSubjectBean(order - 1);

        if (beanToMoveDown != null) {
            beanToMoveUp.decreaseOrder();
            beanToMoveDown.increaseOrder();
            phdBean.sortThesisSubjectBeans();
        }

        request.setAttribute("candidacyBean", getCandidacyBean());
        request.setAttribute("individualProcessBean", phdBean);
        RenderUtils.invalidateViewState();

        return mapping.findForward("editPhdIndividualProgramProcessInformation");
    }

    public ActionForward moveDownThesisSubjectForEditPhdInformation(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        int order = getIntegerFromRequest(request, "order");
        PhdIndividualProgramProcessBean phdBean = getRenderedObject("individualProcessBean");
        PhdThesisSubjectOrderBean beanToMoveDown = phdBean.getThesisSubjectBean(order);
        PhdThesisSubjectOrderBean beanToMoveUp = phdBean.getThesisSubjectBean(order + 1);

        if (beanToMoveUp != null) {
            beanToMoveDown.increaseOrder();
            beanToMoveUp.decreaseOrder();
            phdBean.sortThesisSubjectBeans();
        }

        request.setAttribute("candidacyBean", getCandidacyBean());
        request.setAttribute("individualProcessBean", phdBean);
        RenderUtils.invalidateViewState();

        return mapping.findForward("editPhdIndividualProgramProcessInformation");
    }

    public ActionForward moveUpThesisSubjectForCandidacyStepTwo(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        int order = getIntegerFromRequest(request, "order");
        PhdProgramCandidacyProcessBean candidacyBean = getCandidacyBean();
        PhdThesisSubjectOrderBean beanToMoveUp = candidacyBean.getThesisSubjectBean(order);
        PhdThesisSubjectOrderBean beanToMoveDown = candidacyBean.getThesisSubjectBean(order - 1);

        if (beanToMoveDown != null) {
            beanToMoveUp.decreaseOrder();
            beanToMoveDown.increaseOrder();
            candidacyBean.sortThesisSubjectBeans();
        }

        request.setAttribute("candidacyBean", candidacyBean);
        RenderUtils.invalidateViewState();

        return mapping.findForward("createCandidacyStepTwo");
    }

    public ActionForward moveDownThesisSubjectForCandidacyStepTwo(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        int order = getIntegerFromRequest(request, "order");
        PhdProgramCandidacyProcessBean candidacyBean = getCandidacyBean();
        PhdThesisSubjectOrderBean beanToMoveDown = candidacyBean.getThesisSubjectBean(order);
        PhdThesisSubjectOrderBean beanToMoveUp = candidacyBean.getThesisSubjectBean(order + 1);

        if (beanToMoveUp != null) {
            beanToMoveDown.increaseOrder();
            beanToMoveUp.decreaseOrder();
            candidacyBean.sortThesisSubjectBeans();
        }

        request.setAttribute("candidacyBean", candidacyBean);
        RenderUtils.invalidateViewState();

        return mapping.findForward("createCandidacyStepTwo");
    }

    public ActionForward prepareCreateCandidacyStepTwoFocusAreaPostback(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        PhdProgramCandidacyProcessBean candidacyBean = getCandidacyBean();
        candidacyBean.updateThesisSubjectBeans();

        request.setAttribute("candidacyBean", candidacyBean);
        RenderUtils.invalidateViewState();

        return mapping.findForward("createCandidacyStepTwo");
    }

    public ActionForward createCandidacyStepThree(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        return createCandidacy(mapping, actionForm, request, response);
    }

    private List<PhdParticipantBean> createGuidingsMinimumList(final PhdIndividualProgramProcess process) {
        final List<PhdParticipantBean> result = new ArrayList<PhdParticipantBean>();

        final PhdParticipantBean g1 = new PhdParticipantBean(process);
        g1.setParticipantType(PhdParticipantType.EXTERNAL);
        g1.setWorkLocation("IST");

        final PhdParticipantBean g2 = new PhdParticipantBean(process);
        g2.setParticipantType(PhdParticipantType.EXTERNAL);
        // TODO: change this according to collaboration type acronym
        g2.setWorkLocation("EPFL");

        result.add(g1);
        result.add(g2);

        return result;
    }

    public ActionForward createCandidacyStepTwoInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("candidacyBean", getRenderedObject("candidacyBean"));
        RenderUtils.invalidateViewState();

        addErrorMessage(request, "error.required.fields");

        return mapping.findForward("createCandidacyStepTwo");
    }

    public ActionForward returnCreateCandidacyStepTwo(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        final PhdProgramCandidacyProcessBean bean = getCandidacyBean();
        request.setAttribute("candidacyBean", bean);
        clearDocumentsInformation(bean);
        RenderUtils.invalidateViewState();
        return mapping.findForward("createCandidacyStepTwo");
    }

    public ActionForward addGuiding(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final PhdProgramCandidacyProcessBean bean = getCandidacyBean();
        final PhdParticipantBean guiding = new PhdParticipantBean(bean.getIndividualProgramProcess());
        guiding.setParticipantType(PhdParticipantType.EXTERNAL);

        bean.addGuiding(guiding);

        request.setAttribute("candidacyBean", bean);
        RenderUtils.invalidateViewState();

        return mapping.findForward("createCandidacyStepTwo");
    }

    public ActionForward removeGuiding(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final PhdProgramCandidacyProcessBean bean = getCandidacyBean();
        bean.removeGuiding(getIntegerFromRequest(request, "removeIndex").intValue());

        request.setAttribute("candidacyBean", bean);
        RenderUtils.invalidateViewState();

        return mapping.findForward("createCandidacyStepTwo");
    }

    public ActionForward addQualification(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final PhdProgramCandidacyProcessBean bean = getCandidacyBean();
        bean.addQualification(new QualificationBean());

        request.setAttribute("candidacyBean", bean);
        RenderUtils.invalidateViewState();

        return mapping.findForward("createCandidacyStepTwo");
    }

    public ActionForward removeQualification(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final PhdProgramCandidacyProcessBean bean = getCandidacyBean();
        bean.removeQualification(getIntegerFromRequest(request, "removeIndex").intValue());

        request.setAttribute("candidacyBean", bean);
        RenderUtils.invalidateViewState();

        return mapping.findForward("createCandidacyStepTwo");
    }

    public ActionForward addCandidacyReferee(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final PhdProgramCandidacyProcessBean bean = getCandidacyBean();
        bean.addCandidacyReferee(new PhdCandidacyRefereeBean());

        request.setAttribute("candidacyBean", bean);
        RenderUtils.invalidateViewState();

        return mapping.findForward("createCandidacyStepTwo");
    }

    public ActionForward removeCandidacyReferee(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final PhdProgramCandidacyProcessBean bean = getCandidacyBean();
        if (bean.getCandidacyReferees().size() > MINIMUM_CANDIDACY_REFEREES) {
            bean.removeCandidacyReferee(getIntegerFromRequest(request, "removeIndex").intValue());
        }

        request.setAttribute("candidacyBean", bean);
        RenderUtils.invalidateViewState();

        return mapping.findForward("createCandidacyStepTwo");
    }

    private PhdProgramDocumentUploadBean createDocumentBean(final PhdIndividualProgramDocumentType type) {
        final PhdProgramDocumentUploadBean bean = new PhdProgramDocumentUploadBean();
        bean.setType(type);
        return bean;
    }

    private List<PhdProgramDocumentUploadBean> createHabilitationCertificateDocuments(final PhdProgramCandidacyProcessBean bean) {
        final List<PhdProgramDocumentUploadBean> result =
                new ArrayList<PhdProgramDocumentUploadBean>(bean.getQualifications().size());
        if (bean.hasAnyQualification()) {
            bean.sortQualificationsByAttendedEnd();
            for (final QualificationBean qualification : bean.getQualifications()) {
                final PhdProgramDocumentUploadBean uploadBean =
                        createDocumentBean(PhdIndividualProgramDocumentType.HABILITATION_CERTIFICATE_DOCUMENT);
                uploadBean.setRemarks(qualification.getType().getLocalizedName());
                result.add(uploadBean);
            }
        }
        return result;
    }

    private List<PhdProgramDocumentUploadBean> createPhdGuidingLetters(final PhdProgramCandidacyProcessBean bean) {
        final List<PhdProgramDocumentUploadBean> result = new ArrayList<PhdProgramDocumentUploadBean>(bean.getGuidings().size());
        if (bean.hasAnyGuiding()) {
            for (int i = 0; i < bean.getGuidings().size(); i++) {
                result.add(createDocumentBean(PhdIndividualProgramDocumentType.GUIDER_ACCEPTANCE_LETTER));
            }
        }
        return result;
    }

    public ActionForward createCandidacyStepThreeInvalid(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("candidacyBean", getRenderedObject("candidacyBean"));
        return mapping.findForward("createCandidacyStepThree");
    }

    @Override
    public ActionForward createCandidacy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final PhdProgramCandidacyProcessBean bean = getCandidacyBean();
        try {
            CreateNewProcess.run(PublicPhdIndividualProgramProcess.class, bean);
            sendApplicationSuccessfullySubmitedEmail(bean.getCandidacyHashCode(), request);
        } catch (final DomainException e) {
            if ("error.person.existent.docIdAndType".equals(e.getKey())) {
                addErrorMessage(request, "error.phd.public.candidacy.fill.personal.information.and.institution.id", e.getArgs());
            } else {
                addErrorMessage(request, e.getKey(), e.getArgs());
            }
            bean.clearPerson();

            // clearDocumentsInformation(bean);
            return createCandidacyStepOneInvalid(mapping, form, request, response);
        }

        final String url = EPFLPhdCandidacyProcessProperties.getPublicCandidacyAccessLink(bean.getCandidacyHashCode());
        return new FenixActionForward(request, new ActionForward(url, true));
    }

    public ActionForward showCandidacySuccess(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        return mapping.findForward("showCandidacySuccess");
    }

    private void sendApplicationSuccessfullySubmitedEmail(final PhdProgramPublicCandidacyHashCode hashCode,
            final HttpServletRequest request) {

        // TODO: if candidacy period exists, then change body message to send
        // candidacy limit end date

        final ResourceBundle bundle = ResourceBundle.getBundle("resources.PhdResources", Language.getLocale());
        final String subject = bundle.getString("message.phd.epfl.email.subject.application.submited");
        final String body = bundle.getString("message.phd.epfl.email.body.application.submited");
        hashCode.sendEmail(subject, String.format(body, hashCode.getPhdProgramCandidacyProcess().getProcessNumber(),
                EPFLPhdCandidacyProcessProperties.getPublicCandidacyAccessLink(), hashCode.getValue()));
    }

    private void clearDocumentsInformation(final PhdProgramCandidacyProcessBean bean) {
        bean.getCurriculumVitae().setFile(null);
        RenderUtils.invalidateViewState("candidacyBean.curriculumVitae");

        bean.getIdentificationDocument().setFile(null);
        RenderUtils.invalidateViewState("candidacyBean.identificationDocument");

        bean.getMotivationLetter().setFile(null);
        RenderUtils.invalidateViewState("candidacyBean.motivationLetter");

        bean.getSocialSecurityDocument().setFile(null);
        RenderUtils.invalidateViewState("candidacyBean.socialSecurityDocument");

        bean.getResearchPlan().setFile(null);
        RenderUtils.invalidateViewState("candidacyBean.researchPlan");

        bean.getDissertationOrFinalWorkDocument().setFile(null);
        RenderUtils.invalidateViewState("candidacyBean.dissertationOrFinalWorkDocument");

        bean.removeHabilitationCertificateDocumentFiles();
        invalidateHabilitationCertificateDocumentViewStates();

        bean.setPhdGuidingLetters(null);
        invalidatePhdGuidingLetterViewStates();
    }

    private void invalidateHabilitationCertificateDocumentViewStates() {
        invalidViewStatesWith("candidacyBean.habilitationCertificateDocument");
    }

    private void invalidatePhdGuidingLetterViewStates() {
        invalidViewStatesWith("candidacyBean.phdGuidingLetter");
    }

    private void invalidViewStatesWith(final String prefixId) {
        for (final IViewState viewState : getViewStatesWithPrefixId(prefixId)) {
            RenderUtils.invalidateViewState(viewState.getId());
        }
    }

    public ActionForward addHabilitationCertificateDocument(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        final PhdProgramCandidacyProcessBean bean = getCandidacyBean();
        final PhdProgramDocumentUploadBean document =
                createDocumentBean(PhdIndividualProgramDocumentType.HABILITATION_CERTIFICATE_DOCUMENT);
        bean.addHabilitationCertificateDocument(document);

        request.setAttribute("candidacyBean", bean);
        RenderUtils.invalidateViewState();

        return mapping.findForward("createCandidacyStepThree");
    }

    public ActionForward removeHabilitationCertificateDocument(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        final PhdProgramCandidacyProcessBean bean = getCandidacyBean();
        if (bean.getHabilitationCertificateDocuments().size() > MINIMUM_HABILITATIONS_AND_CERTIFICATES) {
            bean.removeHabilitationCertificateDocument(getIntegerFromRequest(request, "removeIndex").intValue());
        }
        request.setAttribute("candidacyBean", bean);
        clearDocumentsInformation(bean);
        RenderUtils.invalidateViewState();

        return mapping.findForward("createCandidacyStepThree");
    }

    public ActionForward viewCandidacy(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        return viewCandidacy(mapping, request,
                (PhdProgramPublicCandidacyHashCode) PublicCandidacyHashCode.getPublicCandidacyCodeByHash(request
                        .getParameter("hash")));
    }

    private ActionForward viewCandidacy(ActionMapping mapping, HttpServletRequest request,
            final PhdProgramPublicCandidacyHashCode hashCode) {

        if (hashCode == null || !hashCode.hasCandidacyProcess()) {
            return mapping.findForward("createCandidacyStepOne");
        }

        PhdIndividualProgramProcess phdProcess = hashCode.getIndividualProgramProcess();

        final PhdProgramCandidacyProcessBean bean = new PhdProgramCandidacyProcessBean();
        bean.setCandidacyHashCode(hashCode);

        request.setAttribute("candidacyBean", bean);
        request.setAttribute("individualProgramProcess", phdProcess);
        canEditCandidacy(request, bean.getCandidacyHashCode());
        canEditPersonalInformation(request, hashCode.getPerson());

        PersonBean personBean = new PersonBean(phdProcess.getPerson());
        initPersonBean(personBean, phdProcess.getPerson());
        request.setAttribute("personBean", personBean);

        request.setAttribute("candidacyPeriod", getPhdCandidacyPeriod(hashCode));
        validateProcess(request, hashCode.getIndividualProgramProcess());

        request.setAttribute("pendingPartyContactBean", new PendingPartyContactBean(hashCode.getIndividualProgramProcess()
                .getPerson()));

        return mapping.findForward("viewCandidacy");
    }

    public ActionForward backToViewCandidacy(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        return viewCandidacy(mapping, request, getCandidacyBean().getCandidacyHashCode());
    }

    public ActionForward prepareEditPersonalInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final PhdProgramCandidacyProcessBean bean = getCandidacyBean();
        final PhdIndividualProgramProcess individualProgramProcess = bean.getCandidacyHashCode().getIndividualProgramProcess();
        final Person person = individualProgramProcess.getPerson();

        canEditPersonalInformation(request, person);
        bean.setPersonBean(new PersonBean(person));

        /* TODO: UGLY HACK DUE TO PENDING VALIDATION DATA FOR PERSON */
        initPersonBean(bean.getPersonBean(), person);

        request.setAttribute("candidacyBean", bean);

        return mapping.findForward("editPersonalInformation");
    }

    private void initPersonBean(final PersonBean personBean, Person person) {
        personBean.setName(person.getName());
        personBean.setGivenNames(person.getGivenNames());
        personBean.setFamilyNames(person.getFamilyNames());
        personBean.setUsername(person.getUsername());
        personBean.setGender(person.getGender());
        personBean.setMaritalStatus(person.getMaritalStatus());
        personBean.setFatherName(person.getNameOfFather());
        personBean.setMotherName(person.getNameOfMother());
        personBean.setProfession(person.getProfession());
        personBean.setNationality(person.getCountry());

        personBean.setCountryOfBirth(person.getCountryOfBirth());
        personBean.setDateOfBirth(person.getDateOfBirthYearMonthDay());
        personBean.setParishOfBirth(person.getParishOfBirth());
        personBean.setDistrictOfBirth(person.getDistrictOfBirth());
        personBean.setDistrictSubdivisionOfBirth(person.getDistrictSubdivisionOfBirth());

        personBean.setDocumentIdEmissionDate(person.getEmissionDateOfDocumentIdYearMonthDay());
        personBean.setDocumentIdEmissionLocation(person.getEmissionLocationOfDocumentId());
        personBean.setDocumentIdExpirationDate(person.getExpirationDateOfDocumentIdYearMonthDay());
        personBean.setDocumentIdNumber(person.getDocumentIdNumber());
        personBean.setIdDocumentType(person.getIdDocumentType());
        personBean.setSocialSecurityNumber(person.getSocialSecurityNumber());

        PendingPartyContactBean pendingPartyContactBean = new PendingPartyContactBean(person);
        if (pendingPartyContactBean.getDefaultPhysicalAddress() != null) {
            final PhysicalAddress physicalAddress = pendingPartyContactBean.getDefaultPhysicalAddress();
            personBean.setAddress(physicalAddress.getAddress());
            personBean.setArea(physicalAddress.getArea());
            personBean.setAreaCode(physicalAddress.getAreaCode());
            personBean.setAreaOfAreaCode(physicalAddress.getAreaOfAreaCode());
            personBean.setParishOfResidence(physicalAddress.getParishOfResidence());
            personBean.setDistrictSubdivisionOfResidence(physicalAddress.getDistrictSubdivisionOfResidence());
            personBean.setDistrictOfResidence(physicalAddress.getDistrictOfResidence());
            personBean.setCountryOfResidence(physicalAddress.getCountryOfResidence());
        }

        personBean.setPhone(pendingPartyContactBean.getDefaultPhone() != null ? pendingPartyContactBean.getDefaultPhone()
                .getNumber() : null);
        personBean.setMobile(pendingPartyContactBean.getDefaultMobilePhone() != null ? pendingPartyContactBean
                .getDefaultMobilePhone().getNumber() : null);

        personBean.setEmail(pendingPartyContactBean.getDefaultEmailAddress().getValue());

        personBean.setEmailAvailable(person.getAvailableEmail());
        personBean.setHomepageAvailable(person.getAvailableWebSite());

        personBean.setPerson(person);
    }

    public ActionForward editPersonalInformationInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final PhdProgramCandidacyProcessBean bean = getCandidacyBean();
        canEditPersonalInformation(request, bean.getPersonBean().getPerson());
        request.setAttribute("candidacyBean", bean);
        return mapping.findForward("editPersonalInformation");
    }

    public ActionForward editPersonalInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final PhdProgramCandidacyProcessBean bean = getCandidacyBean();
        final PhdIndividualProgramProcess individualProgramProcess = bean.getCandidacyHashCode().getIndividualProgramProcess();
        final Person person = individualProgramProcess.getPerson();
        canEditPersonalInformation(request, person);

        PersonBean personBean = bean.getPersonBean();
        final String familyName = personBean.getFamilyNames();
        final String composedName =
                familyName == null || familyName.isEmpty() ? personBean.getGivenNames() : personBean.getGivenNames() + " "
                        + familyName;
        personBean.setName(composedName);

        try {
            ExecuteProcessActivity.run(createMockUserView(person), individualProgramProcess, EditPersonalInformation.class,
                    bean.getPersonBean());
        } catch (final DomainException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());
            request.setAttribute("candidacyBean", bean);
            return mapping.findForward("editPersonalInformation");
        }

        return viewCandidacy(mapping, request, bean.getCandidacyHashCode());
    }

    public ActionForward prepareUploadDocuments(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final PhdProgramCandidacyProcessBean bean = getCandidacyBean();
        RenderUtils.invalidateViewState();

        request.setAttribute("candidacyBean", bean);
        request.setAttribute("candidacyProcessDocuments", bean.getCandidacyHashCode().getIndividualProgramProcess()
                .getCandidacyProcessDocuments());

        final PhdProgramDocumentUploadBean uploadBean = new PhdProgramDocumentUploadBean();
        uploadBean.setIndividualProgramProcess(bean.getCandidacyHashCode().getIndividualProgramProcess());
        request.setAttribute("documentByType", uploadBean);

        validateProcessDocuments(request, bean.getCandidacyHashCode().getIndividualProgramProcess());

        return mapping.findForward("uploadCandidacyDocuments");
    }

    @Override
    public ActionForward uploadDocumentsInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final PhdProgramCandidacyProcessBean bean = getCandidacyBean();
        request.setAttribute("candidacyBean", bean);
        request.setAttribute("documentByType", getRenderedObject("documentByType"));
        request.setAttribute("candidacyProcessDocuments", bean.getCandidacyHashCode().getIndividualProgramProcess()
                .getCandidacyProcessDocuments());

        if (!RenderUtils.getViewState("documentByType").isValid()) {
            addErrorMessage(request, "error.documentToUpload.not.valid");
        }
        RenderUtils.invalidateViewState();
        validateProcessDocuments(request, bean.getCandidacyHashCode().getIndividualProgramProcess());

        return mapping.findForward("uploadCandidacyDocuments");
    }

    @Override
    public ActionForward uploadDocuments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!RenderUtils.getViewState("documentByType").isValid()) {
            return uploadDocumentsInvalid(mapping, form, request, response);
        }

        final PhdProgramDocumentUploadBean uploadBean = getRenderedObject("documentByType");

        if (!uploadBean.hasAnyInformation()) {
            addErrorMessage(request, "message.no.documents.to.upload");
            return uploadDocumentsInvalid(mapping, form, request, response);

        }
        try {
            PhdIndividualProgramProcess individualProgramProcess = uploadBean.getIndividualProgramProcess();
            final Person person = individualProgramProcess.getPerson();
            ExecuteProcessActivity.run(createMockUserView(person), individualProgramProcess, UploadDocuments.class,
                    Collections.singletonList(uploadBean));
            addSuccessMessage(request, "message.documents.uploaded.with.success");

        } catch (final DomainException e) {
            addErrorMessage(request, "message.no.documents.to.upload");
            return uploadDocumentsInvalid(mapping, form, request, response);
        }

        return prepareUploadDocuments(mapping, form, request, response);
    }

    public ActionForward prepareEditPhdIndividualProgramProcessInformation(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        final PhdProgramCandidacyProcessBean candidacyBean = getCandidacyBean();
        request.setAttribute("candidacyBean", candidacyBean);
        request.setAttribute("individualProcessBean", new PhdIndividualProgramProcessBean(candidacyBean.getCandidacyHashCode()
                .getIndividualProgramProcess()));

        return mapping.findForward("editPhdIndividualProgramProcessInformation");
    }

    public ActionForward prepareEditPhdIndividualProgramProcessInformationFocusAreaPostback(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("candidacyBean", getCandidacyBean());

        PhdIndividualProgramProcessBean phdBean = getRenderedObject("individualProcessBean");
        phdBean.updateThesisSubjectBeans();

        request.setAttribute("individualProcessBean", phdBean);

        RenderUtils.invalidateViewState();

        return mapping.findForward("editPhdIndividualProgramProcessInformation");
    }

    public ActionForward editPhdIndividualProgramProcessInformationInvalid(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute("candidacyBean", getCandidacyBean());
        request.setAttribute("individualProcessBean", getRenderedObject("individualProcessBean"));

        return mapping.findForward("editPhdIndividualProgramProcessInformation");
    }

    public ActionForward editPhdIndividualProgramProcessInformation(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        final PhdIndividualProgramProcessBean bean = getRenderedObject("individualProcessBean");
        try {
            PhdIndividualProgramProcess individualProgramProcess = bean.getIndividualProgramProcess();
            final Person person = individualProgramProcess.getPerson();
            ExecuteProcessActivity.run(createMockUserView(person), individualProgramProcess,
                    EditIndividualProcessInformation.class, bean);
            addSuccessMessage(request, "message.phdIndividualProgramProcessInformation.edit.success");

        } catch (final DomainException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());
            request.setAttribute("candidacyBean", getCandidacyBean());
            request.setAttribute("individualProcessBean", bean);
            return mapping.findForward("editPhdIndividualProgramProcessInformation");
        }

        return viewCandidacy(mapping, request, getCandidacyBean().getCandidacyHashCode());
    }

    public ActionForward prepareEditCandidacyGuidings(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        return prepareAddGuidingToExistingCandidacy(mapping, actionForm, request, response);
    }

    public ActionForward editCandidacyGuidingsInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("candidacyBean", getCandidacyBean());
        return mapping.findForward("editCandidacyGuidings");
    }

    public ActionForward prepareAddGuidingToExistingCandidacy(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        final PhdProgramCandidacyProcessBean bean = getCandidacyBean();
        request.setAttribute("candidacyBean", bean);

        if (!bean.getCandidacyHashCode().getIndividualProgramProcess().hasAnyGuidings()) {
            bean.setGuidings(createGuidingsMinimumList(bean.getIndividualProgramProcess()));
        } else {
            bean.setGuidings(new ArrayList<PhdParticipantBean>());
            bean.addGuiding(new PhdParticipantBean(bean.getIndividualProgramProcess()));
        }

        RenderUtils.invalidateViewState();
        return mapping.findForward("editCandidacyGuidings");
    }

    public ActionForward removeGuidingFromCreationList(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final PhdProgramCandidacyProcessBean bean = getCandidacyBean();
        if (bean.getGuidings().size() > 1) {
            bean.removeGuiding(getIntegerFromRequest(request, "removeIndex").intValue());
        }

        request.setAttribute("candidacyBean", bean);
        RenderUtils.invalidateViewState();

        return mapping.findForward("editCandidacyGuidings");
    }

    public ActionForward addGuidingToExistingCandidacy(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final PhdProgramCandidacyProcessBean bean = getCandidacyBean();

        try {
            PhdIndividualProgramProcess individualProgramProcess = bean.getCandidacyHashCode().getIndividualProgramProcess();
            final Person person = individualProgramProcess.getPerson();
            ExecuteProcessActivity.run(createMockUserView(person), individualProgramProcess, AddGuidingsInformation.class,
                    bean.getGuidings());
            addSuccessMessage(request, "message.guiding.created.with.success");

        } catch (final DomainException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());
            return editCandidacyGuidingsInvalid(mapping, actionForm, request, response);
        }

        bean.setGuidings(null);
        return prepareEditCandidacyGuidings(mapping, actionForm, request, response);
    }

    public ActionForward removeGuidingFromExistingCandidacy(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        final String externalId = (String) getFromRequest(request, "removeIndex");
        final PhdProgramCandidacyProcessBean bean = getCandidacyBean();
        final PhdParticipant guiding = getGuiding(bean.getCandidacyHashCode().getIndividualProgramProcess(), externalId);

        try {
            PhdIndividualProgramProcess individualProgramProcess =
                    getCandidacyBean().getCandidacyHashCode().getIndividualProgramProcess();
            final Person person = individualProgramProcess.getPerson();
            ExecuteProcessActivity.run(createMockUserView(person), individualProgramProcess, DeleteGuiding.class, guiding);
            addSuccessMessage(request, "message.guiding.deleted.with.success");

        } catch (final DomainException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());
            return editCandidacyGuidingsInvalid(mapping, actionForm, request, response);
        }

        return prepareEditCandidacyGuidings(mapping, actionForm, request, response);
    }

    private PhdParticipant getGuiding(final PhdIndividualProgramProcess individualProgramProcess, final String externalId) {
        for (final PhdParticipant guiding : individualProgramProcess.getGuidingsSet()) {
            if (guiding.getExternalId().equals(externalId)) {
                return guiding;
            }
        }
        return null;
    }

    public ActionForward prepareEditCandidacyReferees(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        return prepareAddCandidacyRefereeToExistingCandidacy(mapping, actionForm, request, response);
    }

    public ActionForward prepareAddCandidacyRefereeToExistingCandidacy(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute("candidacyBean", getCandidacyBean());
        request.setAttribute("refereeBean", new PhdCandidacyRefereeBean());
        return mapping.findForward("editCandidacyReferees");
    }

    public ActionForward editCandidacyRefereesInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("candidacyBean", getCandidacyBean());
        request.setAttribute("refereeBean", getRenderedObject("refereeBean"));
        return mapping.findForward("editCandidacyReferees");
    }

    public ActionForward addCandidacyRefereeToExistingCandidacy(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        try {
            PhdIndividualProgramProcess individualProgramProcess =
                    getCandidacyBean().getCandidacyHashCode().getIndividualProgramProcess();
            final Person person = individualProgramProcess.getPerson();

            ExecuteProcessActivity.run(createMockUserView(person), individualProgramProcess, AddCandidacyReferees.class,
                    Collections.singletonList(getRenderedObject("refereeBean")));
            addSuccessMessage(request, "message.qualification.information.create.success");
            RenderUtils.invalidateViewState("refereeBean");

        } catch (final DomainException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());
            return editCandidacyRefereesInvalid(mapping, actionForm, request, response);
        }

        return prepareEditCandidacyReferees(mapping, actionForm, request, response);
    }

    public ActionForward sendCandidacyRefereeEmail(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final PhdProgramCandidacyProcessBean bean = getCandidacyBean();
        final PhdCandidacyReferee referee = getReferee(bean.getCandidacyHashCode().getIndividualProgramProcess(), request);
        referee.sendEmail();
        addSuccessMessage(request, "message.candidacy.referee.email.sent.with.success", referee.getName());

        return prepareEditCandidacyReferees(mapping, actionForm, request, response);
    }

    private PhdCandidacyReferee getReferee(final PhdIndividualProgramProcess process, final HttpServletRequest request) {
        final String externalId = (String) getFromRequest(request, "removeIndex");
        for (final PhdCandidacyReferee referee : process.getPhdCandidacyReferees()) {
            if (referee.getExternalId().equals(externalId)) {
                return referee;
            }
        }
        return null;
    }

    public ActionForward prepareEditQualifications(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        return prepareAddQualificationToExistingCandidacy(mapping, actionForm, request, response);
    }

    public ActionForward editQualificationsInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("candidacyBean", getCandidacyBean());
        request.setAttribute("qualificationBean", getRenderedObject("qualificationBean"));
        return mapping.findForward("editQualifications");
    }

    public ActionForward prepareAddQualificationToExistingCandidacy(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute("candidacyBean", getCandidacyBean());
        request.setAttribute("qualificationBean", new QualificationBean());
        return mapping.findForward("editQualifications");
    }

    public ActionForward addQualificationToExistingCandidacy(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        try {
            PhdIndividualProgramProcess individualProgramProcess =
                    getCandidacyBean().getCandidacyHashCode().getIndividualProgramProcess();
            final Person person = individualProgramProcess.getPerson();
            ExecuteProcessActivity.run(createMockUserView(person), individualProgramProcess, AddQualification.class,
                    getRenderedObject("qualificationBean"));
            addSuccessMessage(request, "message.qualification.information.create.success");
            RenderUtils.invalidateViewState("qualificationBean");

        } catch (final DomainException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());
            return editQualificationsInvalid(mapping, actionForm, request, response);
        }

        return prepareEditQualifications(mapping, actionForm, request, response);
    }

    public ActionForward removeQualificationFromExistingCandidacy(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        final PhdProgramCandidacyProcessBean bean = getCandidacyBean();

        final String externalId = (String) getFromRequest(request, "removeIndex");
        PhdIndividualProgramProcess individualProgramProcess = bean.getCandidacyHashCode().getIndividualProgramProcess();
        final Person person = individualProgramProcess.getPerson();

        final Qualification qualification = getQualification(individualProgramProcess, externalId);

        try {
            ExecuteProcessActivity.run(createMockUserView(person), individualProgramProcess, DeleteQualification.class,
                    qualification);
            addSuccessMessage(request, "message.qualification.information.delete.success");

        } catch (final DomainException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());
        }

        RenderUtils.invalidateViewState("qualificationBean");
        return prepareEditQualifications(mapping, actionForm, request, response);
    }

    private Qualification getQualification(final PhdIndividualProgramProcess individualProgramProcess, final String externalId) {
        for (final Qualification qualification : individualProgramProcess.getQualifications()) {
            if (qualification.getExternalId().equals(externalId)) {
                return qualification;
            }
        }
        return null;
    }

    public ActionForward prepareCreateRefereeLetter(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final PhdCandidacyReferee hashCode =
                (PhdCandidacyReferee) PublicCandidacyHashCode.getPublicCandidacyCodeByHash(request.getParameter("hash"));

        if (hashCode == null) {
            request.setAttribute("no-information", Boolean.TRUE);
            return mapping.findForward("createRefereeLetterSuccess");
        }

        if (hashCode.hasLetter()) {
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

    public ActionForward createRefereeLetterInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        final PhdCandidacyRefereeLetterBean bean = getRenderedObject("createRefereeLetterBean");
        request.setAttribute("createRefereeLetterBean", bean);
        RenderUtils.invalidateViewState("createRefereeLetterBean.comments");
        bean.setFile(null);
        return mapping.findForward("createRefereeLetter");
    }

    public ActionForward createRefereeLetter(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

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

    private boolean hasAnyRefereeLetterViewStateInvalid() {
        for (final IViewState viewState : getViewStatesWithPrefixId("createRefereeLetterBean.")) {
            if (!viewState.isValid()) {
                return true;
            }
        }
        return false;
    }

    public ActionForward prepareUploadPhoto(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("candidacyBean", getCandidacyBean());
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
            UploadOwnPhoto.upload(photo, bean.getCandidacyHashCode().getPerson());

        } catch (final UnableToProcessTheImage e) {
            addErrorMessage(request, "error.photo.upload.unable.to.process.image");
            photo.deleteTemporaryFiles();
            return uploadPhotoInvalid(mapping, actionForm, request, response);

        } catch (final DomainException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());
            photo.deleteTemporaryFiles();
            return uploadPhotoInvalid(mapping, actionForm, request, response);
        }

        return viewCandidacy(mapping, request, bean.getCandidacyHashCode());
    }

    private boolean validateProcess(final HttpServletRequest request, final PhdIndividualProgramProcess process) {
        boolean result = true;

        if (!process.hasPhdProgramFocusArea()) {
            addValidationMessage(request, "message.validation.missing.focus.area");
            result &= false;
        }
        if (process.getPhdCandidacyReferees().size() < MINIMUM_CANDIDACY_REFEREES) {
            addValidationMessage(request, "message.validation.missing.minimum.candidacy.referees",
                    String.valueOf(MINIMUM_CANDIDACY_REFEREES));
            result &= false;
        }
        return validateProcessDocuments(request, process);
    }

    private boolean validateProcessDocuments(final HttpServletRequest request, final PhdIndividualProgramProcess process) {
        boolean result = true;

        if (!process.hasCandidacyProcessDocument(PhdIndividualProgramDocumentType.CV)) {
            addValidationMessage(request, "message.validation.missing.cv");
            result &= false;
        }
        if (!process.hasCandidacyProcessDocument(PhdIndividualProgramDocumentType.ID_DOCUMENT)) {
            addValidationMessage(request, "message.validation.missing.id.document");
            result &= false;
        }
        if (!process.hasCandidacyProcessDocument(PhdIndividualProgramDocumentType.MOTIVATION_LETTER)) {
            addValidationMessage(request, "message.validation.missing.motivation.letter");
            result &= false;
        }
        if (process.getCandidacyProcessDocumentsCount(PhdIndividualProgramDocumentType.HABILITATION_CERTIFICATE_DOCUMENT) < process
                .getQualifications().size()) {
            addValidationMessage(request, "message.validation.missing.qualification.documents",
                    String.valueOf(process.getQualifications().size()));
            result &= false;
        }

        return result;
    }

    public ActionForward prepareValidateCandidacy(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final PhdProgramCandidacyProcessBean bean = getCandidacyBean();
        validateProcess(request, bean.getCandidacyHashCode().getIndividualProgramProcess());
        request.setAttribute("candidacyBean", bean);

        return mapping.findForward("validateCandidacy");
    }

    public ActionForward validateCandidacy(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final PhdProgramCandidacyProcessBean bean = getCandidacyBean();
        final PhdIndividualProgramProcess process = bean.getCandidacyHashCode().getIndividualProgramProcess();
        final Person person = process.getPerson();

        if (!validateProcess(request, process)) {
            request.setAttribute("candidacyBean", bean);
            return mapping.findForward("validateCandidacy");
        }

        try {
            ExecuteProcessActivity.run(createMockUserView(person), process, ValidatedByCandidate.class, null);
            addSuccessMessage(request, "message.validation.with.success");

        } catch (final DomainException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());
            return prepareValidateCandidacy(mapping, actionForm, request, response);
        }

        return viewCandidacy(mapping, request, bean.getCandidacyHashCode());
    }

}
