package net.sourceforge.fenixedu.domain.phd.migration;

import java.util.Locale;
import java.util.ResourceBundle;

import org.fenixedu.commons.i18n.I18N;

import java.util.Locale;

public enum PhdMigrationProcessStateType {

    NOT_MIGRATED,

    CANDIDACY_CREATED,

    CANDIDACY_RATIFIED,

    WORK_DEVELOPMENT,

    REQUESTED_THESIS_DISCUSSION,

    COMPLETED_THESIS_DISCUSSION,

    CANCELED,

    CONCLUDED;

    public String getLocalizedName() {
        return getLocalizedName(I18N.getLocale());
    }

    public String getLocalizedName(final Locale locale) {
        return ResourceBundle.getBundle("resources.PhdResources", locale).getString(getQualifiedName());
    }

    public String getQualifiedName() {
        return getClass().getSimpleName() + "." + name();
    }

}
