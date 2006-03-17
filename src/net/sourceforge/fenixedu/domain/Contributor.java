package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class Contributor extends Contributor_Base {

    public Contributor() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public Contributor(Integer contributorNumber, String contributorName, String contributorAddress) {
        this();
        this.setContributorNumber(contributorNumber);
        this.setContributorName(contributorName);
        this.setContributorAddress(contributorAddress);
    }

    public String toString() {
        String result = "Contributor:\n";
        result += "\n  - Internal Code : " + getIdInternal();
        result += "\n  - Contributor Number : " + getContributorNumber();
        result += "\n  - Contributor Name : " + getContributorName();
        result += "\n  - Contributor Address : " + getContributorAddress();
        return result;
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

    public static Contributor readByContributorNumber(final Integer contributorNumber) {
        for (final Contributor contributor : RootDomainObject.getInstance().getContributors()) {
            if (contributor.getContributorNumber().equals(contributorNumber)) {
                return contributor;
            }
        }
        return null;
    }

}
