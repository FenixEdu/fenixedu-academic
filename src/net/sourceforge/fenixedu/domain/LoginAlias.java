package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.apache.commons.lang.StringUtils;

public class LoginAlias extends LoginAlias_Base {

    private LoginAlias() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    private LoginAlias(Login login, String alias, LoginAliasType loginAliasType) {
	this();
	checkParameters(login, alias, loginAliasType, null);
	setLogin(login);
	super.setAlias(alias);
	super.setType(loginAliasType);
    }

    private LoginAlias(Login login, String alias, RoleType roleType) {
	this();
	LoginAliasType loginAliasType = LoginAliasType.ROLE_TYPE_ALIAS;
	checkParameters(login, alias, loginAliasType, roleType);
	setLogin(login);
	super.setAlias(alias);
	super.setType(loginAliasType);
	super.setRoleType(roleType);
    }

    public static void createNewCustomLoginAlias(Login login, String alias) {
	new LoginAlias(login, alias, LoginAliasType.CUSTOM_ALIAS);
    }

    public static void createNewInstitutionalLoginAlias(Login login, String alias) {
	new LoginAlias(login, alias, LoginAliasType.INSTITUTION_ALIAS);
    }

    public static void createNewRoleLoginAlias(Login login, String alias, RoleType roleType) {
	new LoginAlias(login, alias, roleType);
    }

    public void delete() {
	removeLogin();		
	removeRootDomainObject();
	deleteDomainObject();
    }

    private void checkParameters(Login login, String alias, LoginAliasType aliasType, RoleType roleType) {
	if (StringUtils.isEmpty(alias)) {
	    throw new DomainException("error.loginAlias.no.alias");
	}
	if (login == null) {
	    throw new DomainException("error.loginAlias.no.login");
	}
	if (aliasType == null) {
	    throw new DomainException("error.alias.no.alias.type");
	}
	if (checkIfAliasExists(alias, login)) {
	    throw new DomainException("error.alias.already.exists");
	}
	if (aliasType.equals(LoginAliasType.INSTITUTION_ALIAS)
		&& login.getInstitutionalLoginAlias() != null) {
	    throw new DomainException("error.INSTITUTION_ALIAS.already.exists");
	}
	if (aliasType.equals(LoginAliasType.ROLE_TYPE_ALIAS) && roleType == null) {
	    throw new DomainException("error.alias.no.role.type");
	}
	if (aliasType.equals(LoginAliasType.ROLE_TYPE_ALIAS)
		&& !login.getRoleLoginAlias(roleType).isEmpty()) {
	    throw new DomainException("error.ROLE_TYPE_ALIAS.already.exists");
	}
    }

    private boolean checkIfAliasExists(String username, Login login) {
	for (LoginAlias loginAlias : RootDomainObject.getInstance().getLoginAlias()) {
	    if (username.equalsIgnoreCase(loginAlias.getAlias()) && loginAlias != this
		    && !loginAlias.getLogin().equals(login)) {
		return true;
	    }
	}
	return false;
    }

    @Override
    public void setAlias(String alias) {
	throw new DomainException("error.impossible.to.edit.alias");
    }

    @Override
    public void setRoleType(RoleType roleType) {
	throw new DomainException("error.impossible.to.edit.roleType");
    }

    @Override
    public void setType(LoginAliasType loginAliasType) {
	throw new DomainException("error.impossible.to.edit.loginAliasType");
    }
}
