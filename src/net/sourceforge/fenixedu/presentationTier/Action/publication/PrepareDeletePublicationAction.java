/*
 * Created on 21-Oct-2004
 *
 * @author Carlos Pereira & Francisco Passos
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.publication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.publication.InfoPublication;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Carlos Pereira & Francisco Passos
 *
 */
public class PrepareDeletePublicationAction extends FenixAction {
	public ActionForward execute(
			ActionMapping mapping,
			ActionForm actionForm,
			HttpServletRequest request,
			HttpServletResponse response)
	throws Exception
	{
		IUserView userView = SessionUtils.getUserView(request);
		Integer internalId = new Integer(request.getParameter("idInternal"));
		
		Object[] args = { internalId, userView };
		
		InfoPublication infoPublication =
			(InfoPublication) ServiceUtils.executeService(userView, "ReadPublicationByInternalId", args);
		
		/*
		DynaActionForm form = (DynaActionForm) actionForm;
		form.set("idInternal",internalId);
		*/
		request.setAttribute("infoPublication",infoPublication);
		return mapping.findForward("prepared");
	}
}