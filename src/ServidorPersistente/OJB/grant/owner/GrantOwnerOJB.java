package ServidorPersistente.OJB.grant.owner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.grant.owner.GrantOwner;
import Dominio.grant.owner.IGrantOwner;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.grant.IPersistentGrantOwner;
import Util.TipoDocumentoIdentificacao;

/**
 * @author Barbosa
 * @author Pica
 */

public class GrantOwnerOJB extends ServidorPersistente.OJB.ObjectFenixOJB
        implements IPersistentGrantOwner {

    public GrantOwnerOJB() {
    }

    public IGrantOwner readGrantOwnerByNumber(Integer grantOwnerNumber)
            throws ExcepcaoPersistencia {
        IGrantOwner grantOwner = null;

        Criteria criteria = new Criteria();
        criteria.addEqualTo("number", grantOwnerNumber);
        grantOwner = (IGrantOwner) queryObject(GrantOwner.class, criteria);
        return grantOwner;
    }

    public IGrantOwner readGrantOwnerByPerson(Integer personIdInternal)
            throws ExcepcaoPersistencia {
        IGrantOwner grantOwner = null;
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyPerson", personIdInternal);
        grantOwner = (IGrantOwner) queryObject(GrantOwner.class, criteria);
        return grantOwner;
    }

    public List readGrantOwnerByPersonName(String personName)
            throws ExcepcaoPersistencia {
        List grantOwnerList = null;
        Criteria criteria = new Criteria();
        criteria.addLike("person.nome", personName);
        grantOwnerList = (List) queryList(GrantOwner.class, criteria);
        return grantOwnerList;
    }

    public IGrantOwner readGrantOwnerByPersonID(String idNumber,
            TipoDocumentoIdentificacao idType) throws ExcepcaoPersistencia {
        IGrantOwner grantOwner = null;
        Criteria criteria = new Criteria();
        criteria.addEqualTo("person.numeroDocumentoIdentificacao", idNumber);
        criteria.addEqualTo("person.tipoDocumentoIdentificacao", idType);
        grantOwner = (IGrantOwner) queryObject(GrantOwner.class, criteria);
        return grantOwner;
    }

    public Integer readMaxGrantOwnerNumber() throws ExcepcaoPersistencia {
        IGrantOwner grantOwner = new GrantOwner();
        Integer maxGrantOwnerNumber = new Integer(0);

        Criteria criteria = new Criteria();
        criteria.addOrderBy("number", false);
        grantOwner = (IGrantOwner) queryObject(GrantOwner.class, criteria);
        if (grantOwner != null)
            maxGrantOwnerNumber = grantOwner.getNumber();
        return maxGrantOwnerNumber;
    }

    public List readAll() throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        return queryList(GrantOwner.class, criteria);
    }

    public List readAllBySpan(Integer spanNumber, Integer numberOfElementsInSpan)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        return readSpan(GrantOwner.class, criteria, numberOfElementsInSpan,
                spanNumber);
    }

    public Integer countAll() {
        return new Integer(count(GrantOwner.class, new Criteria()));
    }

    private List readBySpanAndCriteria(Integer spanNumber,
            Integer numberOfElementsInSpan, Criteria criteria)
            throws ExcepcaoPersistencia {
        
        List result = new ArrayList();

        Iterator iter = readIteratorByCriteria(GrantOwner.class, criteria);

        int begin = (spanNumber.intValue() - 1) * numberOfElementsInSpan.intValue();
        int end = begin + numberOfElementsInSpan.intValue();

        if (begin != 0) {
            for (int j = 0; j < (begin - 1) && iter.hasNext(); j++) {
                iter.next();
            }
        }

        for (int i = begin; i < end && iter.hasNext(); i++) {
            IGrantOwner grantOwner = (IGrantOwner) iter.next();
            result.add(grantOwner);
        }
        return result;
    }

    public List readAllGrantOwnersBySpan(Integer spanNumber,
            Integer numberOfElementsInSpan, String orderBy)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addOrderBy(orderBy, true);
        return readBySpanAndCriteria(spanNumber, numberOfElementsInSpan,criteria);
    }
    
    public List readAllGrantOwnersBySpanAndCriteria(String orderBy, Boolean justActiveContracts, Boolean justDesactiveContracts, Date beginContract, Date endContract, Integer spanNumber, Integer numberOfElementsInSpan) 
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addOrderBy(orderBy, true);
        
        criteria.addEqualTo("contractRegimes.state", new Integer(1));
        
        if(justActiveContracts != null && justActiveContracts.booleanValue()) { 
            criteria.addGreaterOrEqualThan("contractRegimes.dateEndContract",Calendar.getInstance().getTime());
            criteria.addEqualTo("endContractMotive","");
        }
        
        if(justDesactiveContracts != null && justDesactiveContracts.booleanValue()) {
            criteria.addLessOrEqualThan("contractRegimes.dateEndContract",Calendar.getInstance().getTime());
            criteria.addNotEqualTo("endContractMotive","");
        }
        
        if(beginContract != null) {
            criteria.addGreaterOrEqualThan("contractRegimes.dateBeginContract",beginContract);
        } 
        if(endContract != null) {
            criteria.addLessOrEqualThan("contractRegimes.dateEndContract",endContract);
        }
        return readBySpanAndCriteria(spanNumber, numberOfElementsInSpan,criteria);        
    }

}