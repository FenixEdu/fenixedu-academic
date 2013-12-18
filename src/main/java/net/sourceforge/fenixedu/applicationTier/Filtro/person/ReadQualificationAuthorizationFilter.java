package net.sourceforge.fenixedu.applicationTier.Filtro.person;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Qualification;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixframework.FenixFramework;

public class ReadQualificationAuthorizationFilter {

    public static final ReadQualificationAuthorizationFilter instance = new ReadQualificationAuthorizationFilter();

    public ReadQualificationAuthorizationFilter() {
    }

    protected RoleType getRoleTypeTeacher() {
        return RoleType.TEACHER;
    }

    protected RoleType getRoleTypeAlumni() {
        return RoleType.ALUMNI;
    }

    public void execute(String objectId) throws NotAuthorizedException {
        User id = Authenticate.getUser();
        try {
            boolean isNew = objectId == null;

            // Verify if needed fields are null
            if ((id == null) || (id.getPerson().getPersonRolesSet() == null)) {
                throw new NotAuthorizedException();
            }

            // Verify if:
            // 1: The user is a Grant Owner Manager and the qualification
            // belongs to a Grant Owner
            // 2: The user is a Teacher and the qualification is his own
            // 3: The user is an Alumni and the qualification is his own
            if (!isNew) {
                boolean valid = false;

                if (isOwnQualification(id, objectId)) {

                    if (id.getPerson().hasRole(getRoleTypeTeacher()) || id.getPerson().hasRole(getRoleTypeAlumni())) {
                        valid = true;
                    }
                }

                if (!valid) {
                    throw new NotAuthorizedException();
                }
            }
        } catch (RuntimeException e) {
            throw new NotAuthorizedException();
        }
    }

    private boolean isOwnQualification(User userView, String objectId) {
        final Qualification qualification = FenixFramework.getDomainObject(objectId);
        return qualification.getPerson() == userView.getPerson();
    }
}