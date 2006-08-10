package net.sourceforge.fenixedu.applicationTier.Servico;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.security.PasswordEncryptor;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.cas.CASServiceUrlProvider;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import edu.yale.its.tp.cas.client.CASAuthenticationException;
import edu.yale.its.tp.cas.client.CASReceipt;
import edu.yale.its.tp.cas.client.ProxyTicketValidator;

/**
 * @author Luis Cruz
 * 
 */
public class Authenticate extends Service implements Serializable {

    private static final String URL_ENCODING = "UTF-8";

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

        final private DomainReference<Person> personRef;

        final private Collection<RoleType> roleTypes;

        private transient Collection<Role> roles;

        private UserView(final Person person, final Set allowedRoles) {
            this.personRef = new DomainReference<Person>((Person) person);

            final Collection<Role> roles = getInfoRoles(person.getUsername(), person.getPersonRoles(),
                    allowedRoles);
            if (roles != null) {
                final SortedSet<RoleType> rolesSet = new TreeSet<RoleType>();
                for (final Role role : roles) {
                    rolesSet.add(role.getRoleType());
                }
                this.roleTypes = Collections.unmodifiableSortedSet(rolesSet);
            } else {
                this.roleTypes = null;
            }
        }

        public boolean hasRoleType(final RoleType roleType) {
            return roleTypes == null ? false : roleTypes.contains(roleType);
        }

        public Person getPerson() {
            return personRef.getObject();
        }

        public String getUtilizador() {
            return getPerson().getUsername();
        }

        public Collection<RoleType> getRoleTypes() {
            return roleTypes;
        }

        public String getFullName() {
            return getPerson().getNome();
        }

        public boolean isPublicRequester() {
            return false;
        }
    }

    public static final boolean isValidUserView(IUserView userView) {
        return userView instanceof UserView;
    }

    public IUserView run(final String username, final String password, final String requestURL,
            final String remoteHost) throws ExcepcaoAutenticacao, ExcepcaoPersistencia,
            FenixServiceException {

        Person person = Person.readPersonByUsername(username);

        if (person == null) {
            person = Person.readPersonByIstUsername(username);
        }

        if (person == null || !PasswordEncryptor.areEquals(person.getPassword(), password)) {
            throw new ExcepcaoAutenticacao("bad.authentication");
        }

        setLoginHostNameAndDateTime(remoteHost, person);

        return getUserView(person, requestURL);
    }

    protected IUserView getUserView(final Person person, final String requestURL) {
        final Set allowedRoles = getAllowedRolesByHostname(requestURL);
        return new UserView(person, allowedRoles);
    }

    public IUserView run(final String casTicket, final String requestURL, final String remoteHost)
            throws ExcepcaoPersistencia, ExcepcaoAutenticacao {
        final CASReceipt receipt = getCASReceipt(casTicket, requestURL);

        if (receipt == null) {
            throw new ExcepcaoAutenticacao("bad.authentication");
        }

        final String username = receipt.getUserName();
        Person person = Person.readPersonByUsername(username);

        if (person == null) {
            person = Person.readPersonByIstUsername(username);
        }

        if (person == null) {
            throw new ExcepcaoAutenticacao("error.Exception");
        }
      
        setLoginHostNameAndDateTime(remoteHost, person);

        return getUserView(person, requestURL);
    }

    private void setLoginHostNameAndDateTime(final String remoteHost, Person person) {
        User user = person.getUser();
        user.setLastLoginHost(user.getCurrentLoginHost());
        user.setLastLoginDateTimeDateTime(user.getCurrentLoginDateTimeDateTime());
        user.setCurrentLoginDateTimeDateTime(new DateTime());
        user.setCurrentLoginHost(remoteHost);               
    }

    private CASReceipt getCASReceipt(final String casTicket, final String requestURL)
            throws ExcepcaoAutenticacao {
        CASReceipt receipt = null;

        try {
            final String casValidateUrl = PropertiesManager.getProperty("cas.validateUrl");
            final String casServiceUrl = URLEncoder.encode(CASServiceUrlProvider
                    .getServiceUrl(requestURL), URL_ENCODING);

            ProxyTicketValidator pv = new ProxyTicketValidator();
            pv.setCasValidateUrl(casValidateUrl);
            pv.setServiceTicket(casTicket);
            pv.setService(casServiceUrl);
            pv.setRenew(false);

            receipt = CASReceipt.getReceipt(pv);

        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (CASAuthenticationException e) {
            throw new ExcepcaoAutenticacao("bad.authentication", e);
        }

        return receipt;
    }

    protected Collection<Role> getInfoRoles(final String username, final Collection personRoles,
            final Set allowedRoles) {
        final Map<RoleType, Role> infoRoles = new HashMap<RoleType, Role>(personRoles.size());
        for (final Iterator iterator = personRoles.iterator(); iterator.hasNext();) {
            final Role role = (Role) iterator.next();
            final RoleType roleType = role.getRoleType();
            if (allowedRoles.contains(roleType)) {
                infoRoles.put(roleType, role);
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

    protected void filterRoles(final Map<RoleType, Role> infoRoles) {
        filterEmployeeRoleForTeachers(infoRoles);
    }

    protected void filterEmployeeRoleForTeachers(Map<RoleType, Role> infoRoles) {
        if (infoRoles.containsKey(RoleType.TEACHER) && infoRoles.containsKey(RoleType.EMPLOYEE)) {
            infoRoles.remove(RoleType.EMPLOYEE);
        }
    }

}