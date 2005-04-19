/*
 * Created on 21/Mar/2003
 *
 * 
 */
package net.sourceforge.fenixedu.domain;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class Contributor extends Contributor_Base {

    public Contributor() {
    }

    public Contributor(Integer contributorNumber, String contributorName, String contributorAddress) {
        setContributorNumber(contributorNumber);
        setContributorName(contributorName);
        setContributorAddress(contributorAddress);
    }

    public boolean equals(Object o) {
        return ((o instanceof IContributor)
                && (getContributorNumber().equals(((IContributor) o).getContributorNumber()))
                && (getContributorName().equals(((IContributor) o).getContributorName())) && (getContributorAddress()
                .equals(((IContributor) o).getContributorAddress())));

    }

    public String toString() {
        String result = "Contributor:\n";
        result += "\n  - Internal Code : " + getIdInternal();
        result += "\n  - Contributor Number : " + getContributorNumber();
        result += "\n  - Contributor Name : " + getContributorName();
        result += "\n  - Contributor Address : " + getContributorAddress();
        return result;
    }

}