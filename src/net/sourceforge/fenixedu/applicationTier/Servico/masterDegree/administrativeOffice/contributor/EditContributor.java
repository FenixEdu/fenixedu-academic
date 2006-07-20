package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.contributor;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoContributor;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;

public class EditContributor extends Service {

    public InfoContributor run(InfoContributor infoContributor, Integer contributorNumber,
            String contributorName, String contributorAddress, String areaCode, String areaOfAreaCode,
            String area, String parishOfResidence, String districtSubdivisionOfResidence,
            String districtOfResidence) throws FenixServiceException {

        final Party storedContributor = rootDomainObject.readPartyByOID(infoContributor.getIdInternal());
        if (storedContributor == null) {
            throw new NonExistingServiceException();
        }

        try {
            storedContributor.editContributor(contributorName, contributorNumber.toString(), contributorAddress, areaCode, areaOfAreaCode, area,
                    parishOfResidence, districtSubdivisionOfResidence, districtOfResidence);
        } catch (DomainException e) {
            throw new ExistingServiceException();
        }

        return InfoContributor.newInfoFromDomain(storedContributor);
    }

}
