/*
 * Created on May 15, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package middleware;

import java.util.List;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

/**
 *
 * @author  Luis Cruz & Sara Ribeiro
 */
public class PersistentObjectOJB {

	protected PersistenceBroker broker = null;

	public PersistentObjectOJB() {
		try {
			broker = PersistenceBrokerFactory.defaultPersistenceBroker();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public void beginTransaction() {
		broker.beginTransaction();
	}

	public void commitTransaction() {
		broker.commitTransaction();
	}

	public synchronized void lockWrite(Object obj) {
		try {

			commitTransaction();
			beginTransaction();
			broker.store(obj);
			commitTransaction();
			beginTransaction();

		} catch (org.apache.ojb.broker.PersistenceBrokerSQLException ex) {
		}
	}

	public List query(Class classToQuery, Criteria criteria) {
		List result = null;

                       // commitTransaction();
                        beginTransaction();
		Query query = new QueryByCriteria(classToQuery, criteria);
		result = (List) broker.getCollectionByQuery(query);
                        commitTransaction();
                       // beginTransaction();

		return result;
	}

	public List query(Object object) {
		List result = null;
	
                      //  commitTransaction();
                        beginTransaction();
		result = (List) broker.getCollectionByQuery(new QueryByCriteria(object));
                        commitTransaction();
                      //  beginTransaction();

		return result;
	}

}
