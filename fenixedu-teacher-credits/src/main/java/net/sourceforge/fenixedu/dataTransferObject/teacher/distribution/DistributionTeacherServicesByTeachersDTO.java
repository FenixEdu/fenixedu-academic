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

import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.fenixedu.academic.dto.DataTranferObject;
import org.fenixedu.academic.domain.personnelSection.contracts.PersonContractSituation;

import org.joda.time.Duration;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

/**
 * amak, jpmsit
 * 
 */
public class DistributionTeacherServicesByTeachersDTO extends DataTranferObject {

    public class ExecutionCourseTeacherServiceDTO {
        private final String executionCourseExternalId;

        private final String executionCourseName;

        private final Integer hoursSpentByTeacher;

        private final Duration timeSpentByTeacher;

        private final Map<String, String> courseDegreesList;

        private final String executionPeriodName;

        private final Map<String, Set<String>> executionYearsSet;

        public ExecutionCourseTeacherServiceDTO(String externalId, String name, Integer hours, Duration timeSpentByTeacher,
                Map<String, String> executionCourseDegreesNameMap, Map<String, Set<String>> executionYearsMap, String periodName) {
            super();

            this.executionCourseExternalId = externalId;
            this.hoursSpentByTeacher = hours;
            this.timeSpentByTeacher = timeSpentByTeacher;
            this.executionCourseName = name;
            this.executionYearsSet = executionYearsMap;
            this.executionPeriodName = periodName;
            this.courseDegreesList = executionCourseDegreesNameMap;
        }

        public String getExecutionCourseExternalId() {
            return executionCourseExternalId;
        }

        public String getExecutionCourseName() {
            return executionCourseName;
        }

        public Integer getHoursSpentByTeacher() {
            return hoursSpentByTeacher;
        }

        public Map<String, String> getCourseDegreesList() {
            return courseDegreesList;
        }

        public String getExecutionPeriodName() {
            return executionPeriodName;
        }

        public Duration getTimeSpentByTeacher() {
            return timeSpentByTeacher;
        }

        public String getDescription() {
            StringBuilder finalString = new StringBuilder(getExecutionCourseName());

            finalString.append("(");

            Set<String> degreeIdSet = courseDegreesList.keySet();

            Iterator<String> iteratorDegreeIdSet = degreeIdSet.iterator();

            if (iteratorDegreeIdSet.hasNext()) {
                String firstDegreeExternalId = iteratorDegreeIdSet.next();

                finalString.append(courseDegreesList.get(firstDegreeExternalId));
                finalString.append(" (");

                Set<String> firstCurricularYearsSet = executionYearsSet.get(firstDegreeExternalId);

                Iterator<String> iteratorFirstCurricularYearsSet = firstCurricularYearsSet.iterator();

                if (iteratorFirstCurricularYearsSet.hasNext()) {
                    finalString.append(iteratorFirstCurricularYearsSet.next());

                    while (iteratorFirstCurricularYearsSet.hasNext()) {
                        finalString.append(", ");
                        finalString.append(iteratorFirstCurricularYearsSet.next());
                    }

                    finalString.append("ºano");
                }

                finalString.append(")");

                while (iteratorDegreeIdSet.hasNext()) {
                    finalString.append(", ");

                    String degreeExternalId = iteratorDegreeIdSet.next();

                    finalString.append(courseDegreesList.get(degreeExternalId));
                    finalString.append(" (");

                    Set<String> curricularYearsSet = executionYearsSet.get(degreeExternalId);

                    Iterator<String> iteratorCurricularYearsSet = curricularYearsSet.iterator();

                    if (iteratorCurricularYearsSet.hasNext()) {
                        finalString.append(iteratorCurricularYearsSet.next());

                        while (iteratorCurricularYearsSet.hasNext()) {
                            finalString.append(", ");
                            finalString.append(iteratorCurricularYearsSet.next());
                        }

                        finalString.append("ºano");
                    }
                    finalString.append(")");

                }

                finalString.append(")");
            }

            finalString.append("/");
            finalString.append("(");
            finalString.append(executionPeriodName);
            finalString.append(")");
            finalString.append(" - ");
            PeriodFormatter periodFormatter =
                    new PeriodFormatterBuilder().printZeroAlways().minimumPrintedDigits(2).appendHours().appendSuffix(":")
                            .appendMinutes().toFormatter();
            finalString.append(periodFormatter.print(getTimeSpentByTeacher().toPeriod()));
            return finalString.toString();
        }

    }

