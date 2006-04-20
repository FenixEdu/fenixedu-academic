package net.sourceforge.fenixedu.domain;


import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

public class RootDomainObject extends RootDomainObject_Base {

    private static RootDomainObject instance = null;

    private interface DomainObjectReader {
    	public DomainObject readDomainObjectByOID();
    }

    public static synchronized void init() {
        if (instance == null) {
            final ISuportePersistente suportePersistente = PersistenceSupportFactory.getDefaultPersistenceSupport();
            try {
                suportePersistente.iniciarTransaccao();
                final IPersistentObject persistentObject = suportePersistente.getIPersistentObject();
                instance = persistentObject.readRootDomainObject();
                suportePersistente.confirmarTransaccao();
            } catch (ExcepcaoPersistencia e) {
                throw new Error("error.root.domain.object.not.retrieved", e);
            }
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
