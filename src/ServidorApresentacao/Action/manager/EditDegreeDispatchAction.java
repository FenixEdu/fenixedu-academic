/*
 * Created on 28/Jul/2003
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
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.TipoCurso;

/**
 * @author lmac1
 */

public class EditDegreeDispatchAction extends FenixDispatchAction {


	public ActionForward prepareEdit(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws FenixActionException {
		
			HttpSession session = request.getSession(false);
			DynaActionForm readDegreeForm = (DynaActionForm) form;
			
			UserView userView =
				(UserView) session.getAttribute(SessionConstants.U_VIEW);
				
			Integer degreeId =new Integer(request.getParameter("degreeId"));
			
			InfoDegree oldInfoDegree = null;

			Object args[] = { degreeId };
			GestorServicos manager = GestorServicos.manager();
			try{
				oldInfoDegree = (InfoDegree) manager.executar(userView, "ReadDegreeService", args);
			}catch (FenixServiceException fenixServiceException) {
			throw new FenixActionException(fenixServiceException.getMessage());
			}
			
			TipoCurso degreeType =(TipoCurso) oldInfoDegree.getTipoCurso();
	
			
			
			readDegreeForm.set("name",(String) oldInfoDegree.getNome());
			readDegreeForm.set("code",(String) oldInfoDegree.getSigla());
			readDegreeForm.set("degreeType",degreeType.getTipoCurso());
			readDegreeForm.set("degreeId",degreeId);
			

			request.setAttribute("degreeId",degreeId);
			return mapping.findForward("editDegree");
		}

//	COMO +ONHO O ID EM SESSAO E DEFINO NUM BEAN DO JSP DEVIA IR BUSCÁ-LO AO REQUEST DO EDIT E 
//  TIRÁLO TB DO SET DO PREPARE

	public ActionForward edit(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
	
		HttpSession session = request.getSession(false);
		DynaActionForm editDegreeForm = (DynaActionForm) form;
			
		UserView userView =
				(UserView) session.getAttribute(SessionConstants.U_VIEW);
				
//		Integer oldDegreeId = (Integer)request.getAttribute("degreeId");
		
		Integer oldDegreeId = (Integer) editDegreeForm.get("degreeId");
		System.out.println("DEGREEIDaaaaaaaaaaaaaaaaaaaAA"+oldDegreeId);	
		
		String code = (String) editDegreeForm.get("code");
		String name = (String) editDegreeForm.get("name");
		Integer degreeTypeInt = (Integer) editDegreeForm.get("degreeType");
		TipoCurso degreeType = new TipoCurso(degreeTypeInt);
		
		InfoDegree newInfoDegree = new InfoDegree(
												code,
												name,
												degreeType);
		
		Object args[] = { oldDegreeId, newInfoDegree };
		GestorServicos manager = GestorServicos.manager();
		List serviceResult = null;
		try {
				serviceResult = (List) manager.executar(userView, "EditDegreeService", args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e.getMessage());
		}

		try {	
				List degrees = null;
				degrees = (List) manager.executar(userView,"ReadDegreesService",null);
				if (serviceResult != null) {
					ActionErrors actionErrors = new ActionErrors();
					ActionError error = null;
					if(serviceResult.get(0) != null) {
						error = new ActionError("message.existingDegreeCode", serviceResult.get(0));
						actionErrors.add("message.existingDegreeCode", error);
					}	
					if(serviceResult.get(1) != null)
					{
						error = new ActionError("message.existingDegreeName", serviceResult.get(1));
						actionErrors.add("message.existingDegreeName", error);
					}			
					saveErrors(request, actionErrors);
				}
				Collections.sort(degrees);
				request.setAttribute(SessionConstants.INFO_DEGREES_LIST,degrees);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		return mapping.findForward("readDegrees");
	}
	
	
				
}

