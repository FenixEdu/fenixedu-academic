package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.ManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.ScientificCouncilAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class SaveTeachersBody {

    protected Boolean run(final List<String> responsibleTeachersIds, final List<String> professorShipTeachersIds,
            final String executionCourseId) throws FenixServiceException {

        final ExecutionCourse executionCourse = AbstractDomainObject.fromExternalId(executionCourseId);

        final List<String> auxProfessorshipTeacherIDs = new ArrayList<String>(professorShipTeachersIds);

        final List<Professorship> professorships = executionCourse.getProfessorships();
        for (int i = 0; i < professorships.size(); i++) {
            final Professorship professorship = professorships.get(i);
            final Teacher teacher = professorship.getTeacher();
            final String teacherID = teacher.getExternalId();
            if (auxProfessorshipTeacherIDs.contains(teacherID)) {
                if (responsibleTeachersIds.contains(teacherID) && !professorship.getResponsibleFor()) {
                    professorship.setResponsibleFor(Boolean.TRUE);
                } else if (!responsibleTeachersIds.contains(teacherID) && professorship.getResponsibleFor()) {
                    professorship.setResponsibleFor(Boolean.FALSE);
                }
                auxProfessorshipTeacherIDs.remove(teacherID);
            } else {
                professorship.delete();
                i--;
            }
        }

        for (final String teacherID : auxProfessorshipTeacherIDs) {
            final Teacher teacher = AbstractDomainObject.fromExternalId(teacherID);
            final Boolean isResponsible = Boolean.valueOf(responsibleTeachersIds.contains(teacherID));
            Professorship.create(isResponsible, executionCourse, teacher, null);
        }

        return Boolean.TRUE;
    }

    // Service Invokers migrated from Berserk

    private static final SaveTeachersBody serviceInstance = new SaveTeachersBody();

    @Service
    public static Boolean runSaveTeachersBody(List<String> responsibleTeachersIds, List<String> professorShipTeachersIds,
            String executionCourseId) throws FenixServiceException, NotAuthorizedException {
        try {
            ManagerAuthorizationFilter.instance.execute();
            return serviceInstance.run(responsibleTeachersIds, professorShipTeachersIds, executionCourseId);
        } catch (NotAuthorizedException ex1) {
            try {
                ScientificCouncilAuthorizationFilter.instance.execute();
                return serviceInstance.run(responsibleTeachersIds, professorShipTeachersIds, executionCourseId);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

}