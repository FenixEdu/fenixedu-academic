/*
 * Created on 5/Ago/2003, 19:44:39
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorAplicacao.Servico.Seminaries;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import DataBeans.Seminaries.InfoCandidacy;
import Dominio.Seminaries.ICandidacy;
import Dominio.Seminaries.ISeminary;
import Dominio.Seminaries.Seminary;
import ServidorAplicacao.IServico;
import ServidorApresentacao.Action.Seminaries.Exceptions.BDException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.Seminaries.IPersistentSeminary;
import ServidorPersistente.Seminaries.IPersistentSeminaryCandidacy;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 *
 * 
 * Created at 5/Ago/2003, 19:44:39
 * 
 */
public class GetCandidaciesByStudentID implements IServico
{
    private static GetCandidaciesByStudentID service= new GetCandidaciesByStudentID();
        /**
         * The singleton access method of this class.
         **/
        public static GetCandidaciesByStudentID getService()
        {
            return service;
        }
        /**
         * The actor of this class.
         **/
        private GetCandidaciesByStudentID()
        {
        }
        /**
         * Returns The Service Name */
        public final String getNome()
        {
            return "Seminaries.GetCandidaciesByStudentID";
        }
        public List run(Integer id) throws BDException
        {
            List candidaciesInfo= new LinkedList();
            try
            {
                ISuportePersistente persistenceSupport= SuportePersistenteOJB.getInstance();
                IPersistentSeminaryCandidacy persistentSeminaryCandidacy= persistenceSupport.getIPersistentSeminaryCandidacy();
                IPersistentSeminary persistentSeminary= persistenceSupport.getIPersistentSeminary();                
                List candidacies = persistentSeminaryCandidacy.readByStudentID(id);            
                for (Iterator iterator= candidacies.iterator(); iterator.hasNext();)
                {
                    ICandidacy candidacy = (ICandidacy) iterator.next();
                    
                    //CLONER
                    //InfoCandidacy infoCandidacy= Cloner.copyICandicacy2InfoCandidacy(candidacy);
                    InfoCandidacy infoCandidacy= InfoCandidacy.newInfoFromDomain(candidacy);
                    
                    ISeminary seminary = (ISeminary) persistentSeminary.readByOID(Seminary.class,candidacy.getSeminaryIdInternal());
                    infoCandidacy.setSeminaryName(seminary.getName());
                    candidaciesInfo.add(infoCandidacy);
                }
            }
            catch (ExcepcaoPersistencia ex)
            {
                throw new BDException("Got an error while trying to retrieve multiple candidacies from the database", ex);
            }
            return candidaciesInfo;
        }
}
