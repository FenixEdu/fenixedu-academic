/*
 * Created on 10/Fev/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.enrollment.shift;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourseWithExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoShiftWithInfoExecutionCourseAndInfoLessons;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.enrollment.shift.InfoShiftEnrollment;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IFrequentaPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

/**
 * @author Tânia Pousão
 * 
 * This class read and prepare all information usefull for shift enrollment use
 * case
 * 
 */
public class PrepareInfoShiftEnrollmentByStudentNumber extends Service {

    public Object run(Integer studentNumber, Integer executionDegreeIdChosen)
            throws FenixServiceException, ExcepcaoPersistencia {
        InfoShiftEnrollment infoShiftEnrollment = new InfoShiftEnrollment();

        // read student to enroll
        IPersistentStudent persistentStudent = persistentSupport.getIPersistentStudent();
        Student student = persistentStudent.readStudentByNumberAndDegreeType(studentNumber,
                DegreeType.DEGREE);
        if (student == null) {
            student = persistentStudent.readStudentByNumberAndDegreeType(studentNumber,
                    DegreeType.MASTER_DEGREE);
        }
        if (student == null) {
            throw new FenixServiceException("errors.impossible.operation");
        }

        if (student.getPayedTuition() == null || student.getPayedTuition().equals(Boolean.FALSE)) {
            if (student.getInterruptedStudies().equals(Boolean.FALSE))
                throw new FenixServiceException("error.exception.notAuthorized.student.warningTuition");
        }

        if (student.getFlunked() == null || student.getFlunked().equals(Boolean.TRUE)) {
            throw new FenixServiceException("error.exception.notAuthorized.student.warningTuition");
        }

        infoShiftEnrollment.setInfoStudent(InfoStudent.newInfoFromDomain(student));

        List attendingExecutionCourses = new ArrayList();
        // retrieve all courses that student is currently attended in
        infoShiftEnrollment.setInfoAttendingCourses(readAttendingCourses(persistentSupport, student.getNumber(),
                student.getDegreeType(), attendingExecutionCourses));

        // retrieve all shifts that student is currently enrollment
        // it will be shift associated with all or some attending courses
        infoShiftEnrollment.setInfoShiftEnrollment(readShiftEnrollment(persistentSupport, student, infoShiftEnrollment
                .getInfoAttendingCourses()));

        // calculate the number of courses that have shift enrollment
        infoShiftEnrollment.setNumberCourseWithShiftEnrollment(calculeNumberCoursesWithEnrollment(
                infoShiftEnrollment.getInfoAttendingCourses(), infoShiftEnrollment
                        .getInfoShiftEnrollment()));

        // calculate the number of courses in wich are shifts unenrolled
        infoShiftEnrollment.setNumberCourseUnenrolledShifts(calculeNumberCoursesUnenrolledShifts(
                attendingExecutionCourses, infoShiftEnrollment.getInfoShiftEnrollment()));

        final ExecutionPeriod executionPeriod = ExecutionPeriod.readActualExecutionPeriod();
        if (executionPeriod == null) {
            throw new FenixServiceException("errors.impossible.operation");
        }

        // read current execution year
        ExecutionYear executionYear = executionPeriod.getExecutionYear();
        if (executionYear == null) {
            throw new FenixServiceException("errors.impossible.operation");
        }

        // retrieve the list of existing degrees
        List infoExecutionDegreeList = readInfoExecutionDegrees(persistentSupport, executionYear);
        infoShiftEnrollment.setInfoExecutionDegreesList(infoExecutionDegreeList);

        // select the first execution degree or the execution degree of the
        // student logged
        InfoExecutionDegree infoExecutionDegree = selectExecutionDegree(persistentSupport, infoExecutionDegreeList,
                executionDegreeIdChosen, student);
        infoShiftEnrollment.setInfoExecutionDegree(infoExecutionDegree);

        // retrieve all courses pertaining to the selected degree
        infoShiftEnrollment.setInfoExecutionCoursesList(readInfoExecutionCoursesOfdegree(persistentSupport,
                executionPeriod, infoExecutionDegree));

        return infoShiftEnrollment;
    }

