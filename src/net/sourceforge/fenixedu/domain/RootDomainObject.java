package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

public class RootDomainObject extends RootDomainObject_Base {

    public static final RootDomainObject instance;
    static {
        final ISuportePersistente suportePersistente = PersistenceSupportFactory.getDefaultPersistenceSupport();
        try {
            suportePersistente.iniciarTransaccao();
            final IPersistentObject persistentObject = suportePersistente.getIPersistentObject();
            instance = (RootDomainObject) persistentObject.readByOID(RootDomainObject.class, Integer.valueOf(1));
            suportePersistente.confirmarTransaccao();
        } catch (ExcepcaoPersistencia e) {
            throw new Error("error.root.domain.object.not.retrieved", e);
        }
    }

    private RootDomainObject() {
        throw new Error("error.root.domain.object.constructor");
    }
    
}
