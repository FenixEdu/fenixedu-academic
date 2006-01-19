/*
 * Created on 14/Mar/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.candidate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoBranch;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class GetBranchListByCandidateID implements IService {

	public List run(Integer candidateID) throws FenixServiceException, ExcepcaoPersistencia {

		ISuportePersistente sp = null;
		List result = null;

		sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

		MasterDegreeCandidate masterDegreeCandidate = (MasterDegreeCandidate) sp
				.getIPersistentMasterDegreeCandidate().readByOID(MasterDegreeCandidate.class,
						candidateID);
		// result =
		// sp.getIPersistentBranch().readByExecutionDegree(masterDegreeCandidate.getExecutionDegree());
		result = masterDegreeCandidate.getExecutionDegree().getDegreeCurricularPlan().getAreas();

		List branchList = new ArrayList();
		if (result == null) {
			InfoBranch infoBranch = new InfoBranch();
			infoBranch.setName("Tronco Comum");
			branchList.add(infoBranch);
			return branchList;
		}

		Iterator iterator = result.iterator();

		while (iterator.hasNext()) {
			Branch branch = (Branch) iterator.next();
			InfoBranch infoBranch = InfoBranch.newInfoFromDomain(branch);

			if ((infoBranch.getName() == null) || (infoBranch.getName().length() == 0)) {

				// FIXME: Common branch
				infoBranch.setName("Tronco Comum");
			}
			branchList.add(infoBranch);
		}

		return branchList;
	}
}