package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

/**
 * This class only exists to allow database access in scripts.
 * A Contributor is now a Party: either a Person or an ExternalUnit 
 *
 */

@Deprecated
public class Contributor extends Contributor_Base {

    private Contributor() {
        throw new DomainException("Contributor.class.is.deprecated");
    }

    public void edit(Integer contributorNumber, String contributorName, String contributorAddress) {
        Contributor contributor = Contributor.readByContributorNumber(contributorNumber);
        if (contributor != null && !contributor.equals(this)) {
            throw new DomainException("duplicate.contributor.number");
        }

        this.setContributorNumber(contributorNumber);
        this.setContributorName(contributorName);
        this.setContributorAddress(contributorAddress);
    }    

    public void delete() {
        if (hasAnyGuides() || hasAnyReceipts()) {
            throw new DomainException("contributor.cannot.be.deleted");
        }
        removeRootDomainObject();
        super.deleteDomainObject();
    }
    
    public static Contributor readByContributorNumber(final Integer contributorNumber) {
        for (final Contributor contributor : RootDomainObject.getInstance().getContributors()) {
            if (contributor.getContributorNumber().equals(contributorNumber)) {
                return contributor;
            }
        }
        return null;
    }
    
}
