package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.candidate;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoBranch;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class GetBranchListByCandidateID extends Service {

	public List<InfoBranch> run(Integer candidateID) throws FenixServiceException, ExcepcaoPersistencia {
        List<InfoBranch> result = new ArrayList<InfoBranch>();
        
        MasterDegreeCandidate masterDegreeCandidate = rootDomainObject.readMasterDegreeCandidateByOID(candidateID);
		List<Branch> branches = masterDegreeCandidate.getExecutionDegree().getDegreeCurricularPlan().getAreas();
		if (branches == null) {
			InfoBranch infoBranch = new InfoBranch();
			infoBranch.setName("Tronco Comum");
			result.add(infoBranch);
			return result;
		}

		for (Branch branch : branches) {
			InfoBranch infoBranch = InfoBranch.newInfoFromDomain(branch);
			if ((infoBranch.getName() == null) || (infoBranch.getName().length() == 0)) {
				// FIXME: Common branch
				infoBranch.setName("Tronco Comum");
			}
			result.add(infoBranch);
		}

		return result;
	}
    
}
