package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.List;

import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.branch.BranchType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentBranch;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class BranchVO extends VersionedObjectsBase implements IPersistentBranch {

	public List readAll() throws ExcepcaoPersistencia {
		return (List)readAll(Branch.class);
	}

	public Branch readByDegreeCurricularPlanAndBranchName(Integer degreeCurricularPlanId, final String branchName) throws ExcepcaoPersistencia {
		DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) readByOID(DegreeCurricularPlan.class,degreeCurricularPlanId);
		
		if (degreeCurricularPlan != null) {
			List<Branch> branches = degreeCurricularPlan.getAreas();
			return (Branch)CollectionUtils.find(branches,new Predicate() {
				public boolean evaluate (Object o) {
					return ((Branch)o).getName().equals(branchName);
				}
			});
		}
		return null;
	}

	public Branch readByDegreeCurricularPlanAndCode(Integer degreeCurricularPlanId, final String code) throws ExcepcaoPersistencia {
		DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) readByOID(DegreeCurricularPlan.class,degreeCurricularPlanId);
		
		if (degreeCurricularPlan != null) {
			List<Branch> branches = degreeCurricularPlan.getAreas();
			return (Branch)CollectionUtils.find(branches,new Predicate() {
				public boolean evaluate (Object o) {
					return ((Branch)o).getCode().equals(code);
				}
			});
		}
		return null;
	}

	public List readAllByDegreeCurricularPlanAndBranchType(Integer degreeCurricularPlanId, final BranchType branchType) throws ExcepcaoPersistencia {
		DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) readByOID(DegreeCurricularPlan.class,degreeCurricularPlanId);
		
		if (degreeCurricularPlan != null) {
			List<Branch> branches = degreeCurricularPlan.getAreas();
			return (List)CollectionUtils.select(branches,new Predicate() {
				public boolean evaluate (Object o) {
					return ((Branch)o).getBranchType().equals(branchType);
				}
			});
		}
		return null;
	}
}
