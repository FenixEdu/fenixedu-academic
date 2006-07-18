/*
 * Created on Feb 23, 2005
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.projectsManagement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Susana Fernandes
 * 
 */
public class IndexAction extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixFilterException, FenixServiceException {

        final IUserView userView = SessionUtils.getUserView(request);
        String costCenter = request.getParameter("costCenter");
        ServiceManagerServiceFactory.executeService(userView, "ReviewProjectAccess", new Object[] {
                userView.getPerson(), costCenter });

        if (costCenter != null && !costCenter.equals("")) {
            request.setAttribute("infoCostCenter", ServiceUtils.executeService(userView,
                    "ReadCostCenter", new Object[] { userView.getUtilizador(), costCenter }));
        }
        return mapping.findForward("success");
    }
}
