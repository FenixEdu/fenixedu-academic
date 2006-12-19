/**
 * 
 */
package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class CourseGroupPredicates {

    public static final AccessControlPredicate<CourseGroup> curricularPlanMemberWritePredicate = new AccessControlPredicate<CourseGroup>() {

        public boolean evaluate(CourseGroup cg) {
            
            final DegreeCurricularPlan parentDegreeCurricularPlan = cg.getParentDegreeCurricularPlan();
            if (!parentDegreeCurricularPlan.isBolonha()) {
                return true;
            }

            final Person person = AccessControl.getPerson();
            if(person.hasRole(RoleType.SCIENTIFIC_COUNCIL) || person.hasRole(RoleType.MANAGER) || person.hasRole(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER)) {
                return true;
            }
            
            return parentDegreeCurricularPlan.getCurricularPlanMembersGroup().isMember(person);
        }

    };

}
