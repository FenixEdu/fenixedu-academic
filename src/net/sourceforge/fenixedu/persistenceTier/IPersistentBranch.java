package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.IBranch;
import net.sourceforge.fenixedu.domain.branch.BranchType;

/**
 * 
 * @author dcs-rjao
 * 
 * 25/Mar/2003
 */

public interface IPersistentBranch extends IPersistentObject {

    public List readAll() throws ExcepcaoPersistencia;

    public IBranch readByDegreeCurricularPlanAndBranchName(Integer degreeCurricularPlanId,
            String branchName) throws ExcepcaoPersistencia;

    public IBranch readByDegreeCurricularPlanAndCode(Integer degreeCurricularPlanId,
            String code) throws ExcepcaoPersistencia;

    public List readAllByDegreeCurricularPlanAndBranchType(Integer degreeCurricularPlanId,
            BranchType branchType) throws ExcepcaoPersistencia;
}