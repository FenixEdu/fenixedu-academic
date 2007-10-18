package net.sourceforge.fenixedu.domain.student;

import java.util.ResourceBundle;

import net.sourceforge.fenixedu.util.LanguageUtils;

/**
 * TODO: remove this, enrich RegistrationAgreement instead
 * This enum shouldn't have been created. 
 *
 */

@Deprecated
public enum MobilityProgram {

    SOCRATES,
    
    ERASMUS,

    MINERVA,
    
    COVENANT_WITH_AZORES;

    public String getQualifiedName() {
	return this.getClass().getSimpleName() + "." + name();
    }

    public String getDescription() {
	return ResourceBundle.getBundle("resources.EnumerationResources", LanguageUtils.getLocale()).getString(name());
    }

}
