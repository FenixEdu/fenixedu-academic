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
package net.sourceforge.fenixedu.domain.classProperties;

import net.sourceforge.fenixedu.util.classProperties.GeneralClassPropertyName;
import net.sourceforge.fenixedu.util.classProperties.GeneralClassPropertyValue;

import org.fenixedu.bennu.core.domain.Bennu;

/**
 * @author David Santos in Apr 7, 2004
 */

public abstract class GeneralClassProperty extends GeneralClassProperty_Base {

    public GeneralClassProperty() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public GeneralClassPropertyName getName() {
        return new GeneralClassPropertyName(getNameString());
    }

    public void setName(GeneralClassPropertyName name) {
        setNameString(name.getName());
    }

    public GeneralClassPropertyValue getValue() {
        return new GeneralClassPropertyValue(getValueString());
    }

    public void setValue(GeneralClassPropertyValue value) {
        setValueString(value.getValue());
    }

    @Deprecated
    public boolean hasValueString() {
        return getValueString() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasNameString() {
        return getNameString() != null;
    }

}
