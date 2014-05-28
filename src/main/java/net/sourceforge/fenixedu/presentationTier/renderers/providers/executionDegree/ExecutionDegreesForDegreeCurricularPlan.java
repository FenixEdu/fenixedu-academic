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
package net.sourceforge.fenixedu.presentationTier.renderers.providers.executionDegree;

import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.ExecutionDegreeBean;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;

import org.apache.commons.collections.comparators.ReverseComparator;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ExecutionDegreesForDegreeCurricularPlan implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        Set<ExecutionDegree> result =
                new TreeSet<ExecutionDegree>(new ReverseComparator(ExecutionDegree.EXECUTION_DEGREE_COMPARATORY_BY_YEAR));

        ExecutionDegreeBean executionDegreeBean = (ExecutionDegreeBean) source;
        DegreeCurricularPlan degreeCurricularPlan = executionDegreeBean.getDegreeCurricularPlan();

        if (degreeCurricularPlan != null) {
            result.addAll(degreeCurricularPlan.getExecutionDegrees());
        }

        return result;
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
