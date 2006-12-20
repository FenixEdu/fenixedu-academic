package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.research.result.ResearchResult;
import net.sourceforge.fenixedu.domain.research.result.ResearchResultDocumentFile;
import net.sourceforge.fenixedu.domain.research.result.ResultEventAssociation;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation;
import net.sourceforge.fenixedu.domain.research.result.ResultUnitAssociation;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

public class ResultPredicates {

	/**
	 * Predicates to access Result objects.
	 */
	public static final AccessControlPredicate<ResearchResult> createPredicate = new AccessControlPredicate<ResearchResult>() {
		public boolean evaluate(ResearchResult result) {
			final IUserView userView = AccessControl.getUserView();
			if (userView != null && userView.hasRoleType(RoleType.RESEARCHER)
					&& !result.hasAnyResultParticipations()) {
				return true;
			}
			return false;
		}
	};

	public static final AccessControlPredicate<ResearchResult> writePredicate = new AccessControlPredicate<ResearchResult>() {
		public boolean evaluate(ResearchResult result) {
			final IUserView userView = AccessControl.getUserView();
			if (userView != null && userView.hasRoleType(RoleType.RESEARCHER)
					&& result.hasPersonParticipation(userView.getPerson())) {
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
			final ResearchResult result = association.getResult();
			final IUserView userView = AccessControl.getUserView();
			if (userView != null && userView.hasRoleType(RoleType.RESEARCHER)
					&& result.hasPersonParticipation(userView.getPerson())) {
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
			final ResearchResult result = association.getResult();
			final IUserView userView = AccessControl.getUserView();
			if (userView != null && userView.hasRoleType(RoleType.RESEARCHER)
					&& result.hasPersonParticipation(userView.getPerson())) {
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
			final ResearchResult result = participation.getResult();
			final IUserView userView = AccessControl.getUserView();
			if (userView != null && userView.hasRoleType(RoleType.RESEARCHER)
					&& result.hasPersonParticipation(userView.getPerson())) {
				return true;
			}
			return false;
		}
	};

	/**
	 * Predicates to access ResultDocumentFile objects.
	 */
	public static final AccessControlPredicate<ResearchResultDocumentFile> documentFileWritePredicate = new AccessControlPredicate<ResearchResultDocumentFile>() {
		public boolean evaluate(ResearchResultDocumentFile documentFile) {
			final ResearchResult result = documentFile.getResult();
			final IUserView userView = AccessControl.getUserView();
			if (userView != null && userView.hasRoleType(RoleType.RESEARCHER)
					&& result.hasPersonParticipation(userView.getPerson())) {
				return true;
			}
			return false;
		}
	};
}
