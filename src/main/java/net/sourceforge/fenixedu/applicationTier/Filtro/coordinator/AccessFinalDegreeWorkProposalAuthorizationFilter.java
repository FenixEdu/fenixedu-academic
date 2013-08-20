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
import net.sourceforge.fenixedu.domain.ScientificCommission;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.domain.person.RoleType;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author Luis Cruz
 * 
 */
public class AccessFinalDegreeWorkProposalAuthorizationFilter extends DomainObjectAuthorizationFilter {

    public static final AccessFinalDegreeWorkProposalAuthorizationFilter instance =
            new AccessFinalDegreeWorkProposalAuthorizationFilter();

    @Override
    protected RoleType getRoleType() {
        return RoleType.COORDINATOR;
    }

    @Override
    protected boolean verifyCondition(IUserView id, String objectId) {
        if (objectId == null) {
            return false;
        }

        final Proposal proposal = AbstractDomainObject.fromExternalId(objectId);
        if (proposal == null) {
            return false;
        }

        final Person person = id.getPerson();
        if (person == proposal.getOrientator() || person == proposal.getCoorientator()) {
            return true;
        }

        for (final ExecutionDegree executionDegree : proposal.getScheduleing().getExecutionDegreesSet()) {
            for (final Coordinator coordinator : executionDegree.getCoordinatorsListSet()) {
                if (coordinator != null && person == coordinator.getPerson()) {
                    return true;
                }
            }
            for (final ScientificCommission scientificCommission : person.getScientificCommissionsSet()) {
                if (executionDegree == scientificCommission.getExecutionDegree()
                        || (executionDegree.getDegreeCurricularPlan() == scientificCommission.getExecutionDegree()
                                .getDegreeCurricularPlan() && executionDegree.getExecutionYear() == scientificCommission
                                .getExecutionDegree().getExecutionYear().getPreviousExecutionYear())) {
                    return true;
                }
            }
        }

        return false;
    }
}