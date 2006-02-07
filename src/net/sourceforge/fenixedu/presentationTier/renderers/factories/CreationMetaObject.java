package net.sourceforge.fenixedu.presentationTier.renderers.factories;

import java.util.List;

import net.sourceforge.fenixedu._development.MetadataManager;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.renderers.UpdateObjects.ObjectChange;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.renderers.model.MetaObjectKey;
import dml.DomainClass;

public class CreationMetaObject extends DomainMetaObject {

    private Class type;

    public CreationMetaObject(DomainClass domainClass) {
        super();

        try {
            this.type = Class.forName(domainClass.getFullName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("could not find concrete class for domain class "+ domainClass);
        }
    }

    public Object getObject() {
        return MetadataManager.getDomainModel().findClass(getType().getName());
    }

    @Override
    public int getOid() {
        return 0;
    }
    
    @Override
    public Class getType() {
        return type;
    }

    public MetaObjectKey getKey() {
        return new CreationMetaObjectKey(getType());
    }

    @Override
    protected String getServiceName() {
        return "CreateObjects";
    }
}
