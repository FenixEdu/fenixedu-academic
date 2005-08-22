package net.sourceforge.fenixedu.applicationTier.Servico;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Map.Entry;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.security.PasswordEncryptor;
import net.sourceforge.fenixedu.dataTransferObject.InfoRole;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IRole;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.kerberos.UpdateKerberos;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

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
        
        if(!person.getIsPassInKerberos()) {
        	UpdateKerberos.changeKerberosPass(username, password);
        	person.setIsPassInKerberos(true);
        }
        
        final Set allowedRoles = getAllowedRolesByHostname(requestURL);
        final UserView userView = getUserView(person, allowedRoles);

        return userView;
    }

}