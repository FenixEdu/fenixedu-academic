package net.sourceforge.fenixedu.presentationTier.Action.publico.candidacies;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.caseHandling.CreateNewProcess;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessDocumentUploadBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyDocumentFile;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.PublicCandidacyHashCode;
import net.sourceforge.fenixedu.domain.candidacyProcess.exceptions.HashCodeForEmailAndProcessAlreadyBounded;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.candidacy.IndividualCandidacyProcessDA;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.LocalDateTime;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.components.state.LifeCycleConstants;
import pt.ist.fenixWebFramework.renderers.plugin.RenderersRequestProcessorImpl;
import pt.utl.ist.fenix.tools.util.i18n.Language;

import com.octo.captcha.module.struts.CaptchaServicePlugin;
import com.octo.captcha.service.CaptchaServiceException;

public abstract class IndividualCandidacyProcessPublicDA extends IndividualCandidacyProcessDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	ResourceBundle bundle = ResourceBundle.getBundle("resources.CandidateResources", Language.getLocale());
	request.setAttribute("application.name", bundle.getString(getCandidacyNameKey()));
	request.setAttribute("mappingPath", mapping.getPath());
	request.setAttribute("endSubmissionDate", getFormattedApplicationSubmissionEndDate());
	request.setAttribute("isApplicationSubmissionPeriodValid", isApplicationSubmissionPeriodValid());

	setProcess(request);
	return super.execute(mapping, actionForm, request, response);
    }

    @SuppressWarnings("unchecked")
    protected CandidacyProcess getCurrentOpenParentProcess() {
	Set<CandidacyProcess> degreeChangeCandidacyProcesses = RootDomainObject.readAllDomainObjects(getParentProcessType());

	for (CandidacyProcess candidacyProcess : degreeChangeCandidacyProcesses) {
	    if (candidacyProcess.hasOpenCandidacyPeriod()) {
		return candidacyProcess;
	    }
	}

	return null;
    }

    @Override
    protected void setParentProcess(HttpServletRequest request) {
	CandidacyProcess parentProcess = getCurrentOpenParentProcess();
	request.setAttribute("parentProcess", parentProcess);
    }

    protected IndividualCandidacyProcess createNewProcess(IndividualCandidacyProcessBean bean) throws DomainException {
	return (IndividualCandidacyProcess) CreateNewProcess.run(getProcessType().getName(), bean);
    }

    public ActionForward firstCycleCandidacyIntro(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	if (isInEnglishLocale()) {
	    return mapping.findForward("candidacy-process-intro-en");
	}

	return mapping.findForward("candidacy-process-intro");
    }

    public ActionForward candidaciesTypesInformationIntro(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	if (isInEnglishLocale()) {
	    return mapping.findForward("candidacy-types-information-intro-en");
	}

	return mapping.findForward("candidacy-types-information-intro");
    }

    public ActionForward nationalAdmissionTestIntro(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	if (isInEnglishLocale()) {
	    return mapping.findForward("candidacy-national-admission-test-en");
	}

	return mapping.findForward("candidacy-national-admission-test");
    }

    public ActionForward beginCandidacyProcessIntro(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	if (isInEnglishLocale()) {
	    return mapping.findForward("begin-candidacy-process-intro-en");
	}

	return mapping.findForward("begin-candidacy-process-intro");
    }

    private boolean isInEnglishLocale() {
	Locale locale = Language.getLocale();
	return locale.getLanguage().equals(Locale.ENGLISH.getLanguage());
    }

    protected abstract String getCandidacyNameKey();

    public abstract ActionForward viewCandidacy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response);

    public ActionForward prepareEditCandidacyDocuments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	CandidacyProcessDocumentUploadBean bean = new CandidacyProcessDocumentUploadBean();
	bean.setIndividualCandidacyProcess(getIndividualCandidacyProcessBean().getIndividualCandidacyProcess());
	request.setAttribute("candidacyDocumentUploadBean", bean);
	return mapping.findForward("edit-candidacy-documents");
    }

    public ActionForward prepareAutheticationCandidacyRequest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("credentialsBean", new CandidacyCredentialsBean());
	request.setAttribute("mappingPath", mapping.getPath());

	return mapping.findForward("show-candadidacy-authentication-page");
    }

    public ActionForward authenticationCandidacyRequest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	CandidacyCredentialsBean crendentialsBean = (CandidacyCredentialsBean) getObjectFromViewState("credentialsBean");
	IndividualCandidacyProcess individualCandidacyProcess = IndividualCandidacyProcess.findIndividualCandidacyProcess(this
		.getProcessType(), crendentialsBean.getEmail(), crendentialsBean.getAccessHash());

	if (individualCandidacyProcess == null) {
	    return mapping.findForward("individual-candidacy-not-found");
	}

	request.setAttribute("individualCandidacyProcess", individualCandidacyProcess);
	return viewCandidacy(mapping, form, request, response);
    }

    public ActionForward editCandidacyDocuments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException, IOException {
	CandidacyProcessDocumentUploadBean uploadBean = (CandidacyProcessDocumentUploadBean) getObjectFromViewState("individualCandidacyProcessBean.document.file");
	try {
	    IndividualCandidacyDocumentFile documentFile = createIndividualCandidacyDocumentFile(uploadBean, uploadBean
		    .getIndividualCandidacyProcess().getPersonalDetails().getDocumentIdNumber());
	    uploadBean.setDocumentFile(documentFile);

	    executeActivity(uploadBean.getIndividualCandidacyProcess(), "EditPublicCandidacyDocumentFile", uploadBean);
	    request.setAttribute("individualCandidacyProcess", uploadBean.getIndividualCandidacyProcess());
	    return backToViewCandidacyInternal(mapping, form, request, response);
	} catch (final DomainException e) {
	    invalidateDocumentFileRelatedViewStates();
	    CandidacyProcessDocumentUploadBean bean = new CandidacyProcessDocumentUploadBean();
	    bean.setIndividualCandidacyProcess(uploadBean.getIndividualCandidacyProcess());
	    request.setAttribute("candidacyDocumentUploadBean", bean);

	    addActionMessage(request, e.getMessage(), e.getArgs());
	    request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	    return mapping.findForward("edit-candidacy-documents");
	}
    }

    public ActionForward executeCreateCandidacyPersonalInformationInvalid(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	invalidateDocumentFileRelatedViewStates();

	return mapping.findForward("show-candidacy-creation-page");
    }

    public ActionForward executeEditCandidacyPersonalInformationInvalid(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	return mapping.findForward("edit-candidacy");
    }

    public ActionForward executeEditCandidacyQualificationsInvalid(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	return mapping.findForward("edit-candidacy-habilitations");
    }

    public ActionForward backToViewCandidacyInternal(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	IndividualCandidacyProcess individualCandidacyProcess = (IndividualCandidacyProcess) request
		.getAttribute("individualCandidacyProcess");
	return forward(request, getLinkFromPublicCandidacyHashCodeForInternalUse(mapping, request, individualCandidacyProcess
		.getCandidacyHashCode()));
    }

    public ActionForward backToViewCandidacy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	IndividualCandidacyProcess individualCandidacyProcess = getDomainObject(request, "individualCandidacyProcess");
	return forward(request, getLinkFromPublicCandidacyHashCodeForInternalUse(mapping, request, individualCandidacyProcess
		.getCandidacyHashCode()));
    }

    private ActionForward forward(HttpServletRequest request, String windowLocation) {
	final ActionForward actionForward = new ActionForward();
	String contextContextPath = request.getParameter(ContentInjectionRewriter.CONTEXT_ATTRIBUTE_NAME);
	windowLocation = windowLocation + "&" + ContentInjectionRewriter.CONTEXT_ATTRIBUTE_NAME + "=" + contextContextPath;
	actionForward.setName(windowLocation);
	actionForward.setPath(windowLocation);
	actionForward.setRedirect(true);
	return actionForward;
    }

    public ActionForward preparePreCreationOfCandidacy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	if (getCurrentOpenParentProcess() == null)
	    return mapping.findForward("open-candidacy-process-closed");

	CandidacyPreCreationBean bean = new CandidacyPreCreationBean();
	request.setAttribute("candidacyPreCreationBean", bean);

	return mapping.findForward("show-pre-creation-candidacy-form");
    }

    public ActionForward preparePreCreationOfCandidacyInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	if (getCurrentOpenParentProcess() == null)
	    return mapping.findForward("open-candidacy-process-closed");

	CandidacyPreCreationBean bean = (CandidacyPreCreationBean) getObjectFromViewState("candidacyPreCreationBean");
	request.setAttribute("candidacyPreCreationBean", bean);
	return mapping.findForward("show-pre-creation-candidacy-form");
    }

    private static final String SEND_LINK_TO_ACCESS_SUBMISSION_FORM_SUBJECT = "message.email.subject.send.link.to.submission.form";
    private static final String INFORM_APPLICATION_SUCCESS_SUBJECT = "message.email.subject.application.submited";
    private static final String RECOVER_LINK_SUBJECT = "message.email.subject.recovery.access";

    private static final String SEND_LINK_TO_ACCESS_SUBMISSION_FORM_BODY = "message.email.body.send.link.to.submission.form";
    private static final String INFORM_APPLICATION_SUCCESS_BODY = "message.email.body.application.submited";
    private static final String RECOVER_LINK_BODY = "message.email.body.recovery.access";

    private static final String LINK_HTTP_HOSTNAME = "const.public.applications.link.http.hostname";

    public ActionForward bindEmailWithHashCodeAndSendMailWithLink(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	try {
	    String email = (String) getObjectFromViewState("PublicAccessCandidacy.preCreationForm");
	    PublicCandidacyHashCode candidacyHashCode = PublicCandidacyHashCode.getUnusedOrCreateNewHashCode(getProcessType(),
		    getCurrentOpenParentProcess(), email);
	    sendEmailForApplicationSubmissionCandidacyForm(candidacyHashCode, mapping, request);

	    // String link =
	    // getFullLinkForSubmissionFromPublicCandidacyHashCodeForEmails(mapping,
	    // request, candidacyHashCode);
	    // request.setAttribute("link", link);

	    return mapping.findForward("show-email-message-sent");
	} catch (HashCodeForEmailAndProcessAlreadyBounded e) {
	    addActionMessage(request, "error.candidacy.hash.code.already.bounded");
	    return mapping.findForward("show-pre-creation-candidacy-form");
	}
    }

    private void sendEmailForApplicationSubmissionCandidacyForm(PublicCandidacyHashCode candidacyHashCode, ActionMapping mapping,
	    HttpServletRequest request) {
	ResourceBundle bundle = ResourceBundle.getBundle("resources.CandidateResources", Language.getLocale());
	String subject = bundle.getString(SEND_LINK_TO_ACCESS_SUBMISSION_FORM_SUBJECT);
	String body = bundle.getString(SEND_LINK_TO_ACCESS_SUBMISSION_FORM_BODY);
	String link = getFullLinkForSubmissionFromPublicCandidacyHashCodeForEmails(mapping, request, candidacyHashCode);

	body = String.format(body, new String[] { link });

	candidacyHashCode.sendEmail(subject, body);

    }

    protected void sendEmailForApplicationSuccessfullySubmited(IndividualCandidacyProcess process, ActionMapping mapping,
	    HttpServletRequest request) {
	PublicCandidacyHashCode candidacyHashCode = process.getCandidacyHashCode();
	ResourceBundle bundle = ResourceBundle.getBundle("resources.CandidateResources", Language.getLocale());
	String subject = bundle.getString(INFORM_APPLICATION_SUCCESS_SUBJECT);
	String body = bundle.getString(INFORM_APPLICATION_SUCCESS_BODY);
	String link = getFullLinkFromPublicCandidacyHashCodeForEmails(mapping, request, candidacyHashCode);

	body = String.format(body, new String[] { candidacyHashCode.getIndividualCandidacyProcess().getProcessCode(), link,
		getFormattedApplicationSubmissionEndDate() });

	candidacyHashCode.sendEmail(subject, body);
    }

    public ActionForward prepareApplicationAccessRecovery(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	CandidacyPreCreationBean bean = new CandidacyPreCreationBean();
	request.setAttribute("candidacyRecoveryForm", bean);

	return mapping.findForward("show-application-access-recovery-form");
    }

    public ActionForward recoverApplicationAccess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	String email = (String) getObjectFromViewState("PublicAccessCandidacy.recoveryAccessForm");
	PublicCandidacyHashCode candidacyHashCode = PublicCandidacyHashCode
		.getPublicCandidacyHashCodeByEmailAndCandidacyProcessType(email, getProcessType(), getCurrentOpenParentProcess());

	if (candidacyHashCode != null) {
	    ResourceBundle bundle = ResourceBundle.getBundle("resources.CandidateResources", Language.getLocale());
	    String subject = bundle.getString(RECOVER_LINK_SUBJECT);
	    String body = bundle.getString(RECOVER_LINK_BODY);
	    String link = getFullLinkFromPublicCandidacyHashCodeForEmails(mapping, request, candidacyHashCode);

	    body = String.format(body, new String[] { link, candidacyHashCode.getIndividualCandidacyProcess().getProcessCode() });

	    candidacyHashCode.sendEmail(subject, body);
	}

	return mapping.findForward("show-application-access-recovery-email-sent");
    }

    public ActionForward showApplicationSubmissionConditions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	String hash = request.getParameter("hash");
	PublicCandidacyHashCode candidacyHashCode = PublicCandidacyHashCode.getPublicCandidacyCodeByHash(hash);

	if (candidacyHashCode == null) {
	    return mapping.findForward("open-candidacy-processes-not-found");
	}

	if (candidacyHashCode.getIndividualCandidacyProcess() != null) {
	    request.setAttribute("individualCandidacyProcess", candidacyHashCode.getIndividualCandidacyProcess());
	    return viewCandidacy(mapping, form, request, response);
	}

	request.setAttribute("hash", hash);
	return mapping.findForward("show-application-submission-conditions");
    }

    protected abstract String getRootPortalCandidacyAccess();

    protected abstract String getRootPortalCandidacySubmission();

    protected String getLinkFromPublicCandidacyHashCodeForInternalUse(ActionMapping mapping, HttpServletRequest request,
	    PublicCandidacyHashCode hashCode) {
	return mapping.getPath() + ".do?method=prepareCandidacyCreation&hash=" + hashCode.getValue();
    }

    protected String getLinkFromPublicCandidacyHashCode(ActionMapping mapping, HttpServletRequest request,
	    PublicCandidacyHashCode hashCode) {
	return getRootPortalCandidacyAccess() + "?hash=" + hashCode.getValue() + "&locale=" + Language.getLocale().getLanguage();
    }

    protected String getFullLinkFromPublicCandidacyHashCodeForEmails(ActionMapping mapping, HttpServletRequest request,
	    PublicCandidacyHashCode hashCode) {
	ResourceBundle bundle = ResourceBundle.getBundle("resources.CandidateResources");
	String link = bundle.getString(LINK_HTTP_HOSTNAME);

	return link + request.getContextPath() + getLinkFromPublicCandidacyHashCode(mapping, request, hashCode);
    }

    protected String getLinkForSubmissionFromPublicCandidacyHashCode(ActionMapping mapping, HttpServletRequest request,
	    PublicCandidacyHashCode hashCode) {
	return getRootPortalCandidacySubmission() + "?hash=" + hashCode.getValue() + "&locale="
		+ Language.getLocale().getLanguage();
    }

    protected String getFullLinkForSubmissionFromPublicCandidacyHashCodeForEmails(ActionMapping mapping,
	    HttpServletRequest request, PublicCandidacyHashCode hashCode) {
	ResourceBundle bundle = ResourceBundle.getBundle("resources.CandidateResources");
	String link = bundle.getString(LINK_HTTP_HOSTNAME);

	return link + request.getContextPath() + getLinkForSubmissionFromPublicCandidacyHashCode(mapping, request, hashCode);
    }

    protected boolean candidacyIndividualProcessExistsForThisEmail(String email) {
	PublicCandidacyHashCode candidacyHashCode = PublicCandidacyHashCode
		.getPublicCandidacyHashCodeByEmailAndCandidacyProcessType(email, getProcessType(), getCurrentOpenParentProcess());
	return candidacyHashCode != null;
    }

    protected boolean validateCaptcha(ActionMapping mapping, HttpServletRequest request) {
	final String captchaId = request.getSession().getId();
	final String captchaResponse = request.getParameter("j_captcha_response");

	try {
	    if (!CaptchaServicePlugin.getInstance().getService().validateResponseForID(captchaId, captchaResponse)) {
		addActionMessage("captcha.error", request, "captcha.wrong.word");
		return false;
	    }
	    return true;
	} catch (CaptchaServiceException e) { // may be thrown if the id is not
	    // valid
	    e.printStackTrace();
	    addActionMessage("captcha.error", request, "captcha.wrong.word");
	    return false;
	}
    }

    protected void setLinkFromProcess(ActionMapping mapping, HttpServletRequest request, PublicCandidacyHashCode candidacyHashCode) {
	String link = request.getContextPath() + "/publico" + mapping.getPath()
		+ ".do?method=showApplicationSubmissionConditions&hash=" + candidacyHashCode.getValue();
	request.setAttribute("link", link);
    }

    protected boolean hasInvalidViewState() {
	List<IViewState> viewStates = (List<IViewState>) RenderersRequestProcessorImpl.getCurrentRequest().getAttribute(
		LifeCycleConstants.VIEWSTATE_PARAM_NAME);
	boolean valid = true;
	if (viewStates != null) {
	    for (IViewState state : viewStates) {
		valid &= state.isValid();
	    }
	}
	return valid;
    }

    public static class CandidacyCredentialsBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String email;
	private String accessHash;

	public String getEmail() {
	    return this.email;
	}

	public void setEmail(String email) {
	    this.email = email;
	}

	public String getAccessHash() {
	    return this.accessHash;
	}

	public void setAccessHash(String accessHash) {
	    this.accessHash = accessHash;
	}
    }

    public static class CandidacyPreCreationBean implements java.io.Serializable {
	/**
         * 
         */
	private static final long serialVersionUID = 1L;

	private String email;

	public String getEmail() {
	    return this.email;
	}

	public void setEmail(String email) {
	    this.email = email;
	}
    }

    protected abstract LocalDateTime getApplicationDateStart();

    protected abstract LocalDateTime getApplicationDateEnd();

    protected boolean isApplicationSubmissionPeriodValid() {
	LocalDateTime now = new LocalDateTime(System.currentTimeMillis());
	return now.isAfter(getApplicationDateStart()) && now.isBefore(getApplicationDateEnd());
    }

    protected String getFormattedApplicationSubmissionEndDate() {
	LocalDateTime end = getApplicationDateEnd();
	if (isInEnglishLocale()) {
	    return end.toString("dd', 'MMMM' of 'yyyy", Language.getLocale());
	}
	return end.toString("dd' de 'MMMM' de 'yyyy", Language.getLocale());
    }

    @Override
    protected void prepareInformationForBindPersonToCandidacyOperation(HttpServletRequest request,
	    IndividualCandidacyProcess process) {
	// TODO Auto-generated method stub

    }

}
