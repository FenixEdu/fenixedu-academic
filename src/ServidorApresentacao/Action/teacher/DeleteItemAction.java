/*
 * Created on 7/Abr/2003
 *
 */
package ServidorApresentacao.Action.teacher;

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

import DataBeans.InfoItem;
import DataBeans.InfoSection;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

public class DeleteItemAction extends FenixAction
{

    public ActionForward execute(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {

        HttpSession session = request.getSession(false);

        UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
        String indexString = request.getParameter("index");
        Integer index = new Integer(indexString);

        List infoItemsList = (List) session.getAttribute(SessionConstants.INFO_SECTION_ITEMS_LIST);

        InfoItem infoItem = (InfoItem) infoItemsList.get(index.intValue());

        try
        {
            Object deleteItemArguments[] = { infoItem };
            GestorServicos manager = GestorServicos.manager();

            manager.executar(userView, "DeleteItem", deleteItemArguments);

            session.removeAttribute(SessionConstants.INFO_SECTION_ITEMS_LIST);
            InfoSection infoSection = infoItem.getInfoSection();
            Object readItensArguments[] = { infoSection };
            List allInfoItens = (List) manager.executar(null, "ReadItems", readItensArguments);

            Collections.sort(allInfoItens);
            session.setAttribute(SessionConstants.INFO_SECTION_ITEMS_LIST, allInfoItens);

            return mapping.findForward("AccessSectionManagement");

        } catch (FenixServiceException fenixServiceException)
        {
            throw new FenixActionException(fenixServiceException.getMessage());
        }

    }

}
