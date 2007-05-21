package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.security.PasswordEncryptor;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.presentationTier.Action.publico.LoginRequestBean;

public class EnableExternalLogin extends Service {

	public void run(LoginRequestBean bean) {
		Person person = bean.getPerson();
		person.setGender(bean.getGender());
		person.setPassword(PasswordEncryptor.encryptPassword(bean.getPassword()));
	}
}
