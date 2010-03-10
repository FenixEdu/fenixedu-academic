package net.sourceforge.fenixedu.presentationTier.Action.publico.candidacies.erasmus;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.PublicCandidacyHashCode;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.DegreeOfficePublicCandidacyHashCode;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusIndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusIndividualCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.exceptions.HashCodeForEmailAndProcessAlreadyBounded;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.presentationTier.Action.candidacy.erasmus.DegreeCourseInformationBean;
import net.sourceforge.fenixedu.presentationTier.Action.publico.candidacies.RefactoredIndividualCandidacyProcessPublicDA;
import net.sourceforge.fenixedu.presentationTier.formbeans.FenixActionForm;
import net.sourceforge.fenixedu.util.stork.Attribute;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.servlets.filters.I18NFilter;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/candidacies/caseHandlingErasmusCandidacyIndividualProcess", module = "publico", formBeanClass = FenixActionForm.class)
@Forwards( {
	@Forward(name = "candidacy-process-intro", path = "erasmus.candidacy.process.intro"),
	@Forward(name = "candidacy-submission-type", path = "erasmus.candidacy.submission.type"),
	@Forward(name = "show-pre-creation-candidacy-form", path = "erasmus.show.pre.creation.candidacy.form"),
	@Forward(name = "show-email-message-sent", path = "erasmus.show.email.message.sent"),
	@Forward(name = "show-application-submission-conditions", path = "erasmus.show.application.submission.conditions"),
	@Forward(name = "open-candidacy-processes-not-found", path = "individual.candidacy.not.found"),
	@Forward(name = "show-candidacy-creation-page", path = "erasmus.candidacy.creation.page"),
	@Forward(name = "candidacy-continue-creation", path = "erasmus.candidacy.continue.creation"),
	@Forward(name = "fill-degree-and-courses-information", path = "erasmus.fill.degree.and.courses.information"),
	@Forward(name = "accept-honour-declaration", path = "erasmus.fill.accept.honour.declaration"),
	@Forward(name = "inform-submited-candidacy", path = "erasmus.inform.submited.candidacy"),
	@Forward(name = "show-candidacy-details", path = "erasmus.show.candidacy.details"),
	@Forward(name = "edit-candidacy", path = "erasmus.edit.candidacy"),
	@Forward(name = "edit-candidacy-information", path = "erasmus.edit.candidacy.information"),
	@Forward(name = "edit-candidacy-degree-and-courses", path = "erasmus.edit.candidacy.degree.and.courses"),
	@Forward(name = "edit-candidacy-documents", path = "erasmus.edit.candidacy.documents"),
	@Forward(name = "edit-candidacy-degree-and-courses", path = "erasmus.edit.candidacy.degree.and.courses"),
	@Forward(name = "redirect-to-peps", path = "erasmus.redirect.to.peps"),
	@Forward(name = "show-application-submission-conditions-for-stork", path = "erasmus.show.application.submission.conditions.for.stork"),
	@Forward(name = "show-stork-error-page", path = "erasmus.show.stork.error.page") })
