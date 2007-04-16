/*
 * Created on 21/Mar/2003
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddress;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;

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
    private IDDocumentType documentType;
    private String documentIdNumber;

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
            
            if (contributor.hasDefaultPhysicalAddress()) {
        	final PhysicalAddress physicalAddress = contributor.getDefaultPhysicalAddress();
        	setContributorAddress(physicalAddress.getAddress());
                setAreaCode(physicalAddress.getAreaCode());
                setAreaOfAreaCode(physicalAddress.getAreaOfAreaCode());
                setArea(physicalAddress.getArea());
                setParishOfResidence(physicalAddress.getParishOfResidence());
                setDistrictSubdivisionOfResidence(physicalAddress.getDistrictSubdivisionOfResidence());
                setDistrictOfResidence(physicalAddress.getDistrictOfResidence());
            }
            
            if (contributor.getSocialSecurityNumber() == null && contributor instanceof Person) {
		Person contributorPerson = (Person) contributor;
		setDocumentIdNumber(contributorPerson.getDocumentIdNumber());
		setDocumentType(contributorPerson.getIdDocumentType());
	    }
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

    public String getDocumentIdNumber() {
        return documentIdNumber;
    }

    public IDDocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentIdNumber(String documentIdNumber) {
        this.documentIdNumber = documentIdNumber;
    }

    public void setDocumentType(IDDocumentType documentType) {
        this.documentType = documentType;
    }

}
