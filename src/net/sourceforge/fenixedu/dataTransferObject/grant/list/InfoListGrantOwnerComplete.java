/*
 * Created on Jun 24, 2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.grant.list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.grant.owner.InfoGrantOwner;
import net.sourceforge.fenixedu.dataTransferObject.grant.owner.InfoGrantOwnerWithPerson;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Qualification;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContract;
import net.sourceforge.fenixedu.domain.grant.owner.GrantOwner;

/**
 * @author Pica
 * @author Pica
 */
public class InfoListGrantOwnerComplete extends InfoObject {

    private final DomainReference<GrantOwner> domainReference;

    public InfoListGrantOwnerComplete(final GrantOwner domainObject) {
	domainReference = new DomainReference<GrantOwner>(domainObject);
    }

    private GrantOwner getGrantOwner() {
	return domainReference == null ? null : domainReference.getObject();
    }

    @Override
    public boolean equals(Object obj) {
	return obj != null && getGrantOwner() == ((InfoListGrantOwnerComplete) obj).getGrantOwner();
    }

    public InfoGrantOwner getInfoGrantOwner() {
	return InfoGrantOwnerWithPerson.newInfoFromDomain(getGrantOwner());
    }

    public List<InfoListGrantContract> getInfoListGrantContracts() {
	final List<InfoListGrantContract> result = new ArrayList<InfoListGrantContract>();

	for (GrantContract grantContract : getGrantOwner().getGrantContracts()) {
	    result.add(new InfoListGrantContract(grantContract));
	}

	Collections.reverse(result);
	return result;
    }

    public List<Qualification> getInfoQualifications() {
	return getGrantOwner().getPerson().getAssociatedQualifications();
    }

}
