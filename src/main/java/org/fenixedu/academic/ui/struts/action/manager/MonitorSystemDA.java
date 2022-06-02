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
 * Created on 2003/12/25
 * 
 */
package org.fenixedu.academic.ui.struts.action.manager;

import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.CompetenceCourse;
import org.fenixedu.academic.domain.CourseLoad;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.EnrolmentEvaluation;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Lesson;
import org.fenixedu.academic.domain.LessonInstance;
import org.fenixedu.academic.domain.OccupationPeriod;
import org.fenixedu.academic.domain.SchoolClass;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.degreeStructure.CourseGroup;
import org.fenixedu.academic.domain.degreeStructure.DegreeModule;
import org.fenixedu.academic.domain.degreeStructure.RootCourseGroup;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumLine;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumModule;
import org.fenixedu.academic.domain.studentCurriculum.RootCurriculumGroup;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.manager.ManagerApplications.ManagerSystemManagementApp;
import org.fenixedu.bennu.portal.servlet.PortalLayoutInjector;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.core.SharedIdentityMap;

/**
 * @author Luis Cruz
 */
@StrutsFunctionality(app = ManagerSystemManagementApp.class, path = "system-info", titleKey = "title.system.information")
@Mapping(module = "manager", path = "/monitorSystem")
@Forwards(@Forward(name = "Show", path = "/manager/monitorSystem_bd.jsp"))
public class MonitorSystemDA extends FenixDispatchAction {

    private static final Logger logger = LoggerFactory.getLogger(MonitorSystemDA.class);

    @EntryPoint
    public ActionForward monitor(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        request.setAttribute("properties", System.getProperties());

        request.setAttribute("startMillis", ""
                + ExecutionSemester.readActualExecutionSemester().getAcademicInterval().getStartMillis());
        request.setAttribute("endMillis", ""
                + ExecutionSemester.readActualExecutionSemester().getAcademicInterval().getEndMillis());
        request.setAttribute("chronology", ""
                + ExecutionSemester.readActualExecutionSemester().getAcademicInterval().getChronology().toString());
        request.setAttribute("cacheSize", SharedIdentityMap.getCache().size());

        return mapping.findForward("Show");
    }

    public ActionForward dumpThreadTrace(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Map<Thread, StackTraceElement[]> traces = Thread.getAllStackTraces();

        StringBuilder builder = new StringBuilder();

        for (Entry<Thread, StackTraceElement[]> trace : traces.entrySet()) {
            builder.append(trace.getKey());
            builder.append(":\n");
            for (StackTraceElement element : trace.getValue()) {
                builder.append("\t");
                builder.append(element);
                builder.append("\n");
            }
            builder.append("\n");
        }

        try (PrintWriter writer = response.getWriter()) {
            PortalLayoutInjector.skipLayoutOn(request);
            response.setContentType("text/plain");
            response.setStatus(HttpServletResponse.SC_OK);
            writer.write(builder.toString());
            writer.flush();
        }

        return null;
    }

