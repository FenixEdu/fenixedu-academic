package net.sourceforge.fenixedu.util.domain;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * Allows to obtain the elements being ordered and the order for each element
 * through reflection by given the name of the slots containing each information
 * required.
 * 
 * @author cfgi
 */
public class SlotSelector<HolderType, ObjectType> implements OrdinalAccessor<HolderType, ObjectType> {
    private String relationName;
    private String slotName;

    public SlotSelector(String relationName, String slotName) {
        super();

        this.relationName = relationName;
        this.slotName = slotName;
    }

    public Collection<ObjectType> getObjects(HolderType holder) {
        try {
            return (Collection<ObjectType>) PropertyUtils.getProperty(holder, this.relationName);
        } catch (IllegalAccessException e) {
            throw new DomainException("adapter.ordered.relation.no.slot.access", e);
        } catch (InvocationTargetException e) {
            throw new DomainException("adapter.ordered.relation.invocation.exception", e.getCause());
        } catch (NoSuchMethodException e) {
            throw new DomainException("adapter.ordered.relation.no.slot", e);
        }
    }

    public Integer getOrder(ObjectType target) {
        try {
            return (Integer) PropertyUtils.getProperty(target, this.slotName);
        } catch (IllegalAccessException e) {
            throw new DomainException("adapter.ordered.relation.no.slot.access", e);
        } catch (InvocationTargetException e) {
            throw new DomainException("adapter.ordered.relation.invocation.exception", e.getCause());
        } catch (NoSuchMethodException e) {
            throw new DomainException("adapter.ordered.relation.no.slot", e);
        }
    }

    public void setOrder(ObjectType target, Integer order) {
        try {
            PropertyUtils.setProperty(target, this.slotName, order);
        } catch (IllegalAccessException e) {
            throw new DomainException("adapter.ordered.relation.no.slot.access", e);
        } catch (InvocationTargetException e) {
            throw new DomainException("adapter.ordered.relation.invocation.exception", e.getCause());
        } catch (NoSuchMethodException e) {
            throw new DomainException("adapter.ordered.relation.no.slot", e);
        }
    }

}