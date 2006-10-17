package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.accessControl.AccessControlPredicate;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.research.result.Result;
import net.sourceforge.fenixedu.domain.research.result.ResultDocumentFile;
import net.sourceforge.fenixedu.domain.research.result.ResultEventAssociation;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation;
import net.sourceforge.fenixedu.domain.research.result.ResultUnitAssociation;

public class ResultPredicates {

    /**
     * Predicates to access Result objects.
     */
    public static final AccessControlPredicate<Result> createPredicate = new AccessControlPredicate<Result>() {
	public boolean evaluate(Result result) {
	    final IUserView userView = AccessControl.getUserView();
	    if (userView.hasRoleType(RoleType.RESEARCHER) && !result.hasAnyResultParticipations()) {
		return true;
	    }
	    return false;
	}
    };
    
    public static final AccessControlPredicate<Result> writePredicate = new AccessControlPredicate<Result>() {
	public boolean evaluate(Result result) {
	    final IUserView userView = AccessControl.getUserView();
	    if (userView.hasRoleType(RoleType.RESEARCHER) && result.hasPersonParticipation(userView.getPerson())) {
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
	    final IUserView userView = AccessControl.getUserView();
	    if (userView.hasRoleType(RoleType.RESEARCHER) && result.hasPersonParticipation(userView.getPerson())) {
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
	    final IUserView userView = AccessControl.getUserView();
	    if (userView.hasRoleType(RoleType.RESEARCHER) && result.hasPersonParticipation(userView.getPerson())) {
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
	    final IUserView userView = AccessControl.getUserView();
	    if (userView.hasRoleType(RoleType.RESEARCHER) && result.hasPersonParticipation(userView.getPerson())) {
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
	    final IUserView userView = AccessControl.getUserView();
	    if (userView.hasRoleType(RoleType.RESEARCHER) && result.hasPersonParticipation(userView.getPerson())) {
		return true;
	    }
	    return false;
	}
    };
}
