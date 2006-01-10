/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlanMembersGroup;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
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

    public DegreeCurricularPlanMembersGroup run(DegreeCurricularPlan degreeCurricularPlan) throws ExcepcaoPersistencia {

        Person creator = AccessControl.getUserView().getPerson();
        if (!creator.hasRole(RoleType.BOLONHA_MANAGER)) {
            final ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            final Role bolonhaRole = persistentSuport.getIPersistentRole().readByRoleType(RoleType.BOLONHA_MANAGER);

            creator.addPersonRoles(bolonhaRole);    
        }
        
        return (degreeCurricularPlan.getCurricularPlanMembersGroup() == null) ? DomainFactory
                .makeDegreeCurricularPlanMembersGroup(creator, degreeCurricularPlan)
                : degreeCurricularPlan.getCurricularPlanMembersGroup();
    }

}
