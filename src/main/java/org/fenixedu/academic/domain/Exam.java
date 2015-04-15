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
package org.fenixedu.academic.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.space.SpaceUtils;
import org.fenixedu.academic.domain.space.WrittenEvaluationSpaceOccupation;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.util.icalendar.EvaluationEventBean;
import org.fenixedu.academic.dto.InfoEvaluation;
import org.fenixedu.academic.dto.InfoExam;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.DateFormatUtil;
import org.fenixedu.academic.util.Season;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.spaces.domain.Space;

public class Exam extends Exam_Base {

    public Exam(Date examDay, Date examStartTime, Date examEndTime, List<ExecutionCourse> executionCoursesToAssociate,
            List<DegreeModuleScope> curricularCourseScopesToAssociate, List<Space> rooms, GradeScale gradeScale, Season season) {

        super();
        checkScopeAndSeasonConstrains(executionCoursesToAssociate, curricularCourseScopesToAssociate, season);

        setAttributesAndAssociateRooms(examDay, examStartTime, examEndTime, executionCoursesToAssociate,
                curricularCourseScopesToAssociate, rooms);

        this.setSeason(season);
        if (gradeScale == null) {
            this.setGradeScale(GradeScale.TYPE20);
        } else {
            this.setGradeScale(gradeScale);
        }
        checkIntervalBetweenEvaluations();
        logCreate();
    }

    public void edit(Date examDay, Date examStartTime, Date examEndTime, List<ExecutionCourse> executionCoursesToAssociate,
            List<DegreeModuleScope> curricularCourseScopesToAssociate, List<Space> rooms, GradeScale gradeScale, Season season) {

        // It's necessary to remove this associations before check some
        // constrains
        this.getAssociatedExecutionCoursesSet().clear();
        this.getAssociatedCurricularCourseScopeSet().clear();
        this.getAssociatedContextsSet().clear();

        checkScopeAndSeasonConstrains(executionCoursesToAssociate, curricularCourseScopesToAssociate, season);

        super.edit(examDay, examStartTime, examEndTime, executionCoursesToAssociate, curricularCourseScopesToAssociate, rooms,
                gradeScale);
        this.setSeason(season);
        checkIntervalBetweenEvaluations();
        logEdit();
    }

    private boolean checkScopeAndSeasonConstrains(List<ExecutionCourse> executionCoursesToAssociate,
            List<DegreeModuleScope> curricularCourseScopesToAssociate, Season season) {

        // for each execution course, there must not exist an exam for the same
        // season and scope

        for (ExecutionCourse executionCourse : executionCoursesToAssociate) {
            for (Evaluation evaluation : executionCourse.getAssociatedEvaluationsSet()) {
                if (evaluation instanceof Exam) {
                    Exam existingExam = (Exam) evaluation;
                    if (existingExam.getSeason().equals(season)) {
                        // is necessary to confirm if is for the same scope
                        for (DegreeModuleScope scope : existingExam.getDegreeModuleScopes()) {
                            if (curricularCourseScopesToAssociate.contains(scope)) {
                                throw new DomainException("error.existingExam");
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    public boolean isExamsMapPublished() {
        for (final ExecutionCourse executionCourse : getAssociatedExecutionCoursesSet()) {
            for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCoursesSet()) {
                final DegreeCurricularPlan degreeCurricularPlan = curricularCourse.getDegreeCurricularPlan();
                for (final ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegreesSet()) {
                    if (executionCourse.getExecutionPeriod().getExecutionYear() == executionDegree.getExecutionYear()
                            && (executionDegree.isPublishedExam(executionCourse.getExecutionPeriod()))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean getIsExamsMapPublished() {
        return isExamsMapPublished();
    }

    public static List<Exam> getAllByRoomAndExecutionPeriod(String room, String executionPeriod, String year) {
        List<Exam> result = new ArrayList<Exam>();

        outter: for (Exam exam : Exam.readExams()) {
            for (WrittenEvaluationSpaceOccupation occupation : exam.getWrittenEvaluationSpaceOccupationsSet()) {
                if (!(occupation.getRoom()).getName().equals(room)) {
                    continue outter;
                }
            }

            for (ExecutionCourse course : exam.getAssociatedExecutionCoursesSet()) {
                if (!course.getExecutionPeriod().getName().equals(executionPeriod)) {
                    continue outter;
                }

                if (!course.getExecutionPeriod().getExecutionYear().getYear().equals(year)) {
                    continue outter;
                }
            }

            result.add(exam);
        }

        return result;
    }

    public static List<Exam> getAllByDate(Calendar examDay, Calendar examStartTime) {
        return getAllByDate(examDay, examStartTime, null);
    }

    public static List<Exam> getAllByDate(Calendar examDay, Calendar examStartTime, Calendar examEndTime) {
        List<Exam> result = new ArrayList<Exam>();

        outter: for (Exam exam : Exam.readExams()) {
            if (!DateFormatUtil.equalDates("dd/MM/yyyy", examDay.getTime(), exam.getDayDate())) {
                continue;
            }

            if (examStartTime != null && !DateFormatUtil.equalDates("HH:mm", examStartTime.getTime(), exam.getBeginningDate())) {
                continue;
            }

            if (examEndTime != null && !DateFormatUtil.equalDates("HH:mm", examEndTime.getTime(), exam.getEndDate())) {
                continue;
            }

            result.add(exam);
        }

        return result;
    }

    public static List<Exam> readExams() {
        List<Exam> result = new ArrayList<Exam>();

        for (Evaluation evaluation : Bennu.getInstance().getEvaluationsSet()) {
            if (evaluation instanceof Exam) {
                result.add((Exam) evaluation);
            }
        }
        return result;
    }

    @Override
    public boolean isExam() {
        return true;
    }

    public boolean isForSeason(final Season season) {
        return this.getSeason().equals(season);
    }

    public boolean isSpecialSeason() {
        return isForSeason(Season.SPECIAL_SEASON_OBJ);
    }

    @Override
    public boolean canBeAssociatedToRoom(Space room) {
        return SpaceUtils.isFree(room, getBeginningDateTime().toYearMonthDay(), getEndDateTime().toYearMonthDay(),
                getBeginningDateHourMinuteSecond(), getEndDateHourMinuteSecond(), getDayOfWeek(), null, null, null);
    }

    @Override
    public List<EvaluationEventBean> getAllEvents(Registration registration) {
        return getAllEvents("Exame (" + this.getSeason() + ")", registration);
    }

    @Override
    public String getPresentationName() {
        return BundleUtil.getString(Bundle.APPLICATION, "label.exam") + " "
                + BundleUtil.getString(Bundle.APPLICATION, getSeason().getKey());
    }

    @Override
    public InfoEvaluation newInfoFromDomain() {
        return InfoExam.newInfoFromDomain(this);
    }
}
