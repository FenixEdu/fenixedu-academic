package net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy.publicProgram;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.caseHandling.CreateNewProcess;
import net.sourceforge.fenixedu.applicationTier.Servico.caseHandling.ExecuteProcessActivity;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PublicCandidacyHashCode;
import net.sourceforge.fenixedu.domain.QualificationBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramCollaborationType;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramDocumentType;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdProgramGuidingBean;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.AddCandidacyReferees;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.AddGuidingsInformation;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.AddQualifications;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.EditPersonalInformation;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.UploadDocuments;
import net.sourceforge.fenixedu.domain.phd.PhdProgramGuidingBean.PhdProgramGuidingType;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyDocumentUploadBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyReferee;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyRefereeBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramPublicCandidacyHashCode;
import net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy.academicAdminOffice.PhdProgramCandidacyProcessDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.Pair;
import pt.utl.ist.fenix.tools.util.i18n.Language;

@Mapping(path = "/candidacies/phdProgramCandidacyProcess", module = "publico")
@Forwards( {

@Forward(name = "createCandidacyIdentification", path = "phdProgram.createCandidacyIdentification"),

@Forward(name = "createCandidacyIdentificationSuccess", path = "phdProgram.createCandidacyIdentificationSuccess"),

@Forward(name = "candidacyIdentificationRecovery", path = "phdProgram.candidacyIdentificationRecovery"),

@Forward(name = "createCandidacyStepOne", path = "phdProgram.createCandidacyStepOne"),

@Forward(name = "createCandidacyStepTwo", path = "phdProgram.createCandidacyStepTwo"),

@Forward(name = "createCandidacyStepThree", path = "phdProgram.createCandidacyStepThree"),

@Forward(name = "createCandidacySuccess", path = "phdProgram.createCandidacySuccess"),

@Forward(name = "viewCandidacy", path = "phdProgram.viewCandidacy"),

@Forward(name = "editPersonalInformation", path = "phdProgram.editPersonalInformation")

})
public class PublicPhdProgramCandidacyProcessDA extends PhdProgramCandidacyProcessDA {

    static private final int MINIMUM_HABILITATIONS_AND_CERTIFICATES = 2;
    static private final int MINIMUM_CANDIDACY_REFEREES = 3;

