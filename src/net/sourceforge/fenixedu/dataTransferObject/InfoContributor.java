/*
 * Created on 21/Mar/2003
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.organizationalStructure.Party;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class InfoContributor extends InfoObject {

    public enum ContributorType {
        EXTERNAL_PERSON,
        EXTERNAL_INSTITUTION_UNIT,
    }

    private ContributorType contributorType;
    private String contributorName;
    private String contributorNumber;
    private String contributorAddress;
    private String areaCode;
    private String areaOfAreaCode;
    private String area;
    private String parishOfResidence;
    private String districtSubdivisionOfResidence;
    private String districtOfResidence;

    public InfoContributor() {
    }

    public InfoContributor(String contributorNumber, String contributorName, String contributorAddress) {
        this.contributorNumber = contributorNumber;
        this.contributorName = contributorName;
        this.contributorAddress = contributorAddress;
    }

    public boolean equals(Object o) {
        return ((o instanceof InfoContributor)
                && (contributorNumber.equals(((InfoContributor) o).getContributorNumber()))
                && (contributorName.equals(((InfoContributor) o).getContributorName())) && (contributorAddress
                .equals(((InfoContributor) o).getContributorAddress())));

    }

    public void copyFromDomain(Party contributor) {
        super.copyFromDomain(contributor);
        if (contributor != null) {
            setContributorName(contributor.getName());
            setContributorNumber(contributor.getSocialSecurityNumber());
            setContributorAddress(contributor.getAddress());
            setAreaCode(contributor.getAreaCode());
            setAreaOfAreaCode(contributor.getAreaOfAreaCode());
            setArea(contributor.getArea());
            setParishOfResidence(contributor.getParishOfResidence());
            setDistrictSubdivisionOfResidence(contributor.getDistrictSubdivisionOfResidence());
            setDistrictOfResidence(contributor.getDistrictOfResidence());
        }
    }

    public static InfoContributor newInfoFromDomain(Party contributor) {
        InfoContributor infoContributor = null;
        if (contributor != null) {
            infoContributor = new InfoContributor();
            infoContributor.copyFromDomain(contributor);
        }
        return infoContributor;
    }

    public ContributorType getContributorType() {
        return contributorType;
    }

    public void setContributorType(ContributorType contributorType) {
        this.contributorType = contributorType;
    }

    public String getContributorName() {
        return contributorName;
    }

    public void setContributorName(String contributorName) {
        this.contributorName = contributorName;
    }

    public String getContributorAddress() {
        return contributorAddress;
    }

    public void setContributorAddress(String contributorAddress) {
        this.contributorAddress = contributorAddress;
    }

    public String getContributorNumber() {
        return contributorNumber;
    }

    public void setContributorNumber(String contributorNumber) {
        this.contributorNumber = contributorNumber;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaOfAreaCode() {
        return areaOfAreaCode;
    }

    public void setAreaOfAreaCode(String areaOfAreaCode) {
        this.areaOfAreaCode = areaOfAreaCode;
    }

    public String getDistrictOfResidence() {
        return districtOfResidence;
    }

    public void setDistrictOfResidence(String districtOfResidence) {
        this.districtOfResidence = districtOfResidence;
    }

    public String getDistrictSubdivisionOfResidence() {
        return districtSubdivisionOfResidence;
    }

    public void setDistrictSubdivisionOfResidence(String districtSubdivisionOfResidence) {
        this.districtSubdivisionOfResidence = districtSubdivisionOfResidence;
    }

    public String getParishOfResidence() {
        return parishOfResidence;
    }

    public void setParishOfResidence(String parishOfResidence) {
        this.parishOfResidence = parishOfResidence;
    }

}
