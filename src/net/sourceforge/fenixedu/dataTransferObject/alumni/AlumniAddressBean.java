package net.sourceforge.fenixedu.dataTransferObject.alumni;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Alumni;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddress;

public class AlumniAddressBean implements Serializable {

    private Alumni alumni;
    private String address;
    private String areaCode;
    private String areaOfAreaCode;
    private Country country;

    public AlumniAddressBean(Alumni alumni) {
        setAlumni(alumni);
    }

    public AlumniAddressBean(Alumni alumni, PhysicalAddress address) {
        setAlumni(alumni);
        setAddress(address.getAddress());
        setAreaCode(address.getAreaCode());
        setAreaOfAreaCode(address.getAreaOfAreaCode());
        setCountry(address.getCountryOfResidence());
    }

    public void setAlumni(Alumni alumni) {
        this.alumni = alumni;
    }

    public Alumni getAlumni() {
        return this.alumni;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public Country getCountry() {
        return this.country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

}
