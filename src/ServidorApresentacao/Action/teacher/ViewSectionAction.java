/*
 * Created on 7/Abr/2003
 *
 */
package ServidorApresentacao.Action.teacher;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoSection;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author asnr and scpo
 */

public class ViewSectionAction extends FenixAction
{

    public ActionForward execute(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {

        HttpSession session = request.getSession(false);

        String indexString = request.getParameter("index");

        if (indexString != null)
        {

            Integer index = new Integer(indexString);

            List sectionsList = (List) session.getAttribute(SessionConstants.SECTIONS);

            InfoSection infoSection = (InfoSection) sectionsList.get(index.intValue());

            session.setAttribute(SessionConstants.INFO_SECTION, infoSection);

            Object argsViewSection[] = { infoSection };

			List infoItems =
				(List) ServiceManagerServiceFactory.executeService(null, "ReadItems", argsViewSection);

            session.setAttribute(SessionConstants.INFO_SECTION_ITEMS_LIST, infoItems);

        }
        return mapping.findForward("Sucess");

    }
}
