package net.sourceforge.fenixedu.presentationTier.Action.publico.candidacies.over23;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.PublicCandidacyHashCode;
import net.sourceforge.fenixedu.domain.candidacy.CandidacyInformationBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessDocumentUploadBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.DegreeOfficePublicCandidacyHashCode;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyDocumentFile;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.over23.Over23CandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.over23.Over23IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.over23.Over23IndividualCandidacyProcessBean;
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

@Mapping(path = "/candidacies/caseHandlingAncientOver23CandidacyIndividualProcess", module = "publico", formBeanClass = FenixActionForm.class)
@Forwards( { @Forward(name = "candidacy-types-information-intro", path = "candidacy.types.information.intro"),
	@Forward(name = "candidacy-types-information-intro-en", path = "candidacy.types.information.intro.en"),
	@Forward(name = "candidacy-process-intro", path = "candidacy.process.intro"),
	@Forward(name = "candidacy-process-intro-en", path = "candidacy.process.intro.en"),
	@Forward(name = "candidacy-national-admission-test", path = "candidacy.national.admission.test"),
	@Forward(name = "candidacy-national-admission-test-en", path = "candidacy.national.admission.test.en"),
	@Forward(name = "begin-candidacy-process-intro", path = "over23.candidacy.process.intro"),
	@Forward(name = "begin-candidacy-process-intro-en", path = "over23.candidacy.process.intro.en"),
	@Forward(name = "open-candidacy-processes-not-found", path = "individual.candidacy.not.found"),
	@Forward(name = "open-candidacy-process-closed", path = "candidacy.process.closed"),
	@Forward(name = "show-application-submission-conditions", path = "show.application.submission.conditions"),
	@Forward(name = "show-candidacy-creation-page", path = "over23.candidacy.creation.page"),
	@Forward(name = "candidacy.continue.creation", path = "over23.candidacy.continue.creation"),
	@Forward(name = "inform-submited-candidacy", path = "inform.submited.candidacy"),
	@Forward(name = "show-candadidacy-authentication-page", path = "show.candadidacy.authentication.page"),
	@Forward(name = "show-candidacy-details", path = "over23.show.candidacy.details"),
	@Forward(name = "edit-candidacy", path = "over23.edit.candidacy"),
	@Forward(name = "show-pre-creation-candidacy-form", path = "show.pre.creation.candidacy.form"),
	@Forward(name = "show-email-message-sent", path = "show.email.message.sent"),
	@Forward(name = "edit-candidacy-habilitations", path = "over23.edit.candidacy.habilitations"),
	@Forward(name = "edit-candidacy-documents", path = "over23.edit.candidacy.documents"),
	@Forward(name = "show-application-access-recovery-form", path = "show.application.access.recovery.form"),
	@Forward(name = "show-application-access-recovery-email-sent", path = "show.application.access.recovery.email.sent") })
public class Over23IndividualCandidacyProcessDA extends IndividualCandidacyProcessPublicDA {

    private static final LocalDateTime start = new LocalDateTime(2009, 4, 29, 0, 0);
    private static final LocalDateTime end = new LocalDateTime(2009, 5, 29, 23, 59);

    @Override
    protected String getCandidacyNameKey() {
	return "title.application.name.over23";
    }

    @Override
    public ActionForward viewCandidacy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	Over23IndividualCandidacyProcess individualCandidacyProcess = (Over23IndividualCandidacyProcess) request
		.getAttribute("individualCandidacyProcess");
	IndividualCandidacyProcessBean bean = new Over23IndividualCandidacyProcessBean(individualCandidacyProcess);
	bean.setPersonBean(new PersonBean(individualCandidacyProcess.getPersonalDetails()));
	bean.setCandidacyInformationBean(new CandidacyInformationBean(individualCandidacyProcess.getCandidacy()));

