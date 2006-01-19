/*
 * Created on 21/Mar/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.contributor;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoContributor;
import net.sourceforge.fenixedu.domain.Contributor;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class CreateContributor implements IService {

    public void run(InfoContributor newContributor) throws ExcepcaoPersistencia,
            ExistingServiceException {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        Contributor contributor = sp.getIPersistentContributor().readByContributorNumber(
                newContributor.getContributorNumber());

        if (contributor != null) {
            throw new ExistingServiceException();
        }

        contributor = DomainFactory.makeContributor(newContributor.getContributorNumber(),
                newContributor.getContributorName(), newContributor.getContributorAddress());

    }

}
