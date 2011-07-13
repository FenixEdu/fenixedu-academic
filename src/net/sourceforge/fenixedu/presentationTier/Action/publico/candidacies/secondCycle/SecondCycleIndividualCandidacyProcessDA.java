package net.sourceforge.fenixedu.presentationTier.Action.publico.candidacies.secondCycle;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.PublicCandidacyHashCode;
import net.sourceforge.fenixedu.domain.candidacy.CandidacyInformationBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyPrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessDocumentUploadBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.DegreeOfficePublicCandidacyHashCode;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyDocumentFile;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle.SecondCycleCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle.SecondCycleIndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle.SecondCycleIndividualCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.publico.candidacies.IndividualCandidacyProcessPublicDA;
import net.sourceforge.fenixedu.presentationTier.formbeans.FenixActionForm;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.LocalDateTime;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/candidacies/caseHandlingAncientSecondCycleCandidacyIndividualProcess", module = "publico", formBeanClass = FenixActionForm.class)
@Forwards( { @Forward(name = "candidacy-types-information-intro", path = "candidacy.types.information.intro"),
	@Forward(name = "candidacy-types-information-intro-en", path = "candidacy.types.information.intro.en"),
	@Forward(name = "candidacy-process-intro", path = "candidacy.process.intro"),
	@Forward(name = "candidacy-process-intro-en", path = "candidacy.process.intro.en"),
	@Forward(name = "candidacy-national-admission-test", path = "candidacy.national.admission.test"),
	@Forward(name = "candidacy-national-admission-test-en", path = "candidacy.national.admission.test.en"),
	@Forward(name = "begin-candidacy-process-intro", path = "second.cycle.candidacy.process.intro"),
	@Forward(name = "begin-candidacy-process-intro-en", path = "second.cycle.candidacy.process.intro.en"),
	@Forward(name = "open-candidacy-processes-not-found", path = "individual.candidacy.not.found"),
	@Forward(name = "open-candidacy-process-closed", path = "candidacy.process.closed"),
	@Forward(name = "show-application-submission-conditions", path = "show.application.submission.conditions"),
	@Forward(name = "show-candidacy-creation-page", path = "second.cycle.candidacy.creation.page"),
	@Forward(name = "candidacy.continue.creation", path = "second.cycle.candidacy.continue.creation"),
	@Forward(name = "inform-submited-candidacy", path = "inform.submited.candidacy"),
	@Forward(name = "show-candadidacy-authentication-page", path = "show.candadidacy.authentication.page"),
	@Forward(name = "show-candidacy-details", path = "second.cycle.show.candidacy.details"),
	@Forward(name = "edit-candidacy", path = "second.cycle.edit.candidacy"),
	@Forward(name = "show-pre-creation-candidacy-form", path = "show.pre.creation.candidacy.form"),
	@Forward(name = "show-email-message-sent", path = "show.email.message.sent"),
	@Forward(name = "edit-candidacy-habilitations", path = "second.cycle.edit.candidacy.habilitations"),
	@Forward(name = "edit-candidacy-documents", path = "second.cycle.edit.candidacy.documents"),
	@Forward(name = "show-application-access-recovery-form", path = "show.application.access.recovery.form"),
	@Forward(name = "show-application-access-recovery-email-sent", path = "show.application.access.recovery.email.sent") })
public class SecondCycleIndividualCandidacyProcessDA extends IndividualCandidacyProcessPublicDA {
    private static final LocalDateTime start = new LocalDateTime(2009, 4, 28, 0, 0);
    private static final LocalDateTime end = new LocalDateTime(2009, 6, 30, 12, 00);

    @Override
    protected String getCandidacyNameKey() {
	return "title.application.name.secondCycle";
    }

    @Override
    public ActionForward viewCandidacy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	SecondCycleIndividualCandidacyProcess individualCandidacyProcess = (SecondCycleIndividualCandidacyProcess) request
		.getAttribute("individualCandidacyProcess");
	SecondCycleIndividualCandidacyProcessBean bean = new SecondCycleIndividualCandidacyProcessBean(individualCandidacyProcess);
	bean.setPersonBean(new PersonBean(individualCandidacyProcess.getPersonalDetails()));
	bean.setCandidacyInformationBean(new CandidacyInformationBean(individualCandidacyProcess.getCandidacy()));
	bean.setPrecedentDegreeInformation(new CandidacyPrecedentDegreeInformationBean(individualCandidacyProcess
		.getCandidacyPrecedentDegreeInformation()));

