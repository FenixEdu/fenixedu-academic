package ServidorPersistente.OJB.grant.owner;

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
        if (grantOwner != null) maxGrantOwnerNumber = grantOwner.getNumber();
        return maxGrantOwnerNumber;
    }

    public List readAll() throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        return queryList(GrantOwner.class, criteria);
    }
}
