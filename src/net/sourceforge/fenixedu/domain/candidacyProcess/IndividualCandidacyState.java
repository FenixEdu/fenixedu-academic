package net.sourceforge.fenixedu.domain.candidacyProcess;

import java.util.Locale;
import java.util.ResourceBundle;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public enum IndividualCandidacyState {

    STAND_BY,

    ACCEPTED,

    REJECTED,

    CANCELLED,

    NOT_ACCEPTED;

    public String getName() {
	return name();
    }

    public String getQualifiedName() {
	return IndividualCandidacyState.class.getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
	return IndividualCandidacyState.class.getName() + "." + name();
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
