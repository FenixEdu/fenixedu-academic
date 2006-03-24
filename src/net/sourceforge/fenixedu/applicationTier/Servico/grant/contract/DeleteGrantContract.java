package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.DeleteDomainObjectService;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContractMovement;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContractRegime;
import net.sourceforge.fenixedu.domain.grant.contract.GrantInsurance;
import net.sourceforge.fenixedu.domain.grant.contract.GrantOrientationTeacher;
import net.sourceforge.fenixedu.domain.grant.contract.GrantPart;
import net.sourceforge.fenixedu.domain.grant.contract.GrantSubsidy;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContractRegime;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantInsurance;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantOrientationTeacher;

public class DeleteGrantContract extends DeleteDomainObjectService {

    protected Class getDomainObjectClass() {
        return GrantContract.class;
    }

    protected IPersistentObject getIPersistentObject(ISuportePersistente persistentSupport) {
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
        GrantOrientationTeacher grantOrientationTeacher = pgot.readActualGrantOrientationTeacherByContract(grantContract.getIdInternal(), new Integer(0));
        if (grantOrientationTeacher != null)
            pgot.deleteByOID(GrantOrientationTeacher.class, grantOrientationTeacher.getIdInternal());
    }

    private void deleteAssociatedInsurance(GrantContract grantContract) throws ExcepcaoPersistencia {
        IPersistentGrantInsurance persistentGrantInsurance = persistentSupport.getIPersistentGrantInsurance();
        GrantInsurance grantInsurance = persistentGrantInsurance.readGrantInsuranceByGrantContract(grantContract.getIdInternal());
        if (grantInsurance != null)
            persistentGrantInsurance.deleteByOID(GrantInsurance.class, grantInsurance.getIdInternal());
    }

    private void deleteAssociatedSubsidiesAndParts(GrantContract grantContract) throws ExcepcaoPersistencia {
        List<GrantSubsidy> subsidiesList = grantContract.getAssociatedGrantSubsidies();
        
        for (GrantSubsidy grantSubsidy : subsidiesList) {
            persistentObject.deleteByOID(GrantSubsidy.class, grantSubsidy.getIdInternal());
            
            List<GrantPart> partsList = grantSubsidy.getAssociatedGrantParts();
            for (GrantPart grantPart : partsList) {
                persistentObject.deleteByOID(GrantPart.class, grantPart.getIdInternal());
            }
        }
    }

    private void deleteAssociatedMovements(GrantContract grantContract) throws ExcepcaoPersistencia {
        List<GrantContractMovement> movementsList = grantContract.getAssociatedGrantContractMovements();
        for (GrantContractMovement grantContractMovement : movementsList) {
            persistentObject.deleteByOID(GrantContractMovement.class, grantContractMovement.getIdInternal());
        }
    }

    private void deleteAssociatedRegimes(GrantContract grantContract) throws ExcepcaoPersistencia {
        IPersistentGrantContractRegime pgcr = persistentSupport.getIPersistentGrantContractRegime();
        List<GrantContractRegime> regimesList = pgcr.readGrantContractRegimeByGrantContract(grantContract.getIdInternal());
        if (regimesList != null) {
            for (GrantContractRegime grantContractRegime : regimesList) {
                persistentObject.deleteByOID(GrantContractRegime.class, grantContractRegime.getIdInternal());
            }
        }
    }
	
	protected void deleteDomainObject(DomainObject domainObject) throws ExcepcaoPersistencia {
	    persistentObject.deleteByOID(getDomainObjectClass(), domainObject.getIdInternal());
	}

}
