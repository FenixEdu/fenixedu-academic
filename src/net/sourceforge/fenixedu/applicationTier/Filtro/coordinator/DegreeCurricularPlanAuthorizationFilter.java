/*
 * Created on Oct 24, 2005
 *  by jdnf
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.coordinator;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.framework.DomainObjectAuthorizationFilter;
import net.sourceforge.fenixedu.domain.ICoordinator;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

public class DegreeCurricularPlanAuthorizationFilter extends DomainObjectAuthorizationFilter {

    @Override
    protected RoleType getRoleType() {
        return RoleType.COORDINATOR;
    }

    @Override
    protected boolean verifyCondition(IUserView id, Integer degreeCurricularPlanID)
            throws ExcepcaoPersistencia {

        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final ITeacher teacher = persistentSupport.getIPersistentTeacher().readTeacherByUsername(
                id.getUtilizador());

        for (final ICoordinator coordinator : teacher.getCoordinators()) {
            if (coordinator.getExecutionDegree().getDegreeCurricularPlan().getIdInternal().equals(
                    degreeCurricularPlanID)) {
                return true;
            }
        }
        return false;
    }

}
