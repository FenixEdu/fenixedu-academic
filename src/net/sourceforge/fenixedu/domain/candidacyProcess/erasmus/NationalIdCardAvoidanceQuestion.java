package net.sourceforge.fenixedu.domain.candidacyProcess.erasmus;

import java.util.Locale;
import java.util.ResourceBundle;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public enum NationalIdCardAvoidanceQuestion {
    UNANSWERED,
    NOT_OWNER_ELECTRONIC_ID_CARD, ELECTRONIC_ID_CARD_CODES_UNKNOWN, COUNTRY_NOT_LISTED_IN_FENIX_AUTHENTICATION, 
    ELECTRONIC_ID_CARD_SUBMISSION_AVAILABILITY_UNKNOWN, ELECTRONIC_ID_CARD_SUBMISSION_TRUST_LACK, OTHER_REASON;

    public String getLocalizedName() {
	return getLocalizedName(Language.getLocale());
    }

    public String getLocalizedName(final Locale locale) {
	return ResourceBundle.getBundle("resources.CandidateResources", locale).getString(getQualifiedName());
    }

    public String getQualifiedName() {
	return NationalIdCardAvoidanceQuestion.class.getSimpleName() + "." + name();
    }

    public String getName() {
	return name();
    }
}
