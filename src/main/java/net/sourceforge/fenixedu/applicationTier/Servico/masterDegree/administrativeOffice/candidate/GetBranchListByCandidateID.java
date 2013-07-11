package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.candidate;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoBranch;
import net.sourceforge.fenixedu.dataTransferObject.InfoBranchEditor;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class GetBranchListByCandidateID {

    @Checked("RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Atomic
    public static List<InfoBranch> run(String candidateID) throws FenixServiceException {
        List<InfoBranch> result = new ArrayList<InfoBranch>();

        MasterDegreeCandidate masterDegreeCandidate = FenixFramework.getDomainObject(candidateID);
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