	request.setAttribute("individualCandidacyProcessBean", bean);
	return mapping.findForward("show-candidacy-details");
    }

    @Override
    protected Class<? extends CandidacyProcess> getParentProcessType() {
	return SecondCycleCandidacyProcess.class;
    }

    @Override
    protected void setStartInformation(ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    }

    @Override
    protected Class<? extends IndividualCandidacyProcess> getProcessType() {
	return SecondCycleIndividualCandidacyProcess.class;
    }

    public ActionForward prepareCandidacyCreation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	String hash = request.getParameter("hash");
	DegreeOfficePublicCandidacyHashCode candidacyHashCode = (DegreeOfficePublicCandidacyHashCode) PublicCandidacyHashCode
		.getPublicCandidacyCodeByHash(hash);

	if (candidacyHashCode == null) {
	    return mapping.findForward("open-candidacy-processes-not-found");
	}

	if (candidacyHashCode.getIndividualCandidacyProcess() != null
		&& candidacyHashCode.getIndividualCandidacyProcess().getCandidacyProcess().getClass() == getParentProcessType()) {
	    request.setAttribute("individualCandidacyProcess", candidacyHashCode.getIndividualCandidacyProcess());
	    return viewCandidacy(mapping, form, request, response);
	} else if (candidacyHashCode.getIndividualCandidacyProcess() != null
		&& candidacyHashCode.getIndividualCandidacyProcess().getCandidacyProcess().getClass() != getParentProcessType()) {
	    return mapping.findForward("open-candidacy-processes-not-found");
	}

	if (!isApplicationSubmissionPeriodValid()) {
	    return beginCandidacyProcessIntro(mapping, form, request, response);
	}

	CandidacyProcess candidacyProcess = getCurrentOpenParentProcess();
	if (candidacyProcess == null)
	    return mapping.findForward("open-candidacy-processes-not-found");

	SecondCycleIndividualCandidacyProcessBean bean = new SecondCycleIndividualCandidacyProcessBean();
	bean.setPrecedentDegreeInformation(new CandidacyPrecedentDegreeInformationBean());
	bean.setPersonBean(new PersonBean());
	bean.setCandidacyProcess(candidacyProcess);
	bean.setCandidacyInformationBean(new CandidacyInformationBean());
	bean.setPublicCandidacyHashCode(candidacyHashCode);

	request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);

	bean.getPersonBean().setEmail(candidacyHashCode.getEmail());

	return mapping.findForward("show-candidacy-creation-page");
    }

    public ActionForward continueCandidacyCreation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	SecondCycleIndividualCandidacyProcessBean bean = (SecondCycleIndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();
	IndividualCandidacyDocumentFile photoDocumentFile = createIndividualCandidacyDocumentFile(bean.getPhotoDocument(), bean
		.getPersonBean().getDocumentIdNumber());
	bean.getPhotoDocument().setDocumentFile(photoDocumentFile);
	request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());

	return mapping.findForward("candidacy.continue.creation");
    }

    public ActionForward backCandidacyCreation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	return mapping.findForward("show-candidacy-creation-page");
    }

    public ActionForward submitCandidacy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	try {

	    SecondCycleIndividualCandidacyProcessBean bean = (SecondCycleIndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();
	    bean.setInternalPersonCandidacy(Boolean.FALSE);

	    boolean isValid = hasInvalidViewState();
	    if (!isValid) {
		invalidateDocumentFileRelatedViewStates();
		request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
		return mapping.findForward("candidacy.continue.creation");
	    }

	    // if (!validateCaptcha(mapping, request)) {
	    // invalidateDocumentFileRelatedViewStates();
	    // request.setAttribute(getIndividualCandidacyProcessBeanName(),
	    // getIndividualCandidacyProcessBean());
	    // return mapping.findForward("candidacy.continue.creation");
	    // }

	    if (candidacyIndividualProcessExistsForThisEmail(bean.getPersonBean().getEmail())) {
		return beginCandidacyProcessIntro(mapping, form, request, response);
	    }

	    if (!isApplicationSubmissionPeriodValid()) {
		return beginCandidacyProcessIntro(mapping, form, request, response);
	    }

	    copyPrecedentBeanToCandidacyInformationBean(bean.getPrecedentDegreeInformation(), bean.getCandidacyInformationBean());
	    saveDocumentFiles(bean);

	    SecondCycleIndividualCandidacyProcess process = (SecondCycleIndividualCandidacyProcess) createNewProcess(bean);
	    sendEmailForApplicationSuccessfullySubmited(process, mapping, request);

	    request.setAttribute("process", process);
	    request.setAttribute("mappingPath", mapping.getPath());
	    request.setAttribute("individualCandidacyProcess", process);
	    setLinkFromProcess(mapping, request, process.getCandidacyHashCode());

	    return mapping.findForward("inform-submited-candidacy");
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	    e.printStackTrace();
	    request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	    return mapping.findForward("fill-candidacy-information");
	}
    }

    private void saveDocumentFiles(SecondCycleIndividualCandidacyProcessBean bean) throws IOException {
	String documentIdNumber = bean.getPersonBean().getDocumentIdNumber();

	if (bean.getDocumentIdentificationDocument() != null) {
	    IndividualCandidacyDocumentFile documentIdentificationDocumentFile = createIndividualCandidacyDocumentFile(bean
		    .getDocumentIdentificationDocument(), documentIdNumber);
	    bean.getDocumentIdentificationDocument().setDocumentFile(documentIdentificationDocumentFile);
	}

	if (bean.getFirstCycleAccessHabilitationDocument() != null) {
	    IndividualCandidacyDocumentFile firstCycleAccessHabilitationDocumentFile = createIndividualCandidacyDocumentFile(bean
		    .getFirstCycleAccessHabilitationDocument(), documentIdNumber);
	    bean.getFirstCycleAccessHabilitationDocument().setDocumentFile(firstCycleAccessHabilitationDocumentFile);
	}

	if (bean.getHabilitationCertificationDocument() != null) {
	    IndividualCandidacyDocumentFile habilitationCertficationDocument = createIndividualCandidacyDocumentFile(bean
		    .getHabilitationCertificationDocument(), documentIdNumber);
	    bean.getHabilitationCertificationDocument().setDocumentFile(habilitationCertficationDocument);
	}

	if (bean.getPaymentDocument() != null) {
	    IndividualCandidacyDocumentFile paymentDocumentFile = createIndividualCandidacyDocumentFile(
		    bean.getPaymentDocument(), documentIdNumber);
	    bean.getPaymentDocument().setDocumentFile(paymentDocumentFile);
	}

	if (bean.getVatCatCopyDocument() != null) {
	    IndividualCandidacyDocumentFile vatDocumentFile = createIndividualCandidacyDocumentFile(bean.getVatCatCopyDocument(),
		    documentIdNumber);
	    bean.getVatCatCopyDocument().setDocumentFile(vatDocumentFile);
	}

	for (CandidacyProcessDocumentUploadBean uploadBean : bean.getReportOrWorkDocumentList()) {
	    IndividualCandidacyDocumentFile documentFile = createIndividualCandidacyDocumentFile(uploadBean, documentIdNumber);
	    uploadBean.setDocumentFile(documentFile);
	}

	for (CandidacyProcessDocumentUploadBean uploadBean : bean.getHabilitationCertificateList()) {
	    IndividualCandidacyDocumentFile documentFile = createIndividualCandidacyDocumentFile(uploadBean, documentIdNumber);
	    uploadBean.setDocumentFile(documentFile);
	}
    }

    public ActionForward prepareEditCandidacyProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	return mapping.findForward("edit-candidacy");
    }

    public ActionForward editCandidacyProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	SecondCycleIndividualCandidacyProcessBean bean = (SecondCycleIndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();
	try {
	    copyPrecedentBeanToCandidacyInformationBean(bean.getPrecedentDegreeInformation(), bean.getCandidacyInformationBean());

	    if (!isApplicationSubmissionPeriodValid()) {
		return beginCandidacyProcessIntro(mapping, form, request, response);
	    }

	    executeActivity(bean.getIndividualCandidacyProcess(), "EditPublicCandidacyPersonalInformation",
		    getIndividualCandidacyProcessBean());
	} catch (final DomainException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	    request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	    return mapping.findForward("edit-candidacy");
	}

	request.setAttribute("individualCandidacyProcess", bean.getIndividualCandidacyProcess());
	return backToViewCandidacyInternal(mapping, form, request, response);
    }

    public ActionForward prepareEditCandidacyQualifications(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	return mapping.findForward("edit-candidacy-habilitations");
    }

    public ActionForward editCandidacyQualifications(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	SecondCycleIndividualCandidacyProcessBean bean = (SecondCycleIndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();
	try {
	    // boolean isValid = validateSecondCycleIndividualCandidacy(request,
	    // bean) && RenderUtils.getViewState().isValid();
	    boolean isValid = hasInvalidViewState();
	    if (!isValid) {
		request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
		return mapping.findForward("edit-candidacy-habilitations");
	    }

	    if (!isApplicationSubmissionPeriodValid()) {
		return beginCandidacyProcessIntro(mapping, form, request, response);
	    }

	    copyPrecedentBeanToCandidacyInformationBean(bean.getPrecedentDegreeInformation(), bean.getCandidacyInformationBean());

	    executeActivity(bean.getIndividualCandidacyProcess(), "EditPublicCandidacyHabilitations",
		    getIndividualCandidacyProcessBean());
	} catch (final DomainException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	    request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	    return mapping.findForward("edit-candidacy");
	}

	request.setAttribute("individualCandidacyProcess", bean.getIndividualCandidacyProcess());
	return backToViewCandidacyInternal(mapping, form, request, response);
    }

    public ActionForward addConcludedHabilitationsEntry(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	SecondCycleIndividualCandidacyProcessBean bean = (SecondCycleIndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();
	bean.addConcludedFormationBean();

	request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
	invalidateDocumentFileRelatedViewStates();

	return forwardTo(mapping, request);
    }

    public ActionForward removeConcludedHabilitationsEntry(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	SecondCycleIndividualCandidacyProcessBean bean = (SecondCycleIndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();
	Integer index = getIntegerFromRequest(request, "removeIndex");
	bean.removeFormationConcludedBean(index);

	request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
	invalidateDocumentFileRelatedViewStates();

	return forwardTo(mapping, request);
    }

    public ActionForward addHabilitationCertificateDocumentFileEntry(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	SecondCycleIndividualCandidacyProcessBean bean = (SecondCycleIndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();
	bean.addHabilitationCertificateDocument();

	request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
	invalidateDocumentFileRelatedViewStates();

	return forwardTo(mapping, request);
    }

    public ActionForward removeHabilitationCertificateDocumentFileEntry(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	SecondCycleIndividualCandidacyProcessBean bean = (SecondCycleIndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();
	Integer removeIndex = getRequestParameterAsInteger(request, "removeIndex");
	bean.removeHabilitationCertificateDocument(removeIndex);

	request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
	invalidateDocumentFileRelatedViewStates();

	return forwardTo(mapping, request);
    }

    public ActionForward addReportsOrWorksDocumentFileEntry(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	SecondCycleIndividualCandidacyProcessBean bean = (SecondCycleIndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();
	bean.addReportOrWorkDocument();

	request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
	invalidateDocumentFileRelatedViewStates();

	return forwardTo(mapping, request);
    }

    public ActionForward removeReportsOrWorksDocumentFileEntry(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	SecondCycleIndividualCandidacyProcessBean bean = (SecondCycleIndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();
	Integer removeIndex = getRequestParameterAsInteger(request, "removeIndex");

	bean.removeReportOrWorkDocument(removeIndex);

	request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
	invalidateDocumentFileRelatedViewStates();

	return forwardTo(mapping, request);
    }

    private ActionForward forwardTo(ActionMapping mapping, HttpServletRequest request) {
	if (getFromRequest(request, "userAction").equals("createCandidacy")) {
	    return mapping.findForward("candidacy.continue.creation");
	} else if (getFromRequest(request, "userAction").equals("editCandidacyQualifications")) {
	    return mapping.findForward("edit-candidacy-habilitations");
	}

	return null;
    }

    @Override
    protected LocalDateTime getApplicationDateEnd() {
	return end;
    }

    @Override
    protected LocalDateTime getApplicationDateStart() {
	return start;
    }

    @Override
    protected String getRootPortalCandidacyAccess() {
	return "/candidaturas/segundo_ciclo/acesso";
    }

    @Override
    protected String getRootPortalCandidacySubmission() {
	return "/candidaturas/segundo_ciclo/submissao";
    }

}
