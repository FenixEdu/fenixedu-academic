/*
 * Created on 15/Mai/2003
 *
 */
package ServidorApresentacao.Action.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoDegree;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author lmac1
 */
public class ReadDegreeAction extends FenixAction  {
	
	public ActionForward execute(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws FenixActionException {
				
				HttpSession session = request.getSession(false);
				DynaActionForm readDegreeForm = (DynaActionForm) form;
		
				UserView userView =
					(UserView) session.getAttribute(SessionConstants.U_VIEW);

//		        List infoDegrees =
//					(List) session.getAttribute(SessionConstants.INFO_DEGREES_LIST);

				Integer internalId = (Integer)readDegreeForm.get("idInternal");

				Object args[] = { internalId };
						GestorServicos manager = GestorServicos.manager();
		
				try {
					InfoDegree degree = (InfoDegree) manager.executar(userView, "ReadDegreeService", args);
					request.setAttribute(SessionConstants.INFO_DEGREE,degree);
					 } catch (NonExistingServiceException e) {
						throw new NonExistingActionException(e.getMessage());
					} catch (FenixServiceException e) {
						throw new FenixActionException(e);
					}
					 
				return mapping.findForward("viewDegree");
			}
}
