/*
 * Created on 2004/03/13
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.finalDegreeWork;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.FinalDegreeWorkProposalHeader;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Scheduleing;
import pt.ist.fenixframework.Atomic;

/**
 * @author Luis Cruz
 */
public class ReadFinalDegreeWorkProposalHeadersByTeacher {

    @Atomic
    public static List run(final Person person) throws FenixServiceException {
        final List<FinalDegreeWorkProposalHeader> result = new ArrayList<FinalDegreeWorkProposalHeader>();
        for (final Proposal proposal : person.findFinalDegreeWorkProposals()) {
            final Scheduleing scheduleing = proposal.getScheduleing();

            for (final ExecutionDegree executionDegree : scheduleing.getExecutionDegrees()) {
                result.add(FinalDegreeWorkProposalHeader.newInfoFromDomain(proposal, executionDegree));
            }
        }

        return result;
    }

}