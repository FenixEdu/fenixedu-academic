/*
 * Created on 3/Set/2003, 16:21:47
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorAplicacao.Servico.Seminaries;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import DataBeans.Seminaries.InfoTheme;
import Dominio.Seminaries.ITheme;
import ServidorAplicacao.IServico;
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
public class GetAllThemes implements IServico
{
    private static GetAllThemes service= new GetAllThemes();
        /**
         * The singleton access method of this class.
         **/
        public static GetAllThemes getService()
        {
            return service;
        }
        /**
         * The actor of this class.
         **/
        private GetAllThemes()
        {
        }
        /**
         * Returns The Service Name */
        public final String getNome()
        {
            return "Seminaries.GetAllThemes";
        }
        public List run() throws BDException
        {
            List seminariesInfo= new LinkedList();
            try
            {
                ISuportePersistente persistenceSupport= SuportePersistenteOJB.getInstance();
                IPersistentSeminaryTheme persistentTheme= persistenceSupport.getIPersistentSeminaryTheme();
                List themes= persistentTheme.readAll();            
                for (Iterator iterator= themes.iterator(); iterator.hasNext();)
                {
                    //CLONER
                    //InfoTheme infoTheme= Cloner.copyITheme2InfoTheme((ITheme) iterator.next());
                    InfoTheme infoTheme= InfoTheme.newInfoFromDomain((ITheme) iterator.next());
                    
                    seminariesInfo.add(infoTheme);
                }
            }
            catch (ExcepcaoPersistencia ex)
            {
                throw new BDException("Got an error while trying to retrieve multiple themes from the database", ex);
            }
            return seminariesInfo;
        }
    
}
