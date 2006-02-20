package net.sourceforge.fenixedu.presentationTier.renderers.factories;

import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu._development.MetadataManager;
import net.sourceforge.fenixedu.applicationTier.Servico.renderers.UpdateObjects.ObjectChange;
import net.sourceforge.fenixedu.renderers.model.MetaObjectKey;
import dml.DomainClass;

public class CreationMetaObject extends DomainMetaObject {

    private Class type;
    private transient Object createdObject;

    public CreationMetaObject(DomainClass domainClass) {
        super();

        try {
            this.type = Class.forName(domainClass.getFullName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("could not find concrete class for domain class " + domainClass);
        }
    }

    public Object getObject() {
        return MetadataManager.getDomainModel().findClass(getType().getName());
    }
    
    public Object getCreatedObject() {
        return this.createdObject;
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

    @Override
    protected Object callService(List<ObjectChange> changes) {
        Collection created = (Collection) super.callService(changes);
        
        // heuristic, does not work so well if multiple objects are created
        for (Object object : created) {
            if (getType().isAssignableFrom(object.getClass())) {
                this.createdObject = object;
                break;
            }
        }
        
        return created;
    }
}
