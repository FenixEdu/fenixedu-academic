/*
 * Created on 3/Set/2003, 16:21:47
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorAplicacao.Servico.Seminaries;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.Seminaries.InfoTheme;
import Dominio.Seminaries.ITheme;
import ServidorApresentacao.Action.Seminaries.Exceptions.BDException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.Seminaries.IPersistentSeminaryTheme;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 3/Set/2003, 16:21:47
 *  
 */
public class GetAllThemes implements IService {

    public GetAllThemes() {
    }

    public List run() throws BDException {
        List seminariesInfo = new LinkedList();
        try {
            ISuportePersistente persistenceSupport = SuportePersistenteOJB.getInstance();
            IPersistentSeminaryTheme persistentTheme = persistenceSupport.getIPersistentSeminaryTheme();
            List themes = persistentTheme.readAll();
            for (Iterator iterator = themes.iterator(); iterator.hasNext();) {

                InfoTheme infoTheme = InfoTheme.newInfoFromDomain((ITheme) iterator.next());

                seminariesInfo.add(infoTheme);
            }
        } catch (ExcepcaoPersistencia ex) {
            throw new BDException(
                    "Got an error while trying to retrieve multiple themes from the database", ex);
        }
        return seminariesInfo;
    }

}