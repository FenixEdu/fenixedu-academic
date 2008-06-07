/*
 * Created on Feb 23, 2005
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.projectsManagement;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoRubric;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.security.UserView;

/**
 * @author Susana Fernandes
 * 
 */
public class InstitucionalProjectManagerIndexAction extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixFilterException, FenixServiceException {

        final IUserView userView = UserView.getUser();

        ServiceManagerServiceFactory.executeService( "ReviewProjectAccess", new Object[] {
                userView.getPerson(), mapping.getModuleConfig().getPrefix() });
        List<InfoRubric> infoCostCenterList = (List) ServiceManagerServiceFactory.executeService(
                userView, "ReadUserCostCenters", new Object[] { userView.getPerson(),
                        mapping.getModuleConfig().getPrefix() });
        request.setAttribute("infoCostCenterList", infoCostCenterList);
        request.setAttribute("infoCostCenter", new InfoRubric());
        return mapping.findForward("success");
    }
}
