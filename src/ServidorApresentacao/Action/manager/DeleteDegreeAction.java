/*
 * Created on 16/Mai/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
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

import DataBeans.InfoDegree;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author lmac1
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
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
		
		try {
			Object deleteDegreeArguments[] = { infoDegree };
			GestorServicos manager = GestorServicos.manager();
			
			Boolean result = (Boolean) manager.executar(userView, "DeleteDegreeService", deleteDegreeArguments);

			session.removeAttribute(SessionConstants.INFO_DEGREES_LIST);
			
			List allInfoDegrees = (List) manager.executar(userView, "ReadDegreesService",null);

		    Collections.sort(allInfoDegrees);
			session.setAttribute(SessionConstants.INFO_DEGREES_LIST, allInfoDegrees);
		    
		    
        	
		 return mapping.findForward("readDegrees");
		          
		   }
			catch (FenixServiceException fenixServiceException){
					   throw new FenixActionException(fenixServiceException.getMessage());
				   }
			
		}			
			
}

