package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class SaveTeachersBody implements IService {

    public Boolean run(final List responsibleTeachersIds, final List professorShipTeachersIds,
            final Integer executionCourseId) throws FenixServiceException, ExcepcaoPersistencia {

        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();
        final IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();

        final IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.
                readByOID(ExecutionCourse.class, executionCourseId);

        final List<Integer> auxProfessorshipTeacherIDs = new ArrayList(professorShipTeachersIds);

        final List<IProfessorship> professorships = executionCourse.getProfessorships();
        for (int i = 0; i < professorships.size(); i++) {
            final IProfessorship professorship = professorships.get(i);
            final ITeacher teacher = professorship.getTeacher();
            final Integer teacherID = teacher.getIdInternal();
            if (auxProfessorshipTeacherIDs.contains(teacherID)) {
                if (responsibleTeachersIds.contains(teacherID) && !professorship.getResponsibleFor()) {
                        professorship.setResponsibleFor(Boolean.TRUE);
                }
                auxProfessorshipTeacherIDs.remove(teacherID);
            } else {
                professorship.delete();
                i--;
            }
        }

        for (final Integer teacherID : auxProfessorshipTeacherIDs) {
            final ITeacher teacher = (ITeacher) persistentTeacher.readByOID(Teacher.class, teacherID);
            final Boolean isResponsible = Boolean.valueOf(responsibleTeachersIds.contains(teacherID));
            Professorship.create(isResponsible, executionCourse, teacher, null);
        }

        return Boolean.TRUE;
    }
}