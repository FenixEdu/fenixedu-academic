/*
 * Created on 21/Mar/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.contributor;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
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
            NonExistingContributorServiceException, ExistingServiceException {

        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        //Read if exists any contributor with the new number
        IContributor contributor = sp.getIPersistentContributor().readByContributorNumber(
                contributorNumber);
        if (contributor != null) {
            throw new ExistingServiceException();
        }

        // Read the Actual Contributor
        final IContributor storedContributor = sp.getIPersistentContributor().readByContributorNumber(
                infoContributor.getContributorNumber());
        if (storedContributor == null) {
            throw new NonExistingContributorServiceException();
        }

        storedContributor.setContributorNumber(contributorNumber);
        storedContributor.setContributorName(contributorName);
        storedContributor.setContributorAddress(contributorAddress);

        return InfoContributor.newInfoFromDomain(storedContributor);
    }

}
