package net.sourceforge.fenixedu.domain.contacts;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.DomainReference;

public class PhysicalAddressData implements Serializable {
    
    private String address;	
    private String areaCode;
    private String areaOfAreaCode;
    private String area;
    private String parishOfResidence;
    private String districtSubdivisionOfResidence;	
    private String districtOfResidence;
    
    private DomainReference<Country> countryOfResidence;
    
    public PhysicalAddressData() {
    }
    
    public PhysicalAddressData(final String address, final String areaCode, final String areaOfAreaCode, final String area,
	    final String parishOfResidence, final String districtSubdivisionOfResidence, final String districtOfResidence,
	    final Country countryOfResidence) {
	
	setAddress(address);
	setAreaCode(areaCode);
	setAreaOfAreaCode(areaOfAreaCode);
	setArea(area);
	setParishOfResidence(parishOfResidence);
	setDistrictSubdivisionOfResidence(districtSubdivisionOfResidence);
	setDistrictOfResidence(districtOfResidence);
	setCountryOfResidence(countryOfResidence);
    }

    public String getAddress() {
        return address;
    }

    public PhysicalAddressData setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getArea() {
        return area;
    }

    public PhysicalAddressData setArea(String area) {
        this.area = area;
        return this;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public PhysicalAddressData setAreaCode(String areaCode) {
        this.areaCode = areaCode;
        return this;
    }

    public String getAreaOfAreaCode() {
        return areaOfAreaCode;
    }

    public PhysicalAddressData setAreaOfAreaCode(String areaOfAreaCode) {
        this.areaOfAreaCode = areaOfAreaCode;
        return this;
    }

    public String getDistrictOfResidence() {
        return districtOfResidence;
    }

    public PhysicalAddressData setDistrictOfResidence(String districtOfResidence) {
        this.districtOfResidence = districtOfResidence;
        return this;
    }

    public String getDistrictSubdivisionOfResidence() {
        return districtSubdivisionOfResidence;
    }

    public PhysicalAddressData setDistrictSubdivisionOfResidence(String districtSubdivisionOfResidence) {
        this.districtSubdivisionOfResidence = districtSubdivisionOfResidence;
        return this;
    }

    public String getParishOfResidence() {
        return parishOfResidence;
    }

    public PhysicalAddressData setParishOfResidence(String parishOfResidence) {
        this.parishOfResidence = parishOfResidence;
        return this;
    }
    
    public Country getCountryOfResidence() {
	return (this.countryOfResidence != null) ? this.countryOfResidence.getObject() : null;
    }

    public PhysicalAddressData setCountryOfResidence(Country countryOfResidence) {
	this.countryOfResidence = (countryOfResidence != null) ? new DomainReference<Country>(countryOfResidence) : null;
	return this;
    }

}
