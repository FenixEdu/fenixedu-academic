package net.sourceforge.fenixedu.applicationTier.Servico.operator;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.security.PasswordEncryptor;
import net.sourceforge.fenixedu.applicationTier.utils.GeneratePassword;
import net.sourceforge.fenixedu.dataTransferObject.person.PasswordBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.candidacy.StudentCandidacy;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class GeneratePasswordsForCandidacies {

    @Atomic
    public static List<PasswordBean> run(final List<String> studentCandidacyIds) {
        check(RolePredicates.OPERATOR_PREDICATE);

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