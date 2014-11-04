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

import java.util.Collection;

/**
 * Provides an interface to obtain all the information required by the
 * {@link net.sourceforge.fenixedu.util.domain.OrderedRelationAdapter
 * OrderedRelationAdapter}.
 * 
 * @author cfgi
 */
public interface OrdinalAccessor<HolderType, ObjectType> {

    /**
     * @param holder
     *            the holder of the ordered relation
     * @return the elements having an order in the relation
     */
    public Collection<ObjectType> getObjects(HolderType holder);

    /**
     * @param target
     *            one of the objects being ordered in the relation
     * @return the current order for the object
     */
    public Integer getOrder(ObjectType target);

    /**
     * Changes the order of the target.
     * 
     * @param target
     *            one of the objects being ordered in the relation
     * @param order
     *            the new order for the target
     */
    public void setOrder(ObjectType target, Integer order);
}