/*
 * 
 * Created on 27 of March de 2003
 *
 *
 * Autores :
 *   - Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *   - Joana Mota (jccm@rnl.ist.utl.pt)
 *
 * modified by Fernanda Quitério
 *
 */

package ServidorApresentacao.Action.masterDegree.coordinator;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import framework.factory.ServiceManagerServiceFactory;

public class ReadCoordinatedDegreesAction extends ServidorApresentacao.Action.base.FenixAction
{

  public ActionForward execute(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response)
      throws Exception {

	HttpSession session = request.getSession(false);
	if (session != null) {
      IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
	  
      Object args[] = new Object[1];
	  args[0] = userView;
	  List degrees = null;
	  List candidates = new ArrayList();
	  try {
	  	degrees = (List) ServiceManagerServiceFactory.executeService(userView, "ReadCoordinatedDegrees", args);
	  
	  } catch (FenixServiceException e) {
		  throw new FenixActionException(e);
	  }

      session.setAttribute(SessionConstants.MASTER_DEGREE_LIST, degrees);
      
	  if (degrees.size() == 1) {
	      request.setAttribute("degree", "0");
	      return mapping.findForward("oneDegreeSucces");
	  }
      return mapping.findForward("ChooseDegree");

    } else
      throw new Exception();   
  }

}
