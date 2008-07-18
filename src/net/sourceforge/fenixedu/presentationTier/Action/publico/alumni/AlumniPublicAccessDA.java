package net.sourceforge.fenixedu.presentationTier.Action.publico.alumni;

import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.alumni.AlumniIdentityCheckRequestBean;
import net.sourceforge.fenixedu.dataTransferObject.alumni.formation.AlumniFormation;
import net.sourceforge.fenixedu.dataTransferObject.alumni.formation.AlumniFormationBean;
import net.sourceforge.fenixedu.dataTransferObject.alumni.publicAccess.AlumniLinkRequestBean;
import net.sourceforge.fenixedu.dataTransferObject.alumni.publicAccess.AlumniPasswordBean;
import net.sourceforge.fenixedu.dataTransferObject.alumni.publicAccess.AlumniPublicAccessBean;
import net.sourceforge.fenixedu.domain.Alumni;
import net.sourceforge.fenixedu.domain.AlumniRequestType;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.cms.messaging.mailSender.SimpleMailSenderAction;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.utl.ist.fenix.tools.util.i18n.Language;

import com.octo.captcha.module.struts.CaptchaServicePlugin;

public class AlumniPublicAccessDA extends SimpleMailSenderAction {

    final ResourceBundle RESOURCES = ResourceBundle.getBundle("resources.AlumniResources", Language.getLocale());

    public ActionForward initFenixPublicAccess(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	if (getFromRequest(request, "showForm") != null) {
	    request.setAttribute("showForm", "true");
	    request.setAttribute("alumniBean", getFromRequest(request, "alumniBean"));
	} else {
	    request.setAttribute("showForm", "false");
	    request.setAttribute("alumniBean", new AlumniLinkRequestBean());
	}
	return mapping.findForward("alumniPublicAccess");
    }

    public ActionForward requestIdentityCheck(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	request.setAttribute("alumniBean", new AlumniIdentityCheckRequestBean(AlumniRequestType.STUDENT_NUMBER_RECOVERY));
	return mapping.findForward("alumniPublicAccessIdentityCheck");
    }

    public ActionForward processRequestIdentityCheckError(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	request.setAttribute("alumniBean", getFromRequest(request, "alumniBean"));
	return mapping.findForward("alumniPublicAccessIdentityCheck");
    }

    public ActionForward processRequestIdentityCheck(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	if (!validateCaptcha(mapping, request)) {
	    return requestIdentityCheck(mapping, actionForm, request, response);
	}

	try {
	    executeService("RegisterAlumniData", getObjectFromViewState("alumniBean"));
	} catch (DomainException e) {
	    addActionMessage(request, e.getKey(), e.getArgs());
	    return processRequestIdentityCheckError(mapping, actionForm, request, response);
	}

	request.setAttribute("alumniPublicAccessTitle", "identity.check.request.successful.creation.title");
	request.setAttribute("alumniPublicAccessMessage", "identity.check.request.successful.creation.message");
	return mapping.findForward("alumniPublicAccessMessage");
    }

    private boolean validateCaptcha(ActionMapping mapping, HttpServletRequest request) {
	final String captchaId = request.getSession().getId();
	final String captchaResponse = request.getParameter("j_captcha_response");

	try {
	    if (!CaptchaServicePlugin.getInstance().getService().validateResponseForID(captchaId, captchaResponse)) {
		addActionMessage("error", request, "captcha.wrong.word");
		return false;
	    }
	    return true;
	} catch (Exception e) { // should not happen, may be thrown if the id is
	    // not valid
	    request.setAttribute("captcha.unknown.error", "captcha.unknown.error");
	    request.setAttribute("showForm", "true");
	    request.setAttribute("alumniBean", getObjectFromViewState("alumniBean"));
	    return false;
	}
    }

