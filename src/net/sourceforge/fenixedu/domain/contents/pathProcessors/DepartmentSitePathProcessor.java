package net.sourceforge.fenixedu.domain.contents.pathProcessors;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.organizationalStructure.DepartmentUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitAcronym;

public class DepartmentSitePathProcessor extends AbstractPathProcessor {

    private String getDepartmentName(String path) {
	final int indexOfSlash = path.indexOf('/');
	return indexOfSlash >= 0 ? path.substring(0, indexOfSlash) : path;
    }

    public Content processPath(String path) {
	String unitAcronym = getDepartmentName(path);
	Unit departmentUnit = null;

	for (UnitAcronym acronym : RootDomainObject.getInstance().getUnitAcronyms()) {
	    if (acronym.getAcronym().equalsIgnoreCase(unitAcronym)) {
		departmentUnit = getDepartmentFromAcronym(acronym);
		break;
	    }
	}

	if (departmentUnit == null) {
	    return null;
	}
	Site site = departmentUnit.getSite();
	return site != null && site.isAvailable() ? site : null;
    }

    private DepartmentUnit getDepartmentFromAcronym(UnitAcronym acronym) {
	for (Unit unit : acronym.getUnits()) {
	    if (unit instanceof DepartmentUnit) {
		return (DepartmentUnit) unit;
	    }
	}
	return null;
    }
}
