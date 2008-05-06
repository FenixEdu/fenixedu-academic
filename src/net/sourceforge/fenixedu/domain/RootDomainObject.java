package net.sourceforge.fenixedu.domain;

import pt.ist.fenixframework.pstm.Transaction;

public class RootDomainObject extends RootDomainObject_Base {

    private static RootDomainObject instance = null;

    private interface DomainObjectReader {
	public DomainObject readDomainObjectByOID();
    }

    public static synchronized void init() {
	if (instance == null) {
	    Transaction.withTransaction(new jvstm.TransactionalCommand() {
		public void doIt() {
		    instance = (RootDomainObject)Transaction.getDomainObject(RootDomainObject.class.getName(), 1);
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

    private RootDomainObject() {
    }

    public static void initTests() {
	instance = new RootDomainObject();
    }

    protected RootDomainObject getRootDomainObject() {
        return this;
    }
}
