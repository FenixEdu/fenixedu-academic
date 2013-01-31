package net.sourceforge.fenixedu.domain;

import java.io.Serializable;

import org.joda.time.LocalDate;

public class JobBean implements Serializable {

	static private final long serialVersionUID = 5885003369040710968L;

	private BusinessArea parentBusinessArea;
	private BusinessArea childBusinessArea;

	private String city;
	private String employerName;
	private String position;

	private LocalDate beginDate;
	private LocalDate endDate;

	private Country country;

	public JobBean() {
	}

	public BusinessArea getParentBusinessArea() {
		return this.parentBusinessArea;
	}

	public void setParentBusinessArea(BusinessArea parentBusinessArea) {
		this.parentBusinessArea = parentBusinessArea;
	}

	public boolean hasParentBusinessArea() {
		return getParentBusinessArea() != null;
	}

	public BusinessArea getChildBusinessArea() {
		return this.childBusinessArea;
	}

	public void setChildBusinessArea(BusinessArea childBusinessArea) {
		this.childBusinessArea = childBusinessArea;
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
		return this.country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

}
