/*
 * Created on 2004/10/25
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.ITurnoAluno;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IFrequentaPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoAlunoPersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author Luis Cruz
 */
public class TransferCurricularCourse implements IService {
   
    public void run(Integer sourceExecutionCourseId,
            final Integer curricularCourseId,
            Integer destinationExecutionCourseId) throws ExcepcaoPersistencia {

        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IPersistentExecutionCourse persistentExecutionCourse = persistentSuport.getIPersistentExecutionCourse();
        IFrequentaPersistente persistentAttend = persistentSuport.getIFrequentaPersistente();
        ITurnoPersistente persistentShift = persistentSuport.getITurnoPersistente();
        ITurnoAlunoPersistente persistentShiftStudent = persistentSuport.getITurnoAlunoPersistente();

        IExecutionCourse sourceExecutionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(ExecutionCourse.class, sourceExecutionCourseId);
        IExecutionCourse destinationExecutionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(ExecutionCourse.class, destinationExecutionCourseId);
        ICurricularCourse curricularCourse = (ICurricularCourse) CollectionUtils.find(sourceExecutionCourse.getAssociatedCurricularCourses(), new Predicate() {
            public boolean evaluate(Object arg0) {
                ICurricularCourse curricularCourse = (ICurricularCourse) arg0;
                return curricularCourseId.equals(curricularCourse.getIdInternal());
            }
            });

        persistentExecutionCourse.simpleLockWrite(sourceExecutionCourse);
        persistentExecutionCourse.simpleLockWrite(destinationExecutionCourse);

        Set transferedStudents = new HashSet();

        transferAttends(destinationExecutionCourseId, persistentAttend, sourceExecutionCourse, destinationExecutionCourse, curricularCourse, transferedStudents);

        deleteShiftStudents(persistentShift, persistentShiftStudent, sourceExecutionCourse, transferedStudents);

        sourceExecutionCourse.getAssociatedCurricularCourses().remove(curricularCourse);

        destinationExecutionCourse.getAssociatedCurricularCourses().add(curricularCourse);
   }

    /**
     * @param persistentShift
     * @param persistentShiftStudent
     * @param sourceExecutionCourse
     * @param transferedStudents
     * @throws ExcepcaoPersistencia
     */
    private void deleteShiftStudents(ITurnoPersistente persistentShift, ITurnoAlunoPersistente persistentShiftStudent, IExecutionCourse sourceExecutionCourse, Set transferedStudents) throws ExcepcaoPersistencia
    {
        List shifts = persistentShift.readByExecutionCourseID(sourceExecutionCourse.getIdInternal());
        for (Iterator iterator = shifts.iterator(); iterator.hasNext();) {
            IShift shift = (IShift) iterator.next();
            List shiftStudents = persistentShiftStudent.readByShiftID(shift.getIdInternal());
            for (Iterator shiftStudentIterator = shiftStudents.iterator(); shiftStudentIterator.hasNext();) {
                ITurnoAluno shiftStudent = (ITurnoAluno) shiftStudentIterator.next();
                IStudent student = shiftStudent.getStudent();
                
                if (transferedStudents.contains(student.getIdInternal())) {
                    persistentShiftStudent.delete(shiftStudent);
                }
            }
        }
    }

    /**
     * @param destinationExecutionCourseId
     * @param persistentAttend
     * @param sourceExecutionCourse
     * @param destinationExecutionCourse
     * @param curricularCourse
     * @param transferedStudents
     * @throws ExcepcaoPersistencia
     */
    private void transferAttends(Integer destinationExecutionCourseId, IFrequentaPersistente persistentAttend, IExecutionCourse sourceExecutionCourse, IExecutionCourse destinationExecutionCourse, ICurricularCourse curricularCourse, Set transferedStudents) throws ExcepcaoPersistencia
    {
        List attends = sourceExecutionCourse.getAttends();
        for (Iterator iterator = attends.iterator(); iterator.hasNext();) {
            IAttends attend = (IAttends) iterator.next();
            IEnrolment enrollment = attend.getEnrolment();
            final IStudent student = attend.getAluno();
            if (enrollment != null) {
                ICurricularCourse associatedCurricularCourse = attend.getEnrolment().getCurricularCourse();
                if (curricularCourse.getIdInternal().equals(associatedCurricularCourse.getIdInternal())) {
                    IAttends existingAttend = (IAttends) CollectionUtils.find(destinationExecutionCourse.getAttends(), new Predicate() {
                        public boolean evaluate(Object arg0) {
                            IAttends attendFromDestination = (IAttends) arg0;
                            return attendFromDestination.getAluno().getIdInternal().equals(student.getIdInternal());
                        }});
                    if (existingAttend != null) {
                        persistentAttend.simpleLockWrite(existingAttend);
                        existingAttend.setEnrolment(enrollment);
                        persistentAttend.delete(attend);
                    } else {
                        persistentAttend.simpleLockWrite(attend);
                        attend.setDisciplinaExecucao(destinationExecutionCourse);                        
                    }

                    transferedStudents.add(student.getIdInternal());
                }
            }
        }
    }

}