package middleware.persistentMiddlewareSupport.OJBDatabaseSupport;

import middleware.persistentMiddlewareSupport.IPersistentMiddlewareSupport;
import middleware.persistentMiddlewareSupport.IPersistentAluno;
import middleware.persistentMiddlewareSupport.exceptions.PersistentMiddlewareSupportException;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * 
 */
public class PersistentMiddlewareSupportOJB implements IPersistentMiddlewareSupport {

	private static PersistentMiddlewareSupportOJB instance = null;

	public static synchronized PersistentMiddlewareSupportOJB getInstance() throws PersistentMiddlewareSupportException {
		if (instance == null) {
			instance = new PersistentMiddlewareSupportOJB();
		}
		return instance;
	}

	public static synchronized void resetInstance() {
		if (instance != null) {
			PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();
			broker.clearCache();
			instance = null;
		}
	}

	/** Creates a new instance of SuportePersistenteOJB */
	private PersistentMiddlewareSupportOJB() throws PersistentMiddlewareSupportException {
	}


	public IPersistentAluno getIPersistentAluno() {
		return new AlunoOJB();
	}

}