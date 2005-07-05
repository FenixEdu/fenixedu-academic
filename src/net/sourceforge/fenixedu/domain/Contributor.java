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
        this.setContributorNumber(contributorNumber);
        this.setContributorName(contributorName);
        this.setContributorAddress(contributorAddress);
    }

    public boolean equals(Object obj) {
        if (obj instanceof IContributor) {
            final IContributor contributor = (IContributor) obj;
            return this.getIdInternal().equals(contributor.getIdInternal());
        }
        return false;
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
