package net.sourceforge.fenixedu.domain.degreeStructure;

import java.util.Locale;
import java.util.ResourceBundle;

import org.fenixedu.commons.i18n.I18N;

import java.util.Locale;

public enum BranchType {

    MAJOR,

    MINOR;

    public String getName() {
        return name();
    }

    public String getQualifiedName() {
        return this.getClass().getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
        return getClass().getName() + "." + name();
    }

    public String getDescription() {
        return getDescription(I18N.getLocale());
    }

    public String getDescription(final Locale locale) {
        return ResourceBundle.getBundle("resources.EnumerationResources", locale).getString(getQualifiedName());
    }

}
