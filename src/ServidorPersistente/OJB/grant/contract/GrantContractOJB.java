package ServidorPersistente.OJB.grant.contract;

import java.util.Calendar;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import DataBeans.grant.list.InfoSpanByCriteriaListGrantOwner;
import Dominio.grant.contract.GrantContract;
import Dominio.grant.contract.IGrantContract;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.grant.IPersistentGrantContract;

/**
 * @author Barbosa
 * @author Pica
 */

public class GrantContractOJB extends ServidorPersistente.OJB.ObjectFenixOJB
        implements IPersistentGrantContract {

    public GrantContractOJB() {
    }

    public IGrantContract readGrantContractByNumberAndGrantOwner(
            Integer grantContractNumber, Integer grantOwnerId)
            throws ExcepcaoPersistencia {
        IGrantContract grantContract = null;

        Criteria criteria = new Criteria();
        criteria.addEqualTo("number", grantContractNumber);
        criteria.addEqualTo("key_grant_owner", grantOwnerId);
        grantContract = (IGrantContract) queryObject(GrantContract.class,
                criteria);
        return grantContract;
    }

    public List readAllContractsByGrantOwner(Integer grantOwnerId)
            throws ExcepcaoPersistencia {
        List contractsList = null;
        Criteria criteria = new Criteria();
        criteria.addEqualTo("key_grant_owner", grantOwnerId);
        criteria.addOrderBy("number", false);
        contractsList = queryList(GrantContract.class, criteria);
        return contractsList;
    }

    public Integer readMaxGrantContractNumberByGrantOwner(Integer grantOwnerId)
            throws ExcepcaoPersistencia {
        IGrantContract grantContract = new GrantContract();
        Integer maxGrantContractNumber = new Integer(0);

        Criteria criteria = new Criteria();
        criteria.addEqualTo("key_grant_owner", grantOwnerId);
        criteria.addOrderBy("number", false);
        grantContract = (IGrantContract) queryObject(GrantContract.class,
                criteria);
        if (grantContract != null)
                maxGrantContractNumber = grantContract.getContractNumber();
        return maxGrantContractNumber;
    }

    public List readAll() throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        return queryList(GrantContract.class, criteria);
    }
    
    public List readAllActiveContractsByGrantOwner(Integer grantOwnerId) throws ExcepcaoPersistencia
	{
    	Criteria criteria = new Criteria();
    	criteria.addEqualTo("key_grant_owner", grantOwnerId);
    	criteria.addEqualTo("contractRegimes.state",new Integer(1));
    	criteria.addLessOrEqualThan("contractRegimes.dateEndContract",Calendar.getInstance().getTime());
    	return queryList(GrantContract.class, criteria);
	}
    
    public List readAllContractsByGrantOwnerAndCriteria(Integer grantOwnerId, InfoSpanByCriteriaListGrantOwner infoSpanByCriteriaListGrantOwner) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("key_grant_owner", grantOwnerId);
        criteria.addEqualTo("contractRegimes.state", new Integer(1));
        
        if(infoSpanByCriteriaListGrantOwner.getJustActiveContract() != null && 
                infoSpanByCriteriaListGrantOwner.getJustActiveContract().booleanValue()) { 
            criteria.addGreaterOrEqualThan("contractRegimes.dateEndContract",Calendar.getInstance().getTime());
            criteria.addEqualTo("endContractMotive","");
        }
        if(infoSpanByCriteriaListGrantOwner.getJustDesactiveContract() != null && 
                infoSpanByCriteriaListGrantOwner.getJustDesactiveContract().booleanValue()) {
            criteria.addLessOrEqualThan("contractRegimes.dateEndContract",Calendar.getInstance().getTime());
            criteria.addNotEqualTo("endContractMotive","");
        }
        
        if(infoSpanByCriteriaListGrantOwner.getBeginContract() != null) {
            criteria.addGreaterOrEqualThan("contractRegimes.dateBeginContract",infoSpanByCriteriaListGrantOwner.getBeginContract());
        } 
        if(infoSpanByCriteriaListGrantOwner.getEndContract() != null) {
            criteria.addLessOrEqualThan("contractRegimes.dateEndContract",infoSpanByCriteriaListGrantOwner.getEndContract());
        }
            
        return queryList(GrantContract.class, criteria);
    }
}
