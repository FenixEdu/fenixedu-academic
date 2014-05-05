package net.sourceforge.fenixedu.presentationTier.Action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.util.FenixConfigurationManager;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.domain.MenuItem;
import org.fenixedu.bennu.portal.domain.PortalConfiguration;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/home")
public class HomeAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final MenuItem initialMenuEntry = findTopLevelContainer();
        if (initialMenuEntry == null) {
            response.sendRedirect(FenixConfigurationManager.getConfiguration().getIndexPageRedirect());
        } else {
            response.sendRedirect(request.getContextPath() + initialMenuEntry.getFullPath());
        }

        return null;
    }

    private MenuItem findTopLevelContainer() {
        for (MenuItem item : PortalConfiguration.getInstance().getMenu().getOrderedChild()) {
            if (item.isAvailableForCurrentUser() && item.isVisible()) {
                return item;
            }
        }
        return null;
    }

}
