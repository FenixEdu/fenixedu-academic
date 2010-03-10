package net.sourceforge.fenixedu.domain.phd;

import java.util.Locale;
import java.util.ResourceBundle;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public enum PhdProgramCandidacyProcessState implements PhdProcessStateType {

    PRE_CANDIDATE,

    STAND_BY_WITH_MISSING_INFORMATION,

    STAND_BY_WITH_COMPLETE_INFORMATION,

    PENDING_FOR_COORDINATOR_OPINION,

    REJECTED,

    WAITING_FOR_SCIENTIFIC_COUNCIL_RATIFICATION,

    RATIFIED_BY_SCIENTIFIC_COUNCIL,

    CONCLUDED;

    @Override
    public String getName() {
	return name();
    }

    @Override
    public String getLocalizedName() {
	return getLocalizedName(Language.getLocale());
    }

    @Override
    public String getLocalizedName(final Locale locale) {
	return ResourceBundle.getBundle("resources.PhdResources", locale).getString(getQualifiedName());
    }

    private String getQualifiedName() {
	return getClass().getSimpleName() + "." + name();
    }

}
