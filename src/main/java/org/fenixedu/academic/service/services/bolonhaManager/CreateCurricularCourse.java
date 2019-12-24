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
/*
 * Created on Dec 9, 2005
 */
package org.fenixedu.academic.service.services.bolonhaManager;

import org.fenixedu.academic.domain.CompetenceCourse;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.curricularPeriod.CurricularPeriod;
import org.fenixedu.academic.domain.degreeStructure.CourseGroup;
import org.fenixedu.academic.domain.degreeStructure.CurricularStage;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class CreateCurricularCourse {

    @Atomic
    public static void run(CreateCurricularCourseArgs curricularCourseArgs) throws FenixServiceException {

        DegreeCurricularPlan degreeCurricularPlan = readDegreeCurricularPlan(curricularCourseArgs);
        CourseGroup parentCourseGroup = readParentCourseGroup(curricularCourseArgs);
        CurricularPeriod curricularPeriod = readCurricularPeriod(curricularCourseArgs, degreeCurricularPlan);
        ExecutionInterval beginExecutionPeriod = readBeginExecutionPeriod(curricularCourseArgs);
        ExecutionInterval endExecutionPeriod = readEndExecutionPeriod(curricularCourseArgs);

        final CompetenceCourse competenceCourse = FenixFramework.getDomainObject(curricularCourseArgs.getCompetenceCourseID());
        if (competenceCourse == null) {
            throw new FenixServiceException("error.noCompetenceCourse");
        }

        // TODO this is not generic thinking... must find a way to abstract from
        // years/semesters
        if (competenceCourse.isAnual()) {
            degreeCurricularPlan.createCurricularPeriodFor(curricularCourseArgs.getYear(),
                    curricularCourseArgs.getSemester() + 1);
        }

        degreeCurricularPlan.createCurricularCourse(curricularCourseArgs.getWeight(), curricularCourseArgs.getPrerequisites(),
                curricularCourseArgs.getPrerequisitesEn(), CurricularStage.DRAFT, competenceCourse, parentCourseGroup,
                curricularPeriod, beginExecutionPeriod, endExecutionPeriod);
    }

    /**
     * For Optional Curricular Courses
     * 
     * @param curricularCourseArgs
     * @throws FenixServiceException
     */
    @Atomic
    public static void run(CreateOptionalCurricularCourseArgs curricularCourseArgs) throws FenixServiceException {

        DegreeCurricularPlan degreeCurricularPlan = readDegreeCurricularPlan(curricularCourseArgs);
        CourseGroup parentCourseGroup = readParentCourseGroup(curricularCourseArgs);
        CurricularPeriod curricularPeriod = readCurricularPeriod(curricularCourseArgs, degreeCurricularPlan);
        ExecutionInterval beginExecutionPeriod = readBeginExecutionPeriod(curricularCourseArgs);
        ExecutionInterval endExecutionPeriod = readEndExecutionPeriod(curricularCourseArgs);

        degreeCurricularPlan.createOptionalCurricularCourse(parentCourseGroup, curricularCourseArgs.getName(),
                curricularCourseArgs.getNameEn(), CurricularStage.DRAFT, curricularPeriod, beginExecutionPeriod,
                endExecutionPeriod);
    }

    private static DegreeCurricularPlan readDegreeCurricularPlan(CurricularCourseArgs curricularCourseArgs)
            throws FenixServiceException {
        DegreeCurricularPlan degreeCurricularPlan =
                FenixFramework.getDomainObject(curricularCourseArgs.getDegreeCurricularPlanID());
        if (degreeCurricularPlan == null) {
            throw new FenixServiceException("error.noDegreeCurricularPlan");
        }
        return degreeCurricularPlan;
    }

    private static CourseGroup readParentCourseGroup(CurricularCourseArgs curricularCourseArgs) throws FenixServiceException {
        CourseGroup parentCourseGroup =
                (CourseGroup) FenixFramework.getDomainObject(curricularCourseArgs.getParentCourseGroupID());
        if (parentCourseGroup == null) {
            throw new FenixServiceException("error.noCourseGroup");
        }
        return parentCourseGroup;
    }

    private static CurricularPeriod readCurricularPeriod(CurricularCourseArgs curricularCourseArgs,
            DegreeCurricularPlan degreeCurricularPlan) {
        // TODO this is not generic thinking... must find a way to abstract from
        // years/semesters
        CurricularPeriod curricularPeriod =
                degreeCurricularPlan.getCurricularPeriodFor(curricularCourseArgs.getYear(), curricularCourseArgs.getSemester());
        if (curricularPeriod == null) {
            curricularPeriod = degreeCurricularPlan.createCurricularPeriodFor(curricularCourseArgs.getYear(),
                    curricularCourseArgs.getSemester());
        }
        return curricularPeriod;
    }

    private static ExecutionInterval readBeginExecutionPeriod(CurricularCourseArgs curricularCourseArgs) {
        if (curricularCourseArgs.getBeginExecutionPeriodID() == null) {
            throw new DomainException("error.CreateCurricularCourse.beginExecutionPeriod.required");
        }

        return FenixFramework.getDomainObject(curricularCourseArgs.getBeginExecutionPeriodID());
    }

    private static ExecutionInterval readEndExecutionPeriod(CurricularCourseArgs curricularCourseArgs) {
        ExecutionInterval endExecutionPeriod;
        if (curricularCourseArgs.getEndExecutionPeriodID() == null) {
            endExecutionPeriod = null;
        } else {
            endExecutionPeriod = FenixFramework.getDomainObject(curricularCourseArgs.getEndExecutionPeriodID());
        }
        return endExecutionPeriod;
    }

    private abstract static class CurricularCourseArgs {
        private String degreeCurricularPlanID, parentCourseGroupID;

        private Integer year, semester;

        private String beginExecutionPeriodID, endExecutionPeriodID;

        public String getBeginExecutionPeriodID() {
            return beginExecutionPeriodID;
        }

        public void setBeginExecutionPeriodID(String beginExecutionPeriodID) {
            this.beginExecutionPeriodID = beginExecutionPeriodID;
        }

        public String getDegreeCurricularPlanID() {
            return degreeCurricularPlanID;
        }

        public void setDegreeCurricularPlanID(String degreeCurricularPlanID) {
            this.degreeCurricularPlanID = degreeCurricularPlanID;
        }

        public String getEndExecutionPeriodID() {
            return endExecutionPeriodID;
        }

        public void setEndExecutionPeriodID(String endExecutionPeriodID) {
            this.endExecutionPeriodID = endExecutionPeriodID;
        }

        public String getParentCourseGroupID() {
            return parentCourseGroupID;
        }

        public void setParentCourseGroupID(String parentCourseGroupID) {
            this.parentCourseGroupID = parentCourseGroupID;
        }

        public Integer getSemester() {
            return semester;
        }

        public void setSemester(Integer semester) {
            this.semester = semester;
        }

        public Integer getYear() {
            return year;
        }

        public void setYear(Integer year) {
            this.year = year;
        }
    }

    public static class CreateCurricularCourseArgs extends CurricularCourseArgs {
        private Double weight;

        private String prerequisites, prerequisitesEn;

        private String competenceCourseID;

        public CreateCurricularCourseArgs(Double weight, String prerequisites, String prerequisitesEn, String competenceCourseID,
                String parentCourseGroupID, Integer year, Integer semester, String degreeCurricularPlanID,
                String beginExecutionPeriodID, String endExecutionPeriodID) {

            setWeight(weight);
            setPrerequisites(prerequisites);
            setPrerequisitesEn(prerequisitesEn);
            setCompetenceCourseID(competenceCourseID);
            setParentCourseGroupID(parentCourseGroupID);
            setYear(year);
            setSemester(semester);
            setDegreeCurricularPlanID(degreeCurricularPlanID);
            setBeginExecutionPeriodID(beginExecutionPeriodID);
            setEndExecutionPeriodID(endExecutionPeriodID);
        }

        public String getCompetenceCourseID() {
            return competenceCourseID;
        }

        public void setCompetenceCourseID(String competenceCourseID) {
            this.competenceCourseID = competenceCourseID;
        }

        public String getPrerequisites() {
            return prerequisites;
        }

        public void setPrerequisites(String prerequisites) {
            this.prerequisites = prerequisites;
        }

        public String getPrerequisitesEn() {
            return prerequisitesEn;
        }

        public void setPrerequisitesEn(String prerequisitesEn) {
            this.prerequisitesEn = prerequisitesEn;
        }

        public Double getWeight() {
            return weight;
        }

        public void setWeight(Double weight) {
            this.weight = weight;
        }
    }

    public static class CreateOptionalCurricularCourseArgs extends CurricularCourseArgs {
        private String name, nameEn;

        public CreateOptionalCurricularCourseArgs(String degreeCurricularPlanID, String parentCourseGroupID, String name,
                String nameEn, Integer year, Integer semester, String beginExecutionPeriodID, String endExecutionPeriodID) {

            setDegreeCurricularPlanID(degreeCurricularPlanID);
            setParentCourseGroupID(parentCourseGroupID);
            setName(name);
            setNameEn(nameEn);
            setYear(year);
            setSemester(semester);
            setBeginExecutionPeriodID(beginExecutionPeriodID);
            setEndExecutionPeriodID(endExecutionPeriodID);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNameEn() {
            return nameEn;
        }

        public void setNameEn(String nameEn) {
            this.nameEn = nameEn;
        }
    }

}
