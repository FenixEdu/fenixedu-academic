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
package org.fenixedu.academic.dto.teacher.distribution;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.fenixedu.academic.dto.DataTranferObject;

import org.joda.time.Duration;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

/**
 * amak, jpmsit
 * 
 */
public class DistributionTeacherServicesByCourseDTO extends DataTranferObject {

    public class TeacherExecutionCourseServiceDTO {
        private final String teacherExternalId;

        private final String teacherUsername;

        private final String teacherName;

        private final Double hoursSpentByTeacher;

        private final Duration timeSpentByTeacher;

        private final Boolean teacherOfDepartment;

        public TeacherExecutionCourseServiceDTO(String teacherExternalId, String teacherUsername, String teacherName,
                Double hoursSpentByTeacher, Duration timeSpentByTeacher, Boolean teacherOfDepartment) {
            this.teacherExternalId = teacherExternalId;
            this.teacherUsername = teacherUsername;
            this.teacherName = teacherName;
            this.hoursSpentByTeacher = hoursSpentByTeacher;
            this.timeSpentByTeacher = timeSpentByTeacher;
            this.teacherOfDepartment = teacherOfDepartment;

        }

        public Double getHoursSpentByTeacher() {
            return hoursSpentByTeacher;
        }

        public Duration getTimeSpentByTeacher() {
            return timeSpentByTeacher;
        }

        public String getTeacherExternalId() {
            return teacherExternalId;
        }

        public String getTeacherUsername() {
            return teacherUsername;
        }

        public String getTeacherName() {
            return teacherName;
        }

        public String getDescription() {
            StringBuilder finalString = new StringBuilder(teacherUsername);

            finalString.append(" - ");
            finalString.append(teacherName);
            finalString.append(" - ");
            PeriodFormatter periodFormatter =
                    new PeriodFormatterBuilder().printZeroAlways().minimumPrintedDigits(2).appendHours().appendSuffix(":")
                            .appendMinutes().toFormatter();
            finalString.append(periodFormatter.print(getTimeSpentByTeacher().toPeriod()));
            return finalString.toString();

        }

        public Boolean getTeacherOfDepartment() {
            return teacherOfDepartment;
        }

    }

    public class ExecutionCourseDistributionServiceEntryDTO implements Comparable {
        private final String executionCourseExternalId;

        private final String executionCourseName;

        private final String executionCourseCampus;

        private final Set<String> executionCourseDegreeList;

        private final Set<String> executionCourseCurricularYearsList;

        private final Integer executionCourseSemester;

        private final Integer executionCourseFirstTimeEnrollementStudentNumber;

        private final Integer executionCourseSecondTimeEnrollementStudentNumber;

        private final Duration totalDuration;

        private final Double executionCourseTheoreticalHours;

        private final Double executionCoursePraticalHours;

        private final Double executionCourseLaboratorialHours;

        private final Double executionCourseTheoPratHours;

        private final Double executionCourseSeminaryHours;
        private final Double executionCourseProblemsHours;
        private final Double executionCourseTutorialOrientationHours;

        private final Double executionCourseFieldWorkHours;
        private final Double executionCourseTrainingPeriodHours;

        private Double executionCourseTotalHoursLecturedByTeachers;

        private Duration executionCourseTotalTimeLecturedByTeachers;

        private final Double executionCourseStudentsNumberByTheoreticalShift;

        private final Double executionCourseStudentsNumberByPraticalShift;

        private final Double executionCourseStudentsNumberByLaboratorialShift;

        private final Double executionCourseStudentsNumberByTheoPraticalShift;

        private final Double executionCourseStudentsNumberBySeminaryShift;
        private final Double executionCourseStudentsNumberByProblemsShift;
        private final Double executionCourseStudentsNumberByTutorialOrientationShift;
        private final Double executionCourseStudentsNumberByFieldWorkShift;
        private final Double executionCourseStudentsNumberByTrainingPeriodShift;

        private final List<TeacherExecutionCourseServiceDTO> teacherExecutionCourseServiceList;

