/**
 * 
 */
package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ContextPredicates {

    public static final AccessControlPredicate<Context> curricularPlanMemberWritePredicate = new AccessControlPredicate<Context>() {

        public boolean evaluate(Context context) {

            DegreeCurricularPlan parentDegreeCurricularPlan = context.getParentCourseGroup()
                    .getParentDegreeCurricularPlan();
            if (!parentDegreeCurricularPlan.isBolonha()) {
                return true;
            }

            Person person = AccessControl.getPerson();
            if (person.hasRole(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER) || person.hasRole(RoleType.MANAGER)) {
                return true;
            }
            
            return parentDegreeCurricularPlan.getCurricularPlanMembersGroup().isMember(person);
        }

    };

}
