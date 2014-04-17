package net.sourceforge.fenixedu.domain;

import java.util.Locale;
import java.util.ResourceBundle;

import org.fenixedu.commons.i18n.I18N;

import java.util.Locale;

public enum SalaryType {

    INTERVAL_0_500, INTERVAL_501_750, INTERVAL_751_1000, INTERVAL_1001_1250, INTERVAL_1251_1500, INTERVAL_1501_2000,
    INTERVAL_2001_3000, INTERVAL_3000_OR_MORE, NO_ANSWER;

    public String getName() {
        return name();
    }

    public String getQualifiedName() {
        return SalaryType.class.getSimpleName() + "." + name();
    }

    public String getLocalizedName() {
        return getLocalizedName(I18N.getLocale());
    }

    public String getLocalizedName(Locale locale) {
        return ResourceBundle.getBundle("resources.EnumerationResources", locale).getString(getQualifiedName());
    }

}
