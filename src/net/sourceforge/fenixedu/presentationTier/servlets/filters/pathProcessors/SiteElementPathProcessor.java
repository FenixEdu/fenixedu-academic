package net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import net.sourceforge.fenixedu.domain.Language;
import net.sourceforge.fenixedu.domain.SiteElement;

public abstract class SiteElementPathProcessor extends PathProcessor {

    public SiteElementPathProcessor(String forwardURI) {
        super(forwardURI);
    }

    public static String getElementPathName(SiteElement element) {
        String name = element.getName().getContent(Language.getApplicationLanguage());
        
        if (name == null) {
            return null;
        }
        
        try {
            return URLEncoder.encode(name.toLowerCase().replace(' ', '-'), "iso-8859-1");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("iso-8859-1 should be supported");
        }
    }
    
}
