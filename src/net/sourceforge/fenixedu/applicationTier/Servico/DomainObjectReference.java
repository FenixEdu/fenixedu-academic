package net.sourceforge.fenixedu.applicationTier.Servico;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

public class DomainObjectReference<E extends DomainObject> implements Serializable {
    transient E obj = null;
    Integer oid;
    Class objClass;

    DomainObjectReference(E obj) {
        objClass = obj.getClass();
        oid = obj.getIdInternal();
        this.obj = obj;
    }

    synchronized E get() {
        if (obj == null) {
            obj = getPersistentObject(objClass, oid);
        }
        return obj;
    }

    private E getPersistentObject(final Class ojbClass, final Integer oid) {
        try {
        	final ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        	final IPersistentObject persistentObject = persistentSupport.getIPersistentObject();
			return (E) persistentObject.readByOID(objClass, oid);
		} catch (ExcepcaoPersistencia e) {
			throw new Error("Unable to retrieve object of class " + ojbClass + " with oid " + oid, e);
		}    	
    }

}