    public ActionForward prepareCreateCandidacyIdentification(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	final String hash = request.getParameter("hash");
	final PhdProgramPublicCandidacyHashCode hashCode = (PhdProgramPublicCandidacyHashCode) PublicCandidacyHashCode
		.getPublicCandidacyCodeByHash(hash);
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

    private PhdProgramCandidacyProcessBean getCandidacyBean() {
	return (PhdProgramCandidacyProcessBean) getRenderedObject("candidacyBean");
    }

    public ActionForward createCandidacyIdentification(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final PhdProgramCandidacyProcessBean bean = getCandidacyBean();
	final PhdProgramPublicCandidacyHashCode hashCode = PhdProgramPublicCandidacyHashCode
		.getOrCreatePhdProgramCandidacyHashCode(bean.getEmail());

	if (hashCode.hasCandidacyProcess()) {
	    addErrorMessage(request, "error.PhdProgramPublicCandidacyHashCode.already.has.candidacy");
	    return prepareCreateCandidacyIdentification(mapping, actionForm, request, response);
	}

	sendSubmissionEmailForCandidacy(hashCode, request);
	return mapping.findForward("createCandidacyIdentificationSuccess");
    }

    private void sendSubmissionEmailForCandidacy(final PublicCandidacyHashCode hashCode, final HttpServletRequest request) {
	final ResourceBundle bundle = ResourceBundle.getBundle("resources.PhdResources", Language.getLocale());
	final String subject = bundle.getString("message.phd.email.subject.send.link.to.submission.form");
	final String body = bundle.getString("message.phd.email.body.send.link.to.submission.form");
	hashCode.sendEmail(subject, String.format(body, getFullLink(getSubmissionLinkPrefix(), request, hashCode)));
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
	final PhdProgramPublicCandidacyHashCode hashCode = PhdProgramPublicCandidacyHashCode.getPhdProgramCandidacyHashCode(bean
		.getEmail());

	if (hashCode != null) {
	    if (hashCode.hasCandidacyProcess()) {
		sendRecoveryEmailForCandidate(hashCode, request);
	    } else {
		sendSubmissionEmailForCandidacy(hashCode, request);
	    }
	}

	return mapping.findForward("createCandidacyIdentificationSuccess");
    }

    private void sendRecoveryEmailForCandidate(PhdProgramPublicCandidacyHashCode candidacyHashCode, HttpServletRequest request) {
	final ResourceBundle bundle = ResourceBundle.getBundle("resources.PhdResources", Language.getLocale());
	final String subject = bundle.getString("message.phd.email.subject.recovery.access");
	final String body = bundle.getString("message.phd.email.body.recovery.access");
	final String link = getFullLink(getCandidacyAccessLinkPrefix(), request, candidacyHashCode);
	candidacyHashCode.sendEmail(subject, String.format(body, link, candidacyHashCode.getPhdProgramCandidacyProcess()
		.getProcessNumber()));
    }

    private String getFullLink(final String prefix, HttpServletRequest request, PublicCandidacyHashCode hashCode) {
	final ResourceBundle bundle = ResourceBundle.getBundle("resources.CandidateResources");
	final String link = bundle.getString("const.public.applications.link.http.hostname");
	return link + request.getContextPath() + getLink(prefix, hashCode);
    }

    private String getLink(final String prefix, final PublicCandidacyHashCode hashCode) {
	return prefix + "?hash=" + hashCode.getValue() + "&locale=" + Language.getLocale().getLanguage();
    }

    private String getSubmissionLinkPrefix() {
	return Language.getLanguage().equals(Language.en) ? "/candidacies/phd-program/submission"
		: "/candidaturas/programa-doutoral/submissao";
    }

    private String getCandidacyAccessLinkPrefix() {
	return Language.getLanguage().equals(Language.en) ? "/candidacies/phd-program/access"
		: "/candidaturas/programa-doutoral/acesso";
    }

    public ActionForward prepareCreateCandidacy(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	// TODO: for now send directly to first page
	return createCandidacyStepOne(mapping, actionForm, request, response);
    }

    public ActionForward createCandidacyStepOne(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final String hash = request.getParameter("hash");
	final PhdProgramPublicCandidacyHashCode hashCode = (PhdProgramPublicCandidacyHashCode) PublicCandidacyHashCode
		.getPublicCandidacyCodeByHash(hash);

	if (hashCode == null) {
	    // TODO: if prepareCreateCandidacy is different then send to that
	    // page
	    return mapping.findForward("createCandidacyStepOne");
	}

	// TODO check for candidacy period if appliable?

	if (hashCode.hasCandidacyProcess()) {
	    return viewCandidacy(mapping, request, hashCode);
	}

	final PhdProgramCandidacyProcessBean bean = new PhdProgramCandidacyProcessBean();
	bean.setPersonBean(new PersonBean());
	bean.getPersonBean().setEmail(hashCode.getEmail());
	bean.getPersonBean().setCreateLoginIdentificationAndUserIfNecessary(false);
	bean.setCandidacyHashCode(hashCode);

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
	request.setAttribute("candidacyBean", getRenderedObject("candidacyBean"));
	RenderUtils.invalidateViewState();
	return mapping.findForward("createCandidacyStepOne");
    }

    public ActionForward createCandidacyStepTwo(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final PhdProgramCandidacyProcessBean bean = getCandidacyBean();
	bean.setExecutionYear(ExecutionYear.readCurrentExecutionYear());
	// TODO:IMPORTANT change when extending this candidacies to all types
	bean.setCollaborationType(PhdIndividualProgramCollaborationType.EPFL);
	// TODO: ---------------------------------------------------------------

	bean.setGuidings(new ArrayList<PhdProgramGuidingBean>());
	bean.setQualifications(new ArrayList<QualificationBean>());
	bean.setCandidacyReferees(new ArrayList<PhdCandidacyRefereeBean>());

	request.setAttribute("candidacyBean", bean);
	RenderUtils.invalidateViewState();

	return mapping.findForward("createCandidacyStepTwo");
    }

    public ActionForward createCandidacyStepTwoInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("candidacyBean", getRenderedObject("candidacyBean"));
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
	final PhdProgramGuidingBean guiding = new PhdProgramGuidingBean();
	guiding.setGuidingType(PhdProgramGuidingType.EXTERNAL);
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

    public ActionForward createCandidacyStepThree(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final PhdProgramCandidacyProcessBean bean = getCandidacyBean();

	bean.setCurriculumVitae(createDocumentBean(PhdIndividualProgramDocumentType.CV));
	bean.setIdentificationDocument(createDocumentBean(PhdIndividualProgramDocumentType.ID_DOCUMENT));
	bean.setMotivationLetter(createDocumentBean(PhdIndividualProgramDocumentType.MOTIVATION_LETTER));
	bean.setSocialSecurityDocument(createDocumentBean(PhdIndividualProgramDocumentType.SOCIAL_SECURITY));
	bean.setResearchPlan(createDocumentBean(PhdIndividualProgramDocumentType.RESEARCH_PLAN));
	bean.setReportOrWorkDocument(createDocumentBean(PhdIndividualProgramDocumentType.REPORT_OR_WORK_DOCUMENT));
	bean.setHabilitationCertificateDocuments(createHabilitationCertificateDocuments());
	bean.setPhdGuidingLetters(createPhdGuidingLetters(bean));

	request.setAttribute("candidacyBean", bean);
	RenderUtils.invalidateViewState();

	return mapping.findForward("createCandidacyStepThree");
    }

    private PhdCandidacyDocumentUploadBean createDocumentBean(final PhdIndividualProgramDocumentType type) {
	final PhdCandidacyDocumentUploadBean bean = new PhdCandidacyDocumentUploadBean();
	bean.setType(type);
	return bean;
    }

    private List<PhdCandidacyDocumentUploadBean> createHabilitationCertificateDocuments() {
	final List<PhdCandidacyDocumentUploadBean> result = new ArrayList<PhdCandidacyDocumentUploadBean>(2);
	result.add(createDocumentBean(PhdIndividualProgramDocumentType.HABILITATION_CERTIFICATE_DOCUMENT));
	result.add(createDocumentBean(PhdIndividualProgramDocumentType.HABILITATION_CERTIFICATE_DOCUMENT));
	return result;
    }

    private List<PhdCandidacyDocumentUploadBean> createPhdGuidingLetters(final PhdProgramCandidacyProcessBean bean) {
	final List<PhdCandidacyDocumentUploadBean> result = new ArrayList<PhdCandidacyDocumentUploadBean>(bean.getGuidings()
		.size());
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

	    if (!hasMinimumDocuments(request)) {
		clearDocumentsInformation(bean);
		return createCandidacyStepThreeInvalid(mapping, form, request, response);
	    }

	    // check for CANDIDACY PERIOD?

	    // --------------------------
	    // **********************************************************
	    // CHECK IF PERSON ALREADY EXISTS AND USE EXISTING or use ist number
	    // ????

	    /*
	     * then check by that number and if information is correct use,
	     * otherwise error
	     * 
	     * and if exists any user with document id equal then error
	     */

	    // **********************************************************
	    // --------------------------
	    /*
	     * 
	     * CHECK IF CANDIDACY EMAIL REFEREE IS EQUAL TO CANDIDATES
	     */

	    final PhdIndividualProgramProcess process = (PhdIndividualProgramProcess) CreateNewProcess.run(
		    PhdIndividualProgramProcess.class, bean, buildActivities(bean));

	    sendApplicationSuccessfullySubmitedEmail(bean.getCandidacyHashCode(), request);
	    sendCandidacyRefereesEmail(process, request);

	} catch (final DomainException e) {
	    addErrorMessage(request, e.getKey(), e.getArgs());
	    bean.clearPerson();
	    clearDocumentsInformation(bean);
	    request.setAttribute("candidacyBean", bean);
	    return mapping.findForward("createCandidacyStepThree");
	}

	return mapping.findForward("createCandidacySuccess");
    }

