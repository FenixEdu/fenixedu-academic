package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.DeleteDomainObjectService;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContractMovement;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContractRegime;
import net.sourceforge.fenixedu.domain.grant.contract.GrantOrientationTeacher;
import net.sourceforge.fenixedu.domain.grant.contract.GrantSubsidy;

public class DeleteGrantContract extends DeleteDomainObjectService {

    protected void doBeforeDelete(DomainObject domainObject) {
        GrantContract grantContract = (GrantContract) domainObject;

        deleteAssociatedOrientationTeacher(grantContract);
        deleteAssociatedRegimes(grantContract);
        deleteAssociatedMovements(grantContract);
        deleteAssociatedSubsidiesAndParts(grantContract);
        deleteAssociatedInsurance(grantContract);
    }

    private void deleteAssociatedOrientationTeacher(GrantContract grantContract) {
        GrantOrientationTeacher grantOrientationTeacher = grantContract
                .readActualGrantOrientationTeacher();
        if (grantOrientationTeacher != null)
            grantOrientationTeacher.delete();
    }

    private void deleteAssociatedInsurance(GrantContract grantContract) {
        if (grantContract.getGrantInsurance() != null) {
            grantContract.getGrantInsurance().delete();
        }
    }

    private void deleteAssociatedSubsidiesAndParts(GrantContract grantContract) {
        for (GrantSubsidy grantSubsidy : grantContract.getAssociatedGrantSubsidies()) {
            grantSubsidy.delete();
        }
    }

    private void deleteAssociatedMovements(GrantContract grantContract) {
        for (GrantContractMovement grantContractMovement : grantContract
                .getAssociatedGrantContractMovements()) {
            grantContractMovement.delete();
        }
    }

    private void deleteAssociatedRegimes(GrantContract grantContract) {
        List<GrantContractRegime> regimesList = grantContract.readGrantContractRegimeByGrantContract();
        if (regimesList != null) {
            for (GrantContractRegime grantContractRegime : regimesList) {
                grantContractRegime.delete();
            }
        }
    }

    protected void deleteDomainObject(DomainObject domainObject) {
        GrantContract grantContract = (GrantContract) domainObject;
        grantContract.delete();
    }

	@Override
	protected DomainObject readDomainObject(Integer idInternal) {
		return rootDomainObject.readGrantContractByOID(idInternal);
	}

}
