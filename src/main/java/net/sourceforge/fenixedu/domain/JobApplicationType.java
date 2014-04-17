package net.sourceforge.fenixedu.domain;

import java.util.Locale;
import java.util.ResourceBundle;

import org.fenixedu.commons.i18n.I18N;

import java.util.Locale;

public enum JobApplicationType {

    ANNOUNCEMENT, PUBLIC_ANNOUNCEMENT, SPONTANEOUS_PROPOSAL, EMPLOYMENT_AGENCY, DEPARTMENTS, UNIVA, AEIST, IAESTE, IEFP,
    PERSONAL_CONTACT, ENTERPRENEURSHIP, HEAD_HUNTERS, OTHERS;

    public String getName() {
        return name();
    }

    public String getQualifiedName() {
        return JobApplicationType.class.getSimpleName() + "." + name();
    }

    public String getLocalizedName() {
        return getLocalizedName(I18N.getLocale());
    }

    public String getLocalizedName(Locale locale) {
        return ResourceBundle.getBundle("resources.EnumerationResources", locale).getString(getQualifiedName());
    }

}
