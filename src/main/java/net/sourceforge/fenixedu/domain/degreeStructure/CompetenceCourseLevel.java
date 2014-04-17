package net.sourceforge.fenixedu.domain.degreeStructure;

import java.util.Locale;
import java.util.ResourceBundle;

import org.fenixedu.commons.i18n.I18N;

import java.util.Locale;

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
        return getLocalizedName(I18N.getLocale());
    }

    public String getLocalizedName(final Locale locale) {
        return ResourceBundle.getBundle("resources.EnumerationResources", locale).getString(name());
    }

}
