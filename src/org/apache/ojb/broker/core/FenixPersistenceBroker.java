package org.apache.ojb.broker.core;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Iterator;

import org.apache.ojb.broker.PBKey;
import org.apache.ojb.broker.PersistenceBrokerException;
import org.apache.ojb.broker.accesslayer.OJBIterator;
import org.apache.ojb.broker.accesslayer.PagingIterator;
import org.apache.ojb.broker.accesslayer.RsIterator;
import org.apache.ojb.broker.accesslayer.RsQueryObject;
import org.apache.ojb.broker.accesslayer.ChainingIterator;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryBySQL;
import org.apache.ojb.broker.metadata.ClassDescriptor;
import org.apache.ojb.broker.util.ClassHelper;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.stm.Transaction;

import net.sourceforge.fenixedu.persistenceTier.OJB.FenixJdbcAccessImpl;
import net.sourceforge.fenixedu.persistenceTier.OJB.DomainAllocator;


public class FenixPersistenceBroker extends PersistenceBrokerImpl {

    public FenixPersistenceBroker(PBKey key, PersistenceBrokerFactoryIF pbf) {
        super(key, pbf);
    }


    // copied from PersistenceBrokerImpl, to change the RsIteratorFactory used
    protected OJBIterator getIteratorFromQuery(Query query, ClassDescriptor cld) throws PersistenceBrokerException {
        RsIteratorFactory factory = FenixRsIteratorFactory.getInstance();
        OJBIterator result = getRsIteratorFromQuery(query, cld, factory);

        if (query.usePaging()) {
            result = new PagingIterator(result, query.getStartAtIndex(), query.getEndAtIndex());
        }

        return result;
    }

    // verbatim copy from PersistenceBrokerImpl because the method was private there...
    private OJBIterator getRsIteratorFromQuery(Query query, ClassDescriptor cld, RsIteratorFactory factory)
        throws PersistenceBrokerException
    {
        if (query instanceof QueryBySQL)
        {
            return factory.createRsIterator((QueryBySQL) query, cld, this);
        }

        if (!cld.isExtent() || !query.getWithExtents())
        {
            // no extents just use the plain vanilla RsIterator
            return factory.createRsIterator(query, cld, this);
        }


        ChainingIterator chainingIter = new ChainingIterator();

        // BRJ: add base class iterator
        if (!cld.isInterface())
        {

            chainingIter.addIterator(factory.createRsIterator(query, cld, this));
        }

        Iterator extents = getDescriptorRepository().getAllConcreteSubclassDescriptors(cld).iterator();
        while (extents.hasNext())
        {
            ClassDescriptor extCld = (ClassDescriptor) extents.next();

            // read same table only once
            if (chainingIter.containsIteratorForTable(extCld.getFullTableName()))
            {
            }
            else
            {
                // add the iterator to the chaining iterator.
                chainingIter.addIterator(factory.createRsIterator(query, extCld, this));
            }
        }

        return chainingIter;
    }


    static class FenixRsIteratorFactory extends RsIteratorFactoryImpl {
	private static RsIteratorFactory instance;

	synchronized static RsIteratorFactory getInstance() {
            if (instance == null) {
                instance = new FenixRsIteratorFactory();
            }

            return instance;
	}

	public RsIterator createRsIterator(Query query, ClassDescriptor cld, PersistenceBrokerImpl broker) {
            return new FenixRsIterator(RsQueryObject.get(cld, query), broker);
	}
    }

    static class FenixRsIterator extends RsIterator {
        FenixRsIterator(RsQueryObject queryObject, PersistenceBrokerImpl broker) {
            super(queryObject, broker);
        }

        protected Object getObjectFromResultSet() throws PersistenceBrokerException {
            ClassDescriptor cld = getQueryObject().getClassDescriptor();
            
            if (cld.getFactoryClass() != DomainAllocator.class) {
                System.out.println("INFO: FenixRsIterator loading a non-DomainObject delegating to the superclass");
                return super.getObjectFromResultSet();
            } else {
                try {
                    ResultSet rs = getRsAndStmt().m_rs;
                    ClassDescriptor targetClassDescriptor = FenixJdbcAccessImpl.findCorrectClassDescriptor(cld, rs);

                    Integer oid = rs.getInt("ID_INTERNAL");

                    DomainObject result = (DomainObject)Transaction.getCache().lookup(getTopLevelClass(), oid);

                    if (result == null) {
                        result = (DomainObject)ClassHelper.buildNewObjectInstance(targetClassDescriptor);
                        result.setIdInternal(oid);

                        // cache object
                        result = (DomainObject)Transaction.getCache().cache(result);

                        result.readFromResultSet(rs);
                    }

                    return result;
                } catch (SQLException sqle) {
                    throw new PersistenceBrokerException(sqle);
                }
            }
        }
    }
}
