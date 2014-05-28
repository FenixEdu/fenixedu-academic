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
package net.sourceforge.fenixedu.presentationTier.renderers.providers.choiceType.replacement.multiple;

import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyArrayConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class FunctionProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        return CollectionUtils.select(Bennu.getInstance().getAccountabilityTypesSet(), new Predicate() {

            @Override
            public boolean evaluate(Object arg0) {
                return isFunction((AccountabilityType) arg0);
            }

        });
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyArrayConverter();
    }

    private boolean isFunction(final AccountabilityType type) {
        return type instanceof net.sourceforge.fenixedu.domain.organizationalStructure.Function;
    }

}