        public ExecutionCourseDistributionServiceEntryDTO(String executionCourseExternalId, String executionCourseName,
                String executionCourseCampus, String executionCourseDegreeName, Set<String> executionCourseCurricularYearsList,
                Integer executionCourseSemester, Integer executionCourseFirstTimeEnrollementStudentNumber,
                Integer executionCourseSecondTimeEnrollementStudentNumber, Duration totalDuration,
                Double executionCourseTheoreticalHours, Double executionCoursePraticalHours,
                Double executionCourseLaboratorialHours, Double executionCourseTheoPratHours,
                Double executionCourseSeminaryHours, Double executionCourseProblemsHours,
                Double executionCourseTutorialOrientationHours, Double executionCourseFieldWorkHours,
                Double executionCourseTrainingPeriodHours, Double executionCourseStudentsNumberByTheoreticalShift,
                Double executionCourseStudentsNumberByPraticalShift, Double executionCourseStudentsNumberByLaboratorialShift,
                Double executionCourseStudentsNumberByTheoPraticalShift, Double seminaryStudentsNumberPerShift,
                Double problemsStudentsNumberPerShift, Double tutorialOrientationStudentsNumberPerShift,
                Double fieldWorkStudentsNumberPerShift, Double trainingPeriodStudentsNumberPerShift) {

            this.executionCourseExternalId = executionCourseExternalId;
            this.executionCourseName = executionCourseName;
            this.executionCourseCampus = executionCourseCampus;
            this.executionCourseCurricularYearsList = executionCourseCurricularYearsList;
            this.executionCourseSemester = executionCourseSemester;
            this.executionCourseFirstTimeEnrollementStudentNumber = executionCourseFirstTimeEnrollementStudentNumber;
            this.executionCourseSecondTimeEnrollementStudentNumber = executionCourseSecondTimeEnrollementStudentNumber;
            this.executionCourseTheoreticalHours = executionCourseTheoreticalHours;
            this.totalDuration = totalDuration;
            this.executionCoursePraticalHours = executionCoursePraticalHours;
            this.executionCourseTheoPratHours = executionCourseTheoPratHours;
            this.executionCourseSeminaryHours = executionCourseSeminaryHours;
            this.executionCourseProblemsHours = executionCourseProblemsHours;
            this.executionCourseTutorialOrientationHours = executionCourseTutorialOrientationHours;
            this.executionCourseFieldWorkHours = executionCourseFieldWorkHours;
            this.executionCourseTrainingPeriodHours = executionCourseTrainingPeriodHours;

            teacherExecutionCourseServiceList = new ArrayList<TeacherExecutionCourseServiceDTO>();

            this.executionCourseDegreeList = new HashSet<String>();

            this.executionCourseDegreeList.add(executionCourseDegreeName);

            this.executionCourseTotalHoursLecturedByTeachers = Double.valueOf(0);
            this.executionCourseTotalTimeLecturedByTeachers = Duration.ZERO;

            this.executionCourseLaboratorialHours = executionCourseLaboratorialHours;

            this.executionCourseStudentsNumberByTheoreticalShift = executionCourseStudentsNumberByTheoreticalShift;
            this.executionCourseStudentsNumberByPraticalShift = executionCourseStudentsNumberByPraticalShift;
            this.executionCourseStudentsNumberByLaboratorialShift = executionCourseStudentsNumberByLaboratorialShift;
            this.executionCourseStudentsNumberByTheoPraticalShift = executionCourseStudentsNumberByTheoPraticalShift;

            this.executionCourseStudentsNumberBySeminaryShift = seminaryStudentsNumberPerShift;
            this.executionCourseStudentsNumberByProblemsShift = problemsStudentsNumberPerShift;
            this.executionCourseStudentsNumberByTutorialOrientationShift = tutorialOrientationStudentsNumberPerShift;

            this.executionCourseStudentsNumberByFieldWorkShift = fieldWorkStudentsNumberPerShift;
            this.executionCourseStudentsNumberByTrainingPeriodShift = trainingPeriodStudentsNumberPerShift;

        }

        public String getExecutionCourseCampus() {
            return executionCourseCampus;
        }

        public Set<String> getExecutionCourseCurricularYear() {
            return executionCourseCurricularYearsList;
        }

        public Set<String> getExecutionCourseDegreeName() {
            return executionCourseDegreeList;
        }

        public Integer getExecutionCourseFirstTimeEnrollementStudentNumber() {
            return executionCourseFirstTimeEnrollementStudentNumber;
        }

        public String getExecutionCourseExternalId() {
            return executionCourseExternalId;
        }

        public Double getExecutionCourseTotalHoursLecturedByTeachers() {
            return executionCourseTotalHoursLecturedByTeachers;
        }

        public String getExecutionCourseName() {
            return executionCourseName;
        }

        public Double getExecutionCoursePraticalHours() {
            return executionCoursePraticalHours;
        }