    public class TeacherCreditsInfoDTO {
        private Set<PersonContractSituation> exemptionTypes;

        private String functionName;

        private final Double credits;

        TeacherCreditsInfoDTO(Set<PersonContractSituation> exemptionTypes, Double credits) {
            this.exemptionTypes = exemptionTypes;
            this.credits = credits;
        }

        TeacherCreditsInfoDTO(String functionName, Double credits) {
            this.functionName = functionName;
            this.credits = credits;
        }

        public Double getCredits() {
            return credits;
        }

        public Set<PersonContractSituation> getExemptionTypes() {
            return exemptionTypes;
        }

        public String getFunctionName() {
            return functionName;
        }
    }

    public class TeacherDistributionServiceEntryDTO implements Comparable {
        private final String teacherExternalId;

        private final String teacherId;

        private final String teacherCategory;

        private final String teacherName;

        private Double teacherRequiredHours;

        private final Double teacherAccumulatedCredits;

        List<ExecutionCourseTeacherServiceDTO> executionCourseTeacherServiceList;

        List<TeacherCreditsInfoDTO> managementFunctionList;

        List<TeacherCreditsInfoDTO> exemptionSituationList;

        public TeacherDistributionServiceEntryDTO(String internal, String teacherId, String category, String name, Double hours,
                Double accumulatedCredits) {
            this.teacherId = teacherId;
            teacherCategory = category;
            teacherExternalId = internal;
            teacherName = name;
            teacherRequiredHours = hours;
            teacherAccumulatedCredits = accumulatedCredits;

            executionCourseTeacherServiceList = new ArrayList<ExecutionCourseTeacherServiceDTO>();

            managementFunctionList = new ArrayList<TeacherCreditsInfoDTO>();

            exemptionSituationList = new ArrayList<TeacherCreditsInfoDTO>();
        }

        public List<ExecutionCourseTeacherServiceDTO> getExecutionCourseTeacherServiceList() {
            return executionCourseTeacherServiceList;
        }

        public String getTeacherCategory() {
            return teacherCategory;
        }

        public String getTeacherExternalId() {
            return teacherExternalId;
        }

        public String getTeacherName() {
            return teacherName;
        }

        public Double getTeacherRequiredHours() {
            return teacherRequiredHours;
        }

        public Double getTeacherSpentCredits() {
            double credits = 0d;

            for (TeacherCreditsInfoDTO managementCredits : managementFunctionList) {
                credits += managementCredits.getCredits();
            }
            for (TeacherCreditsInfoDTO exemptionCredits : exemptionSituationList) {
                credits += exemptionCredits.getCredits();
            }

            return credits;
        }

        public void addExecutionCourse(ExecutionCourseTeacherServiceDTO executionCourse) {
            executionCourseTeacherServiceList.add(executionCourse);
        }

        public String getTeacherId() {
            return teacherId;
        }

        private String getFormattedValue(Double value) {
            StringBuilder sb = new StringBuilder();
            Formatter formatter = new Formatter(sb);

            formatter.format("%.1f", value);
            return sb.toString();
        }

        public String getFormattedTeacherSpentCredits() {

            return getFormattedValue(getTeacherSpentCredits());
        }

        public String getFormattedTeacherAccumulatedCredits() {

            return getFormattedValue(getAccumulatedCredits());
        }

        public Integer getTotalLecturedHours() {
            int totalHours = 0;

            for (ExecutionCourseTeacherServiceDTO executionCourse : executionCourseTeacherServiceList) {
                totalHours += executionCourse.getHoursSpentByTeacher();
            }

            return totalHours;
        }

