package net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors;

import net.sourceforge.fenixedu.domain.Language;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.util.StringUtils;

public abstract class SiteElementPathProcessor extends PathProcessor {

    public SiteElementPathProcessor() {
        super();
    }

    public static String getElementPathName(Content element) {
        String name = element.getName().getContent(Language.getApplicationLanguage());
        
        if (name == null) {
            name = element.getName().getContent();
        }
        
        return name != null ? StringUtils.normalize(name.toLowerCase().replace(' ', '-').replace('/', '-')) : "";
    }
    
}
