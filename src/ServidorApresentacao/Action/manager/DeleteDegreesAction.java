package ServidorApresentacao.Action.manager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.CantDeleteServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.CantDeleteActionException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author lmac1
 */
public class DeleteDegreesAction extends FenixAction{
	
	public ActionForward execute(
				ActionMapping mapping,
				ActionForm form,
				HttpServletRequest request,
				HttpServletResponse response)
				throws FenixActionException {
					
		HttpSession session = request.getSession(false);
		DynaActionForm deleteDegreesForm = (DynaActionForm) form;
		
		UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
		List infoDegrees =(List) session.getAttribute(
								SessionConstants.INFO_DEGREES_LIST);
		
		List degreesInternalIds = Arrays.asList((Integer [])deleteDegreesForm.get("internalIds"));

		//ESTE SERVICO PASSA A RECEBER O CODIGO INTERNO EM VEZ DO INFO DEGREE
		
		Object args[] = { degreesInternalIds };
		GestorServicos manager = GestorServicos.manager();
		
		try {
			Boolean result = (Boolean) manager.executar(userView, "DeleteDegreesService", args);
		session.removeAttribute(SessionConstants.INFO_DEGREES_LIST);	
			System.out.println("APAGOU OU NAO"+result);
		} 
		catch (CantDeleteServiceException e) {
     	    throw new CantDeleteActionException(e);
     	     }
		catch (FenixServiceException fenixServiceException) {
	    	throw new FenixActionException(fenixServiceException.getMessage());
			}
			
	
			List allInfoDegrees;
		try{
			
			allInfoDegrees = (List) manager.executar(userView, "ReadDegreesService",null);
			
		   }
			catch (FenixServiceException fenixServiceException){
			   throw new FenixActionException(fenixServiceException.getMessage());
		   }
		     
		    Collections.sort(allInfoDegrees);
	       session.setAttribute(SessionConstants.INFO_DEGREES_LIST, allInfoDegrees);
		    
		    
        	
		 return mapping.findForward("readDegrees");

			
		}			
			
}

