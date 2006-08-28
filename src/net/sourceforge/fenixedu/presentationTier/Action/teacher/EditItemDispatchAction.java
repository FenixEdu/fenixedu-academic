/*
 * Created on 8/Abr/2003
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

public class EditItemDispatchAction extends FenixDispatchAction {

    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        String indexString = request.getParameter("index");

        Integer index = new Integer(indexString);

        HttpSession session = request.getSession(false);

        List infoItemsList = (List) session.getAttribute(SessionConstants.INFO_SECTION_ITEMS_LIST);

        InfoItem oldInfoItem = (InfoItem) infoItemsList.get(index.intValue());
        session.setAttribute(SessionConstants.INFO_ITEM, oldInfoItem);

        //		GestorServicos manager = GestorServicos.manager();
        //		Object readSectionArgs[] = { oldInfoItem.getInfoSection() };
        //		List items;
        //		try {
        //			infoItemsList =(ArrayList)
        // ServiceManagerServiceFactory.executeService(null,"ReadItems",readSectionArgs);
        //			} catch (FenixServiceException fenixServiceException) {
        //				throw new FenixActionException(fenixServiceException.getMessage());
        //			}
        //			
        //		//infoItemsList.remove(oldInfoItem);
        //			
        //		
        //		
        //	 
        //	   session.setAttribute(SessionConstants.INFO_SECTION_ITEMS_LIST,infoItemsList);
        return mapping.findForward("editItem");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        DynaActionForm itemForm = (DynaActionForm) form;
        HttpSession session = request.getSession(false);

        IUserView userView = getUserView(request);

        InfoSection infoSection = (InfoSection) session.getAttribute(SessionConstants.INFO_SECTION);

        InfoItem oldInfoItem = (InfoItem) session.getAttribute(SessionConstants.INFO_ITEM);

        //InfoItem newInfoItem = (InfoItem)
        // session.getAttribute(SessionConstants.INFO_ITEM);
        Object readSectionArgs[] = { infoSection };
        List items;
        try {
            items = (ArrayList) ServiceManagerServiceFactory.executeService(null, "ReadItems",
                    readSectionArgs);
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }

        InfoItem newInfoItem = new InfoItem();
        newInfoItem.setInfoSection(infoSection);
        newInfoItem.setInformation((String) itemForm.get("information"));
        Integer order = new Integer((String) itemForm.get("itemOrder"));
        if (items != null && items.size() != 0) {

            switch (order.intValue()) {
            case -1:
                order = new Integer(items.size() - 1);
                break;

            default:
                order = new Integer(order.intValue() - 1);
                break;
            }
        } else {
            order = new Integer(0);
        }
        newInfoItem.setItemOrder(order);
        newInfoItem.setName((String) itemForm.get("name"));

        Object editItemArgs[] = { oldInfoItem, newInfoItem };

        try {
            ServiceManagerServiceFactory.executeService(userView, "EditItem", editItemArgs);
        } catch (ExistingServiceException e) {
            throw new ExistingActionException("Um item com esse nome", e);
        } catch (FenixServiceException fenixServiceException) {

            throw new FenixActionException(fenixServiceException.getMessage());
        }

        session.setAttribute(SessionConstants.INFO_ITEM, newInfoItem);

        //			read section items

        try {
            items = (ArrayList) ServiceManagerServiceFactory.executeService(null, "ReadItems",
                    readSectionArgs);
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }

        session.setAttribute(SessionConstants.INFO_SECTION_ITEMS_LIST, items);

        return mapping.findForward("viewSection");
    }
}