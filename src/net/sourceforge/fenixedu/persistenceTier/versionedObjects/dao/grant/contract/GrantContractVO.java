package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.grant.contract;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.grant.contract.GrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContractRegime;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContract;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

public class GrantContractVO extends VersionedObjectsBase implements IPersistentGrantContract {

    public GrantContract readGrantContractByNumberAndGrantOwner(Integer grantContractNumber,
            Integer grantOwnerId) throws ExcepcaoPersistencia {

        List<GrantContract> grantContracts = (List<GrantContract>) readAll(GrantContract.class);

        for (GrantContract contract : grantContracts) {
            if (contract.getContractNumber().equals(grantContractNumber)
                    && contract.getKeyGrantOwner().equals(grantOwnerId)) {
                return contract;
            }
        }

        return null;
    }

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

    public List readAll() throws ExcepcaoPersistencia {
        return (List) readAll(GrantContract.class);
    }

    public List readAllContractsByCriteria(String orderBy, Boolean justActiveContracts,
            Boolean justDesactiveContracts, Date dateBeginContract, Date dateEndContract,
            Integer spanNumber, Integer numberOfElementsInSpan, Integer grantTypeId) {

        List<GrantContract> grantContracts = (List<GrantContract>) readAll(GrantContract.class);
        ComparatorChain comparatorChain = new ComparatorChain(new BeanComparator(orderBy), true);
        Collections.sort(grantContracts, comparatorChain);

        List<GrantContract> result = new ArrayList();
        Integer desiredContractRegimeState = Integer.valueOf(1);
        long now = System.currentTimeMillis();

        for (GrantContract grantContract : grantContracts) {
            boolean verifiesConditions = true;

            for (GrantContractRegime grantContractRegime : grantContract
                    .getContractRegimes()) {

                if (!grantContractRegime.getState().equals(desiredContractRegimeState)) {
                    verifiesConditions = false;
                    break;
                }

                if (!grantContract.getEndContractMotive().equals("")) {
                    verifiesConditions = false;
                    break;
                }

                if (justActiveContracts != null
                        && justActiveContracts.booleanValue()
                        && (grantContractRegime.getDateEndContract().getTime() < now || !grantContract
                                .getEndContractMotive().equals(""))) {
                    verifiesConditions = false;
                    break;
                }

                if (justDesactiveContracts != null
                        && justDesactiveContracts.booleanValue()
                        && (grantContractRegime.getDateEndContract().getTime() > now || grantContract
                                .getEndContractMotive().equals(""))) {
                    verifiesConditions = false;
                    break;
                }

                if (dateBeginContract != null
                        && grantContractRegime.getDateBeginContract().getTime() < dateBeginContract
                                .getTime()) {
                    verifiesConditions = false;
                    break;
                }

                if (dateEndContract != null
                        && grantContractRegime.getDateEndContract().getTime() > dateEndContract
                                .getTime()) {
                    verifiesConditions = false;
                    break;
                }

                if (grantTypeId != null
                        && !grantContract.getGrantType().getIdInternal().equals(grantTypeId)) {
                    verifiesConditions = false;
                    break;
                }
            }

            if (verifiesConditions) {
                result.add(grantContract);
            }
        }

        int begin = (spanNumber - 1) * numberOfElementsInSpan;
        int end = begin + numberOfElementsInSpan;

        return result.subList(begin, end);
    }

    public Integer countAll() {
        return readAll(GrantContract.class).size();
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