package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.grant.contract;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.grant.contract.GrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContractRegime;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContract;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

public class GrantContractVO extends VersionedObjectsBase implements IPersistentGrantContract {

    public List readAllContractsByGrantOwner(Integer grantOwnerId) throws ExcepcaoPersistencia {
        List<GrantContract> grantContracts = (List<GrantContract>) readAll(GrantContract.class);
        List<GrantContract> result = null;

        for (GrantContract contract : grantContracts) {
            if (contract.getKeyGrantOwner().equals(grantOwnerId)) {
                result.add(contract);
            }
        }

        return result;
    }

    public Integer readMaxGrantContractNumberByGrantOwner(Integer grantOwnerId)
            throws ExcepcaoPersistencia {

        List<GrantContract> grantContracts = (List<GrantContract>) readAll(GrantContract.class);
        Integer maxGrantContractNumber = 0;

        for (GrantContract contract : grantContracts) {
            if (contract.getKeyGrantOwner().equals(grantOwnerId)
                    && contract.getContractNumber() > maxGrantContractNumber) {
                maxGrantContractNumber = contract.getContractNumber();
            }
        }

        return maxGrantContractNumber;
    }

    public Integer countAllByCriteria(Boolean justActiveContracts, Boolean justDesactiveContracts,
            Date dateBeginContract, Date dateEndContract, Integer grantTypeId) {

        List<GrantContract> grantContracts = (List<GrantContract>) readAll(GrantContract.class);

        int result = 0;
        long now = System.currentTimeMillis();

        for (GrantContract grantContract : grantContracts) {

            boolean verifiesConditions = true;
            for (GrantContractRegime grantContractRegime : grantContract
                    .getContractRegimes()) {

                if (justActiveContracts != null
                        && justActiveContracts.booleanValue()
                        && (grantContractRegime.getDateEndContract().getTime() < now || !grantContract
                                .getEndContractMotive().equals(""))) {
                    verifiesConditions = false;
                }

                if (justDesactiveContracts != null
                        && justDesactiveContracts.booleanValue()
                        && (grantContractRegime.getDateEndContract().getTime() > now || grantContract
                                .getEndContractMotive().equals(""))) {
                    verifiesConditions = false;
                }

                if (dateBeginContract != null
                        && grantContractRegime.getDateBeginContract().getTime() < dateBeginContract
                                .getTime()) {
                    verifiesConditions = false;
                }

                if (dateEndContract != null
                        && grantContractRegime.getDateEndContract().getTime() > dateEndContract
                                .getTime()) {
                    verifiesConditions = false;
                }

                if (grantTypeId != null
                        && !grantContract.getGrantType().getIdInternal().equals(grantTypeId)) {
                    verifiesConditions = false;
                }

                if (verifiesConditions) {
                    break;
                }
                verifiesConditions = true;
            }

            if (verifiesConditions) {
                result++;
                break;
            }
        }

        return result;
    }

}