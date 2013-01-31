package net.sourceforge.fenixedu.domain.phd;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess;
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

	public static List<PhdProgramCandidacyProcessState> getPossibleNextStates(final PhdProgramCandidacyProcess process) {
		PhdProgramCandidacyProcessState type = process.getActiveState();
		return getPossibleNextStates(type);
	}

	public static List<PhdProgramCandidacyProcessState> getPossibleNextStates(final PhdProgramCandidacyProcessState type) {
		if (type == null) {
			return Arrays.asList(PRE_CANDIDATE, STAND_BY_WITH_MISSING_INFORMATION, STAND_BY_WITH_COMPLETE_INFORMATION);
		}

		switch (type) {
		case PRE_CANDIDATE:
			return Arrays.asList(STAND_BY_WITH_COMPLETE_INFORMATION, PENDING_FOR_COORDINATOR_OPINION, REJECTED);
		case STAND_BY_WITH_MISSING_INFORMATION:
			return Arrays.asList(PENDING_FOR_COORDINATOR_OPINION, REJECTED);
		case STAND_BY_WITH_COMPLETE_INFORMATION:
			return Arrays.asList(PENDING_FOR_COORDINATOR_OPINION, REJECTED);
		case PENDING_FOR_COORDINATOR_OPINION:
			return Arrays.asList(WAITING_FOR_SCIENTIFIC_COUNCIL_RATIFICATION, REJECTED);
		case WAITING_FOR_SCIENTIFIC_COUNCIL_RATIFICATION:
			return Arrays.asList(PENDING_FOR_COORDINATOR_OPINION, REJECTED, RATIFIED_BY_SCIENTIFIC_COUNCIL);
		case RATIFIED_BY_SCIENTIFIC_COUNCIL:
			return Arrays.asList(CONCLUDED, REJECTED);
		case CONCLUDED:
			return Arrays.asList(RATIFIED_BY_SCIENTIFIC_COUNCIL, REJECTED);
		case REJECTED:
			return Arrays.asList(PENDING_FOR_COORDINATOR_OPINION);
		}

		return Collections.emptyList();
	}

}
