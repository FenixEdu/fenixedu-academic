package net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors;

import net.sourceforge.fenixedu.domain.Language;
import net.sourceforge.fenixedu.domain.SiteElement;
import net.sourceforge.fenixedu.util.StringUtils;

public abstract class SiteElementPathProcessor extends PathProcessor {

    public SiteElementPathProcessor() {
        super();
    }

    public static String getElementPathName(SiteElement element) {
        String name = element.getName().getContent(Language.getApplicationLanguage());
        
        if (name == null) {
            return null;
        }
        
        return StringUtils.normalize(name.toLowerCase().replace(' ', '-').replace('/', '-'));
    }
    
}
