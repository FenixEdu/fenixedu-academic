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
                existingLoad.getTutorialOrientationHours(), existingLoad.getAutonomousWorkHours(), existingLoad.getEctsCredits(),
                existingLoad.getLoadOrder(), existingLoad.getAcademicPeriod());
    }

    public CompetenceCourseLoad(Double theoreticalHours, Double problemsHours, Double laboratorialHours, Double seminaryHours,
            Double fieldWorkHours, Double trainingPeriodHours, Double tutorialOrientationHours, Double autonomousWorkHours,
            Double ectsCredits, Integer order, AcademicPeriod academicPeriod) {
        this();
        setInformation(theoreticalHours, problemsHours, laboratorialHours, seminaryHours, fieldWorkHours, trainingPeriodHours,
                tutorialOrientationHours, autonomousWorkHours, ectsCredits, order, academicPeriod);
    }

    public void edit(Double theoreticalHours, Double problemsHours, Double laboratorialHours, Double seminaryHours,
            Double fieldWorkHours, Double trainingPeriodHours, Double tutorialOrientationHours, Double autonomousWorkHours,
            Double ectsCredits, Integer order, AcademicPeriod academicPeriod) {

        setInformation(theoreticalHours, problemsHours, laboratorialHours, seminaryHours, fieldWorkHours, trainingPeriodHours,
                tutorialOrientationHours, autonomousWorkHours, ectsCredits, order, academicPeriod);
    }

    private void setInformation(Double theoreticalHours, Double problemsHours, Double laboratorialHours, Double seminaryHours,
            Double fieldWorkHours, Double trainingPeriodHours, Double tutorialOrientationHours, Double autonomousWorkHours,
            Double ectsCredits, Integer order, AcademicPeriod academicPeriod) {

        setTheoreticalHours(theoreticalHours == null ? Double.valueOf(0) : theoreticalHours);
        setProblemsHours(problemsHours == null ? Double.valueOf(0) : problemsHours);
        setLaboratorialHours(laboratorialHours == null ? Double.valueOf(0) : laboratorialHours);
        setSeminaryHours(seminaryHours == null ? Double.valueOf(0) : seminaryHours);
        setFieldWorkHours(fieldWorkHours == null ? Double.valueOf(0) : fieldWorkHours);
        setTrainingPeriodHours(trainingPeriodHours == null ? Double.valueOf(0) : trainingPeriodHours);
        setTutorialOrientationHours(tutorialOrientationHours == null ? Double.valueOf(0) : tutorialOrientationHours);
        setAutonomousWorkHours(autonomousWorkHours == null ? Double.valueOf(0) : autonomousWorkHours);
        setEctsCredits(ectsCredits);
        setLoadOrder(order);
        setAcademicPeriod(academicPeriod);
    }

    public void delete() {
        setCompetenceCourseInformation(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    public double getTotalLessonHours() {
        return getTheoreticalHours().doubleValue() + getProblemsHours().doubleValue() + getLaboratorialHours().doubleValue()
                + getSeminaryHours().doubleValue() + getFieldWorkHours().doubleValue() + getTrainingPeriodHours().doubleValue()
                + getTutorialOrientationHours().doubleValue();
    }

    public Double getContactLoad() {
        return NUMBER_OF_WEEKS * getTotalLessonHours();
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

}
