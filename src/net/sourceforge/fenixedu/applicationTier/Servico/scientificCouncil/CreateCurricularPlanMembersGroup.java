/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlanMembersGroup;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IRole;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class CreateCurricularPlanMembersGroup implements IService {

    public IDegreeCurricularPlanMembersGroup run(IDegreeCurricularPlan degreeCurricularPlan) throws ExcepcaoPersistencia {

        IPerson creator = AccessControl.getUserView().getPerson();
        if (!creator.hasRole(RoleType.BOLONHA_MANAGER)) {
            final ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            final IRole bolonhaRole = persistentSuport.getIPersistentRole().readByRoleType(RoleType.BOLONHA_MANAGER);

            creator.addPersonRoles(bolonhaRole);    
        }
        
        return (degreeCurricularPlan.getCurricularPlanMembersGroup() == null) ? DomainFactory
                .makeDegreeCurricularPlanMembersGroup(creator, degreeCurricularPlan)
                : degreeCurricularPlan.getCurricularPlanMembersGroup();
    }

}
