/*
 * Created on 10/Fev/2004
 *  
 */
package ServidorAplicacao.Servico.enrollment.shift;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionCourseWithExecutionPeriod;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlan;
import DataBeans.InfoShift;
import DataBeans.InfoShiftWithInfoExecutionCourseAndInfoLessons;
import DataBeans.InfoStudent;
import DataBeans.enrollment.shift.InfoShiftEnrollment;
import Dominio.CursoExecucao;
import Dominio.ExecutionPeriod;
import Dominio.Frequenta;
import Dominio.ICursoExecucao;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.IFrequenta;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.ITurnoAluno;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentExecutionDegree;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IPersistentStudentCurricularPlan;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoAlunoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.PeriodState;
import Util.TipoCurso;

/**
 * @author Tânia Pousão
 * 
 * This class read and prepare all information usefull for shift enrollment use
 * case
 *  
 */
public class PrepareInfoShiftEnrollmentByStudentNumber implements IService {

    public PrepareInfoShiftEnrollmentByStudentNumber() {
    }

    public Object run(Integer studentNumber, Integer executionDegreeIdChosen)
            throws FenixServiceException {
        InfoShiftEnrollment infoShiftEnrollment = new InfoShiftEnrollment();
        ISuportePersistente sp = null;

        try {
            sp = SuportePersistenteOJB.getInstance();

            //read student to enroll
            IPersistentStudent persistentStudent = sp.getIPersistentStudent();
            IStudent student = persistentStudent.readStudentByNumberAndDegreeType(studentNumber,
                    TipoCurso.LICENCIATURA_OBJ);
            if (student == null) {
                student = persistentStudent.readStudentByNumberAndDegreeType(studentNumber,
                        TipoCurso.MESTRADO_OBJ);
            }
            if (student == null) {
                throw new FenixServiceException("errors.impossible.operation");
            }

            if (student.getPayedTuition() == null || student.getPayedTuition().equals(Boolean.FALSE)) {
                throw new FenixServiceException("error.exception.notAuthorized.student.warningTuition");
            }

            infoShiftEnrollment.setInfoStudent(InfoStudent.newInfoFromDomain(student));

            //retrieve all courses that student is currently attended in
            infoShiftEnrollment.setInfoAttendingCourses(readAttendingCourses(sp, student.getNumber(), student.getDegreeType()));

            //retrieve all shifts that student is currently enrollment
            //it will be shift associated with all or some attending courses
            infoShiftEnrollment.setInfoShiftEnrollment(readShiftEnrollment(sp, student,
                    infoShiftEnrollment.getInfoAttendingCourses()));

            //calculate the number of courses that have shift enrollment
            infoShiftEnrollment.setNumberCourseWithShiftEnrollment(calculeNumberCoursesWithEnrollment(
                    infoShiftEnrollment.getInfoAttendingCourses(), infoShiftEnrollment
                            .getInfoShiftEnrollment()));

            //read current execution period
            IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();
            IExecutionPeriod executionPeriod = persistentExecutionPeriod.readActualExecutionPeriod();
            if (executionPeriod == null) {
                throw new FenixServiceException("errors.impossible.operation");
            }

            //read current execution year
            IExecutionYear executionYear = executionPeriod.getExecutionYear();
            if (executionYear == null) {
                throw new FenixServiceException("errors.impossible.operation");
            }

            //retrieve the list of existing degrees
            List infoExecutionDegreeList = readInfoExecutionDegrees(sp, executionYear);
            infoShiftEnrollment.setInfoExecutionDegreesList(infoExecutionDegreeList);

            //select the first execution degree or the execution degree of the
            // student logged
            InfoExecutionDegree infoExecutionDegree = selectExecutionDegree(sp, infoExecutionDegreeList,
                    executionDegreeIdChosen, student);
            infoShiftEnrollment.setInfoExecutionDegree(infoExecutionDegree);

            //retrieve all courses pertaining to the selected degree
            infoShiftEnrollment.setInfoExecutionCoursesList(readInfoExecutionCoursesOfdegree(sp,
                    executionPeriod, infoExecutionDegree));

        } catch (ExcepcaoPersistencia excepcaoPersistencia) {

            throw new FenixServiceException(excepcaoPersistencia);
        }

        return infoShiftEnrollment;
    }

    private List readAttendingCourses(ISuportePersistente sp, Integer studentNumber, TipoCurso tipoCurso)
            throws ExcepcaoPersistencia {
        List infoAttendingCourses = null;

        IFrequentaPersistente frequentaPersistente = sp.getIFrequentaPersistente();
        List attendList = frequentaPersistente.readByStudentNumber(studentNumber, tipoCurso);
        if (attendList != null && attendList.size() > 0) {
            infoAttendingCourses = new ArrayList();

            Iterator iterator = attendList.iterator();
            while (iterator.hasNext()) {
                IFrequenta attend = (Frequenta) iterator.next();
                IExecutionCourse executionCourse = attend.getDisciplinaExecucao();
                if (executionCourse != null
                        && executionCourse.getExecutionPeriod() != null
                        && executionCourse.getExecutionPeriod().getState().equals(
                                new PeriodState(PeriodState.CURRENT))) {
                    infoAttendingCourses.add(InfoExecutionCourseWithExecutionPeriod
                            .newInfoFromDomain(executionCourse));
                }
            }

        }

        return infoAttendingCourses;
    }

