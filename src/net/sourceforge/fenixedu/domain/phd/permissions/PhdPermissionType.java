package net.sourceforge.fenixedu.domain.phd.permissions;

import java.util.Locale;
import java.util.ResourceBundle;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public enum PhdPermissionType {
    CANDIDACY_PROCESS_MANAGEMENT, 
    
    PUBLIC_PRESENTATION_SEMINAR_PROCESS_MANAGEMENT, 
    
    THESIS_PROCESS_MANAGEMENT;
    
    public String getLocalizedName() {
	return getLocalizedName(Language.getLocale());
    }

    public String getLocalizedName(final Locale locale) {
	return ResourceBundle.getBundle("resources.PhdResources", locale).getString(getQualifiedName());
    }

    public String getQualifiedName() {
	return getClass().getSimpleName() + "." + name();
    }


}
