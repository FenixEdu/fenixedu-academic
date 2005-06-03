package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.IBranch;
import net.sourceforge.fenixedu.domain.branch.BranchType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentBranch;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author dcs-rjao
 * 
 * 20/Mar/2003
 */

public class BranchOJB extends PersistentObjectOJB implements IPersistentBranch {
    public BranchOJB() {
    }

    public List readAll() throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        return queryList(Branch.class, crit);

    }

//    public List readByExecutionDegree(IExecutionDegree executionDegree) throws ExcepcaoPersistencia {
//        Criteria crit = new Criteria();
//        crit.addEqualTo("degreeCurricularPlan.idInternal", executionDegree.getDegreeCurricularPlan()
//                .getIdInternal());
//        return queryList(Branch.class, crit);
//
//    }

    public IBranch readByDegreeCurricularPlanAndBranchName(Integer degreeCurricularPlanId,
            String branchName) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("keyDegreeCurricularPlan", degreeCurricularPlanId);
        crit.addEqualTo("name", branchName);
        return (IBranch) queryObject(Branch.class, crit);
    }

    public IBranch readByDegreeCurricularPlanAndCode(Integer degreeCurricularPlanId,
            String code) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("keyDegreeCurricularPlan", degreeCurricularPlanId);
        crit.addEqualTo("code", code);
        return (IBranch) queryObject(Branch.class, crit);
    }

    public List readAllByDegreeCurricularPlanAndBranchType(Integer degreeCurricularPlanId,
            BranchType branchType) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("degreeCurricularPlan.idInternal", degreeCurricularPlanId);
        criteria.addEqualTo("branchType", branchType);
        return queryList(Branch.class, criteria);
    }

}