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
package org.fenixedu.academic.domain.degreeStructure;

import java.math.BigDecimal;
import java.util.Optional;

import org.fenixedu.academic.domain.time.calendarStructure.AcademicPeriod;
import org.fenixedu.bennu.core.domain.Bennu;

public class CompetenceCourseLoad extends CompetenceCourseLoad_Base implements Comparable {

    public static int NUMBER_OF_WEEKS = 14;

    protected CompetenceCourseLoad() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public CompetenceCourseLoad(CompetenceCourseLoad existingLoad) {
        this(existingLoad.getTheoreticalHours(), existingLoad.getProblemsHours(), existingLoad.getLaboratorialHours(),
                existingLoad.getSeminaryHours(), existingLoad.getFieldWorkHours(), existingLoad.getTrainingPeriodHours(),
                existingLoad.getTutorialOrientationHours(), existingLoad.getOtherHours(), existingLoad.getAutonomousWorkHours(),
                existingLoad.getEctsCredits(), existingLoad.getLoadOrder(), existingLoad.getAcademicPeriod());
    }

    public CompetenceCourseLoad(CompetenceCourseInformation courseInformation, Double theoreticalHours, Double problemsHours,
            Double laboratorialHours, Double seminaryHours, Double fieldWorkHours, Double trainingPeriodHours,
            Double tutorialOrientationHours, Double otherHours, Double autonomousWorkHours, Double ectsCredits, Integer order,
            AcademicPeriod academicPeriod) {
        this();
        setCompetenceCourseInformation(courseInformation);
        setInformation(theoreticalHours, problemsHours, laboratorialHours, seminaryHours, fieldWorkHours, trainingPeriodHours,
                tutorialOrientationHours, otherHours, autonomousWorkHours, ectsCredits, order, academicPeriod);
    }

    public CompetenceCourseLoad(Double theoreticalHours, Double problemsHours, Double laboratorialHours, Double seminaryHours,
            Double fieldWorkHours, Double trainingPeriodHours, Double tutorialOrientationHours, Double otherHours,
            Double autonomousWorkHours, Double ectsCredits, Integer order, AcademicPeriod academicPeriod) {
        this();
        setInformation(theoreticalHours, problemsHours, laboratorialHours, seminaryHours, fieldWorkHours, trainingPeriodHours,
                tutorialOrientationHours, otherHours, autonomousWorkHours, ectsCredits, order, academicPeriod);
    }

    public void edit(Double theoreticalHours, Double problemsHours, Double laboratorialHours, Double seminaryHours,
            Double fieldWorkHours, Double trainingPeriodHours, Double tutorialOrientationHours, Double otherHours,
            Double autonomousWorkHours, Double ectsCredits, Integer order, AcademicPeriod academicPeriod) {

        setInformation(theoreticalHours, problemsHours, laboratorialHours, seminaryHours, fieldWorkHours, trainingPeriodHours,
                tutorialOrientationHours, otherHours, autonomousWorkHours, ectsCredits, order, academicPeriod);
    }

    private void setInformation(Double theoreticalHours, Double problemsHours, Double laboratorialHours, Double seminaryHours,
            Double fieldWorkHours, Double trainingPeriodHours, Double tutorialOrientationHours, Double otherHours,
            Double autonomousWorkHours, Double ectsCredits, Integer order, AcademicPeriod academicPeriod) {

        setTheoreticalHours(theoreticalHours == null ? Double.valueOf(0) : theoreticalHours);
        setProblemsHours(problemsHours == null ? Double.valueOf(0) : problemsHours);
        setLaboratorialHours(laboratorialHours == null ? Double.valueOf(0) : laboratorialHours);
        setSeminaryHours(seminaryHours == null ? Double.valueOf(0) : seminaryHours);
        setFieldWorkHours(fieldWorkHours == null ? Double.valueOf(0) : fieldWorkHours);
        setTrainingPeriodHours(trainingPeriodHours == null ? Double.valueOf(0) : trainingPeriodHours);
        setTutorialOrientationHours(tutorialOrientationHours == null ? Double.valueOf(0) : tutorialOrientationHours);
        setOtherHours(otherHours == null ? Double.valueOf(0) : otherHours);
        setAutonomousWorkHours(autonomousWorkHours == null ? Double.valueOf(0) : autonomousWorkHours);
        setEctsCredits(ectsCredits);
        setLoadOrder(order);
        setAcademicPeriod(academicPeriod);
    }

    @Override
    public Double getOtherHours() {
        return super.getOtherHours() != null ? super.getOtherHours() : Double.valueOf(0);
    }