    private List readShiftEnrollment(ISuportePersistente sp, IStudent student, List infoAttendingCourses)
            throws ExcepcaoPersistencia {
        List infoShiftEnrollment = null;

        if (infoAttendingCourses != null && infoAttendingCourses.size() > 0) {
            IExecutionPeriod executionPeriod = new ExecutionPeriod();
            executionPeriod.setIdInternal(((InfoExecutionCourse) infoAttendingCourses.get(0))
                    .getInfoExecutionPeriod().getIdInternal());

            ITurnoAlunoPersistente persistentShiftStudent = sp.getITurnoAlunoPersistente();
            List studentShifts = persistentShiftStudent.readByStudentAndExecutionPeriod(student,
                    executionPeriod);

            infoShiftEnrollment = (List) CollectionUtils.collect(studentShifts, new Transformer() {

                public Object transform(Object input) {
                    ITurnoAluno shiftStudent = (ITurnoAluno) input;
                    InfoShift infoShift = InfoShiftWithInfoExecutionCourseAndInfoLessons
                            .newInfoFromDomain(shiftStudent.getShift());
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

    private List readInfoExecutionDegrees(ISuportePersistente sp, IExecutionYear executionYear)
            throws ExcepcaoPersistencia, FenixServiceException {
        IPersistentExecutionDegree presistentExecutionDegree = sp.getIPersistentExecutionDegree();
        List executionDegrees = presistentExecutionDegree.readByExecutionYearAndDegreeType(
                executionYear, TipoCurso.LICENCIATURA_OBJ);
        if (executionDegrees == null || executionDegrees.size() <= 0) {
            throw new FenixServiceException("errors.impossible.operation");
        }

        Iterator iterator = executionDegrees.iterator();
        List infoExecutionDegreeList = new ArrayList();
        while (iterator.hasNext()) {
            ICursoExecucao executionDegree = (ICursoExecucao) iterator.next();
            infoExecutionDegreeList.add(InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlan
                    .newInfoFromDomain(executionDegree));
        }

        return infoExecutionDegreeList;
    }

    private List readInfoExecutionCoursesOfdegree(ISuportePersistente sp,
            IExecutionPeriod executionPeriod, InfoExecutionDegree infoExecutionDegree)
            throws ExcepcaoPersistencia, FenixServiceException {
        IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();
        IPersistentExecutionDegree persistentExecutionDegree = sp.getIPersistentExecutionDegree();
        ICursoExecucao executionDegree = (ICursoExecucao) persistentExecutionDegree.readByOID(
                CursoExecucao.class, infoExecutionDegree.getIdInternal());

        List executionCourseList = persistentExecutionCourse.readByExecutionPeriodAndExecutionDegree(
                executionPeriod, executionDegree);
        if (executionCourseList == null || executionCourseList.size() <= 0) {
            throw new FenixServiceException("errors.impossible.operation");
        }

        ListIterator listIterator = executionCourseList.listIterator();
        List infoExecutionCourseList = new ArrayList();
        while (listIterator.hasNext()) {
            IExecutionCourse executionCourse = (IExecutionCourse) listIterator.next();
            InfoExecutionCourse infoExecutionCourse = InfoExecutionCourse
                    .newInfoFromDomain(executionCourse);
            infoExecutionCourseList.add(infoExecutionCourse);
        }

        return infoExecutionCourseList;
    }

    /**
     * @param infoExecutionDegreeList
     * @return
     */
    private InfoExecutionDegree selectExecutionDegree(ISuportePersistente sp,
            List infoExecutionDegreeList, Integer executionDegreeIdChosen, IStudent student)
            throws ExcepcaoPersistencia {
        ICursoExecucao executionDegree = null;

        //read the execution degree chosen
        if (executionDegreeIdChosen != null) {
            IPersistentExecutionDegree persistentExecutionDegree = sp.getIPersistentExecutionDegree();

            executionDegree = (ICursoExecucao) persistentExecutionDegree.readByOID(CursoExecucao.class,
                    executionDegreeIdChosen);
            if (executionDegree != null) {
                return InfoExecutionDegree.newInfoFromDomain(executionDegree);
            }
        }

        IPersistentStudentCurricularPlan persistentCurricularPlan = sp
                .getIStudentCurricularPlanPersistente();
        IStudentCurricularPlan studentCurricularPlan = persistentCurricularPlan
                .readActiveByStudentNumberAndDegreeType(student.getNumber(), TipoCurso.LICENCIATURA_OBJ);
        if (studentCurricularPlan == null || studentCurricularPlan.getDegreeCurricularPlan() == null
                || studentCurricularPlan.getDegreeCurricularPlan().getDegree() == null
                || studentCurricularPlan.getDegreeCurricularPlan().getDegree().getNome() == null) {

            return (InfoExecutionDegree) infoExecutionDegreeList.get(0);
        }

        final Integer degreeCode = studentCurricularPlan.getDegreeCurricularPlan().getDegree()
                .getIdInternal();
        List infoExecutionDegreeListWithDegreeCode = (List) CollectionUtils.select(
                infoExecutionDegreeList, new Predicate() {

                    public boolean evaluate(Object input) {
                        InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) input;
                        return infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree()
                                .getIdInternal().equals(degreeCode);
                    }
                });
        if (!infoExecutionDegreeListWithDegreeCode.isEmpty()) {
            return (InfoExecutionDegree) infoExecutionDegreeListWithDegreeCode.get(0);
        }
        return (InfoExecutionDegree) infoExecutionDegreeList.get(0);

    }

}