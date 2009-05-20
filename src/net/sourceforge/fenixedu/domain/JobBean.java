package net.sourceforge.fenixedu.domain;

import java.io.Serializable;

import org.joda.time.LocalDate;

public class JobBean implements Serializable {

    static private final long serialVersionUID = 5885003369040710968L;

    private DomainReference<BusinessArea> parentBusinessArea;
    private DomainReference<BusinessArea> childBusinessArea;

    private String city;
    private String employerName;
    private String position;

    private LocalDate beginDate;
    private LocalDate endDate;

    private DomainReference<Country> country;

    public JobBean() {
    }

    public BusinessArea getParentBusinessArea() {
	return (this.parentBusinessArea != null) ? this.parentBusinessArea.getObject() : null;
    }

    public void setParentBusinessArea(BusinessArea parentBusinessArea) {
	this.parentBusinessArea = (parentBusinessArea != null) ? new DomainReference<BusinessArea>(parentBusinessArea) : null;
    }

    public boolean hasParentBusinessArea() {
	return getParentBusinessArea() != null;
    }

    public BusinessArea getChildBusinessArea() {
	return (this.childBusinessArea != null) ? this.childBusinessArea.getObject() : null;
    }

    public void setChildBusinessArea(BusinessArea childBusinessArea) {
	this.childBusinessArea = (childBusinessArea != null) ? new DomainReference<BusinessArea>(childBusinessArea) : null;
    }

    public String getCity() {
	return city;
    }

    public void setCity(String city) {
	this.city = city;
    }

    public String getEmployerName() {
	return employerName;
    }

    public void setEmployerName(String employerName) {
	this.employerName = employerName;
    }

    public String getPosition() {
	return position;
    }

    public void setPosition(String position) {
	this.position = position;
    }

    public LocalDate getBeginDate() {
	return beginDate;
    }

    public void setBeginDate(LocalDate beginDate) {
	this.beginDate = beginDate;
    }

    public LocalDate getEndDate() {
	return endDate;
    }

    public void setEndDate(LocalDate endDate) {
	this.endDate = endDate;
    }

    public Country getCountry() {
	return (this.country != null) ? this.country.getObject() : null;
    }

    public void setCountry(Country country) {
	this.country = (country != null) ? new DomainReference<Country>(country) : null;
    }

}
