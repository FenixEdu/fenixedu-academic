package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.contributor;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoContributor;
import net.sourceforge.fenixedu.domain.Contributor;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class EditContributor extends Service {

    public InfoContributor run(InfoContributor infoContributor, Integer contributorNumber,
            String contributorName, String contributorAddress) throws ExcepcaoPersistencia, FenixServiceException {

        final Contributor storedContributor = (Contributor) persistentObject.readByOID(Contributor.class, infoContributor.getIdInternal());
        if (storedContributor == null) {
            throw new NonExistingServiceException();
        }

        try {
            storedContributor.edit(contributorNumber, contributorName, contributorAddress);    
        } catch (DomainException e) {
            throw new ExistingServiceException();
        }
        
        return InfoContributor.newInfoFromDomain(storedContributor);
    }

}