    public ActionForward warmUpCacheForEnrolmentPeriodStart(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final ExecutionSemester ces = ExecutionSemester.readActualExecutionSemester();
        final ExecutionSemester pes = ces == null ? null : ces.getPreviousExecutionPeriod();

        if (ces != null && pes != null) {
            long s = System.currentTimeMillis();
            for (final ExecutionCourse executionCourse : ces.getAssociatedExecutionCoursesSet()) {
                executionCourse.getNameI18N().getContent();
                for (final CourseLoad courseLoad : executionCourse.getCourseLoadsSet()) {
                    courseLoad.getType();
                    for (final Shift shift : courseLoad.getShiftsSet()) {
                        shift.getNome();
                        for (final SchoolClass schoolClass : shift.getAssociatedClassesSet()) {
                            schoolClass.getNome();
                            final ExecutionDegree executionDegree = schoolClass.getExecutionDegree();
                            final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
                            degreeCurricularPlan.getName();
                            final Degree degree = degreeCurricularPlan.getDegree();
                            degree.getDegreeType();
                            final RootCourseGroup root = degreeCurricularPlan.getRoot();
                            load(root);
                        }
                        for (final Lesson lesson : shift.getAssociatedLessonsSet()) {
                            lesson.getBeginHourMinuteSecond();
                            for (OccupationPeriod period = lesson.getPeriod(); period != null; period = period.getNextPeriod()) {
                                period.getStartDate();
                            }
                            for (final LessonInstance lessonInstance : lesson.getLessonInstancesSet()) {
                                lessonInstance.getBeginDateTime();
                            }
                        }
                    }
                }
            }
            long e = System.currentTimeMillis();
            logger.info("Warming up cache for enrolment period. Load of current semester information took {}ms.", e - s);

            s = System.currentTimeMillis();
//            for (final RoomClassification roomClassification : rootDomainObject.getRoomClassificationSet()) {
//                for (final RoomInformation roomInformation : roomClassification.getRoomInformationsSet()) {
//                    roomInformation.getDescription();
//                    final Room room = roomInformation.getRoom();
//                    room.getNormalCapacity();
//                }
//            }
            e = System.currentTimeMillis();
            logger.info("Warming up cache for enrolment period. Load of room listing took {}ms.", e - s);

            final Set<Student> students = new HashSet<Student>();
            s = System.currentTimeMillis();
            for (final Enrolment enrolment : pes.getEnrolmentsSet()) {
                students.add(enrolment.getStudent());
            }
            e = System.currentTimeMillis();
            logger.info("Warming up cache for enrolment period. Search for students took {}ms.", e - s);

            s = System.currentTimeMillis();
            for (final Student student : students) {
                student.getNumber();
                for (final Registration registration : student.getRegistrationsSet()) {
                    registration.getNumber();
                    for (final StudentCurricularPlan studentCurricularPlan : registration.getStudentCurricularPlansSet()) {
                        final RootCurriculumGroup root = studentCurricularPlan.getRoot();
                        load(root);
                    }
                }
            }
            e = System.currentTimeMillis();
            logger.info("Warming up cache for enrolment period. Load of student curriculum took {}ms.", e - s);
        }

        return monitor(mapping, form, request, response);
    }

    private void load(final CurriculumModule curriculumModule) {
        if (curriculumModule != null) {
            curriculumModule.getCreationDateDateTime();
            final DegreeModule degreeModule = curriculumModule.getDegreeModule();
            if (degreeModule != null) {
                degreeModule.getName();
            }
            if (curriculumModule.isCurriculumLine()) {
                final CurriculumLine curriculumLine = (CurriculumLine) curriculumModule;
                if (curriculumLine.isEnrolment()) {
                    final Enrolment enrolment = (Enrolment) curriculumLine;
                    for (final EnrolmentEvaluation enrolmentEvaluation : enrolment.getEvaluationsSet()) {
                        enrolmentEvaluation.getGrade();
                    }
                }
            } else {
                final CurriculumGroup curriculumGroup = (CurriculumGroup) curriculumModule;
                for (final CurriculumModule child : curriculumGroup.getCurriculumModulesSet()) {
                    load(child);
                }
            }
        }
    }

    private void load(final DegreeModule degreeModule) {
        degreeModule.getName();
        if (degreeModule.isCourseGroup()) {
            final CourseGroup courseGroup = (CourseGroup) degreeModule;
            for (final org.fenixedu.academic.domain.degreeStructure.Context context : courseGroup.getChildContextsSet()) {
                final DegreeModule child = context.getChildDegreeModule();
                load(child);
            }
        } else {
            final CurricularCourse curricularCourse = (CurricularCourse) degreeModule;
            final CompetenceCourse competenceCourse = curricularCourse.getCompetenceCourse();
            if (competenceCourse != null) {
                competenceCourse.getName();
            }
        }
    }

}
