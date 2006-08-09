package net.sourceforge.fenixedu.presentationTier.renderers.factories;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.renderers.ObjectChange;
import net.sourceforge.fenixedu.applicationTier.Servico.renderers.ObjectKey;
import net.sourceforge.fenixedu.renderers.model.InstanceCreator;
import net.sourceforge.fenixedu.renderers.model.MetaObjectKey;

public class CreationDomainMetaObject extends DomainMetaObject {

    public CreationDomainMetaObject(Class type) {
        super();
        
        setType(type);
        setOid(0);
        
        setService("CreateObjects");
    }

    public MetaObjectKey getKey() {
        return new CreationMetaObjectKey(getType());
    }
    
    @Override
    protected Object callService(List<ObjectChange> changes) {
        List<ObjectChange> newChanges = new ArrayList<ObjectChange>(changes);
        
        InstanceCreator instanceCreator = getInstanceCreator();
        if (instanceCreator != null) {
            ObjectKey key = new ObjectKey(getOid(), getType());
            
            try {
                newChanges.add(0, new ObjectChange(key, instanceCreator.getConstructor(), instanceCreator.getArgumentValues()));
            } catch (Exception e) {
                throw new RuntimeException("could not find constructor for '" + getType().getName() + "' with arguments " + instanceCreator.getArgumentTypes(), e);
            }
        }
        
        Collection created = (Collection) super.callService(newChanges);
        
        // heuristic, does not work so well if multiple objects are created
        for (Object object : created) {
            if (getType().isAssignableFrom(object.getClass())) {
                setObject(object);
                break;
            }
        }
        
        return created;
    }

}
