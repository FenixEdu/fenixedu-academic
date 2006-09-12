package net.sourceforge.fenixedu.domain;


import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import net.sourceforge.fenixedu.stm.Transaction;


public class RootDomainObject extends RootDomainObject_Base {

    private static RootDomainObject instance = null;

    private interface DomainObjectReader {
    	public DomainObject readDomainObjectByOID();
    }

    public static synchronized void init() {
        if (instance == null) {
            Transaction.withTransaction(new jvstm.TransactionalCommand() {
                    public void doIt() {
                        final ISuportePersistente suportePersistente = PersistenceSupportFactory.getDefaultPersistenceSupport();
                        final IPersistentObject persistentObject = suportePersistente.getIPersistentObject();
                        instance = persistentObject.readRootDomainObject();
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
}
