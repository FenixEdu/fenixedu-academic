package net.sourceforge.fenixedu.applicationTier.Servico.person;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidPasswordServiceException;
import net.sourceforge.fenixedu.predicates.RolePredicates;

import org.fenixedu.bennu.core.domain.User;

import pt.ist.fenixframework.Atomic;

public class ChangePassword {

    @Atomic
    public static void run(User user, String oldPassword, String newPassword) throws Exception {
        check(RolePredicates.PERSON_PREDICATE);
        if (user.matchesPassword(oldPassword)) {
            user.changePassword(newPassword);
        } else {
            throw new InvalidPasswordServiceException("change.passworld.invalid.old.password");
        }
    }
}