    private void sendApplicationSuccessfullySubmitedEmail(final PhdProgramPublicCandidacyHashCode hashCode,
	    final HttpServletRequest request) {

	// TODO: if candidacy period exists, then change body message to send
	// candidacy limit end date

	final ResourceBundle bundle = ResourceBundle.getBundle("resources.PhdResources", Language.getLocale());
	final String subject = bundle.getString("message.phd.email.subject.application.submited");
	final String body = bundle.getString("message.phd.email.body.application.submited");
	final String link = getFullLink(getCandidacyAccessLinkPrefix(), request, hashCode);
	hashCode.sendEmail(subject, String.format(body, hashCode.getPhdProgramCandidacyProcess().getProcessNumber(), link));
    }

    private void sendCandidacyRefereesEmail(final PhdIndividualProgramProcess process, final HttpServletRequest request) {
	// TODO: if candidacy period exists, then change body message to send
	// candidacy limit end date

	final ResourceBundle bundle = ResourceBundle.getBundle("resources.PhdResources", Language.getLocale());
	final String subject = String.format(bundle.getString("message.phd.email.subject.referee.form"), process.getPerson()
		.getName());
	final String body = bundle.getString("message.phd.email.body.referee.form");
	final String link = getFullLink(getCandidacyRefereeAccessLinkPrefix(), request, process.getCandidacyProcessHashCode());
	final String finalBody = String.format(body, process.getPerson().getName(), link);

	for (final PhdCandidacyReferee referee : process.getPhdCandidacyReferees()) {
	    referee.sendEmail(subject, finalBody);
	}
    }

