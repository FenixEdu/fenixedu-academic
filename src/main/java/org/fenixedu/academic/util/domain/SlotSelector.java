/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.util.domain;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.beanutils.PropertyUtils;

import pt.ist.fenixframework.core.WriteOnReadError;

/**
 * Allows to obtain the elements being ordered and the order for each element
 * through reflection by given the name of the slots containing each information
 * required.
 * 
 * @author cfgi
 */
public class SlotSelector<HolderType, ObjectType> implements OrdinalAccessor<HolderType, ObjectType> {
    private final String relationName;
    private final String slotName;

    public SlotSelector(String relationName, String slotName) {
        super();

        this.relationName = relationName;
        this.slotName = slotName;
    }

    @Override
    public Collection<ObjectType> getObjects(HolderType holder) {
        try {
            return (Collection<ObjectType>) PropertyUtils.getProperty(holder, this.relationName);
        } catch (IllegalAccessException e) {
            throw new DomainException("adapter.ordered.relation.no.slot.access", e);
        } catch (InvocationTargetException e) {
            throw handleInvocationTargetException(e, "adapter.ordered.relation.invocation.exception");
        } catch (NoSuchMethodException e) {
            throw new DomainException("adapter.ordered.relation.no.slot", e);
        }
    }

    private RuntimeException handleInvocationTargetException(InvocationTargetException e, String message) {
        if (e.getCause() instanceof WriteOnReadError) {
            throw (WriteOnReadError) e.getCause();
        }
        if (e.getCause() instanceof RuntimeException) {
            return (RuntimeException) e.getCause();
        } else {
            return new DomainException(message, e.getCause());
        }
    }

    @Override
    public Integer getOrder(ObjectType target) {
        try {
            return (Integer) PropertyUtils.getProperty(target, this.slotName);
        } catch (IllegalAccessException e) {
            throw new DomainException("adapter.ordered.relation.no.slot.access", e);
        } catch (InvocationTargetException e) {
            throw handleInvocationTargetException(e, "adapter.ordered.relation.invocation.exception");
        } catch (NoSuchMethodException e) {
            throw new DomainException("adapter.ordered.relation.no.slot", e);
        }
    }

    @Override
    public void setOrder(ObjectType target, Integer order) {
        try {
            PropertyUtils.setProperty(target, this.slotName, order);
        } catch (IllegalAccessException e) {
            throw new DomainException("adapter.ordered.relation.no.slot.access", e);
        } catch (InvocationTargetException e) {
            throw handleInvocationTargetException(e, "adapter.ordered.relation.invocation.exception");
        } catch (NoSuchMethodException e) {
            throw new DomainException("adapter.ordered.relation.no.slot", e);
        }
    }

}