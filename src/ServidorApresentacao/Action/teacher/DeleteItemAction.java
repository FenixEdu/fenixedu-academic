/*
 * Created on 7/Abr/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorApresentacao.Action.teacher;

/**
 * @author lmac1
 *
 */
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.gesdis.InfoItem;
import DataBeans.gesdis.InfoSection;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;



public class DeleteItemAction extends FenixAction{
	
	public ActionForward execute(
				ActionMapping mapping,
				ActionForm form,
				HttpServletRequest request,
				HttpServletResponse response)
				throws FenixActionException {
					
		HttpSession session = request.getSession(false);
	
		UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
		
		InfoItem infoItem = (InfoItem) session.getAttribute(SessionConstants.INFO_ITEM);
		
		
		try {
			Object deleteItemArguments[] = { infoItem };
			GestorServicos manager = GestorServicos.manager();
			Boolean result = (Boolean) manager.executar(userView, "DeleteItem", deleteItemArguments);

			session.removeAttribute(SessionConstants.INFO_ITEM);
			session.removeAttribute(SessionConstants.INFO_SECTION_ITEMS_LIST);
			
			InfoSection infoSection = infoItem.getInfoSection();
			Object readItemArguments[] = { infoSection };
			List allInfoItens = (List) manager.executar(null, "ReadItems", readItemArguments);
			
			Collections.sort(allInfoItens);
			session.setAttribute(SessionConstants.INFO_SECTION_ITEMS_LIST, allInfoItens);	
			
			
			Iterator iterator = allInfoItens.iterator();
						while (iterator.hasNext()){
							InfoItem infoItem2 = (InfoItem) iterator.next();
							System.out.println("infoItem2: " + infoItem2);
						}
		    
		    
        	
			        return mapping.findForward("AccessItemManagement");
		          
		   }
			catch (FenixServiceException fenixServiceException){
					   throw new FenixActionException(fenixServiceException.getMessage());
				   }
			
		}			
			
}