    private List readAttendingCourses(ISuportePersistente persistentSupport, Integer studentNumber,
            DegreeType tipoCurso, List attendingExecutionCourses) throws ExcepcaoPersistencia {
        List infoAttendingCourses = null;

        IFrequentaPersistente frequentaPersistente = persistentSupport.getIFrequentaPersistente();
        List attendList = frequentaPersistente.readByStudentNumber(studentNumber, tipoCurso);
        if (attendList != null && attendList.size() > 0) {
            infoAttendingCourses = new ArrayList();

            Iterator iterator = attendList.iterator();
            while (iterator.hasNext()) {
                Attends attend = (Attends) iterator.next();
                ExecutionCourse executionCourse = attend.getDisciplinaExecucao();
                if (executionCourse != null
                        && executionCourse.getExecutionPeriod() != null
                        && executionCourse.getExecutionPeriod().getState().equals(
                                new PeriodState(PeriodState.CURRENT))) {
                    infoAttendingCourses.add(InfoExecutionCourseWithExecutionPeriod
                            .newInfoFromDomain(executionCourse));
                    attendingExecutionCourses.add(executionCourse);
                }
            }

        }

        return infoAttendingCourses;
    }

    private List readShiftEnrollment(ISuportePersistente persistentSupport, Student student, List infoAttendingCourses)
            throws ExcepcaoPersistencia {
        List infoShiftEnrollment = null;

        if (infoAttendingCourses != null && infoAttendingCourses.size() > 0) {
            Integer executionPeriodIdInternal = ((InfoExecutionCourse) infoAttendingCourses.get(0))
                    .getInfoExecutionPeriod().getIdInternal();

            List<Shift> shifts = student.getShifts();
            List studentShifts = new ArrayList();
            for (Shift shift : shifts) {
                if (shift.getDisciplinaExecucao().getExecutionPeriod().getIdInternal().equals(
                        executionPeriodIdInternal))
                    studentShifts.add(shift);
            }

            infoShiftEnrollment = (List) CollectionUtils.collect(studentShifts, new Transformer() {

                public Object transform(Object input) {
                    Shift shift = (Shift) input;
                    InfoShift infoShift = InfoShiftWithInfoExecutionCourseAndInfoLessons
                            .newInfoFromDomain(shift);
                    return infoShift;
                }
            });
        }

        return infoShiftEnrollment;
    }

    private Integer calculeNumberCoursesWithEnrollment(List attendingList, List shiftEnrollmentList) {
        Integer result = null;

        if (attendingList == null) {
            result = new Integer(-1);

            return result;
        }

        List infoCoursesWithShiftEnrollment = new ArrayList();
        if (shiftEnrollmentList != null && shiftEnrollmentList.size() > 0) {
            ListIterator iterator = shiftEnrollmentList.listIterator();
            while (iterator.hasNext()) {
                InfoShift infoShift = (InfoShift) iterator.next();

                if (!infoCoursesWithShiftEnrollment.contains(infoShift.getInfoDisciplinaExecucao())) {

                    infoCoursesWithShiftEnrollment.add(infoShift.getInfoDisciplinaExecucao());
                }
            }
        }

        result = new Integer(attendingList.size() - infoCoursesWithShiftEnrollment.size());

        return result;
    }

    private List readInfoExecutionDegrees(ISuportePersistente persistentSupport, ExecutionYear executionYear)
            throws ExcepcaoPersistencia, FenixServiceException {
        IPersistentExecutionDegree presistentExecutionDegree = persistentSupport.getIPersistentExecutionDegree();
        List executionDegrees = presistentExecutionDegree.readByExecutionYearAndDegreeType(executionYear
                .getYear(), DegreeType.DEGREE);
        if (executionDegrees == null || executionDegrees.size() <= 0) {
            throw new FenixServiceException("errors.impossible.operation");
        }

        Iterator iterator = executionDegrees.iterator();
        List infoExecutionDegreeList = new ArrayList();
        while (iterator.hasNext()) {
            ExecutionDegree executionDegree = (ExecutionDegree) iterator.next();
            infoExecutionDegreeList.add(InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlan
                    .newInfoFromDomain(executionDegree));
        }

        return infoExecutionDegreeList;
    }

