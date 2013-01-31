package net.sourceforge.fenixedu.domain.candidacyProcess;

import java.util.Locale;
import java.util.ResourceBundle;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public enum CandidacyProcessState {

	STAND_BY,

	SENT_TO_JURY,

	SENT_TO_COORDINATOR,

	SENT_TO_SCIENTIFIC_COUNCIL,

	PUBLISHED;

	public String getName() {
		return name();
	}

	public String getQualifiedName() {
		return CandidacyProcessState.class.getSimpleName() + "." + name();
	}

	public String getFullyQualifiedName() {
		return CandidacyProcessState.class.getName() + "." + name();
	}

	protected String localizedName(Locale locale) {
		return ResourceBundle.getBundle("resources.EnumerationResources", locale).getString(getQualifiedName());
	}

	protected String localizedName() {
		return localizedName(Language.getLocale());
	}

	public String getLocalizedName() {
		return localizedName();
	}

}
