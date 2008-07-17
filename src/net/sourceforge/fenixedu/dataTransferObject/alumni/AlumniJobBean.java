package net.sourceforge.fenixedu.dataTransferObject.alumni;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import net.sourceforge.fenixedu.domain.Alumni;
import net.sourceforge.fenixedu.domain.BusinessArea;
import net.sourceforge.fenixedu.domain.ContractType;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Job;
import net.sourceforge.fenixedu.domain.organizationalStructure.AcademicalInstitutionType;

import org.joda.time.LocalDate;

public class AlumniJobBean implements Serializable {

    private DomainReference<Alumni> alumni;
    private String employerName;
    private String city;
    private DomainReference<Country> country;
    private DomainReference<BusinessArea> parentBusinessArea;
    private DomainReference<BusinessArea> childBusinessArea;
    private String position;
    private Date beginDate;
    private Date endDate;
    private Integer jobId;
    private String schema;
    private ContractType contractType;

    private AlumniJobBean(Alumni alumni, String schema) {
	setAlumni(alumni);
	setSchema(schema);
    }

    public AlumniJobBean(Alumni alumni) {
	this(alumni, "alumni.public.access.jobContact");
    }

    public AlumniJobBean(Alumni alumni, Job job) {
	this(alumni, "alumni.public.access.jobContact.full");
	setEmployerName(job.getEmployerName());
	setCity(job.getCity());
	setCountry(job.getCountry());
	setParentBusinessArea(job.getBusinessArea().getParentArea());
	setChildBusinessArea(job.getBusinessArea());
	setPosition(job.getPosition());
	setBeginDateAsDate(job.getBeginDate());
	setEndDateAsDate(job.getEndDate());
	setContractType(job.getContractType());
	setJobId(job.getIdInternal());
    }

    public void setAlumni(Alumni alumni) {
	this.alumni = (alumni != null) ? new DomainReference<Alumni>(alumni) : null;
    }

    public Alumni getAlumni() {
	return (this.alumni != null) ? this.alumni.getObject() : null;
    }

    public void setCountry(Country country) {
	this.country = (country != null) ? new DomainReference<Country>(country) : null;
    }

    public Country getCountry() {
	return (this.country != null) ? this.country.getObject() : null;
    }

    public String getEmployerName() {
	return employerName;
    }

    public void setEmployerName(String employerName) {
	this.employerName = employerName;
    }

    public String getCity() {
	return city;
    }

    public void setCity(String city) {
	this.city = city;
    }

    public String getPosition() {
	return position;
    }

    public void setPosition(String jobPosition) {
	this.position = jobPosition;
    }

    public void setParentBusinessArea(BusinessArea businessArea) {
	this.parentBusinessArea = (businessArea != null) ? new DomainReference<BusinessArea>(businessArea) : null;
    }

    public BusinessArea getParentBusinessArea() {
	return (this.parentBusinessArea != null) ? this.parentBusinessArea.getObject() : null;
    }

    public void setChildBusinessArea(BusinessArea businessArea) {
	this.childBusinessArea = (businessArea != null) ? new DomainReference<BusinessArea>(businessArea) : null;
    }

    public BusinessArea getChildBusinessArea() {
	return (this.childBusinessArea != null) ? this.childBusinessArea.getObject() : null;
    }

    public Date getBeginDate() {
	return beginDate;
    }

    public void setBeginDate(Date beginDate) {
	this.beginDate = beginDate;
    }

    public Date getEndDate() {
	return endDate;
    }

    public void setEndDate(Date endDate) {
	this.endDate = endDate;
    }

    public Integer getJobId() {
	return jobId;
    }

    public void setJobId(Integer jobId) {
	this.jobId = jobId;
    }

    public String getSchema() {
	return schema;
    }

    public void setSchema(String schema) {
	this.schema = schema;
    }

    public void updateSchema() {
	if (getParentBusinessArea() == null) {
	    setSchema("alumni.public.access.jobContact");
	} else {
	    setSchema("alumni.public.access.jobContact.full");
	}
    }

    private void setBeginDateAsDate(LocalDate beginDate) {
	if (beginDate != null) {
	    final Calendar date = Calendar.getInstance();
	    date.set(Calendar.YEAR, beginDate.getYear());
	    date.set(Calendar.MONTH, beginDate.getMonthOfYear() - 1);
	    date.set(Calendar.DAY_OF_MONTH, beginDate.getDayOfMonth());
	    setBeginDate(date.getTime());
	}
    }

    private void setEndDateAsDate(LocalDate endDate) {
	if (endDate != null) {
	    final Calendar date = Calendar.getInstance();
	    date.set(Calendar.YEAR, endDate.getYear());
	    date.set(Calendar.MONTH, endDate.getMonthOfYear() - 1);
	    date.set(Calendar.DAY_OF_MONTH, endDate.getDayOfMonth());
	    setEndDate(date.getTime());
	}
    }

    public LocalDate getBeginDateAsLocalDate() {
	return beginDate != null ? LocalDate.fromDateFields(beginDate) : null;
    }

    public LocalDate getEndDateAsLocalDate() {
	return endDate != null ? LocalDate.fromDateFields(endDate) : null;
    }

    public ContractType getContractType() {
	return contractType;
    }

    public void setContractType(ContractType contractType) {
	this.contractType = contractType;
    }


}
