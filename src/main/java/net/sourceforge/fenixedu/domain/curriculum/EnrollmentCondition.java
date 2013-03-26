package net.sourceforge.fenixedu.domain.curriculum;

import java.util.ResourceBundle;

import pt.utl.ist.fenix.tools.util.i18n.Language;

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
        return ResourceBundle.getBundle("resources.EnumerationResources", Language.getLocale()).getString(getQualifiedName());
    }

}
