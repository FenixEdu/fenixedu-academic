/*
 * Created on 14/Mar/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.contributor;

import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoContributor;
import net.sourceforge.fenixedu.domain.Contributor;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadContributor implements IService {

	public InfoContributor run(Integer contributorNumber) throws FenixServiceException, ExcepcaoPersistencia {

		ISuportePersistente sp = null;
		Contributor contributor = null;

		sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

		// Read the contributor

		contributor = sp.getIPersistentContributor().readByContributorNumber(contributorNumber);

		if (contributor == null)
			throw new ExcepcaoInexistente("Unknown Contributor !!");

		return InfoContributor.newInfoFromDomain(contributor);
	}
}