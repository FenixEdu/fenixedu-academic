package net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy.publicProgram;

import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.PublicCandidacyHashCode;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramPublicCandidacyHashCode;
import net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy.academicAdminOffice.PhdProgramCandidacyProcessDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.i18n.Language;

@Mapping(path = "/candidacies/phdProgramCandidacyProcess", module = "publico")
@Forwards( {

@Forward(name = "createCandidacyIdentification", path = "phdProgram.createCandidacyIdentification"),

@Forward(name = "createCandidacyIdentificationSuccess", path = "phdProgram.createCandidacyIdentificationSuccess"),

@Forward(name = "candidacyIdentificationRecovery", path = "phdProgram.candidacyIdentificationRecovery"),

@Forward(name = "createCandidacyStepOne", path = "phdProgram.createCandidacyStepOne")

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

	sendSubmissionEmailForandidacyForm(hashCode, mapping, request);

	return mapping.findForward("createCandidacyIdentificationSuccess");
    }

    private void sendSubmissionEmailForandidacyForm(PhdProgramPublicCandidacyHashCode candidacyHashCode, ActionMapping mapping,
	    HttpServletRequest request) {
	final ResourceBundle bundle = ResourceBundle.getBundle("resources.CandidateResources", Language.getLocale());
	final String subject = bundle.getString("message.phd.email.subject.send.link.to.submission.form");
	final String body = bundle.getString("message.phd.email.body.send.link.to.submission.form");
	final String link = getFullLinkForSubmission(mapping, request, candidacyHashCode);
	candidacyHashCode.sendEmail(subject, String.format(body, link));
    }

    private String getFullLinkForSubmission(ActionMapping mapping, HttpServletRequest request, PublicCandidacyHashCode hashCode) {
	final ResourceBundle bundle = ResourceBundle.getBundle("resources.CandidateResources");
	final String link = bundle.getString("const.public.applications.link.http.hostname");
	return link + request.getContextPath() + getLinkForSubmission(mapping, request, hashCode);
    }

    private String getLinkForSubmission(ActionMapping mapping, HttpServletRequest request, PublicCandidacyHashCode hashCode) {
	String prefix;
	if (Language.getLanguage().equals(Language.en)) {
	    prefix = "/candidacies/phd-program/submission";
	} else {
	    prefix = "/candidaturas/programa-doutoral/submissao";

	}
	return prefix + "?hash=" + hashCode.getValue() + "&locale=" + Language.getLocale().getLanguage();
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
		sendRecoveryEmailForCandidate(hashCode, mapping, request);
	    } else {
		sendSubmissionEmailForandidacyForm(hashCode, mapping, request);
	    }
	}

	return mapping.findForward("createCandidacyIdentificationSuccess");
    }

    private void sendRecoveryEmailForCandidate(PhdProgramPublicCandidacyHashCode candidacyHashCode, ActionMapping mapping,
	    HttpServletRequest request) {
	final ResourceBundle bundle = ResourceBundle.getBundle("resources.CandidateResources", Language.getLocale());
	final String subject = bundle.getString("message.phd.email.subject.recovery.access");
	final String body = bundle.getString("message.phd.email.body.recovery.access");
	final String link = getFullLinkForRecovery(mapping, request, candidacyHashCode);
	candidacyHashCode.sendEmail(subject, String.format(body, link, candidacyHashCode.getPhdProgramCandidacyProcess()
		.getProcessNumber()));
    }

    private String getFullLinkForRecovery(ActionMapping mapping, HttpServletRequest request, PublicCandidacyHashCode hashCode) {
	final ResourceBundle bundle = ResourceBundle.getBundle("resources.CandidateResources");
	final String link = bundle.getString("const.public.applications.link.http.hostname");
	return link + request.getContextPath() + getLinkForRecovery(mapping, request, hashCode);
    }

    private String getLinkForRecovery(ActionMapping mapping, HttpServletRequest request, PublicCandidacyHashCode hashCode) {
	String prefix;
	if (Language.getLanguage().equals(Language.en)) {
	    prefix = "/candidacies/phd-program/recovery-access";
	} else {
	    prefix = "/candidaturas/programa-doutoral/recuperar-acesso";
	}
	return prefix + "?hash=" + hashCode.getValue() + "&locale=" + Language.getLocale().getLanguage();
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
	    // TODO: if prepareCreateCandidacy is different then send that page
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
	bean.setCandidacyHashCode(hashCode);

	request.setAttribute("candidacyBean", bean);
	return mapping.findForward("createCandidacyStepOne");
    }

    public ActionForward createCandidacyStep1Invalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("candidacyBean", getRenderedObject("candidacyBean"));
	return mapping.findForward("createCandidacyStepOne");
    }

    public ActionForward viewCandidacy(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	//TODO: ..............
	return null;
    }

    // TODO: createCandidacyStep2 methods
    // TODO: createCandidacyStep2Invalid and postback methods
    // TODO: createCandidacy methods

}
