/*
 * Created on 26/Ago/2003, 14:50:16
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.Seminaries;

import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoCandidacy;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoCandidacyWithCaseStudyChoices;
import net.sourceforge.fenixedu.domain.Seminaries.Candidacy;
import net.sourceforge.fenixedu.domain.Seminaries.ICandidacy;
import net.sourceforge.fenixedu.presentationTier.Action.Seminaries.Exceptions.BDException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminaryCandidacy;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 26/Ago/2003, 14:50:16
 *  
 */
public class GetCandidacyById implements IService {

    public GetCandidacyById() {
    }

    public InfoCandidacy run(Integer id) throws BDException {
        InfoCandidacy infoCandidacy = null;
        try {
            ISuportePersistente persistenceSupport = SuportePersistenteOJB.getInstance();
            IPersistentSeminaryCandidacy persistentSeminaryCandidacy = persistenceSupport
                    .getIPersistentSeminaryCandidacy();
            ICandidacy candidacy = (ICandidacy) persistentSeminaryCandidacy.readByOID(Candidacy.class,
                    id);
            infoCandidacy = InfoCandidacyWithCaseStudyChoices.newInfoFromDomain(candidacy);

        } catch (ExcepcaoPersistencia ex) {
            throw new BDException(
                    "Got an error while trying to retrieve multiple candidacies from the database", ex);
        }
        return infoCandidacy;
    }
}