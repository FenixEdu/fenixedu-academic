package net.sourceforge.fenixedu.applicationTier.Filtro.person;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Qualification;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

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

    public void execute(Integer objectId) throws NotAuthorizedException {
        IUserView id = AccessControl.getUserView();
        try {
            boolean isNew = (objectId == null) || objectId.equals(Integer.valueOf(0));

            // Verify if needed fields are null
            if ((id == null) || (id.getRoleTypes() == null)) {
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

                    if (id.hasRoleType(getRoleTypeTeacher()) || id.hasRoleType(getRoleTypeAlumni())) {
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

    private boolean isOwnQualification(IUserView userView, Integer objectId) {
        final Qualification qualification = AbstractDomainObject.fromExternalId(objectId);
        return qualification.getPerson() == userView.getPerson();
    }
}