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
package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.studentEnrolment.bolonha;

import java.math.BigDecimal;
import java.math.RoundingMode;

import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationConclusionBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.Atomic;

public class CurriculumValidationServicesHelper {

    @Atomic
    public void concludeRegistration(final RegistrationConclusionBean conclusionBean) {
        final Registration registration = conclusionBean.getRegistration();

        registration.conclude();

        if (conclusionBean.hasEnteredConclusionDate() || conclusionBean.hasEnteredFinalAverageGrade()
                || conclusionBean.hasEnteredAverageGrade()) {

            checkEnteredConclusionDate(conclusionBean);

            checkEnteredFinalAverageGrade(conclusionBean);

            YearMonthDay conclusionDate =
                    conclusionBean.hasEnteredConclusionDate() ? new YearMonthDay(conclusionBean.getEnteredConclusionDate()) : registration
                            .getConclusionDate();

            Integer finalAverage =
                    conclusionBean.hasEnteredFinalAverageGrade() ? conclusionBean.getEnteredFinalAverageGrade() : registration
                            .getFinalAverage();

            BigDecimal averageGrade =
                    conclusionBean.hasEnteredAverageGrade() ? new BigDecimal(conclusionBean.getEnteredAverageGrade()).setScale(2,
                            RoundingMode.FLOOR) : registration.getAverage();

            registration.editConclusionInformation(null, finalAverage, averageGrade, conclusionDate, null);
        }
    }

    private void checkEnteredFinalAverageGrade(RegistrationConclusionBean conclusionBean) {
        if (conclusionBean.getEnteredFinalAverageGrade() < 10 || conclusionBean.getEnteredFinalAverageGrade() > 20) {
            throw new DomainException("error.RegistrationConclusionProcess.final.average.grade.is.not.correct");
        }
    }

    private void checkEnteredConclusionDate(final RegistrationConclusionBean conclusionBean) {
        final YearMonthDay startDate = conclusionBean.getRegistration().getStartDate();

        if (startDate.isAfter(conclusionBean.getEnteredConclusionDate())) {
            throw new DomainException("error.RegistrationConclusionProcess.start.date.is.after.entered.date");
        }

    }

}
