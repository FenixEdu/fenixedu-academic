package net.sourceforge.fenixedu.dataTransferObject.assiduousness;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.EmployeeExtraWorkAuthorization;
import net.sourceforge.fenixedu.domain.assiduousness.ExtraWorkAuthorization;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;

import org.joda.time.YearMonthDay;

public class ExtraWorkAuthorizationFactory implements FactoryExecutor, Serializable {

    YearMonthDay beginDate;

    YearMonthDay endDate;
    
    Integer workingCostCenterCode;

    Integer payingCostCenterCode;
    
    DomainReference<Unit> workingUnit;

    DomainReference<Unit> payingUnit;

    DomainReference<Employee> modifiedBy;

    DomainReference<ExtraWorkAuthorization> extraWorkAuthorization;

    List<EmployeeExtraWorkAuthorizationBean> employeesExtraWorkAuthorizations = new ArrayList<EmployeeExtraWorkAuthorizationBean>();

    public ExtraWorkAuthorizationFactory(Employee modifiedBy, ExtraWorkAuthorization extraWorkAuthorization) {
        setBeginDate(extraWorkAuthorization.getBeginDate());
        setEndDate(extraWorkAuthorization.getEndDate());
        setExtraWorkAuthorization(extraWorkAuthorization);
        setWorkingUnit(extraWorkAuthorization.getWorkingUnit());
        setWorkingCostCenterCode(getWorkingUnit().getCostCenterCode());
        setPayingUnit(extraWorkAuthorization.getPayingUnit());
        setPayingCostCenterCode(getPayingUnit().getCostCenterCode());
        setModifiedBy(modifiedBy);
        setEmployeesExtraWorkAuthorizations(extraWorkAuthorization.getEmployeeExtraWorkAuthorizations());
    }

    public ExtraWorkAuthorizationFactory(Employee modifiedBy) {
        setModifiedBy(modifiedBy);
        addEmployeeExtraWorkAuthorization();
    }

    public ExtraWorkAuthorizationFactory(ExtraWorkAuthorization extraWorkAuthorization) {
        setExtraWorkAuthorization(extraWorkAuthorization);
        setEmployeesExtraWorkAuthorizations(extraWorkAuthorization.getEmployeeExtraWorkAuthorizations());
    }

    public void addEmployeeExtraWorkAuthorization(){
        getEmployeesExtraWorkAuthorizations().add(new EmployeeExtraWorkAuthorizationBean(getModifiedBy()));
    }
    
    public Object execute() {
        ExtraWorkAuthorization extraWorkAuthorization = getExtraWorkAuthorization();
        if(extraWorkAuthorization == null) {
            return new ExtraWorkAuthorization(this);
        } else {
            extraWorkAuthorization.edit(this);
            return extraWorkAuthorization;
        }
    }

    public YearMonthDay getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(YearMonthDay beginDate) {
        this.beginDate = beginDate;
    }

    public List<EmployeeExtraWorkAuthorizationBean> getEmployeesExtraWorkAuthorizations() {
        return employeesExtraWorkAuthorizations;
    }

    public void setEmployeesExtraWorkAuthorizations(
            List<EmployeeExtraWorkAuthorization> employeesExtraWorkAuthorizations) {
        if (employeesExtraWorkAuthorizations != null) {
            List<EmployeeExtraWorkAuthorizationBean> employeesExtraWorkAuthorizationsList = new ArrayList<EmployeeExtraWorkAuthorizationBean>();
            for (EmployeeExtraWorkAuthorization employeeExtraWorkAuthorization : employeesExtraWorkAuthorizations) {
                employeesExtraWorkAuthorizationsList.add(new EmployeeExtraWorkAuthorizationBean(
                        employeeExtraWorkAuthorization.getExtraWorkAuthorization(),
                        employeeExtraWorkAuthorization));
            }
            this.employeesExtraWorkAuthorizations = employeesExtraWorkAuthorizationsList;
        }
    }

    public YearMonthDay getEndDate() {
        return endDate;
    }

    public void setEndDate(YearMonthDay endDate) {
        this.endDate = endDate;
    }

    public Employee getModifiedBy() {
        return modifiedBy == null ? null : modifiedBy.getObject();
    }

    public void setModifiedBy(Employee modifiedBy) {
        if (modifiedBy != null) {
            this.modifiedBy = new DomainReference<Employee>(modifiedBy);
        } else {
            this.modifiedBy = null;
        }
    }

    public Unit getPayingUnit() {
        return payingUnit == null ? null : payingUnit.getObject();
    }

    public void setPayingUnit(Unit payingUnit) {
        if (payingUnit != null) {
            this.payingUnit = new DomainReference<Unit>(payingUnit);
        } else {
            this.payingUnit = null;
        }
    }

    public Unit getWorkingUnit() {
        return workingUnit == null ? null : workingUnit.getObject();
    }

    public void setWorkingUnit(Unit workingUnit) {
        if (workingUnit != null) {
            this.workingUnit = new DomainReference<Unit>(workingUnit);
        } else {
            this.workingUnit = null;
        }
    }

    public ExtraWorkAuthorization getExtraWorkAuthorization() {
        return extraWorkAuthorization == null ? null : extraWorkAuthorization.getObject();
    }

    public void setExtraWorkAuthorization(ExtraWorkAuthorization extraWorkAuthorization) {
        if (extraWorkAuthorization != null) {
            this.extraWorkAuthorization = new DomainReference<ExtraWorkAuthorization>(
                    extraWorkAuthorization);
        } else {
            this.extraWorkAuthorization = null;
        }
    }

    public Integer getPayingCostCenterCode() {
        return payingCostCenterCode;
    }

    public void setPayingCostCenterCode(Integer payingCostCenterCode) {
        this.payingCostCenterCode = payingCostCenterCode;
    }

    public Integer getWorkingCostCenterCode() {
        return workingCostCenterCode;
    }

    public void setWorkingCostCenterCode(Integer workingCostCenterCode) {
        this.workingCostCenterCode = workingCostCenterCode;
    }
}
