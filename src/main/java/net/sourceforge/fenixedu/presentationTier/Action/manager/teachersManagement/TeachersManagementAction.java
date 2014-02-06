package net.sourceforge.fenixedu.presentationTier.Action.manager.teachersManagement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;

/**
 * @author Fernanda Quitério 4/Dez/2003
 * 
 */
public class TeachersManagementAction extends FenixDispatchAction {
    @EntryPoint
    public ActionForward firstPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return mapping.findForward("firstPage");
    }

    public ActionForward mainPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("mainPage");
    }
}