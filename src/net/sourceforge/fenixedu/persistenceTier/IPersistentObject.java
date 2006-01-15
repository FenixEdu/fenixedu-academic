package net.sourceforge.fenixedu.persistenceTier;

import java.util.Collection;
import java.util.Iterator;

import net.sourceforge.fenixedu.domain.DomainObject;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author tfc130
 */
public interface IPersistentObject {

    void deleteByOID(Class classToQuery, Integer oid) throws ExcepcaoPersistencia;

    public DomainObject readByOId(DomainObject obj, boolean lockWrite);

    public DomainObject readByOID(Class classToQuery, Integer oid) throws ExcepcaoPersistencia;

    public DomainObject readByOID(Class classToQuery, Integer oid, boolean lockWrite)
            throws ExcepcaoPersistencia;

    public Object lockIteratorNextObj(Iterator iterator) throws ExcepcaoPersistencia;

    public int count(Class classToQuery, Criteria criteria);

    public Collection readAll(Class classToQuery)  throws ExcepcaoPersistencia;
}