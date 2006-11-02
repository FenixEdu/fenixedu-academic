/*
 * Created on 2004/03/12
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.coordinator;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.framework.DomainObjectAuthorizationFilter;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.domain.person.RoleType;

/**
 * @author Luis Cruz
 * 
 */
public class AccessFinalDegreeWorkProposalAuthorizationFilter extends DomainObjectAuthorizationFilter {

    protected RoleType getRoleType() {
        return RoleType.COORDINATOR;
    }

    protected boolean verifyCondition(IUserView id, Integer objectId) {
        if (objectId == null) {
            return false;
        }

        final Proposal proposal = rootDomainObject.readProposalByOID(objectId);
        if (proposal == null) {
            return false;
        }

        final Person person = id.getPerson();
        final Teacher teacher = person == null ? null : person.getTeacher();

        if (teacher == proposal.getOrientator() || teacher == proposal.getCoorientator()) {
            return true;
        }

        for (final ExecutionDegree executionDegree : proposal.getScheduleing().getExecutionDegreesSet()) { 
            for (final Coordinator coordinator : executionDegree.getCoordinatorsListSet()) {
                if (coordinator != null && person == coordinator.getPerson()) {
                    return true;
                }
            }
        }

        return false;
    }
}