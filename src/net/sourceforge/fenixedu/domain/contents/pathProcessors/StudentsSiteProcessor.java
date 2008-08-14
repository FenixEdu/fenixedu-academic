package net.sourceforge.fenixedu.domain.contents.pathProcessors;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentsSite;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitAcronym;

public class StudentsSiteProcessor extends AbstractPathProcessor {

    private StudentsSite site = null;

    @Override
    public Content processPath(String path) {
	return getSite();
    }

    private Content getSite() {
	if (site == null) {
	    for (UnitAcronym acronym : RootDomainObject.getInstance().getUnitAcronyms()) {
		if (acronym.getAcronym().equalsIgnoreCase("ACD")) {
		    Unit studentsUnit = acronym.getUnits().get(0);
		    site = (StudentsSite) studentsUnit.getSite();
		    return site;
		}
	    }
	}
	return site;
    }

    @Override
    public String getTrailingPath(String path) {
	return path;
    }

    @Override
    public Content getInitialContent() {
	return getSite();
    }

    public boolean keepPortalInContentsPath() {
	return false;
    }
}
