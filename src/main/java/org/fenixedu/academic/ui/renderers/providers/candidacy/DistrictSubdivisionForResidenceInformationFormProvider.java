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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.fenixedu.academic.domain.DistrictSubdivision;
import org.fenixedu.academic.domain.candidacy.workflow.form.ResidenceInformationForm;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DistrictSubdivisionForResidenceInformationFormProvider implements DataProvider {

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

    @Override
    public Object provide(Object source, Object currentValue) {
        final ResidenceInformationForm residenceInformationForm = (ResidenceInformationForm) source;
        if (residenceInformationForm.getDistrictOfResidence() != null) {
            List<DistrictSubdivision> result =
                    new ArrayList<DistrictSubdivision>(residenceInformationForm.getDistrictOfResidence()
                            .getDistrictSubdivisionsSet());
            Collections.sort(result, new BeanComparator("name"));
            return result;
        }

        return Collections.emptyList();
    }

}
