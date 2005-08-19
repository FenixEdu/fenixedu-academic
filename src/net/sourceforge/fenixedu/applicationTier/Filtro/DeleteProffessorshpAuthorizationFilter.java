package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentProfessorship;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
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
        final ISuportePersistente persistenteSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentProfessorship persistentProfessorship = persistenteSupport.getIPersistentProfessorship();

        final Integer executionCourseID = (Integer) argumentos[0];
        final Integer teacherID = (Integer) argumentos[1];

        final IProfessorship professorship = persistentProfessorship.readByTeacherAndExecutionCourse(
                teacherID, executionCourseID);
        final ITeacher teacher = professorship.getTeacher();
        final IPerson person = teacher.getPerson();
        return id.getUtilizador().equalsIgnoreCase(person.getUsername());
    }

}
