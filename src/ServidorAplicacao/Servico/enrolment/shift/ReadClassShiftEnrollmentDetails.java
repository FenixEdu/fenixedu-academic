/*
 * Created on 11/Fev/2004
 */
package ServidorAplicacao.Servico.enrolment.shift;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
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
import DataBeans.enrollment.shift.ExecutionCourseShiftEnrollmentDetails;
import DataBeans.enrollment.shift.InfoClassEnrollmentDetails;
import DataBeans.enrollment.shift.ShiftEnrollmentDetails;
import Dominio.IAula;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.ISala;
import Dominio.IStudent;
import Dominio.ITurma;
import Dominio.ITurno;
import Dominio.ITurnoAluno;
import Dominio.Student;
import Dominio.Turma;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurmaPersistente;
import ServidorPersistente.ITurnoAlunoPersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @throws StudentNotFoundServiceException
 * @author jpvl
 */
public class ReadClassShiftEnrollmentDetails implements IService {

    /**
     * @author jpvl
     */
    public class StudentNotFoundServiceException extends FenixServiceException {

    }

    /**
     *  
     */
    public ReadClassShiftEnrollmentDetails() {
        super();
    }

    public InfoClassEnrollmentDetails run(InfoStudent infoStudent,
            Integer klassId) throws FenixServiceException {
        InfoClassEnrollmentDetails enrollmentDetails = null;
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IPersistentStudent studentDAO = sp.getIPersistentStudent();
            ITurmaPersistente classDAO = sp.getITurmaPersistente();
            IPersistentExecutionPeriod executionPeriodDAO = sp
                    .getIPersistentExecutionPeriod();
            ITurnoAlunoPersistente shiftStudentDAO = sp
                    .getITurnoAlunoPersistente();
            ITurnoPersistente shiftDAO = sp.getITurnoPersistente();

            //Current Execution Period
            IExecutionPeriod executionPeriod = executionPeriodDAO
                    .readActualExecutionPeriod();

            //Student
            IStudent student = readStudent(infoStudent, studentDAO);

            //Classes
            List classList = classDAO
                    .readClassesThatContainsStudentAttendsOnExecutionPeriod(
                            student, executionPeriod);

            //Class selected
            ITurma klass = null;
            if (klassId != null) {
                klass = (ITurma) classDAO.readByOId(new Turma(klassId), false);
            }

            //Shifts correspond to student attends
            List shiftAttendList = shiftDAO
                    .readShiftsThatContainsStudentAttendsOnExecutionPeriod(
                            student, executionPeriod);

            //Shifts enrolment
            List studentShifts = shiftStudentDAO
                    .readByStudentAndExecutionPeriod(student, executionPeriod);
            List shifts = collectShifts(studentShifts);
            List infoShifts = collectInfoShifts(shifts);

            List infoClassList = new ArrayList();
            Map classExecutionCourseShiftEnrollmentDetailsMap = createMapAndPopulateInfoClassList(
                    shiftStudentDAO, classList, shiftAttendList, infoClassList,
                    klass);

            enrollmentDetails = new InfoClassEnrollmentDetails();
            enrollmentDetails.setInfoStudent(copyIStudent2InfoStudent(student));
            //enrollmentDetails.setInfoShiftEnrolledList(studentShifts);
            enrollmentDetails.setInfoShiftEnrolledList(infoShifts);
            enrollmentDetails.setInfoClassList(infoClassList);
            enrollmentDetails
                    .setClassExecutionCourseShiftEnrollmentDetailsMap(classExecutionCourseShiftEnrollmentDetailsMap);

        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace(System.out);
            throw new FenixServiceException("Problems on database!", e);
        }

