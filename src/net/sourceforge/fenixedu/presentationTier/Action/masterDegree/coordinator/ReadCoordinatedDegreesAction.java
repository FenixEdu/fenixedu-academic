/**
 * 
 * Created on 27 of March de 2003
 * 
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 * 
 * modified by Fernanda Quitério
 * 
 * @author Francisco Paulo 27/Out/04 frnp@mega.ist.utl.pt (edit)
 *  
 */

package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.coordinator;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ReadCoordinatedDegreesAction extends
		FenixAction {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		HttpSession session = request.getSession(false);
		if (session != null) {
			IUserView userView = SessionUtils.getUserView(request);

			Object args[] = new Object[1];
			args[0] = userView;
			List degrees = null;
			try {
				degrees = (List) ServiceManagerServiceFactory.executeService(
						userView, "ReadCoordinatedDegrees", args);

			} catch (FenixServiceException e) {
				throw new FenixActionException(e);
			}

			
			//sets in the session a list containing the master degree curricular plans
			session.setAttribute(SessionConstants.MASTER_DEGREE_LIST, degrees);

//			if (degrees.size() == 1) {
//				request.setAttribute("degree", "0");
//				return mapping.findForward("oneDegreeSucces");
//			}

			return mapping.findForward("ChooseDegree");
		}
		throw new Exception();
	}

}