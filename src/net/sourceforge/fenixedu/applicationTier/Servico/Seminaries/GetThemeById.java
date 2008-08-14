/*
 * Created on 26/Ago/2003, 9:14:57
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.Seminaries;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoTheme;
import net.sourceforge.fenixedu.domain.Seminaries.Theme;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 *         Created at 26/Ago/2003, 9:14:57
 * 
 */
public class GetThemeById extends Service {

    public InfoTheme run(Integer themeID) {
	InfoTheme infoTheme = null;
	if (themeID != null) {
	    Theme theme = rootDomainObject.readThemeByOID(themeID);
	    infoTheme = InfoTheme.newInfoFromDomain(theme);

	}
	return infoTheme;
    }
}