        public Integer getExecutionCourseSecondTimeEnrollementStudentNumber() {
            return executionCourseSecondTimeEnrollementStudentNumber;
        }

        public Integer getExecutionCourseSemester() {
            return executionCourseSemester;
        }

        public Double getExecutionCourseTheoreticalHours() {
            return executionCourseTheoreticalHours;
        }

        public Double getExecutionCourseSeminaryHours() {
            return executionCourseSeminaryHours;
        }

        public Double getExecutionCourseProblemsHours() {
            return executionCourseProblemsHours;
        }

        public Double getExecutionCourseTutorialOrientationHours() {
            return executionCourseTutorialOrientationHours;
        }

        public Double getExecutionCourseFieldWorkHours() {
            return executionCourseFieldWorkHours;
        }

        public Double getExecutionCourseTrainingPeriodHours() {
            return executionCourseTrainingPeriodHours;
        }

        public List<TeacherExecutionCourseServiceDTO> getTeacherExecutionCourseServiceList() {
            return teacherExecutionCourseServiceList;
        }

        public Integer getExecutionCourseStudentsTotalNumber() {
            return executionCourseFirstTimeEnrollementStudentNumber + executionCourseSecondTimeEnrollementStudentNumber;
        }

        public Double getExecutionCourseTotalHours() {
            return executionCoursePraticalHours + executionCourseTheoreticalHours + executionCourseTheoPratHours
                    + executionCourseLaboratorialHours + executionCourseSeminaryHours + executionCourseProblemsHours
                    + executionCourseTutorialOrientationHours + executionCourseFieldWorkHours
                    + executionCourseTrainingPeriodHours;
        }

        public Duration getTotalDuration() {
            return totalDuration;
        }

        public String getTotalDurationString() {
            PeriodFormatter periodFormatter =
                    new PeriodFormatterBuilder().printZeroAlways().minimumPrintedDigits(2).appendHours().appendSuffix(":")
                            .appendMinutes().toFormatter();
            return periodFormatter.print(getTotalDuration().toPeriod());
        }

        public void addTeacher(TeacherExecutionCourseServiceDTO teacher) {
            teacherExecutionCourseServiceList.add(teacher);
        }

        public void addToCourseDegreesList(String degreeName) {
            executionCourseDegreeList.add(degreeName);
        }

        public void addToCurricularYears(Set<String> curricularYearsList) {
            executionCourseCurricularYearsList.addAll(curricularYearsList);
        }

        public Set<String> getExecutionCourseCurricularYearsList() {
            return executionCourseCurricularYearsList;
        }

        public Set<String> getExecutionCourseDegreeList() {
            return executionCourseDegreeList;
        }

        public void updateHoursLecturedByTeachers(Double hoursSpentByTeacher) {
            this.executionCourseTotalHoursLecturedByTeachers += hoursSpentByTeacher.doubleValue();
        }

        public void updateTimeLecturedByTeachers(Duration timeSpentByTeacher) {
            this.executionCourseTotalTimeLecturedByTeachers =
                    this.executionCourseTotalTimeLecturedByTeachers.plus(timeSpentByTeacher);
        }

        public Double getExecutionCourseHoursBalance() {
            DecimalFormat df = new DecimalFormat("#.##");
            DecimalFormatSymbols decimalFormatSymbols = df.getDecimalFormatSymbols();
            decimalFormatSymbols.setDecimalSeparator('.');
            df.setDecimalFormatSymbols(decimalFormatSymbols);
            return new Double(df.format(getExecutionCourseTotalHours() - executionCourseTotalHoursLecturedByTeachers));
        }

        public String getExecutionCourseDurationBalance() {
            Duration balance = getTotalDuration().minus(executionCourseTotalTimeLecturedByTeachers);
            PeriodFormatter periodFormatter =
                    new PeriodFormatterBuilder().printZeroAlways().minimumPrintedDigits(2).appendHours().appendSuffix(":")
                            .appendMinutes().toFormatter();
            return periodFormatter.print(balance.toPeriod());
        }

        public Double getExecutionCourseTheoPratHours() {
            return executionCourseTheoPratHours;
        }

        public String getFormattedExecutionCourseTheoPratHours() {

            return getFormattedValue(getExecutionCourseTheoPratHours());
        }

        public String getFormattedExecutionCoursePraticalHours() {

            return getFormattedValue(getExecutionCoursePraticalHours());
        }

