/**
 * 
 */
package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.accessControl.AccessControlPredicate;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class CourseGroupPredicates {

    public static final AccessControlPredicate<CourseGroup> curricularPlanMemberWritePredicate = new AccessControlPredicate<CourseGroup>() {

        public boolean evaluate(CourseGroup cg) {

            
            DegreeCurricularPlan parentDegreeCurricularPlan = cg.getParentDegreeCurricularPlan();
            if (parentDegreeCurricularPlan.getCurricularStage().equals(CurricularStage.OLD)) {
                return true;
            }

            Person person = AccessControl.getUserView().getPerson();
            return parentDegreeCurricularPlan.getCurricularPlanMembersGroup().isMember(person);
        }

    };

}
