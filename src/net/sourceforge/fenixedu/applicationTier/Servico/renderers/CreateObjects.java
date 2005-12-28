package net.sourceforge.fenixedu.applicationTier.Servico.renderers;

import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class CreateObjects extends UpdateObjects {

    public CreateObjects() {
        super();
    }

    @Override
    protected IDomainObject getNewObject(ObjectChange change) throws ExcepcaoPersistencia, ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class objectClass = change.key.getType();
        
        return (IDomainObject) objectClass.newInstance();
    }
}
