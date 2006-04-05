package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.DeleteDomainObjectService;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContractMovement;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContractRegime;
import net.sourceforge.fenixedu.domain.grant.contract.GrantOrientationTeacher;
import net.sourceforge.fenixedu.domain.grant.contract.GrantSubsidy;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantOrientationTeacher;

public class DeleteGrantContract extends DeleteDomainObjectService {

    protected Class getDomainObjectClass() {
        return GrantContract.class;
    }

    protected IPersistentObject getIPersistentObject() {
        return persistentSupport.getIPersistentGrantContract();
    }

    protected void doBeforeDelete(DomainObject domainObject, ISuportePersistente persistentSupport) throws ExcepcaoPersistencia {
        GrantContract grantContract = (GrantContract) domainObject;

        deleteAssociatedOrientationTeacher(grantContract);
        deleteAssociatedRegimes(grantContract);
        deleteAssociatedMovements(grantContract);
        deleteAssociatedSubsidiesAndParts(grantContract);
        deleteAssociatedInsurance(grantContract);
    }

    private void deleteAssociatedOrientationTeacher(GrantContract grantContract) throws ExcepcaoPersistencia {
        IPersistentGrantOrientationTeacher pgot = persistentSupport.getIPersistentGrantOrientationTeacher();
        GrantOrientationTeacher grantOrientationTeacher = pgot.readActualGrantOrientationTeacherByContract(grantContract.getIdInternal(), Integer.valueOf(0));
        if (grantOrientationTeacher != null)
            grantOrientationTeacher.delete();
    }

    private void deleteAssociatedInsurance(GrantContract grantContract) throws ExcepcaoPersistencia {
        if (grantContract.getGrantInsurance() != null) {
            grantContract.getGrantInsurance().delete();
        }
    }

    private void deleteAssociatedSubsidiesAndParts(GrantContract grantContract) throws ExcepcaoPersistencia {
        for (GrantSubsidy grantSubsidy : grantContract.getAssociatedGrantSubsidies()) {
            grantSubsidy.delete();
        }
    }

    private void deleteAssociatedMovements(GrantContract grantContract) throws ExcepcaoPersistencia {
        for (GrantContractMovement grantContractMovement : grantContract.getAssociatedGrantContractMovements()) {
            grantContractMovement.delete();
        }
    }

    private void deleteAssociatedRegimes(GrantContract grantContract) throws ExcepcaoPersistencia {        
        List<GrantContractRegime> regimesList = grantContract.readGrantContractRegimeByGrantContract();
        if (regimesList != null) {
            for (GrantContractRegime grantContractRegime : regimesList) {
                grantContractRegime.delete();
            }
        }
    }
	
	protected void deleteDomainObject(DomainObject domainObject) throws ExcepcaoPersistencia {
        GrantContract grantContract = (GrantContract) domainObject;
        grantContract.delete();
	}

}
