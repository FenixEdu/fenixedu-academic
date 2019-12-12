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
package org.fenixedu.academic.service.services.administrativeOffice.student;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.fenixedu.academic.domain.Grade;
import org.fenixedu.academic.domain.GradeScaleEnum;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumGroup;
import org.fenixedu.academic.dto.student.RegistrationConclusionBean;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.Atomic;

public class RegistrationConclusionProcess {

    @Atomic
    public static void revert(final RegistrationConclusionBean conclusionBean) {
        conclusionBean.getConclusionProcess().disableLastVersion();
    }

    @Atomic
    public static void run(final RegistrationConclusionBean conclusionBean) {
        final Registration registration = conclusionBean.getRegistration();

        final CurriculumGroup curriculumGroup = conclusionBean.getCurriculumGroup();

        registration.conclude(curriculumGroup);

        if (conclusionBean.hasEnteredConclusionDate() || conclusionBean.hasEnteredFinalAverageGrade()
                || conclusionBean.hasEnteredAverageGrade() || conclusionBean.hasEnteredDescriptiveGrade()) {
            GradeScaleEnum gradeScale = registration.getDegree().getGradeScale();
            YearMonthDay conclusionDate = conclusionBean.getConclusionDate();
            Grade finalGrade = curriculumGroup.getFinalGrade();
            Grade rawGrade = curriculumGroup.getRawGrade();
            Grade descriptiveGrade = null;

            if (conclusionBean.hasEnteredConclusionDate()) {
                checkEnteredConclusionDate(conclusionBean);
                conclusionDate = new YearMonthDay(conclusionBean.getEnteredConclusionDate());
            }

            if (conclusionBean.hasEnteredFinalAverageGrade()) {
                checkGrade(conclusionBean.getEnteredFinalAverageGrade(), gradeScale);
                finalGrade = Grade.createGrade(conclusionBean.getEnteredFinalAverageGrade(), gradeScale);
            }

            if (conclusionBean.hasEnteredAverageGrade()) {
                checkGrade(conclusionBean.getEnteredAverageGrade(), gradeScale);
                rawGrade = Grade.createGrade(
                        new BigDecimal(conclusionBean.getEnteredAverageGrade()).setScale(2, RoundingMode.HALF_UP).toString(),
                        gradeScale);
            }

            if (conclusionBean.hasEnteredDescriptiveGrade()) {
                GradeScaleEnum qualitativeGradeScale =
                        GradeScaleEnum.valueOf(registration.getDegree().getQualitativeGradeScale().getCode());
                checkGrade(conclusionBean.getEnteredDescriptiveGrade(), qualitativeGradeScale);
                descriptiveGrade = Grade.createGrade(conclusionBean.getEnteredDescriptiveGrade(), qualitativeGradeScale);
            }

            curriculumGroup.editConclusionInformation(AccessControl.getPerson(), finalGrade, rawGrade, descriptiveGrade,
                    conclusionDate, conclusionBean.getObservations());
        }
    }

    private static void checkGrade(String value, GradeScaleEnum gradeScale) {
        if (!gradeScale.belongsTo(value)) {
            throw new DomainException("error.RegistrationConclusionProcess.final.average.is.invalid", value,
                    BundleUtil.getLocalizedString(Bundle.ENUMERATION, gradeScale.name()).getContent());
        }
    }

    private static void checkEnteredConclusionDate(final RegistrationConclusionBean conclusionBean) {
        final YearMonthDay startDate = conclusionBean.getRegistration().getStartDate();

        if (startDate.isAfter(conclusionBean.getEnteredConclusionDate())) {
            throw new DomainException("error.RegistrationConclusionProcess.start.date.is.after.entered.date");
        }

    }

}
