package middleware.persistentMiddlewareSupport.OJBDatabaseSupport;

import middleware.persistentMiddlewareSupport.IPersistentMWAluno;
import middleware.persistentMiddlewareSupport.IPersistentMWBranch;
import middleware.persistentMiddlewareSupport.IPersistentMWEnrolment;
import middleware.persistentMiddlewareSupport.IPersistentMiddlewareSupport;
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


	public IPersistentMWAluno getIPersistentMWAluno() {
		return new MWAlunoOJB();
	}

	public IPersistentMWBranch getIPersistentMWBranch() {
		return new MWBranchOJB();
	}

	public IPersistentMWEnrolment getIPersistentMWEnrolment() {
		return new MWEnrolmentOJB();
	}

}