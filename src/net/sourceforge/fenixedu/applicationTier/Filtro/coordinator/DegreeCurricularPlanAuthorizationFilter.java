/*
 * Created on Oct 24, 2005
 *  by jdnf
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.coordinator;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.framework.DomainObjectAuthorizationFilter;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DegreeCurricularPlanAuthorizationFilter extends DomainObjectAuthorizationFilter {

    @Override
    protected RoleType getRoleType() {
        return RoleType.COORDINATOR;
    }

    @Override
    protected boolean verifyCondition(IUserView id, Integer degreeCurricularPlanID)
            throws ExcepcaoPersistencia {
        final Person person = id.getPerson();
        final Teacher teacher = person == null ? null : person.getTeacher();

        for (final Coordinator coordinator : teacher.getCoordinators()) {
            if (coordinator.getExecutionDegree().getDegreeCurricularPlan().getIdInternal().equals(
                    degreeCurricularPlanID)) {
                return true;
            }
        }
        return false;
    }

}
