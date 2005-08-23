package net.sourceforge.fenixedu.applicationTier.Servico;

import java.io.Serializable;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.security.PasswordEncryptor;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.kerberos.UpdateKerberos;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Luis Cruz
 * 
 */
public class AuthenticateKerberos extends Authenticate implements IService, Serializable {

    public IUserView run(final String username, final String password, final String application,
            final String requestURL) throws ExcepcaoPersistencia, ExcepcaoAutenticacao {

        final ISuportePersistente persistenceSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final IPessoaPersistente persistentPerson = persistenceSupport.getIPessoaPersistente();

        final IPerson person = persistentPerson.lerPessoaPorUsername(username);
        if (person == null || !PasswordEncryptor.areEquals(person.getPassword(), password)) {
            throw new ExcepcaoAutenticacao("bad.authentication");
        }
        
        if(person.getIstUsername() != null && !person.getIsPassInKerberos()) {
        	try {
	        	UpdateKerberos.createUser(person.getIstUsername(), person.getPassword());
	        	person.setIsPassInKerberos(true);
        	} catch(Exception e) {
        		//for now, do nothing
        	}
        }
        
        final Set allowedRoles = getAllowedRolesByHostname(requestURL);
        final UserView userView = getUserView(person, allowedRoles);

        return userView;
    }

}