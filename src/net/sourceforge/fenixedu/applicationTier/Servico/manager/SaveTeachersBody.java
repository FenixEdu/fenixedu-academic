package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class SaveTeachersBody extends Service {

    public Boolean run(final List responsibleTeachersIds, final List<Integer> professorShipTeachersIds,
            final Integer executionCourseId) throws FenixServiceException{

        final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseId);

        final List<Integer> auxProfessorshipTeacherIDs = new ArrayList<Integer>(professorShipTeachersIds);

        final List<Professorship> professorships = executionCourse.getProfessorships();
        for (int i = 0; i < professorships.size(); i++) {
            final Professorship professorship = professorships.get(i);
            final Teacher teacher = professorship.getTeacher();
            final Integer teacherID = teacher.getIdInternal();
            if (auxProfessorshipTeacherIDs.contains(teacherID)) {
                if (responsibleTeachersIds.contains(teacherID) && !professorship.getResponsibleFor()) {
                    professorship.setResponsibleFor(Boolean.TRUE);
                } else if (!responsibleTeachersIds.contains(teacherID)
                        && professorship.getResponsibleFor()) {
                    professorship.setResponsibleFor(Boolean.FALSE);
                }
                auxProfessorshipTeacherIDs.remove(teacherID);
            } else {
                professorship.delete();
                i--;
            }
        }

        for (final Integer teacherID : auxProfessorshipTeacherIDs) {
            final Teacher teacher = rootDomainObject.readTeacherByOID(
                    teacherID);
            final Boolean isResponsible = Boolean.valueOf(responsibleTeachersIds.contains(teacherID));
            Professorship.create(isResponsible, executionCourse, teacher, null);
        }

        return Boolean.TRUE;
    }

}
