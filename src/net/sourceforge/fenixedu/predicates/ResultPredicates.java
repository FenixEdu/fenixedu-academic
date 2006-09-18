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
    public static final AccessControlPredicate<Result> createPredicate = new AccessControlPredicate<Result>() {
	public boolean evaluate(Result result) {
	    if (person.hasRole(RoleType.RESEARCHER) && !result.hasAnyResultParticipations()) {
		return true;
	    }
	    return false;
	}
    };
    
    public static final AccessControlPredicate<Result> writePredicate = new AccessControlPredicate<Result>() {
	public boolean evaluate(Result result) {
	    if (person.hasRole(RoleType.RESEARCHER) && result.hasPersonParticipation(person)) {
		return true;
	    }
	    return false;
	}
    };

    /**
     * Predicates to access ResultUnitAssociation objects.
     */
    public static final AccessControlPredicate<ResultUnitAssociation> unitWritePredicate = new AccessControlPredicate<ResultUnitAssociation>() {
	public boolean evaluate(ResultUnitAssociation association) {
	    final Result result = association.getResult();
	    
	    if (person.hasRole(RoleType.RESEARCHER) && result.hasPersonParticipation(person)) {
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
	    final Result result = association.getResult();
	    
	    if (person.hasRole(RoleType.RESEARCHER) && result.hasPersonParticipation(person)) {
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
	
	    if (person.hasRole(RoleType.RESEARCHER) && result.hasPersonParticipation(person)) {
		    return true;    
	    }
	    return false;
	}
    };
    
    /**
     * Predicates to access ResultDocumentFile objects.
     */
    public static final AccessControlPredicate<ResultDocumentFile> documentFileWritePredicate = new AccessControlPredicate<ResultDocumentFile>() {
	public boolean evaluate(ResultDocumentFile documentFile) {
	    final Result result = documentFile.getResult();
	    
	    if (person.hasRole(RoleType.RESEARCHER) && result.hasPersonParticipation(person)) {
		return true;
	    }
	    return false;
	}
    };
}
