package net.sourceforge.fenixedu.dataTransferObject.manager.loginsManagement;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Login;
import net.sourceforge.fenixedu.domain.LoginAliasType;
import net.sourceforge.fenixedu.domain.person.RoleType;

public class LoginAliasBean implements Serializable{

    private DomainReference<Login> loginReference;
    
    private String alias;
    
    private RoleType roleType;
    
    private LoginAliasType loginAliasType;

        
    public LoginAliasBean(Login login, LoginAliasType type) {
	setLogin(login);
	setLoginAliasType(type);
    }
        
    public Login getLogin() {
	return loginReference == null ? null : loginReference.getObject();
    }

    public void setLogin(Login login) {
	this.loginReference = login == null ? null : new DomainReference<Login>(login);
    }      
    
    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public LoginAliasType getLoginAliasType() {
        return loginAliasType;
    }

    public void setLoginAliasType(LoginAliasType loginAliasType) {
        this.loginAliasType = loginAliasType;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }
}
