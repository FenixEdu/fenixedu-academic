package net.sourceforge.fenixedu.applicationTier.Servico.operator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.security.PasswordEncryptor;
import net.sourceforge.fenixedu.applicationTier.utils.GeneratePassword;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;

public class GenerateNewStudentsPasswordsService extends Service {

    public List run(final Integer fromNumber, final Integer toNumber) {
	
	final List<InfoPerson> result = new ArrayList<InfoPerson>();
	
	for (final Registration registration : new HashSet<Registration>(Registration
		.readAllStudentsBetweenNumbers(fromNumber, toNumber))) {

	    if (registration.getPerson().hasRole(RoleType.FIRST_TIME_STUDENT)) {
		final String password = GeneratePassword.getInstance().generatePassword(
			registration.getPerson());
		registration.getPerson().setPassword(PasswordEncryptor.encryptPassword(password));
		result.add(InfoPerson.newInfoFromDomain(registration.getPerson()));
	    }
	}
	return result;
    }
}