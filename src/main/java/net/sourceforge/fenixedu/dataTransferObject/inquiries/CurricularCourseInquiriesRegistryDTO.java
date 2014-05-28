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
/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.inquiries.StudentInquiryRegistry;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.converters.IntegerNumberConverter;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class CurricularCourseInquiriesRegistryDTO implements Serializable {

    private StudentInquiryRegistry inquiryRegistry;

    private Integer weeklyHoursSpentPercentage;

    private Double studyDaysSpentInExamsSeason;

    private Integer autonomousWorkHoursForSimulation;

    private Integer attendenceClassesPercentage;

    public CurricularCourseInquiriesRegistryDTO(final StudentInquiryRegistry inquiryRegistry) {
        super();
        setInquiryRegistry(inquiryRegistry);
        setWeeklyHoursSpentPercentage(inquiryRegistry.getWeeklyHoursSpentPercentage());
        setStudyDaysSpentInExamsSeason(inquiryRegistry.getStudyDaysSpentInExamsSeason());
        setAttendenceClassesPercentage(inquiryRegistry.getAttendenceClassesPercentage());
    }

    public CurricularCourse getCurricularCourse() {
        return getInquiryRegistry().getCurricularCourse();
    }

    public Integer getWeeklyHoursSpentPercentage() {
        return weeklyHoursSpentPercentage;
    }

    public void setWeeklyHoursSpentPercentage(Integer weeklyHoursSpentPercentage) {
        this.weeklyHoursSpentPercentage = weeklyHoursSpentPercentage;
    }

    public Double getStudyDaysSpentInExamsSeason() {
        return studyDaysSpentInExamsSeason;
    }

    public double getWeeklyContactLoad() {
        BigDecimal result = new BigDecimal(getCurricularCourse().getCompetenceCourse().getContactLoad(getExecutionSemester()));
        return result.divide(new BigDecimal(14), 1, RoundingMode.UP).doubleValue();
    }

    public void setStudyDaysSpentInExamsSeason(Double studyDaysSpentInExamsSeason) {
        this.studyDaysSpentInExamsSeason = studyDaysSpentInExamsSeason;
    }

    public Double getCalculatedECTSCredits() {
        return calculateECTSCredits(getInquiryRegistry().getStudentInquiryExecutionPeriod().getWeeklyHoursSpentInClassesSeason());
    }

    public Double getSimulatedECTSCredits() {
        return calculateECTSCredits(getAutonomousWorkHoursForSimulation());
    }

    public Double calculateECTSCredits(final Integer weeklyHoursSpentInClassesSeason) {

        if (getWeeklyHoursSpentPercentage() == null || getStudyDaysSpentInExamsSeason() == null
                || weeklyHoursSpentInClassesSeason == null) {
            return 0d;
        }

        // a - weeklyHoursSpentInClassesSeason; b1 - attendenceClassesPercentage
        // c - weeklyHoursSpentPercentage; d - studyHoursSpentInExamsSeason
        final double result =
                ((weeklyHoursSpentInClassesSeason /* a */* (getWeeklyHoursSpentPercentage() / 100d) /* c */+ getWeeklyContactLoad() /* b */
                        * (getAttendenceClassesPercentage() / 100d) /* b1 */) * 14 + getStudyDaysSpentInExamsSeason() /* d */* 8) / 28;

        return new BigDecimal(result).setScale(1, BigDecimal.ROUND_UP).doubleValue();
    }

    public double getCourseEctsCredits() {
        return getCurricularCourse().getCompetenceCourse().getEctsCredits(getExecutionSemester().getSemester(),
                getExecutionSemester());
    }

    private ExecutionSemester getExecutionSemester() {
        return getInquiryRegistry().getExecutionPeriod();
    }

    public Double getSimulatedSpentHours() {
        if (getWeeklyHoursSpentPercentage() == null || getAutonomousWorkHoursForSimulation() == null) {
            return 0d;
        }

        final double result = (getWeeklyHoursSpentPercentage() / 100d) * getAutonomousWorkHoursForSimulation();
        return new BigDecimal(result).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public Integer getAutonomousWorkHoursForSimulation() {
        return autonomousWorkHoursForSimulation;
    }

    public void setAutonomousWorkHoursForSimulation(Integer autonomousWorkHoursForSimulation) {
        this.autonomousWorkHoursForSimulation = autonomousWorkHoursForSimulation;
    }

    public StudentInquiryRegistry getInquiryRegistry() {
        return inquiryRegistry;
    }

    public void setInquiryRegistry(StudentInquiryRegistry inquiryRegistry) {
        this.inquiryRegistry = inquiryRegistry;
    }

    public Integer getAttendenceClassesPercentage() {
        return attendenceClassesPercentage;
    }

    public void setAttendenceClassesPercentage(Integer attendenceClassesPercentage) {
        this.attendenceClassesPercentage = attendenceClassesPercentage;
    }

    public static class NumbersToHundred5To5 implements DataProvider {

        @Override
        public Object provide(Object source, Object currentValue) {
            List<Integer> numbers = new ArrayList<Integer>();
            for (int iter = 0; iter <= 100; iter += 5) {
                numbers.add(iter);
            }
            Collections.sort(numbers);
            Collections.reverse(numbers);
            return numbers;
        }

        @Override
        public Converter getConverter() {
            return new IntegerNumberConverter();
        }
    }
}
