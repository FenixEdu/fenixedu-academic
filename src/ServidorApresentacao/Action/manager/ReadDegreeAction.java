/*
 * Created on 15/Mai/2003
 *
 */
package ServidorApresentacao.Action.manager;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoDegree;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
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
						
				UserView userView =
					(UserView) session.getAttribute(SessionConstants.U_VIEW);
						
				Integer degreeId = new Integer(request.getParameter("degreeId"));	
				Object args[] = { degreeId };
				
				GestorServicos manager = GestorServicos.manager();
				InfoDegree degree = null;
				
				try {
						degree = (InfoDegree) manager.executar(userView, "ReadDegreeService", args);
				} catch(FenixServiceException e) {
					throw new FenixActionException(e);
			    }
			    
			    // trying to read a degree that doesn´t exist in the database
				if(degree == null) {
						ActionErrors actionErrors = new ActionErrors();
						ActionError error = new ActionError("message.nonExistingDegree");
						actionErrors.add("message.nonExistingDegree", error);
						saveErrors(request, actionErrors);
						return mapping.findForward("readDegrees");
				}
			
				// in case the degree really exists
				List degreeCurricularPlans = null;
		
				try {		
						degreeCurricularPlans = (List) manager.executar(
									userView,
									"ReadDegreeCurricularPlansService",
									args);	
				} catch (FenixServiceException e) {
					throw new FenixActionException(e);
				}		
				Collections.sort(degreeCurricularPlans);
				request.setAttribute("degreeId", degreeId);
				request.setAttribute("infoDegree", degree);					
				request.setAttribute("lista de planos curriculares", degreeCurricularPlans);
				return mapping.findForward("viewDegree");
	}
}
