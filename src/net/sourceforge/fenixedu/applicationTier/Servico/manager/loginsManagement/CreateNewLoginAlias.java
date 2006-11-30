package net.sourceforge.fenixedu.applicationTier.Servico.manager.loginsManagement;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.manager.loginsManagement.LoginAliasBean;
import net.sourceforge.fenixedu.domain.LoginAlias;
import net.sourceforge.fenixedu.domain.LoginAliasType;

public class CreateNewLoginAlias extends Service {

    public void run(LoginAliasBean bean) {
	if (bean != null) {
	    if (bean.getLoginAliasType().equals(LoginAliasType.CUSTOM_ALIAS)) {
		LoginAlias.createNewCustomLoginAlias(bean.getLogin(), bean.getAlias());
	    } else if (bean.getLoginAliasType().equals(LoginAliasType.ROLE_TYPE_ALIAS)) {
		LoginAlias.createNewRoleLoginAlias(bean.getLogin(), bean.getAlias(), bean.getRoleType());
	    }
	}
    }

}
