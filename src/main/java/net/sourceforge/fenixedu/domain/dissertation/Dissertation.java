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
        setRootDomainObject(getRootDomainObject());
    }    

    private Person orientator;
    private Person coorientator;
    private Integer orientatorCredits;
    private Integer coorientatorCredits;
    
	private List<ExecutionDegree> executionDegrees = null;
	   
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
	public Integer getOrientatorCredits() {
	    return getOrientatorCredits();
	}
	
	public Integer getCoorientatorCredits() {
	    return getCoorientatorCredits();
	}
	
	public void setOrientatorCredits(Integer orientatorCredits) {
	    this.orientatorCredits = orientatorCredits;
	}
	
	public void setCoorientatorCredits(Integer coorientatorCredits) {
	    this.coorientatorCredits = coorientatorCredits;
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