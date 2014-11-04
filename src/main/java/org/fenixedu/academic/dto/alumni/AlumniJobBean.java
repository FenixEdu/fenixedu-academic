/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.dataTransferObject.alumni;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Alumni;
import net.sourceforge.fenixedu.domain.BusinessArea;
import net.sourceforge.fenixedu.domain.ContractType;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Job;
import net.sourceforge.fenixedu.domain.JobApplicationType;
import net.sourceforge.fenixedu.domain.SalaryType;

import org.joda.time.LocalDate;

public class AlumniJobBean implements Serializable {

    private Alumni alumni;
    private String employerName;
    private String city;
    private Country country;
    private BusinessArea parentBusinessArea;
    private BusinessArea childBusinessArea;
    private String position;
    private LocalDate beginDate;
    private LocalDate endDate;
    private String jobId;
    private String schema;
    private JobApplicationType applicationType;
    private ContractType contractType;
    private SalaryType salaryType;
    private Double salary;

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
        setParentBusinessArea(job.getParentBusinessArea());
        setChildBusinessArea(job.getBusinessArea());
        setPosition(job.getPosition());
        setBeginDateAsDate(job.getBeginDate());
        setEndDateAsDate(job.getEndDate());
        setApplicationType(job.getJobApplicationType());
        setContractType(job.getContractType());
        setSalaryType(job.getSalaryType());
        setJobId(job.getExternalId());
        setSalary(job.getSalary());
    }

    public void setAlumni(Alumni alumni) {
        this.alumni = alumni;
    }

    public Alumni getAlumni() {
        return this.alumni;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Country getCountry() {
        return this.country;
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
        this.parentBusinessArea = businessArea;
        if (businessArea == null) {
            setChildBusinessArea(null);
        }
    }

    public BusinessArea getParentBusinessArea() {
        return this.parentBusinessArea;
    }

    public void setChildBusinessArea(BusinessArea businessArea) {
        this.childBusinessArea = businessArea;
    }

    public BusinessArea getChildBusinessArea() {
        return this.childBusinessArea;
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

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
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
        setBeginDate(beginDate);
    }

    private void setEndDateAsDate(LocalDate endDate) {
        setEndDate(endDate);
    }

    public LocalDate getBeginDateAsLocalDate() {
        return getBeginDate();
    }

    public LocalDate getEndDateAsLocalDate() {
        return getEndDate();
    }

    public ContractType getContractType() {
        return contractType;
    }

    public void setContractType(ContractType contractType) {
        this.contractType = contractType;
    }

    public JobApplicationType getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(JobApplicationType applicationType) {
        this.applicationType = applicationType;
    }

    public SalaryType getSalaryType() {
        return salaryType;
    }

    public void setSalaryType(SalaryType salaryType) {
        this.salaryType = salaryType;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }
}
