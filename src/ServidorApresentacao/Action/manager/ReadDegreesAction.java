/*
 * Created on 13/Mai/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorApresentacao.Action.manager;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author lmac1
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ReadDegreesAction extends FenixAction {

		public ActionForward execute(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws FenixActionException {
		
			HttpSession session = getSession(request);
			UserView userView =
				(UserView) session.getAttribute(SessionConstants.U_VIEW);
		
			try {
		
		List degrees = null;
				//Object args1[]={};
				GestorServicos serviceManager = GestorServicos.manager();
				degrees = (List) serviceManager.executar(
				userView,
				"ReadDegreesService",
				null);
		
				Collections.sort(degrees);
				session.setAttribute(SessionConstants.INFO_DEGREES_LIST,degrees);
			} catch (FenixServiceException e) {
				throw new FenixActionException(e);
			}

			return mapping.findForward("readDegrees");
		}

}