        public String getFormattedExecutionCourseLaboratorialHours() {

            return getFormattedValue(getExecutionCourseLaboratorialHours());
        }

        public String getFormattedExecutionCourseTheoreticalHours() {
            return getFormattedValue(getExecutionCourseTheoreticalHours());
        }

        private String getFormattedValue(Double value) {
            StringBuilder sb = new StringBuilder();
            Formatter formatter = new Formatter(sb);

            formatter.format("%.1f", value);
            return sb.toString();
        }

        public Double getExecutionCourseLaboratorialHours() {
            return executionCourseLaboratorialHours;
        }

        public Double getExecutionCourseStudentsNumberByLaboratorialShift() {
            return executionCourseStudentsNumberByLaboratorialShift;
        }

        public Double getExecutionCourseStudentsNumberByPraticalShift() {
            return executionCourseStudentsNumberByPraticalShift;
        }

        public Double getExecutionCourseStudentsNumberByTheoPraticalShift() {
            return executionCourseStudentsNumberByTheoPraticalShift;
        }

        public Double getExecutionCourseStudentsNumberByTheoreticalShift() {
            return executionCourseStudentsNumberByTheoreticalShift;
        }

        public Double getExecutionCourseStudentsNumberBySeminaryShift() {
            return executionCourseStudentsNumberBySeminaryShift;
        }

        public Double getExecutionCourseStudentsNumberByProblemsShift() {
            return executionCourseStudentsNumberByProblemsShift;
        }

        public Double getExecutionCourseStudentsNumberByTutorialOrientationShift() {
            return executionCourseStudentsNumberByTutorialOrientationShift;
        }

        public Double getExecutionCourseStudentsNumberByFieldWorkShift() {
            return executionCourseStudentsNumberByFieldWorkShift;
        }

        public Double getExecutionCourseStudentsNumberByTrainingPeriodShift() {
            return executionCourseStudentsNumberByTrainingPeriodShift;
        }

        public String getFormattedExecutionCourseStudentsNumberByLaboratorialShift() {
            return getFormattedValue(getExecutionCourseStudentsNumberByLaboratorialShift());
        }

        public String getFormattedExecutionCourseStudentsNumberByPraticalShift() {
            return getFormattedValue(getExecutionCourseStudentsNumberByPraticalShift());
        }

        public String getFormattedExecutionCourseStudentsNumberByTheoPraticalShift() {
            return getFormattedValue(getExecutionCourseStudentsNumberByTheoPraticalShift());
        }

        public String getFormattedExecutionCourseStudentsNumberByTheoreticalShift() {
            return getFormattedValue(getExecutionCourseStudentsNumberByTheoreticalShift());
        }

        public String getFormattedExecutionCourseStudentsNumberBySeminaryShift() {
            return getFormattedValue(executionCourseStudentsNumberBySeminaryShift);
        }

        public String getFormattedExecutionCourseStudentsNumberByProblemsShift() {
            return getFormattedValue(executionCourseStudentsNumberByProblemsShift);
        }

        public String getFormattedExecutionCourseStudentsNumberByTutorialOrientationShift() {
            return getFormattedValue(executionCourseStudentsNumberByTutorialOrientationShift);
        }

        @Override
        public int compareTo(Object obj) {
            if (obj instanceof ExecutionCourseDistributionServiceEntryDTO) {
                ExecutionCourseDistributionServiceEntryDTO executionCourse1 = (ExecutionCourseDistributionServiceEntryDTO) obj;
                return this.getExecutionCourseName().compareTo(executionCourse1.getExecutionCourseName());
            }
            return 0;
        }

        public String getDegreeListString() {

            StringBuilder finalString = new StringBuilder();
            String[] stringArrayDegrees = executionCourseDegreeList.toArray(new String[] {});

            if (stringArrayDegrees.length > 0) {
                finalString.append(stringArrayDegrees[0]);

                for (int i = 1; i < stringArrayDegrees.length; i++) {
                    finalString.append(", ");
                    finalString.append(stringArrayDegrees[i]);
                }
            }
            return finalString.toString();
        }

        public String getCurricularYearListString() {
            StringBuilder finalString = new StringBuilder();
            String[] stringArrayYears = executionCourseCurricularYearsList.toArray(new String[] {});

            if (stringArrayYears.length > 0) {
                finalString.append(stringArrayYears[0]);

                for (int i = 1; i < stringArrayYears.length; i++) {
                    finalString.append(", ");
                    finalString.append(stringArrayYears[i]);
                }
            }
            return finalString.toString();
        }
    }

