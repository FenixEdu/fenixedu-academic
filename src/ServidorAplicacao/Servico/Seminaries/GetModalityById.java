/*
 * Created on 29/Ago/2003, 12:59:46
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorAplicacao.Servico.Seminaries;

import DataBeans.Seminaries.InfoModality;
import Dominio.Seminaries.Modality;
import ServidorAplicacao.IServico;
import ServidorApresentacao.Action.Seminaries.Exceptions.BDException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.Seminaries.IPersistentSeminaryCaseStudy;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 *
 * 
 * Created at 29/Ago/2003, 12:59:46
 * 
 */
public class GetModalityById implements IServico
{
    private static GetModalityById service= new GetModalityById();
        /**
         * The singleton access method of this class.
         **/
        public static GetModalityById getService()
        {
            return service;
        }
        /**
         * The actor of this class.
         **/
        private GetModalityById()
        {
        }
        /**
         * Returns The Service Name */
        public final String getNome()
        {
            return "Seminaries.GetModalityById";
        }
        public InfoModality run(Integer id) throws BDException
        {
            InfoModality infoModality= null;
            try
            {
                ISuportePersistente persistenceSupport= SuportePersistenteOJB.getInstance();
                IPersistentSeminaryCaseStudy persistentCaseStudy= persistenceSupport.getIPersistentSeminaryCaseStudy();
                Modality modality = (Modality) persistentCaseStudy.readByOID(Modality.class,id);
                
                //CLONER
                //infoModality = Cloner.copyIModality2InfoModality(modality);
                infoModality = InfoModality.newInfoFromDomain(modality);
                
            }
            catch (ExcepcaoPersistencia ex)
            {
                throw new BDException(
                    "Got an error while trying to retrieve a modality from the database",
                    ex);
            }
            return infoModality;
        }

}
