package net.sourceforge.fenixedu.applicationTier.Servico;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.applicationTier.ICandidateView;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.security.PasswordEncryptor;
import net.sourceforge.fenixedu.dataTransferObject.InfoCandidateSituation;
import net.sourceforge.fenixedu.dataTransferObject.InfoRole;
import net.sourceforge.fenixedu.domain.ICandidateSituation;
import net.sourceforge.fenixedu.domain.IMasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IRole;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentMasterDegreeCandidate;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.State;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Luis Cruz
 * 
 */
public class Authenticate implements IService, Serializable {

	private static final Logger logger = Logger.getLogger(Authenticate.class);

	private static final Map allowedRolesByHostname = new HashMap();

	static {
		final String propertiesFilename = "/.authenticationServiceHostnamesFiltering.properties";
		try {
			final Properties properties = new Properties();
			PropertiesManager.loadProperties(properties, propertiesFilename);
			for (final Iterator iterator = properties.entrySet().iterator(); iterator
					.hasNext();) {
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

	private class UserView implements IUserView {

		final private String utilizador;

		final private Collection roles;

		private ICandidateView candidateView;

		private String fullName;

		private UserView(final String utilizador, final Collection roles) {
			super();
			this.utilizador = utilizador;
			if (roles != null) {
				final Set rolesSet = new HashSet(roles);
				this.roles = Collections.unmodifiableSet(rolesSet);
			} else {
				this.roles = null;
			}
		}

		public boolean hasRoleType(final RoleType roleType) {
			if (roles == null) {
				return false;
			}
			return roles.contains(roleType);
		}

		public String getUtilizador() {
			return utilizador;
		}

		public Collection getRoles() {
			return roles;
		}

		public ICandidateView getCandidateView() {
			return candidateView;
		}

		public void setCandidateView(final ICandidateView view) {
			candidateView = view;
		}

		public String getFullName() {
			return fullName;
		}

		public void setFullName(final String string) {
			fullName = string;
		}
	}

	public static final boolean isValidUserView(IUserView userView) {
		return userView instanceof UserView;
	}

	public IUserView run(final String username, final String password,
			final String application, final String requestURL)
			throws ExcepcaoPersistencia, ExcepcaoAutenticacao {

		final ISuportePersistente persistenceSupport = PersistenceSupportFactory
				.getDefaultPersistenceSupport();
		final IPessoaPersistente persistentPerson = persistenceSupport
				.getIPessoaPersistente();

		final IPerson person = persistentPerson.lerPessoaPorUsername(username);
		if (person == null
				|| !PasswordEncryptor.areEquals(person.getPassword(), password)) {
			throw new ExcepcaoAutenticacao("bad.authentication");
		}

		final Set allowedRoles = getAllowedRolesByHostname(requestURL);
		final UserView userView = getUserView(person, allowedRoles);

		setCandidateView(persistenceSupport, userView);

		return userView;
	}

	private UserView getUserView(final IPerson person, final Set allowedRoles) {
		final Collection infoRoles = getInfoRoles(person.getUsername(), person
				.getPersonRoles(), allowedRoles);

		final UserView userView = new UserView(person.getUsername(), infoRoles);
		userView.setFullName(person.getNome());

		return userView;
	}

	protected Collection<InfoRole> getInfoRoles(final String username,
			final Collection personRoles, final Set allowedRoles) {
		final Map<RoleType, InfoRole> infoRoles = new HashMap<RoleType, InfoRole>(
				personRoles.size());
		for (final Iterator iterator = personRoles.iterator(); iterator
				.hasNext();) {
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
		for (final Iterator iterator = allowedRolesByHostname.keySet().iterator(); iterator.hasNext(); ) {
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

	protected void filterEmployeeRoleForTeachers(
			Map<RoleType, InfoRole> infoRoles) {
		if (infoRoles.containsKey(RoleType.TEACHER)
				&& infoRoles.containsKey(RoleType.EMPLOYEE)) {
			infoRoles.remove(RoleType.EMPLOYEE);
		}
	}

	protected void setCandidateView(
			final ISuportePersistente persistenceSupport,
			final UserView userView) throws ExcepcaoPersistencia {
		if (userView.hasRoleType(RoleType.MASTER_DEGREE_CANDIDATE)) {
			final IPersistentMasterDegreeCandidate persistentMasterDegreeCandidate = persistenceSupport
					.getIPersistentMasterDegreeCandidate();

			final String username = userView.getUtilizador();

			final List masterDegreeCandidates = persistentMasterDegreeCandidate
					.readMasterDegreeCandidatesByUsername(username);

			final ICandidateView candidateView = getCandidateView(masterDegreeCandidates);

			userView.setCandidateView(candidateView);
		}
	}

	protected ICandidateView getCandidateView(final List masterDegreeCandidates) {
		final List infoSituations = new ArrayList();
		for (final Iterator iterator = masterDegreeCandidates.iterator(); iterator
				.hasNext();) {
			final IMasterDegreeCandidate masterDegreeCandidate = (IMasterDegreeCandidate) iterator
					.next();
			final List situations = masterDegreeCandidate.getSituations();
			for (final Iterator iterator2 = situations.iterator(); iterator2
					.hasNext();) {
				final ICandidateSituation candidateSituation = (ICandidateSituation) iterator2
						.next();
				if (candidateSituation.getValidation().equals(
						new State(State.ACTIVE))) {
					final InfoCandidateSituation infoCandidateSituation = InfoCandidateSituation
							.newInfoFromDomain(candidateSituation);
					infoSituations.add(infoCandidateSituation);
				}
			}
		}
		return new CandidateView(infoSituations);
	}

}