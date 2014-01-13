/*
 * Created on Oct 24, 2005
 *  by jdnf
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.coordinator;

import net.sourceforge.fenixedu.applicationTier.Filtro.framework.DomainObjectAuthorizationFilter;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.fenixedu.bennu.core.domain.User;

public class DegreeCurricularPlanAuthorizationFilter extends DomainObjectAuthorizationFilter {

    public static final DegreeCurricularPlanAuthorizationFilter instance = new DegreeCurricularPlanAuthorizationFilter();

    @Override
    protected RoleType getRoleType() {
        return RoleType.COORDINATOR;
    }

    @Override
    protected boolean verifyCondition(User id, String degreeCurricularPlanID) {
        final Person person = id.getPerson();
        final Teacher teacher = person == null ? null : person.getTeacher();

        for (final Coordinator coordinator : person.getCoordinators()) {
            if (coordinator.getExecutionDegree().getDegreeCurricularPlan().getExternalId().equals(degreeCurricularPlanID)) {
                return true;
            }
        }
        return false;
    }

}
