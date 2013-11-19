package net.sourceforge.fenixedu.presentationTier.Action.publico.alumni;

import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.alumni.RegisterAlumniData;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.alumni.AlumniNotificationService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.alumni.AlumniErrorSendingMailBean;
import net.sourceforge.fenixedu.dataTransferObject.alumni.AlumniIdentityCheckRequestBean;
import net.sourceforge.fenixedu.dataTransferObject.alumni.publicAccess.AlumniLinkRequestBean;
import net.sourceforge.fenixedu.dataTransferObject.alumni.publicAccess.AlumniPasswordBean;
import net.sourceforge.fenixedu.dataTransferObject.alumni.publicAccess.AlumniPublicAccessBean;
import net.sourceforge.fenixedu.domain.Alumni;
import net.sourceforge.fenixedu.domain.AlumniRequestType;
import net.sourceforge.fenixedu.domain.Instalation;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.bennu.core.util.CoreConfiguration;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.EMail;
import pt.utl.ist.fenix.tools.util.i18n.Language;

import com.octo.captcha.module.struts.CaptchaServicePlugin;

@Mapping(module = "publico", path = "/alumni", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "alumniPublicAccessInner", path = "alumni.alumniPublicAccessInner"),
        @Forward(name = "alumniMailingLists", path = "alumni.alumniMailingLists"),
        @Forward(name = "alumniCreateFormation", path = "alumni.alumniPublicAccessCreateFormation"),
        @Forward(name = "alumniCreatePasswordRequest", path = "alumni.alumniCreatePasswordRequest"),
        @Forward(name = "alumniPublicAccessInformationInquiry", path = "alumni.alumniPublicAccessInformationInquiry"),
        @Forward(name = "alumniPublicAccessIdentityCheck", path = "alumni.alumniPublicAccessIdentityCheck"),
        @Forward(name = "alumniPublicAccess", path = "alumni.alumniPublicAccess"),
        @Forward(name = "alumniErrorSendMail", path = "alumni.errorSendMail"),
        @Forward(name = "alumniRegistrationResult", path = "alumni.alumniRegistrationResult"),
        @Forward(name = "alumniPasswordRequired", path = "alumni.alumniPasswordRequired"),
        @Forward(name = "alumniPublicAccessMessage", path = "alumni.alumniPublicAccessMessage"),
        @Forward(name = "alumniPublicAccessRegistrationEmail", path = "alumni.alumniPublicAccessRegistrationEmail") })
public class AlumniPublicAccessDA extends FenixDispatchAction {

    final ResourceBundle RESOURCES = ResourceBundle.getBundle("resources.AlumniResources", Language.getLocale());

