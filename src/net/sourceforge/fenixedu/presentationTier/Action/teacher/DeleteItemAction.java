/*
 * Created on 7/Abr/2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.teacher;

/**
 * @author lmac1
 *  
 */
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import net.sourceforge.fenixedu.dataTransferObject.InfoItem;
import net.sourceforge.fenixedu.dataTransferObject.InfoSection;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.UserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

public class DeleteItemAction extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        HttpSession session = request.getSession(false);

        UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
        String indexString = request.getParameter("index");
        Integer index = new Integer(indexString);

        List infoItemsList = (List) session.getAttribute(SessionConstants.INFO_SECTION_ITEMS_LIST);

        InfoItem infoItem = (InfoItem) infoItemsList.get(index.intValue());

        try {
            Object deleteItemArguments[] = { infoItem };

            ServiceManagerServiceFactory.executeService(userView, "DeleteItem", deleteItemArguments);

            session.removeAttribute(SessionConstants.INFO_SECTION_ITEMS_LIST);
            InfoSection infoSection = infoItem.getInfoSection();
            Object readItensArguments[] = { infoSection };
            List allInfoItens = (List) ServiceManagerServiceFactory.executeService(null, "ReadItems",
                    readItensArguments);

            Collections.sort(allInfoItens);
            session.setAttribute(SessionConstants.INFO_SECTION_ITEMS_LIST, allInfoItens);

            return mapping.findForward("AccessSectionManagement");

        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }

    }

}