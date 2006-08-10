/*
 * Created on 10/Nov/2003
 */

package net.sourceforge.fenixedu.applicationTier.Filtro.person;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.Filtro;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.person.InfoQualification;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Qualification;
import net.sourceforge.fenixedu.domain.person.RoleType;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

public class QualificationManagerAuthorizationFilter extends Filtro {

    public QualificationManagerAuthorizationFilter() {
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
            // Verify if needed fields are null
            if ((id == null) || (id.getRoleTypes() == null)) {
                throw new NotAuthorizedException();
            }

            if (!id.hasRoleType(getRoleTypeGrantOwnerManager()) && !id.hasRoleType(getRoleTypeTeacher()))
                throw new NotAuthorizedException();

            InfoQualification infoQualification = null;

            // New Qualification, second argument is a qualification
            infoQualification = (InfoQualification) arguments[1];
            if (infoQualification == null)
                throw new NotAuthorizedException();

            boolean valid = false;
            // Verify if:
            // 1: The user ir a Grant Owner Manager and the qualification
            // belongs to a Grant Owner
            // 2: The user ir a Teacher and the qualification is his own
            if (id.hasRoleType(getRoleTypeGrantOwnerManager()) && isGrantOwner(infoQualification))
                valid = true;

            if (id.hasRoleType(getRoleTypeTeacher()) && isOwnQualification(id.getUtilizador(), infoQualification))
                valid = true;

            if (!valid)
                throw new NotAuthorizedException();

        } catch (RuntimeException e) {
            throw new NotAuthorizedException();
        }
    }

    private boolean isGrantOwner(InfoQualification infoQualification) {
        try {
            Person person = (Person) rootDomainObject.readPartyByOID(infoQualification.getInfoPerson()
                    .getIdInternal());
            return person.hasGrantOwner();
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isOwnQualification(String username, InfoQualification infoQualification) {
        boolean isNew = (infoQualification.getIdInternal() == null)
                || (infoQualification.getIdInternal().equals(Integer.valueOf(0)));
        if (isNew) {
            return true;
        }
        final Qualification qualification = rootDomainObject.readQualificationByOID(infoQualification
                .getIdInternal());
        return qualification.getPerson() == Person.readPersonByUsername(username);
    }
}