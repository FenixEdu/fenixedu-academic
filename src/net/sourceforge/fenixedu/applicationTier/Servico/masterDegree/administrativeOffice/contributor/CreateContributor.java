package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.contributor;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoContributor;
import net.sourceforge.fenixedu.dataTransferObject.InfoContributor.ContributorType;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class CreateContributor extends Service {

    public void run(InfoContributor newContributor) throws InvalidArgumentsServiceException, ExistingServiceException {
        if (newContributor.getContributorType() == ContributorType.EXTERNAL_PERSON) {
            try {
                Person.createContributor(
                        newContributor.getContributorName(),
                        newContributor.getContributorNumber().toString(),
                        newContributor.getContributorAddress(),
                        newContributor.getAreaCode(),
                        newContributor.getAreaOfAreaCode(),
                        newContributor.getArea(),
                        newContributor.getParishOfResidence(),
                        newContributor.getDistrictSubdivisionOfResidence(),
                        newContributor.getDistrictOfResidence());
            } catch (DomainException e) {
                throw new ExistingServiceException();
            }
        } else if (newContributor.getContributorType() == ContributorType.EXTERNAL_INSTITUTION_UNIT) {
            try {
                Unit.createContributor(
                        newContributor.getContributorName(),
                        newContributor.getContributorNumber().toString(),
                        newContributor.getContributorAddress(),
                        newContributor.getAreaCode(),
                        newContributor.getAreaOfAreaCode(),
                        newContributor.getArea(),
                        newContributor.getParishOfResidence(),
                        newContributor.getDistrictSubdivisionOfResidence(),
                        newContributor.getDistrictOfResidence());
            } catch (DomainException e) {
                throw new ExistingServiceException();
            }
        } else {
            throw new InvalidArgumentsServiceException();
        }
    }

}