    public ActionForward initFenixPublicAccess(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        request.getSession(true);

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
            RegisterAlumniData.run((AlumniIdentityCheckRequestBean) getObjectFromViewState("alumniBean"));
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
        } catch (Exception e) { // may be thrown if the id is not valid
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
            final Alumni alumni =
                    RegisterAlumniData.run(alumniBean.getStudentNumber(), alumniBean.getDocumentIdNumber().trim(),
                            alumniBean.getEmail());

            // development help
            // String url =
            // MessageFormat.format(RESOURCES.getString("alumni.public.registration.url"),
            // alumni.getStudent()
            // .getPerson().getFirstAndLastName(),
            // alumni.getExternalId().toString(), alumni.getUrlRequestToken(),
            // ResourceBundle.getBundle("resources.GlobalResources").getString("fenix.url"));
            String url = AlumniNotificationService.getRegisterConclusionURL(alumni);
            request.setAttribute("alumniEmailSuccessMessage", "http" + url.split("http")[1]);
            request.setAttribute("alumni", alumni);

        } catch (DomainException e) {
            if ("error.no.concluded.registrations".equals(e.getKey()) || "error.no.registrations".equals(e.getKey())
                    || "error.person.no.student".equals(e.getKey())) {
                request.setAttribute("showReportError", "true");
                request.setAttribute("errorMessage", getResources(request).getMessage(e.getKey(), e.getArgs()));
            }
            addActionMessage(request, e.getKey(), e.getArgs());
            request.setAttribute("showForm", "true");
            request.setAttribute("alumniBean", alumniBean);
            return mapping.findForward("alumniPublicAccess");
        }
        return mapping.findForward("alumniPublicAccessRegistrationEmail");
    }

    public ActionForward prepareSendEmailReportingError(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        AlumniErrorSendingMailBean mailSendingBean = new AlumniErrorSendingMailBean();
        mailSendingBean.setDocumentIdNumber(request.getParameter("documentIdNumber"));
        mailSendingBean.setStudentNumber(Integer.valueOf(request.getParameter("studentNumber")));
        mailSendingBean.setContactEmail(request.getParameter("email"));
        mailSendingBean.setErrorMessage(request.getParameter("errorMessage"));

        request.setAttribute("alumniErrorSendMail", mailSendingBean);
        return mapping.findForward("alumniErrorSendMail");
    }

    public ActionForward sendEmailReportingError(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.AlumniResources", Language.getLocale());

        final AlumniErrorSendingMailBean alumniBean = getRenderedObject();
        StringBuilder mailBody = new StringBuilder();
        mailBody.append(resourceBundle.getString("message.alumni.mail.body.header"));
        mailBody.append("'").append(resourceBundle.getString(alumniBean.getErrorMessage())).append("'\n\n");

        String[] mailArgs = new String[8];
        mailArgs[0] = alumniBean.getFullName();
        mailArgs[1] = alumniBean.getStudentNumber().toString();
        mailArgs[2] = alumniBean.getContactEmail();
        mailArgs[3] = alumniBean.getDocumentIdNumber();
        mailArgs[4] = alumniBean.getDateOfBirthYearMonthDay().toString();
        mailArgs[5] = alumniBean.getSocialSecurityNumber();
        mailArgs[6] = alumniBean.getNameOfFather();
        mailArgs[7] = alumniBean.getNameOfMother();

        String messageBody =
                RenderUtils.getFormatedResourceString("ALUMNI_RESOURCES", "message.alumni.mail.person.data", mailArgs);
        mailBody.append(messageBody);
        mailBody.append("\n\n").append(resourceBundle.getString("message.alumni.mail.body.footer"));
        EMail email = null;
        try {
            if (!request.getServerName().equals("localhost")) {
                email = new EMail("mail.adm", "erro@dot.ist.utl.pt");
                String aluminiEmailAddress = Instalation.getInstance().getInstituitionalEmailAddress("alumni");
                email.send(aluminiEmailAddress, "Erro Registo Alumni", mailBody.toString());
                if (CoreConfiguration.getConfiguration().developmentMode()) {
                    System.out.println("send email to alumni@ist.utl.pt with subject Erro Registo Alumni and body : "
                            + mailBody.toString());
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
            throw new Error(t);
        }

        request.setAttribute("alumniPublicAccessTitle", "title.report.error");
        request.setAttribute("alumniPublicAccessMessage", "message.public.error.mail.success");
        return mapping.findForward("alumniPublicAccessMessage");
    }

    public ActionForward innerFenixPublicAccessValidation(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        String alumniId = RESOURCES.getString("alumni.public.registration.first.argument");
        String urlToken = RESOURCES.getString("alumni.public.registration.second.argument");
        final Alumni alumni = getDomainObject(request, alumniId);

        if (StringUtils.isEmpty(alumniId) || StringUtils.isEmpty(urlToken) || alumni == null) {
            request.setAttribute("alumniPublicAccessTitle", "registration.error.old.request.link.title");
            request.setAttribute("alumniPublicAccessMessage", "error.alumni.wrong.arguments");
            return mapping.findForward("alumniPublicAccessMessage");
        }

        if (alumni.isRegistered() && !alumni.isRecoveringPassword()) {
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
        request.setAttribute("alumniId", getFromRequest(request, alumniId));
        request.setAttribute("urlToken", request.getParameter(urlToken));
        return mapping.findForward("alumniPublicAccessInner");
    }

    public ActionForward registrationConclusion(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final Alumni alumni = ((AlumniLinkRequestBean) getObjectFromViewState("alumniBean")).getAlumni();
        RenderUtils.invalidateViewState();

        alumni.validateEmailFromRegistrationProcess();

        if (alumni.hasPastLogin()) {
            try {
                RegisterAlumniData.run(alumni, Boolean.TRUE);
                request.setAttribute("loginAlias", alumni.getLoginUsername());
                request.setAttribute("registrationResult", "true");
            } catch (FenixServiceException e) {
                request.setAttribute("registrationResult", "false");
            }

            request.setAttribute("passwordAccessBean", new AlumniPasswordBean(alumni));
            request.setAttribute("alumni", alumni);
            return mapping.findForward("alumniRegistrationResult");

        } else {
            request.setAttribute("passwordAccessBean", new AlumniPasswordBean(alumni, AlumniRequestType.PASSWORD_REQUEST));
            if (alumni.hasAnyPendingIdentityRequests()) {
                request.setAttribute("pendingRequests", "true");
            }
            return mapping.findForward("alumniCreatePasswordRequest");
        }
    }

    public ActionForward createPasswordRequestInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        request.setAttribute("publicAccessBean", getObjectFromViewState("publicAccessBean"));
        return mapping.findForward("alumniCreatePasswordRequest");
    }

    public ActionForward createPasswordRequest(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        AlumniPasswordBean alumniBean = (AlumniPasswordBean) getObjectFromViewState("passwordAccessBean");

        try {
            RegisterAlumniData.run(alumniBean);
            request.setAttribute("loginAlias", alumniBean.getAlumni().getLoginUsername());
            request.setAttribute("registrationResult", "true");
        } catch (DomainException e) {
            addActionMessage("error", request, e.getKey());
            request.setAttribute("registrationResult", "false");
            return mapping.findForward("alumniCreatePasswordRequest");
        }

        request.setAttribute("publicAccessBean", new AlumniPublicAccessBean(alumniBean.getAlumni()));
        request.setAttribute("alumni", alumniBean.getAlumni());
        return mapping.findForward("alumniPasswordRequired");
    }

    public ActionForward checkLists(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String publicAccessURL =
                MessageFormat.format(RESOURCES.getString("alumni.public.registration.url.content.path"), ResourceBundle
                        .getBundle("resources.GlobalResources").getString("fenix.url"));
        request.setAttribute("publicAccessUrl", publicAccessURL);
        return mapping.findForward("alumniMailingLists");
    }

}