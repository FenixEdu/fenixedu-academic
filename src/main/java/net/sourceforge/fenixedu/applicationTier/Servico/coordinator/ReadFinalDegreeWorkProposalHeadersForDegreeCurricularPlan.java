package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.coordinator.ExecutionDegreeCoordinatorOrScientificCouncilmemberAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.gep.GEPAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.FinalDegreeWorkProposalHeader;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import pt.ist.fenixframework.Atomic;

public class ReadFinalDegreeWorkProposalHeadersForDegreeCurricularPlan {

    protected List run(final ExecutionDegree executionDegree) {
        final List<FinalDegreeWorkProposalHeader> result = new ArrayList<FinalDegreeWorkProposalHeader>();

        if (executionDegree.hasScheduling()) {
            final Collection<Proposal> finalDegreeWorkProposals = executionDegree.getScheduling().getProposals();

            for (final Proposal proposal : finalDegreeWorkProposals) {
                result.add(FinalDegreeWorkProposalHeader.newInfoFromDomain(proposal, executionDegree));
            }
        }

        return result;
    }

    // Service Invokers migrated from Berserk

    private static final ReadFinalDegreeWorkProposalHeadersForDegreeCurricularPlan serviceInstance =
            new ReadFinalDegreeWorkProposalHeadersForDegreeCurricularPlan();

    @Atomic
    public static List runReadFinalDegreeWorkProposalHeadersForDegreeCurricularPlan(ExecutionDegree executionDegree)
            throws NotAuthorizedException {
        try {
            ExecutionDegreeCoordinatorOrScientificCouncilmemberAuthorizationFilter.instance.execute(executionDegree);
            return serviceInstance.run(executionDegree);
        } catch (NotAuthorizedException ex1) {
            try {
                GEPAuthorizationFilter.instance.execute();
                return serviceInstance.run(executionDegree);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

}