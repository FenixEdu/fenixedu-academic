/*
 * Created on Nov 18, 2003
 *  
 */
package ServidorApresentacao.Action.person;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import antlr.collections.List;


/**
 * @author João Simas
 * @author Nuno Barbosa
 */
public class ReadQualificationsAction extends FenixAction
{
	/*
		* (non-Javadoc)
		* 
		* @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping,
		*      org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest,
		*      javax.servlet.http.HttpServletResponse)
		*/
	   public ActionForward execute(
		   ActionMapping mapping,
		   ActionForm actionForm,
		   HttpServletRequest request,
		   HttpServletResponse response)
		   throws Exception
	   {
		   HttpSession session = request.getSession(false);

		   IUserView userView = SessionUtils.getUserView(request);

		   if (session != null)
		   {	
		   		Object[] args = { userView.getUtilizador() };

			   List infoQualifications = (List)ServiceUtils.executeService(userView, "ReadQualifications", args);

			   request.setAttribute("infoQualifications", infoQualifications);
		   }
		   return mapping.findForward("show-form");
	   }
	
}