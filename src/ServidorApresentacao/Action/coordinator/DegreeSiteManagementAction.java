package ServidorApresentacao.Action.coordinator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;

/**
 * 
 * @author João Mota
 * 
 */
public class DegreeSiteManagementAction extends FenixDispatchAction {

	public ActionForward subMenu(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
			
		return mapping.findForward("degreeSiteMenu");
	}
	
	public ActionForward prepareEdit(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
			
		return mapping.findForward("editInformation");
	}
	
	public ActionForward viewHistoric(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
			
		return mapping.findForward("viewHistoric");
	}
	
	public ActionForward viewDegreeSite(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
			
		return mapping.findForward("viewSite");
	}

}