    private final Map<String, ExecutionCourseDistributionServiceEntryDTO> executionCourseMap;

    public DistributionTeacherServicesByCourseDTO() {
        executionCourseMap = new HashMap<String, ExecutionCourseDistributionServiceEntryDTO>();
    }

    public void addExecutionCourse(String executionCourseExternalId, String executionCourseName, String executionCourseCampus,
            String executionCourseDegreeName, Set<String> executionCourseCurricularYear, Integer executionCourseSemester,
            Integer executionCourseFirstTimeEnrollementStudentNumber, Integer executionCourseSecondTimeEnrollementStudentNumber,
            Duration totalDuration, Double executionCourseTheoreticalHours, Double executionCoursePraticalHours,
            Double executionCourseLaboratorialHours, Double executionCourseTheoPratHours, Double executionCourseSeminaryHours,
            Double executionCourseProblemsHours, Double executionCourseTutorialOrientationHours,
            Double executionCourseFieldWorkHours, Double executionCourseTrainingPeriodHours,
            Double executionCourseStudentsNumberByTheoreticalShift, Double executionCourseStudentsNumberByPraticalShift,
            Double executionCourseStudentsNumberByLaboratorialShift, Double executionCourseStudentsNumberByTheoPraticalShift,
            Double seminaryStudentsNumberPerShift, Double problemsStudentsNumberPerShift,
            Double tutorialOrientationStudentsNumberPerShift, Double fieldWorkStudentsNumberPerShift,
            Double trainingPeriodStudentsNumberPerShift) {
        ExecutionCourseDistributionServiceEntryDTO t =
                new ExecutionCourseDistributionServiceEntryDTO(executionCourseExternalId, executionCourseName,
                        executionCourseCampus, executionCourseDegreeName, executionCourseCurricularYear, executionCourseSemester,
                        executionCourseFirstTimeEnrollementStudentNumber, executionCourseSecondTimeEnrollementStudentNumber,
                        totalDuration, executionCourseTheoreticalHours, executionCoursePraticalHours,
                        executionCourseLaboratorialHours, executionCourseTheoPratHours, executionCourseSeminaryHours,
                        executionCourseProblemsHours, executionCourseTutorialOrientationHours, executionCourseFieldWorkHours,
                        executionCourseTrainingPeriodHours, executionCourseStudentsNumberByTheoreticalShift,
                        executionCourseStudentsNumberByPraticalShift, executionCourseStudentsNumberByLaboratorialShift,
                        executionCourseStudentsNumberByTheoPraticalShift, seminaryStudentsNumberPerShift,
                        problemsStudentsNumberPerShift, tutorialOrientationStudentsNumberPerShift,
                        fieldWorkStudentsNumberPerShift, trainingPeriodStudentsNumberPerShift);

        if (!executionCourseMap.containsKey(executionCourseExternalId)) {
            executionCourseMap.put(executionCourseExternalId, t);
        }
    }

    public void addTeacherToExecutionCourse(String keyExecutionCourse, String externalIdTeacher, String username, String name,
            Double hoursSpentByTeacher, Duration timeSpentByTeacher, boolean teacherBelongsToDepartment) {
        TeacherExecutionCourseServiceDTO teacher =
                new TeacherExecutionCourseServiceDTO(externalIdTeacher, username, name, hoursSpentByTeacher, timeSpentByTeacher,
                        teacherBelongsToDepartment);

        ExecutionCourseDistributionServiceEntryDTO executionCourseEntry = executionCourseMap.get(keyExecutionCourse);
        executionCourseEntry.updateHoursLecturedByTeachers(hoursSpentByTeacher);
        executionCourseEntry.updateTimeLecturedByTeachers(timeSpentByTeacher);
        executionCourseEntry.addTeacher(teacher);
    }

    public Map<String, ExecutionCourseDistributionServiceEntryDTO> getExecutionCourseMap() {
        return executionCourseMap;
    }

    public void addDegreeNameToExecutionCourse(String keyExecutionCourse, String degreeName) {

        executionCourseMap.get(keyExecutionCourse).addToCourseDegreesList(degreeName);
        return;

    }

    public void addCurricularYearsToExecutionCourse(String keyExecutionCourse, Set<String> curricularYearsList) {

        executionCourseMap.get(keyExecutionCourse).addToCurricularYears(curricularYearsList);
    }

}
