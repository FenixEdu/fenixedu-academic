/*
 * Created on 28/Jul/2003, 11:01:39
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.persistenceTier.OJB.Seminaries;

import java.util.List;

import net.sourceforge.fenixedu.domain.Seminaries.ISeminary;
import net.sourceforge.fenixedu.domain.Seminaries.Seminary;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminary;

import org.apache.ojb.broker.query.Criteria;

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