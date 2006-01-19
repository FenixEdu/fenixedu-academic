/*
 * Created on 5/Ago/2003, 19:44:39
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.Seminaries;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoCandidacy;
import net.sourceforge.fenixedu.domain.Seminaries.Candidacy;
import net.sourceforge.fenixedu.domain.Seminaries.Seminary;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminaryCandidacy;
import net.sourceforge.fenixedu.presentationTier.Action.Seminaries.Exceptions.BDException;
import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 5/Ago/2003, 19:44:39
 * 
 */
public class GetCandidaciesByStudentIDAndSeminaryID implements IService {

	public List run(Integer studentID, Integer seminaryID) throws BDException, ExcepcaoPersistencia {
		List candidaciesInfo = new LinkedList();

		ISuportePersistente persistenceSupport = PersistenceSupportFactory
				.getDefaultPersistenceSupport();
		IPersistentSeminaryCandidacy persistentSeminaryCandidacy = persistenceSupport
				.getIPersistentSeminaryCandidacy();
		List candidacies = persistentSeminaryCandidacy.readByStudentIDAndSeminaryID(studentID,
				seminaryID);
		for (Iterator iterator = candidacies.iterator(); iterator.hasNext();) {
			Candidacy candidacy = (Candidacy) iterator.next();
			InfoCandidacy infoCandidacy = InfoCandidacy.newInfoFromDomain(candidacy);
			Seminary seminary = candidacy.getSeminary();
			infoCandidacy.setSeminaryName(seminary.getName());
			candidaciesInfo.add(infoCandidacy);
		}

		return candidaciesInfo;
	}
}