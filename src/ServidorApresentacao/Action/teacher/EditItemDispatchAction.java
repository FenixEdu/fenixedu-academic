/*
 * Created on 8/Abr/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package ServidorApresentacao.Action.teacher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.gesdis.InfoItem;
import DataBeans.gesdis.InfoSection;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

public class EditItemDispatchAction extends FenixAction {

	public ActionForward prepareEdit(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		return mapping.findForward("editItem");
	}

	public ActionForward edit(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		DynaActionForm itemForm = (DynaActionForm) form;
		SessionUtils.validSessionVerification(request, mapping);

		String indexString = (String) request.getParameter("index");
		Integer index = new Integer(indexString);

		HttpSession session = request.getSession(false);

		UserView userView =
			(UserView) session.getAttribute(SessionConstants.U_VIEW);

		InfoSection infoSection =
			(InfoSection) session.getAttribute(SessionConstants.INFO_SECTION);
		List infoItemsList =
			(List) session.getAttribute(
				SessionConstants.INFO_SECTION_ITEMS_LIST);

		InfoItem oldInfoItem = (InfoItem) infoItemsList.get(index.intValue());

		InfoItem newInfoItem = new InfoItem();

		newInfoItem.setInfoSection(infoSection);
		newInfoItem.setInformation((String) itemForm.get("information"));
		newInfoItem.setItemOrder((Integer) itemForm.get("itemOrder"));
		newInfoItem.setName((String) itemForm.get("itemName"));
		newInfoItem.setUrgent((Boolean) itemForm.get("urgent"));

		Object editItemArgs[] = { oldInfoItem, newInfoItem };

		GestorServicos manager = GestorServicos.manager();
		try {
			manager.executar(userView, "EditItem", editItemArgs);
		} catch (FenixServiceException fenixServiceException) {

			throw new FenixActionException(fenixServiceException.getMessage());
		}
		session.setAttribute(SessionConstants.INFO_ITEM, newInfoItem);

		//			read section items 

		Object readSectionArgs[] = { infoSection };
		ArrayList items;
		try {
			items =
				(ArrayList) manager.executar(
					userView,
					"ReadSection",
					readSectionArgs);
		} catch (FenixServiceException fenixServiceException) {
			throw new FenixActionException(fenixServiceException.getMessage());
		}

		Collections.sort(items);
		session.setAttribute(SessionConstants.INFO_SECTION_ITEMS_LIST, items);

		return mapping.findForward("viewSection");
	}
}
		
		
