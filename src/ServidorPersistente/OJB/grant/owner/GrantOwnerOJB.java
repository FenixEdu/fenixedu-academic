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
import ServidorPersistente.OJB.PersistentObjectOJB;
import ServidorPersistente.grant.IPersistentGrantOwner;
import Util.TipoDocumentoIdentificacao;

/**
 * @author Barbosa
 * @author Pica
 */

public class GrantOwnerOJB extends PersistentObjectOJB implements IPersistentGrantOwner {

    public GrantOwnerOJB() {
    }

    public IGrantOwner readGrantOwnerByNumber(Integer grantOwnerNumber) throws ExcepcaoPersistencia {
        IGrantOwner grantOwner = null;

        Criteria criteria = new Criteria();
        criteria.addEqualTo("number", grantOwnerNumber);
        grantOwner = (IGrantOwner) queryObject(GrantOwner.class, criteria);
        return grantOwner;
    }

    public IGrantOwner readGrantOwnerByPerson(Integer personIdInternal) throws ExcepcaoPersistencia {
        IGrantOwner grantOwner = null;
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyPerson", personIdInternal);
        grantOwner = (IGrantOwner) queryObject(GrantOwner.class, criteria);
        return grantOwner;
    }

    public List readGrantOwnerByPersonName(String personName) throws ExcepcaoPersistencia {
        List grantOwnerList = null;
        Criteria criteria = new Criteria();
        criteria.addLike("person.nome", personName);
        grantOwnerList = queryList(GrantOwner.class, criteria);
        return grantOwnerList;
    }

    public List readGrantOwnerByPersonName(String personName, Integer startIndex,
            Integer numberOfElementsInSpan) throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addLike("person.nome", personName);
        if (startIndex != null && numberOfElementsInSpan != null) {
            return readInterval(GrantOwner.class, criteria, numberOfElementsInSpan, startIndex);
        }
        return queryList(GrantOwner.class, criteria);
    }

    public Integer countAllGrantOwnerByName(String personName) {
        Criteria criteria = new Criteria();
        criteria.addLike("person.nome", personName);
        return new Integer(count(GrantOwner.class, criteria));
    }

    public IGrantOwner readGrantOwnerByPersonID(String idNumber, TipoDocumentoIdentificacao idType)
            throws ExcepcaoPersistencia {
        IGrantOwner grantOwner = null;
        Criteria criteria = new Criteria();
        criteria.addEqualTo("person.numeroDocumentoIdentificacao", idNumber);
        criteria.addEqualTo("person.tipoDocumentoIdentificacao", idType);
        grantOwner = (IGrantOwner) queryObject(GrantOwner.class, criteria);
        return grantOwner;
    }

    public Integer readMaxGrantOwnerNumber() throws ExcepcaoPersistencia {
        IGrantOwner grantOwner = null;
        Integer maxGrantOwnerNumber = null;

        grantOwner = (IGrantOwner) queryObject(GrantOwner.class, new Criteria(), "number", false);
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
        return readSpan(GrantOwner.class, criteria, numberOfElementsInSpan, spanNumber);
    }

    public Integer countAll() {
        return new Integer(count(GrantOwner.class, new Criteria()));
    }

    private List readBySpanAndCriteria(Integer spanNumber, Integer numberOfElementsInSpan,
            Criteria criteria, String orderBy, boolean reverseOrder) {

        List result = new ArrayList();

        Iterator iter = readIteratorByCriteria(GrantOwner.class, criteria, orderBy, reverseOrder);

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

    public List readAllGrantOwnersBySpan(Integer spanNumber, Integer numberOfElementsInSpan,
            String orderBy) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        return readBySpanAndCriteria(spanNumber, numberOfElementsInSpan, criteria, orderBy, true);
    }

    public Integer countAllByCriteria(Boolean justActiveContracts, Boolean justDesactiveContracts,
            Date dateBeginContract, Date dateEndContract, Integer grantTypeId)
            throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();

        if (justActiveContracts != null && justActiveContracts.booleanValue()) {
            criteria.addGreaterOrEqualThan("grantContracts.contractRegimes.dateEndContract", Calendar
                    .getInstance().getTime());
            criteria.addEqualTo("grantContracts.endContractMotive", "");
        }
        if (justDesactiveContracts != null && justDesactiveContracts.booleanValue()) {
            criteria.addLessOrEqualThan("grantContracts.contractRegimes.dateEndContract", Calendar
                    .getInstance().getTime());
            criteria.addNotEqualTo("grantContracts.endContractMotive", "");
        }

        if (dateBeginContract != null) {
            criteria.addGreaterOrEqualThan("grantContracts.contractRegimes.dateBeginContract",
                    dateBeginContract);
        }
        if (dateEndContract != null) {
            criteria.addLessOrEqualThan("grantContracts.contractRegimes.dateEndContract",
                    dateEndContract);
        }

        if (grantTypeId != null) {
            criteria.addEqualTo("grantContracts.grantType.idInternal", grantTypeId);
        }
        return new Integer(count(GrantOwner.class, criteria, new Boolean(true)));
    }

}