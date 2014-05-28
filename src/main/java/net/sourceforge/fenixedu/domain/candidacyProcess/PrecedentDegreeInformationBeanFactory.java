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
package net.sourceforge.fenixedu.domain.candidacyProcess;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.dataTransferObject.candidacy.PrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.student.PrecedentDegreeInformation;
import net.sourceforge.fenixedu.domain.student.curriculum.AverageType;
import net.sourceforge.fenixedu.domain.student.curriculum.Curriculum;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.LocalDate;

public class PrecedentDegreeInformationBeanFactory {

    public static final PrecedentDegreeInformationBean createBean(final IndividualCandidacy individualCandidacy) {

        if (individualCandidacy.isOver23()) {
            return null;
        } else if (individualCandidacy.isErasmus()) {
            return null;
        } else if (individualCandidacy.isSecondCycle() || individualCandidacy.isDegreeCandidacyForGraduatedPerson()) {
            return precedentDegreeInformationBeanForSecondCycleOrForGraduatedPerson(individualCandidacy);
        } else if (individualCandidacy.isDegreeTransfer() || individualCandidacy.isDegreeChange()) {
            return precedentDegreeInformationBeanForTransferOrChange(individualCandidacy);
        }

        return null;
    }

    private static final PrecedentDegreeInformationBean precedentDegreeInformationBeanForSecondCycleOrForGraduatedPerson(
            IndividualCandidacy individualCandidacy) {
        PrecedentDegreeInformationBean bean = new PrecedentDegreeInformationBean();
        PrecedentDegreeInformation pid = individualCandidacy.getRefactoredPrecedentDegreeInformation();

        bean.setPrecedentDegreeInformation(pid);
        bean.setDegreeDesignation(pid.getDegreeDesignation());
        bean.setInstitution(pid.getInstitution());
        bean.setInstitutionName(pid.getInstitutionName());
        bean.setCountry(pid.getCountry());
        bean.setConclusionDate(pid.getConclusionDate());
        bean.setConclusionGrade(pid.getConclusionGrade());

        return bean;
    }

    private static final PrecedentDegreeInformationBean precedentDegreeInformationBeanForTransferOrChange(
            IndividualCandidacy individualCandidacy) {
        PrecedentDegreeInformationBean bean = new PrecedentDegreeInformationBean();
        PrecedentDegreeInformation pid = individualCandidacy.getRefactoredPrecedentDegreeInformation();

        bean.setPrecedentDegreeInformation(pid);
        bean.setDegreeDesignation(pid.getPrecedentDegreeDesignation());
        bean.setInstitution(pid.getPrecedentInstitution());
        bean.setInstitutionName(pid.getPrecedentInstitution().getName());
        bean.setNumberOfEnroledCurricularCourses(pid.getNumberOfEnroledCurricularCourses());
        bean.setNumberOfApprovedCurricularCourses(pid.getNumberOfApprovedCurricularCourses());
        bean.setGradeSum(pid.getGradeSum());
        bean.setApprovedEcts(pid.getApprovedEcts());
        bean.setEnroledEcts(pid.getEnroledEcts());

        return bean;
    }

    public static final PrecedentDegreeInformationBean createBean(final StudentCurricularPlan studentCurricularPlan,
            final CycleType cycleType) {
        PrecedentDegreeInformationBean bean = new PrecedentDegreeInformationBean();

        if (!studentCurricularPlan.isBolonhaDegree() || cycleType == null) {
            throw new IllegalArgumentException();
        }

        bean.setDegreeDesignation(studentCurricularPlan.getName());
        bean.setInstitutionUnitName(Bennu.getInstance().getInstitutionUnit().getUnitName());

        if (studentCurricularPlan.getConclusionDate(cycleType) != null) {
            bean.setConclusionDate(new LocalDate(studentCurricularPlan.getConclusionDate(cycleType)));
        }

        if (studentCurricularPlan.getFinalAverage(cycleType) != null) {
            bean.setConclusionGrade(studentCurricularPlan.getFinalAverage(cycleType).toString());
        }

        bean.setNumberOfEnroledCurricularCourses(calculateNumberOfEnroledCurricularCourses(studentCurricularPlan));
        bean.setNumberOfApprovedCurricularCourses(calculateNumberOfApprovedCurricularCourses(studentCurricularPlan));
        bean.setGradeSum(calculateGradeSum(studentCurricularPlan));
        bean.setApprovedEcts(calculateApprovedEcts(studentCurricularPlan));
        bean.setEnroledEcts(calculateEnroledEcts(studentCurricularPlan));

        return bean;
    }

    public static final PrecedentDegreeInformationBean createBean(final StudentCurricularPlan studentCurricularPlan) {
        PrecedentDegreeInformationBean bean = new PrecedentDegreeInformationBean();

        if (studentCurricularPlan.isBolonhaDegree()
                || !studentCurricularPlan.getRegistration().isRegistrationConclusionProcessed()) {
            throw new IllegalArgumentException("error.studentCurricularPlan.must.be.pre.bolonha.and.concluded");
        }

        bean.setDegreeDesignation(studentCurricularPlan.getName());
        bean.setConclusionDate(new LocalDate(studentCurricularPlan.getRegistration().getConclusionDate()));
        bean.setConclusionGrade(studentCurricularPlan.getRegistration().getFinalAverage().toString());
        bean.setInstitution(Bennu.getInstance().getInstitutionUnit());
        bean.setInstitutionName(Bennu.getInstance().getInstitutionUnit().getName());
        bean.setInstitutionUnitName(Bennu.getInstance().getInstitutionUnit().getUnitName());

        bean.setNumberOfEnroledCurricularCourses(calculateNumberOfEnroledCurricularCourses(studentCurricularPlan));
        bean.setNumberOfApprovedCurricularCourses(calculateNumberOfApprovedCurricularCourses(studentCurricularPlan));
        bean.setGradeSum(calculateGradeSum(studentCurricularPlan));
        bean.setApprovedEcts(calculateApprovedEcts(studentCurricularPlan));
        bean.setEnroledEcts(calculateEnroledEcts(studentCurricularPlan));

        return bean;
    }

    private static final Integer calculateNumberOfEnroledCurricularCourses(final StudentCurricularPlan studentCurricularPlan) {
        return studentCurricularPlan.getRoot().getNumberOfAllEnroledCurriculumLines();
    }

    private static final Integer calculateNumberOfApprovedCurricularCourses(final StudentCurricularPlan studentCurricularPlan) {
        return studentCurricularPlan.getRoot().getNumberOfAllApprovedCurriculumLines();
    }

    private static final BigDecimal calculateGradeSum(final StudentCurricularPlan studentCurricularPlan) {
        final Curriculum curriculum = studentCurricularPlan.getRoot().getCurriculum();
        curriculum.setAverageType(AverageType.SIMPLE);
        return curriculum.getSumPiCi();
    }

    private static final BigDecimal calculateApprovedEcts(final StudentCurricularPlan studentCurricularPlan) {
        return BigDecimal.valueOf(studentCurricularPlan.getRoot().getAprovedEctsCredits());
    }

    private static final BigDecimal calculateEnroledEcts(final StudentCurricularPlan studentCurricularPlan) {
        return BigDecimal.valueOf(studentCurricularPlan.getRoot().getEctsCredits());
    }

}