    private String getCandidacyRefereeAccessLinkPrefix() {
	return Language.getLanguage().equals(Language.en) ? "/candidacies/phd-program/referee-form"
		: "/candidaturas/programa-doutoral/carta-referencia";
    }

    private boolean hasMinimumDocuments(final HttpServletRequest request) {
	boolean result = true;

	if (!RenderUtils.getViewState("candidacyBean.curriculumVitae").isValid()) {
	    addErrorMessage(request, "error.candidacyBean.curriculumVitae.not.valid");
	    result = false;
	}

	if (!RenderUtils.getViewState("candidacyBean.identificationDocument").isValid()) {
	    addErrorMessage(request, "error.candidacyBean.identificationDocument.not.valid");
	    result = false;
	}

	if (!RenderUtils.getViewState("candidacyBean.motivationLetter").isValid()) {
	    addErrorMessage(request, "error.candidacyBean.motivationLetter.not.valid");
	    result = false;
	}

	if (!RenderUtils.getViewState("candidacyBean.socialSecurityDocument").isValid()) {
	    addErrorMessage(request, "error.candidacyBean.socialSecurityDocument.not.valid");
	    result = false;
	}

	if (!RenderUtils.getViewState("candidacyBean.researchPlan").isValid()) {
	    addErrorMessage(request, "error.candidacyBean.researchPlan.not.valid");
	    result = false;
	}

	if (!RenderUtils.getViewState("candidacyBean.reportOrWorkDocument").isValid()) {
	    addErrorMessage(request, "error.candidacyBean.reportOrWorkDocument.not.valid");
	    result = false;
	}

	if (hasAnyHabilitationCertificateDocumentInvalid()) {
	    addErrorMessage(request, "error.candidacyBean.habilitationCertificateDocuments.not.valid");
	    result = false;
	}

	if (hasAnyPhdGuidingLetterInvalid()) {
	    addErrorMessage(request, "error.candidacyBean.phdGuidingLetters.not.valid");
	    result = false;
	}

	return result;
    }

    private boolean hasAnyHabilitationCertificateDocumentInvalid() {
	for (final IViewState viewState : getViewStatesWithPrefixId("candidacyBean.habilitationCertificateDocument")) {
	    if (!viewState.isValid()) {
		return true;
	    }
	}
	return false;
    }

    private boolean hasAnyPhdGuidingLetterInvalid() {
	for (final IViewState viewState : getViewStatesWithPrefixId("candidacyBean.phdGuidingLetter")) {
	    if (!viewState.isValid()) {
		return true;
	    }
	}
	return false;
    }

