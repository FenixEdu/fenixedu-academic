package net.sourceforge.fenixedu.domain.phd.migration;

import java.util.Locale;
import java.util.ResourceBundle;

import pt.utl.ist.fenix.tools.util.i18n.Language;

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
        return getLocalizedName(Language.getLocale());
    }

    public String getLocalizedName(final Locale locale) {
        return ResourceBundle.getBundle("resources.PhdResources", locale).getString(getQualifiedName());
    }

    public String getQualifiedName() {
        return getClass().getSimpleName() + "." + name();
    }

}
