package net.sourceforge.fenixedu.presentationTier.Action.publico.department;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.publico.SearchPublicationsDA;

public class PublicDepartmentSiteSearchPublicationsDA extends SearchPublicationsDA {

    @Override
    protected void setRequestDomainObject(HttpServletRequest request) {
	Integer unitId = getIntegerFromRequest(request, "selectedDepartmentUnitID");
	Unit unit = (Unit) rootDomainObject.readPartyByOID(unitId);
	
	Department department = unit.getDepartment();
	request.setAttribute("department", department);
    }

}
