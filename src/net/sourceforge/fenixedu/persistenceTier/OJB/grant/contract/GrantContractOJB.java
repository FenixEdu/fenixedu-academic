package net.sourceforge.fenixedu.persistenceTier.OJB.grant.contract;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.grant.contract.GrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantContract;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContract;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author Barbosa
 * @author Pica
 */

public class GrantContractOJB extends PersistentObjectOJB implements IPersistentGrantContract {

    public IGrantContract readGrantContractByNumberAndGrantOwner(Integer grantContractNumber,
            Integer grantOwnerId) throws ExcepcaoPersistencia {
        IGrantContract grantContract = null;

        Criteria criteria = new Criteria();
        criteria.addEqualTo("number", grantContractNumber);
        criteria.addEqualTo("key_grant_owner", grantOwnerId);
        grantContract = (IGrantContract) queryObject(GrantContract.class, criteria);
        return grantContract;
    }

    public List readAllContractsByGrantOwner(Integer grantOwnerId) throws ExcepcaoPersistencia {
        List contractsList = null;
        Criteria criteria = new Criteria();
        criteria.addEqualTo("key_grant_owner", grantOwnerId);
        contractsList = queryList(GrantContract.class, criteria, "number", false);
        return contractsList;
    }

    public Integer readMaxGrantContractNumberByGrantOwner(Integer grantOwnerId)
            throws ExcepcaoPersistencia {
        Integer maxGrantContractNumber = new Integer(0);

        Criteria criteria = new Criteria();
        criteria.addEqualTo("key_grant_owner", grantOwnerId);
        IGrantContract grantContract = (IGrantContract) queryObject(GrantContract.class, criteria, "number", false);
        if (grantContract != null)
            maxGrantContractNumber = grantContract.getContractNumber();
        return maxGrantContractNumber;
    }

    public List readAll() throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        return queryList(GrantContract.class, criteria);
    }

    public List readAllContractsByCriteria(String orderBy, Boolean justActiveContracts,
            Boolean justDesactiveContracts, Date dateBeginContract, Date dateEndContract,
            Integer spanNumber, Integer numberOfElementsInSpan, Integer grantTypeId) {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("contractRegimes.state", new Integer(1));

        if (justActiveContracts != null && justActiveContracts.booleanValue()) {
            criteria.addGreaterOrEqualThan("contractRegimes.dateEndContract", Calendar.getInstance()
                    .getTime());
            criteria.addEqualTo("endContractMotive", "");
        }
        if (justDesactiveContracts != null && justDesactiveContracts.booleanValue()) {
            criteria.addLessOrEqualThan("contractRegimes.dateEndContract", Calendar.getInstance()
                    .getTime());
            criteria.addNotEqualTo("endContractMotive", "");
        }

        if (dateBeginContract != null) {
            criteria.addGreaterOrEqualThan("contractRegimes.dateBeginContract", dateBeginContract);
        }
        if (dateEndContract != null) {
            criteria.addLessOrEqualThan("contractRegimes.dateEndContract", dateEndContract);
        }

        if (grantTypeId != null) {
            criteria.addEqualTo("grantType.idInternal", grantTypeId);
        }

        return readBySpanAndCriteria(spanNumber, numberOfElementsInSpan, criteria, orderBy, true);
    }

    private List readBySpanAndCriteria(Integer spanNumber, Integer numberOfElementsInSpan,
            Criteria criteria, String orderBy, boolean reverseOrder) {

        List result = new ArrayList();

        Iterator iter = readIteratorByCriteria(GrantContract.class, criteria, orderBy, reverseOrder);

        int begin = (spanNumber.intValue() - 1) * numberOfElementsInSpan.intValue();
        int end = begin + numberOfElementsInSpan.intValue();

        if (begin != 0) {
            for (int j = 0; j < (begin - 1) && iter.hasNext(); j++) {
                iter.next();
            }
        }

        for (int i = begin; i < end && iter.hasNext(); i++) {
            IGrantContract grantContract = (IGrantContract) iter.next();
            result.add(grantContract);
        }
        return result;
    }

    public Integer countAll() {
        return new Integer(count(GrantContract.class, new Criteria()));
    }

    public Integer countAllByCriteria(Boolean justActiveContracts, Boolean justDesactiveContracts,
            Date dateBeginContract, Date dateEndContract, Integer grantTypeId) {

        Criteria criteria = new Criteria();

        if (justActiveContracts != null && justActiveContracts.booleanValue()) {
            criteria.addGreaterOrEqualThan("contractRegimes.dateEndContract", Calendar.getInstance()
                    .getTime());
            criteria.addEqualTo("endContractMotive", "");
        }
        if (justDesactiveContracts != null && justDesactiveContracts.booleanValue()) {
            criteria.addLessOrEqualThan("contractRegimes.dateEndContract", Calendar.getInstance()
                    .getTime());
            criteria.addNotEqualTo("endContractMotive", "");
        }

        if (dateBeginContract != null) {
            criteria.addGreaterOrEqualThan("contractRegimes.dateBeginContract", dateBeginContract);
        }
        if (dateEndContract != null) {
            criteria.addLessOrEqualThan("contractRegimes.dateEndContract", dateEndContract);
        }

        if (grantTypeId != null) {
            criteria.addEqualTo("grantType.idInternal", grantTypeId);
        }
        return new Integer(count(GrantContract.class, criteria));
    }
}
