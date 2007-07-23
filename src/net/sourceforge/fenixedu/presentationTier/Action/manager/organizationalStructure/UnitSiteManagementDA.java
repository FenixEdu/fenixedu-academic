package net.sourceforge.fenixedu.presentationTier.Action.manager.organizationalStructure;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.webSiteManager.CustomUnitSiteManagementDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class UnitSiteManagementDA extends CustomUnitSiteManagementDA {

	@Override
	protected Unit getUnit(HttpServletRequest request) {
		Unit unit = super.getUnit(request);
		
		if (unit != null) {
			return unit;
		}
		
		Integer unitId = getIdInternal(request, "unitID");
		return (Unit) RootDomainObject.getInstance().readPartyByOID(unitId);
	}
	
    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
    	request.setAttribute("units", Collections.singleton(RootDomainObject.getInstance().getInstitutionUnit()));
    	return mapping.findForward("showUnits");
    }

    public ActionForward createSite(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
    	Unit unit = getUnit(request);

		try {
			executeService("CreateUnitSite", unit);
		} catch (DomainException e) {
			addActionMessage("error", request, e.getKey(), e.getArgs());
		}
		
		return prepare(mapping, actionForm, request, response);
    }

	@Override
	protected String getAuthorNameForFile(HttpServletRequest request, Item item) {
		return null;
	}

}
