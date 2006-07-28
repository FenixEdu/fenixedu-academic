package net.sourceforge.fenixedu.presentationTier.Action.spaceManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

public class ManageMaterialSpaceOccupationsDA extends FenixDispatchAction {

    public ActionForward showMaterialSpaceOccupation(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        
        
        
        return mapping.findForward("showMaterialSpaceOccupation");
    }
}
