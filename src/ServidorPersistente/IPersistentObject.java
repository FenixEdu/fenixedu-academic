package ServidorPersistente;

import java.util.Iterator;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IDomainObject;

/**
 * @author tfc130
 */
public interface IPersistentObject
{
    List readByCriteria(Object obj) throws ExcepcaoPersistencia;

    Object readDomainObjectByCriteria(Object obj) throws ExcepcaoPersistencia;

    void deleteByCriteria(Object obj) throws ExcepcaoPersistencia;

    void deleteByOID(Class classToQuery, Integer oid)
            throws ExcepcaoPersistencia;

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
     * Only locks the object for write. <b>Doesn't do anything else</b>
     * 
     * @param obj
     *            object to lock
     * @throws ExcepcaoPersistencia
     *             when can't lock object.
     */
    void simpleLockWrite(IDomainObject obj) throws ExcepcaoPersistencia;

    public IDomainObject readByOID(Class classToQuery, Integer oid)
            throws ExcepcaoPersistencia;

    public Object lockIteratorNextObj(Iterator iterator)
            throws ExcepcaoPersistencia;

    public int count(Class classToQuery, Criteria criteria);

    public Iterator readSpanIterator(Class classToQuery, Criteria criteria,
            Integer numberOfElementsInSpan, Integer spanNumber);
    
    public Iterator readIteratorByCriteria(Class classToQuery, Criteria criteria);
}
