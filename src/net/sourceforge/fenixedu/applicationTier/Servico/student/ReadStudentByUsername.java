package net.sourceforge.fenixedu.applicationTier.Servico.student;

/**
 * 
 * @author tfc130
 */
import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.student.Registration;

public class ReadStudentByUsername extends FenixService {

	public Object run(String username) {
		final Registration registration = Registration.readByUsername(username);
		return registration == null ? null : InfoStudent.newInfoFromDomain(registration);
	}

}