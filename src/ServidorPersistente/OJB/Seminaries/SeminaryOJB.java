/*
 * Created on 28/Jul/2003, 11:01:39
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorPersistente.OJB.Seminaries;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.Seminaries.ISeminary;
import Dominio.Seminaries.Seminary;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.PersistentObjectOJB;
import ServidorPersistente.Seminaries.IPersistentSeminary;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 28/Jul/2003, 11:01:39
 *  
 */
public class SeminaryOJB extends PersistentObjectOJB implements IPersistentSeminary {
    public ISeminary readByName(String name) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("name", name);
        return (ISeminary) super.queryObject(Seminary.class, criteria);
    }

    public List readAll() throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        return super.queryList(Seminary.class, criteria);
    }

    public void delete(ISeminary seminary) throws ExcepcaoPersistencia {
        super.deleteByOID(Seminary.class, seminary.getIdInternal());
    }
}