/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlanMembersGroup;
import net.sourceforge.fenixedu.domain.IPerson;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class CreateCurricularPlanMembersGroup implements IService {

    public IDegreeCurricularPlanMembersGroup run(IDegreeCurricularPlan degreeCurricularPlan) {

        IPerson creator = AccessControl.getUserView().getPerson();

        return (degreeCurricularPlan.getCurricularPlanMembersGroup() == null) ? DomainFactory
                .makeDegreeCurricularPlanMembersGroup(creator, degreeCurricularPlan)
                : degreeCurricularPlan.getCurricularPlanMembersGroup();
    }

}
