/*
 * Created on 26/Ago/2003, 9:14:57
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.Seminaries;

import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoTheme;
import net.sourceforge.fenixedu.domain.Seminaries.ITheme;
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

    public GetThemeById() {
    }

    public InfoTheme run(Integer themeID) throws BDException {
        InfoTheme infoTheme = null;
        if (themeID != null) {
            try {
                ISuportePersistente persistenceSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
                IPersistentObject persistentObject = persistenceSupport.getIPersistentObject();
                ITheme theme = (ITheme) persistentObject.readByOID(Theme.class, themeID);

                infoTheme = InfoTheme.newInfoFromDomain(theme);
            } catch (ExcepcaoPersistencia ex) {
                throw new BDException(
                        "Got an error while trying to retrieve mutiple case studies from the database",
                        ex);
            }
        }
        return infoTheme;
    }
}