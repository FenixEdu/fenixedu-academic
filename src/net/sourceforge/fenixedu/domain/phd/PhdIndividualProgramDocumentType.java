package net.sourceforge.fenixedu.domain.phd;

import java.util.Locale;
import java.util.ResourceBundle;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public enum PhdIndividualProgramDocumentType {

    CANDIDACY_FORM,

    STUDY_PLAN,

    CV,

    DEGREE_FINALIZATION_CERTIFICATE,

    HABILITATION_CERTIFICATE_DOCUMENT,

    REPORT_OR_WORK_DOCUMENT,

    ID_DOCUMENT,

    SOCIAL_SECURITY,

    RECOMMENDATION_LETTER,

    HEALTH_BULLETIN,

    GUIDER_ACCEPTANCE_LETTER,

    ASSISTENT_GUIDER_ACCEPTANCE_LETTER,

    MOTIVATION_LETTER,

    RESEARCH_PLAN,

    OTHER;

    public String getLocalizedName() {
	return getLocalizedName(Language.getLocale());
    }

    public String getLocalizedName(final Locale locale) {
	return ResourceBundle.getBundle("resources.EnumerationResources", locale).getString(getQualifiedName());
    }

    public String getQualifiedName() {
	return PhdIndividualProgramDocumentType.class.getSimpleName() + "." + name();
    }

}
