
package ServidorApresentacao.Action.masterDegree.administrativeOffice.student;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.TipoCurso;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 * 
 * This is the Action to display all the master degrees.
 * 
 */
public class MasterDegreeListingDispatchAction extends DispatchAction {

	public ActionForward chooseDegreeFromList(ActionMapping mapping, ActionForm form,
											HttpServletRequest request,
											HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession(false);

		if (session != null) {
			
			GestorServicos serviceManager = GestorServicos.manager();
			
			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
			TipoCurso degreeType = new TipoCurso(TipoCurso.MESTRADO);
			
			Object args[] = {degreeType};
	  
			List result = null;
			try {
				result = (List) serviceManager.executar(userView, "ReadAllMasterDegrees", args);
			} catch (NonExistingServiceException e) {
				throw new NonExistingActionException("O Curso de Mestrado", e);
			}

			session.setAttribute(SessionConstants.MASTER_DEGREE_LIST, result);
		  
			return mapping.findForward("DisplayMasterDegreeList");
		} else
		  throw new Exception();   
	  }

	
	  
}
