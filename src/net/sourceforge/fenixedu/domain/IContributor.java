package net.sourceforge.fenixedu.domain;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public interface IContributor extends IDomainObject {

    void setContributorNumber(Integer contributorNumber);

    void setContributorName(String contributorName);

    void setContributorAddress(String contributorAddress);

    Integer getContributorNumber();

    String getContributorName();

    String getContributorAddress();

}