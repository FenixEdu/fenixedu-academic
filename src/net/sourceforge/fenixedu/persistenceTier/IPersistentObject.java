package net.sourceforge.fenixedu.persistenceTier;

import java.util.Iterator;

import net.sourceforge.fenixedu.domain.IDomainObject;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author tfc130
 */
public interface IPersistentObject {

    void deleteByOID(Class classToQuery, Integer oid) throws ExcepcaoPersistencia;

    void lockWrite(Object obj) throws ExcepcaoPersistencia;

    /**
     * Reads an object from db using his <code>Identity</code>.
     * 
     * @param obj
     *            Domain object to read from database
     * @param lockWrite
     *            tells if after reading the object it should be marked with a
     *            WRITE <code>true</code> or READ <code>false</code> lock.
     * @return Object that we want to read
     * @deprecated
     */
    public IDomainObject readByOId(IDomainObject obj, boolean lockWrite);

    /**
     * Only locks the object for write. <b>Doesn't do anything else </b>
     * 
     * @param obj
     *            object to lock
     * @throws ExcepcaoPersistencia
     *             when can't lock object.
     */
    void simpleLockWrite(IDomainObject obj) throws ExcepcaoPersistencia;

    public IDomainObject readByOID(Class classToQuery, Integer oid) throws ExcepcaoPersistencia;

    public IDomainObject readByOID(Class classToQuery, Integer oid, boolean lockWrite)
            throws ExcepcaoPersistencia;

    public Object lockIteratorNextObj(Iterator iterator) throws ExcepcaoPersistencia;

    public int count(Class classToQuery, Criteria criteria);

    public IDomainObject materialize(IDomainObject domainObject);
}