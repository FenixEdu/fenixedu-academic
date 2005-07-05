/*
 * Created on 17/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.IBranch;
import net.sourceforge.fenixedu.domain.ICurricularCourseGroup;
import net.sourceforge.fenixedu.domain.ICurricularCourseScope;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlanLEEC;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlanLEIC;
import net.sourceforge.fenixedu.domain.finalDegreeWork.IProposal;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentBranch;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourseGroup;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourseScope;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author lmac1
 */

public class DeleteBranches implements IService {

    // delete a set of branches
    public List run(List internalIds, Boolean forceDelete) throws FenixServiceException {

        try {

            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentBranch persistentBranch = sp.getIPersistentBranch();
            IPersistentStudentCurricularPlan persistentStudentCurricularPlan = sp
                    .getIStudentCurricularPlanPersistente();

            Iterator iter = internalIds.iterator();

            List undeletedCodes = new ArrayList();
            Integer internalId;
            IBranch branch;

            while (iter.hasNext()) {
                internalId = (Integer) iter.next();
                branch = (IBranch) persistentBranch.readByOID(Branch.class, internalId);
                if (branch != null) {
                    if (branch.getStudentCurricularPlans().isEmpty()) {
						dereferenceBranch(sp,branch);
                        persistentBranch.deleteByOID(Branch.class,branch.getIdInternal());
                    } else {
                        if (forceDelete.booleanValue() == true) {
                            dereferenceStudentCurricularPlansFromBranch(persistentStudentCurricularPlan, branch);
							dereferenceBranch(sp,branch);
                            persistentBranch.deleteByOID(Branch.class,branch.getIdInternal());
                        } else {
                            undeletedCodes.add(branch.getCode());
                        }
                    }
                }
            }

            return undeletedCodes;

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

    }

	private void dereferenceStudentCurricularPlansFromBranch(IPersistentStudentCurricularPlan persistentStudentCurricularPlan, IBranch branch) throws ExcepcaoPersistencia {
		List<IStudentCurricularPlan> scps = branch.getStudentCurricularPlans();
		for (IStudentCurricularPlan scp : scps) {
			persistentStudentCurricularPlan.lockWrite(scp);
		    scp.setBranch(null);
		}
	}

	private void dereferenceBranch(ISuportePersistente sp, IBranch branch) throws ExcepcaoPersistencia {
		
		branch.getDegreeCurricularPlan().getAreas().remove(branch);
		
		IPersistentStudentCurricularPlan persistentSCP = sp.getIStudentCurricularPlanPersistente();
		List<IStudentCurricularPlanLEIC> scpsLEIC = branch.getSecundaryStudentCurricularPlansLEIC();
		for(IStudentCurricularPlan scp : scpsLEIC) {
			persistentSCP.simpleLockWrite(scp);
			scp.setSecundaryBranch(null);
		}
		
		List<IStudentCurricularPlanLEEC> scpsLEEC = branch.getSecundaryStudentCurricularPlansLEEC();
		for(IStudentCurricularPlan scp : scpsLEEC) {
			persistentSCP.simpleLockWrite(scp);
			scp.setSecundaryBranch(null);
		}
		
		IPersistentCurricularCourseGroup persistentCurricularCourseGroup = sp.getIPersistentCurricularCourseGroup();
		List<ICurricularCourseGroup> curricularCourseGroups = branch.getCurricularCourseGroups();
		for (ICurricularCourseGroup curricularCourseGroup : curricularCourseGroups) {
			persistentCurricularCourseGroup.simpleLockWrite(curricularCourseGroup);
			curricularCourseGroup.setBranch(null);
		}
		
		IPersistentCurricularCourseScope persistentCurricularCourseScope = sp.getIPersistentCurricularCourseScope();
		List<ICurricularCourseScope> scopes = branch.getScopes();
		for (ICurricularCourseScope scope : scopes) {
			persistentCurricularCourseScope.simpleLockWrite(scope);
			scope.setBranch(null);
		}
		
		List<IProposal> proposals = branch.getAssociatedFinalDegreeWorkProposals();
		for (IProposal proposal : proposals) {
			proposal.getBranches().remove(branch);
		}
	}

}