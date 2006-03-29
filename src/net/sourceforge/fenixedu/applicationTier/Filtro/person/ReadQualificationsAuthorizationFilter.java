package net.sourceforge.fenixedu.applicationTier.Filtro.person;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.AuthorizationUtils;
import net.sourceforge.fenixedu.applicationTier.Filtro.Filtro;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

public class ReadQualificationsAuthorizationFilter extends Filtro {

    public ReadQualificationsAuthorizationFilter() {
    }

    protected RoleType getRoleTypeTeacher() {
        return RoleType.TEACHER;
    }

    protected RoleType getRoleTypeGrantOwnerManager() {
        return RoleType.GRANT_OWNER_MANAGER;
    }

    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
        IUserView id = getRemoteUser(request);
        Object[] arguments = getServiceCallArguments(request);
        try {
            //Verify if needed fields are null
            if ((id == null) || (id.getRoles() == null)) {
                throw new NotAuthorizedException();
            }

            //Verify if:
            // 1: The user ir a Grant Owner Manager and the qualification
            // belongs to a Grant Owner
            // 2: The user ir a Teacher and the qualification is his own
            boolean valid = false;

            if ((AuthorizationUtils.containsRole(id.getRoles(), getRoleTypeGrantOwnerManager()))
                    && isGrantOwner((String) arguments[0])) {
                valid = true;
            }

            if (AuthorizationUtils.containsRole(id.getRoles(), getRoleTypeTeacher())) {
                valid = true;
            }

            if (!valid)
                throw new NotAuthorizedException();
        } catch (RuntimeException e) {
            throw new NotAuthorizedException();
        }
    }

    private boolean isGrantOwner(String user) {
        return Person.readPersonByUsername(user).hasGrantOwner();
    }

}