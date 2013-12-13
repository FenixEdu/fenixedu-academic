package net.sourceforge.fenixedu.domain.contents.pathProcessors;

import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.thesis.Thesis;

import org.apache.commons.lang.StringUtils;

import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;

public class ThesisSiteProcessor extends AbstractPathProcessor {

    @Override
    public Content processPath(String path) {
        String thesisId = path.split("/")[0];
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
    public boolean keepPortalInContentsPath() {
        return false;
    }
}
