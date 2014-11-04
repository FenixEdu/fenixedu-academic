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

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionDegree;

public class ExecutionDegree1stAnd2ndCycleProviderForExecutionCourseSearchBean extends
        ExecutionDegreeProviderForExecutionCourseSearchBean {

    @Override
    public Object provide(Object source, Object currentValue) {
        List<ExecutionDegree> filteredExecutionDegrees = new ArrayList<ExecutionDegree>();
        final List<ExecutionDegree> executionDegrees = (List<ExecutionDegree>) super.provide(source, currentValue);
        for (ExecutionDegree executionDegree : executionDegrees) {
            if (executionDegree.getDegree().isFirstCycle() || executionDegree.getDegree().isSecondCycle()) {
                filteredExecutionDegrees.add(executionDegree);
            }
        }
        return filteredExecutionDegrees;
    }
}
