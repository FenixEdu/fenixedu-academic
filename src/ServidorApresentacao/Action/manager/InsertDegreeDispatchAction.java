/*
 * Created on 30/Mai/2003
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
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoDegree;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.TipoCurso;

/**
 * @author lmac1
 */
public class InsertDegreeDispatchAction extends FenixAction {


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

		DynaActionForm dynaForm = (DynaValidatorForm) form;
		String code = (String) dynaForm.get("code");
		String name = (String) dynaForm.get("name");
		String tipoCursoString = (String) dynaForm.get("tipoCurso");
		
		Integer tipoCursoInt = new Integer(tipoCursoString);
		
		TipoCurso tipoCurso =new TipoCurso(tipoCursoInt);
		
		System.out.println("TIPO DE CURSO"+tipoCursoInt);
		
		HttpSession session = request.getSession(false);

		UserView userView =
			(UserView) session.getAttribute(SessionConstants.U_VIEW);


    	InfoDegree infoDegree =
				new InfoDegree(
					code,
					name,
					tipoCurso);

		Object args[] = { infoDegree };
		GestorServicos manager = GestorServicos.manager();
		try {
			manager.executar(userView, "InsertDegreeService", args);
		}
		//FAZER TB PARA JAH EXISTE COM ESSA SIGLA 
		catch (ExistingServiceException e) {
						   throw new ExistingActionException("Uma secção com esse nome",e);
					   }
		catch (FenixServiceException e) {
			throw new FenixActionException(e.getMessage());
		}

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
