/*
 * Created on 21-Oct-2004
 *
 * @author Carlos Pereira & Francisco Passos
 * 
 */
package ServidorApresentacao.Action.publication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
/**
 * @author Carlos Pereira & Francisco Passos
 *
 */
public class DeletePublicationAction extends FenixDispatchAction {
	public ActionForward delete(
			ActionMapping mapping,
			ActionForm actionForm,
			HttpServletRequest request,
			HttpServletResponse response)
	throws Exception
	{
		IUserView userView = SessionUtils.getUserView(request);
		Integer internalId = new Integer(request.getParameter("idInternal"));

		Object[] args = { internalId };
		
		ServiceUtils.executeService(userView, "DeletePublication", args);
		

		request.setAttribute("msg", "message.publications.managementDeleted");

		return mapping.findForward("deleted");
	}
	
	public ActionForward cancel(
			ActionMapping mapping,
			ActionForm actionForm,
			HttpServletRequest request,
			HttpServletResponse response)
	throws Exception
	{
		return mapping.findForward("cancelled");
	}
}