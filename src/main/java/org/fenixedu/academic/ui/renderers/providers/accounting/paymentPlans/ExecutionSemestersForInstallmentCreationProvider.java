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
package org.fenixedu.academic.ui.renderers.providers.accounting.paymentPlans;

import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.dto.accounting.paymentPlan.InstallmentBean;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyArrayConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ExecutionSemestersForInstallmentCreationProvider implements DataProvider {
    @Override
    public Object provide(Object source, Object currentValue) {
        final InstallmentBean installmentBean = (InstallmentBean) source;

        if (installmentBean.getPaymentPlanBean().getExecutionYear() != null) {
            final SortedSet<ExecutionSemester> result =
                    new TreeSet<ExecutionSemester>(ExecutionSemester.COMPARATOR_BY_SEMESTER_AND_YEAR);
            result.addAll(installmentBean.getPaymentPlanBean().getExecutionYear().getExecutionPeriodsSet());
            return result;
        }

        return Collections.EMPTY_LIST;

    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyArrayConverter();
    }
}
