/*
 * Created on 21/Mar/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.contributor;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingContributorServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoContributor;
import net.sourceforge.fenixedu.domain.IContributor;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class EditContributor implements IService {

    public InfoContributor run(InfoContributor infoContributor, Integer contributorNumber,
            String contributorName, String contributorAddress) throws ExcepcaoPersistencia,
            NonExistingContributorServiceException {

        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        // Read the Actual Contributor
        final IContributor contributorBD = sp.getIPersistentContributor().readByContributorNumber(
                infoContributor.getContributorNumber());

        if (contributorBD == null) {
            throw new NonExistingContributorServiceException();
        }

        sp.getIPersistentContributor().simpleLockWrite(contributorBD);
        contributorBD.setContributorNumber(contributorNumber);
        contributorBD.setContributorName(contributorName);
        contributorBD.setContributorAddress(contributorAddress);

        return InfoContributor.newInfoFromDomain(contributorBD);
    }

}