public class ErasmusIndividualCandidacyProcessPublicDA extends RefactoredIndividualCandidacyProcessPublicDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	I18NFilter.setLocale(request, request.getSession(true), Locale.ENGLISH);
	return super.execute(mapping, actionForm, request, response);
    }

    @Override
    protected String getCandidacyInformationLinkDefaultLanguage() {
	return "link.candidacy.information.default.erasmus";
    }

    @Override
    protected String getCandidacyInformationLinkEnglish() {
	return "link.candidacy.information.english.erasmus";
    }

    @Override
    protected String getCandidacyNameKey() {
	return "title.application.name.erasmus";
    }

    @Override
    public ActionForward viewCandidacy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	ErasmusIndividualCandidacyProcess individualCandidacyProcess = (ErasmusIndividualCandidacyProcess) request
		.getAttribute("individualCandidacyProcess");
	ErasmusIndividualCandidacyProcessBean bean = new ErasmusIndividualCandidacyProcessBean(individualCandidacyProcess);

	bean.setPersonBean(new PersonBean(individualCandidacyProcess.getPersonalDetails()));

	request.setAttribute("individualCandidacyProcessBean", bean);

	return mapping.findForward("show-candidacy-details");
    }

    @Override
    protected Class getParentProcessType() {
	return ErasmusCandidacyProcess.class;
    }

    @Override
    protected Class getProcessType() {
	return ErasmusIndividualCandidacyProcess.class;
    }

    public ActionForward intro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	return mapping.findForward("candidacy-process-intro");
    }

    public ActionForward chooseSubmissionType(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return mapping.findForward("candidacy-submission-type");
    }

    public ActionForward prepareCandidacyCreation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	ActionForward actionForwardError = verifySubmissionPreconditions(mapping);
	if (actionForwardError != null)
	    return actionForwardError;

	CandidacyProcess candidacyProcess = getCurrentOpenParentProcess();

	String hash = request.getParameter("hash");
	DegreeOfficePublicCandidacyHashCode candidacyHashCode = (DegreeOfficePublicCandidacyHashCode) PublicCandidacyHashCode
		.getPublicCandidacyCodeByHash(hash);

	if (candidacyHashCode == null) {
	    return mapping.findForward("open-candidacy-processes-not-found");
	}

	if (candidacyHashCode.getIndividualCandidacyProcess() != null
		&& candidacyHashCode.getIndividualCandidacyProcess().getCandidacyProcess() == candidacyProcess) {
	    request.setAttribute("individualCandidacyProcess", candidacyHashCode.getIndividualCandidacyProcess());
	    return viewCandidacy(mapping, form, request, response);
	} else if (candidacyHashCode.getIndividualCandidacyProcess() != null
		&& candidacyHashCode.getIndividualCandidacyProcess().getCandidacyProcess() != candidacyProcess) {
	    return mapping.findForward("open-candidacy-processes-not-found");
	}

	ErasmusIndividualCandidacyProcessBean bean = new ErasmusIndividualCandidacyProcessBean(candidacyProcess);
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
	request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());

	return mapping.findForward("candidacy-continue-creation");
    }

    public ActionForward fillDegreeInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	request.setAttribute("degreeCourseInformationBean", new DegreeCourseInformationBean(
		(ExecutionYear) getCurrentOpenParentProcess().getCandidacyExecutionInterval()));

	return mapping.findForward("fill-degree-and-courses-information");
    }

    private DegreeCourseInformationBean readDegreeCourseInformationBean(HttpServletRequest request) {
	return (DegreeCourseInformationBean) getRenderedObject("degree.course.information.bean");
    }

    public ActionForward chooseDegree(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

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
	request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());

	ErasmusIndividualCandidacyProcessBean bean = (ErasmusIndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();
	DegreeCourseInformationBean degreeCourseBean = readDegreeCourseInformationBean(request);

	if (degreeCourseBean.getChosenCourse() != null) {
	    bean.addCurricularCourse(degreeCourseBean.getChosenCourse());
	}

	request.setAttribute("degreeCourseInformationBean", readDegreeCourseInformationBean(request));

	RenderUtils.invalidateViewState();

	if ("editCandidacy".equals(request.getParameter("userAction"))) {
	    return mapping.findForward("edit-candidacy-degree-and-courses");
	}

	return mapping.findForward("fill-degree-and-courses-information");
    }

    public ActionForward removeCourse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());

	ErasmusIndividualCandidacyProcessBean bean = (ErasmusIndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();
	DegreeCourseInformationBean degreeCourseBean = readDegreeCourseInformationBean(request);

	CurricularCourse courseToRemove = getDomainObject(request, "removeCourseId");
	bean.removeCurricularCourse(courseToRemove);

	request.setAttribute("degreeCourseInformationBean", readDegreeCourseInformationBean(request));

	RenderUtils.invalidateViewState();

	if ("editCandidacy".equals(request.getParameter("userAction"))) {
	    return mapping.findForward("edit-candidacy-degree-and-courses");
	}

	return mapping.findForward("fill-degree-and-courses-information");
    }

    public ActionForward acceptHonourDeclaration(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());

	return mapping.findForward("accept-honour-declaration");
    }

    public ActionForward submitCandidacy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, FenixFilterException, FenixServiceException {
	try {
	    ActionForward actionForwardError = verifySubmissionPreconditions(mapping);
	    if (actionForwardError != null)
		return actionForwardError;

	    ErasmusIndividualCandidacyProcessBean bean = (ErasmusIndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();
	    bean.setInternalPersonCandidacy(Boolean.TRUE);

	    boolean isValid = hasInvalidViewState();
	    if (!isValid) {
		invalidateDocumentFileRelatedViewStates();
		request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
		return mapping.findForward("accept-honour-declaration");
	    }

	    if (candidacyIndividualProcessExistsForThisEmail(bean.getPersonBean().getEmail())) {
		return beginCandidacyProcessIntro(mapping, form, request, response);
	    }

	    if (!bean.getHonorAgreement()) {
		addActionMessage("error", request, "error.must.agree.on.declaration.of.honor");
		invalidateDocumentFileRelatedViewStates();
		request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
		return mapping.findForward("accept-honour-declaration");
	    }

	    ErasmusIndividualCandidacyProcess process = (ErasmusIndividualCandidacyProcess) createNewPublicProcess(bean);

	    request.setAttribute("process", process);
	    request.setAttribute("mappingPath", mapping.getPath());
	    request.setAttribute("individualCandidacyProcess", process);
	    request.setAttribute("endSubmissionDate", getFormattedApplicationSubmissionEndDate());

	    return mapping.findForward("inform-submited-candidacy");
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	    e.printStackTrace();
	    request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	    return mapping.findForward("accept-honour-declaration");
	}
    }

    public ActionForward editCandidacyProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	ErasmusIndividualCandidacyProcessBean bean = (ErasmusIndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();
	try {
	    ActionForward actionForwardError = verifySubmissionPreconditions(mapping);
	    if (actionForwardError != null)
		return actionForwardError;

	    if (!isApplicationSubmissionPeriodValid()) {
		return beginCandidacyProcessIntro(mapping, form, request, response);
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
	return mapping.findForward("redirect-to-peps");
    }

    public ActionForward returnFromPeps(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	String errorCode = (String) request.getParameter("errorCode");
	String errorMessage = (String) request.getParameter("errorMessage");
	String attrList = request.getParameter("attrList");

	if (!(errorCode == null || errorMessage == null || errorCode.length() <= 0 || errorMessage.length() <= 0)) {
	    return mapping.findForward("show-stork-error-page");
	}

	Map<String, Attribute> attributes = buildStorkAttributes(attrList);

	String name = getStorkName(attributes);
	YearMonthDay birthDate = getBirthDate(attributes);
	String email = getEmail(attributes);
	Country nationality = getNationality(attributes);

	DegreeOfficePublicCandidacyHashCode candidacyHashCode = null;
	try {
	    candidacyHashCode = DegreeOfficePublicCandidacyHashCode
		    .getUnusedOrCreateNewHashCodeAndSendEmailForApplicationSubmissionToCandidate(getProcessType(),
			    getCurrentOpenParentProcess(), email);
	} catch (HashCodeForEmailAndProcessAlreadyBounded e) {
	    addActionMessage(request, "error.candidacy.hash.code.already.bounded");
	    return mapping.findForward("show-pre-creation-candidacy-form");
	}

	ActionForward actionForwardError = verifySubmissionPreconditions(mapping);
	if (actionForwardError != null)
	    return actionForwardError;

	if (candidacyHashCode == null) {
	    return mapping.findForward("open-candidacy-processes-not-found");
	}

	CandidacyProcess candidacyProcess = getCurrentOpenParentProcess();

	if (candidacyHashCode.getIndividualCandidacyProcess() != null
		&& candidacyHashCode.getIndividualCandidacyProcess().getCandidacyProcess() == candidacyProcess) {
	    request.setAttribute("individualCandidacyProcess", candidacyHashCode.getIndividualCandidacyProcess());
	    return viewCandidacy(mapping, form, request, response);
	} else if (candidacyHashCode.getIndividualCandidacyProcess() != null
		&& candidacyHashCode.getIndividualCandidacyProcess().getCandidacyProcess() != candidacyProcess) {
	    return mapping.findForward("open-candidacy-processes-not-found");
	}

	ErasmusIndividualCandidacyProcessBean bean = new ErasmusIndividualCandidacyProcessBean(candidacyProcess);
	bean.setPersonBean(new PersonBean());
	bean.getPersonBean().setIdDocumentType(IDDocumentType.FOREIGNER_IDENTITY_CARD);
	bean.setPublicCandidacyHashCode(candidacyHashCode);

	request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
	bean.getPersonBean().setEmail(candidacyHashCode.getEmail());
	bean.getPersonBean().setName(name);
	bean.getPersonBean().setDateOfBirth(birthDate);
	bean.getPersonBean().setNationality(nationality);

	bean.willAccessFenix();

	return mapping.findForward("show-application-submission-conditions-for-stork");
    }

    private String getEmail(Map<String, Attribute> attributes) {
	// FIXME For now just generate an random email

	String[] names = { "raul", "gonzalez", "pedro", "joao", "mata", "peter", "michael", "jose", "miguel", "mamede",
		"cristina", "martins" };

	String[] domains = { "teste.ist.utl.pt", "ist.utl.pt", "mail.pt", "hotmail.com", "kmail.com", "zenix.com" };

	Random random = new Random();
	while (true) {
	    try {
		String name = f("%s.%s", names[random.nextInt(names.length)], names[random.nextInt(names.length)]);
		String domain = domains[random.nextInt(domains.length)];
		String email = f("%s@%s", name, domain);

		DegreeOfficePublicCandidacyHashCode.getUnusedOrCreateNewHashCodeAndSendEmailForApplicationSubmissionToCandidate(
			getProcessType(), getCurrentOpenParentProcess(), email);

		return email;
	    } catch (HashCodeForEmailAndProcessAlreadyBounded e) {
		continue;
	    }
	}

    }

    private YearMonthDay getBirthDate(Map<String, Attribute> attributes) {
	Attribute attr = attributes.get(STORK_BIRTHDATE);
	String birthDateText = attr != null ? attr.getSemanticValue() : null;

	if (birthDateText == null || !birthDateText.matches("\\d{2}\\.\\d{2}\\.\\d{4}")) {
	    return null;
	}

	String[] compounds = birthDateText.split("\\.");
	return new YearMonthDay(Integer.valueOf(compounds[2]), Integer.valueOf(compounds[1]), Integer.valueOf(compounds[0]));
    }

    private String getStorkName(Map<String, Attribute> attributes) {
	Attribute attr = attributes.get(STORK_NAME);
	return attr != null ? attr.getSemanticValue() : null;
    }

    private Country getNationality(Map<String, Attribute> attributes) {
	Attribute attr = attributes.get(STORK_NATIONALITY);
	String nationalityCode = attr != null ? attr.getSemanticValue() : null;

	if (nationalityCode == null || !nationalityCode.matches("\\w{2,3}")) {
	    return null;
	}

	return nationalityCode.length() == 2 ? Country.readByTwoLetterCode(nationalityCode) : Country
		.readByThreeLetterCode(nationalityCode);
    }

    public ActionForward prepareCandidacyCreationForStork(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());

	return mapping.findForward("show-candidacy-creation-page");

    }

    public ActionForward prepareEditCandidacyInformation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());

	return mapping.findForward("edit-candidacy-information");
    }

    public ActionForward editCandidacyInformationInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	return mapping.findForward("edit-candidacy-information");
    }

    public ActionForward editCandidacyInformation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	ErasmusIndividualCandidacyProcessBean bean = (ErasmusIndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();
	try {
	    ActionForward actionForwardError = verifySubmissionPreconditions(mapping);
	    if (actionForwardError != null)
		return actionForwardError;

	    if (!isApplicationSubmissionPeriodValid()) {
		return beginCandidacyProcessIntro(mapping, form, request, response);
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
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	request.setAttribute("degreeCourseInformationBean", new DegreeCourseInformationBean(
		(ExecutionYear) getCurrentOpenParentProcess().getCandidacyExecutionInterval()));

	return mapping.findForward("edit-candidacy-degree-and-courses");
    }

    public ActionForward prepareEditDegreeAndCoursesInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	return mapping.findForward("edit-candidacy-degree-and-courses");
    }

    public ActionForward editDegreeAndCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	ErasmusIndividualCandidacyProcessBean bean = (ErasmusIndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();
	try {
	    ActionForward actionForwardError = verifySubmissionPreconditions(mapping);
	    if (actionForwardError != null)
		return actionForwardError;

	    if (!isApplicationSubmissionPeriodValid()) {
		return beginCandidacyProcessIntro(mapping, form, request, response);
	    }

	    executeActivity(bean.getIndividualCandidacyProcess(), "EditPublicDegreeAndCoursesInformation",
		    getIndividualCandidacyProcessBean());
	} catch (final DomainException e) {
	    if (e.getMessage().equals("error.IndividualCandidacyEvent.invalid.payment.code")) {
		throw e;
	    }

	    addActionMessage(request, e.getMessage(), e.getArgs());
	    request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	    return mapping.findForward("edit-candidacy-degree-and-courses");
	}

	request.setAttribute("individualCandidacyProcess", bean.getIndividualCandidacyProcess());
	return backToViewCandidacyInternal(mapping, form, request, response);
    }

    private Map<String, Attribute> buildStorkAttributes(String attrList) {
	StringTokenizer st = new StringTokenizer(attrList, ";");
	Map<String, Attribute> attributes = new HashMap<String, Attribute>();

	int i = 1;
	while (st.hasMoreTokens()) {
	    String[] params = st.nextToken().split(":");
	    String attrName = params[0];
	    String value = params[2].equals("null") ? null : params[2].substring(1, params[2].length() - 2);

	    attributes.put(attrName, new Attribute(i++, attrName, false, value));
	}

	return attributes;
    }

    private static final String STORK_NAME = "nombre";
    private static final String STORK_BIRTHDATE = "fechadenacimiento";
    private static final String STORK_NATIONALITY = "nacionalidad";

    private static final String f(String format, Object... args) {
	return String.format(format, args);
    }

    public ActionForward chooseCountry(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	ErasmusIndividualCandidacyProcessBean bean = (ErasmusIndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();
	request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
	request.setAttribute("degreeCourseInformationBean", readDegreeCourseInformationBean(request));

	RenderUtils.invalidateViewState();

	if ("editCandidacy".equals(request.getParameter("userAction"))) {
	    bean.getErasmusStudentDataBean().setSelectedUniversity(null);

	    return mapping.findForward("edit-candidacy-degree-and-courses");
	}

	return mapping.findForward("candidacy-continue-creation");
    }

    public ActionForward chooseUniversity(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	ErasmusIndividualCandidacyProcessBean bean = (ErasmusIndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();
	request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
	request.setAttribute("degreeCourseInformationBean", readDegreeCourseInformationBean(request));

	RenderUtils.invalidateViewState();

	return mapping.findForward("edit-candidacy-degree-and-courses");
    }

}
