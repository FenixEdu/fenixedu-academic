/*
 * Created on 26/Ago/2003, 9:14:57
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorAplicacao.Servico.Seminaries;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.Seminaries.InfoTheme;
import Dominio.Seminaries.ITheme;
import Dominio.Seminaries.Theme;
import ServidorApresentacao.Action.Seminaries.Exceptions.BDException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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
                ISuportePersistente persistenceSupport = SuportePersistenteOJB.getInstance();
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