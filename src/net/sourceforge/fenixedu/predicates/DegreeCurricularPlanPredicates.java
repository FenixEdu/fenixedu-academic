/**
 * 
 */
package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class DegreeCurricularPlanPredicates {

    public static final AccessControlPredicate<DegreeCurricularPlan> readPredicate = new AccessControlPredicate<DegreeCurricularPlan>() {

        public boolean evaluate(DegreeCurricularPlan dcp) {
            
            if (!dcp.isBolonha()) {
                return true;
            }
            
            Person person = AccessControl.getUserView().getPerson();

            if (person.hasRole(RoleType.SCIENTIFIC_COUNCIL)) {
                return true;
            }

            boolean isCurricularPlanMember = dcp.getCurricularPlanMembersGroup().isMember(person);

            switch (dcp.getCurricularStage()) {
            case DRAFT:
                return isCurricularPlanMember;
            case PUBLISHED:
                return isCurricularPlanMember || person.hasRole(RoleType.BOLONHA_MANAGER);
            case APPROVED:
                return true;
            default:
                return false;
            }

        }

    };

    public static final AccessControlPredicate<DegreeCurricularPlan> scientificCouncilWritePredicate = new AccessControlPredicate<DegreeCurricularPlan>() {

        public boolean evaluate(DegreeCurricularPlan dcp) {
            final Person person = AccessControl.getUserView().getPerson();
            return person.hasRole(RoleType.SCIENTIFIC_COUNCIL) || !dcp.isBolonha();
        }

    };

    public static final AccessControlPredicate<DegreeCurricularPlan> curricularPlanMemberWritePredicate = new AccessControlPredicate<DegreeCurricularPlan>() {

        public boolean evaluate(DegreeCurricularPlan dcp) {
            return !dcp.isBolonha() || dcp.getCurricularPlanMembersGroup().isMember(AccessControl.getUserView().getPerson());
        }

    };

}
