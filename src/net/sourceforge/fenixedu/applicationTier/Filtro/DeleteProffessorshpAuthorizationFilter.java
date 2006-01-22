package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentProfessorship;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

public class DeleteProffessorshpAuthorizationFilter extends AuthorizationByRoleFilter {

    @Override
    protected RoleType getRoleType() {
        return RoleType.TEACHER;
    }

    @Override
    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
        IUserView id = getRemoteUser(request);
        Object[] arguments = getServiceCallArguments(request);

        try {
            if ((id == null) || (id.getRoles() == null)
                    || !AuthorizationUtils.containsRole(id.getRoles(), getRoleType())
                    || isSamePersonAsBeingRemoved(id, arguments)) {
                throw new NotAuthorizedFilterException();
            }
        } catch (RuntimeException e) {
            throw new NotAuthorizedFilterException();
        }
    }

    private boolean isSamePersonAsBeingRemoved(IUserView id, Object[] argumentos) 
            throws ExcepcaoPersistencia {
        final IPersistentProfessorship persistentProfessorship = persistentSupport.getIPersistentProfessorship();

        final Integer executionCourseID = (Integer) argumentos[0];
        final Integer teacherID = (Integer) argumentos[1];

        final Professorship professorship = persistentProfessorship.readByTeacherAndExecutionCourse(
                teacherID, executionCourseID);
        final Teacher teacher = professorship.getTeacher();
        final Person person = teacher.getPerson();
        return id.getUtilizador().equalsIgnoreCase(person.getUsername());
    }

}
