/*
 * Created on 14/Mar/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.contributor;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoContributor;
import net.sourceforge.fenixedu.domain.Contributor;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadContributorList extends Service {

    public List run(final Integer contributorNumber) throws FenixServiceException, ExcepcaoPersistencia {

        final Contributor contributor = persistentSupport.getIPersistentContributor().readByContributorNumber(
                contributorNumber);

        final List result = new ArrayList();
        if (contributor != null) {
            final InfoContributor infoContributor = InfoContributor.newInfoFromDomain(contributor);
            result.add(infoContributor);
        }

        return result;
    }
}