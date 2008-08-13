/*
 * Created on 25/Ago/2003, 15:09:58
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.Seminaries;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Seminaries.SeminaryCandidacy;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.presentationTier.Action.Seminaries.Exceptions.BDException;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 25/Ago/2003, 15:09:58
 * 
 */
public class DeleteCandidacy extends Service {

	public void run(Integer id) throws BDException{
		SeminaryCandidacy candidacy = rootDomainObject.readSeminaryCandidacyByOID(id);
        candidacy.delete();
	}

}
