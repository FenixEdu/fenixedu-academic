package net.sourceforge.fenixedu.persistenceTier.OJB.grant.contract;

import java.util.List;

import net.sourceforge.fenixedu.domain.grant.contract.GrantType;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantType;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author Barbosa
 * @author Pica
 */

public class GrantTypeOJB extends PersistentObjectOJB implements IPersistentGrantType {

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