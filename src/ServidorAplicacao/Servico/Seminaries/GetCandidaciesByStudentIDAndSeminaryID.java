/*
 * Created on 5/Ago/2003, 19:44:39
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorAplicacao.Servico.Seminaries;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.Seminaries.InfoCandidacy;
import Dominio.Seminaries.ICandidacy;
import Dominio.Seminaries.ISeminary;
import ServidorApresentacao.Action.Seminaries.Exceptions.BDException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.Seminaries.IPersistentSeminaryCandidacy;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 5/Ago/2003, 19:44:39
 *  
 */
public class GetCandidaciesByStudentIDAndSeminaryID implements IService {

    public GetCandidaciesByStudentIDAndSeminaryID() {
    }

    public List run(Integer studentID, Integer seminaryID) throws BDException {
        List candidaciesInfo = new LinkedList();
        try {
            ISuportePersistente persistenceSupport = SuportePersistenteOJB.getInstance();
            IPersistentSeminaryCandidacy persistentSeminaryCandidacy = persistenceSupport
                    .getIPersistentSeminaryCandidacy();
            List candidacies = persistentSeminaryCandidacy.readByStudentIDAndSeminaryID(studentID,
                    seminaryID);
            for (Iterator iterator = candidacies.iterator(); iterator.hasNext();) {
                ICandidacy candidacy = (ICandidacy) iterator.next();
                InfoCandidacy infoCandidacy = InfoCandidacy.newInfoFromDomain(candidacy);
                ISeminary seminary = candidacy.getSeminary();
                infoCandidacy.setSeminaryName(seminary.getName());
                candidaciesInfo.add(infoCandidacy);
            }
        } catch (ExcepcaoPersistencia ex) {
            throw new BDException(
                    "Got an error while trying to retrieve multiple candidacies from the database", ex);
        }
        return candidaciesInfo;
    }
}