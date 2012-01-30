package net.sourceforge.fenixedu.domain.phd.access;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.phd.PhdProcessStateType;
import net.sourceforge.fenixedu.domain.phd.PhdProgramCandidacyProcessState;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessStateType;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public enum PhdProcessAccessType {

    CANDIDACY_FEEDBACK_DOCUMENTS_DOWNLOAD(

    "CandidacyFeedbackDocumentsDownload",

    PhdProgramCandidacyProcessState.PENDING_FOR_COORDINATOR_OPINION),

    CANDIDACY_FEEDBACK_UPLOAD(

    "CandidacyFeedbackUpload",

    PhdProgramCandidacyProcessState.PENDING_FOR_COORDINATOR_OPINION),

    JURY_DOCUMENTS_DOWNLOAD(

    "JuryDocumentsDownload",

    PhdThesisProcessStateType.WAITING_FOR_JURY_REPORTER_FEEDBACK),

    JURY_REPORTER_FEEDBACK_UPLOAD(

    "JuryReporterFeedbackUpload",

    PhdThesisProcessStateType.WAITING_FOR_JURY_REPORTER_FEEDBACK),

    JURY_REVIEW_DOCUMENTS_DOWNLOAD(

    "JuryReviewDocumentsDownload",

    PhdThesisProcessStateType.WAITING_FOR_THESIS_DISCUSSION_DATE_SCHEDULING);

    private String descriptor;

    /**
     * Indicates accepted process states. Operation will be available if no
     * access type is specified or if process contais any of given process state
     * 
     * 
     * <pre>
     * Example:
     * <code>
     * 	ACCESS_TYPE("name", PhdProgramCandidacyProcessState.PENDING_FOR_COORDINATOR_OPINION)
     * </code>
     *  If process is not pending for coordination opinion, then ACCESS_TYPE will not be available.
     *</pre>
     */
    private PhdProcessStateType[] acceptedTypes;

    private PhdProcessAccessType(String type, PhdProcessStateType... acceptedTypes) {
	this.descriptor = type;
	this.acceptedTypes = acceptedTypes;
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
	return ResourceBundle.getBundle("resources.PhdResources", locale).getString(getQualifiedName());
    }

    public String getQualifiedName() {
	return getClass().getSimpleName() + "." + name();
    }

    public boolean hasAcceptedTypes() {
	return acceptedTypes != null && acceptedTypes.length > 0;
    }

    public List<PhdProcessStateType> getAcceptedTypes() {
	return Arrays.asList(acceptedTypes);
    }

}
