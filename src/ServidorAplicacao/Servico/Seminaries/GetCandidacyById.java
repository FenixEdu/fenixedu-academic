/*
 * Created on 26/Ago/2003, 14:50:16
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorAplicacao.Servico.Seminaries;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.Seminaries.InfoCandidacy;
import DataBeans.Seminaries.InfoCandidacyWithCaseStudyChoices;
import Dominio.Seminaries.Candidacy;
import Dominio.Seminaries.ICandidacy;
import ServidorApresentacao.Action.Seminaries.Exceptions.BDException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.Seminaries.IPersistentSeminaryCandidacy;

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