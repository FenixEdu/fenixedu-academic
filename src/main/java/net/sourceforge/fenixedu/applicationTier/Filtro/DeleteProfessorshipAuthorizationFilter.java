package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class DeleteProfessorshipAuthorizationFilter extends AuthorizationByRoleFilter {

    public static final DeleteProfessorshipAuthorizationFilter instance = new DeleteProfessorshipAuthorizationFilter();

    @Override
    protected RoleType getRoleType() {
        return RoleType.TEACHER;
    }

    public void execute(Integer executionCourseID, Integer selectedTeacherID) throws Exception {
        IUserView id = AccessControl.getUserView();

        try {
            final Person loggedPerson = id.getPerson();

            Teacher teacher = RootDomainObject.getInstance().readTeacherByOID(selectedTeacherID);
            ExecutionCourse executionCourse = RootDomainObject.getInstance().readExecutionCourseByOID(executionCourseID);

            Professorship selectedProfessorship = null;
            if (teacher != null) {
                selectedProfessorship = teacher.getProfessorshipByExecutionCourse(executionCourse);
            }

            if ((id == null) || (selectedProfessorship == null) || (id.getRoleTypes() == null) || !id.hasRoleType(getRoleType())
                    || isSamePersonAsBeingRemoved(loggedPerson, selectedProfessorship.getPerson())
                    || selectedProfessorship.getResponsibleFor()) {
                throw new NotAuthorizedException();
            }
        } catch (RuntimeException e) {
            throw new NotAuthorizedException();
        }
    }

    private boolean isSamePersonAsBeingRemoved(Person loggedPerson, Person selectedPerson) {
        return loggedPerson == selectedPerson;
    }

}
