/*
 * Created on 26/Ago/2003, 14:50:16
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.Seminaries;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoCandidacy;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoCandidacyWithCaseStudyChoices;
import net.sourceforge.fenixedu.domain.Seminaries.SeminaryCandidacy;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.presentationTier.Action.Seminaries.Exceptions.BDException;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 26/Ago/2003, 14:50:16
 * 
 */
public class GetCandidacyById extends Service {

	public InfoCandidacy run(Integer id) throws BDException{
		InfoCandidacy infoCandidacy = null;

		SeminaryCandidacy candidacy = rootDomainObject.readSeminaryCandidacyByOID(id);
		infoCandidacy = InfoCandidacyWithCaseStudyChoices.newInfoFromDomain(candidacy);

		return infoCandidacy;
	}
    
}
