/*
 * Created on 5/Ago/2003, 15:46:40 By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT]
 * utl [DOT] pt
 */
package ServidorAplicacao.Servico.Seminaries;

import java.util.Iterator;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.Seminaries.InfoCandidacy;
import DataBeans.util.Cloner;
import Dominio.Seminaries.ICandidacy;
import Dominio.Seminaries.ICaseStudyChoice;
import Dominio.Seminaries.IModality;
import ServidorApresentacao.Action.Seminaries.Exceptions.BDException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.Seminaries.IPersistentSeminaryCandidacy;
import ServidorPersistente.Seminaries.IPersistentSeminaryCaseStudyChoice;
import ServidorPersistente.Seminaries.IPersistentSeminaryModality;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt Created at
 *         5/Ago/2003, 15:46:40
 */
public class WriteCandidacy implements IService
{

   
    /**
     * The actor of this class.
     */
    public WriteCandidacy()
    {
    }

   
    public void run(InfoCandidacy infoCandidacy) throws BDException
    {
        try
        {
            ISuportePersistente persistenceSupport = SuportePersistenteOJB.getInstance();
            IPersistentSeminaryCandidacy persistentCandidacy = persistenceSupport
                    .getIPersistentSeminaryCandidacy();
            IPersistentSeminaryModality persistentModality = persistenceSupport
                    .getIPersistentSeminaryModality();
            IPersistentSeminaryCaseStudyChoice persistentChoice = persistenceSupport
                    .getIPersistentSeminaryCaseStudyChoice();
            ICandidacy candidacy = Cloner.copyInfoCandicacy2ICandidacy(infoCandidacy);
            IModality modality = persistentModality.readByName("Completa");
            persistentCandidacy.simpleLockWrite(candidacy);
            if (modality.getIdInternal().equals(infoCandidacy.getModalityIdInternal()))
            {
                candidacy.setThemeIdInternal(null);
            }
            for (Iterator iter = candidacy.getCaseStudyChoices().iterator(); iter.hasNext();)
            {
                ICaseStudyChoice element = (ICaseStudyChoice) iter.next();
                persistentChoice.simpleLockWrite(element);
                element.setCandidacyIdInternal(candidacy.getIdInternal());
            }
        }
        catch (ExcepcaoPersistencia ex)
        {
            throw new BDException("Got an error while trying to write a candidacy to the database", ex);
        }
    }
}