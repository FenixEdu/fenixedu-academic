package net.sourceforge.fenixedu.domain.degreeStructure;

import java.util.Locale;
import java.util.ResourceBundle;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public enum CompetenceCourseLevel {

    FIRST_CYCLE,

    SECOND_CYCLE,

    FORMATION,

    DOCTORATE,

    SPECIALIZATION,

    UNKNOWN;

    public String getName() {
        return name();
    }

    public String getLocalizedName() {
        return getLocalizedName(Language.getLocale());
    }

    public String getLocalizedName(final Locale locale) {
        return ResourceBundle.getBundle("resources.EnumerationResources", locale).getString(name());
    }

}
