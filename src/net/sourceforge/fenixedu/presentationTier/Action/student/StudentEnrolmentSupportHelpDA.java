package net.sourceforge.fenixedu.presentationTier.Action.student;

import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.support.SupportRequestBean;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.ExceptionHandlingAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.i18n.Language;

@Mapping(path = "/exceptionHandlingAction", module = "student")
@Forwards( { @Forward(name = "supportHelpInquiry", path = "/supportHelpInquiry.jsp", contextRelative = true, useTile = false),
	@Forward(name = "showErrorPage", path = "/exception/errorRegistered.jsp", contextRelative = true, useTile = false)

})
public class StudentEnrolmentSupportHelpDA extends ExceptionHandlingAction {

    @Override
    public final ActionForward prepareSupportHelp(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	final SupportRequestBean requestBean = new SupportRequestBean();
	requestBean.setResponseEmail(getLoggedPerson(request).getInstitutionalOrDefaultEmailAddressValue());
	requestBean.setRequestContext(rootDomainObject.readContentByOID(Integer.valueOf(request.getParameter("contextId"))));

	final ResourceBundle bundle = ResourceBundle.getBundle("resources.ApplicationResources", Language.getLocale());
	final ExecutionSemester executionSemester = ExecutionSemester.readActualExecutionSemester();
	requestBean.setSubject(bundle.getString("label.student.enrolments") + " " + executionSemester.getQualifiedName());

	request.setAttribute("requestBean", requestBean);
	return mapping.findForward("supportHelpInquiry");
    }

    @Override
    protected ActionForward getActionForward(ActionMapping mapping) {
	return mapping.findForward("showErrorPage");
    }

    @Override
    protected String getSendToEmailAddress(HttpServletRequest request, SupportRequestBean requestBean) {
	final ResourceBundle bundle = ResourceBundle.getBundle("resources.GlobalResources", Language.getLocale());
	return bundle.getString("support.enrolments.mail");
    }

}
