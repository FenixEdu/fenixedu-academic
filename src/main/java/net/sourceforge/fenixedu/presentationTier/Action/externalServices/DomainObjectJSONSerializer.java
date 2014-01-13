package net.sourceforge.fenixedu.presentationTier.Action.externalServices;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.core.AbstractDomainObject;
import pt.ist.fenixframework.dml.DomainClass;
import pt.ist.fenixframework.dml.DomainModel;
import pt.ist.fenixframework.dml.Role;
import pt.ist.fenixframework.dml.Slot;

public class DomainObjectJSONSerializer {
    private static DomainModel model;

    static {
        model = FenixFramework.getDomainModel();
    }

    public static JSONObject getDomainObject(DomainObject obj) throws SecurityException, NoSuchMethodException,
            IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        final JSONObject jsonObject = new JSONObject();
        final Class<? extends DomainObject> clazz = obj.getClass();
        final String objClassName = clazz.getName();

        jsonObject.put("externalId", obj.getExternalId());
        jsonObject.put("className", objClassName);
        final DomainClass domainClass = getDomainClass(objClassName);
        if (domainClass == null) {
            return jsonObject;
        }
        for (Slot slot : getAllSlots(domainClass)) {
            final String slotName = slot.getName();
            final Method method = clazz.getMethod("get" + StringUtils.capitalize(slotName));
            final Object result = method.invoke(obj);
            jsonObject.put(slotName, result == null ? null : result.toString());
        }

        for (Role roleSlot : getAllRoleSlots(domainClass)) {
            final String slotName = roleSlot.getName();
            if (roleSlot.getMultiplicityUpper() == 1) {
                final Method method = clazz.getMethod("get" + StringUtils.capitalize(slotName));
                final AbstractDomainObject singleRelationObj = (AbstractDomainObject) method.invoke(obj);
                final JSONArray oneRelation = new JSONArray();
                if (singleRelationObj != null) {
                    oneRelation.add(singleRelationObj.getExternalId());
                }
                jsonObject.put(slotName, oneRelation);
            } else {
                final Method method = clazz.getMethod("get" + StringUtils.capitalize(slotName) + "Set");
                final Set<? extends AbstractDomainObject> result = (Set<? extends AbstractDomainObject>) method.invoke(obj);
                jsonObject.put(slotName, serializeRelation(result));
            }

        }

        return jsonObject;
    }

    private static JSONArray serializeRelation(Set<? extends AbstractDomainObject> roleSet) {
        JSONArray jsonList = new JSONArray();
        for (AbstractDomainObject obj : roleSet) {
            jsonList.add(obj.getExternalId());
        }
        return jsonList;
    }

    private static DomainClass getDomainClass(String objClassName) {
        for (DomainClass clazz : model.getDomainClasses()) {
            if (clazz.getFullName().equals(objClassName)) {
                return clazz;
            }
        }
        return null;
    }

    private static List<Slot> getAllSlots(DomainClass clazz) {
        final List<Slot> slots = new ArrayList<Slot>();
        if (clazz.hasSuperclass()) {
            slots.addAll(getAllSlots((DomainClass) clazz.getSuperclass()));
        }
        slots.addAll(clazz.getSlotsList());
        return slots;
    }

    private static List<Role> getAllRoleSlots(DomainClass clazz) {
        final List<Role> slots = new ArrayList<Role>();
        if (clazz.hasSuperclass()) {
            slots.addAll(getAllRoleSlots((DomainClass) clazz.getSuperclass()));
        }
        slots.addAll(clazz.getRoleSlotsList());
        return slots;
    }

}
