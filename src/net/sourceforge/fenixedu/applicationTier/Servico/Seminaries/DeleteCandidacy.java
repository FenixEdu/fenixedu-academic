/*
 * Created on 25/Ago/2003, 15:09:58
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.Seminaries;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.Seminaries.Candidacy;
import net.sourceforge.fenixedu.domain.Seminaries.ICandidacy;
import net.sourceforge.fenixedu.domain.Seminaries.ICaseStudyChoice;
import net.sourceforge.fenixedu.presentationTier.Action.Seminaries.Exceptions.BDException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminaryCandidacy;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminaryCaseStudyChoice;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 25/Ago/2003, 15:09:58
 *  
 */
public class DeleteCandidacy implements IService {

    public DeleteCandidacy() {
    }

    public void run(Integer id) throws BDException {
        try {
            ISuportePersistente persistenceSupport = SuportePersistenteOJB.getInstance();
            IPersistentSeminaryCandidacy persistentCandidacy = persistenceSupport
                    .getIPersistentSeminaryCandidacy();
            IPersistentSeminaryCaseStudyChoice persistentChoice = persistenceSupport
                    .getIPersistentSeminaryCaseStudyChoice();
            ICandidacy candidacy = (ICandidacy) persistentCandidacy.readByOID(Candidacy.class, id);
            List choices = candidacy.getCaseStudyChoices();
            for (Iterator iterator = choices.iterator(); iterator.hasNext();) {
                ICaseStudyChoice choice = (ICaseStudyChoice) iterator.next();
                persistentChoice.delete(choice);
            }
            persistentCandidacy.deleteByOID(Candidacy.class, id);
        } catch (ExcepcaoPersistencia ex) {
            throw new BDException("Got an error while trying to delete a candidacy from the database",
                    ex);
        }
    }

}