    private List readInfoExecutionCoursesOfdegree(ISuportePersistente persistentSupport,
            ExecutionPeriod executionPeriod, InfoExecutionDegree infoExecutionDegree)
            throws ExcepcaoPersistencia, FenixServiceException {
        IPersistentExecutionCourse persistentExecutionCourse = persistentSupport.getIPersistentExecutionCourse();
        ExecutionDegree executionDegree = (ExecutionDegree) persistentObject.readByOID(
                ExecutionDegree.class, infoExecutionDegree.getIdInternal());

        List executionCourseList = persistentExecutionCourse.readByExecutionPeriodAndExecutionDegree(
                executionPeriod.getIdInternal(), executionDegree.getDegreeCurricularPlan().getName(),
                executionDegree.getDegreeCurricularPlan().getDegree().getSigla());

        ListIterator listIterator = executionCourseList.listIterator();
        List infoExecutionCourseList = new ArrayList();
        while (listIterator.hasNext()) {
            ExecutionCourse executionCourse = (ExecutionCourse) listIterator.next();
            InfoExecutionCourse infoExecutionCourse = InfoExecutionCourse
                    .newInfoFromDomain(executionCourse);
            infoExecutionCourseList.add(infoExecutionCourse);
        }

        return infoExecutionCourseList;
    }

    /**
     * @param infoExecutionDegreeList
     * @return
     * @throws FenixServiceException 
     */
    private InfoExecutionDegree selectExecutionDegree(ISuportePersistente persistentSupport,
            List infoExecutionDegreeList, Integer executionDegreeIdChosen, Student student)
            throws ExcepcaoPersistencia, FenixServiceException {

        final ExecutionDegree executionDegreeChosen = (ExecutionDegree) persistentObject.readByOID(ExecutionDegree.class, executionDegreeIdChosen);
        if (executionDegreeChosen != null && executionDegreeChosen.getExecutionYear().getState().equals(PeriodState.CURRENT)) {
            return InfoExecutionDegree.newInfoFromDomain(executionDegreeChosen);
        }

        final StudentCurricularPlan studentCurricularPlan = student.getActiveStudentCurricularPlan();
        if (studentCurricularPlan == null) {
            throw new FenixServiceException("errors.impossible.operation");
        }
        final DegreeCurricularPlan degreeCurricularPlan = studentCurricularPlan.getDegreeCurricularPlan();
        for (final ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegrees()) {
            final ExecutionYear executionYear = executionDegree.getExecutionYear();
            if (executionYear.getState().equals(PeriodState.CURRENT)) {
                return InfoExecutionDegree.newInfoFromDomain(executionDegree);
            }
        }
        final Degree degree = degreeCurricularPlan.getDegree();
        for (final DegreeCurricularPlan otherDegreeCurricularPlan : degree.getDegreeCurricularPlans()) {
            for (final ExecutionDegree executionDegree : otherDegreeCurricularPlan.getExecutionDegrees()) {
                final ExecutionYear executionYear = executionDegree.getExecutionYear();
                if (executionYear.getState().equals(PeriodState.CURRENT)) {
                    return InfoExecutionDegree.newInfoFromDomain(executionDegree);
                }
            }
        }
        throw new FenixServiceException("errors.impossible.operation");
    }

    /**
     * @param infoAttendingCourses
     * @param infoShiftEnrollment
     * @return
     */
    private Integer calculeNumberCoursesUnenrolledShifts(List attendingExecutionCourses,
            List infoShiftEnrollment) {
        Set executionCoursesWithNonEnrolledShifts = new HashSet();
        for (Iterator iter = attendingExecutionCourses.iterator(); iter.hasNext();) {
            final ExecutionCourse executionCourse = (ExecutionCourse) iter.next();
            for (Iterator iterator = executionCourse.getAssociatedShifts().iterator(); iterator
                    .hasNext();) {
                final Shift shift = (Shift) iterator.next();
                if (!CollectionUtils.exists(infoShiftEnrollment, new Predicate() {

                    public boolean evaluate(Object arg0) {
                        InfoShift infoShift = (InfoShift) arg0;
                        return infoShift.getInfoDisciplinaExecucao().getIdInternal().equals(
                                executionCourse.getIdInternal())
                                && infoShift.getTipo().equals(shift.getTipo());
                    }
                })) {
                    executionCoursesWithNonEnrolledShifts.add(executionCourse);
                }
            }
        }
        return executionCoursesWithNonEnrolledShifts.size();
    }

}
