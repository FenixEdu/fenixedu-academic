/*
 * Created on 15/Mai/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorApresentacao.Action.manager;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoDegree;
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author lmac1
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ReadDegreeAction extends FenixAction  {
	
	public ActionForward execute(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws FenixActionException {
				
				HttpSession session = request.getSession(false);
				UserView userView =
					(UserView) session.getAttribute(SessionConstants.U_VIEW);

		        List infoDegrees =
					(List) session.getAttribute(SessionConstants.INFO_DEGREES_LIST);

	            InfoDegree infoDegree = null;
				String index = (String) request.getParameter("index");
				if (index != null) {
		            	infoDegree = (InfoDegree) infoDegrees.get((new Integer(index)).intValue());
						} else {
					infoDegree = (InfoDegree) session.getAttribute(SessionConstants.INFO_DEGREE);
						}
						session.setAttribute(SessionConstants.INFO_DEGREE, infoDegree);
		
				return mapping.findForward("viewDegree");
			}
}
