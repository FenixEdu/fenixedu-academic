package net.sourceforge.fenixedu.domain.dissertation;

import java.util.List;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
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

    private Person orientator = null;
    private Person coorientator = null;
    private Integer orientatorCredits = null;
    private Integer coorientatorCredits = null;
    
    private List<DissertationCandidacy> dissertationCandidacies = null;
    private List<Enrolment> enrolments = null;
	private List<Comment> comments = null;
	private List<Log> logs = null;
	private DissertationState dissertationState = null;
	private List<DissertationFile> dissertationFiles = null;
	private List<DissertationEvaluationParticipant> dissertationEvaluationParticipants = null;
	private List<Location> locations = null;
	private List<Scheduling> schedulings = null;
	
	private List<ExecutionDegree> executionDegrees = null;
	  
	private final static double CREDITS = 1;
	   
	public Person getOrientator() {
		return orientator;
	}
	public void setOrientator(Person orientator) {
		this.orientator = orientator;
	}
	public Person getCoorientator() {
		return coorientator;
	}
	public void setCoorientator(Person coorientator) {
		this.coorientator = coorientator;
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
	public void setDissertationEvaluationParticipants(List<DissertationEvaluationParticipant> dissertationEvaluationParticipants) {
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
	
	public void setOrientatorCreditsDistribution(Integer percent) {
	    if (percent != null && (percent < 0 || percent > 100)) {
	        throw new DomainException(/*"thesis.orietation.credits.notValid"*/ "A IMPLEMENTAR NOS RESOURCES");
	    }
	    orientatorCredits = percent;
	    setCoorientatorCreditsDistribution(percent != null ? 100 - percent : null);
	}
	 
	public Integer getOrientatorCreditsDistribution() {
		Integer distribution = getCoorientatorCreditsDistribution();	
	    return distribution != null ? 100 - distribution : null;
	}
	
	public Integer getCoorientatorCreditsDistribution() {
		Integer distribution = getOrientatorCreditsDistribution();	
	    return distribution != null ? 100 - distribution : null;
	}
	    
	public void setCoorientatorCreditsDistribution(Integer percent) {
	    if (percent != null && (percent < 0 || percent > 100)) {
	        throw new DomainException(/*"thesis.orietation.credits.notValid"*/ "A IMPLEMENTAR NOS RESOURCES");
	    }
	    coorientatorCredits = percent;
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
	    
	/*public boolean isEvaluated() {
	    return getDissertationState().getDissertationStateValue() == DissertationStateValue.EVALUATED;
	}*/
	    
	// the credits calculation always depends on the current credits' value for
	// the thesis (no history)
	public static double getCredits() {
	    return CREDITS;
	}
	
	public Boolean hasOrientator() {
		Boolean result = false;
		if (orientator != null) {
			result = true;
		}
		return result;
	}
	
	public Boolean hasCoorientator() {
		Boolean result = false;
		if (coorientator != null) {
			result = true;
		}
		return result;
	}
	public List<ExecutionDegree> getExecutionDegrees() {
		return executionDegrees;
	}
	public void setExecutionDegrees(List<ExecutionDegree> executionDegrees) {
		this.executionDegrees = executionDegrees;
	}
}