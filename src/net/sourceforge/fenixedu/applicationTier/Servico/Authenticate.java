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
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Luis Cruz
 * 
 */
public class Authenticate implements IService, Serializable {

    protected static final Logger logger = Logger.getLogger(Authenticate.class);

    protected static final Map allowedRolesByHostname = new HashMap();

    static {
        final String propertiesFilename = "/.authenticationServiceHostnamesFiltering.properties";
        try {
            final Properties properties = new Properties();
            PropertiesManager.loadProperties(properties, propertiesFilename);
            for (final Iterator iterator = properties.entrySet().iterator(); iterator.hasNext();) {
                final Entry entry = (Entry) iterator.next();
                final String hostnameKey = (String) entry.getKey();
                final String rolesList = (String) entry.getValue();

                final String hostname = hostnameKey.substring(16);
                final String[] roles = rolesList.split(",");

                final Set rolesSet = new HashSet(roles.length);
                for (int i = 0; i < roles.length; i++) {
                    final RoleType roleType = RoleType.valueOf(roles[i].trim());
                    logger.info("Host: " + hostname + " provides role: " + roleType.toString() + '.');
                    rolesSet.add(roleType);
                }
                allowedRolesByHostname.put(hostname, rolesSet);
            }
        } catch (IOException e) {
            throw new RuntimeException("Unable to load " + propertiesFilename
                    + ". User authentication is therefor not possible.");
        }
    }

    protected class UserView implements IUserView {

    	final private DomainObjectReference<Person> personRef;

        final private Collection roles;

        private UserView(final IPerson person, final Set allowedRoles) {
            this.personRef = new DomainObjectReference<Person>((Person) person);

            final Collection infoRoles = getInfoRoles(person.getUsername(), person.getPersonRoles(),
                    allowedRoles);
            if (infoRoles != null) {
                final SortedSet rolesSet = new TreeSet(infoRoles);
                this.roles = Collections.unmodifiableSortedSet(rolesSet);
            } else {
                this.roles = null;
            }
        }

        public boolean hasRoleType(final RoleType roleType) {
            if (roles == null) {
                return false;
            }
            final InfoRole infoRole = new InfoRole(roleType);
            return roles.contains(infoRole);
        }

		public IPerson getPerson() {
			return personRef.get();
		}

        public String getUtilizador() {
            return getPerson().getUsername();
        }

        public Collection getRoles() {
            return roles;
        }

        public String getFullName() {
            return getPerson().getNome();
        }
    }

    public static final boolean isValidUserView(IUserView userView) {
        return userView instanceof UserView;
    }

    public IUserView run(final String username, final String password, final String application,
            final String requestURL) throws ExcepcaoPersistencia, ExcepcaoAutenticacao {

        final ISuportePersistente persistenceSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final IPessoaPersistente persistentPerson = persistenceSupport.getIPessoaPersistente();

        final IPerson person = persistentPerson.lerPessoaPorUsername(username);
        if (person == null || !PasswordEncryptor.areEquals(person.getPassword(), password)) {
            throw new ExcepcaoAutenticacao("bad.authentication");
        }
        
        final Set allowedRoles = getAllowedRolesByHostname(requestURL);
        return new UserView(person, allowedRoles);
    }

    protected Collection<InfoRole> getInfoRoles(final String username, final Collection personRoles,
            final Set allowedRoles) {
        final Map<RoleType, InfoRole> infoRoles = new HashMap<RoleType, InfoRole>(personRoles.size());
        for (final Iterator iterator = personRoles.iterator(); iterator.hasNext();) {
            final IRole role = (IRole) iterator.next();
            final RoleType roleType = role.getRoleType();
            if (allowedRoles.contains(roleType)) {
                final InfoRole infoRole = InfoRole.newInfoFromDomain(role);
                infoRoles.put(roleType, infoRole);
            }
        }
        filterRoles(infoRoles);
        return infoRoles.values();
    }

    protected Set getAllowedRolesByHostname(final String requestURL) {
        for (final Iterator iterator = allowedRolesByHostname.keySet().iterator(); iterator.hasNext();) {
            final String hostname = (String) iterator.next();
            if (StringUtils.substringAfter(requestURL, "://").startsWith(hostname)) {
                return (Set) allowedRolesByHostname.get(hostname);
            }
        }
        return new HashSet(0);
    }

    protected void filterRoles(final Map<RoleType, InfoRole> infoRoles) {
        filterEmployeeRoleForTeachers(infoRoles);
    }

    protected void filterEmployeeRoleForTeachers(Map<RoleType, InfoRole> infoRoles) {
        if (infoRoles.containsKey(RoleType.TEACHER) && infoRoles.containsKey(RoleType.EMPLOYEE)) {
            infoRoles.remove(RoleType.EMPLOYEE);
        }
    }

}