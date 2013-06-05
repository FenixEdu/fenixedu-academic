package net.sourceforge.fenixedu.domain.dissertation;

import java.util.List;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.candidacy.Candidacy;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.thesis.ThesisEvaluationParticipant;
import net.sourceforge.fenixedu.domain.thesis.ThesisParticipationType;
import net.sourceforge.fenixedu.domain.thesis.ThesisState;

import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class Dissertation extends Dissertation_Base {
    
    public  Dissertation() {
        super();
    }
    
    private String proposalNumber = null;
    private MultiLanguageString title = null;
    private MultiLanguageString keywords = null;
    private MultiLanguageString description = null;
    private MultiLanguageString requirements = null;
    private MultiLanguageString objectives = null;
    private MultiLanguageString deliverable = null;
    private MultiLanguageString framing = null;
    private String url = null;
    private Boolean declarationAccepted = null;
    private DissertationVisibilityType visibility = null;
    private DateTime documentPublicAvailabilityDate = null;
    private String proposedPlace = null;
    private DateTime proposalDiscussionDate = null;
    private MultiLanguageString dissertationAbstract = null;
    private Integer mark = null;
    
   private List<DissertationCandidacy> dissertationCandidacies = null;
   private List<Enrolment> enrolments = null;
   private List<Comment> comments = null;
   private List<Log> logs = null;
   private DissertationState dissertationState = null;
   private List<DissertationFile> dissertationFiles = null;
   private List<DissertationEvaluationParticipant> dissertationEvaluationParticipants = null;
   private List<Location> locations = null;
   private List<Scheduling> schedulings = null;
   
   private final static double CREDITS = 1;
   
	public String getProposalNumber() {
		return proposalNumber;
	}
	public void setProposalNumber(String proposalNumber) {
		this.proposalNumber = proposalNumber;
	}
	public MultiLanguageString getTitle() {
		return title;
	}
	public void setTitle(MultiLanguageString title) {
		this.title = title;
	}
	public MultiLanguageString getKeywords() {
		return keywords;
	}
	public void setKeywords(MultiLanguageString keywords) {
		this.keywords = keywords;
	}
	public MultiLanguageString getDescription() {
		return description;
	}
	public void setDescription(MultiLanguageString description) {
		this.description = description;
	}
	public MultiLanguageString getRequirements() {
		return requirements;
	}
	public void setRequirements(MultiLanguageString requirements) {
		this.requirements = requirements;
	}
	public MultiLanguageString getObjectives() {
		return objectives;
	}
	public void setObjectives(MultiLanguageString objectives) {
		this.objectives = objectives;
	}
	public MultiLanguageString getDeliverable() {
		return deliverable;
	}
	public void setDeliverable(MultiLanguageString deliverable) {
		this.deliverable = deliverable;
	}
	public MultiLanguageString getFraming() {
		return framing;
	}
	public void setFraming(MultiLanguageString framing) {
		this.framing = framing;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Boolean getDeclarationAccepted() {
		return declarationAccepted;
	}
	public void setDeclarationAccepted(Boolean declarationAccepted) {
		this.declarationAccepted = declarationAccepted;
	}
	public DissertationVisibilityType getVisibility() {
		return visibility;
	}
	public void setVisibility(DissertationVisibilityType visibility) {
		this.visibility = visibility;
	}
	public DateTime getDocumentPublicAvailabilityDate() {
		return documentPublicAvailabilityDate;
	}
	public void setDocumentPublicAvailabilityDate(
			DateTime documentPublicAvailabilityDate) {
		this.documentPublicAvailabilityDate = documentPublicAvailabilityDate;
	}
	public String getProposedPlace() {
		return proposedPlace;
	}
	public void setProposedPlace(String proposedPlace) {
		this.proposedPlace = proposedPlace;
	}
	public DateTime getProposalDiscussionDate() {
		return proposalDiscussionDate;
	}
	public void setProposalDiscussionDate(DateTime proposalDiscussionDate) {
		this.proposalDiscussionDate = proposalDiscussionDate;
	}
	public MultiLanguageString getDissertationAbstract() {
		return dissertationAbstract;
	}
	public void setDissertationAbstract(MultiLanguageString dissertationAbstract) {
		this.dissertationAbstract = dissertationAbstract;
	}
	public Integer getMark() {
		return mark;
	}
	public void setMark(Integer mark) {
		this.mark = mark;
	}
	public List<DissertationCandidacy> getDissertationCandidacies() {
		return dissertationCandidacies;
	}
	public void setCandidacies(List<DissertationCandidacy> dissertationCandidacies) {
		this.dissertationCandidacies = dissertationCandidacies;
	}
	public List<Enrolment> getEnrolments() {
		return enrolments;
	}
	public void setEnrolments(List<Enrolment> enrolments) {
		this.enrolments = enrolments;
	}
	public List<Comment> getComments() {
		return comments;
	}
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	public List<Log> getLogs() {
		return logs;
	}
	public void setLogs(List<Log> logs) {
		this.logs = logs;
	}
	public DissertationState getDissertationState() {
		return dissertationState;
	}
	public void setDissertationStates(DissertationState dissertationState) {
		this.dissertationState = dissertationState;
	}
	public List<DissertationFile> getDissertationFiles() {
		return dissertationFiles;
	}
	public void setDissertationFiles(List<DissertationFile> dissertationFiles) {
		this.dissertationFiles = dissertationFiles;
	}
	public List<DissertationEvaluationParticipant> getDissertationEvaluationParticipants() {
		return dissertationEvaluationParticipants;
	}
	public void setDissertationEvaluationParticipants(
			List<DissertationEvaluationParticipant> dissertationEvaluationParticipants) {
		this.dissertationEvaluationParticipants = dissertationEvaluationParticipants;
	}
	public List<Location> getLocations() {
		return locations;
	}
	public void setLocations(List<Location> locations) {
		this.locations = locations;
	}
	public List<Scheduling> getSchedulings() {
		return schedulings;
	}
	public void setSchedulings(List<Scheduling> schedulings) {
		this.schedulings = schedulings;
	}
	
    public DissertationEvaluationParticipant getParticipant(DissertationParticipationType type) {
        for (DissertationEvaluationParticipant participant : getDissertationEvaluationParticipants()) {
            if (participant.getType() == type) {
                return participant;
            }
        }
        return null;
    }

    public void setOrientatorCreditsDistribution(Integer percent) {
        if (percent != null && (percent < 0 || percent > 100)) {
            throw new DomainException(/*"thesis.orietation.credits.notValid"*/ "A IMPLEMENTAR NOS RESOURCES");
        }

        setOrientatorCreditsDistribution(percent);
    }
    
    public Integer getCoorientatorCreditsDistribution() {
        Integer distribution = getOrientatorCreditsDistribution();

        return distribution != null ? 100 - distribution : null;
    }
    
    public void setCoorientatorCreditsDistribution(Integer percent) {
        if (percent != null && (percent < 0 || percent > 100)) {
            throw new DomainException(/*"thesis.orietation.credits.notValid"*/ "A IMPLEMENTAR NOS RESOURCES");
        }

        setOrientatorCreditsDistribution(percent != null ? 100 - percent : null);
    }
    
    public boolean hasFinalEnrolmentEvaluation() {
    	Boolean isApproved = false;
    	for (Enrolment enrolment : getEnrolments()) {
    		if (enrolment.isApproved()) {
    			break;
    		}
    	}
        return isApproved;
    }
    
    public boolean hasCredits() {
        if (isEvaluated() && hasFinalEnrolmentEvaluation()) {
            return true;
        } else {
            return false;
        }
    }
    
    public boolean isEvaluated() {
        return getDissertationState().getDissertationStateValue() == DissertationStateValue.EVALUATED;
    }
    
    // the credits calculation always depends on the current credits' value for
    // the thesis (no history)
    public static double getCredits() {
        return CREDITS;
    }
}
