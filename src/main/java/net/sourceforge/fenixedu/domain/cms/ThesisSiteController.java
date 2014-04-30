package net.sourceforge.fenixedu.domain.cms;

import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisSite;

import org.apache.commons.lang.StringUtils;

import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;

public class ThesisSiteController extends SiteTemplateController {

    @Override
    public Site selectSiteForPath(String[] parts) {
        String thesisId = parts[0];
        // If the ID is neither an OID or an IdInternal, simply return null
        if (!StringUtils.isNumeric(thesisId)) {
            return null;
        }
        DomainObject selectedObject = FenixFramework.getDomainObject(thesisId);
        if (selectedObject instanceof Thesis) {
            return ((Thesis) selectedObject).getSite();
        } else {
            // Identifier should be an IdInternal, let's try to process it...
            long oid = 2353642078208l + Integer.parseInt(thesisId);
            Thesis thesis = FenixFramework.getConfig().getBackEnd().fromOid(oid);
            return thesis == null ? null : thesis.getSite();
        }
    }

    @Override
    public Class<? extends Site> getControlledClass() {
        return ThesisSite.class;
    }

    @Override
    public int getTrailingPath(Site site, String[] parts) {
        return 1;
    }

}
