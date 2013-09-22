package net.sourceforge.fenixedu.presentationTier.Action.externalServices;

import static org.apache.commons.lang.StringUtils.capitalize;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.pstm.AbstractDomainObject;
import dml.DomainClass;
import dml.Role;
import dml.Slot;

public class OAuthUtils {

    
    public static final <T extends DomainObject> T getDomainObject(final String externalId, final Class<T> clazz) {
        try {
            Long.parseLong(externalId);
            final T domainObject = AbstractDomainObject.fromExternalId(externalId);
            // Dirty check to see if domain object still exists due fenix-framework limitations.
            // When using fromExternalId fenix-framework creates a shallow objects with that id.
            // On following requests to object's methods it will throw a VersionNotAvailableException if the object was deleted.
            if (domainObject == null) {
                return null;
            }
            
            String getterName = null;
            final DomainClass domainClass = FenixFramework.getDomainModel().findClass(clazz.getName());
            if (domainClass != null) {
                final List<Slot> slotsList = domainClass.getSlotsList();
                if (slotsList.isEmpty()) {
                    final List<Role> roleSlots = domainClass.getRoleSlotsList();
                    if (roleSlots.isEmpty()) {
                        return null;
                    }
                    getterName = String.format("get%sSet",capitalize(roleSlots.get(0).getName()));
                }else {
                    getterName = String.format("%sSet", capitalize(slotsList.get(0).getName()));
                }
            }
            final Method method = clazz.getMethod(getterName, (Class[])null);
            
            if (method == null) {
                return null;
            }
            
            method.invoke(domainObject, (Object[])null);
            
            return domainObject;
        } catch (Exception nfe) {
            return null;
        }
    }
}
