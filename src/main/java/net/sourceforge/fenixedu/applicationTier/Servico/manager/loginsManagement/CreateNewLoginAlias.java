package net.sourceforge.fenixedu.applicationTier.Servico.manager.loginsManagement;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Filtro.ManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.OperatorAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.manager.loginsManagement.LoginAliasBean;
import net.sourceforge.fenixedu.domain.LoginAlias;
import net.sourceforge.fenixedu.domain.LoginAliasType;
import pt.ist.fenixWebFramework.services.Service;

public class CreateNewLoginAlias extends FenixService {

    protected void run(LoginAliasBean bean) {
        if (bean != null) {
            if (bean.getLoginAliasType().equals(LoginAliasType.CUSTOM_ALIAS)) {
                LoginAlias.createNewCustomLoginAlias(bean.getLogin(), bean.getAlias());
            } else if (bean.getLoginAliasType().equals(LoginAliasType.ROLE_TYPE_ALIAS)) {
                LoginAlias.createNewRoleLoginAlias(bean.getLogin(), bean.getAlias(), bean.getRoleType());
            } else if (bean.getLoginAliasType().equals(LoginAliasType.INSTITUTION_ALIAS)) {
                bean.getLogin().getUser().getPerson().setIstUsername();
            }
        }
    }

    // Service Invokers migrated from Berserk

    private static final CreateNewLoginAlias serviceInstance = new CreateNewLoginAlias();

    @Service
    public static void runCreateNewLoginAlias(LoginAliasBean bean) throws NotAuthorizedException {
        try {
            ManagerAuthorizationFilter.instance.execute();
            serviceInstance.run(bean);
        } catch (NotAuthorizedException ex1) {
            try {
                OperatorAuthorizationFilter.instance.execute();
                serviceInstance.run(bean);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

}