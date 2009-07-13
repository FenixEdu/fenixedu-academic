package net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy.publicProgram;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.caseHandling.CreateNewProcess;
import net.sourceforge.fenixedu.applicationTier.Servico.caseHandling.ExecuteProcessActivity;
import net.sourceforge.fenixedu.applicationTier.Servico.fileManager.UploadOwnPhoto;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.dataTransferObject.person.PhotographUploadBean;
import net.sourceforge.fenixedu.dataTransferObject.person.PhotographUploadBean.UnableToProcessTheImage;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PublicCandidacyHashCode;
import net.sourceforge.fenixedu.domain.Qualification;
import net.sourceforge.fenixedu.domain.QualificationBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramCollaborationType;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramDocumentType;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessBean;
import net.sourceforge.fenixedu.domain.phd.PhdProgramCandidacyProcessState;
import net.sourceforge.fenixedu.domain.phd.PhdProgramGuiding;
import net.sourceforge.fenixedu.domain.phd.PhdProgramGuidingBean;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.AddCandidacyReferees;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.AddGuidingsInformation;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.AddQualification;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.AddQualifications;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.DeleteGuiding;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.DeleteQualification;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.EditIndividualProcessInformation;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.EditPersonalInformation;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.UploadDocuments;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.ValidatedByCandidate;
import net.sourceforge.fenixedu.domain.phd.PhdProgramGuidingBean.PhdProgramGuidingType;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyDocumentUploadBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyPeriod;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyReferee;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyRefereeBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyRefereeLetter;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyRefereeLetterBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramPublicCandidacyHashCode;
import net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy.academicAdminOffice.PhdProgramCandidacyProcessDA;
import net.sourceforge.fenixedu.util.ContentType;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

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

@Forward(name = "showCandidacySuccess", path = "phdProgram.showCandidacySuccess"),

@Forward(name = "viewCandidacy", path = "phdProgram.viewCandidacy"),

@Forward(name = "editPersonalInformation", path = "phdProgram.editPersonalInformation"),

@Forward(name = "uploadCandidacyDocuments", path = "phdProgram.uploadCandidacyDocuments"),

@Forward(name = "editPhdIndividualProgramProcessInformation", path = "phdProgram.editPhdIndividualProgramProcessInformation"),

@Forward(name = "editCandidacyGuidings", path = "phdProgram.editCandidacyGuidings"),

@Forward(name = "editQualifications", path = "phdProgram.editQualifications"),

@Forward(name = "createRefereeLetter", path = "phdProgram.createRefereeLetter"),

@Forward(name = "createRefereeLetterSuccess", path = "phdProgram.createRefereeLetterSuccess"),

@Forward(name = "editCandidacyReferees", path = "phdProgram.editCandidacyReferees"),

@Forward(name = "uploadPhoto", path = "phdProgram.uploadPhoto"),

@Forward(name = "out.of.candidacy.period", path = "phdProgram.outOfCandidacyPeriod"),

@Forward(name = "validateCandidacy", path = "phdProgram.validateCandidacy")

})
public class PublicPhdProgramCandidacyProcessDA extends PhdProgramCandidacyProcessDA {

    static private final List<String> DO_NOT_VALIDATE_CANDIDACY_PERIOD_IN_METHODS = Arrays.asList(

    "prepareCreateCandidacyIdentification",

    "prepareCandidacyIdentificationRecovery",

    "candidacyIdentificationRecoveryInvalid",

    "candidacyIdentificationRecovery",

    "showCandidacySuccess",

    "viewCandidacy",

    "backToViewCandidacy");

    static private final int MINIMUM_HABILITATIONS_AND_CERTIFICATES = 2;
    static private final int MINIMUM_CANDIDACY_REFEREES = 3;
    static private final int MAXIMUM_DAYS_TO_EDIT_CANDIDACY = 2;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	final PhdProgramCandidacyProcessBean bean = getCandidacyBean();
	if (bean != null && bean.hasCandidacyHashCode()) {
	    canEditCandidacy(request, bean.getCandidacyHashCode());
	}

