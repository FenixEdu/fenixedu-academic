package middleware.persistentMiddlewareSupport.OJBDatabaseSupport;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * 
 */

public abstract class MiddlewareObjectOJB {
	protected PersistenceBroker broker = null;

	/** Creates a new instance of ObjectFenixOJB */
	public MiddlewareObjectOJB() {
		broker = PersistenceBrokerFactory.defaultPersistenceBroker();
	}


}
