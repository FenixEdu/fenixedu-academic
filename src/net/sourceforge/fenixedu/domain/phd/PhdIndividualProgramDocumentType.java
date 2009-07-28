package net.sourceforge.fenixedu.domain.phd;

import java.util.Locale;
import java.util.ResourceBundle;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public enum PhdIndividualProgramDocumentType {

    CANDIDACY_FORM,

    CANDIDACY_REVIEW(true),

    STUDY_PLAN(true),

    CV,

    DEGREE_FINALIZATION_CERTIFICATE(true),

    HABILITATION_CERTIFICATE_DOCUMENT(true),

    DISSERTATION_OR_FINAL_WORK_DOCUMENT(true),

    ID_DOCUMENT,

    SOCIAL_SECURITY,

    RECOMMENDATION_LETTER(true),

    HEALTH_BULLETIN,

    GUIDER_ACCEPTANCE_LETTER(true),

    ASSISTENT_GUIDER_ACCEPTANCE_LETTER(true),

    MOTIVATION_LETTER,

    RESEARCH_PLAN,

    CANDIDACY_RATIFICATION,

    OTHER(true);

    private boolean multipleDocumentsAllowed;

    private PhdIndividualProgramDocumentType() {
	this(false);
    }

    private PhdIndividualProgramDocumentType(boolean multipleDocumentsAllowed) {
	this.multipleDocumentsAllowed = multipleDocumentsAllowed;
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

    public boolean isMultipleDocumentsAllowed() {
	return multipleDocumentsAllowed;
    }

}
