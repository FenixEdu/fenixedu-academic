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
package net.sourceforge.fenixedu.presentationTier.renderers.providers.lists;

import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.lists.ExecutionDegreeListBean;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ExecutionYearsProviderForListsContextBean implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        final SortedSet<ExecutionYear> executionYears = new TreeSet<ExecutionYear>(ExecutionYear.REVERSE_COMPARATOR_BY_YEAR);

        final ExecutionDegreeListBean executionDegreeBean = (ExecutionDegreeListBean) source;
        if (executionDegreeBean.getDegreeCurricularPlan() != null) {
            final SortedSet<ExecutionDegree> executionDegrees =
                    new TreeSet<ExecutionDegree>(ExecutionDegree.EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME);
            executionDegrees.addAll(executionDegreeBean.getDegreeCurricularPlan().getExecutionDegrees());

            for (ExecutionDegree exeDegree : executionDegrees) {
                executionYears.add(exeDegree.getExecutionYear());
            }
        } else if (executionDegreeBean.getDegreeCurricularPlan() == null) {
            executionYears.addAll(ExecutionYear.readNotClosedExecutionYears());
        }

        return executionYears;
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
