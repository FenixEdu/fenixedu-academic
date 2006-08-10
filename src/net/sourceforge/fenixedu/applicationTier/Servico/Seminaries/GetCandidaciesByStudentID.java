/*
 * Created on 5/Ago/2003, 19:44:39
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.Seminaries;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoCandidacy;
import net.sourceforge.fenixedu.domain.Seminaries.Seminary;
import net.sourceforge.fenixedu.domain.Seminaries.SeminaryCandidacy;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.presentationTier.Action.Seminaries.Exceptions.BDException;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 5/Ago/2003, 19:44:39
 * 
 */
public class GetCandidaciesByStudentID extends Service {

	public List run(Integer id) throws BDException, ExcepcaoPersistencia {
		List candidaciesInfo = new LinkedList();

		List candidacies = rootDomainObject.readRegistrationByOID(id).getAssociatedCandidancies();
		for (Iterator iterator = candidacies.iterator(); iterator.hasNext();) {
			SeminaryCandidacy candidacy = (SeminaryCandidacy) iterator.next();

			InfoCandidacy infoCandidacy = InfoCandidacy.newInfoFromDomain(candidacy);

			Seminary seminary = candidacy.getSeminary();
			infoCandidacy.setSeminaryName(seminary.getName());
			candidaciesInfo.add(infoCandidacy);
		}

		return candidaciesInfo;
	}
}