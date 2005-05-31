/*
 * Created on 12/Fev/2004
 */
package net.sourceforge.fenixedu.applicationTier.Servico.enrollment.shift;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.enrollment.shift.ShiftEnrollmentErrorReport;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IShiftStudent;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftStudent;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoAlunoPersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author jmota
 */
public class EnrollStudentInShifts implements IService {

    public class StudentNotFoundServiceException extends FenixServiceException {

    }

    /**
     *  
     */
    public EnrollStudentInShifts() {
    }

    /**
     * @param studentId
     * @param shiftIdsToEnroll
     * @return @throws
     *         StudentNotFoundServiceException
     * @throws FenixServiceException
     */
    public ShiftEnrollmentErrorReport run(Integer studentId, List shiftIdsToEnroll)
            throws StudentNotFoundServiceException, FenixServiceException {
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentStudent persistentStudent = sp.getIPersistentStudent();
            ITurnoPersistente persistentShift = sp.getITurnoPersistente();
            ITurnoAlunoPersistente persistentShiftStudent = sp.getITurnoAlunoPersistente();
            IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();
            ShiftEnrollmentErrorReport errorReport = new ShiftEnrollmentErrorReport();

            if (shiftIdsToEnroll != null && !shiftIdsToEnroll.isEmpty()) {
                IStudent student = (IStudent) persistentStudent.readByOID(Student.class, studentId);
                if (student == null) {
                    throw new StudentNotFoundServiceException();
                }

                if (student.getPayedTuition() == null || student.getPayedTuition().equals(Boolean.FALSE)) {
                    throw new FenixServiceException(
                            "error.exception.notAuthorized.student.warningTuition");
                }

                IExecutionPeriod executionPeriod = persistentExecutionPeriod.readActualExecutionPeriod();
                List shiftEnrollments = persistentShiftStudent.readByStudentAndExecutionPeriod(student.getIdInternal(),
                        executionPeriod.getIdInternal());

                List shiftsToUnEnroll = new ArrayList();
                List filteredShiftsIdsToEnroll = new ArrayList();

                filteredShiftsIdsToEnroll = calculateShiftsToEnroll(shiftIdsToEnroll, shiftEnrollments);

                shiftsToUnEnroll = enrollStudentsInShiftsAndCalculateShiftsToUnEnroll(student,
                        filteredShiftsIdsToEnroll, errorReport, persistentShift, persistentShiftStudent);

                shiftsToUnEnroll = calculateShiftsToUnEnroll(shiftIdsToEnroll, shiftEnrollments,
                        shiftsToUnEnroll);

                unEnrollStudentsInShifts(shiftsToUnEnroll, errorReport, persistentShiftStudent);

            }

            return errorReport;
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new FenixServiceException(e);
        }
    }

    /**
     * @param shiftsToUnEnroll
     * @param errors
     * @param persistentShiftStudent
     */
    private void unEnrollStudentsInShifts(List shiftsToUnEnroll, ShiftEnrollmentErrorReport errorReport,
            ITurnoAlunoPersistente persistentShiftStudent) throws ExcepcaoPersistencia {
        Iterator iter = shiftsToUnEnroll.iterator();
        while (iter.hasNext()) {
            IShiftStudent shiftStudent = (IShiftStudent) iter.next();
            //persistentShiftStudent.delete(shiftStudent);
            shiftStudent.getStudent().getShiftStudents().remove(shiftStudent);
            shiftStudent.getShift().getStudentShifts().remove(shiftStudent);
            shiftStudent.setShift(null);
            shiftStudent.setStudent(null);
            persistentShiftStudent.deleteByOID(ShiftStudent.class, shiftStudent.getIdInternal());
        }

    }

    /**
     * @param student
     * @param filteredShiftsIdsToEnroll
     * @param errors
     * @param persistentShift
     * @param persistentShiftStudent
     * @throws ExcepcaoPersistencia
     */
    private List enrollStudentsInShiftsAndCalculateShiftsToUnEnroll(IStudent student,
            List filteredShiftsIdsToEnroll, ShiftEnrollmentErrorReport errorReport,
            ITurnoPersistente persistentShift, ITurnoAlunoPersistente persistentShiftStudent)
            throws ExcepcaoPersistencia {
        List shiftsToUnEnroll = new ArrayList();
        Iterator iter = filteredShiftsIdsToEnroll.iterator();
        while (iter.hasNext()) {
            Integer shiftId = (Integer) iter.next();
            IShift shift = (IShift) persistentShift.readByOID(Shift.class, shiftId);
            if (shift == null) {
                errorReport.getUnExistingShifts().add(shiftId);
            } else {
                IShiftStudent shiftStudentToDelete = persistentShiftStudent
                        .readByStudentAndExecutionCourseAndLessonType(student.getIdInternal(), shift
                                .getDisciplinaExecucao().getIdInternal(), shift.getTipo());

                if (shift.getLotacao().intValue() > persistentShiftStudent
                        .readNumberOfStudentsByShift(shift.getIdInternal())) {
                    if (shiftStudentToDelete != null) {
                        shiftsToUnEnroll.add(shiftStudentToDelete);
                    }
                    IShiftStudent shiftStudentToWrite = new ShiftStudent();
                    persistentShiftStudent.simpleLockWrite(shiftStudentToWrite);
                    shiftStudentToWrite.setShift(shift);
                    shiftStudentToWrite.setStudent(student);
                } else {
                    errorReport.getUnAvailableShifts().add(InfoShift.newInfoFromDomain(shift));
                    //copyShift2InfoShift(shift));
                }

            }

        }
        return shiftsToUnEnroll;
    }

    //    private InfoShift copyShift2InfoShift(IShift shift) {
    //        InfoShift infoShift = null;
    //        if (shift != null) {
    //            infoShift = new InfoShift();
    //            infoShift.setIdInternal(shift.getIdInternal());
    //            infoShift.setNome(shift.getNome());
    //            infoShift
    //                    .setInfoDisciplinaExecucao(copyIExecutionCourse2InfoExecutionCourse(shift
    //                            .getDisciplinaExecucao()));
    //            infoShift.setInfoLessons(copyILessons2InfoLessons(shift
    //                    .getAssociatedLessons()));
    //            infoShift.setTipo(shift.getTipo());
    //            infoShift.setLotacao(shift.getLotacao());
    //            infoShift.setOcupation(shift.getOcupation());
    //            infoShift.setPercentage(shift.getPercentage());
    //            infoShift.setInfoClasses(copyIClasses2InfoClasses(shift
    //                    .getAssociatedClasses()));
    //        }
    //        return infoShift;
    //    }
    //
    //    /**
    //     * @param list
    //     * @return
    //     */
    //    private List copyIClasses2InfoClasses(List list) {
    //        List infoClasses = null;
    //        if (list != null) {
    //            infoClasses = (List) CollectionUtils.collect(list,
    //                    new Transformer() {
    //
    //                        public Object transform(Object arg0) {
    //
    //                            return copyIClass2InfoClass((ISchoolClass) arg0);
    //                        }
    //
    //                    });
    //        }
    //        return infoClasses;
    //    }
    //
    //    private InfoClass copyIClass2InfoClass(ISchoolClass turma) {
    //        InfoClass infoClass = null;
    //        if (turma != null) {
    //            infoClass = new InfoClass();
    //            infoClass.setIdInternal(turma.getIdInternal());
    //            infoClass.setNome(turma.getNome());
    //            infoClass
    //                    .setInfoExecutionDegree(copyIExecutionDegree2InfoExecutionDegree(turma
    //                            .getExecutionDegree()));
    //
    //        }
    //        return infoClass;
    //    }
    //
    //    /**
    //     * @param executionDegree
    //     * @return
    //     */
    //    private InfoExecutionDegree copyIExecutionDegree2InfoExecutionDegree(
    //            IExecutionDegree executionDegree) {
    //        InfoExecutionDegree infoExecutionDegree = null;
    //        if (executionDegree != null) {
    //            infoExecutionDegree = new InfoExecutionDegree();
    //            infoExecutionDegree.setIdInternal(executionDegree.getIdInternal());
    //            infoExecutionDegree
    //                    .setInfoExecutionYear(copyIExecutionYear2InfoExecutionYear(executionDegree
    //                            .getExecutionYear()));
    //            infoExecutionDegree
    //                    .setInfoDegreeCurricularPlan(copyIDegreeCurricularPlan2InfoDegreeCurricularPlan(executionDegree
    //                            .getDegreeCurricularPlan()));
    //        }
    //        return infoExecutionDegree;
    //    }
    //
    //    /**
    //     * @param plan
    //     * @return
    //     */
    //    private InfoDegreeCurricularPlan
    // copyIDegreeCurricularPlan2InfoDegreeCurricularPlan(
    //            IDegreeCurricularPlan plan) {
    //        InfoDegreeCurricularPlan infoDegreeCurricularPlan = null;
    //        if (plan != null) {
    //            infoDegreeCurricularPlan = new InfoDegreeCurricularPlan();
    //            infoDegreeCurricularPlan.setIdInternal(plan.getIdInternal());
    //            infoDegreeCurricularPlan.setName(plan.getName());
    //            infoDegreeCurricularPlan.setInfoDegree(copyIDegree2InfoDegree(plan
    //                    .getDegree()));
    //        }
    //        return infoDegreeCurricularPlan;
    //    }
    //
    //    /**
    //     * @param degree
    //     * @return
    //     */
    //    private InfoDegree copyIDegree2InfoDegree(IDegree degree) {
    //        InfoDegree infoDegree = null;
    //        if (degree != null) {
    //            infoDegree = new InfoDegree();
    //            infoDegree.setIdInternal(degree.getIdInternal());
    //            infoDegree.setNome(degree.getNome());
    //            infoDegree.setSigla(degree.getSigla());
    //            infoDegree.setTipoCurso(degree.getTipoCurso());
    //        }
    //        return infoDegree;
    //    }
    //
    //    /**
    //     * @param list
    //     * @return
    //     */
    //    private List copyILessons2InfoLessons(List list) {
    //        List infoLessons = null;
    //        if (list != null) {
    //            infoLessons = (List) CollectionUtils.collect(list,
    //                    new Transformer() {
    //
    //                        public Object transform(Object arg0) {
    //
    //                            return copyILesson2InfoLesson((ILesson) arg0);
    //                        }
    //
    //                    });
    //        }
    //        return infoLessons;
    //    }
    //
    //    private Object copyILesson2InfoLesson(ILesson lesson) {
    //        InfoLesson infoLesson = null;
    //        if (lesson != null) {
    //            infoLesson = new InfoLesson();
    //            infoLesson.setIdInternal(lesson.getIdInternal());
    //            infoLesson.setDiaSemana(lesson.getDiaSemana());
    //            infoLesson.setFim(lesson.getFim());
    //            infoLesson.setInicio(lesson.getInicio());
    //            infoLesson.setTipo(lesson.getTipo());
    //            infoLesson.setInfoSala(copyISala2InfoRoom(lesson.getSala()));
    //        }
    //        return infoLesson;
    //    }
    //
    //    /**
    //     * @param sala
    //     * @return
    //     */
    //    private InfoRoom copyISala2InfoRoom(IRoom sala) {
    //        InfoRoom infoRoom = null;
    //        if (sala != null) {
    //            infoRoom = new InfoRoom();
    //            infoRoom.setIdInternal(sala.getIdInternal());
    //            infoRoom.setNome(sala.getNome());
    //        }
    //        return infoRoom;
    //    }
    //
    //    /**
    //     * @param executionCourse
    //     * @return
    //     */
    //    private InfoExecutionCourse copyIExecutionCourse2InfoExecutionCourse(
    //            IExecutionCourse executionCourse) {
    //        InfoExecutionCourse infoExecutionCourse = null;
    //        if (executionCourse != null) {
    //            infoExecutionCourse = new InfoExecutionCourse();
    //            infoExecutionCourse.setIdInternal(executionCourse.getIdInternal());
    //            infoExecutionCourse.setNome(executionCourse.getNome());
    //            infoExecutionCourse.setSigla(executionCourse.getSigla());
    //            infoExecutionCourse
    //                    .setInfoExecutionPeriod(copyIExecutionPeriod2InfoExecutionPeriod(executionCourse
    //                            .getExecutionPeriod()));
    //        }
    //        return infoExecutionCourse;
    //    }
    //
    //    /**
    //     * @param period
    //     * @return
    //     */
    //    private InfoExecutionPeriod copyIExecutionPeriod2InfoExecutionPeriod(
    //            IExecutionPeriod period) {
    //        InfoExecutionPeriod infoExecutionPeriod = null;
    //        if (period != null) {
    //            infoExecutionPeriod = new InfoExecutionPeriod();
    //            infoExecutionPeriod.setIdInternal(period.getIdInternal());
    //            infoExecutionPeriod.setName(period.getName());
    //            infoExecutionPeriod
    //                    .setInfoExecutionYear(copyIExecutionYear2InfoExecutionYear(period
    //                            .getExecutionYear()));
    //        }
    //        return infoExecutionPeriod;
    //    }
    //
    //    /**
    //     * @param year
    //     * @return
    //     */
    //    private InfoExecutionYear copyIExecutionYear2InfoExecutionYear(
    //            IExecutionYear year) {
    //        InfoExecutionYear infoExecutionYear = null;
    //        if (year != null) {
    //            infoExecutionYear = new InfoExecutionYear();
    //            infoExecutionYear.setIdInternal(year.getIdInternal());
    //            infoExecutionYear.setYear(year.getYear());
    //        }
    //        return infoExecutionYear;
    //    }

    /**
     * @param shiftIdsToEnroll
     * @param shiftEnrollments
     * @param filteredShiftsIdsToEnroll
     */
    private List calculateShiftsToEnroll(List shiftIdsToEnroll, List shiftEnrollments) {

        if (shiftEnrollments != null) {
            List shiftIdsToIgnore = new ArrayList();
            Iterator iter = shiftEnrollments.iterator();
            while (iter.hasNext()) {
                IShiftStudent shiftStudent = (IShiftStudent) iter.next();

                if (shiftIdsToEnroll.contains(shiftStudent.getShift().getIdInternal())) {

                    shiftIdsToIgnore.add(shiftStudent.getShift().getIdInternal());
                }
            }

            return (List) CollectionUtils.subtract(shiftIdsToEnroll, shiftIdsToIgnore);
        }

        return shiftIdsToEnroll;

    }

    /**
     * @param shiftIdsToEnroll
     * @param shiftEnrollments
     */
    private List calculateShiftsToUnEnroll(List shiftIdsToEnroll, List shiftEnrollments,
            List shiftsToUnEnroll) {
        return shiftsToUnEnroll;
    }

}