/*
 * ObjectFenixOJB.java Created on 22 de Agosto de 2002, 23:51
 */

package ServidorPersistente.OJB;

/**
 * @author ars
 */

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.ojb.broker.Identity;
import org.apache.ojb.broker.ManageableCollection;
import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.core.proxy.ProxyHelper;
import org.apache.ojb.broker.metadata.ClassDescriptor;
import org.apache.ojb.broker.metadata.CollectionDescriptor;
import org.apache.ojb.broker.metadata.FieldDescriptor;
import org.apache.ojb.broker.metadata.ObjectReferenceDescriptor;
import org.apache.ojb.broker.metadata.fieldaccess.PersistentField;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.apache.ojb.broker.query.QueryFactory;
import org.apache.ojb.broker.util.logging.LoggerFactory;
import org.apache.ojb.odmg.HasBroker;
import org.apache.ojb.odmg.TransactionImpl;
import org.apache.ojb.odmg.TxManagerFactory;
import org.odmg.Database;
import org.odmg.Implementation;
import org.odmg.ODMGException;
import org.odmg.ODMGRuntimeException;
import org.odmg.OQLQuery;
import org.odmg.QueryException;
import org.odmg.Transaction;

import Dominio.Advisory;
import Dominio.CandidateSituation;
import Dominio.IDomainObject;
import Dominio.MasterDegreeCandidate;
import Dominio.Pessoa;
import Dominio.Summary;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;

public abstract class ObjectFenixOJB implements IPersistentObject {
    protected Implementation odmg = null;

    protected Database db = null;

    protected OQLQuery query = null;

    protected Transaction tx = null;

    /** Creates a new instance of ObjectFenixOJB */
    public ObjectFenixOJB() {

        try {
            odmg = SuportePersistenteOJB.getInstance().getImplementation();
        } catch (ExcepcaoPersistencia e1) {
            e1.printStackTrace();
        }

        db = odmg.getDatabase(null);
        if (db == null) {
            System.out.println("Opening a new database connection in Object Fenix!!!");
            db = odmg.newDatabase();
            try {
                db.open("OJB/repository.xml", Database.OPEN_READ_WRITE);
            } catch (ODMGException e) {
                e.printStackTrace();
            }
        }

        query = odmg.newOQLQuery();

    }

