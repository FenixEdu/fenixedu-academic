package net.sourceforge.fenixedu.presentationTier.Action.spaceManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ManageSpaceResponsabilityDA extends FenixDispatchAction {

    
    public ActionForward showSpaceResponsability(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {        
           
        
        return mapping.findForward("showSpaceResponsability");
    }  
    
}
