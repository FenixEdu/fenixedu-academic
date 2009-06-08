package net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy.publicProgram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.caseHandling.CreateNewProcess;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
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
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.UploadDocuments;
import net.sourceforge.fenixedu.domain.phd.PhdProgramGuidingBean.PhdProgramGuidingType;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyDocumentUploadBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyRefereeBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramPublicCandidacyHashCode;
import net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy.academicAdminOffice.PhdProgramCandidacyProcessDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

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

@Forward(name = "createCandidacySuccess", path = "phdProgram.createCandidacySuccess")

})
public class PublicPhdProgramCandidacyProcessDA extends PhdProgramCandidacyProcessDA {

    public ActionForward prepareCreateCandidacyIdentification(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
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

	final PhdProgramCandidacyProcessBean bean = (PhdProgramCandidacyProcessBean) getRenderedObject("candidacyBean");
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
	final ResourceBundle bundle = ResourceBundle.getBundle("resources.CandidateResources", Language.getLocale());
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

	final PhdProgramCandidacyProcessBean bean = (PhdProgramCandidacyProcessBean) getRenderedObject("candidacyBean");
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
	final ResourceBundle bundle = ResourceBundle.getBundle("resources.CandidateResources", Language.getLocale());
	final String subject = bundle.getString("message.phd.email.subject.recovery.access");
	final String body = bundle.getString("message.phd.email.body.recovery.access");
	final String link = getFullLink(getRecoveryLinkPrefix(), request, candidacyHashCode);
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

    private String getRecoveryLinkPrefix() {
	return Language.getLanguage().equals(Language.en) ? "/candidacies/phd-program/recovery-access"
		: "/candidaturas/programa-doutoral/recuperar-acesso";
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
	    // TODO check what is necessary in request to the following method
	    return viewCandidacy(mapping, actionForm, request, response);
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

	final PhdProgramCandidacyProcessBean bean = (PhdProgramCandidacyProcessBean) getRenderedObject("candidacyBean");
	bean.setExecutionYear(ExecutionYear.readCurrentExecutionYear());
	// TODO:IMPORTANT change when extending this candidacies to all types
	bean.setCollaborationType(PhdIndividualProgramCollaborationType.EPFL);
	// TODO: ---------------------------------------------------------------

	bean.setGuidings(new ArrayList<PhdProgramGuidingBean>());
	bean.setQualifications(new ArrayList<QualificationBean>());
	bean.setCandidacyReferees(new ArrayList<PhdCandidacyRefereeBean>());
	bean.setCurriculumVitae(createCurriculumVitaeDocumentBean());

	request.setAttribute("candidacyBean", bean);
	RenderUtils.invalidateViewState();

	return mapping.findForward("createCandidacyStepTwo");
    }

    private PhdCandidacyDocumentUploadBean createCurriculumVitaeDocumentBean() {
	final PhdCandidacyDocumentUploadBean bean = new PhdCandidacyDocumentUploadBean();
	bean.setType(PhdIndividualProgramDocumentType.CV);
	return bean;
    }

    public ActionForward createCandidacyStepTwoInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("candidacyBean", getRenderedObject("candidacyBean"));
	return mapping.findForward("createCandidacyStepTwo");
    }

    public ActionForward returnCreateCandidacyStepTwo(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("candidacyBean", getRenderedObject("candidacyBean"));
	RenderUtils.invalidateViewState();
	return mapping.findForward("createCandidacyStepTwo");
    }

    public ActionForward addGuiding(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final PhdProgramCandidacyProcessBean bean = (PhdProgramCandidacyProcessBean) getRenderedObject("candidacyBean");
	final PhdProgramGuidingBean guiding = new PhdProgramGuidingBean();
	guiding.setGuidingType(PhdProgramGuidingType.EXTERNAL);
	bean.addGuiding(guiding);

	request.setAttribute("candidacyBean", bean);
	RenderUtils.invalidateViewState();

	return mapping.findForward("createCandidacyStepTwo");

    }

    public ActionForward removeGuiding(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final PhdProgramCandidacyProcessBean bean = (PhdProgramCandidacyProcessBean) getRenderedObject("candidacyBean");
	bean.removeGuiding(getIntegerFromRequest(request, "removeIndex").intValue());

	request.setAttribute("candidacyBean", bean);
	RenderUtils.invalidateViewState();

	return mapping.findForward("createCandidacyStepTwo");
    }

