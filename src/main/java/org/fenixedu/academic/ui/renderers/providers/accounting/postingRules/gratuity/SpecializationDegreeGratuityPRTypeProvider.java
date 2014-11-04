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
package net.sourceforge.fenixedu.presentationTier.renderers.providers.accounting.postingRules.gratuity;

import java.util.Arrays;

import net.sourceforge.fenixedu.domain.accounting.postingRules.gratuity.SpecializationDegreeGratuityByAmountPerEctsPR;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.BiDirectionalConverter;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class SpecializationDegreeGratuityPRTypeProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        return Arrays.asList(SpecializationDegreeGratuityByAmountPerEctsPR.class);
    }

    @Override
    public Converter getConverter() {
        return new BiDirectionalConverter() {

            /**
	     * 
	     */
            private static final long serialVersionUID = 1L;

            @Override
            public String deserialize(Object target) {
                return ((Class<?>) target).getName();
            }

            @Override
            public Object convert(Class type, Object target) {
                try {
                    return Class.forName((String) target);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

}