        return enrollmentDetails;
    }

    /**
     * @param shifts
     */
    private List collectInfoShifts(List shifts) {
        /* Prepare return */
        List infoShifts = (List) CollectionUtils.collect(shifts,
                new Transformer() {

                    public Object transform(Object input) {
                        ITurno shift = (ITurno) input;
                        InfoShift infoShift = copyShift2InfoShift(shift);
                        return infoShift;
                    }
                });

        return infoShifts;
    }

    /**
     * @param studentShifts
     * @return
     */
    private List collectShifts(List studentShifts) {
        List shifts = (List) CollectionUtils.collect(studentShifts,
                new Transformer() {

                    public Object transform(Object input) {
                        ITurnoAluno shiftStudent = (ITurnoAluno) input;
                        return shiftStudent.getShift();
                    }
                });
        return shifts;
    }

    /**
     * @param shiftStudentDAO
     * @param classList
     * @param shifts
     * @param infoClassList
     * @return
     */
    private Map createMapAndPopulateInfoClassList(
            ITurnoAlunoPersistente shiftStudentDAO, List classList,
            List shiftsAttendList, List infoClassList, ITurma klassToTreat) {
        Map classExecutionCourseShiftEnrollmentDetailsMap = new HashMap();

        /* shift id -> ShiftEnrollmentDetails */
        Map shiftsTreated = new HashMap();

        /* executionCourse id -> ExecutionCourseShiftEnrollmentDetails */
        Map executionCourseTreated = new HashMap();

        for (int i = 0; i < classList.size(); i++) {
            // Clean auxiliar
            shiftsTreated.clear();
            executionCourseTreated.clear();

            //Clone class
            ITurma klass = (ITurma) classList.get(i);
            InfoClass infoClass = copyIClass2InfoClass(klass);
            infoClassList.add(infoClass);

            Integer klassId = klass.getIdInternal();
            if ((klassToTreat == null && i == 0)
                    || (klassToTreat != null && klassId.equals(klassToTreat
                            .getIdInternal()))) {

                List shiftsRequired = (List) CollectionUtils.intersection(klass
                        .getAssociatedShifts(), shiftsAttendList);
                if (shiftsRequired != null) {
                    for (int j = 0; j < shiftsRequired.size(); j++) {
                        ITurno shift = (ITurno) shiftsRequired.get(j);

                        ShiftEnrollmentDetails shiftEnrollmentDetails = createShiftEnrollmentDetails(
                                shiftStudentDAO, shiftsTreated, shift);

                        ExecutionCourseShiftEnrollmentDetails executionCourseShiftEnrollmentDetails = createExecutionCourseShiftEnrollmentDetails(
                                executionCourseTreated, shift);
                        executionCourseShiftEnrollmentDetails
                                .addShiftEnrollmentDetails(shiftEnrollmentDetails);

                        List executionCourseShiftEnrollmentDetailsList = (List) classExecutionCourseShiftEnrollmentDetailsMap
                                .get(klassId);
                        if (executionCourseShiftEnrollmentDetailsList == null) {
                            executionCourseShiftEnrollmentDetailsList = new ArrayList();
                            executionCourseShiftEnrollmentDetailsList
                                    .add(executionCourseShiftEnrollmentDetails);
                            classExecutionCourseShiftEnrollmentDetailsMap.put(
                                    klassId,
                                    executionCourseShiftEnrollmentDetailsList);
                        } else {
                            if (!executionCourseShiftEnrollmentDetailsList
                                    .contains(executionCourseShiftEnrollmentDetails)) {
                                executionCourseShiftEnrollmentDetailsList
                                        .add(executionCourseShiftEnrollmentDetails);
                            }
                        }
                    }
                }
            }
        }
        return classExecutionCourseShiftEnrollmentDetailsMap;
    }

    /**
     * @param executionCourseTreated
     * @param shift
     * @return
     */
    private ExecutionCourseShiftEnrollmentDetails createExecutionCourseShiftEnrollmentDetails(
            Map executionCourseTreated, ITurno shift) {
        IExecutionCourse executionCourse = shift.getDisciplinaExecucao();
        ExecutionCourseShiftEnrollmentDetails executionCourseShiftEnrollmentDetails = (ExecutionCourseShiftEnrollmentDetails) executionCourseTreated
                .get(executionCourse.getIdInternal());

        if (executionCourseShiftEnrollmentDetails == null) {
            executionCourseShiftEnrollmentDetails = new ExecutionCourseShiftEnrollmentDetails();
            InfoExecutionCourse infoExecutionCourse = copyIExecutionCourse2InfoExecutionCourse(executionCourse);
            executionCourseShiftEnrollmentDetails
                    .setInfoExecutionCourse(infoExecutionCourse);

            executionCourseTreated.put(executionCourse.getIdInternal(),
                    executionCourseShiftEnrollmentDetails);
        }

        return executionCourseShiftEnrollmentDetails;
    }

    /**
     * @param shiftStudentDAO
     * @param shiftsTreated
     * @param shift
     * @return
     */
    private ShiftEnrollmentDetails createShiftEnrollmentDetails(
            ITurnoAlunoPersistente shiftStudentDAO, Map shiftsTreated,
            ITurno shift) {
        ShiftEnrollmentDetails shiftEnrollmentDetails = (ShiftEnrollmentDetails) shiftsTreated
                .get(shift.getIdInternal());
        if (shiftEnrollmentDetails == null) {
            shiftEnrollmentDetails = new ShiftEnrollmentDetails();

            InfoShift infoShift = copyShift2InfoShift(shift);
            int occupation = shiftStudentDAO.readNumberOfStudentsByShift(shift);
            shiftEnrollmentDetails.setInfoShift(infoShift);
            shiftEnrollmentDetails.setVacancies(new Integer(shift.getLotacao()
                    .intValue()
                    - occupation));

            shiftsTreated.put(shift.getIdInternal(), shiftEnrollmentDetails);
        }
        return shiftEnrollmentDetails;
    }

    /**
     * @param infoStudent
     * @param studentDAO
     * @return @throws
     *         StudentNotFoundServiceException
     */
    private IStudent readStudent(InfoStudent infoStudent,
            IPersistentStudent studentDAO)
            throws StudentNotFoundServiceException {
        IStudent student = (IStudent) studentDAO.readByOId(new Student(
                infoStudent.getIdInternal()), false);
        if (student == null) { throw new StudentNotFoundServiceException(); }
        return student;
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
}