    public void delete() {
        setCompetenceCourseInformation(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    public double getTotalLessonHours() {
        return getTheoreticalHours().doubleValue() + getProblemsHours().doubleValue() + getLaboratorialHours().doubleValue()
                + getSeminaryHours().doubleValue() + getFieldWorkHours().doubleValue() + getTrainingPeriodHours().doubleValue()
                + getTutorialOrientationHours().doubleValue() + getOtherHours().doubleValue();
    }

    public Double getContactLoad() {
        return getTotalLessonHours();
    }

    public Double getTotalLoad() {
        return getAutonomousWorkHours() + getContactLoad();
    }

    @Override
    public int compareTo(Object o) {
        return getOrder().compareTo(((CompetenceCourseLoad) o).getOrder());
    }

    @Deprecated
    public Integer getOrder() {
        return super.getLoadOrder();
    }

    @Deprecated
    public void setOrder(Integer order) {
        super.setLoadOrder(order);
    }

    @Override
    public void setTheoreticalHours(Double theoreticalHours) {
        super.setTheoreticalHours(theoreticalHours);

        final Double otherLoadIfAnual = getOtherLoadIfAnual().map(CompetenceCourseLoad::getTheoreticalHours).orElse(null);
        updateCourseLoadDuration(CourseLoadType.THEORETICAL, theoreticalHours, otherLoadIfAnual);
    }

    @Override
    public void setProblemsHours(Double problemsHours) {
        super.setProblemsHours(problemsHours);

        final Double otherLoadIfAnual = getOtherLoadIfAnual().map(CompetenceCourseLoad::getProblemsHours).orElse(null);
        updateCourseLoadDuration(CourseLoadType.THEORETICAL_PRACTICAL, problemsHours, otherLoadIfAnual);
    }

    @Override
    public void setLaboratorialHours(Double laboratorialHours) {
        super.setLaboratorialHours(laboratorialHours);

        final Double otherLoadIfAnual = getOtherLoadIfAnual().map(CompetenceCourseLoad::getLaboratorialHours).orElse(null);
        updateCourseLoadDuration(CourseLoadType.PRACTICAL_LABORATORY, laboratorialHours, otherLoadIfAnual);
    }

    @Override
    public void setSeminaryHours(Double seminaryHours) {
        super.setSeminaryHours(seminaryHours);

        final Double otherLoadIfAnual = getOtherLoadIfAnual().map(CompetenceCourseLoad::getSeminaryHours).orElse(null);
        updateCourseLoadDuration(CourseLoadType.SEMINAR, seminaryHours, otherLoadIfAnual);
    }

    @Override
    public void setFieldWorkHours(Double fieldWorkHours) {
        super.setFieldWorkHours(fieldWorkHours);

        final Double otherLoadIfAnual = getOtherLoadIfAnual().map(CompetenceCourseLoad::getFieldWorkHours).orElse(null);
        updateCourseLoadDuration(CourseLoadType.FIELD_WORK, fieldWorkHours, otherLoadIfAnual);
    }

    @Override
    public void setTrainingPeriodHours(Double trainingPeriodHours) {
        super.setTrainingPeriodHours(trainingPeriodHours);

        final Double otherLoadIfAnual = getOtherLoadIfAnual().map(CompetenceCourseLoad::getTrainingPeriodHours).orElse(null);
        updateCourseLoadDuration(CourseLoadType.INTERNSHIP, trainingPeriodHours, otherLoadIfAnual);
    }

    @Override
    public void setTutorialOrientationHours(Double tutorialOrientationHours) {
        super.setTutorialOrientationHours(tutorialOrientationHours);

        final Double otherLoadIfAnual = getOtherLoadIfAnual().map(CompetenceCourseLoad::getTutorialOrientationHours).orElse(null);
        updateCourseLoadDuration(CourseLoadType.TUTORIAL_ORIENTATION, tutorialOrientationHours, otherLoadIfAnual);
    }

    @Override
    public void setOtherHours(Double otherHours) {
        super.setOtherHours(otherHours);

        final Double otherLoadIfAnual = getOtherLoadIfAnual().map(CompetenceCourseLoad::getOtherHours).orElse(null);
        updateCourseLoadDuration(CourseLoadType.OTHER, otherHours, otherLoadIfAnual);
    }

    @Override
    public void setAutonomousWorkHours(Double autonomousWorkHours) {
        super.setAutonomousWorkHours(autonomousWorkHours);

        final Double otherLoadIfAnual = getOtherLoadIfAnual().map(CompetenceCourseLoad::getAutonomousWorkHours).orElse(null);
        updateCourseLoadDuration(CourseLoadType.AUTONOMOUS_WORK, autonomousWorkHours, otherLoadIfAnual);
    }

    private Optional<CompetenceCourseLoad> getOtherLoadIfAnual() {
        final CompetenceCourseInformation courseInformation = getCompetenceCourseInformation();
        return courseInformation != null && courseInformation.isAnual() ? getCompetenceCourseInformation()
                .getCompetenceCourseLoadsSet().stream().filter(ccl -> ccl != this).findAny() : Optional.empty();
    }

    private void updateCourseLoadDuration(String loadTypeCode, Double hours, Double otherHoursIfAnual) {
        final CompetenceCourseInformation courseInformation = getCompetenceCourseInformation();
        if (courseInformation == null) {
            return; // being created.. //TODO fix this!
        }

        CourseLoadType.findByCode(loadTypeCode).ifPresent(loadType -> {
            final Optional<CourseLoadDuration> duration = courseInformation.findLoadDurationByType(loadType);

            final Double totalHours = sumHours(hours, otherHoursIfAnual);

            if (totalHours != null && totalHours.doubleValue() != 0d) {
                duration.orElseGet(() -> CourseLoadDuration.create(courseInformation, loadType, null))
                        .setHours(BigDecimal.valueOf(totalHours));
            } else {
                duration.ifPresent(CourseLoadDuration::delete);
            }
        });
    }

    private static Double sumHours(Double hours1, Double hours2) {
        if (hours1 == null) {
            return hours2;
        }
        if (hours2 == null) {
            return hours1;
        }
        return Double.valueOf(hours1.doubleValue() + hours2.doubleValue());
    }
}
