package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.candidate;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoBranch;
import net.sourceforge.fenixedu.dataTransferObject.InfoBranchEditor;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class GetBranchListByCandidateID extends FenixService {

	@Checked("RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE")
	@Service
	public static List<InfoBranch> run(Integer candidateID) throws FenixServiceException {
		List<InfoBranch> result = new ArrayList<InfoBranch>();

		MasterDegreeCandidate masterDegreeCandidate = rootDomainObject.readMasterDegreeCandidateByOID(candidateID);
		List<Branch> branches = masterDegreeCandidate.getExecutionDegree().getDegreeCurricularPlan().getAreas();
		if (branches == null) {
			InfoBranchEditor infoBranch = new InfoBranchEditor();
			infoBranch.setName("Tronco Comum");
			result.add(infoBranch);
			return result;
		}

		for (Branch branch : branches) {
			InfoBranch infoBranch = InfoBranch.newInfoFromDomain(branch);
			result.add(infoBranch);
		}

		return result;
	}

}