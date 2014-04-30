package net.sourceforge.fenixedu.domain.cms;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeSite;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.organizationalStructure.DegreeUnit;

public class DegreeSiteController extends SiteTemplateController {

    @Override
    public Site selectSiteForPath(String[] parts) {
        final Degree degree = Degree.readBySigla(parts[0]);
        if (degree != null) {
            return degree.getSite();
        }

        for (final Degree otherDegree : Degree.readNotEmptyDegrees()) {
            final DegreeUnit otherDegreeUnit = otherDegree.getUnit();
            if (otherDegreeUnit != null && otherDegreeUnit.getAcronym().equalsIgnoreCase(parts[0])) {
                return otherDegreeUnit.getSite();
            }
        }

        return null;
    }

    @Override
    public Class<DegreeSite> getControlledClass() {
        return DegreeSite.class;
    }

    @Override
    public int getTrailingPath(Site site, String[] parts) {
        return 1;
    }

}
