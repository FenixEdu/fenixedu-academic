/*
 * ObjectFenixOJB.java Created on 22 de Agosto de 2002, 23:51
 */

package net.sourceforge.fenixedu.persistenceTier.OJB;

/**
 * @author ars
 */

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.stm.Transaction;

import org.apache.ojb.broker.Identity;
import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.core.proxy.ProxyHelper;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;


public abstract class ObjectFenixOJB implements IPersistentObject {

    /** Creates a new instance of ObjectFenixOJB */
    public ObjectFenixOJB() {
    }

    public static PersistenceBroker getCurrentPersistenceBroker() {
	return Transaction.getOJBBroker();
    }

    protected void lockRead(List list) {
    }

    public void lockWrite(Object obj) {
    }

    protected void lockRead(Object obj) {
    }

    /**
     * @deprecated
     */
    public void simpleLockWrite(IDomainObject obj) {
    }

    /**
     * Locks to WRITE and delete the object from database...
     * 
     * @param obj
     *            Object to delete
     * @throws ExcepcaoPersistencia
     */
    public void delete(Object obj) throws ExcepcaoPersistencia {
	Transaction.deleteObject(obj);
    }

    public final void deleteByOID(Class classToQuery, Integer oid) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("idInternal", oid);
        Object object = queryObject(classToQuery, criteria);
        delete(object);
    }

    /**
     * @see IPersistentObject#readByOId(IDomainObject, boolean)
     * @deprecated
     */
    public IDomainObject readByOId(IDomainObject obj, boolean lockWrite) {
        PersistenceBroker broker = getCurrentPersistenceBroker();
        Identity identity = new Identity(obj, broker);

        return (IDomainObject) broker.getObjectByIdentity(identity);
    }

    //    public Object readDomainObjectByCriteria(Object obj) throws
    // ExcepcaoPersistencia {
    //        List result = readByCriteria(obj);
    //        if (result != null && !result.isEmpty())
    //            return result.get(0);
    //        return null;
    //    }

    protected List queryList(Class classToQuery, Criteria criteria) throws ExcepcaoPersistencia {
        return queryList(classToQuery, criteria, false);
    }

    protected List queryList(Class classToQuery, Criteria criteria, String orderByString,
            boolean reverseOrder) throws ExcepcaoPersistencia {
        return queryList(classToQuery, criteria, false, orderByString, reverseOrder);
    }

    protected List queryList(Class classToQuery, Criteria criteria, boolean distinct)
            throws ExcepcaoPersistencia {
        return queryList(classToQuery, criteria, distinct, null, false);
    }

    protected List queryList(Class classToQuery, Criteria criteria, boolean distinct,
            String orderByString, boolean reverseOrder) throws ExcepcaoPersistencia {

        QueryByCriteria queryCriteria = new QueryByCriteria(classToQuery, criteria, distinct);
        if (orderByString != null) {
            queryCriteria.addOrderBy(orderByString, reverseOrder);
        }

        return queryList(queryCriteria);
    }

    protected Object queryObject(Class classToQuery, Criteria criteria) throws ExcepcaoPersistencia {
        PersistenceBroker pb = getCurrentPersistenceBroker();
        Query query = getQuery(classToQuery, criteria);
        return pb.getObjectByQuery(query);
    }

    /**
     * @author Pica&Barbosa
     * @param classToQuery
     * @param criteria,
     *            orderBy, reverseOrder
     * @return @throws
     *         ExcepcaoPersistencia
     */
    protected Object queryObject(Class classToQuery, Criteria criteria, String orderBy,
            boolean reverseOrder) throws ExcepcaoPersistencia {
        PersistenceBroker pb = getCurrentPersistenceBroker();
        Query query = getQuery(classToQuery, criteria);

        if (orderBy != null) {
            ((QueryByCriteria) query).addOrderBy(orderBy, reverseOrder);
        }

        Object object = pb.getObjectByQuery(query);

        //        if (object instanceof MasterDegreeCandidate || object instanceof
        // CandidateSituation
        //                || object instanceof Summary || object instanceof Person || object
        // instanceof Advisory) {
        //            Object newObject = ((TransactionImpl) odmg.currentTransaction())
        //                    .getObjectByIdentity(new Identity(object, pb));
        //            if (newObject != null) {
        //                object = newObject;
        //            }
        //
        //        }
        return object;
    }

    public IDomainObject readByOID(Class classToQuery, Integer oid) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("idInternal", oid);
        return (IDomainObject) queryObject(classToQuery, criteria);
    }

    public IDomainObject readByOID(Class classToQuery, Integer oid, boolean lockWrite)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("idInternal", oid);
        return (IDomainObject) queryObject(classToQuery, criteria);
    }

    /**
     * Use this method to return numberOfElementsInSpan elements starting at
     * index spanNumber * numberOfElementsInSpan
     * 
     * @param classToQuery
     *            class that is to be queried
     * @param criteria
     *            criteria used to query class
     * @param numberOfElementsInSpan
     *            number of elements to return
     * @param spanNumber
     *            starts at 0
     * @return numberOfElementsInSpan elements
     * @throws ExcepcaoPersistencia
     * @see ObjectFenixOJB#lockRead(List)
     * @throws IllegalArgumentException
     *             if numberOfElementsInSpan is null or is equal to 0
     */
    public List readSpan(Class classToQuery, Criteria criteria, Integer numberOfElementsInSpan,
            Integer spanNumber) throws ExcepcaoPersistencia {
        if (numberOfElementsInSpan == null || numberOfElementsInSpan.intValue() == 0) {
            throw new IllegalArgumentException("Invalid numberOfElementsInSpan!");
        }
        PersistenceBroker pb = getCurrentPersistenceBroker();
        Query query = getQuery(classToQuery, criteria);

        int startIndex = 1;
        if (spanNumber.intValue() != 0) {
            startIndex = spanNumber.intValue() * numberOfElementsInSpan.intValue();
        }

        query.setStartAtIndex(startIndex);

        int endIndex = startIndex + numberOfElementsInSpan.intValue() - 1;

        query.setEndAtIndex(endIndex);
        List list = (List) pb.getCollectionByQuery(query);
        return list;
    }

    protected List queryList(Query query) throws ExcepcaoPersistencia {
        return (List) getCurrentPersistenceBroker().getCollectionByQuery(query);
    }

    public List readInterval(Class classToQuery, Criteria criteria, Integer numberOfElementsInSpan,
            Integer startIndex) throws ExcepcaoPersistencia {
        if (numberOfElementsInSpan == null || numberOfElementsInSpan.intValue() == 0) {
            throw new IllegalArgumentException("Invalid numberOfElementsInSpan!");
        }
        PersistenceBroker pb = getCurrentPersistenceBroker();
        Query query = getQuery(classToQuery, criteria);
        query.setStartAtIndex(startIndex.intValue());
        query.setEndAtIndex(startIndex.intValue() + numberOfElementsInSpan.intValue());

        List list = (List) pb.getCollectionByQuery(query);
        return list;
    }

    public List readInterval(Class classToQuery, Criteria criteria, Integer numberOfElementsInSpan,
            Integer startIndex, String orderByString, boolean reverseOrder) throws ExcepcaoPersistencia {
        if (numberOfElementsInSpan == null || numberOfElementsInSpan.intValue() == 0) {
            throw new IllegalArgumentException("Invalid numberOfElementsInSpan!");
        }

        QueryByCriteria queryCriteria = new QueryByCriteria(classToQuery, criteria);
        if (orderByString != null) {
            queryCriteria.addOrderBy(orderByString, reverseOrder);
        }
        
        queryCriteria.setStartAtIndex(startIndex.intValue());
        queryCriteria.setEndAtIndex(startIndex.intValue() + numberOfElementsInSpan.intValue());

        return queryList(queryCriteria);

    }
    public List readInterval(Class classToQuery, Criteria criteria, Integer numberOfElementsInSpan,
            Integer startIndex, String orderByString, boolean reverseOrder,Boolean groupBy) throws ExcepcaoPersistencia {
        if (numberOfElementsInSpan == null || numberOfElementsInSpan.intValue() == 0) {
            throw new IllegalArgumentException("Invalid numberOfElementsInSpan!");
        }

        QueryByCriteria queryCriteria = new QueryByCriteria(classToQuery, criteria);
        if (orderByString != null) {
            queryCriteria.addOrderBy(orderByString, reverseOrder);
        }
        if (groupBy != null){
        	queryCriteria.setDistinct(groupBy);
        }
        queryCriteria.setStartAtIndex(startIndex.intValue());
        queryCriteria.setEndAtIndex(startIndex.intValue() + numberOfElementsInSpan.intValue());

        return queryList(queryCriteria);

    }

    public Iterator readSpanIterator(Class classToQuery, Criteria criteria,
            Integer numberOfElementsInSpan, Integer spanNumber) {
        if (numberOfElementsInSpan == null || numberOfElementsInSpan.intValue() == 0) {
            throw new IllegalArgumentException("Invalid numberOfElementsInSpan!");
        }
        PersistenceBroker pb = getCurrentPersistenceBroker();
        Query query = getQuery(classToQuery, criteria);

        int startIndex = 1;
        if (spanNumber.intValue() != 0) {
            startIndex = spanNumber.intValue() * numberOfElementsInSpan.intValue();
        }

        query.setStartAtIndex(startIndex);

        int endIndex = startIndex + numberOfElementsInSpan.intValue() - 1;

        query.setEndAtIndex(endIndex);
        Iterator iterator = pb.getIteratorByQuery(query);
        return iterator;
    }

    public Iterator readIteratorByCriteria(Query query) {
        PersistenceBroker pb = getCurrentPersistenceBroker();
        Iterator iterator = pb.getIteratorByQuery(query);
        return iterator;
    }

    /**
     * @author Pica&Barbosa Returns an iterator by criteria and order
     */
    protected Iterator readIteratorByCriteria(Class classToQuery, Criteria criteria,
            String orderByString, boolean reverseOrder) {

        QueryByCriteria queryCriteria = new QueryByCriteria(classToQuery, criteria);
        if (orderByString != null) {
            queryCriteria.addOrderBy(orderByString, reverseOrder);
        }

        return readIteratorByCriteria(queryCriteria);
    }

    /**
     * @see ObjectFenixOJB#count(PersistenceBroker, Query)
     */
    public int count(Class classToQuery, Criteria criteria) {
        return count(getCurrentPersistenceBroker(), getQuery(classToQuery, criteria));
    }

    /**
     * @see ObjectFenixOJB#count(PersistenceBroker, Query)
     */
    public int count(Class classToQuery, Criteria criteria, Boolean distinct) {
        return count(getCurrentPersistenceBroker(), getQuery(classToQuery, criteria, distinct));
    }

    /**
     * @see ObjectFenixOJB#count(PersistenceBroker, Query, GroupBy)
     */
    public int count(Class classToQuery, Criteria criteria, String groupBy) {
        QueryByCriteria query = (QueryByCriteria) getQuery(classToQuery, criteria);
        if (groupBy != null) {
            query.addGroupBy(groupBy);
        }
        return count(getCurrentPersistenceBroker(), query);
    }

    /**
     * Do a count(*) with the parameter query
     * 
     * @param pb
     *            current persistent broker
     * @param query
     *            query to count
     * @return number of elements returned by count(*)
     */
    private int count(PersistenceBroker pb, Query query) {
        return pb.getCount(query);
    }


    /**
     * Returns a QueryByCriteria instance.
     * 
     * @see Query and
     * @see QueryByCriteria
     */
    private Query getQuery(Class classToQuery, Criteria criteria) {
        return new QueryByCriteria(classToQuery, criteria);
    }

    /**
     * Returns a QueryByCriteria instance with distinct.
     * 
     * @see Query and
     * @see QueryByCriteria
     */
    private Query getQuery(Class classToQuery, Criteria criteria, Boolean distinct) {
        return new QueryByCriteria(classToQuery, criteria, distinct.booleanValue());
    }

    public Object lockIteratorNextObj(Iterator iterator) throws ExcepcaoPersistencia {
        return iterator.next();
    }

    public IDomainObject materialize(IDomainObject domainObject) {
        return (IDomainObject) ProxyHelper.getRealObject(domainObject);
    }
    
    public Collection readAll(Class classToQuery) throws ExcepcaoPersistencia {
        return queryList(classToQuery, null);
    }
}
