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
import org.apache.struts.action.DynaActionForm;

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
				DynaActionForm readDegreeForm = (DynaActionForm) form;
						
				UserView userView =
					(UserView) session.getAttribute(SessionConstants.U_VIEW);
				Integer degreeId = (Integer) readDegreeForm.get("degreeId");
				Object args[] = { degreeId };
				
				GestorServicos manager = GestorServicos.manager();
				InfoDegree degree = null;
				
				try {
						degree = (InfoDegree) manager.executar(userView, "ReadDegreeService", args);
				} catch(FenixServiceException e) {
					throw new FenixActionException(e);
			    }
			    
			    //Caso em que se tenta ler 1 curso que não existe na base de dados
				if(degree == null) {
						ActionErrors actionErrors = new ActionErrors();
						ActionError error = new ActionError("message.nonExistingDegree");
						actionErrors.add("message.nonExistingDegree", error);
						saveErrors(request, actionErrors);
						return mapping.findForward("readDegrees");
				}
				
				//Caso tudo corra como esperado, i.e., o curso existe mesmo
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
				System.out.println("ANTES----DEGREEIDaaaaaaaaaaaaaaaaaaaAA"+degreeId);
				request.setAttribute("degreeId",degreeId);
				request.setAttribute("infoDegree", degree);					
				request.setAttribute(SessionConstants.INFO_DEGREE_CURRICULAR_PLANS_LIST, degreeCurricularPlans);
				return mapping.findForward("viewDegree");
	}
}
