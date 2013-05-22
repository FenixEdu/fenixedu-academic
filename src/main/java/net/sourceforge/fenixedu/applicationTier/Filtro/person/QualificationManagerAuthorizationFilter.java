/*
 * Created on 10/Nov/2003
 */

package net.sourceforge.fenixedu.applicationTier.Filtro.person;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.person.InfoQualification;
import net.sourceforge.fenixedu.domain.Qualification;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class QualificationManagerAuthorizationFilter {

    public static final QualificationManagerAuthorizationFilter instance = new QualificationManagerAuthorizationFilter();

    public QualificationManagerAuthorizationFilter() {
    }

    protected RoleType getRoleTypeTeacher() {
        return RoleType.TEACHER;
    }

    public void execute(Integer qualificationId, InfoQualification infoQualification) throws NotAuthorizedException {
        IUserView id = AccessControl.getUserView();

        try {
            // Verify if needed fields are null
            if ((id == null) || (id.getRoleTypes() == null)) {
                throw new NotAuthorizedException();
            }

            if (infoQualification == null) {
                throw new NotAuthorizedException();
            }

            boolean valid = false;
            // Verify if:
            // 2: The user ir a Teacher and the qualification is his own
            if (id.hasRoleType(getRoleTypeTeacher()) && isOwnQualification(id, infoQualification)) {
                valid = true;
            }

            if (!valid) {
                throw new NotAuthorizedException();
            }

        } catch (RuntimeException e) {
            throw new NotAuthorizedException();
        }
    }

    private boolean isOwnQualification(IUserView userView, InfoQualification infoQualification) {
        boolean isNew =
                (infoQualification.getIdInternal() == null) || (infoQualification.getIdInternal().equals(Integer.valueOf(0)));
        if (isNew) {
            return true;
        }
        final Qualification qualification =
                RootDomainObject.getInstance().readQualificationByOID(infoQualification.getIdInternal());
        return qualification.getPerson() == userView.getPerson();
    }
}