/*
 * Created on 14/Mar/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.contributor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoContributor;
import net.sourceforge.fenixedu.domain.Contributor;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadAllContributors extends Service {

	public List run() throws FenixServiceException, ExcepcaoPersistencia {
		List result = persistentSupport.getIPersistentContributor().readAll();

		List contributors = new ArrayList();
		Iterator iterator = result.iterator();
		while (iterator.hasNext())
			contributors.add(InfoContributor.newInfoFromDomain((Contributor) iterator.next()));

		return contributors;

	}
}