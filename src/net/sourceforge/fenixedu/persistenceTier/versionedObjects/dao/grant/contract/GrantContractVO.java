package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.grant.contract;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.grant.contract.GrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantContractRegime;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContract;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

public class GrantContractVO extends VersionedObjectsBase implements IPersistentGrantContract {

    public IGrantContract readGrantContractByNumberAndGrantOwner(Integer grantContractNumber,
            Integer grantOwnerId) throws ExcepcaoPersistencia {

        List<IGrantContract> grantContracts = (List<IGrantContract>) readAll(GrantContract.class);

        for (IGrantContract contract : grantContracts) {
            if (contract.getContractNumber().equals(grantContractNumber)
                    && contract.getKeyGrantOwner().equals(grantOwnerId)) {
                return contract;
            }
        }

        return null;
    }

    public List readAllContractsByGrantOwner(Integer grantOwnerId) throws ExcepcaoPersistencia {
        List<IGrantContract> grantContracts = (List<IGrantContract>) readAll(GrantContract.class);
        List<IGrantContract> result = null;

        for (IGrantContract contract : grantContracts) {
            if (contract.getKeyGrantOwner().equals(grantOwnerId)) {
                result.add(contract);
            }
        }

        return result;
    }

    public Integer readMaxGrantContractNumberByGrantOwner(Integer grantOwnerId)
            throws ExcepcaoPersistencia {

        List<IGrantContract> grantContracts = (List<IGrantContract>) readAll(GrantContract.class);
        Integer maxGrantContractNumber = 0;

        for (IGrantContract contract : grantContracts) {
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

        List<IGrantContract> grantContracts = (List<IGrantContract>) readAll(GrantContract.class);
        ComparatorChain comparatorChain = new ComparatorChain(new BeanComparator(orderBy), true);
        Collections.sort(grantContracts, comparatorChain);

        List<IGrantContract> result = null;
        Integer desiredContractRegimeState = new Integer(1);
        long now = System.currentTimeMillis();

        for (IGrantContract grantContract : grantContracts) {

            boolean verifiesConditions = true;
            for (IGrantContractRegime grantContractRegime : (List<IGrantContractRegime>) grantContract
                    .getContractRegimes()) {

                if (!grantContractRegime.getState().equals(desiredContractRegimeState)) {
                    verifiesConditions = false;
                }

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
                result.add(grantContract);
                break;
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

        List<IGrantContract> grantContracts = (List<IGrantContract>) readAll(GrantContract.class);

        int result = 0;
        long now = System.currentTimeMillis();

        for (IGrantContract grantContract : grantContracts) {

            boolean verifiesConditions = true;
            for (IGrantContractRegime grantContractRegime : (List<IGrantContractRegime>) grantContract
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