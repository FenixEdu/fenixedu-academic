package net.sourceforge.fenixedu.domain.contents.pathProcessors;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.organizationalStructure.DegreeUnit;

public class DegreePathProcessor extends AbstractPathProcessor {

    private String getDegreeName(String path) {
	final int indexOfSlash = path.indexOf('/');
	return indexOfSlash >= 0 ? path.substring(0, indexOfSlash) : path;
    }
    
    public Content processPath(String path) {
	final String unitAcronym = getDegreeName(path);
	final Degree degree = Degree.readBySigla(unitAcronym);
	if (degree != null) {
	    return degree.getSite();
	}

	for (final Degree otherDegree : RootDomainObject.getInstance().getDegreesSet()) {
	    final DegreeUnit otherDegreeUnit = otherDegree.getUnit();
	    if (otherDegreeUnit != null && otherDegreeUnit.getAcronym().equalsIgnoreCase(unitAcronym)) {
		final Site site = otherDegreeUnit.getSite();
		return site == null || !site.isAvailable() ? null : site;		
	    }
	}

	return null;
    }

}
