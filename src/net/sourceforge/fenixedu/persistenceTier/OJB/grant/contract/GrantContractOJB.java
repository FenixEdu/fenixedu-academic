package net.sourceforge.fenixedu.persistenceTier.OJB.grant.contract;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.grant.contract.GrantContract;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContract;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author Barbosa
 * @author Pica
 */

public class GrantContractOJB extends PersistentObjectOJB implements IPersistentGrantContract {
   
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
        GrantContract grantContract = (GrantContract) queryObject(GrantContract.class, criteria, "number", false);
        if (grantContract != null)
            maxGrantContractNumber = grantContract.getContractNumber();
        return maxGrantContractNumber;
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
