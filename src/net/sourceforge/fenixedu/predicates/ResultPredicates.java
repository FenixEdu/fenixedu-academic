package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.accessControl.AccessControlPredicate;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.research.result.Result;
import net.sourceforge.fenixedu.domain.research.result.ResultDocumentFile;
import net.sourceforge.fenixedu.domain.research.result.ResultEventAssociation;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation;
import net.sourceforge.fenixedu.domain.research.result.ResultUnitAssociation;

public class ResultPredicates {

    static Person person = AccessControl.getUserView().getPerson();

    /**
     * Predicates to access Result objects.
     */
    public static final AccessControlPredicate<Result> readPredicate = new AccessControlPredicate<Result>() {
	public boolean evaluate(Result result) {
	    if (person.hasRole(RoleType.RESEARCHER)) {
		return true;
	    }
	    return false;
	}
    };
    
    public static final AccessControlPredicate<Result> writePredicate = new AccessControlPredicate<Result>() {
	public boolean evaluate(Result result) {
	    if (person.hasRole(RoleType.RESEARCHER)) {
		//if we are creating a new object there are no participations yet.
		//if we are changing an existing object then person must be a participator.
		if(!result.hasAnyResultParticipations() || result.hasPersonParticipation(person)) {
		    return true;
		}
	    }
	    return false;
	}
    };

    /**
     * Predicates to access ResultUnitAssociation objects.
     */
    public static final AccessControlPredicate<ResultUnitAssociation> unitWritePredicate = new AccessControlPredicate<ResultUnitAssociation>() {
	public boolean evaluate(ResultUnitAssociation association) {
	    if (person.hasRole(RoleType.RESEARCHER) && association.getResult() != null
		    && association.getResult().hasPersonParticipation(person)) {
		return true;
	    }
	    return false;
	}
    };

    /**
     * Predicates to access ResultEventAssociation objects.
     */
    public static final AccessControlPredicate<ResultEventAssociation> eventWritePredicate = new AccessControlPredicate<ResultEventAssociation>() {
	public boolean evaluate(ResultEventAssociation association) {
	    if (person.hasRole(RoleType.RESEARCHER) && association.getResult() != null
		    && association.getResult().hasPersonParticipation(person)) {
		return true;
	    }
	    return false;
	}
    };

    /**
     * Predicates to access ResultParticipation objects.
     */
    public static final AccessControlPredicate<ResultParticipation> participationWritePredicate = new AccessControlPredicate<ResultParticipation>() {
	public boolean evaluate(ResultParticipation participation) {
	    final Result result = participation.getResult();
	
	    if (person.hasRole(RoleType.RESEARCHER) && result!=null) {
		if(result.hasPersonParticipation(person)) {
		    return true;    
		}
		if(result.getResultParticipationsCount()==1 && result.getResultParticipations().get(0).getPerson()==null) {
		    return true;
		}
	    }
	    return false;
	}
    };
    
    /**
     * Predicates to access ResultDocumentFile objects.
     */
    public static final AccessControlPredicate<ResultDocumentFile> documentFileWritePredicate = new AccessControlPredicate<ResultDocumentFile>() {
	public boolean evaluate(ResultDocumentFile documentFile) {
	    if (person.hasRole(RoleType.RESEARCHER) && documentFile.getResult()!=null
		    && documentFile.getResult().hasPersonParticipation(person)) {
		return true;
	    }
	    return false;
	}
    };
}