package net.sourceforge.fenixedu.applicationTier.Servico.person;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidPasswordServiceException;

import org.fenixedu.bennu.core.domain.User;

import pt.ist.fenixframework.Atomic;

public class InitializePassword {

    @Atomic
    public static void run(User user, String password) throws InvalidPasswordServiceException, Exception {
        //TODO - verify if the password was allready initialized and throw exception
        user.changePassword(password);
    }
}
