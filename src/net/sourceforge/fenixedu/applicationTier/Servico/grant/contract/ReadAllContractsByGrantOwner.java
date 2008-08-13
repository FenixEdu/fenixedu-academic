package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContract;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContractWithGrantOwnerAndGrantType;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantCostCenter;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantOrientationTeacher;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantOrientationTeacherWithTeacherAndGrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContractRegime;
import net.sourceforge.fenixedu.domain.grant.owner.GrantOwner;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadAllContractsByGrantOwner extends Service {

    public List run(Integer grantOwnerId) throws FenixServiceException{

        final GrantOwner grantOwner = rootDomainObject.readGrantOwnerByOID(grantOwnerId);
        if (grantOwner == null) {
            throw new FenixServiceException("error.noGrantOwner");
        }

        final List<InfoGrantContract> result = new ArrayList<InfoGrantContract>();
        final Set<GrantContract> contracts = grantOwner.getGrantContractsSet();

        for (final GrantContract grantContract : contracts) {

            final InfoGrantContract infoGrantContract = InfoGrantContractWithGrantOwnerAndGrantType
                    .newInfoFromDomain(grantContract);
            final InfoGrantOrientationTeacher infoOrientationTeacher = InfoGrantOrientationTeacherWithTeacherAndGrantContract
                    .newInfoFromDomain(grantContract.readActualGrantOrientationTeacher());
            infoGrantContract.setGrantOrientationTeacherInfo(infoOrientationTeacher);

            if (grantContract.getGrantCostCenter() != null) {
                final InfoGrantCostCenter infoGrantCostCenter = InfoGrantCostCenter
                        .newInfoFromDomain(grantContract.getGrantCostCenter());
                infoGrantContract.setGrantCostCenterInfo(infoGrantCostCenter);
            }

            /*
             * Verify if the contract is active or not. The contract is active
             * if: 1- The end contract motive is not filled 2 - The actual grant
             * contract regime is active
             */
            if (infoGrantContract.getEndContractMotiveSet()) {
                infoGrantContract.setActive(Boolean.FALSE);
            } else {
                List grantContractRegimeActual = grantContract
                        .readGrantContractRegimeByGrantContractAndState(new Integer(1));
                if (grantContractRegimeActual.isEmpty()) {
                    throw new FenixServiceException(
                            "Grant Contract has no Grant Contract Regime (actual) Associated");
                }
                GrantContractRegime grantContractRegime = (GrantContractRegime) grantContractRegimeActual
                        .get(0);
                infoGrantContract.setActive(grantContractRegime.getContractRegimeActive());
            }
            result.add(infoGrantContract);
        }

        Collections.reverse(result);
        return result;
    }
}