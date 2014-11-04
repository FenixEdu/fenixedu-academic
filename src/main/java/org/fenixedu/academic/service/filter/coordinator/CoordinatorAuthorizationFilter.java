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
package org.fenixedu.academic.service.filter.coordinator;

import java.util.Comparator;

import org.fenixedu.academic.domain.Coordinator;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.service.filter.Filtro;

/**
 * Base filter for all resources that can be accessed by an degree coordinator.
 * 
 * @author cfgi
 */
public abstract class CoordinatorAuthorizationFilter extends Filtro {

    /**
     * Compares coordinators by they respective execution year. The most recent
     * execution year first.
     * 
     * @author cfgi
     */
    public static class CoordinatorByExecutionDegreeComparator implements Comparator<Coordinator> {

        @Override
        public int compare(Coordinator o1, Coordinator o2) {
            return ExecutionDegree.EXECUTION_DEGREE_COMPARATORY_BY_YEAR.compare(o2.getExecutionDegree(), o1.getExecutionDegree());
        }

    }

}
