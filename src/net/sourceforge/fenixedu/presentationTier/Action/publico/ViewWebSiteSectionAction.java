package net.sourceforge.fenixedu.presentationTier.Action.publico;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.InfoWebSiteSection;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Fernanda Quitério 02/09/2003
 *  
 */
public class ViewWebSiteSectionAction extends FenixContextDispatchAction {

    public ActionForward viewLimitedSection(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {

        String sectionName = request.getParameter("sectionName");

        Object[] args = { sectionName };
        InfoWebSiteSection infoWebSiteSection = null;
        try {
            infoWebSiteSection = (InfoWebSiteSection) ServiceManagerServiceFactory.executeService(null,
                    "ReadLimitedWebSiteSectionByName", args);
        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException("message.nonExisting", sectionName);
        } catch (FenixServiceException e) {
            e.printStackTrace();
            throw new FenixActionException(e.getMessage());
        }

        request.setAttribute("infoWebSiteSection", infoWebSiteSection);

        return mapping.findForward("viewLimitedWebSiteSection");
    }

    public ActionForward viewAllPublishedItemsFromSection(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {

        Integer sectionCode = Integer.valueOf(request.getParameter("objectCode2"));
        Integer itemCode = Integer.valueOf(request.getParameter("objectCode"));

        Object[] args = { sectionCode };
        InfoWebSiteSection infoWebSiteSection = null;
        try {
            infoWebSiteSection = (InfoWebSiteSection) ServiceManagerServiceFactory.executeService(null,
                    "ReadWebSiteSectionByCode", args);
        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException("message.nonExisting");
        } catch (FenixServiceException e) {
            e.printStackTrace();
            throw new FenixActionException(e.getMessage());
        }

        request.setAttribute("objectCode", itemCode);
        request.setAttribute("infoWebSiteSection", infoWebSiteSection);
        return mapping.findForward("viewWebSiteSection");
    }
}