	return filterDispatchMethod(bean, mapping, actionForm, request, response);
    }

    private ActionForward filterDispatchMethod(final PhdProgramCandidacyProcessBean bean, ActionMapping mapping,
	    ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {

	final PhdProgramPublicCandidacyHashCode hashCode = (bean != null ? bean.getCandidacyHashCode() : null);
	final String methodName = getMethodName(mapping, actionForm, request, response, mapping.getParameter());

	if (methodName == null || !DO_NOT_VALIDATE_CANDIDACY_PERIOD_IN_METHODS.contains(methodName)) {
	    if (isOutOfCandidacyPeriod(hashCode)) {
		request.setAttribute("candidacyPeriod", getPhdCandidacyPeriod(hashCode));
		return mapping.findForward("out.of.candidacy.period");
	    }
	}

	return super.execute(mapping, actionForm, request, response);
    }

    private boolean isOutOfCandidacyPeriod(final PhdProgramPublicCandidacyHashCode hashCode) {
	return !getPhdCandidacyPeriod(hashCode).contains(new DateTime());
    }

    private PhdCandidacyPeriod getPhdCandidacyPeriod(final PhdProgramPublicCandidacyHashCode hashCode) {
	final LocalDate localDate = (hashCode != null && hashCode.hasCandidacyProcess()) ? hashCode
		.getPhdProgramCandidacyProcess().getCandidacyDate() : new LocalDate();
	return PhdCandidacyPeriod.getCandidacyPeriod(localDate.toDateTimeAtStartOfDay());
    }

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
	final String subject = bundle.getString("message.phd.email.subject.send.link.to.submission");
	final String body = bundle.getString("message.phd.email.body.send.link.to.submission");
	hashCode.sendEmail(subject, String.format(body, hashCode.getValue()));
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
	candidacyHashCode.sendEmail(subject, String.format(body, candidacyHashCode.getValue()));
    }

    public ActionForward prepareCreateCandidacy(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	return createCandidacyStepOne(mapping, actionForm, request, response);
    }

    public ActionForward createCandidacyStepOne(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final PhdProgramPublicCandidacyHashCode hashCode = (PhdProgramPublicCandidacyHashCode) PublicCandidacyHashCode
		.getPublicCandidacyCodeByHash(request.getParameter("hash"));

	if (hashCode == null) {
	    return mapping.findForward("createCandidacyStepOne");
	}

	if (hashCode.hasCandidacyProcess()) {
	    return viewCandidacy(mapping, request, hashCode);
	}

	final PhdProgramCandidacyProcessBean bean = new PhdProgramCandidacyProcessBean();
	bean.setPersonBean(new PersonBean());
	bean.getPersonBean().setPhotoAvailable(true);
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
	request.setAttribute("candidacyBean", getCandidacyBean());
	RenderUtils.invalidateViewState();
	return mapping.findForward("createCandidacyStepOne");
    }

    public ActionForward createCandidacyStepTwo(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final PhdProgramCandidacyProcessBean bean = getCandidacyBean();

	final PersonBean personBean = bean.getPersonBean();
	final Person person = Person.readByDocumentIdNumberAndIdDocumentType(personBean.getDocumentIdNumber(), personBean
		.getIdDocumentType());

	// check if person already exists
	if (person != null) {
	    if (bean.hasInstitutionId() && bean.getInstitutionId().equals(person.getIstUsername())) {
		if (person.getDateOfBirthYearMonthDay().equals(personBean.getDateOfBirth())) {
		    personBean.setPerson(person);
		} else {
		    // found person with diff date of birth
		    addErrorMessage(request, "error.phd.public.candidacy.fill.personal.information.and.institution.id");
		    return createCandidacyStepOneInvalid(mapping, actionForm, request, response);
		}
	    } else {
		addErrorMessage(request, "error.phd.public.candidacy.fill.personal.information.and.institution.id");
		return createCandidacyStepOneInvalid(mapping, actionForm, request, response);
	    }
	}

	bean.setExecutionYear(ExecutionYear.readCurrentExecutionYear());
	// TODO:IMPORTANT change when extending this candidacies to all types
	bean.setCollaborationType(PhdIndividualProgramCollaborationType.EPFL);
	bean.setState(PhdProgramCandidacyProcessState.PRE_CANDIDATE);
	// TODO: ---------------------------------------------------------------

	if (!bean.hasAnyGuiding()) {
	    bean.setGuidings(createGuidingsMinimumList());
	}
	if (!bean.hasAnyQualification()) {
	    bean.setQualifications(new ArrayList<QualificationBean>());
	}
	if (!bean.hasAnyCandidacyReferee()) {
	    bean.setCandidacyReferees(createCandidacyRefereesMinimumList());
	}

	request.setAttribute("candidacyBean", bean);
	RenderUtils.invalidateViewState();

	return mapping.findForward("createCandidacyStepTwo");
    }

    private List<PhdProgramGuidingBean> createGuidingsMinimumList() {
	final List<PhdProgramGuidingBean> result = new ArrayList<PhdProgramGuidingBean>();

	final PhdProgramGuidingBean g1 = new PhdProgramGuidingBean();
	g1.setWorkLocation("IST");
	final PhdProgramGuidingBean g2 = new PhdProgramGuidingBean();
	// TODO: change this according to collaboration type acronym
	g2.setWorkLocation("EPFL");

	result.add(g1);
	result.add(g2);

	return result;
    }

    private List<PhdCandidacyRefereeBean> createCandidacyRefereesMinimumList() {
	final List<PhdCandidacyRefereeBean> result = new ArrayList<PhdCandidacyRefereeBean>(MINIMUM_CANDIDACY_REFEREES);
	result.add(new PhdCandidacyRefereeBean());
	result.add(new PhdCandidacyRefereeBean());
	result.add(new PhdCandidacyRefereeBean());
	return result;
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
	bean
		.setDissertationOrFinalWorkDocument(createDocumentBean(PhdIndividualProgramDocumentType.DISSERTATION_OR_FINAL_WORK_DOCUMENT));
	bean.setHabilitationCertificateDocuments(createHabilitationCertificateDocuments(bean));
	bean.setPhdGuidingLetters(createPhdGuidingLetters(bean));

	request.setAttribute("candidacyBean", bean);
	request.setAttribute("maximumDaysToEditCandidacy", MAXIMUM_DAYS_TO_EDIT_CANDIDACY);
	RenderUtils.invalidateViewState();

	return mapping.findForward("createCandidacyStepThree");
    }

    private PhdCandidacyDocumentUploadBean createDocumentBean(final PhdIndividualProgramDocumentType type) {
	final PhdCandidacyDocumentUploadBean bean = new PhdCandidacyDocumentUploadBean();
	bean.setType(type);
	return bean;
    }

    private List<PhdCandidacyDocumentUploadBean> createHabilitationCertificateDocuments(final PhdProgramCandidacyProcessBean bean) {
	final List<PhdCandidacyDocumentUploadBean> result = new ArrayList<PhdCandidacyDocumentUploadBean>(bean
		.getQualifications().size());
	if (bean.hasAnyQualification()) {
	    bean.sortQualificationsByAttendedEnd();
	    for (final QualificationBean qualification : bean.getQualifications()) {
		final PhdCandidacyDocumentUploadBean uploadBean = createDocumentBean(PhdIndividualProgramDocumentType.HABILITATION_CERTIFICATE_DOCUMENT);
		uploadBean.setRemarks(qualification.getType().getLocalizedName());
		result.add(uploadBean);
	    }
	}
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
	request.setAttribute("maximumDaysToEditCandidacy", MAXIMUM_DAYS_TO_EDIT_CANDIDACY);
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

	    if (candidacyRefereesEmailsAreInvalid(bean)) {
		addErrorMessage(request, "error.candidacyBean.invalid.referee.emails");
		clearDocumentsInformation(bean);
		return createCandidacyStepThreeInvalid(mapping, form, request, response);
	    }

	    final PhdIndividualProgramProcess process = (PhdIndividualProgramProcess) CreateNewProcess.run(
		    PhdIndividualProgramProcess.class, bean, buildActivities(bean));

	    sendApplicationSuccessfullySubmitedEmail(bean.getCandidacyHashCode(), request);
	    sendCandidacyRefereesEmail(process, request);

	} catch (final DomainException e) {
	    addErrorMessage(request, e.getKey(), e.getArgs());
	    bean.clearPerson();
	    clearDocumentsInformation(bean);
	    return createCandidacyStepThreeInvalid(mapping, form, request, response);
	}

	return redirect("/candidacies/phdProgramCandidacyProcess.do?method=showCandidacySuccess", request);
    }

    public ActionForward showCandidacySuccess(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	// TODO: remove if validate button created
	request.setAttribute("maximumDaysToEditCandidacy", MAXIMUM_DAYS_TO_EDIT_CANDIDACY);
	return mapping.findForward("showCandidacySuccess");
    }

    private boolean candidacyRefereesEmailsAreInvalid(final PhdProgramCandidacyProcessBean bean) {
	final Set<String> emails = new HashSet<String>(bean.getCandidacyReferees().size() + 1);
	emails.add(bean.getCandidacyHashCode().getEmail());

	for (final PhdCandidacyRefereeBean refereeBean : bean.getCandidacyReferees()) {
	    if (emails.contains(refereeBean.getEmail())) {
		return true;
	    }
	    emails.add(refereeBean.getEmail());
	}

	return false;
    }

    private void sendApplicationSuccessfullySubmitedEmail(final PhdProgramPublicCandidacyHashCode hashCode,
	    final HttpServletRequest request) {

	// TODO: if candidacy period exists, then change body message to send
	// candidacy limit end date

	final ResourceBundle bundle = ResourceBundle.getBundle("resources.PhdResources", Language.getLocale());
	final String subject = bundle.getString("message.phd.email.subject.application.submited");
	final String body = bundle.getString("message.phd.email.body.application.submited");
	hashCode.sendEmail(subject, String.format(body, hashCode.getPhdProgramCandidacyProcess().getProcessNumber(), hashCode
		.getValue()));
    }

    private void sendCandidacyRefereesEmail(final PhdIndividualProgramProcess process, final HttpServletRequest request) {
	final ResourceBundle data = ResourceBundle.getBundle("resources.PhdResources", Language.getLocale());
	final String subject = String.format(data.getString("message.phd.email.subject.referee"), process.getPerson().getName());
	final String body = data.getString("message.phd.email.body.referee");

	for (final PhdCandidacyReferee referee : process.getPhdCandidacyReferees()) {
	    referee.sendEmail(subject, String.format(body, referee.getValue()));
	}
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

	if (!RenderUtils.getViewState("candidacyBean.dissertationOrFinalWorkDocument").isValid()) {
	    addErrorMessage(request, "error.candidacyBean.dissertationOrFinalWorkDocument.not.valid");
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

	bean.getDissertationOrFinalWorkDocument().removeFile();
	RenderUtils.invalidateViewState("candidacyBean.dissertationOrFinalWorkDocument");

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

	if (hashCode == null || !hashCode.hasCandidacyProcess()) {
	    return mapping.findForward("createCandidacyStepOne");
	}

	final PhdProgramCandidacyProcessBean bean = new PhdProgramCandidacyProcessBean();
	bean.setCandidacyHashCode(hashCode);
	request.setAttribute("candidacyBean", bean);
	request.setAttribute("individualProgramProcess", hashCode.getIndividualProgramProcess());
	canEditCandidacy(request, bean.getCandidacyHashCode());
	canEditPersonalInformation(request, hashCode.getPerson());

	request.setAttribute("candidacyPeriod", getPhdCandidacyPeriod(hashCode));
	validateProcess(request, hashCode.getIndividualProgramProcess());

	return mapping.findForward("viewCandidacy");
    }

    public ActionForward backToViewCandidacy(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	return viewCandidacy(mapping, request, getCandidacyBean().getCandidacyHashCode());
    }

    public ActionForward prepareEditPersonalInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final PhdProgramCandidacyProcessBean bean = getCandidacyBean();
	final Person person = bean.getCandidacyHashCode().getPerson();
	canEditPersonalInformation(request, person);
	bean.setPersonBean(new PersonBean(person));
	request.setAttribute("candidacyBean", bean);

	return mapping.findForward("editPersonalInformation");
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
	canEditPersonalInformation(request, bean.getPersonBean().getPerson());

	try {

	    ExecuteProcessActivity.run(bean.getCandidacyHashCode().getIndividualProgramProcess(), EditPersonalInformation.class,
		    bean.getPersonBean());

	} catch (final DomainException e) {
	    addErrorMessage(request, e.getKey(), e.getArgs());
	    request.setAttribute("candidacyBean", bean);
	    return mapping.findForward("editPersonalInformation");
	}

	return viewCandidacy(mapping, request, bean.getCandidacyHashCode());
    }

    private void canEditPersonalInformation(final HttpServletRequest request, final Person person) {
	if (person.hasRole(RoleType.EMPLOYEE)) {
	    request.setAttribute("canEditPersonalInformation", false);
	    addWarningMessage(request, "message.employee.data.must.be.updated.in.human.resources.section");
	} else if (person.hasAnyPersonRoles() || person.hasUser() || person.hasStudent()) {
	    request.setAttribute("canEditPersonalInformation", false);
	    addWarningMessage(request, "message.existing.person.data.must.be.updated.in.academic.office");
	} else {
	    request.setAttribute("canEditPersonalInformation", true);
	}
    }

    public ActionForward prepareUploadDocuments(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final PhdProgramCandidacyProcessBean bean = getCandidacyBean();
	RenderUtils.invalidateViewState();

	request.setAttribute("candidacyBean", bean);
	request.setAttribute("candidacyProcessDocuments", bean.getCandidacyHashCode().getIndividualProgramProcess()
		.getCandidacyProcessDocuments());

	final PhdCandidacyDocumentUploadBean uploadBean = new PhdCandidacyDocumentUploadBean();
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
	return mapping.findForward("uploadCandidacyDocuments");
    }

    @Override
    public ActionForward uploadDocuments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	if (!RenderUtils.getViewState("documentByType").isValid()) {
	    return uploadDocumentsInvalid(mapping, form, request, response);
	}

	final PhdCandidacyDocumentUploadBean uploadBean = (PhdCandidacyDocumentUploadBean) getRenderedObject("documentByType");

	if (!uploadBean.hasAnyInformation()) {
	    addErrorMessage(request, "message.no.documents.to.upload");
	    return uploadDocumentsInvalid(mapping, form, request, response);

	}
	try {
	    ExecuteProcessActivity.run(uploadBean.getIndividualProgramProcess(), UploadDocuments.class, Collections
		    .singletonList(uploadBean));
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

    public ActionForward editPhdIndividualProgramProcessInformationInvalid(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("candidacyBean", getCandidacyBean());
	request.setAttribute("individualProcessBean", getRenderedObject("individualProcessBean"));

	return mapping.findForward("editPhdIndividualProgramProcessInformation");
    }

    public ActionForward editPhdIndividualProgramProcessInformation(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	final PhdIndividualProgramProcessBean bean = (PhdIndividualProgramProcessBean) getRenderedObject("individualProcessBean");
	try {
	    ExecuteProcessActivity.run(bean.getIndividualProgramProcess(), EditIndividualProcessInformation.class, bean);
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
	final PhdProgramCandidacyProcessBean bean = getCandidacyBean();
	request.setAttribute("candidacyBean", bean);

	if (!bean.getCandidacyHashCode().getIndividualProgramProcess().hasAnyGuidings()) {
	    bean.setGuidings(createGuidingsMinimumList());
	} else {
	    bean.setGuidings(null);
	}

	return mapping.findForward("editCandidacyGuidings");
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

	if (!bean.hasAnyGuiding()) {
	    bean.setGuidings(new ArrayList<PhdProgramGuidingBean>());
	}

	final PhdProgramGuidingBean guiding = new PhdProgramGuidingBean();
	guiding.setGuidingType(PhdProgramGuidingType.EXTERNAL);
	bean.addGuiding(guiding);

	return mapping.findForward("editCandidacyGuidings");
    }

    public ActionForward removeGuidingFromCreationList(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final PhdProgramCandidacyProcessBean bean = getCandidacyBean();
	bean.removeGuiding(getIntegerFromRequest(request, "removeIndex").intValue());

	request.setAttribute("candidacyBean", bean);
	RenderUtils.invalidateViewState();

	return mapping.findForward("editCandidacyGuidings");
    }

    public ActionForward addGuidingToExistingCandidacy(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final PhdProgramCandidacyProcessBean bean = getCandidacyBean();

	try {
	    ExecuteProcessActivity.run(bean.getCandidacyHashCode().getIndividualProgramProcess(), AddGuidingsInformation.class,
		    bean.getGuidings());
	    addSuccessMessage(request, "message.guiding.created.with.success");

	} catch (final DomainException e) {
	    addErrorMessage(request, e.getKey(), e.getArgs());
	    request.setAttribute("candidacyBean", bean);
	    return mapping.findForward("editCandidacyGuidings");
	}

	bean.setGuidings(null);
	return prepareEditCandidacyGuidings(mapping, actionForm, request, response);
    }

    public ActionForward removeGuidingFromExistingCandidacy(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	final String externalId = (String) getFromRequest(request, "removeIndex");
	final PhdProgramCandidacyProcessBean bean = getCandidacyBean();
	final PhdProgramGuiding guiding = getGuiding(bean.getCandidacyHashCode().getIndividualProgramProcess(), externalId);

	try {
	    ExecuteProcessActivity.run(getCandidacyBean().getCandidacyHashCode().getIndividualProgramProcess(),
		    DeleteGuiding.class, guiding);
	    addSuccessMessage(request, "message.guiding.deleted.with.success");

	} catch (final DomainException e) {
	    addErrorMessage(request, e.getKey(), e.getArgs());
	}

	return prepareEditCandidacyGuidings(mapping, actionForm, request, response);
    }

    private PhdProgramGuiding getGuiding(final PhdIndividualProgramProcess individualProgramProcess, final String externalId) {
	for (final PhdProgramGuiding guiding : individualProgramProcess.getGuidingsSet()) {
	    if (guiding.getExternalId().equals(externalId)) {
		return guiding;
	    }
	}
	return null;
    }

    public ActionForward prepareEditCandidacyReferees(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("candidacyBean", getCandidacyBean());
	return mapping.findForward("editCandidacyReferees");
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

	request.setAttribute("candidacyBean", getCandidacyBean());

	try {
	    ExecuteProcessActivity.run(getCandidacyBean().getCandidacyHashCode().getIndividualProgramProcess(),
		    AddCandidacyReferees.class, Collections.singletonList(getRenderedObject("refereeBean")));
	    addSuccessMessage(request, "message.qualification.information.create.success");

	} catch (final DomainException e) {
	    addErrorMessage(request, e.getKey(), e.getArgs());
	    request.setAttribute("refereeBean", getRenderedObject("refereeBean"));
	}

	return mapping.findForward("editCandidacyReferees");
    }

    public ActionForward sendCandidacyRefereeEmail(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final PhdProgramCandidacyProcessBean bean = getCandidacyBean();
	final PhdIndividualProgramProcess process = bean.getCandidacyHashCode().getIndividualProgramProcess();
	final Person person = process.getPerson();

	final ResourceBundle bundle = ResourceBundle.getBundle("resources.PhdResources", Language.getLocale());
	final String subject = String.format(bundle.getString("message.phd.email.subject.referee.form"), person.getName());
	final String body = bundle.getString("message.phd.email.body.referee.form");

	final PhdCandidacyReferee referee = getReferee(process, request);
	referee.sendEmail(subject, String.format(body, referee.getValue()));

	addSuccessMessage(request, "message.candidacy.referee.email.sent.with.success", referee.getName());
	request.setAttribute("candidacyBean", bean);
	return mapping.findForward("editCandidacyReferees");
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
	request.setAttribute("candidacyBean", getCandidacyBean());
	return mapping.findForward("editQualifications");
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

	request.setAttribute("candidacyBean", getCandidacyBean());

	try {
	    ExecuteProcessActivity.run(getCandidacyBean().getCandidacyHashCode().getIndividualProgramProcess(),
		    AddQualification.class, getRenderedObject("qualificationBean"));
	    addSuccessMessage(request, "message.qualification.information.create.success");

	} catch (final DomainException e) {
	    addErrorMessage(request, e.getKey(), e.getArgs());
	    request.setAttribute("qualificationBean", getRenderedObject("qualificationBean"));
	}

	return mapping.findForward("editQualifications");
    }

    public ActionForward removeQualificationFromExistingCandidacy(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	final PhdProgramCandidacyProcessBean bean = getCandidacyBean();
	request.setAttribute("candidacyBean", getCandidacyBean());

	final String externalId = (String) getFromRequest(request, "removeIndex");
	final Qualification qualification = getQualification(bean.getCandidacyHashCode().getIndividualProgramProcess(),
		externalId);

	try {
	    ExecuteProcessActivity.run(getCandidacyBean().getCandidacyHashCode().getIndividualProgramProcess(),
		    DeleteQualification.class, qualification);
	    addSuccessMessage(request, "message.qualification.information.delete.success");

	} catch (final DomainException e) {
	    addErrorMessage(request, e.getKey(), e.getArgs());
	}

	return mapping.findForward("editQualifications");
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

	final PhdCandidacyReferee hashCode = (PhdCandidacyReferee) PublicCandidacyHashCode.getPublicCandidacyCodeByHash(request
		.getParameter("hash"));

	if (hashCode == null) {
	    return mapping.findForward("createRefereeLetter");
	}

	final PhdCandidacyRefereeLetterBean bean;
	if (hashCode.hasLetter()) {
	    bean = new PhdCandidacyRefereeLetterBean(hashCode.getLetter());
	} else {
	    bean = new PhdCandidacyRefereeLetterBean();
	    bean.setCandidacyReferee(hashCode);
	    bean.setRefereeName(hashCode.getName());
	}

	request.setAttribute("createRefereeLetterBean", bean);
	return mapping.findForward("createRefereeLetter");
    }

    public ActionForward createRefereeLetterInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	final PhdCandidacyRefereeLetterBean bean = (PhdCandidacyRefereeLetterBean) getRenderedObject("createRefereeLetterBean");
	request.setAttribute("createRefereeLetterBean", bean);
	RenderUtils.invalidateViewState("createRefereeLetterBean.comments");
	bean.removeFile();
	return mapping.findForward("createRefereeLetter");
    }

    public ActionForward createRefereeLetter(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final PhdCandidacyRefereeLetterBean bean = (PhdCandidacyRefereeLetterBean) getRenderedObject("createRefereeLetterBean");

	if (hasAnyRefereeLetterViewStateInvalid()) {
	    return createRefereeLetterInvalid(mapping, actionForm, request, response);
	}

	try {
	    PhdCandidacyRefereeLetter.createOrEdit(bean);

	} catch (final DomainException e) {
	    addErrorMessage(request, e.getKey(), e.getArgs());
	    request.setAttribute("createRefereeLetterBean", bean);
	    return mapping.findForward("createRefereeLetter");
	}

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
	final PhotographUploadBean photo = (PhotographUploadBean) getRenderedObject("uploadPhotoBean");

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

    private void canEditCandidacy(final HttpServletRequest request, final PhdProgramPublicCandidacyHashCode hashCode) {
	Boolean value = Boolean.FALSE;
	if (hashCode.hasPhdProgramCandidacyProcess()) {
	    final LocalDate whenCreated = hashCode.getPhdProgramCandidacyProcess().getWhenCreated().toLocalDate();
	    value = !new LocalDate().isAfter(whenCreated.plusDays(MAXIMUM_DAYS_TO_EDIT_CANDIDACY));
	}
	request.setAttribute("canEditCandidacy", value || !hashCode.getIndividualProgramProcess().isValidatedByCandidate());
    }

    private boolean validateProcess(final HttpServletRequest request, final PhdIndividualProgramProcess process) {
	boolean result = true;

	if (!process.hasPhdProgramFocusArea()) {
	    addValidationMessage(request, "message.validation.missing.focus.area");
	    result &= false;
	}
	if (process.getPhdCandidacyReferees().size() < MINIMUM_CANDIDACY_REFEREES) {
	    addValidationMessage(request, "message.validation.missing.minimum.candidacy.referees", String
		    .valueOf(MINIMUM_CANDIDACY_REFEREES));
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
	    addValidationMessage(request, "message.validation.missing.qualification.documents", String.valueOf(process
		    .getQualifications().size()));
	    result &= false;
	}

	return result;
    }

    private void addValidationMessage(final HttpServletRequest request, final String key, final String... args) {
	addActionMessage("validation", request, key, args);
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
	
	if (!validateProcess(request, process)) {
	    request.setAttribute("candidacyBean", bean);
	    return mapping.findForward("validateCandidacy");
	}

	try {
	    ExecuteProcessActivity.run(process, ValidatedByCandidate.class, null);
	    addSuccessMessage(request, "message.validation.with.success");

	} catch (final DomainException e) {
	    addErrorMessage(request, e.getKey(), e.getArgs());
	    return prepareValidateCandidacy(mapping, actionForm, request, response);
	}

	return viewCandidacy(mapping, request, bean.getCandidacyHashCode());
    }

    @Override
    protected void reloadRenderers() throws ServletException {
    }
}
