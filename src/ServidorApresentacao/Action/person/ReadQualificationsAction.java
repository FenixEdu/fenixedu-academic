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

import DataBeans.person.InfoSiteQualifications;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;



/**
 * @author  Barbosa
 * @author  Pica
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

			   InfoSiteQualifications infoSiteQualifications = (InfoSiteQualifications) ServiceUtils.executeService(userView, "ReadQualifications", args);

			   request.setAttribute("infoSiteQualifications", infoSiteQualifications);
		   }
		   return mapping.findForward("show-form");
	   }
	
}