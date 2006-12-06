/*
 * Created on Apr 15, 2005
 */
package net.sourceforge.fenixedu.domain.person;

import java.util.ResourceBundle;
import net.sourceforge.fenixedu.util.LanguageUtils;

public enum IDDocumentType {

    IDENTITY_CARD,
    
    PASSPORT,
    
    FOREIGNER_IDENTITY_CARD,
    
    NATIVE_COUNTRY_IDENTITY_CARD,
    
    NAVY_IDENTITY_CARD,
    
    AIR_FORCE_IDENTITY_CARD,
     
    OTHER,
    
    MILITARY_IDENTITY_CARD,
    
    EXTERNAL;
    
    public String getName() {
        return name();
    }

    public String getLocalizedName() {
    	return ResourceBundle.getBundle("resources.EnumerationResources", LanguageUtils.getLocale()).getString(name());
    }

}
