package ServidorPersistente.OJB.grant.contract;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.grant.contract.GrantType;
import Dominio.grant.contract.IGrantType;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;
import ServidorPersistente.grant.IPersistentGrantType;

/**
 * @author Barbosa
 * @author Pica
 */

public class GrantTypeOJB extends ObjectFenixOJB implements IPersistentGrantType {

    public GrantTypeOJB() {
    }

    public IGrantType readGrantTypeBySigla(String sigla) throws ExcepcaoPersistencia {
        IGrantType grantType = null;

        Criteria criteria = new Criteria();
        criteria.addEqualTo("sigla", sigla);
        grantType = (IGrantType) queryObject(GrantType.class, criteria);
        return grantType;
    }

    public List readAll() throws ExcepcaoPersistencia {
        List grantTypes = queryList(GrantType.class, null, "sigla", true);
        return grantTypes;
    }
}