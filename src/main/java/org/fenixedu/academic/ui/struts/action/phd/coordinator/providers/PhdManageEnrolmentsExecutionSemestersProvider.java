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
package org.fenixedu.academic.ui.struts.action.phd.coordinator.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.comparators.ReverseComparator;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.phd.ManageEnrolmentsBean;
import org.fenixedu.academic.ui.renderers.providers.AbstractDomainObjectProvider;

public class PhdManageEnrolmentsExecutionSemestersProvider extends AbstractDomainObjectProvider {

    @Override
    public Object provide(Object source, Object obj) {
        final ManageEnrolmentsBean bean = (ManageEnrolmentsBean) source;

        final List<ExecutionSemester> result = new ArrayList<ExecutionSemester>();

        ExecutionSemester each = bean.getProcess().getExecutionYear().getFirstExecutionPeriod();
        while (each != null) {
            result.add(each);
            each = each.getNextExecutionPeriod();
        }

        Collections.sort(result, new ReverseComparator(ExecutionSemester.COMPARATOR_BY_SEMESTER_AND_YEAR));

        return result;
    }
}
