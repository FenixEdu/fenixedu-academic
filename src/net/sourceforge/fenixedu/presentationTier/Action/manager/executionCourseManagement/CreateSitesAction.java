package net.sourceforge.fenixedu.presentationTier.Action.manager.executionCourseManagement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class CreateSitesAction extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixFilterException, FenixServiceException {
        final IUserView userView = SessionUtils.getUserView(request);

        final Integer executionCourseId = Integer.valueOf((request.getParameter("executionPeriodID")));
        final Integer numberCreatedSites = (Integer) ServiceManagerServiceFactory.executeService(
                userView, "CreateSites", new Object[] { executionCourseId });

        request.setAttribute("numberCreatedSites", numberCreatedSites);

        return mapping.findForward("ShowCreatedSites");
    }

}