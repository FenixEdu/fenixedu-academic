package net.sourceforge.fenixedu.domain.curriculum;

import java.util.ResourceBundle;

import org.fenixedu.commons.i18n.I18N;

import java.util.Locale;

/**
 * @author David Santos in Jun 15, 2004
 */

public enum EnrollmentCondition {

    FINAL,

    TEMPORARY,

    IMPOSSIBLE,

    VALIDATED,

    INVISIBLE;

    public String getName() {
        return name();
    }

    public String getQualifiedName() {
        return EnrollmentCondition.class.getSimpleName() + "." + name();
    }

    public String getAcronym() {
        return getQualifiedName() + ".acronym";
    }

    public String getDescription() {
        return ResourceBundle.getBundle("resources.EnumerationResources", I18N.getLocale()).getString(getQualifiedName());
    }

}
