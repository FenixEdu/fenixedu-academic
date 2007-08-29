package net.sourceforge.fenixedu.domain;

import java.lang.ref.SoftReference;
import java.text.Collator;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;

public class LoginAlias extends LoginAlias_Base {

    public final static Comparator<LoginAlias> COMPARATOR_BY_TYPE_AND_ROLE_TYPE_AND_ALIAS = new ComparatorChain();
    static {
	((ComparatorChain) COMPARATOR_BY_TYPE_AND_ROLE_TYPE_AND_ALIAS).addComparator(new BeanComparator("type"));
	((ComparatorChain) COMPARATOR_BY_TYPE_AND_ROLE_TYPE_AND_ALIAS).addComparator(new Comparator<LoginAlias>() {
	    public int compare(LoginAlias loginAlias1, LoginAlias loginAlias2) {		
		if (loginAlias1.getRoleType() != null && loginAlias2.getRoleType() != null) {
		    List<RoleType> rolesImportance = RoleType.getRolesImportance();
		    Integer indexOfRoleType1 = Integer.valueOf(rolesImportance.indexOf(loginAlias1.getRoleType()));
		    Integer indexOfRoleType2 = Integer.valueOf(rolesImportance.indexOf(loginAlias2.getRoleType()));
		    return indexOfRoleType1.compareTo(indexOfRoleType2);
		} else if (loginAlias1.getRoleType() != null && loginAlias2.getRoleType() == null) {
		    return 1;
		} else if (loginAlias1.getRoleType() == null && loginAlias2.getRoleType() != null) {
		    return -1;
		}
		return 0;
	    }
	});
	((ComparatorChain) COMPARATOR_BY_TYPE_AND_ROLE_TYPE_AND_ALIAS).addComparator(new BeanComparator("alias", Collator.getInstance()));
    }
        
    public static void createNewCustomLoginAlias(Login login, String alias) {
	new LoginAlias(login, alias, LoginAliasType.CUSTOM_ALIAS);
    }

    public static void createNewInstitutionalLoginAlias(Login login, String alias) {
	new LoginAlias(login, alias, LoginAliasType.INSTITUTION_ALIAS);
    }

    public static void createNewRoleLoginAlias(Login login, String alias, RoleType roleType) {
	new LoginAlias(login, alias, roleType, LoginAliasType.ROLE_TYPE_ALIAS);
    }

    private LoginAlias(Login login, String alias, LoginAliasType loginAliasType) {
	
	super();
	checkIfAliasAlreadyExists(alias, login);
	if (loginAliasType != null && loginAliasType.equals(LoginAliasType.INSTITUTION_ALIAS)) {
	    checkInstitutionalAliasType(login);
	}

	setLogin(login);
	setAlias(alias);
	setType(loginAliasType);
	setRootDomainObject(RootDomainObject.getInstance());
    }

    private LoginAlias(Login login, String alias, RoleType roleType, LoginAliasType loginAliasType) {
	
	super();
	checkIfAliasAlreadyExists(alias, login);
	checkRoleTypeAlias(login, roleType);

	setLogin(login);
	setAlias(alias);
	setType(loginAliasType);
	setRoleType(roleType);
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public void editCustomAlias(String alias) {
	if(getType().equals(LoginAliasType.CUSTOM_ALIAS)) {
	   checkIfAliasAlreadyExists(alias, getLogin());
	   setAlias(alias);
	}
    }
    
    public void delete() {	
	super.setLogin(null);
	removeRootDomainObject();
	super.deleteDomainObject();
    }

    @jvstm.cps.ConsistencyPredicate
    protected boolean checkRequiredParameters() {
	return getType() != null && !StringUtils.isEmpty(getAlias()); 	
    }  

    @Override
    public void setLogin(Login login) {
	if (login == null) {
	    throw new DomainException("error.loginAlias.no.login");
	}
	super.setLogin(login);
    }

    @Override
    public void setAlias(String alias) {
	if (alias == null || StringUtils.isEmpty(alias.trim())) {
	    throw new DomainException("error.loginAlias.no.alias");
	}
	super.setAlias(alias);
    }

    @Override
    public void setType(LoginAliasType loginAliasType) {
	if (loginAliasType == null) {
	    throw new DomainException("error.alias.no.alias.type");
	}
	super.setType(loginAliasType);
    }
    
    @Override
    public void setRoleType(RoleType roleType) {
	if (roleType == null) {
	    throw new DomainException("error.alias.empty.roleType");
	}
	super.setRoleType(roleType);
    }

    private void checkRoleTypeAlias(Login login, RoleType roleType) {
	if (login != null && roleType != null && !login.getRoleLoginAlias(roleType).isEmpty()) {
	    throw new DomainException("error.ROLE_TYPE_ALIAS.already.exists");
	}
    }

    private void checkInstitutionalAliasType(Login login) {
	if (login != null && login.getInstitutionalLoginAlias() != null) {
	    throw new DomainException("error.INSTITUTION_ALIAS.already.exists");
	}
    }

    private void checkIfAliasAlreadyExists(String username, Login login) {
	if (login != null && username != null) {
	    final LoginAlias loginAlias = readLoginByUsername(username);
	    if (loginAlias != null && loginAlias.getLogin() != login) {
		throw new DomainException("error.alias.already.exists");
	    }
	}
    }
    
    /**
     * This map is a temporary solution until DML provides indexed
     * relations.
     * 
     */
    private static final Map<String, SoftReference<LoginAlias>> loginAliasMap = new Hashtable<String, SoftReference<LoginAlias>>();

    public static LoginAlias readLoginByUsername(final String username) {
	// Temporary solution until DML provides indexed relations.
	final String lowerCaseUsername = username.toLowerCase();
	final SoftReference<LoginAlias> loginAliasReference = loginAliasMap.get(lowerCaseUsername);
	if (loginAliasReference != null) {
	    final LoginAlias loginAlias = loginAliasReference.get();
	    try {
	        if (loginAlias != null && loginAlias.getRootDomainObject() == RootDomainObject.getInstance()
	                && loginAlias.getLogin() != null && loginAlias.getLogin().hasUsername(lowerCaseUsername)) {
	            return loginAlias;
	        } else {
	            loginAliasMap.remove(lowerCaseUsername);
	        }
	    } catch (NullPointerException npe) {
	        loginAliasMap.remove(lowerCaseUsername);
	    }
	}
	// *** end of hack

	for (final LoginAlias loginAlias : RootDomainObject.getInstance().getLoginAlias()) {
	    // Temporary solution until DML provides indexed relations.
	    final String lowerCaseLoginUsername = loginAlias.getAlias().toLowerCase();
	    if (!loginAliasMap.containsKey(lowerCaseLoginUsername)) {
		loginAliasMap.put(lowerCaseLoginUsername, new SoftReference<LoginAlias>(loginAlias));
	    }
	    // *** end of hack

	    if (lowerCaseLoginUsername.equalsIgnoreCase(lowerCaseUsername)) {
		return loginAlias;
	    }
	}
	return null;
    } 
}
