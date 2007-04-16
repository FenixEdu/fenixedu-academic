package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.contributor;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoContributor;
import net.sourceforge.fenixedu.dataTransferObject.InfoContributor.ContributorType;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddressData;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class CreateContributor extends Service {

    public void run(InfoContributor newContributor) throws InvalidArgumentsServiceException {
	
	if (newContributor.getContributorType() == ContributorType.EXTERNAL_PERSON) {
	    Person.createContributor(
		    newContributor.getContributorName(),
		    newContributor.getContributorNumber().toString(),
		    new PhysicalAddressData(
			    newContributor.getContributorAddress(),
			    newContributor.getAreaCode(),
			    newContributor.getAreaOfAreaCode(),
			    newContributor.getArea(),
			    newContributor.getParishOfResidence(),
			    newContributor.getDistrictSubdivisionOfResidence(),
			    newContributor.getDistrictOfResidence(),
			    null));
	} else if (newContributor.getContributorType() == ContributorType.EXTERNAL_INSTITUTION_UNIT) {
	    Unit.createContributor(
		    newContributor.getContributorName(),
		    newContributor.getContributorNumber().toString(),
		    new PhysicalAddressData(
			    newContributor.getContributorAddress(),
			    newContributor.getAreaCode(),
			    newContributor.getAreaOfAreaCode(),
			    newContributor.getArea(),
			    newContributor.getParishOfResidence(),
			    newContributor.getDistrictSubdivisionOfResidence(),
			    newContributor.getDistrictOfResidence(),
			    null));
	} else {
	    throw new InvalidArgumentsServiceException();
	}
    }

}
