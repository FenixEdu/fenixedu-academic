/*
 * Created on 30/Mai/2003
 */
package ServidorApresentacao.Action.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoDegree;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.TipoCurso;

/**
 * @author lmac1
 */
public class InsertDegreeDispatchAction extends FenixDispatchAction {


	public ActionForward prepareInsert(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws FenixActionException {

			return mapping.findForward("insertDegree");
		}

	public ActionForward insert(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
	
		HttpSession session = request.getSession(false);
    	UserView userView =	(UserView) session.getAttribute(SessionConstants.U_VIEW);
    	
		DynaActionForm dynaForm = (DynaValidatorForm) form;
		String code = (String) dynaForm.get("code");
		String name = (String) dynaForm.get("name");
		Integer degreeTypeInt = (Integer) dynaForm.get("degreeType");
				
		TipoCurso degreeType = new TipoCurso(degreeTypeInt);
    	InfoDegree infoDegree = new InfoDegree(code, name, degreeType);
    	
		Object args[] = { infoDegree };
		GestorServicos manager = GestorServicos.manager();
		
		try {
				manager.executar(userView, "InsertDegree", args);
				
		} catch (ExistingServiceException ex) {
			throw new ExistingActionException("Um curso com esses dados", ex);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e.getMessage());
		}

		return mapping.findForward("readDegrees");
	}			
}
