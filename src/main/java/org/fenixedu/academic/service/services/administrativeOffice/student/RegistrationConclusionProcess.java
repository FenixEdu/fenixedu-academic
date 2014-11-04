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
package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.student;

import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationConclusionBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.Atomic;

public class RegistrationConclusionProcess {

    @Atomic
    public static void run(final RegistrationConclusionBean conclusionBean) {
        final Registration registration = conclusionBean.getRegistration();

        if (registration.isBolonha()) {
            final CycleCurriculumGroup cycleCurriculumGroup = conclusionBean.getCycleCurriculumGroup();

            registration.conclude(cycleCurriculumGroup);

            if (conclusionBean.hasEnteredConclusionDate()) {

                checkEnteredConclusionDate(conclusionBean);

                cycleCurriculumGroup.editConclusionInformation(AccessControl.getPerson(), cycleCurriculumGroup.getFinalAverage(),
                        new YearMonthDay(conclusionBean.getEnteredConclusionDate()), conclusionBean.getObservations());
            }

        } else {
            registration.conclude();

            if (conclusionBean.hasEnteredConclusionDate()) {

                checkEnteredConclusionDate(conclusionBean);

                registration.editConclusionInformation(AccessControl.getPerson(), registration.getFinalAverage(),
                        new YearMonthDay(conclusionBean.getEnteredConclusionDate()), conclusionBean.getObservations());
            }
        }
    }

    private static void checkEnteredConclusionDate(final RegistrationConclusionBean conclusionBean) {
        final YearMonthDay startDate = conclusionBean.getRegistration().getStartDate();

        if (startDate.isAfter(conclusionBean.getEnteredConclusionDate())) {
            throw new DomainException("error.RegistrationConclusionProcess.start.date.is.after.entered.date");
        }

    }

}
