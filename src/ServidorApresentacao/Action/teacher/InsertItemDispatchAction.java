/*
 * Created on 11/Abr/2003
 */
/**
 * @author lmac1
 *
 */
package ServidorApresentacao.Action.teacher;

import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.gesdis.InfoItem;
import DataBeans.gesdis.InfoSection;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;


public class InsertItemDispatchAction extends FenixDispatchAction {

	public ActionForward prepareInsert(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		return mapping.findForward("insertItem");
			} 


	public ActionForward insert(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		DynaActionForm dynaForm = (DynaValidatorForm) form;
		String itemName = (String) dynaForm.get("itemName");
		Integer itemOrder = (Integer) dynaForm.get("itemOrder");
    	String information = (String) dynaForm.get("information");
		Boolean urgent = (Boolean) dynaForm.get("urgent");
		
		HttpSession session = request.getSession();
		UserView userView =(UserView) session.getAttribute(SessionConstants.U_VIEW);
		InfoSection infoSection =(InfoSection) session.getAttribute(SessionConstants.INFO_SECTION);
	
//		String indexString = (String) request.getParameter("index");
//					Integer index = new Integer(indexString);
//		
//		List infoItemsList = (List) session.getAttribute(
//						SessionConstants.INFO_SECTION_ITEMS_LIST);
//		
//		InfoItem infoItem = (InfoItem) infoItemsList.get(index.intValue());		
//		
		

			InfoItem infoItem =
				new InfoItem(information,itemName, itemOrder, infoSection, urgent);
			

			Object args[] = { infoItem};
			GestorServicos manager = GestorServicos.manager();
			try {
				Boolean result = (Boolean) manager.executar(userView, "InsertItem", args);
			} catch (FenixServiceException fenixServiceException) {
				throw new FenixActionException(
					fenixServiceException.getMessage());
			}
			Object args1[] = { infoSection };
			ArrayList infoItems;
			try {
				infoItems =
					(ArrayList) manager.executar(
						null,
						"ReadItems",
						args1);
			} catch (FenixServiceException fenixServiceException) {
				throw new FenixActionException(
					fenixServiceException.getMessage());
			}

			Collections.sort(infoItems);
			session.setAttribute(SessionConstants.INFO_SECTION_ITEMS_LIST, infoItems);
			
			return mapping.findForward("viewSection"); 
					
	}
}
