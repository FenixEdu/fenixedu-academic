/*
 * Created on 26/Ago/2003, 9:14:57
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.Seminaries;

import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoTheme;
import net.sourceforge.fenixedu.domain.Seminaries.Theme;
import net.sourceforge.fenixedu.domain.Seminaries.Theme;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.presentationTier.Action.Seminaries.Exceptions.BDException;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 26/Ago/2003, 9:14:57
 * 
 */
public class GetThemeById implements IService {

	public InfoTheme run(Integer themeID) throws BDException, ExcepcaoPersistencia {
		InfoTheme infoTheme = null;
		if (themeID != null) {

			ISuportePersistente persistenceSupport = PersistenceSupportFactory
					.getDefaultPersistenceSupport();
			IPersistentObject persistentObject = persistenceSupport.getIPersistentObject();
			Theme theme = (Theme) persistentObject.readByOID(Theme.class, themeID);

			infoTheme = InfoTheme.newInfoFromDomain(theme);

		}
		return infoTheme;
	}
}