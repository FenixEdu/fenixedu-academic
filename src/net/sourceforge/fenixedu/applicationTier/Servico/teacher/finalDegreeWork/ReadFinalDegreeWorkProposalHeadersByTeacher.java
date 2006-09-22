/*
 * Created on 2004/03/13
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.finalDegreeWork;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.FinalDegreeWorkProposalHeader;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Scheduleing;

/**
 * @author Luis Cruz
 */
public class ReadFinalDegreeWorkProposalHeadersByTeacher extends Service {

    public List run(Integer teacherOID) throws FenixServiceException {
	final List<FinalDegreeWorkProposalHeader> result = new ArrayList<FinalDegreeWorkProposalHeader>();

	final Teacher teacher = rootDomainObject.readTeacherByOID(teacherOID);

	for (final Proposal proposal : teacher.findFinalDegreeWorkProposals()) {
	    final Scheduleing scheduleing = proposal.getScheduleing();

	    for (final ExecutionDegree executionDegree : scheduleing.getExecutionDegrees()) {
		result.add(FinalDegreeWorkProposalHeader.newInfoFromDomain(proposal, executionDegree));
	    }
	}

	return result;
    }

}
