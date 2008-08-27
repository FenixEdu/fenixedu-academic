package net.sourceforge.fenixedu.applicationTier.Filtro.person;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.Filtro;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Qualification;
import net.sourceforge.fenixedu.domain.person.RoleType;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

public class ReadQualificationAuthorizationFilter extends Filtro {

    public ReadQualificationAuthorizationFilter() {
    }

    protected RoleType getRoleTypeTeacher() {
	return RoleType.TEACHER;
    }

    protected RoleType getRoleTypeGrantOwnerManager() {
	return RoleType.GRANT_OWNER_MANAGER;
    }

    protected RoleType getRoleTypeAlumni() {
	return RoleType.ALUMNI;
    }

    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
	IUserView id = getRemoteUser(request);
	Object[] arguments = getServiceCallArguments(request);
	try {
	    boolean isNew = ((arguments[0] == null) || ((Integer) arguments[0]).equals(Integer.valueOf(0)));

	    // Verify if needed fields are null
	    if ((id == null) || (id.getRoleTypes() == null)) {
		throw new NotAuthorizedException();
	    }

	    Integer objectId = (Integer) arguments[0];

	    // Verify if:
	    // 1: The user is a Grant Owner Manager and the qualification
	    // belongs to a Grant Owner
	    // 2: The user is a Teacher and the qualification is his own
	    // 3: The user is an Alumni and the qualification is his own
	    if (!isNew) {
		boolean valid = false;

		if (id.hasRoleType(getRoleTypeGrantOwnerManager()) && isGrantOwner(objectId)) {
		    valid = true;
		}

		if (isOwnQualification(id, objectId)) {

		    if (id.hasRoleType(getRoleTypeTeacher()) || id.hasRoleType(getRoleTypeAlumni())) {
			valid = true;
		    }
		}

		if (!valid)
		    throw new NotAuthorizedException();
	    } else {
		if (!id.hasRoleType(getRoleTypeGrantOwnerManager()) && !id.hasRoleType(getRoleTypeTeacher()))
		    throw new NotAuthorizedException();
	    }
	} catch (RuntimeException e) {
	    throw new NotAuthorizedException();
	}
    }

    private boolean isGrantOwner(Integer objectId) {
	Qualification qualification = rootDomainObject.readQualificationByOID(objectId);
	return qualification.getPerson().hasGrantOwner();
    }

    private boolean isOwnQualification(IUserView userView, Integer objectId) {
	final Qualification qualification = rootDomainObject.readQualificationByOID(objectId);
	return qualification.getPerson() == userView.getPerson();
    }
}