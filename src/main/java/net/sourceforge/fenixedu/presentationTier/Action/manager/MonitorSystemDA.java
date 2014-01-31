/*
 * Created on 2003/12/25
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CourseLoad;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.LessonInstance;
import net.sourceforge.fenixedu.domain.OccupationPeriod;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.degreeStructure.RootCourseGroup;
import net.sourceforge.fenixedu.domain.space.Room;
import net.sourceforge.fenixedu.domain.space.RoomClassification;
import net.sourceforge.fenixedu.domain.space.RoomInformation;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumLine;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;
import net.sourceforge.fenixedu.domain.studentCurriculum.RootCurriculumGroup;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.FenixConfigurationManager;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.core.SharedIdentityMap;

/**
 * @author Luis Cruz
 */
public class MonitorSystemDA extends FenixDispatchAction {

    private static final Logger logger = LoggerFactory.getLogger(MonitorSystemDA.class);

    public ActionForward monitor(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        request.setAttribute("properties", System.getProperties());

        request.setAttribute("useBarraAsAuth", FenixConfigurationManager.isBarraAsAuthenticationBroker());

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
            response.setContentType("text/plain");
            response.setStatus(HttpServletResponse.SC_OK);
            writer.write(builder.toString());
            writer.flush();
        }

        return null;
    }

    public ActionForward switchBarraAsAuthenticationBroker(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final boolean useBarraAsAuth = Boolean.parseBoolean(request.getParameter("useBarraAsAuth"));
        FenixConfigurationManager.setBarraAsAuthenticationBroker(useBarraAsAuth);

        return monitor(mapping, form, request, response);
    }

    public ActionForward warmUpCacheForEnrolmentPeriodStart(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final ExecutionSemester ces = ExecutionSemester.readActualExecutionSemester();
        final ExecutionSemester pes = ces == null ? null : ces.getPreviousExecutionPeriod();

        if (ces != null && pes != null) {
            long s = System.currentTimeMillis();
            for (final ExecutionCourse executionCourse : ces.getAssociatedExecutionCoursesSet()) {
                executionCourse.getName();
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
            for (final RoomClassification roomClassification : rootDomainObject.getRoomClassificationSet()) {
                for (final RoomInformation roomInformation : roomClassification.getRoomInformationsSet()) {
                    roomInformation.getDescription();
                    final Room room = roomInformation.getRoom();
                    room.getNormalCapacity();
                }
            }
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
            for (final net.sourceforge.fenixedu.domain.degreeStructure.Context context : courseGroup.getChildContextsSet()) {
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
