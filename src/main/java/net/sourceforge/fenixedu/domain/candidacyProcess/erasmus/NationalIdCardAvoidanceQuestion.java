package net.sourceforge.fenixedu.domain.candidacyProcess.erasmus;

import java.util.Locale;
import java.util.ResourceBundle;

import org.fenixedu.commons.i18n.I18N;

import java.util.Locale;

public enum NationalIdCardAvoidanceQuestion {
    UNANSWERED, NOT_OWNER_ELECTRONIC_ID_CARD, ELECTRONIC_ID_CARD_CODES_UNKNOWN, COUNTRY_NOT_LISTED_IN_FENIX_AUTHENTICATION,
    ELECTRONIC_ID_CARD_SUBMISSION_AVAILABILITY_UNKNOWN, ELECTRONIC_ID_CARD_SUBMISSION_TRUST_LACK, OTHER_REASON;

    public String getLocalizedName() {
        return getLocalizedName(I18N.getLocale());
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
