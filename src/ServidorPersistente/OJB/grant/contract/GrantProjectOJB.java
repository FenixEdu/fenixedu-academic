/*
 * Created on Jan 21, 2004
 */
package ServidorPersistente.OJB.grant.contract;

import org.apache.ojb.broker.query.Criteria;

import Dominio.grant.contract.GrantProject;
import Dominio.grant.contract.IGrantProject;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;
import ServidorPersistente.grant.IPersistentGrantProject;

/**
 * @author pica
 * @author barbosa
 */
public class GrantProjectOJB extends ObjectFenixOJB implements
        IPersistentGrantProject {

    public IGrantProject readGrantProjectByNumber(String number)
            throws ExcepcaoPersistencia {
        IGrantProject grantProject = null;
        Criteria criteria = new Criteria();
        criteria.addEqualTo("number", number);
        criteria
                .addEqualTo("class_name", "Dominio.grant.contract.GrantProject");
        grantProject = (IGrantProject) queryObject(GrantProject.class, criteria);
        return grantProject;
    }
}
