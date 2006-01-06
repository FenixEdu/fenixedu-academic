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

        final IExecutionCourse sourceExecutionCourse = (IExecutionCourse) persistentSuport
                .getIPersistentObject().readByOID(ExecutionCourse.class, sourceExecutionCourseId);
        final IExecutionCourse destinationExecutionCourse = (IExecutionCourse) persistentSuport
                .getIPersistentObject().readByOID(ExecutionCourse.class, destinationExecutionCourseId);

        final ICurricularCourse curricularCourse = (ICurricularCourse) CollectionUtils.find(
                sourceExecutionCourse.getAssociatedCurricularCourses(), new Predicate() {
                    public boolean evaluate(Object arg0) {
                        ICurricularCourse curricularCourse = (ICurricularCourse) arg0;
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
            IExecutionCourse sourceExecutionCourse, IExecutionCourse destinationExecutionCourse,
            ICurricularCourse curricularCourse, Set<Integer> transferedStudents)
            throws ExcepcaoPersistencia {
        for (IAttends attend : sourceExecutionCourse.getAttends()) {
            IEnrolment enrollment = attend.getEnrolment();
            final IStudent student = attend.getAluno();
            if (enrollment != null) {
                ICurricularCourse associatedCurricularCourse = attend.getEnrolment()
                        .getCurricularCourse();
                if (curricularCourse == associatedCurricularCourse) {
                    IAttends existingAttend = (IAttends) CollectionUtils.find(destinationExecutionCourse
                            .getAttends(), new Predicate() {
                        public boolean evaluate(Object arg0) {
                            IAttends attendFromDestination = (IAttends) arg0;
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
    private void deleteShiftStudents(IExecutionCourse sourceExecutionCourse, Set transferedStudents)
            throws ExcepcaoPersistencia {
        List<IShift> shifts = sourceExecutionCourse.getAssociatedShifts();

        for (IShift shift : shifts) {
            Iterator<IStudent> iter = shift.getStudentsIterator();
            while (iter.hasNext()) {
                IStudent student = iter.next();
                if (transferedStudents.contains(student.getIdInternal())) {
                    iter.remove();
                }
            }
        }
    }

}
