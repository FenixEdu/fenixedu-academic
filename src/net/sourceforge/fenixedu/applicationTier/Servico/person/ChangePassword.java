package net.sourceforge.fenixedu.applicationTier.Servico.person;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidPasswordServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

public class ChangePassword extends Service {

    public void run(IUserView userView, String oldPassword, String newPassword) throws Exception{

        ISuportePersistente suportePersistente = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        Person person = suportePersistente.getIPessoaPersistente().lerPessoaPorUsername(
                userView.getUtilizador());
        try {
        	person.changePassword(oldPassword, newPassword);
        }catch(DomainException e) {
        	throw new InvalidPasswordServiceException(e.getKey());
        }
    }
}