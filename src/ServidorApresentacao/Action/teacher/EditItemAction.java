/*
 * Created on 8/Abr/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package ServidorApresentacao.Action.teacher;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.gesdis.InfoItem;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

public class EditItemAction extends FenixAction {
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		try {
			DynaActionForm itemForm = (DynaActionForm) form;
			SessionUtils.validSessionVerification(request, mapping);

			HttpSession session = request.getSession(false);

			String indexString = (String) request.getParameter("index");
			Integer index = new Integer(indexString);

			UserView userView =
				(UserView) session.getAttribute(SessionConstants.U_VIEW);
			List infoItemsList =
				(List) session.getAttribute(
					SessionConstants.INFO_SECTION_ITEMS_LIST);

			InfoItem oldInfoItem =
				(InfoItem) infoItemsList.get(index.intValue());

			session.setAttribute(SessionConstants.INFO_ITEM, oldInfoItem);

			InfoItem newInfoItem = new InfoItem();
			BeanUtils.copyProperties(newInfoItem, oldInfoItem);

			newInfoItem.setInformation((String) itemForm.get("information"));
			newInfoItem.setItemOrder((Integer) itemForm.get("itemOrder"));
			newInfoItem.setName((String) itemForm.get("itemName"));
			newInfoItem.setUrgent((Boolean) itemForm.get("urgent"));

			Object editItemArgs[] = { oldInfoItem, newInfoItem };

			GestorServicos manager = GestorServicos.manager();
			Boolean result =
				(Boolean) manager.executar(userView, "EditItem", editItemArgs);

			if (result.booleanValue()) {
				session.setAttribute(SessionConstants.INFO_ITEM, newInfoItem);
			} else {
				mapping.getInputForward();
				//TODO: error message required. verify which  error
			}

		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		} catch (IllegalAccessException e) {
			throw new FenixActionException(e);
		} catch (InvocationTargetException e) {
			throw new FenixActionException(e);
		}
		return mapping.findForward("viewProgram");
	}
}
