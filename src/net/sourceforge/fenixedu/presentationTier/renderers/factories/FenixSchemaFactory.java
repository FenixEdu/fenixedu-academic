package net.sourceforge.fenixedu.presentationTier.renderers.factories;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.ist.fenixframework.FenixFramework;
import net.sourceforge.fenixedu.domain.DomainObject;
import pt.ist.fenixWebFramework.renderers.model.DefaultSchemaFactory;
import pt.ist.fenixWebFramework.renderers.schemas.Schema;
import pt.ist.fenixWebFramework.renderers.schemas.SchemaSlotDescription;
import dml.DomainClass;
import dml.Role;
import dml.Slot;

public class FenixSchemaFactory extends DefaultSchemaFactory {
    @Override
    public Schema createSchema(Object object) {
        if (object instanceof DomainObject) {
            try {
                return FenixSchemaFactory.getSchemaForDomainObject(object.getClass().getName());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        else if (object instanceof DomainClass) {
            try {
                return FenixSchemaFactory.getSchemaForDomainObject(((DomainClass) object).getFullName());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return super.createSchema(object);
    }
    
    @Override
    public Schema createSchema(Class type) {
        if (DomainObject.class.isAssignableFrom(type)) {
            try {
                return FenixSchemaFactory.getSchemaForDomainObject(type.getName());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        
        return super.createSchema(type);
    }

    public static List<Slot> getDomainClassSlots(DomainClass domainClass) {
        List<Slot> slots = new ArrayList<Slot>();
        String[] slotsToIgnore = new String[] {
          "^key.*", ".*Key$", "^chave.*", "^idInternal$", "^ackOptLock$"      
        };
        
        while (domainClass != null) {
            outter: for (Iterator iter = domainClass.getSlots(); iter.hasNext();) {
                Slot slot = (Slot) iter.next();
    
                String slotName = slot.getName();
                
                // Ignore some slots that only exist to support the persistance framework
                for (int i = 0; i < slotsToIgnore.length; i++) {
                    if (slotName.matches(slotsToIgnore[i])) {
                        continue outter;
                    }
                }
                
                slots.add(slot);
            }
            
            domainClass = (DomainClass) domainClass.getSuperclass();
        }
        
        return slots;
    }
    
    public static List<Role> getDomainClassRoles(DomainClass domainClass) {
        List<Role> roles = new ArrayList<Role>();
        
        while (domainClass != null) {
            for (Iterator iter = domainClass.getRoleSlots(); iter.hasNext();) {
                Role role = (Role) iter.next();
    
                roles.add(role);
            }
            
            domainClass = (DomainClass) domainClass.getSuperclass();
        }
        
        return roles;
    }

    public static Schema getSchemaForDomainObject(String name) throws ClassNotFoundException {
        DomainClass domainClass = FenixFramework.getDomainModel().findClass(name);

        Schema schema = new Schema(Class.forName(domainClass.getFullName()));
        
        List<Slot> slots = getDomainClassSlots(domainClass);
        for (Slot slot : slots) {
            schema.addSlotDescription(new SchemaSlotDescription(slot.getName()));
        }

//        List<Role> roles = getDomainClassRoles(domainClass);
//        for (Role role : roles) {
//            schema.addSlotDescription(new SchemaSlotDescription(role.getName()));
//        }
        
        return schema;
    }

}
