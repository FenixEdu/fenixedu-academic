package net.sourceforge.fenixedu.domain;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.pstm.Transaction;

public class RootDomainObject extends RootDomainObject_Base {

    private volatile static RootDomainObject instance = null;

    private interface DomainObjectReader {
	public DomainObject readDomainObjectByOID();
    }

    public static synchronized void init() {
	if (instance == null) {
	    Transaction.withTransaction(new jvstm.TransactionalCommand() {
		public void doIt() {
		    instance = FenixFramework.getRoot();
		    instance.initAccessClosures();
		}
	    });
	}
    }

    public static RootDomainObject getInstance() {
	if (instance == null) {
	    init();
	}
	return instance;
    }

    public RootDomainObject() {
	checkIfIsSingleton();
    }

    private void checkIfIsSingleton() {
	if (FenixFramework.getRoot() != null && FenixFramework.getRoot() != this) {
	    throw new Error("There can only be one! (instance of MyOrg)");
	}
    }

    public static void initTests() {
	instance = new RootDomainObject();
    }

    @Override
    protected RootDomainObject getRootDomainObject() {
	return this;
    }
}