    private void clearDocumentsInformation(final PhdProgramCandidacyProcessBean bean) {
	bean.getCurriculumVitae().removeFile();
	RenderUtils.invalidateViewState("candidacyBean.curriculumVitae");

	bean.getIdentificationDocument().removeFile();
	RenderUtils.invalidateViewState("candidacyBean.identificationDocument");

	bean.getMotivationLetter().removeFile();
	RenderUtils.invalidateViewState("candidacyBean.motivationLetter");

	bean.getSocialSecurityDocument().removeFile();
	RenderUtils.invalidateViewState("candidacyBean.socialSecurityDocument");

	bean.getResearchPlan().removeFile();
	RenderUtils.invalidateViewState("candidacyBean.researchPlan");

	bean.getReportOrWorkDocument().removeFile();
	RenderUtils.invalidateViewState("candidacyBean.reportOrWorkDocument");

	bean.removeHabilitationCertificateDocumentFiles();
	invalidateHabilitationCertificateDocumentViewStates();

	bean.removePhdGuidingLetters();
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

    private List<Pair<Class<?>, Object>> buildActivities(PhdProgramCandidacyProcessBean bean) {
	final List<Pair<Class<?>, Object>> result = new ArrayList<Pair<Class<?>, Object>>();

	result.add(pair(AddGuidingsInformation.class, bean.getGuidings()));
	result.add(pair(AddQualifications.class, bean.getQualifications()));
	result.add(pair(AddCandidacyReferees.class, bean.getCandidacyReferees()));
	result.add(pair(UploadDocuments.class, bean.getAllDocuments()));

	return result;
    }

    private Pair<Class<?>, Object> pair(final Class<?> class1, final Object object) {
	return new Pair<Class<?>, Object>(class1, object);
    }

    public ActionForward addHabilitationCertificateDocument(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	final PhdProgramCandidacyProcessBean bean = getCandidacyBean();
	final PhdCandidacyDocumentUploadBean document = createDocumentBean(PhdIndividualProgramDocumentType.HABILITATION_CERTIFICATE_DOCUMENT);
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
	return viewCandidacy(mapping, request, (PhdProgramPublicCandidacyHashCode) PublicCandidacyHashCode
		.getPublicCandidacyCodeByHash(request.getParameter("hash")));
    }

    private ActionForward viewCandidacy(ActionMapping mapping, HttpServletRequest request,
	    final PhdProgramPublicCandidacyHashCode hashCode) {

	if (hashCode == null) {
	    // TODO: if prepareCreateCandidacy is different then send to that
	    // page
	    return mapping.findForward("createCandidacyStepOne");
	}

	final PhdProgramCandidacyProcessBean bean = new PhdProgramCandidacyProcessBean();
	bean.setCandidacyHashCode(hashCode);
	request.setAttribute("candidacyBean", bean);
	request.setAttribute("individualProgramProcess", hashCode.getIndividualProgramProcess());

	return mapping.findForward("viewCandidacy");
    }

    public ActionForward prepareEditPersonalInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final PhdProgramCandidacyProcessBean bean = getCandidacyBean();
	final Person person = bean.getCandidacyHashCode().getPerson();
	setIsEmployeeAttributeAndMessage(request, person);
	bean.setPersonBean(new PersonBean(person));
	request.setAttribute("candidacyBean", bean);

	return mapping.findForward("editPersonalInformation");
    }

    public ActionForward editPersonalInformationInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final PhdProgramCandidacyProcessBean bean = getCandidacyBean();
	setIsEmployeeAttributeAndMessage(request, bean.getPersonBean().getPerson());
	request.setAttribute("candidacyBean", bean);
	return mapping.findForward("editPersonalInformation");
    }

    public ActionForward editPersonalInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final PhdProgramCandidacyProcessBean bean = getCandidacyBean();
	setIsEmployeeAttributeAndMessage(request, bean.getPersonBean().getPerson());

	try {

	    ExecuteProcessActivity.run(bean.getCandidacyHashCode().getIndividualProgramProcess(), EditPersonalInformation.class,
		    bean.getPersonBean());

	} catch (final DomainException e) {
	    addErrorMessage(request, e.getKey(), e.getArgs());
	    setIsEmployeeAttributeAndMessage(request, bean.getPersonBean().getPerson());
	    request.setAttribute("candidacyBean", bean);
	    return mapping.findForward("editPersonalInformation");
	}

	return viewCandidacy(mapping, request, bean.getCandidacyHashCode());
    }
    // TODO: uncomment this line
    // @Override
    // protected void reloadRenderers() throws ServletException {
    // }
}
