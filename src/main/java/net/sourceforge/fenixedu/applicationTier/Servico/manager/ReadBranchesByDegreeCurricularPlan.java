package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoBranch;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class ReadBranchesByDegreeCurricularPlan {

    public static List<InfoBranch> run(String idDegreeCurricularPlan) throws FenixServiceException {
        DegreeCurricularPlan degreeCurricularPlan = AbstractDomainObject.fromExternalId(idDegreeCurricularPlan);
        if (degreeCurricularPlan == null) {
            throw new NonExistingServiceException();
        }

        List<Branch> allBranches = degreeCurricularPlan.getAreas();
        if (allBranches == null || allBranches.isEmpty()) {
            return null;
        }

        List<InfoBranch> result = new ArrayList<InfoBranch>(allBranches.size());
        for (Branch branch : allBranches) {
            result.add(InfoBranch.newInfoFromDomain(branch));
        }
        return result;
    }

}