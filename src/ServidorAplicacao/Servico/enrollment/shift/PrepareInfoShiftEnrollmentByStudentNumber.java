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
import DataBeans.InfoClass;
import DataBeans.InfoDegree;
import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import DataBeans.InfoLesson;
import DataBeans.InfoRoom;
import DataBeans.InfoShift;
import DataBeans.InfoStudent;
import DataBeans.enrollment.shift.InfoShiftEnrollment;
import Dominio.CursoExecucao;
import Dominio.ExecutionPeriod;
import Dominio.Frequenta;
import Dominio.IAula;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.IFrequenta;
import Dominio.ISala;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.ITurma;
import Dominio.ITurno;
import Dominio.ITurnoAluno;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IStudentCurricularPlanPersistente;
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
            IStudent student = persistentStudent
                    .readStudentByNumberAndDegreeType(studentNumber,
                            TipoCurso.LICENCIATURA_OBJ);
            if (student == null) {
                student = persistentStudent.readStudentByNumberAndDegreeType(
                        studentNumber, TipoCurso.MESTRADO_OBJ);
            }
            if (student == null) { throw new FenixServiceException(
                    "errors.impossible.operation"); }
            infoShiftEnrollment
                    .setInfoStudent(copyIStudent2InfoStudent(student));

            //retrieve all courses that student is currently attended in
            infoShiftEnrollment.setInfoAttendingCourses(readAttendingCourses(
                    sp, student.getNumber()));

            //retrieve all shifts that student is currently enrollment
            //it will be shift associated with all or some attending courses
            infoShiftEnrollment.setInfoShiftEnrollment(readShiftEnrollment(sp,
                    student, infoShiftEnrollment.getInfoAttendingCourses()));

            //calculate the number of courses that have shift enrollment
            infoShiftEnrollment
                    .setNumberCourseWithShiftEnrollment(calculeNumberCoursesWithEnrollment(
                            infoShiftEnrollment.getInfoAttendingCourses(),
                            infoShiftEnrollment.getInfoShiftEnrollment()));

            //read current execution period
            IPersistentExecutionPeriod persistentExecutionPeriod = sp
                    .getIPersistentExecutionPeriod();
            IExecutionPeriod executionPeriod = persistentExecutionPeriod
                    .readActualExecutionPeriod();
            if (executionPeriod == null) { throw new FenixServiceException(
                    "errors.impossible.operation"); }

            //read current execution year
            IExecutionYear executionYear = executionPeriod.getExecutionYear();
            if (executionYear == null) { throw new FenixServiceException(
                    "errors.impossible.operation"); }

            //retrieve the list of existing degrees
            List infoExecutionDegreeList = readInfoExecutionDegrees(sp,
                    executionYear);
            infoShiftEnrollment
                    .setInfoExecutionDegreesList(infoExecutionDegreeList);

            //select the first execution degree or the execution degree of the
            // student logged
            InfoExecutionDegree infoExecutionDegree = selectExecutionDegree(sp,
                    infoExecutionDegreeList, executionDegreeIdChosen, student);
            infoShiftEnrollment.setInfoExecutionDegree(infoExecutionDegree);

            //retrieve all courses pertaining to the selected degree
            infoShiftEnrollment
                    .setInfoExecutionCoursesList(readInfoExecutionCoursesOfdegree(
                            sp, executionPeriod, infoExecutionDegree));

        } catch (ExcepcaoPersistencia excepcaoPersistencia) {

            throw new FenixServiceException(excepcaoPersistencia);
        }

        return infoShiftEnrollment;
    }

    /**
     * @param executionDegree
     * @return
     */
    private InfoExecutionDegree copyIExecutionDegree2InfoExecutionDegree(
            ICursoExecucao executionDegree) {
        InfoExecutionDegree infoExecutionDegree = null;
        if (executionDegree != null) {
            infoExecutionDegree = new InfoExecutionDegree();
            infoExecutionDegree.setIdInternal(executionDegree.getIdInternal());
            infoExecutionDegree
                    .setInfoExecutionYear(copyIExecutionYear2InfoExecutionYear(executionDegree
                            .getExecutionYear()));
            infoExecutionDegree
                    .setInfoDegreeCurricularPlan(copyIDegreeCurricularPlan2InfoDegreeCurricularPlan(executionDegree
                            .getCurricularPlan()));
        }
        return infoExecutionDegree;
    }

    /**
     * @param plan
     * @return
     */
    private InfoDegreeCurricularPlan copyIDegreeCurricularPlan2InfoDegreeCurricularPlan(
            IDegreeCurricularPlan plan) {
        InfoDegreeCurricularPlan infoDegreeCurricularPlan = null;
        if (plan != null) {
            infoDegreeCurricularPlan = new InfoDegreeCurricularPlan();
            infoDegreeCurricularPlan.setIdInternal(plan.getIdInternal());
            infoDegreeCurricularPlan.setName(plan.getName());
            infoDegreeCurricularPlan.setInfoDegree(copyIDegree2InfoDegree(plan
                    .getDegree()));
        }
        return infoDegreeCurricularPlan;
    }

    /**
     * @param degree
     * @return
     */
    private InfoDegree copyIDegree2InfoDegree(ICurso degree) {
        InfoDegree infoDegree = null;
        if (degree != null) {
            infoDegree = new InfoDegree();
            infoDegree.setIdInternal(degree.getIdInternal());
            infoDegree.setNome(degree.getNome());
            infoDegree.setSigla(degree.getSigla());
            infoDegree.setTipoCurso(degree.getTipoCurso());
        }
        return infoDegree;
    }

    /**
     * @param student
     * @return
     */
    private InfoStudent copyIStudent2InfoStudent(IStudent student) {
        InfoStudent infoStudent = null;
        if (student != null) {
            infoStudent = new InfoStudent();
            infoStudent.setIdInternal(student.getIdInternal());

        }
        return infoStudent;
    }

    private List readAttendingCourses(ISuportePersistente sp,
            Integer studentNumber) throws ExcepcaoPersistencia {
        List infoAttendingCourses = null;

        IFrequentaPersistente frequentaPersistente = sp
                .getIFrequentaPersistente();
        List attendList = frequentaPersistente
                .readByStudentNumber(studentNumber);
        if (attendList != null && attendList.size() > 0) {
            infoAttendingCourses = new ArrayList();

            Iterator iterator = attendList.iterator();
            while (iterator.hasNext()) {
                IFrequenta attend = (Frequenta) iterator.next();
                IExecutionCourse executionCourse = attend
                        .getDisciplinaExecucao();
                if (executionCourse != null
                        && executionCourse.getExecutionPeriod() != null
                        && executionCourse.getExecutionPeriod().getState()
                                .equals(new PeriodState(PeriodState.CURRENT))) {
                    infoAttendingCourses
                            .add(copyIExecutionCourse2InfoExecutionCourse(executionCourse));
                }
            }

        }

        return infoAttendingCourses;
    }

    /**
     * @param executionCourse
     * @return
     */
    private InfoExecutionCourse copyIExecutionCourse2InfoExecutionCourse(
            IExecutionCourse executionCourse) {
        InfoExecutionCourse infoExecutionCourse = null;
        if (executionCourse != null) {
            infoExecutionCourse = new InfoExecutionCourse();
            infoExecutionCourse.setIdInternal(executionCourse.getIdInternal());
            infoExecutionCourse.setNome(executionCourse.getNome());
            infoExecutionCourse.setSigla(executionCourse.getSigla());
            infoExecutionCourse
                    .setInfoExecutionPeriod(copyIExecutionPeriod2InfoExecutionPeriod(executionCourse
                            .getExecutionPeriod()));
        }
        return infoExecutionCourse;
    }

    /**
     * @param period
     * @return
     */
    private InfoExecutionPeriod copyIExecutionPeriod2InfoExecutionPeriod(
            IExecutionPeriod period) {
        InfoExecutionPeriod infoExecutionPeriod = null;
        if (period != null) {
            infoExecutionPeriod = new InfoExecutionPeriod();
            infoExecutionPeriod.setIdInternal(period.getIdInternal());
            infoExecutionPeriod.setName(period.getName());
            infoExecutionPeriod
                    .setInfoExecutionYear(copyIExecutionYear2InfoExecutionYear(period
                            .getExecutionYear()));
        }
        return infoExecutionPeriod;
    }

    /**
     * @param year
     * @return
     */
    private InfoExecutionYear copyIExecutionYear2InfoExecutionYear(
            IExecutionYear year) {
        InfoExecutionYear infoExecutionYear = null;
        if (year != null) {
            infoExecutionYear = new InfoExecutionYear();
            infoExecutionYear.setIdInternal(year.getIdInternal());
            infoExecutionYear.setYear(year.getYear());
        }
        return infoExecutionYear;
    }

    private List readShiftEnrollment(ISuportePersistente sp, IStudent student,
            List infoAttendingCourses) throws ExcepcaoPersistencia {
        List infoShiftEnrollment = null;

        if (infoAttendingCourses != null && infoAttendingCourses.size() > 0) {
            IExecutionPeriod executionPeriod = new ExecutionPeriod();
            executionPeriod
                    .setIdInternal(((InfoExecutionCourse) infoAttendingCourses
                            .get(0)).getInfoExecutionPeriod().getIdInternal());

            ITurnoAlunoPersistente persistentShiftStudent = sp
                    .getITurnoAlunoPersistente();
            List studentShifts = persistentShiftStudent
                    .readByStudentAndExecutionPeriod(student, executionPeriod);

            infoShiftEnrollment = (List) CollectionUtils.collect(studentShifts,
                    new Transformer() {

                        public Object transform(Object input) {
                            ITurnoAluno shiftStudent = (ITurnoAluno) input;
                            InfoShift infoShift = copyShift2InfoShift(shiftStudent
                                    .getShift());
                            return infoShift;
                        }

                    });
        }

        return infoShiftEnrollment;
    }

    private InfoShift copyShift2InfoShift(ITurno shift) {
        InfoShift infoShift = null;
        if (shift != null) {
            infoShift = new InfoShift();
            infoShift.setIdInternal(shift.getIdInternal());
            infoShift.setNome(shift.getNome());
            infoShift
                    .setInfoDisciplinaExecucao(copyIExecutionCourse2InfoExecutionCourse(shift
                            .getDisciplinaExecucao()));
            infoShift.setInfoLessons(copyILessons2InfoLessons(shift
                    .getAssociatedLessons()));
            infoShift.setTipo(shift.getTipo());
            infoShift.setLotacao(shift.getLotacao());
            infoShift.setOcupation(shift.getOcupation());
            infoShift.setPercentage(shift.getPercentage());
            infoShift.setInfoClasses(copyIClasses2InfoClasses(shift
                    .getAssociatedClasses()));
        }
        return infoShift;
    }

    /**
     * @param list
     * @return
     */
    private List copyIClasses2InfoClasses(List list) {
        List infoClasses = null;
        if (list != null) {
            infoClasses = (List) CollectionUtils.collect(list,
                    new Transformer() {

                        public Object transform(Object arg0) {

                            return copyIClass2InfoClass((ITurma) arg0);
                        }

                    });
        }
        return infoClasses;
    }

    private InfoClass copyIClass2InfoClass(ITurma turma) {
        InfoClass infoClass = null;
        if (turma != null) {
            infoClass = new InfoClass();
            infoClass.setIdInternal(turma.getIdInternal());
            infoClass.setNome(turma.getNome());
            infoClass
                    .setInfoExecutionDegree(copyIExecutionDegree2InfoExecutionDegree(turma
                            .getExecutionDegree()));

        }
        return infoClass;
    }

    /**
     * @param list
     * @return
     */
    private List copyILessons2InfoLessons(List list) {
        List infoLessons = null;
        if (list != null) {
            infoLessons = (List) CollectionUtils.collect(list,
                    new Transformer() {

                        public Object transform(Object arg0) {

                            return copyILesson2InfoLesson((IAula) arg0);
                        }

                    });
        }
        return infoLessons;
    }

    private Object copyILesson2InfoLesson(IAula lesson) {
        InfoLesson infoLesson = null;
        if (lesson != null) {
            infoLesson = new InfoLesson();
            infoLesson.setIdInternal(lesson.getIdInternal());
            infoLesson.setDiaSemana(lesson.getDiaSemana());
            infoLesson.setFim(lesson.getFim());
            infoLesson.setInicio(lesson.getInicio());
            infoLesson.setTipo(lesson.getTipo());
            infoLesson.setInfoSala(copyISala2InfoRoom(lesson.getSala()));
        }
        return infoLesson;
    }

    /**
     * @param sala
     * @return
     */
    private InfoRoom copyISala2InfoRoom(ISala sala) {
        InfoRoom infoRoom = null;
        if (sala != null) {
            infoRoom = new InfoRoom();
            infoRoom.setIdInternal(sala.getIdInternal());
            infoRoom.setNome(sala.getNome());
        }
        return infoRoom;
    }

    private Integer calculeNumberCoursesWithEnrollment(List attendingList,
            List shiftEnrollmentList) {
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

                if (!infoCoursesWithShiftEnrollment.contains(infoShift
                        .getInfoDisciplinaExecucao())) {

                    infoCoursesWithShiftEnrollment.add(infoShift
                            .getInfoDisciplinaExecucao());
                }
            }
        }

        result = new Integer(attendingList.size()
                - infoCoursesWithShiftEnrollment.size());

        return result;
    }

    private List readInfoExecutionDegrees(ISuportePersistente sp,
            IExecutionYear executionYear) throws ExcepcaoPersistencia,
            FenixServiceException {
        ICursoExecucaoPersistente presistentExecutionDegree = sp
                .getICursoExecucaoPersistente();
        List executionDegrees = presistentExecutionDegree
                .readByExecutionYearAndDegreeType(executionYear,
                        TipoCurso.LICENCIATURA_OBJ);
        if (executionDegrees == null || executionDegrees.size() <= 0) { throw new FenixServiceException(
                "errors.impossible.operation"); }

        Iterator iterator = executionDegrees.iterator();
        List infoExecutionDegreeList = new ArrayList();
        while (iterator.hasNext()) {
            ICursoExecucao executionDegree = (ICursoExecucao) iterator.next();
            infoExecutionDegreeList
                    .add(copyIExecutionDegree2InfoExecutionDegree(executionDegree));
        }

        return infoExecutionDegreeList;
    }

    private List readInfoExecutionCoursesOfdegree(ISuportePersistente sp,
            IExecutionPeriod executionPeriod,
            InfoExecutionDegree infoExecutionDegree)
            throws ExcepcaoPersistencia, FenixServiceException {
        IPersistentExecutionCourse persistentExecutionCourse = sp
                .getIPersistentExecutionCourse();
        ICursoExecucaoPersistente persistentExecutionDegree = sp
                .getICursoExecucaoPersistente();
        ICursoExecucao executionDegree = new CursoExecucao();
        executionDegree.setIdInternal(infoExecutionDegree.getIdInternal());
        executionDegree = (ICursoExecucao) persistentExecutionDegree.readByOId(
                executionDegree, false);

        List executionCourseList = persistentExecutionCourse
                .readByExecutionPeriodAndExecutionDegree(executionPeriod,
                        executionDegree);
        if (executionCourseList == null || executionCourseList.size() <= 0) { throw new FenixServiceException(
                "errors.impossible.operation"); }

        ListIterator listIterator = executionCourseList.listIterator();
        List infoExecutionCourseList = new ArrayList();
        while (listIterator.hasNext()) {
            IExecutionCourse executionCourse = (IExecutionCourse) listIterator
                    .next();
            InfoExecutionCourse infoExecutionCourse = copyIExecutionCourse2InfoExecutionCourse(executionCourse);
            infoExecutionCourseList.add(infoExecutionCourse);
        }

        return infoExecutionCourseList;
    }

    /**
     * @param infoExecutionDegreeList
     * @return
     */
    private InfoExecutionDegree selectExecutionDegree(ISuportePersistente sp,
            List infoExecutionDegreeList, Integer executionDegreeIdChosen,
            IStudent student) throws ExcepcaoPersistencia {
        ICursoExecucao executionDegree = null;

        //read the execution degree chosen
        if (executionDegreeIdChosen != null) {
            ICursoExecucaoPersistente persistentExecutionDegree = sp
                    .getICursoExecucaoPersistente();

            executionDegree = new CursoExecucao();
            executionDegree.setIdInternal(executionDegreeIdChosen);
            executionDegree = (ICursoExecucao) persistentExecutionDegree
                    .readByOId(executionDegree, false);
            if (executionDegree != null) { return copyIExecutionDegree2InfoExecutionDegree(executionDegree); }
        }

        IStudentCurricularPlanPersistente persistentCurricularPlan = sp
                .getIStudentCurricularPlanPersistente();
        IStudentCurricularPlan studentCurricularPlan = persistentCurricularPlan
                .readActiveByStudentNumberAndDegreeType(student.getNumber(),
                        TipoCurso.LICENCIATURA_OBJ);
        if (studentCurricularPlan == null
                || studentCurricularPlan.getDegreeCurricularPlan() == null
                || studentCurricularPlan.getDegreeCurricularPlan().getDegree() == null
                || studentCurricularPlan.getDegreeCurricularPlan().getDegree()
                        .getNome() == null) {

        return (InfoExecutionDegree) infoExecutionDegreeList.get(0); }

        final Integer degreeCode = studentCurricularPlan
                .getDegreeCurricularPlan().getDegree().getIdInternal();
        List infoExecutionDegreeListWithDegreeCode = (List) CollectionUtils
                .select(infoExecutionDegreeList, new Predicate() {

                    public boolean evaluate(Object input) {
                        InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) input;
                        return infoExecutionDegree
                                .getInfoDegreeCurricularPlan().getInfoDegree()
                                .getIdInternal().equals(degreeCode);
                    }
                });
        if (!infoExecutionDegreeListWithDegreeCode.isEmpty()) {
            return (InfoExecutionDegree) infoExecutionDegreeListWithDegreeCode
                    .get(0);
        } else {
            return (InfoExecutionDegree) infoExecutionDegreeList.get(0);
        }

    }

}