	request.setAttribute("individualCandidacyProcessBean", bean);
	return mapping.findForward("show-candidacy-details");
    }

    @Override
    protected Class<? extends CandidacyProcess> getParentProcessType() {
	return Over23CandidacyProcess.class;
    }

    @Override
    protected void setStartInformation(ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	// TODO Auto-generated method stub
    }

    @Override
    protected Class<? extends IndividualCandidacyProcess> getProcessType() {
	return Over23IndividualCandidacyProcess.class;
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
	}

	if (!isApplicationSubmissionPeriodValid()) {
	    return beginCandidacyProcessIntro(mapping, form, request, response);
	}

	CandidacyProcess candidacyProcess = getCurrentOpenParentProcess();
	if (candidacyProcess == null)
	    return mapping.findForward("open-candidacy-processes-not-found");

	Over23IndividualCandidacyProcessBean bean = new Over23IndividualCandidacyProcessBean();
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
	IndividualCandidacyProcessBean bean = (IndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();
	IndividualCandidacyDocumentFile photoDocumentFile = createIndividualCandidacyDocumentFile(bean.getPhotoDocument(), bean
		.getPersonBean().getDocumentIdNumber());
	bean.getPhotoDocument().setDocumentFile(photoDocumentFile);
	request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
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

	    Over23IndividualCandidacyProcessBean bean = (Over23IndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();
	    bean.setInternalPersonCandidacy(Boolean.FALSE);

	    boolean isValid = validateOver23IndividualCandidacy(request, bean) && hasInvalidViewState();
	    if (!isValid) {
		invalidateDocumentFileRelatedViewStates();
		request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
		return mapping.findForward("candidacy.continue.creation");
	    }

	    if (!bean.getHonorAgreement()) {
		addActionMessage("error", request, "error.must.agree.on.declaration.of.honor");
		invalidateDocumentFileRelatedViewStates();
		request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
		return mapping.findForward("candidacy.continue.creation");
	    }

	    if (!validateCaptcha(mapping, request)) {
		invalidateDocumentFileRelatedViewStates();
		request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
		return mapping.findForward("candidacy.continue.creation");
	    }

	    /*
	     * 10/05/2009 - Since we step candidacy information form we must
	     * copy some fields
	     */
	    bean.copyInformationToCandidacyBean();

	    copyToInformationBeanOnePrecendentInstitution(bean);

	    if (candidacyIndividualProcessExistsForThisEmail(bean.getPersonBean().getEmail())) {
		return beginCandidacyProcessIntro(mapping, form, request, response);
	    }

	    if (!isApplicationSubmissionPeriodValid()) {
		return beginCandidacyProcessIntro(mapping, form, request, response);
	    }

	    saveDocumentFiles(bean);
	    Over23IndividualCandidacyProcess process = (Over23IndividualCandidacyProcess) createNewProcess(bean);
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

    private void copyToInformationBeanOnePrecendentInstitution(IndividualCandidacyProcessBean bean) {
	if (!bean.getFormationConcludedBeanList().isEmpty()) {
	    bean.getCandidacyInformationBean().setInstitution(bean.getFormationConcludedBeanList().get(0).getInstitutionUnit());
	    bean.getCandidacyInformationBean().setInstitutionName(
		    bean.getFormationConcludedBeanList().get(0).getInstitutionName());
	} else {
	    bean.getCandidacyInformationBean()
		    .setInstitution(bean.getFormationNonConcludedBeanList().get(0).getInstitutionUnit());
	    bean.getCandidacyInformationBean().setInstitutionName(
		    bean.getFormationNonConcludedBeanList().get(0).getInstitutionName());
	}
    }

    private boolean validateOver23IndividualCandidacy(HttpServletRequest request, Over23IndividualCandidacyProcessBean bean) {
	boolean isValid = true;

	if (bean.getSelectedDegrees().isEmpty()) {
	    addActionMessage("error", request, "error.formation.selectedDegrees.required");
	    isValid = false;
	}

	if (bean.getFormationConcludedBeanList().isEmpty() && bean.getFormationNonConcludedBeanList().isEmpty()) {
	    addActionMessage("error", request, "error.formation.required");
	    return false;
	}

	return isValid;
    }

    public ActionForward prepareEditCandidacyProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	return mapping.findForward("edit-candidacy");
    }

    public ActionForward editCandidacyProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	IndividualCandidacyProcessBean bean = (IndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();
	try {
	    /*
	     * 10/05/2009 - Since we step candidacy information form we must
	     * copy some fields
	     */
	    bean.copyInformationToCandidacyBean();

	    copyToInformationBeanOnePrecendentInstitution(bean);

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

    public ActionForward prepareEditCandidacyHabilitations(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	return mapping.findForward("edit-candidacy-habilitations");
    }

    public ActionForward editCandidacyHabilitations(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	Over23IndividualCandidacyProcessBean bean = (Over23IndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();
	try {
	    boolean isValid = validateOver23IndividualCandidacy(request, bean) && hasInvalidViewState();
	    if (!isValid) {
		request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
		return mapping.findForward("edit-candidacy-habilitations");
	    }

	    if (!bean.getFormationConcludedBeanList().isEmpty()) {
		bean.getCandidacyInformationBean().setInstitution(
			bean.getFormationConcludedBeanList().get(0).getInstitutionUnit());
	    } else {
		bean.getCandidacyInformationBean().setInstitution(
			bean.getFormationNonConcludedBeanList().get(0).getInstitutionUnit());
	    }

	    if (!isApplicationSubmissionPeriodValid()) {
		return beginCandidacyProcessIntro(mapping, form, request, response);
	    }

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
	IndividualCandidacyProcessBean bean = (IndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();
	bean.addConcludedFormationBean();

	request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
	invalidateDocumentFileRelatedViewStates();

	return forwardTo(mapping, request);
    }

    public ActionForward addNonConcludedHabilitationsEntry(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	IndividualCandidacyProcessBean bean = (IndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();
	bean.addNonConcludedFormationBean();

	request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
	invalidateDocumentFileRelatedViewStates();

	return forwardTo(mapping, request);
    }

    public ActionForward removeConcludedHabilitationsEntry(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	IndividualCandidacyProcessBean bean = (IndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();
	Integer index = getIntegerFromRequest(request, "removeIndex");
	bean.removeFormationConcludedBean(index);

	request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
	invalidateDocumentFileRelatedViewStates();

	return forwardTo(mapping, request);
    }

    public ActionForward removeNonConcludedHabilitationsEntry(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse reponse) {
	IndividualCandidacyProcessBean bean = (IndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();
	Integer index = getIntegerFromRequest(request, "removeIndex");
	bean.removeFormationNonConcludedBean(index);

	request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
	invalidateDocumentFileRelatedViewStates();

	return forwardTo(mapping, request);
    }

    public ActionForward addSelectedDegreesEntry(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	Over23IndividualCandidacyProcessBean bean = (Over23IndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();

	if (bean.hasDegreeToAdd() && !bean.containsDegree(bean.getDegreeToAdd())) {
	    bean.addDegree(bean.getDegreeToAdd());
	    bean.removeDegreeToAdd();
	}

	request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
	invalidateDocumentFileRelatedViewStates();

	return forwardTo(mapping, request);
    }

    public ActionForward removeSelectedDegreesEntry(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	Over23IndividualCandidacyProcessBean bean = (Over23IndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();
	Integer index = getIntegerFromRequest(request, "removeIndex");
	bean.removeDegree(bean.getSelectedDegrees().get(index));

	request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
	invalidateDocumentFileRelatedViewStates();

	return forwardTo(mapping, request);
    }

    public ActionForward addHabilitationCertificateDocumentFileEntry(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	Over23IndividualCandidacyProcessBean bean = (Over23IndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();
	bean.addHabilitationCertificateDocument();

	request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
	invalidateDocumentFileRelatedViewStates();

	return forwardTo(mapping, request);
    }

    public ActionForward removeHabilitationCertificateDocumentFileEntry(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	Over23IndividualCandidacyProcessBean bean = (Over23IndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();
	Integer removeIndex = getRequestParameterAsInteger(request, "removeIndex");
	bean.removeHabilitationCertificateDocument(removeIndex);

	request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
	invalidateDocumentFileRelatedViewStates();

	return forwardTo(mapping, request);
    }

    public ActionForward addReportsOrWorksDocumentFileEntry(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	Over23IndividualCandidacyProcessBean bean = (Over23IndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();
	bean.addReportOrWorkDocument();

	request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
	invalidateDocumentFileRelatedViewStates();

	return forwardTo(mapping, request);
    }

    public ActionForward removeReportsOrWorksDocumentFileEntry(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	Over23IndividualCandidacyProcessBean bean = (Over23IndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();
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

    private void saveDocumentFiles(Over23IndividualCandidacyProcessBean bean) throws IOException {
	String documentIdNumber = bean.getPersonBean().getDocumentIdNumber();

	if (bean.getCurriculumVitaeDocument() != null) {
	    IndividualCandidacyDocumentFile curriculumVitaeDocumentFile = createIndividualCandidacyDocumentFile(bean
		    .getCurriculumVitaeDocument(), documentIdNumber);
	    bean.getCurriculumVitaeDocument().setDocumentFile(curriculumVitaeDocumentFile);
	}

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

	if (bean.getHandicapProofDocument() != null) {
	    IndividualCandidacyDocumentFile handicapProofDocumentFile = createIndividualCandidacyDocumentFile(bean
		    .getHandicapProofDocument(), documentIdNumber);
	    bean.getHandicapProofDocument().setDocumentFile(handicapProofDocumentFile);
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
	return "/candidaturas/lic/vinte_tres_anos/acesso";
    }

    @Override
    protected String getRootPortalCandidacySubmission() {
	return "/candidaturas/lic/vinte_tres_anos/submissao";
    }
}