    // 1st validation: captcha validation
    public ActionForward validateFenixAcessData(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	final AlumniLinkRequestBean alumniBean = (AlumniLinkRequestBean) getObjectFromViewState("alumniBean");

	if (!alumniBean.getPrivacyPolicy().booleanValue()) {
	    request.setAttribute("showForm", "true");
	    request.setAttribute("privacyPolicyPublicAccessMessage", "privacy.policy.acceptance");
	    request.setAttribute("alumniBean", alumniBean);
	    return mapping.findForward("alumniPublicAccess");
	}

	if (!validateCaptcha(mapping, request)) {
	    return mapping.findForward("alumniPublicAccess");
	}

	try {
	    final Alumni alumni = (Alumni) executeService("RegisterAlumniData", alumniBean.getStudentNumber(), alumniBean
		    .getDocumentIdNumber(), alumniBean.getEmail());

	    // TODO remove
	    String url = MessageFormat.format(RESOURCES
		    .getString("alumni.public.registration.url"), alumni.getStudent().getPerson().getFirstAndLastName(), alumni
		    .getIdInternal().toString(), alumni.getUrlRequestToken());
	    request.setAttribute("alumniEmailSuccessMessage", "http" + url.split("http")[1]);
	    // TODO remove

	} catch (DomainException e) {
	    addActionMessage(request, e.getKey(), e.getArgs());
	    request.setAttribute("showForm", "true");
	    request.setAttribute("alumniBean", getFromRequest(request, "alumniBean"));
	    return mapping.findForward("alumniPublicAccess");
	}

	return mapping.findForward("alumniPublicAccessRegistrationEmail");
    }

    public ActionForward innerFenixPublicAccessValidation(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	String alumniId = RESOURCES.getString("alumni.public.registration.first.argument");
	String urlToken = RESOURCES.getString("alumni.public.registration.second.argument");
	final Alumni alumni = rootDomainObject.readAlumniByOID(getIntegerFromRequest(request, alumniId));

	if (alumni.isRegistered()) {
	    request.setAttribute("alumniPublicAccessTitle", "registration.error.old.request.link.title");
	    request.setAttribute("alumniPublicAccessMessage", "error.alumni.already.registered");
	    return mapping.findForward("alumniPublicAccessMessage");
	}

	if (!alumni.getUrlRequestToken().equals(UUID.fromString(request.getParameter(urlToken)))) {
	    request.setAttribute("alumniPublicAccessTitle", "registration.error.old.request.link.title");
	    request.setAttribute("alumniPublicAccessMessage", "registration.error.old.request.link.message");
	    return mapping.findForward("alumniPublicAccessMessage");
	}

	request.setAttribute("alumniBean", new AlumniLinkRequestBean(alumni));
	request.setAttribute("alumniId", getIntegerFromRequest(request, alumniId).toString());
	request.setAttribute("urlToken", request.getParameter(urlToken));
	return mapping.findForward("alumniPublicAccessInner");
    }

    // 2nd validation: student number and document number match
    public ActionForward initPasswordGenerationInquiry(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	final AlumniLinkRequestBean alumniBean = (AlumniLinkRequestBean) getObjectFromViewState("alumniBean");
	final Alumni alumni = alumniBean.getAlumni();

	if (!alumni.hasDocumentIdNumber(alumniBean.getDocumentIdNumber())) {
	    RenderUtils.invalidateViewState("error");
	    addActionMessage("error", request, "registration.error.wrong.document.number");
	    request.setAttribute("alumniBean", new AlumniLinkRequestBean(alumni));
	    return mapping.findForward("alumniPublicAccessInner");
	}

	if (!alumni.hasStudentNumber(alumniBean.getStudentNumber())) {
	    RenderUtils.invalidateViewState("error");
	    addActionMessage("error", request, "registration.error.wrong.student.number");
	    request.setAttribute("alumniBean", new AlumniLinkRequestBean(alumni));
	    return mapping.findForward("alumniPublicAccessInner");
	}

	request.setAttribute("publicAccessBean", new AlumniPublicAccessBean(alumni));
	return mapping.findForward("alumniPublicAccessInformationInquiry");
    }

    public ActionForward invalidPasswordGenerationInquiry(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	request.setAttribute("publicAccessBean", getObjectFromViewState("publicAccessBean"));
	return mapping.findForward("alumniPublicAccessInformationInquiry");
    }

    public ActionForward professionalInformationPostback(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	RenderUtils.invalidateViewState("parentBusinessArea-validated");
	RenderUtils.invalidateViewState("childBusinessArea-validated");
	request.setAttribute("publicAccessBean", getObjectFromViewState("publicAccessBean"));
	return mapping.findForward("alumniPublicAccessInformationInquiry");
    }

