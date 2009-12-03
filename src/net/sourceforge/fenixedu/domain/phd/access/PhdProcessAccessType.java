package net.sourceforge.fenixedu.domain.phd.access;

import java.util.Locale;
import java.util.ResourceBundle;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public enum PhdProcessAccessType {

    JURY_DOCUMENTS_DOWNLOAD("JuryDocumentsDownload"), JURY_REPORTER_FEEDBACK_UPLOAD("JuryReporterFeedbackUpload");

    private String descriptor;

    private PhdProcessAccessType(String type) {
	this.descriptor = type;
    }

    public String getName() {
	return name();
    }

    public String getDescriptor() {
	return descriptor;
    }

    public String getLocalizedName() {
	return getLocalizedName(Language.getLocale());
    }

    public String getLocalizedName(final Locale locale) {
	return ResourceBundle.getBundle("resources.EnumerationResources", locale).getString(getQualifiedName());
    }

    public String getQualifiedName() {
	return getClass().getSimpleName() + "." + name();
    }

}
