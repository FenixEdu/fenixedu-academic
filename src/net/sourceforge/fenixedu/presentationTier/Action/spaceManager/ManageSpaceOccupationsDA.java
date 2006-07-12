package net.sourceforge.fenixedu.presentationTier.Action.spaceManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.space.SpaceInformation;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ManageSpaceOccupationsDA extends FenixDispatchAction {

    public ActionForward showSpaceOccupations(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {        
        final SpaceInformation spaceInformation = getSpaceInformationFromParameter(request);
        request.setAttribute("selectedSpaceInformation", spaceInformation);
        request.setAttribute("selectedSpace", spaceInformation.getSpace());
        return mapping.findForward("showSpaceOccupations");
    }    
    
    private SpaceInformation getSpaceInformationFromParameter(final HttpServletRequest request) {
        final String spaceInformationIDString = request.getParameterMap().containsKey("spaceInformationID") ?
                request.getParameter("spaceInformationID")
                : (String) request.getAttribute("spaceInformationID");
        final Integer spaceInformationID = spaceInformationIDString != null ? Integer.valueOf(spaceInformationIDString) : null;
        return rootDomainObject.readSpaceInformationByOID(spaceInformationID);
    }
    
    public ActionForward addSpacePersonOccupation(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {                       
        return mapping.findForward("addSpacePersonOccupation");
    }
}