    public ActionForward updateAlumniInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	final AlumniPublicAccessBean alumniBean = (AlumniPublicAccessBean) getObjectFromViewState("publicAccessBean");
	if (alumniBean.getJobBean().getChildBusinessArea() == null) {
	    request.setAttribute("childBusinessArea-validated", RESOURCES.getString("label.mandatory.field"));
	    request.setAttribute("publicAccessBean", alumniBean);
	    return mapping.findForward("alumniPublicAccessInformationInquiry");
	}

	if (StringUtils.isEmpty(alumniBean.getJobBean().getPosition())) {
	    request.setAttribute("position-validated", RESOURCES.getString("label.mandatory.field"));
	    request.setAttribute("publicAccessBean", alumniBean);
	    return mapping.findForward("alumniPublicAccessInformationInquiry");
	}

	try {
	    executeService("RegisterAlumniData", alumniBean);
	} catch (FenixServiceException e) {
	    addActionMessage("error", request, e.getMessage());
	    return updateAlumniInformationError(mapping, actionForm, request, response);
	}

	request.setAttribute("formationBean", new AlumniFormationBean(alumniBean.getAlumni()));
	return mapping.findForward("alumniCreateFormation");
    }

    public ActionForward updateAlumniInformationError(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	request.setAttribute("publicAccessBean", getRenderedObject("publicAccessBean"));
	RenderUtils.invalidateViewState("message");
	return mapping.findForward("alumniPublicAccessInformationInquiry");
    }

    public ActionForward updateAlumniFormationTypePostback(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	AlumniFormationBean formationBean = (AlumniFormationBean) getObjectFromViewState("formationBean");
	formationBean.getAlumniFormation().updateTypeSchema();
	request.setAttribute("formationEducationArea", getFromRequest(request, "formationEducationArea"));
	RenderUtils.invalidateViewState();
	request.setAttribute("formationBean", formationBean);
	request.setAttribute("formationUpdate", "true");
	return mapping.findForward("alumniCreateFormation");
    }

    public ActionForward updateAlumniFormationInfoPostback(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	AlumniFormationBean formationBean = (AlumniFormationBean) getObjectFromViewState("formationBean");
	formationBean.getAlumniFormation().updateInstitutionSchema();
	request.setAttribute("formationEducationArea", getFromRequest(request, "formationEducationArea"));
	RenderUtils.invalidateViewState();
	request.setAttribute("formationBean", formationBean);
	request.setAttribute("formationUpdate", "true");
	return mapping.findForward("alumniCreateFormation");
    }

    public ActionForward createFormationNext(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	final AlumniFormationBean formationBean = (AlumniFormationBean) getObjectFromViewState("formationBean");
	RenderUtils.invalidateViewState();

	Alumni alumni = formationBean.getAlumni();

	if (alumni.hasPastLogin()) {

	    try {
		executeService("RegisterAlumniData", alumni, Boolean.TRUE);
		request.setAttribute("loginAlias", getAlumniLoginUsername(alumni.getStudent().getPerson()));
		request.setAttribute("registrationResult", "true");
	    } catch (FenixServiceException e) {
		request.setAttribute("registrationResult", "false");
	    }

	    request.setAttribute("passwordAccessBean", new AlumniPasswordBean(alumni));
	    request.setAttribute("alumni", alumni);
	    return mapping.findForward("alumniRegistrationResult");

	} else {
	    request.setAttribute("passwordAccessBean", new AlumniPasswordBean(alumni, AlumniRequestType.IDENTITY_CHECK));
	    if (alumni.hasAnyPendingIdentityRequests()) {
		request.setAttribute("pendingRequests", "true");
	    }
	    return mapping.findForward("alumniCreatePassword");
	}
    }

    public ActionForward createFormationError(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	request.setAttribute("formationBean", getRenderedObject("formationBean"));
	return mapping.findForward("alumniCreateFormation");
    }

    public ActionForward createFormation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	final Alumni alumni = ((AlumniFormationBean) getObjectFromViewState("formationBean")).getAlumni();
	if (innerFormationCreation(request)) {
	    RenderUtils.invalidateViewState();
	    request.setAttribute("formationBean", new AlumniFormationBean(alumni));
	}
	return mapping.findForward("alumniCreateFormation");
    }

    private Boolean innerFormationCreation(HttpServletRequest request) throws FenixServiceException, FenixFilterException {

	final AlumniFormationBean formationBean = (AlumniFormationBean) getObjectFromViewState("formationBean");
	AlumniFormation alumniFormation = formationBean.getAlumniFormation();

	if (getIntegerFromRequest(request, "formationEducationArea") != null) {
	    alumniFormation.setEducationArea(getIntegerFromRequest(request, "formationEducationArea"));
	}

	if (alumniFormation.hasFullInformation()) {
	    try {
		if (alumniFormation.hasAssociatedFormation()) {
		    executeService("EditFormation", alumniFormation);
		} else {
		    executeService("CreateFormation", formationBean.getAlumni(), alumniFormation);
		}
		return Boolean.TRUE;

	    } catch (DomainException e) {
		addActionMessage("error", request, e.getMessage());
	    }
	} else {
	    addActionMessage("error", request, "formation.creation.incomplete.data");
	}

	request.setAttribute("formationBean", formationBean);
	return Boolean.FALSE;
    }

    public ActionForward deleteFormation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	try {
	    executeService("DeleteAlumniQualification", getIntegerFromRequest(request, "formationId"));
	} catch (FenixServiceException e) {
	    addActionMessage("error", request, e.getMessage());
	}

	RenderUtils.invalidateViewState();
	final Alumni alumni = rootDomainObject.readAlumniByOID(getIntegerFromRequest(request, "alumniId"));
	request.setAttribute("formationBean", new AlumniFormationBean(alumni));
	return mapping.findForward("alumniCreateFormation");
    }

    public ActionForward createPasswordInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	request.setAttribute("publicAccessBean", getObjectFromViewState("publicAccessBean"));
	return mapping.findForward("alumniCreatePassword");
    }

    public ActionForward createPassword(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	AlumniPasswordBean alumniBean = (AlumniPasswordBean) getObjectFromViewState("passwordAccessBean");
	if (StringUtils.isEmpty(alumniBean.getPassword()) || StringUtils.isEmpty(alumniBean.getPasswordConfirmation())) {

	    if (StringUtils.isEmpty(alumniBean.getPassword())) {
		addActionMessage("error", request, "registration.error.empty.password");
	    }

	    if (StringUtils.isEmpty(alumniBean.getPasswordConfirmation())) {
		addActionMessage("error", request, "registration.error.empty.password.confirmation");
	    }

	    request.setAttribute("passwordAccessBean", alumniBean);
	    return mapping.findForward("alumniCreatePassword");
	}

	if (!alumniBean.getPassword().equals(alumniBean.getPasswordConfirmation())) {

	    addActionMessage("error", request, "registration.error.password.mismatch");
	    alumniBean.setPassword("");
	    alumniBean.setPasswordConfirmation("");
	    request.setAttribute("passwordAccessBean", alumniBean);
	    return mapping.findForward("alumniCreatePassword");
	}

	final Person alumniPerson = alumniBean.getAlumni().getStudent().getPerson();

	try {
	    executeService("RegisterAlumniData", alumniBean);
	    request.setAttribute("loginAlias", getAlumniLoginUsername(alumniPerson));
	    request.setAttribute("registrationResult", "true");
	} catch (DomainException e) {
	    addActionMessage("error", request, e.getKey());
	    alumniBean.setPassword("");
	    alumniBean.setPasswordConfirmation("");
	    request.setAttribute("passwordAccessBean", alumniBean);
	    request.setAttribute("registrationResult", "false");
	    return mapping.findForward("alumniCreatePassword");
	}

	request.setAttribute("publicAccessBean", new AlumniPublicAccessBean(alumniBean.getAlumni()));
	request.setAttribute("alumni", alumniBean.getAlumni());
	return mapping.findForward("alumniRegistrationResult");
    }

    private String getAlumniLoginUsername(final Person alumniPerson) {
	if (alumniPerson.getIstUsername() == null) {
	    return alumniPerson.getLoginAlias().iterator().next().getAlias();
	}

	return alumniPerson.getIstUsername();
    }
    

    public ActionForward checkLists(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	return mapping.findForward("alumniMailingLists");
    }

}