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
package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.dataTransferObject.alumni.formation.IFormation;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class CivilYearsProvider implements DataProvider {

    public static class CivilYearsProviderDescendingOrder extends CivilYearsProvider {

        @Override
        public Object provide(Object source, Object currentValue) {

            Set<String> years = new TreeSet<String>(Collections.reverseOrder(String.CASE_INSENSITIVE_ORDER));
            years.addAll((Set<String>) super.provide(source, currentValue));
            return years;
        }

    }

    @Override
    public Object provide(Object source, Object currentValue) {

        IFormation formation = (IFormation) source;
        int firstYear = formation.getFirstYear();

        int currentYear = new DateTime().year().get();
        Set<String> years = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
        do {
            years.add(String.valueOf(firstYear));
        } while (++firstYear <= currentYear);

        return years;
    }

    @Override
    public Converter getConverter() {
        return null;
    }

}
