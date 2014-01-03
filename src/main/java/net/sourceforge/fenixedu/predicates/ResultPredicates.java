package net.sourceforge.fenixedu.predicates;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.research.AuthorGroup;
import net.sourceforge.fenixedu.domain.research.result.ResearchResult;
import net.sourceforge.fenixedu.domain.research.result.ResearchResultDocumentFile;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation;
import net.sourceforge.fenixedu.domain.research.result.ResultUnitAssociation;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

public class ResultPredicates {

    /**
     * Predicates to access Result objects.
     */
    public static final AccessControlPredicate<ResearchResult> createPredicate = new AccessControlPredicate<ResearchResult>() {
        @Override
        public boolean evaluate(ResearchResult result) {
            final User userView = Authenticate.getUser();
            if (userView != null
                    && (userView.getPerson().hasRole(RoleType.SCIENTIFIC_COUNCIL) || ((userView.getPerson().hasRole(
                            RoleType.RESEARCHER) || new AuthorGroup().allows(userView)) && !result.hasAnyResultParticipations()))) {
                return true;
            }
            return false;
        }
    };

    public static final AccessControlPredicate<Object> author = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(Object result) {
            final User userView = Authenticate.getUser();
            if (userView != null
                    && (userView.getPerson().hasRole(RoleType.SCIENTIFIC_COUNCIL)
                            || userView.getPerson().hasRole(RoleType.RESEARCHER) || new AuthorGroup().allows(userView))) {
                return true;
            }
            return false;
        }
    };

    public static final AccessControlPredicate<ResearchResult> writePredicate = new AccessControlPredicate<ResearchResult>() {
        @Override
        public boolean evaluate(ResearchResult result) {
            final User userView = Authenticate.getUser();
            return result.isEditableByCurrentUser()
                    || (userView != null && userView.getPerson().hasRole(RoleType.SCIENTIFIC_COUNCIL));
        }
    };

    /**
     * Predicates to access ResultUnitAssociation objects.
     */
    public static final AccessControlPredicate<ResultUnitAssociation> unitWritePredicate =
            new AccessControlPredicate<ResultUnitAssociation>() {
                @Override
                public boolean evaluate(ResultUnitAssociation association) {
                    return writePredicate.evaluate(association.getResult());
                }
            };

    /**
     * Predicates to access ResultParticipation objects.
     */
    public static final AccessControlPredicate<ResultParticipation> participationWritePredicate =
            new AccessControlPredicate<ResultParticipation>() {
                @Override
                public boolean evaluate(ResultParticipation participation) {
                    return writePredicate.evaluate(participation.getResult());
                }
            };

    /**
     * Predicates to access ResultDocumentFile objects.
     */
    public static final AccessControlPredicate<ResearchResultDocumentFile> documentFileWritePredicate =
            new AccessControlPredicate<ResearchResultDocumentFile>() {
                @Override
                public boolean evaluate(ResearchResultDocumentFile documentFile) {
                    return writePredicate.evaluate(documentFile.getResult());
                }
            };
}
