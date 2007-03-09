package net.sourceforge.fenixedu.presentationTier.Action.manager.organizationalStructure;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

public class UnitsMergeDA extends FenixDispatchAction{

    public ActionForward chooseUnitToStart(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

	Unit externalInstitutionUnit = UnitUtils.readExternalInstitutionUnit();
	request.setAttribute("externalInstitutionUnit", externalInstitutionUnit);	
	return mapping.findForward("chooseUnit");
    }
    
    public ActionForward seeChoosedUnit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

	Unit externalUnit = getDestinationUnitFromParameter(request);
	if(externalUnit != null) {
            Unit earthUnit = UnitUtils.readEarthUnit();	
            Unit externalInstitutionUnit = UnitUtils.readExternalInstitutionUnit();            
            request.setAttribute("externalInstitutionUnit", externalInstitutionUnit);	
            request.setAttribute("externalUnit", externalUnit);
            request.setAttribute("earthUnit", earthUnit);
	}
	
	return mapping.findForward("seeChoosedUnit");
    }
        
    public ActionForward mergeWithOfficial(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	
	readAndSetUnitsToMerge(request);
	request.setAttribute("official", true);
	return mapping.findForward("goToConfirmation");
    }   

    public ActionForward mergeWithNoOfficialUnits(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	
	readAndSetUnitsToMerge(request);
	request.setAttribute("official", false);
	return mapping.findForward("goToConfirmation");
    }
    
    public ActionForward mergeUnits(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	
	Unit fromUnit = null, destinationUnit = null;		
	IViewState viewState = RenderUtils.getViewState("noOfficialMerge");
	
	if(viewState != null) {
	    fromUnit = getFromUnitFromParameter(request);
	    destinationUnit = getDestinationUnitFromParameter(request);	   
	} else {	    
	    DynaActionForm dynaActionForm = (DynaActionForm) form;
	    Integer fromUnitID = (Integer) dynaActionForm.get("fromUnitID");
	    Integer destinationUnitID = (Integer) dynaActionForm.get("destinationUnitID");	    
	    fromUnit = (Unit) rootDomainObject.readPartyByOID(fromUnitID);
	    destinationUnit = (Unit) rootDomainObject.readPartyByOID(destinationUnitID);	    	   
	}
	
	try {
	    executeService("MergeUnits", new Object[] {fromUnit, destinationUnit, Boolean.TRUE});
	} catch (DomainException e) {
	    saveMessages(request, e);	    
	    return returnToConfirmationPage(mapping, request, fromUnit, destinationUnit);
	}
	
	return chooseUnitToStart(mapping, form, request, response);	
    }
       
    // Private methods

    private ActionForward returnToConfirmationPage(ActionMapping mapping, HttpServletRequest request, Unit fromUnit, Unit destinationUnit) {
	request.setAttribute("fromUnit", fromUnit);
	request.setAttribute("destinationUnit", destinationUnit);
	if (destinationUnit.isNoOfficialExternal()) {
	    request.setAttribute("official", false);
	} else {
	    request.setAttribute("official", false);
	}
	return mapping.findForward("goToConfirmation");
    }
    
    private void readAndSetUnitsToMerge(HttpServletRequest request) {
	Unit fromUnit = getFromUnitFromParameter(request);
	Unit destinationUnit = getDestinationUnitFromParameter(request);
	request.setAttribute("fromUnit", fromUnit);
	request.setAttribute("destinationUnit", destinationUnit);
    }
    
    private Unit getFromUnitFromParameter(final HttpServletRequest request) {
	final String unitIDString = request.getParameter("fromUnitID");
	final Integer uniID = Integer.valueOf(unitIDString);
	return (Unit) rootDomainObject.readPartyByOID(uniID);
    }   
    
    private Unit getDestinationUnitFromParameter(final HttpServletRequest request) {
	final String unitIDString = request.getParameter("unitID");
	final Integer uniID = Integer.valueOf(unitIDString);
	return (Unit) rootDomainObject.readPartyByOID(uniID);    
    }     
    
    private void saveMessages(HttpServletRequest request, DomainException e) {
	ActionMessages actionMessages = new ActionMessages();
	actionMessages.add("", new ActionMessage(e.getMessage(), e.getArgs()));
	saveMessages(request, actionMessages);
    }  
}
