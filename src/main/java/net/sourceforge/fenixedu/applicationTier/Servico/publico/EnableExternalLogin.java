package net.sourceforge.fenixedu.applicationTier.Servico.publico;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.security.PasswordEncryptor;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.presentationTier.Action.publico.LoginRequestBean;
import pt.ist.fenixWebFramework.services.Service;

public class EnableExternalLogin {

    @Service
    public static void run(LoginRequestBean bean) throws FenixServiceException {
        Person person = bean.getPerson();
        if (person.getUser().getLoginRequest() != null) {
            person.setGender(bean.getGender());
            person.setDefaultPhoneNumber(bean.getPhone());
            person.setPassword(PasswordEncryptor.encryptPassword(bean.getPassword()));
            person.getUser().getLoginRequest().delete();
        } else {
            throw new FenixServiceException("error.request.already.used");
        }
    }
}