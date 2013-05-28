/*
 * Created on 26/Ago/2003, 9:14:57
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.Seminaries;


import net.sourceforge.fenixedu.applicationTier.Filtro.SeminaryCoordinatorOrStudentFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoTheme;
import net.sourceforge.fenixedu.domain.Seminaries.Theme;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 *         Created at 26/Ago/2003, 9:14:57
 * 
 */
public class GetThemeById {

    protected InfoTheme run(Integer themeID) {
        InfoTheme infoTheme = null;
        if (themeID != null) {
            Theme theme = AbstractDomainObject.fromExternalId(themeID);
            infoTheme = InfoTheme.newInfoFromDomain(theme);

        }
        return infoTheme;
    }

    // Service Invokers migrated from Berserk

    private static final GetThemeById serviceInstance = new GetThemeById();

    @Service
    public static InfoTheme runGetThemeById(Integer themeID) throws NotAuthorizedException {
        SeminaryCoordinatorOrStudentFilter.instance.execute();
        return serviceInstance.run(themeID);
    }

}