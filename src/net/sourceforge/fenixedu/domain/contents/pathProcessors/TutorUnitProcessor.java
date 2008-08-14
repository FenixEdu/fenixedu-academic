package net.sourceforge.fenixedu.domain.contents.pathProcessors;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitAcronym;

public class TutorUnitProcessor extends AbstractPathProcessor {

    private Unit tutorUnit;

    @Override
    public Content processPath(String path) {
	return getTutorUnit().getSite();
    }

    @Override
    public String getTrailingPath(String path) {
	return path;
    }

    @Override
    public Content getInitialContent() {
	return getTutorUnit().getSite();
    }

    public boolean keepPortalInContentsPath() {
	return false;
    }

    private Unit getTutorUnit() {
	if (tutorUnit == null) {
	    for (UnitAcronym acronym : RootDomainObject.getInstance().getUnitAcronyms()) {
		if (acronym.getAcronym().equalsIgnoreCase("Tutor")) {
		    tutorUnit = acronym.getUnits().get(0);
		    return tutorUnit;
		}
	    }
	}
	return tutorUnit;
    }
}
