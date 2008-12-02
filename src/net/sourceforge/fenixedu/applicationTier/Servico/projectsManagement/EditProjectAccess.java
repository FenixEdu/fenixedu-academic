package net.sourceforge.fenixedu.applicationTier.Servico.projectsManagement;

import java.util.Calendar;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.projectsManagement.ProjectAccess;

public class EditProjectAccess extends FenixService {

    public void run(String username, String costCenter, Integer personId, Integer projectCode, Calendar beginDate,
	    Calendar endDate, Boolean it, String userNumber) throws FenixServiceException {

	Person person = (Person) rootDomainObject.readPartyByOID(personId);
	final ProjectAccess projectAccess = ProjectAccess.getByPersonAndProject(person, projectCode, it);

	if (projectAccess == null) {
	    throw new InvalidArgumentsServiceException("project not found");
	}

	if (!projectAccess.getEnd().after(Calendar.getInstance().getTime())) {
	    throw new InvalidArgumentsServiceException("end date has passed");
	}

	projectAccess.setBeginDate(beginDate);
	projectAccess.setEndDate(endDate);
    }

}
