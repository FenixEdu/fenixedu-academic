package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;

public class TransferCurricularCourse implements IService {

    public void run(Integer sourceExecutionCourseId, final Integer curricularCourseId,
            Integer destinationExecutionCourseId) throws ExcepcaoPersistencia {

        final ISuportePersistente persistentSuport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();

        final ExecutionCourse sourceExecutionCourse = (ExecutionCourse) persistentSuport
                .getIPersistentObject().readByOID(ExecutionCourse.class, sourceExecutionCourseId);
        final ExecutionCourse destinationExecutionCourse = (ExecutionCourse) persistentSuport
                .getIPersistentObject().readByOID(ExecutionCourse.class, destinationExecutionCourseId);

        final CurricularCourse curricularCourse = (CurricularCourse) CollectionUtils.find(
                sourceExecutionCourse.getAssociatedCurricularCourses(), new Predicate() {
                    public boolean evaluate(Object arg0) {
                        CurricularCourse curricularCourse = (CurricularCourse) arg0;
                        return (curricularCourseId == curricularCourse.getIdInternal());
                    }
                });

        Set<Integer> transferedStudents = new HashSet<Integer>();
        transferAttends(destinationExecutionCourseId, sourceExecutionCourse, destinationExecutionCourse,
                curricularCourse, transferedStudents);
        deleteShiftStudents(sourceExecutionCourse, transferedStudents);

        sourceExecutionCourse.removeAssociatedCurricularCourses(curricularCourse);
        destinationExecutionCourse.addAssociatedCurricularCourses(curricularCourse);
    }

    /**
     * @param destinationExecutionCourseId
     * @param sourceExecutionCourse
     * @param destinationExecutionCourse
     * @param curricularCourse
     * @param transferedStudents
     * @throws ExcepcaoPersistencia
     */
    private void transferAttends(Integer destinationExecutionCourseId,
            ExecutionCourse sourceExecutionCourse, ExecutionCourse destinationExecutionCourse,
            CurricularCourse curricularCourse, Set<Integer> transferedStudents)
            throws ExcepcaoPersistencia {
        for (Attends attend : sourceExecutionCourse.getAttends()) {
            Enrolment enrollment = attend.getEnrolment();
            final Student student = attend.getAluno();
            if (enrollment != null) {
                CurricularCourse associatedCurricularCourse = attend.getEnrolment()
                        .getCurricularCourse();
                if (curricularCourse == associatedCurricularCourse) {
                    Attends existingAttend = (Attends) CollectionUtils.find(destinationExecutionCourse
                            .getAttends(), new Predicate() {
                        public boolean evaluate(Object arg0) {
                            Attends attendFromDestination = (Attends) arg0;
                            return (attendFromDestination.getAluno() == student);
                        }
                    });
                    if (existingAttend != null) {
                        existingAttend.setEnrolment(enrollment);
                        attend.delete();
                    } else {
                        attend.setDisciplinaExecucao(destinationExecutionCourse);
                    }

                    transferedStudents.add(student.getIdInternal());
                }
            }
        }
    }

    /**
     * @param sourceExecutionCourse
     * @param transferedStudents
     * @throws ExcepcaoPersistencia
     */
    private void deleteShiftStudents(ExecutionCourse sourceExecutionCourse, Set transferedStudents)
            throws ExcepcaoPersistencia {
        List<Shift> shifts = sourceExecutionCourse.getAssociatedShifts();

        for (Shift shift : shifts) {
            Iterator<Student> iter = shift.getStudentsIterator();
            while (iter.hasNext()) {
                Student student = iter.next();
                if (transferedStudents.contains(student.getIdInternal())) {
                    iter.remove();
                }
            }
        }
    }

}
