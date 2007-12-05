package net.sourceforge.fenixedu.presentationTier.Action.publico.scientificalArea;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.publico.SearchPublicationsDA;

public class PublicScientificAreaSiteSearchPublicationsDA extends SearchPublicationsDA {

    @Override
    protected void setRequestDomainObject(HttpServletRequest request) {
	Integer unitId = getIntegerFromRequest(request, "selectedDepartmentUnitID");
	
	Unit unit = (Unit) rootDomainObject.readPartyByOID(unitId);
	request.setAttribute("unit", unit);
	request.setAttribute("site", unit.getSite());
    }
}
