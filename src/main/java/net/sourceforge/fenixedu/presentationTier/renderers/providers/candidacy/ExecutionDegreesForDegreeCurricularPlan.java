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
package net.sourceforge.fenixedu.presentationTier.renderers.providers.candidacy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.candidacy.DFACandidacyBean;
import net.sourceforge.fenixedu.domain.ExecutionDegree;

import org.apache.commons.beanutils.BeanComparator;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ExecutionDegreesForDegreeCurricularPlan implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {

        final DFACandidacyBean dfaCandidacyBean = (DFACandidacyBean) source;
        final List<ExecutionDegree> result = new ArrayList<ExecutionDegree>();
        if (dfaCandidacyBean.getDegree() != null && dfaCandidacyBean.getDegreeCurricularPlan() != null) {
            result.addAll(dfaCandidacyBean.getDegreeCurricularPlan().getExecutionDegreesSet());
            Collections.sort(result, new BeanComparator("year"));
        } else {
            dfaCandidacyBean.setExecutionDegree(null);
        }
        return result;
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
