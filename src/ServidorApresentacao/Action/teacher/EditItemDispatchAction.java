/*
 * Created on 8/Abr/2003
 *
 */
package ServidorApresentacao.Action.teacher;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoItem;
import DataBeans.InfoSection;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

public class EditItemDispatchAction extends FenixDispatchAction
{

    public ActionForward prepareEdit(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {

        String indexString = request.getParameter("index");

        Integer index = new Integer(indexString);

        HttpSession session = request.getSession(false);

        List infoItemsList = (List) session.getAttribute(SessionConstants.INFO_SECTION_ITEMS_LIST);

        InfoItem oldInfoItem = (InfoItem) infoItemsList.get(index.intValue());
        session.setAttribute(SessionConstants.INFO_ITEM, oldInfoItem);

        //		GestorServicos manager = GestorServicos.manager();
        //		Object readSectionArgs[] = { oldInfoItem.getInfoSection() };
        //		ArrayList items;
        //		try {
        //			infoItemsList =(ArrayList) manager.executar(null,"ReadItems",readSectionArgs);
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

    public ActionForward edit(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {

        DynaActionForm itemForm = (DynaActionForm) form;
        HttpSession session = request.getSession(false);

        UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);

        InfoSection infoSection = (InfoSection) session.getAttribute(SessionConstants.INFO_SECTION);

        InfoItem oldInfoItem = (InfoItem) session.getAttribute(SessionConstants.INFO_ITEM);

        //InfoItem newInfoItem = (InfoItem) session.getAttribute(SessionConstants.INFO_ITEM);
        GestorServicos manager = GestorServicos.manager();
        Object readSectionArgs[] = { infoSection };
        ArrayList items;
        try
        {
            items = (ArrayList) manager.executar(null, "ReadItems", readSectionArgs);
        } catch (FenixServiceException fenixServiceException)
        {
            throw new FenixActionException(fenixServiceException.getMessage());
        }

        InfoItem newInfoItem = new InfoItem();
        newInfoItem.setInfoSection(infoSection);
        newInfoItem.setInformation((String) itemForm.get("information"));
        Integer order = new Integer((String) itemForm.get("itemOrder"));
        if (items != null && items.size() != 0)
        {

            switch (order.intValue())
            {
                case -1 :
                    order = new Integer(items.size() - 1);
                    break;

                default :
                    order = new Integer(order.intValue() - 1);
                    break;
            }
        } else
        {
            order = new Integer(0);
        }
        newInfoItem.setItemOrder(order);
        newInfoItem.setName((String) itemForm.get("name"));
        newInfoItem.setUrgent(new Boolean((String) itemForm.get("urgent")));

        Object editItemArgs[] = { oldInfoItem, newInfoItem };

        try
        {
            manager.executar(userView, "EditItem", editItemArgs);
        } catch (ExistingServiceException e)
        {
            throw new ExistingActionException("Um item com esse nome", e);
        } catch (FenixServiceException fenixServiceException)
        {

            throw new FenixActionException(fenixServiceException.getMessage());
        }

        session.setAttribute(SessionConstants.INFO_ITEM, newInfoItem);

        //			read section items 

        try
        {
            items = (ArrayList) manager.executar(null, "ReadItems", readSectionArgs);
        } catch (FenixServiceException fenixServiceException)
        {
            throw new FenixActionException(fenixServiceException.getMessage());
        }

        session.setAttribute(SessionConstants.INFO_SECTION_ITEMS_LIST, items);

        return mapping.findForward("viewSection");
    }
}
