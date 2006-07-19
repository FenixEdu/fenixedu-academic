package net.sourceforge.fenixedu.presentationTier.Action.spaceManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.space.SpaceInformation;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ManageSpaceResponsibilityDA extends FenixDispatchAction {
    
    public ActionForward showSpaceResponsibility(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {        
        SpaceInformation spaceInformation = getSpaceInformationFromParameter(request);
        setSpaceAndSpaceInfo(request, spaceInformation);
        return mapping.findForward("showSpaceResponsibility");
    }  
    
    private void setSpaceAndSpaceInfo(HttpServletRequest request, final SpaceInformation spaceInformation) {
        request.setAttribute("selectedSpaceInformation", spaceInformation);
        request.setAttribute("selectedSpace", spaceInformation.getSpace());
    }
    
    private SpaceInformation getSpaceInformationFromParameter(final HttpServletRequest request) {
        final String spaceInformationIDString = request.getParameterMap().containsKey("spaceInformationID") ?
                request.getParameter("spaceInformationID")
                : (String) request.getAttribute("spaceInformationID");
        final Integer spaceInformationID = spaceInformationIDString != null ? Integer.valueOf(spaceInformationIDString) : null;
        return rootDomainObject.readSpaceInformationByOID(spaceInformationID);
    }    
}