    protected void lockRead(List list) throws ExcepcaoPersistencia {
        try {

            tx = odmg.currentTransaction();

            if (tx == null)
                throw new ExcepcaoPersistencia("No current transaction!");
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    Object obj = list.get(i);

                    lockRead(obj);
                }
            }
        } catch (ODMGRuntimeException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.READ_LOCK, ex);
        }
    }

    public void lockWrite(Object obj) throws ExcepcaoPersistencia {
        try {
            tx = odmg.currentTransaction();
            tx.lock(obj, Transaction.WRITE);

        } catch (ODMGRuntimeException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.UPGRADE_LOCK, ex);
        }

    }

    protected void lockRead(Object obj) throws ExcepcaoPersistencia {
        try {
            tx = odmg.currentTransaction();
            tx.lock(obj, Transaction.READ);

        } catch (ODMGRuntimeException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.READ_LOCK, ex);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentObject#simpleLockWrite(Dominio.IDomainObject)
     */
    public final void simpleLockWrite(IDomainObject obj) throws ExcepcaoPersistencia {
        try {
            tx = odmg.currentTransaction();
            tx.lock(obj, Transaction.WRITE);

        } catch (ODMGRuntimeException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.UPGRADE_LOCK, ex);
        }
    }

    /**
     * Locks to WRITE and delete the object from database...
     * 
     * @param obj
     *            Object to delete
     * @throws ExcepcaoPersistencia
     */
    public void delete(Object obj) throws ExcepcaoPersistencia {
        try {
            tx = odmg.currentTransaction();
            tx.lock(obj, Transaction.WRITE);

            db.deletePersistent(obj);

            PersistenceBroker broker = ((HasBroker) tx).getBroker();
            broker.removeFromCache(obj);
        } catch (ODMGRuntimeException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.UPGRADE_LOCK, ex);
        }
    }

    public final void deleteByOID(Class classToQuery, Integer oid) throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("idInternal", oid);
        List objectsToDelete = queryList(classToQuery, criteria);
        for (int i = 0; i < objectsToDelete.size(); i++) {
            delete(objectsToDelete.get(i));
        }
    }

    public void deleteAll(String oqlQuery) throws ExcepcaoPersistencia {
        try {
            query.create(oqlQuery);
            List result = (List) query.execute();
            ListIterator iterator = result.listIterator();
            while (iterator.hasNext()) {

                delete(iterator.next());
            }
        } catch (QueryException ex) {
            ex.printStackTrace();
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }

    }

    public void deleteByCriteria(Object obj) throws ExcepcaoPersistencia {
        List result = readByCriteria(obj);
        ListIterator iterator = result.listIterator();
        while (iterator.hasNext())
            delete(iterator.next());

    }

    public Object executeQueryByCriteria(Class queryClass, Criteria crit) throws org.odmg.QueryException {

        Query query = QueryFactory.newQuery(queryClass, crit, true);
        //setBindIterator(flatten(query.getCriteria(), new
        // Vector()).listIterator());
        //setQuery(query);

        try {
            //obtain current ODMG transaction
            Transaction tx = TxManagerFactory.instance().getTransaction();

            // we allow queries even if no ODMG transaction is running.
            // thus we have to provide a pseudo tx if necessary
            boolean needsCommit = false;
            if (tx == null) {
                throw new org.odmg.QueryException("Transaction Null!");
                //tx = OJBFactory.getInstance().newTransaction();
            }

            // we allow to work with unopened transactions.
            // we assume that such a tx is to be closed after performing the
            // query
            if (!tx.isOpen()) {
                tx.begin();
                needsCommit = true;
            }
            // obtain a broker instance from the current transaction
            PersistenceBroker broker = ((HasBroker) tx).getBroker();

            //				  if(needsCommit) broker.beginTransaction();
            // ask the broker to perfom the query.
            // the concrete result type is configurable
            ManageableCollection result = (ManageableCollection) broker.getCollectionByQuery(/* this.getCollectionClass(), */
            query);
            //				  if(needsCommit) broker.commitTransaction();

            // read-lock all resulting objects to the current transaction
            Iterator iter = result.ojbIterator();
            Object toBeLocked = null;
            while (iter.hasNext()) {
                toBeLocked = iter.next();
                /**
                 * we can only lock objects, not attributes
                 */
                if (broker.hasClassDescriptor(toBeLocked.getClass()))
                    tx.lock(toBeLocked, Transaction.READ);
            }
            // if query was executed with pseudo tx or with unopened tx, commit
            // it
            if (needsCommit) {
                tx.commit();
            }
            return result;

        } catch (Throwable t) {
            throw new org.odmg.QueryException(t.getMessage());
        }

    }

    /**
     * @see IPersistentObject#readByOId(IDomainObject, boolean)
     * @deprecated
     */
    public IDomainObject readByOId(IDomainObject obj, boolean lockWrite) {

        tx = odmg.currentTransaction();
        PersistenceBroker broker = ((HasBroker) tx).getBroker();
        Identity identity = new Identity(obj, broker);

        IDomainObject domainObject = (IDomainObject) ((TransactionImpl) tx)
                .getObjectByIdentity(identity);

        if (domainObject == null) {
            return null;
        }

        if (lockWrite) {
            tx.lock(domainObject, Transaction.WRITE);
        } else {
            tx.lock(domainObject, Transaction.READ);
        }
        return domainObject;
    }

    public Object readDomainObjectByCriteria(Object obj) throws ExcepcaoPersistencia {
        List result = readByCriteria(obj);
        if (result != null && !result.isEmpty())
            return result.get(0);
        return null;
    }

    public List readByCriteria(Class queryClass, Criteria criteria) throws ExcepcaoPersistencia {

        if (queryClass == null)
            throw new IllegalArgumentException("Class to query cannot be null");

        try {
            //((EnhancedOQLQuery) query).create(queryClass, criteria);
            //List result = (List) query.execute();
            List result = (List) executeQueryByCriteria(queryClass, criteria);

            lockRead(result);
            return result;
        } catch (Exception e) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, e);
        }

    }

    public List readByCriteria(Object obj) throws ExcepcaoPersistencia {

        if (obj == null)
            throw new IllegalArgumentException("Object to query cannot be null");

        return readByCriteria(obj.getClass(), criteriaBuilder(obj));

    }

    protected Criteria criteriaBuilder(Object obj) {
        return criteriaBuilder(new Criteria(), "", obj);
    }

    private static Criteria criteriaBuilder(Criteria criteria, String path, Object anExample) {
        //////////////////////////////////////////////////////////////////////////////////////
        // Do this just to get the broker...
        // There must be a simpler way!
        //////////////////////////////////////////////////////////////////////////////////////

        //obtain current ODMG transaction
        //		Transaction tx = TxManagerFactory.instance().getTransaction();
        PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();
        // we allow queries even if no ODMG transaction is running.
        // thus we have to provide a pseudo tx if necessary
        //boolean needsCommit = false;
        //		if (tx == null) {
        //			System.out.println("Transaction Null!");
        //			//throw new org.odmg.QueryException("Transaction Null!");
        //			//tx = OJBFactory.getInstance().newTransaction();
        //		}

        // we allow to work with unopened transactions.
        // we assume that such a tx is to be closed after performing the query
        //		if (!tx.isOpen()) {
        //			tx.begin();
        //			needsCommit = true;
        //		}
        // obtain a broker instance from the current transaction
        //		PersistenceBroker broker = ((HasBroker) tx).getBroker();
        ///////////////////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////////////

        Class c = ProxyHelper.getRealClass(anExample);
        ClassDescriptor cld = broker.getClassDescriptor(c);

        //		DescriptorRepository
        // descriptorRepository=broker.getDescriptorRepository();
        //		descriptorRepository.

        FieldDescriptor[] fds = cld.getFieldDescriptions();

        PersistentField f;
        Object value;
        Vector ref = cld.getObjectReferenceDescriptors();
        Criteria crit = criteria;

        Vector col = cld.getCollectionDescriptors();

        ///////////////////////////////////////////////////////////////////////////////////////////
        // From code up above...
        ///////////////////////////////////////////////////////////////////////////////////////////

        // if query was executed with pseudo tx or with unopened tx, commit it
        //		if (needsCommit) {
        //			tx.commit();
        //		}
        ///////////////////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////////////

        for (int k = 0; k < col.size(); k++) {
            CollectionDescriptor objCol = (CollectionDescriptor) col.get(k);
            PropertyDescriptor[] xpto = PropertyUtils.getPropertyDescriptors(anExample.getClass());
            //objCol.

            for (int j = 0; j < xpto.length; j++) {
                Method getM = xpto[j].getReadMethod();
                if (xpto[j].getName().equals(objCol.getAttributeName())) {
                    try {
                        Collection propValue = (Collection) getM.invoke(anExample, null);

                        if (propValue != null) {

                            Iterator iter = propValue.iterator();
                            while (iter.hasNext()) {
                                Object element = iter.next();

                                crit = criteriaBuilder(criteria, path + objCol.getAttributeName() + ".",
                                        element);
                            }

                        }
                    } catch (IllegalAccessException ex) {
                        ex.printStackTrace(System.out);
                        LoggerFactory.getDefaultLogger().error(ex);
                    } catch (InvocationTargetException ex) {
                        ex.printStackTrace(System.out);
                        LoggerFactory.getDefaultLogger().error(ex);
                    }
                }
            }

        }

        for (int j = 0; j < ref.size(); j++) {
            ObjectReferenceDescriptor objref = (ObjectReferenceDescriptor) ref.get(j);
            PropertyDescriptor[] xpto = PropertyUtils.getPropertyDescriptors(anExample.getClass());
            for (int k = 0; k < xpto.length; k++) {
                Method getM = xpto[k].getReadMethod();
                if (xpto[k].getName().equals(objref.getAttributeName())) {
                    try {
                        Object propValue = getM.invoke(anExample, null);
                        // --- Try This---
                        //Object propValue = getM.invoke(c, null);
                        // ---------------
                        if (propValue != null) {
                            crit = criteriaBuilder(criteria, path + objref.getAttributeName() + ".",
                                    propValue);
                        }
                    } catch (IllegalAccessException ex) {
                        ex.printStackTrace(System.out);
                        LoggerFactory.getDefaultLogger().error(ex);
                    } catch (InvocationTargetException ex) {
                        ex.printStackTrace(System.out);
                        LoggerFactory.getDefaultLogger().error(ex);
                    }
                }
            }
        }

        for (int i = 0; i < fds.length; i++) {

            try {
                f = fds[i].getPersistentField();
                value = f.get(anExample);
                if (value != null) {
                    crit.addEqualTo(path + f.getName(), value);
                }
            } catch (Throwable ex) {
                ex.printStackTrace(System.out);
                LoggerFactory.getDefaultLogger().error(ex);
            }
        }

        broker.close();

        return crit;
    }

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

    protected List queryList(Query query) throws ExcepcaoPersistencia {
        PersistenceBroker pb = ((HasBroker) odmg.currentTransaction()).getBroker();

        List list = (List) pb.getCollectionByQuery(query);
        if (list != null) {
            if (!list.isEmpty()
                    && (list.get(0) instanceof MasterDegreeCandidate
                            || list.get(0) instanceof CandidateSituation
                            || list.get(0) instanceof Summary || list.get(0) instanceof Pessoa || list
                            .get(0) instanceof Advisory)) {
                lockReadWithReplacement(list, pb);

            }
            lockRead(list);
        }
        return list;
    }

    /**
     * @param list
     */
    private void lockReadWithReplacement(List list, PersistenceBroker pb) throws ExcepcaoPersistencia {
        try {

            tx = odmg.currentTransaction();

            if (tx == null)
                throw new ExcepcaoPersistencia("No current transaction!");
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    Object obj = list.get(i);
                    Object newObject = ((TransactionImpl) odmg.currentTransaction())
                            .getObjectByIdentity(new Identity(obj, pb));
                    if (newObject != null) {
                        obj = newObject;
                        list.add(i, obj);
                        list.remove(i + 1);
                    }

                    lockRead(obj);
                }
            }
        } catch (ODMGRuntimeException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.READ_LOCK, ex);
        }

    }

    protected Object queryObject(Class classToQuery, Criteria criteria) throws ExcepcaoPersistencia {
        PersistenceBroker pb = getCurrentPersistenceBroker();
        Query query = getQuery(classToQuery, criteria);
        Object object = pb.getObjectByQuery(query);

        if (object instanceof MasterDegreeCandidate || object instanceof CandidateSituation
                || object instanceof Summary || object instanceof Pessoa || object instanceof Advisory) {
            Object newObject = ((TransactionImpl) odmg.currentTransaction())
                    .getObjectByIdentity(new Identity(object, pb));
            if (newObject != null) {
                object = newObject;
            }

        }
        if (object != null) {
            lockRead(object);
        }
        return object;
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

        if (object instanceof MasterDegreeCandidate || object instanceof CandidateSituation
                || object instanceof Summary || object instanceof Pessoa || object instanceof Advisory) {
            Object newObject = ((TransactionImpl) odmg.currentTransaction())
                    .getObjectByIdentity(new Identity(object, pb));
            if (newObject != null) {
                object = newObject;
            }

        }
        if (object != null) {
            lockRead(object);
        }
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
        IDomainObject domainObject = (IDomainObject) queryObject(classToQuery, criteria);
        if (lockWrite) {
            tx.lock(domainObject, Transaction.WRITE);
        }
        return domainObject;
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
        lockRead(list);
        return list;
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
        lockRead(list);
        return list;
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

    public Iterator readIteratorByCriteria(Class classToQuery, Criteria criteria) {
        Query query = getQuery(classToQuery, criteria);
        return readIteratorByCriteria(query);
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
     * Gets the persistenceBroker associated with current transaction
     * 
     * @return PersistenceBroker associated with current transaction
     */
    protected PersistenceBroker getCurrentPersistenceBroker() {
        PersistenceBroker pb = ((HasBroker) odmg.currentTransaction()).getBroker();
        return pb;
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

    /**
     * Store the object in the database without creating any lock's
     * 
     * @param domainObject
     *            object to store
     */
    protected void store(IDomainObject domainObject) {
        PersistenceBroker pb = ((HasBroker) odmg.currentTransaction()).getBroker();
        pb.store(domainObject);
    }

    public Object lockIteratorNextObj(Iterator iterator) throws ExcepcaoPersistencia {
        Object object = iterator.next();
        lockRead(object);
        return object;
    }

    public IDomainObject materialize(IDomainObject domainObject) {
        return (IDomainObject) ProxyHelper.getRealObject(domainObject);
    }
}