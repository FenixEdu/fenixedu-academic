package net.sourceforge.fenixedu.applicationTier.Servico.renderers;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class CreateObjects extends UpdateObjects {

    public CreateObjects() {
        super();
    }

    @Override
    protected DomainObject getNewObject(ObjectChange change) throws ExcepcaoPersistencia, ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class objectClass = change.key.getType();
        
        return (DomainObject) objectClass.newInstance();
    }
}
