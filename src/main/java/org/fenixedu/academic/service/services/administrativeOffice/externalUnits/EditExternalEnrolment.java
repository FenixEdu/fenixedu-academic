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
package org.fenixedu.academic.service.services.administrativeOffice.externalUnits;

import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.studentCurriculum.ExternalEnrolment;
import org.fenixedu.academic.dto.administrativeOffice.externalUnits.EditExternalEnrolmentBean;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

public class EditExternalEnrolment {

    @Atomic(mode = TxMode.WRITE)
    public static void run(final EditExternalEnrolmentBean bean, final Registration registration) {
        final ExternalEnrolment externalEnrolment = bean.getExternalEnrolment();
        externalEnrolment.edit(registration, bean.getGrade(), bean.getNormalizedGrade(), bean.getExecutionPeriod(), bean.getEvaluationDate(),
                bean.getEctsCredits());
    }
}
