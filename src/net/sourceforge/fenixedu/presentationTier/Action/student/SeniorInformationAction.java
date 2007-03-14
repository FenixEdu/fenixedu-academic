/*
 * Created on Dec 10, 2004
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Senior;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Luis Egidio, luis.egidio@ist.utl.pt
 * 
 */
public class SeniorInformationAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        RenderUtils.invalidateViewState();
	
	Registration registration = null;
	
	final Integer registrationOID = getIntegerFromRequest(request, "registrationOID");
	final Student loggedStudent = getUserView(request).getPerson().getStudent();
	
	if (registrationOID != null) {
	    registration = rootDomainObject.readRegistrationByOID(registrationOID);
	} else if (loggedStudent != null) {
	    if (loggedStudent.getRegistrations().size() == 1) {
		registration = loggedStudent.getRegistrations().get(0);
	    } else {
		request.setAttribute("student", loggedStudent);
		return mapping.findForward("chooseRegistration");
	    }
	}

	if (registration == null) {
	    throw new FenixActionException();
	} else {
	    final Senior senior = (Senior) executeService("ReadStudentSenior", new Object[] { registration });
	    request.setAttribute("senior", senior);

	    return mapping.findForward("show-form");
	}
    }
    
    public ActionForward change(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	IViewState viewState = RenderUtils.getViewState("EditSeniorProfessionalExperienceID");	
	Senior senior = (Senior) viewState.getMetaObject().getObject();	
	request.setAttribute("senior", senior);

	return mapping.findForward("show-result");
    }

}
