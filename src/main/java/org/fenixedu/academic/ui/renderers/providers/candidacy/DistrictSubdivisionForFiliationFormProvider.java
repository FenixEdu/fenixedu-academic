/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.renderers.providers.candidacy;

import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.fenixedu.academic.domain.District;
import org.fenixedu.academic.domain.DistrictSubdivision;
import org.fenixedu.academic.domain.candidacy.workflow.form.FiliationForm;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DistrictSubdivisionForFiliationFormProvider implements DataProvider {

    @Override
    public Converter getConverter() {
        return null;
    }

    @Override
    public Object provide(Object source, Object currentValue) {
        final FiliationForm filiationForm = (FiliationForm) source;
        if (filiationForm.getDistrictOfBirth() != null) {
            final District district = District.readByName(filiationForm.getDistrictOfBirth());

            if (district != null) {
                return transformToStringCollection(district);
            }
        }

        return Collections.emptyList();
    }

    private Set<String> transformToStringCollection(final District district) {
        final SortedSet<String> result = new TreeSet<String>();

        for (final DistrictSubdivision each : district.getDistrictSubdivisionsSet()) {
            result.add(each.getName());
        }

        return result;
    }

}
