/*
 * Created on May 15, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package middleware.almeida;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;

/**
 *
 * @author  Luis Cruz & Sara Ribeiro
 */
public class PersistentObjectOJB {

	PersistenceBroker broker = null;

	public PersistentObjectOJB() {
		try {
			broker = PersistenceBrokerFactory.defaultPersistenceBroker();
		} catch (Throwable t) {
			t.printStackTrace();
		}
		//super();
	}

	public void beginTransaction() {
		broker.beginTransaction();
	}

	public void commitTransaction() {
		broker.commitTransaction();
	}

	public void lockWrite(Object obj) {
		broker.store(obj);
	}

}