    public ActionForward addQualification(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final PhdProgramCandidacyProcessBean bean = (PhdProgramCandidacyProcessBean) getRenderedObject("candidacyBean");
	bean.addQualification(new QualificationBean());

	request.setAttribute("candidacyBean", bean);
	RenderUtils.invalidateViewState();

	return mapping.findForward("createCandidacyStepTwo");
    }

    public ActionForward removeQualification(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final PhdProgramCandidacyProcessBean bean = (PhdProgramCandidacyProcessBean) getRenderedObject("candidacyBean");
	bean.removeQualification(getIntegerFromRequest(request, "removeIndex").intValue());

	request.setAttribute("candidacyBean", bean);
	RenderUtils.invalidateViewState();

	return mapping.findForward("createCandidacyStepTwo");
    }

    public ActionForward addCandidacyReferee(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final PhdProgramCandidacyProcessBean bean = (PhdProgramCandidacyProcessBean) getRenderedObject("candidacyBean");
	bean.addCandidacyReferee(new PhdCandidacyRefereeBean());

	request.setAttribute("candidacyBean", bean);
	RenderUtils.invalidateViewState();

	return mapping.findForward("createCandidacyStepTwo");
    }

    public ActionForward removeCandidacyReferee(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final PhdProgramCandidacyProcessBean bean = (PhdProgramCandidacyProcessBean) getRenderedObject("candidacyBean");
	bean.removeCandidacyReferee(getIntegerFromRequest(request, "removeIndex").intValue());

	request.setAttribute("candidacyBean", bean);
	RenderUtils.invalidateViewState();

	return mapping.findForward("createCandidacyStepTwo");
    }

    public ActionForward createCandidacyStepThree(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final PhdProgramCandidacyProcessBean bean = (PhdProgramCandidacyProcessBean) getRenderedObject("candidacyBean");
	bean.setCurriculumVitae(createCurriculumVitaeDocumentBean());

	request.setAttribute("candidacyBean", bean);
	RenderUtils.invalidateViewState();

	return mapping.findForward("createCandidacyStepThree");
    }

    public ActionForward createCandidacyStepThreeInvalid(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("candidacyBean", getRenderedObject("candidacyBean"));
	return mapping.findForward("createCandidacyStepThree");
    }

    @Override
    public ActionForward createCandidacy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final PhdProgramCandidacyProcessBean bean = (PhdProgramCandidacyProcessBean) getRenderedObject("candidacyBean");
	try {

	    // --------------------------
	    // TODO: validate minimum documents
	    // TODO: validate minimum referees
	    // --------------------------

	    // --------------------------
	    // **********************************************************
	    // CHECK IF PERSON ALREADY EXISTS AND USE EXISTING ????
	    // **********************************************************
	    // --------------------------
	    // Create Process Individual, Candidacy, Person
	    CreateNewProcess.run(PhdIndividualProgramProcess.class, bean, buildActivities(bean));

	    // TODO: send email to candidate (can send individual process
	    // number)

	    // TODO: send email to all referees

	} catch (final DomainException e) {
	    addErrorMessage(request, e.getKey(), e.getArgs());
	    request.setAttribute("candidacyBean", bean);
	    bean.clearPerson();
	    clearDocumentsInformation(bean);
	    return mapping.findForward("createCandidacyStepTwo");
	}

	return mapping.findForward("createCandidacySuccess");
    }

    private void clearDocumentsInformation(final PhdProgramCandidacyProcessBean bean) {
	bean.getCurriculumVitae().setFile(null);
	RenderUtils.invalidateViewState("candidacyBean.curriculumVitae");

	// TODO: add another documents
    }

    private List<Pair<Class<?>, Object>> buildActivities(PhdProgramCandidacyProcessBean bean) {

	final List<Pair<Class<?>, Object>> result = new ArrayList<Pair<Class<?>, Object>>();

	// Guiding information
	result.add(pair(AddGuidingsInformation.class, bean.getGuidings()));

	// Qualifications
	result.add(pair(AddQualifications.class, bean.getQualifications()));

	// Referees
	result.add(pair(AddCandidacyReferees.class, bean.getCandidacyReferees()));

	// Upload documents
	// TODO: add all documents in here (for now use only cv)
	result.add(pair(UploadDocuments.class, Collections.singletonList(bean.getCurriculumVitae())));

	return result;
    }

    private Pair<Class<?>, Object> pair(final Class<?> class1, final Object object) {
	return new Pair<Class<?>, Object>(class1, object);
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

	request.setAttribute("hash", hashCode.getValue());
	request.setAttribute("IndividualProgramProcess", hashCode.getIndividualProgramProcess());

	return mapping.findForward("viewCandidacy");
    }

}
