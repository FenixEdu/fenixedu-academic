package net.sourceforge.fenixedu.domain.phd;

import java.util.Locale;
import java.util.ResourceBundle;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public enum PhdIndividualProgramDocumentType {

    CANDIDACY_FORM,

    CANDIDACY_REVIEW(false),

    STUDY_PLAN,

    CV,

    DEGREE_FINALIZATION_CERTIFICATE(false),

    HABILITATION_CERTIFICATE_DOCUMENT(false),

    DISSERTATION_OR_FINAL_WORK_DOCUMENT(false),

    ID_DOCUMENT,

    SOCIAL_SECURITY,

    RECOMMENDATION_LETTER(false),

    HEALTH_BULLETIN,

    GUIDER_ACCEPTANCE_LETTER(false),

    ASSISTENT_GUIDER_ACCEPTANCE_LETTER(false),

    MOTIVATION_LETTER,

    RESEARCH_PLAN,

    CANDIDACY_RATIFICATION,

    PUBLIC_PRESENTATION_SEMINAR_COMISSION,

    PUBLIC_PRESENTATION_SEMINAR_REPORT,

    PROVISIONAL_THESIS,

    FINAL_THESIS,

    THESIS_REQUIREMENT,

    JURY_ELEMENTS,
    
    JURY_PRESIDENT_ELEMENT,
    
    OTHER(false);

    private boolean isVersioned;

    private PhdIndividualProgramDocumentType() {
	this(true);
    }

    private PhdIndividualProgramDocumentType(boolean isVersioned) {
	this.isVersioned = isVersioned;
    }

    public String getLocalizedName() {
	return getLocalizedName(Language.getLocale());
    }

    public String getLocalizedName(final Locale locale) {
	return ResourceBundle.getBundle("resources.EnumerationResources", locale).getString(getQualifiedName());
    }

    public String getQualifiedName() {
	return PhdIndividualProgramDocumentType.class.getSimpleName() + "." + name();
    }

    public boolean isVersioned() {
	return isVersioned;
    }

}
