package net.sourceforge.fenixedu.presentationTier.Action.spaceManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.PersonSpaceOccupation;
import net.sourceforge.fenixedu.domain.space.SpaceInformation;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ManageSpaceOccupationsDA extends FenixDispatchAction {

    public ActionForward showSpaceOccupations(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {        
        final SpaceInformation spaceInformation = getSpaceInformationFromParameter(request);
        setSpaceAndSpaceInfo(request, spaceInformation);
        return mapping.findForward("showSpaceOccupations");
    }          
           
    public ActionForward prepareEditSpacePersonOccupation(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
        final PersonSpaceOccupation personSpaceOccupation = getPersonSpaceOccupationFromParameter(request);
        final SpaceInformation spaceInformation = getSpaceInformationFromParameter(request);               
        setSpaceAndSpaceInfo(request, spaceInformation);
        request.setAttribute("personSpaceOccupation", personSpaceOccupation);
        return showSpaceOccupations(mapping, form, request, response);
    }
    
    public ActionForward editSpacePersonOccupation(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
        final PersonSpaceOccupation personSpaceOccupation = getPersonSpaceOccupationFromParameter(request);
        final SpaceInformation spaceInformation = getSpaceInformationFromParameter(request);               
        Object[] args = { personSpaceOccupation };
        try {
            ServiceUtils.executeService(getUserView(request), "EditPersonSpaceOccupation", args);
        } catch(DomainException domainException) {
            
        }
        setSpaceAndSpaceInfo(request, spaceInformation);    
        return mapping.findForward("showSpaceOccupations");
    }
    
    public ActionForward deleteSpacePersonOccupation(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
        final PersonSpaceOccupation personSpaceOccupation = getPersonSpaceOccupationFromParameter(request);
        final SpaceInformation spaceInformation = getSpaceInformationFromParameter(request);                          
        Object[] args = { personSpaceOccupation };        
        ServiceUtils.executeService(getUserView(request), "DeletePersonSpaceOccupation", args);         
        setSpaceAndSpaceInfo(request, spaceInformation);        
        return mapping.findForward("showSpaceOccupations");
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
    
    private PersonSpaceOccupation getPersonSpaceOccupationFromParameter(final HttpServletRequest request) {
        final String personSpaceOccupationIDString = request.getParameterMap().containsKey("spaceOccupationID") ?
                request.getParameter("spaceOccupationID")
                : (String) request.getAttribute("spaceOccupationID");
        final Integer personSpaceOccupationID = personSpaceOccupationIDString != null ? Integer.valueOf(personSpaceOccupationIDString) : null;
        return (PersonSpaceOccupation) rootDomainObject.readSpaceOccupationByOID(personSpaceOccupationID);
    }
}
