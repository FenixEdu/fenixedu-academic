/*
 * Created on 11/Abr/2003
 */
/**
 * @author lmac1
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoItem;
import net.sourceforge.fenixedu.dataTransferObject.InfoSection;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;

public class InsertItemDispatchAction extends FenixDispatchAction {

    public ActionForward prepareInsert(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        //		HttpSession session = request.getSession(false);
        //
        //		InfoSection infoSection =
        //			(InfoSection) session.getAttribute(SessionConstants.INFO_SECTION);
        //		List infoItemsList = null;
        //
        //		Object readSectionArgs[] = { infoSection };
        //
        //		try {
        //			infoItemsList =
        //				(ArrayList) manager.executar(
        //					null,
        //					"ReadItems",
        //					readSectionArgs);
        //		} catch (FenixServiceException fenixServiceException) {
        //			throw new FenixActionException(fenixServiceException.getMessage());
        //		}
        //
        //		session.setAttribute(
        //			SessionConstants.INFO_SECTION_ITEMS_LIST,
        //			infoItemsList);

        return mapping.findForward("insertItem");
    }

    public ActionForward insert(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        HttpSession session = request.getSession(false);
        IUserView userView = getUserView(request);
        DynaActionForm dynaForm = (DynaValidatorForm) form;
        InfoSection infoSection = (InfoSection) session.getAttribute(SessionConstants.INFO_SECTION);

        List items = (List) session.getAttribute(SessionConstants.INFO_SECTION_ITEMS_LIST);
        //PUSEMOS EM COMENTARIO

        //		List infoItemsList = null;
        //		Object readSectionArgs[] = { infoSection };
        //		List items;
        //		try {
        //			items =
        //				(ArrayList) manager.executar(
        //					null,
        //					"ReadItems",
        //					readSectionArgs);
        //		} catch (FenixServiceException fenixServiceException) {
        //			throw new FenixActionException(fenixServiceException.getMessage());
        //		}

        InfoItem newInfoItem = new InfoItem();

        Integer order = new Integer((String) dynaForm.get("itemOrder"));
        switch (order.intValue()) {
        case -1:
            order = new Integer(items.size());
            break;

        default:
            order = new Integer(order.intValue());
            break;
        }
        newInfoItem.setItemOrder(order);
        String itemName = (String) dynaForm.get("name");

        String information = (String) dynaForm.get("information");
        String urgentString = (String) dynaForm.get("urgent");

        InfoItem infoItem = new InfoItem(information, itemName, order, infoSection);

        Object args[] = { infoItem };

        try {
            ServiceManagerServiceFactory.executeService(userView, "InsertItem", args);
        } catch (ExistingServiceException e) {
            throw new ExistingActionException("Um item com esse nome", e);
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }

        Object args1[] = { infoSection };
        List infoItems;
        try {
            infoItems = (ArrayList) ServiceManagerServiceFactory
                    .executeService(null, "ReadItems", args1);

        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }

        session.setAttribute(SessionConstants.INFO_SECTION_ITEMS_LIST, infoItems);

        return mapping.findForward("viewSection");

    }
}