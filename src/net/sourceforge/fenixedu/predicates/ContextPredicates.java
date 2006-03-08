/**
 * 
 */
package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.accessControl.AccessControlPredicate;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ContextPredicates {

    public static final AccessControlPredicate<Context> curricularPlanMemberWritePredicate = new AccessControlPredicate<Context>() {

        public boolean evaluate(Context context) {

            DegreeCurricularPlan parentDegreeCurricularPlan = context.getParentCourseGroup()
                    .getParentDegreeCurricularPlan();
            if (parentDegreeCurricularPlan.getCurricularStage().equals(CurricularStage.OLD)) {
                return true;
            }

            Person person = AccessControl.getUserView().getPerson();
            return parentDegreeCurricularPlan.getCurricularPlanMembersGroup().isMember(person);
        }

    };

}
