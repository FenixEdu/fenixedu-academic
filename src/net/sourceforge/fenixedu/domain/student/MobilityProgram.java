package net.sourceforge.fenixedu.domain.student;

import java.util.ResourceBundle;

import net.sourceforge.fenixedu.util.LanguageUtils;

public enum MobilityProgram {

    SOCRATES,
    
    ERASMUS,

    MINERVA;

    public String getQualifiedName() {
	return this.getClass().getSimpleName() + "." + name();
    }

    public String getDescription() {
	return ResourceBundle.getBundle("resources.EnumerationResources", LanguageUtils.getLocale()).getString(name());
    }

}
