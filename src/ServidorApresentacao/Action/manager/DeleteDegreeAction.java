package ServidorApresentacao.Action.manager;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoDegree;
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
public class DeleteDegreeAction extends FenixAction{
	
	public ActionForward execute(
				ActionMapping mapping,
				ActionForm form,
				HttpServletRequest request,
				HttpServletResponse response)
				throws FenixActionException {
					
		HttpSession session = request.getSession(false);
	
		UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
		String indexString = (String) request.getParameter("index");
		Integer index = new Integer(indexString);
		
		List infoDegrees =
							(List) session.getAttribute(
								SessionConstants.INFO_DEGREES_LIST);
								
		
		InfoDegree infoDegree = (InfoDegree) infoDegrees.get(index.intValue());		
		
		Object deleteDegreeArguments[] = { infoDegree };
		GestorServicos manager = GestorServicos.manager();
		try {
			
			Boolean result = (Boolean) manager.executar(userView, "DeleteDegreeService", deleteDegreeArguments);
			session.removeAttribute(SessionConstants.INFO_DEGREES_LIST);
			
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

