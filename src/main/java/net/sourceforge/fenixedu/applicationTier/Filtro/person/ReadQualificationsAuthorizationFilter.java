package net.sourceforge.fenixedu.applicationTier.Filtro.person;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.Filtro;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class ReadQualificationsAuthorizationFilter extends Filtro {

    public ReadQualificationsAuthorizationFilter() {
    }

    protected RoleType getRoleTypeTeacher() {
        return RoleType.TEACHER;
    }

    @Override
    public void execute(Object[] parameters) throws Exception {
        IUserView id = AccessControl.getUserView();
        try {
            // Verify if needed fields are null
            if ((id == null) || (id.getRoleTypes() == null)) {
                throw new NotAuthorizedException();
            }

            // Verify if:
            // 1: The user ir a Grant Owner Manager and the qualification
            // belongs to a Grant Owner
            // 2: The user ir a Teacher and the qualification is his own
            boolean valid = false;

            if (id.hasRoleType(getRoleTypeTeacher())) {
                valid = true;
            }

            if (!valid) {
                throw new NotAuthorizedException();
            }
        } catch (RuntimeException e) {
            throw new NotAuthorizedException();
        }
    }

}