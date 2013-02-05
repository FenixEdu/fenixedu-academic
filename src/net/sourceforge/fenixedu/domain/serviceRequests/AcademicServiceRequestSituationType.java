package net.sourceforge.fenixedu.domain.serviceRequests;

import java.util.Locale;
import java.util.ResourceBundle;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public enum AcademicServiceRequestSituationType {

    NEW,

    PROCESSING,

    SENT_TO_EXTERNAL_ENTITY,

    RECEIVED_FROM_EXTERNAL_ENTITY,

    CONCLUDED,

    DELIVERED,

    REJECTED,

    CANCELLED;

    public String getName() {
        return name();
    }

    public String getQualifiedName() {
        return AcademicServiceRequestSituationType.class.getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
        return AcademicServiceRequestSituationType.class.getName() + "." + name();
    }

    public String getLocalizedName() {
        return getLocalizedName(Language.getLocale());
    }

    public String getLocalizedName(final Locale locale) {
        return ResourceBundle.getBundle("resources.EnumerationResources", locale).getString(getQualifiedName());
    }

}
