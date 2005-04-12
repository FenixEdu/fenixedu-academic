/*
 * Created on 16/Set/2003, 12:18:28 By Goncalo Luiz gedl [AT] rnl [DOT] ist
 * [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoAttendWithEnrollment;
import net.sourceforge.fenixedu.dataTransferObject.InfoAttendsSummary;
import net.sourceforge.fenixedu.dataTransferObject.InfoFrequenta;
import net.sourceforge.fenixedu.dataTransferObject.InfoFrequentaWithAllAndInfoStudentPlanAndInfoDegreePlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.ITurnoAluno;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IFrequentaPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrollment;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoAlunoPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.presentationTier.Action.Seminaries.Exceptions.BDException;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt Created at
 *         16/Set/2003, 12:18:28
 */
public class GetAttendaciesByStudentList implements IService {
    public GetAttendaciesByStudentList() {
    }

    public InfoAttendsSummary run(Integer executionCourseID, List infoStudents)
            throws FenixServiceException {
        List attendacies = new LinkedList();
        InfoAttendsSummary attendsSummary = new InfoAttendsSummary();
        List keys = new ArrayList();
        Map enrollmentDistribution = new HashMap();
        try {
            ISuportePersistente persistenceSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IFrequentaPersistente persistentAttendacy = persistenceSupport.getIFrequentaPersistente();
            IPersistentStudent persistentStudent = persistenceSupport.getIPersistentStudent();
            IPersistentEnrollment persistentEnrolment = persistenceSupport.getIPersistentEnrolment();
            ITurnoAlunoPersistente persistentShiftStudent = persistenceSupport
                    .getITurnoAlunoPersistente();
            IPersistentExecutionCourse persistentExecutionCourse = persistenceSupport
                    .getIPersistentExecutionCourse();
            List students = new LinkedList();
            IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                    ExecutionCourse.class, executionCourseID);
            if (executionCourse == null) {
                throw new FenixServiceException();
            }
            for (Iterator infoStudentsIterator = infoStudents.iterator(); infoStudentsIterator.hasNext();) {
                InfoStudent infoStudent = (InfoStudent) infoStudentsIterator.next();
                IStudent student = (IStudent) persistentStudent.readByOID(Student.class, infoStudent
                        .getIdInternal());
                if (student != null) {
                    students.add(student);
                }
            }
            for (Iterator studentsIterator = students.iterator(); studentsIterator.hasNext();) {
                IStudent student = (IStudent) studentsIterator.next();
                IAttends attendacy = persistentAttendacy.readByAlunoIdAndDisciplinaExecucaoId(student
                        .getIdInternal(), executionCourseID);
                Integer enrollments = new Integer(0);
                if (attendacy.getEnrolment() != null) {
                    IEnrolment enrollment = attendacy.getEnrolment();
                    ICurricularCourse curricularCourse = enrollment.getCurricularCourse();
                    IDegreeCurricularPlan degreeCurricularPlan = curricularCourse.getDegreeCurricularPlan();
                    IDegree degree = degreeCurricularPlan.getDegree();

                    List enrollmentList = persistentEnrolment
                            .readEnrollmentsByStudentAndCurricularCourseNameAndDegree(
                                    student,
                                    curricularCourse,
                                    degree);
                    enrollments = new Integer(enrollmentList.size());
                    if (enrollments.intValue() == 0) {
                        enrollments = new Integer(1);
                    }
                }
                if (keys.contains(enrollments)) {
                    enrollmentDistribution.put(enrollments, new Integer(
                            ((Integer) enrollmentDistribution.get(enrollments)).intValue() + 1));
                } else {
                    keys.add(enrollments);
                    enrollmentDistribution.put(enrollments, new Integer(1));
                }
                Map infoShifts = new HashMap();
                List shifts = persistentShiftStudent.readByStudentAndExecutionCourse(student,
                        executionCourse);
                if (shifts != null) {
                    Iterator iter = shifts.iterator();
                    while (iter.hasNext()) {
                        ITurnoAluno shiftStudent = (ITurnoAluno) iter.next();
                        //CLONER
                        //infoShifts.put(shiftStudent.getShift().getTipo().getSiglaTipoAula(),Cloner.get(shiftStudent.getShift()));
                        infoShifts.put(shiftStudent.getShift().getTipo().getSiglaTipoAula(), InfoShift
                                .newInfoFromDomain(shiftStudent.getShift()));
                    }
                }
                //CLONER
                //InfoFrequenta infoFrequenta =
                // Cloner.copyIFrequenta2InfoFrequenta(attendacy);
                InfoFrequenta infoFrequenta = InfoFrequentaWithAllAndInfoStudentPlanAndInfoDegreePlan
                        .newInfoFromDomain(attendacy);
                InfoAttendWithEnrollment infoAttendWithEnrollment = new InfoAttendWithEnrollment();
                infoAttendWithEnrollment.setAluno(infoFrequenta.getAluno());
                infoAttendWithEnrollment.setDisciplinaExecucao(infoFrequenta.getDisciplinaExecucao());
                infoAttendWithEnrollment.setIdInternal(infoFrequenta.getIdInternal());
                infoAttendWithEnrollment.setEnrollments(enrollments);
                infoAttendWithEnrollment.setInfoEnrolment(infoFrequenta.getInfoEnrolment());
                infoAttendWithEnrollment.setInfoShifts(infoShifts);
                attendacies.add(infoAttendWithEnrollment);
            }
            attendsSummary.setAttends(attendacies);
            attendsSummary.setEnrollmentDistribution(enrollmentDistribution);
            Collections.sort(keys);
            attendsSummary.setNumberOfEnrollments(keys);
        } catch (ExcepcaoPersistencia ex) {
            throw new BDException("Got an error while trying to get info about a student's work group",
                    ex);
        }
        return attendsSummary;
    }
}