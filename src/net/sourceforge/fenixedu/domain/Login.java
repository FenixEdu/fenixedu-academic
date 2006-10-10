package net.sourceforge.fenixedu.domain;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.util.UsernameUtils;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

public class Login extends Login_Base {

    protected Login() {
	super();
    }

    public Login(User user) {
	this();
	checkParameters(user);
	super.setUser(user);
	setBeginDateDateTime(new DateTime());
    }

    private void checkParameters(User user) {
	if (user.readUserLoginIdentification() != null) {
	    throw new DomainException("error.user.login.already.exists");
	}
	if (user == null) {
	    throw new DomainException("error.login.empty.user");
	}
    }

    public void delete() {
	for (; !getAlias().isEmpty(); getAlias().get(0).delete())
	    ;
	super.delete();
    }

    public boolean hasUsername(String username) {
	return getLoginAlias(username) != null;
    }

    public boolean isClosed() {
	return getEndDateDateTime() != null && getEndDateDateTime().isBefore(new DateTime());
    }

    public void openLogin() {
	if (isClosed()) {
	    setEndDateDateTime(null);
	}
    }

    public void closeLogin() {
	if (!isClosed()) {
	    setEndDateDateTime(new DateTime());
	}
    }

    public boolean isLogin() {
	return true;
    }

    public String getUsername() {
	if (isClosed()) {
	    return null;
	}
	String userUId = getUserUId();
	if (userUId == null) {
	    Role mostImportantRole = UsernameUtils.getMostImportantRole(getUser().getPerson()
		    .getPersonRoles());
	    if (mostImportantRole != null) {
		List<LoginAlias> loginAlias = getRoleLoginAlias(mostImportantRole.getRoleType());
		if (!loginAlias.isEmpty()) {
		    return loginAlias.get(0).getAlias();
		}
	    }
	    return (getAlias().isEmpty()) ? null : getAlias().get(0).getAlias();
	}
	return userUId;
    }

    public String getUserUId() {
	if (isClosed()) {
	    return null;
	}
	LoginAlias loginAliasByType = getInstitutionalLoginAlias();
	return (loginAliasByType != null) ? loginAliasByType.getAlias() : null;
    }

    public void setUsername(RoleType roleType) {
	String newUsername = UsernameUtils.generateNewUsername(roleType, getUser().getPerson());
	removeAlias(roleType);
	if (!StringUtils.isEmpty(newUsername)) {
	    LoginAlias.createNewRoleLoginAlias(this, newUsername, roleType);
	    openLogin();
	}
    }

    public void setUserUID() {
	final LoginAlias loginAlias = getInstitutionalLoginAlias();
	if (loginAlias == null) {
	    final String userUId = UsernameUtils.updateIstUsername(getUser().getPerson());
	    LoginAlias.createNewInstitutionalLoginAlias(this, userUId);
	    getUser().setUserUId(userUId);
	    openLogin();
	}
    }

    public LoginAlias getLoginAlias(String alias) {
	for (LoginAlias loginAlias : getAlias()) {
	    if (loginAlias.getAlias().equalsIgnoreCase(alias)) {
		return loginAlias;
	    }
	}
	return null;
    }

    public LoginAlias getInstitutionalLoginAlias() {
	for (LoginAlias loginAlias : getAlias()) {
	    if (loginAlias.getType().equals(LoginAliasType.INSTITUTION_ALIAS)) {
		return loginAlias;
	    }
	}
	return null;
    }

    public List<LoginAlias> getRoleLoginAlias(RoleType roleType) {
	List<LoginAlias> result = new ArrayList<LoginAlias>();
	for (LoginAlias loginAlias : getAlias()) {
	    if (loginAlias.getType().equals(LoginAliasType.ROLE_TYPE_ALIAS)
		    && loginAlias.getRoleType().equals(roleType)) {
		result.add(loginAlias);
	    }
	}
	return result;
    }

    /**
         * This map is a temporary solution until DML provides indexed
         * relations.
         * 
         */
    private static final Map<String, SoftReference<Login>> loginMap = new Hashtable<String, SoftReference<Login>>();

    public static Login readLoginByUsername(String username) {
	// Temporary solution until DML provides indexed relations.
	final String lowerCaseUsername = username.toLowerCase();
	final SoftReference<Login> loginReference = loginMap.get(lowerCaseUsername);
	if (loginReference != null) {
	    final Login login = loginReference.get();
	    if (login != null && login.getRootDomainObject() == RootDomainObject.getInstance()
		    && login.hasUsername(lowerCaseUsername)) {
		return login;
	    } else {
		loginMap.remove(lowerCaseUsername);
	    }
	}
	// *** end of hack

	for (final LoginAlias loginAlias : RootDomainObject.getInstance().getLoginAlias()) {
	    final String loginUsername = loginAlias.getAlias().toLowerCase();
	    // Temporary solution until DML provides indexed
	    // relations.
	    Login login = loginAlias.getLogin();
	    if (!loginMap.containsKey(loginUsername)) {
		loginMap.put(loginUsername, new SoftReference<Login>(login));
	    }
	    // *** end of hack
	    if (loginAlias.getAlias().equalsIgnoreCase(lowerCaseUsername)) {
		return login;
	    }
	}
	return null;
    }

    public void removeAlias(RoleType roleType) {
	if (roleType != null) {
	    for (final Iterator<LoginAlias> iter = getAlias().iterator(); iter.hasNext();) {
		final LoginAlias loginAlias = iter.next();
		if (loginAlias.getRoleType() == roleType) {
		    iter.remove();
		    loginAlias.delete();
		}
	    }
	}
    }
}
