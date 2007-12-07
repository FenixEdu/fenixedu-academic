package net.sourceforge.fenixedu.domain.contents.pathProcessors;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class DepartmentSitePathProcessor extends AbstractPathProcessor {

    private String getDepartmentName(String path) {
	final int indexOfSlash = path.indexOf('/');
	return indexOfSlash >= 0 ? path.substring(0, indexOfSlash) : path;
    }
    
    public Content processPath(String path) {
	String unitAcronym = getDepartmentName(path);
	Unit departmentUnit = null;

	for(Department department : RootDomainObject.getInstance().getDepartments()) {
	    if(department.getDepartmentUnit().getAcronym().equalsIgnoreCase(unitAcronym)) {
		departmentUnit = department.getDepartmentUnit();
	    }
	}
	
	
	if(departmentUnit == null) {
	    return null;
	}
	Site site = departmentUnit.getSite();
	return site != null && site.isAvailable() ? site : null;
    }
}
