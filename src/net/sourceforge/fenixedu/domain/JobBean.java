package net.sourceforge.fenixedu.domain;

import java.io.Serializable;
import java.util.Date;

public class JobBean implements Serializable {

    static private final long serialVersionUID = 5885003369040710968L;

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
    private JobApplicationType applicationType;
    private ContractType contractType;
    private SalaryType salaryType;

    public JobBean() {

    }

}
