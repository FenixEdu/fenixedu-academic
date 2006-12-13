/*
 * Created on Dec 10, 2004
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.student.Senior;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
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

    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	IUserView userView = getUserView(request);
	
	Senior senior = (Senior) executeService("ReadStudentSenior", new Object[] {userView.getPerson()});
	request.setAttribute("senior", senior);

	return mapping.findForward("show-form");
    }

    public ActionForward change(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	IViewState viewState = RenderUtils.getViewState("EditSeniorProfessionalExperienceID");	
	Senior senior = (Senior) viewState.getMetaObject().getObject();	
	request.setAttribute("senior", senior);

	return mapping.findForward("show-result");
    }
}