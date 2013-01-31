package net.sourceforge.fenixedu.applicationTier.Servico.operator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.security.PasswordEncryptor;
import net.sourceforge.fenixedu.applicationTier.utils.GeneratePassword;
import net.sourceforge.fenixedu.dataTransferObject.person.PasswordBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.candidacy.StudentCandidacy;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class GeneratePasswordsForCandidacies extends FenixService {

	@Checked("RolePredicates.OPERATOR_PREDICATE")
	@Service
	public static List<PasswordBean> run(final List<Integer> studentCandidacyIds) {

		final Set<StudentCandidacy> studentCandidacies = StudentCandidacy.readByIds(studentCandidacyIds);
		final List<PasswordBean> result = new ArrayList<PasswordBean>();
		for (final StudentCandidacy studentCandidacy : studentCandidacies) {
			final Person person = studentCandidacy.getPerson();
			final String newPassword = GeneratePassword.getInstance().generatePassword(person);
			person.setPassword(PasswordEncryptor.encryptPassword(newPassword));

			result.add(new PasswordBean(person, newPassword));
		}

		return result;
	}
}