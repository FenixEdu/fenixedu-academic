package net.sourceforge.fenixedu.domain.candidacyProcess;

import java.util.Locale;
import java.util.ResourceBundle;

import org.fenixedu.commons.i18n.I18N;

import java.util.Locale;

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
        return localizedName(I18N.getLocale());
    }

    public String getLocalizedName() {
        return localizedName();
    }

}
