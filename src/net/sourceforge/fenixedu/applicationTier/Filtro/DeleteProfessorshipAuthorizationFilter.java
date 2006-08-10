package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

public class DeleteProfessorshipAuthorizationFilter extends AuthorizationByRoleFilter {

    @Override
    protected RoleType getRoleType() {
        return RoleType.TEACHER;
    }

    @Override
    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
        IUserView id = getRemoteUser(request);
        Object[] arguments = getServiceCallArguments(request);

        try {

            final Person loggedPerson = id.getPerson();
            final Integer executionCourseID = (Integer) arguments[0];
            final Integer selectedTeacherID = (Integer) arguments[1];

            Teacher teacher = rootDomainObject.readTeacherByOID(selectedTeacherID);
            ExecutionCourse executionCourse = rootDomainObject
                    .readExecutionCourseByOID(executionCourseID);

            Professorship selectedProfessorship = null;
            if (teacher != null) {
                selectedProfessorship = teacher.getProfessorshipByExecutionCourse(executionCourse);
            }

            if ((id == null)
                    || (selectedProfessorship == null)
                    || (id.getRoleTypes() == null)
                    || !id.hasRoleType(getRoleType())
                    || isSamePersonAsBeingRemoved(loggedPerson, selectedProfessorship.getTeacher()
                            .getPerson()) || selectedProfessorship.getResponsibleFor()) {
                throw new NotAuthorizedFilterException();
            }
        } catch (RuntimeException e) {
            throw new NotAuthorizedFilterException();
        }
    }

    private boolean isSamePersonAsBeingRemoved(Person loggedPerson, Person selectedPerson)
            throws ExcepcaoPersistencia {
        return loggedPerson == selectedPerson;
    }

}