        public Integer getAvailability() {
            double availability = getTeacherRequiredHours() - getTotalLecturedHours() - getTeacherSpentCredits();

            return new Double(StrictMath.ceil(StrictMath.abs(availability)) * StrictMath.signum(availability)).intValue();
        }

        public Double getAccumulatedCredits() {
            return teacherAccumulatedCredits;
        }

        @Override
        public int compareTo(Object obj) {
            if (obj instanceof TeacherDistributionServiceEntryDTO) {
                TeacherDistributionServiceEntryDTO teacher1 = (TeacherDistributionServiceEntryDTO) obj;
                if (this.getTeacherId() != null) {
                    return this.getTeacherId().compareTo(teacher1.getTeacherId());
                } else {
                    if (this.getTeacherId().equals(teacher1.getTeacherId())) {
                        return 0;
                    } else {
                        return 1;
                    }

                }
            }
            return 0;
        }

        public void setTeacherRequiredHours(Double teacherRequiredHours) {
            this.teacherRequiredHours = teacherRequiredHours;
        }

        public List<TeacherCreditsInfoDTO> getManagementFunctionList() {
            return managementFunctionList;
        }

        public void addToManagementFunction(String function, Double credits) {
            managementFunctionList.add(new TeacherCreditsInfoDTO(function, credits));
        }

        public void addToExemptionSituation(Set<PersonContractSituation> exemptionType, Double credits) {
            exemptionSituationList.add(new TeacherCreditsInfoDTO(exemptionType, credits));
        }

        public List<TeacherCreditsInfoDTO> getExemptionSituationList() {
            return exemptionSituationList;
        }
    }

    private final Map<String, TeacherDistributionServiceEntryDTO> teachersMap;

    public DistributionTeacherServicesByTeachersDTO() {
        teachersMap = new HashMap<String, TeacherDistributionServiceEntryDTO>();
    }

    public void addTeacher(String key, String teacherId, String category, String name, Double hours, Double accumulatedCredits) {
        TeacherDistributionServiceEntryDTO t =
                new TeacherDistributionServiceEntryDTO(key, teacherId, category, name, hours, accumulatedCredits);

        if (!teachersMap.containsKey(key)) {
            teachersMap.put(key, t);
        }
    }

    public void addExecutionCourseToTeacher(String keyTeacher, String executionCourseExternalId, String executionCourseName,
            Integer hours, Duration timeSpentByTeacher, Map<String, String> executionCourseDegreesNameSet,
            Map<String, Set<String>> curricularYearsSet, String periodName) {
        ExecutionCourseTeacherServiceDTO executionCourse =
                new ExecutionCourseTeacherServiceDTO(executionCourseExternalId, executionCourseName, hours, timeSpentByTeacher,
                        executionCourseDegreesNameSet, curricularYearsSet, periodName);

        teachersMap.get(keyTeacher).addExecutionCourse(executionCourse);

    }

    public void addHoursToTeacher(String keyTeacher, double hours) {
        TeacherDistributionServiceEntryDTO teacher = teachersMap.get(keyTeacher);

        if (teacher != null) {
            teacher.setTeacherRequiredHours(teacher.getTeacherRequiredHours() + hours);
        }
    }

    public boolean isTeacherPresent(String keyTeacher) {
        return teachersMap.containsKey((keyTeacher));
    }

    public Map<String, TeacherDistributionServiceEntryDTO> getTeachersMap() {
        return teachersMap;
    }

    public void addManagementFunctionToTeacher(String keyTeacher, String managementFunction, Double credits) {
        TeacherDistributionServiceEntryDTO teacher = teachersMap.get(keyTeacher);

        if (teacher != null) {
            teacher.addToManagementFunction(managementFunction, credits);
        }
    }

    public void addExemptionSituationToTeacher(String keyTeacher, Set<PersonContractSituation> exemptionTypes, Double credits) {
        TeacherDistributionServiceEntryDTO teacher = teachersMap.get(keyTeacher);

        if (teacher != null) {
            teacher.addToExemptionSituation(exemptionTypes, credits);
        }
    }
}
