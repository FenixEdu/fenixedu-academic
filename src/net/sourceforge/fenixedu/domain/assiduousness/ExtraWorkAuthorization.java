package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeExtraWorkAuthorizationBean;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.ExtraWorkAuthorizationFactory;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.util.DateInterval;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public class ExtraWorkAuthorization extends ExtraWorkAuthorization_Base {

    public ExtraWorkAuthorization(ExtraWorkAuthorizationFactory extraWorkAuthorizationFactory) {
        if (alreadyExistsExtraWorkAuthorization(extraWorkAuthorizationFactory)) {
            throw new DomainException("error.extraWorkAuthorization.alreadyExists");
        }
        setRootDomainObject(RootDomainObject.getInstance());
        setModifiedBy(extraWorkAuthorizationFactory.getModifiedBy());
        setPayingUnit(getUnit(extraWorkAuthorizationFactory.getPayingCostCenterCode()));
        if(getPayingUnit() == null){
            throw new DomainException("error.extraWorkAuthorization.notExist.payingUnit");
        }
        setWorkingUnit(getUnit(extraWorkAuthorizationFactory.getWorkingCostCenterCode()));
        if(getWorkingUnit() == null){
            throw new DomainException("error.extraWorkAuthorization.notExist.workingUnit");
        }
        if (extraWorkAuthorizationFactory.getBeginDate().isAfter(
                extraWorkAuthorizationFactory.getEndDate())) {
            throw new DomainException("error.extraWorkAuthorization.invalidPeriod");
        }
        setBeginDate(extraWorkAuthorizationFactory.getBeginDate());
        setEndDate(extraWorkAuthorizationFactory.getEndDate());
        setLastModifiedDate(new DateTime());
        boolean addedEmployee = false;
        for (EmployeeExtraWorkAuthorizationBean employeeExtraWorkAuthorizationBean : extraWorkAuthorizationFactory
                .getEmployeesExtraWorkAuthorizations()) {
            if (!employeeExtraWorkAuthorizationBean.getDelete()
                    && employeeExtraWorkAuthorizationBean.getEmployee() != null) {
                new EmployeeExtraWorkAuthorization(this, employeeExtraWorkAuthorizationBean);
                addedEmployee = true;
            }
        }
        if (!addedEmployee) {
            throw new DomainException("error.extraWorkAuthorization.deleteAll");
        }
    }

    public void edit(ExtraWorkAuthorizationFactory extraWorkAuthorizationFactory) {
        if (alreadyExistsExtraWorkAuthorization(extraWorkAuthorizationFactory)) {
            throw new DomainException("error.extraWorkAuthorization.alreadyExists");
        }
        setModifiedBy(extraWorkAuthorizationFactory.getModifiedBy());
        setPayingUnit(getUnit(extraWorkAuthorizationFactory.getPayingCostCenterCode()));
        if(getPayingUnit() == null){
            throw new DomainException("error.extraWorkAuthorization.notExist.payingUnit");
        }
        setWorkingUnit(getUnit(extraWorkAuthorizationFactory.getWorkingCostCenterCode()));
        if(getWorkingUnit() == null){
            throw new DomainException("error.extraWorkAuthorization.notExist.workingUnit");
        }
        if (extraWorkAuthorizationFactory.getBeginDate().isAfter(
                extraWorkAuthorizationFactory.getEndDate())) {
            throw new DomainException("error.extraWorkAuthorization.invalidPeriod");
        }
        setBeginDate(extraWorkAuthorizationFactory.getBeginDate());
        setEndDate(extraWorkAuthorizationFactory.getEndDate());
        setLastModifiedDate(new DateTime());

        List<EmployeeExtraWorkAuthorizationBean> employeeExtraWorkAuthorizationBeansToDelete = new ArrayList<EmployeeExtraWorkAuthorizationBean>();
        for (EmployeeExtraWorkAuthorizationBean employeeExtraWorkAuthorizationBean : extraWorkAuthorizationFactory
                .getEmployeesExtraWorkAuthorizations()) {
            if (!employeeExtraWorkAuthorizationBean.getDelete()
                    && employeeExtraWorkAuthorizationBean.getEmployee() != null) {
                EmployeeExtraWorkAuthorization employeeExtraWorkAuthorization = getEmployeeExtraWorkAuthorizationByEmployee(employeeExtraWorkAuthorizationBean
                        .getEmployee());
                if (employeeExtraWorkAuthorization != null) {
                    employeeExtraWorkAuthorization.edit(employeeExtraWorkAuthorizationBean);
                } else {
                    new EmployeeExtraWorkAuthorization(this, employeeExtraWorkAuthorizationBean);
                }
            } else {
                employeeExtraWorkAuthorizationBeansToDelete.add(employeeExtraWorkAuthorizationBean);
            }
        }
        if (employeeExtraWorkAuthorizationBeansToDelete.size() >= extraWorkAuthorizationFactory
                .getEmployeesExtraWorkAuthorizations().size()) {
            throw new DomainException("error.extraWorkAuthorization.deleteAll");
        }
        deleteEmployeeExtraWorkAuthorization(employeeExtraWorkAuthorizationBeansToDelete);
    }

    public boolean existsBetweenDates(YearMonthDay begin, YearMonthDay end) {
        DateInterval dateInterval = new DateInterval(getBeginDate(), getEndDate());
        if (begin != null && end != null) {
            DateInterval internalInterval = new DateInterval(begin, end);
            return dateInterval.containsInterval(internalInterval);
        } else if (begin != null && end == null) {
            return dateInterval.containsDate(begin);
        } else if (begin == null && end != null) {
            return dateInterval.containsDate(end);
        }
        return true;
    }

    private void deleteEmployeeExtraWorkAuthorization(
            List<EmployeeExtraWorkAuthorizationBean> employeeExtraWorkAuthorizationBeansToDelete) {
        for (EmployeeExtraWorkAuthorizationBean employeeExtraWorkAuthorizationBean : employeeExtraWorkAuthorizationBeansToDelete) {
            EmployeeExtraWorkAuthorization employeeExtraWorkAuthorization = employeeExtraWorkAuthorizationBean
                    .getEmployeeExtraWorkAuthorization();
            if (employeeExtraWorkAuthorization != null) {
                employeeExtraWorkAuthorization.delete();
            }
        }
    }

    private EmployeeExtraWorkAuthorization getEmployeeExtraWorkAuthorizationByEmployee(Employee employee) {
        for (EmployeeExtraWorkAuthorization employeeExtraWorkAuthorization : getEmployeeExtraWorkAuthorizations()) {
            if (employeeExtraWorkAuthorization.getAssiduousness().getEmployee() == employee) {
                return employeeExtraWorkAuthorization;
            }
        }
        return null;
    }

    private boolean alreadyExistsExtraWorkAuthorization(
            ExtraWorkAuthorizationFactory extraWorkAuthorizationFactory) {
        for (ExtraWorkAuthorization extraWorkAuthorization : RootDomainObject.getInstance()
                .getExtraWorkAuthorizations()) {
            if (this != extraWorkAuthorization
                    && extraWorkAuthorization.getWorkingUnit() == extraWorkAuthorizationFactory
                            .getWorkingUnit()
                    && extraWorkAuthorization.getBeginDate().equals(
                            extraWorkAuthorizationFactory.getBeginDate())
                    && extraWorkAuthorization.getEndDate().equals(
                            extraWorkAuthorizationFactory.getEndDate())) {
                return true;
            }
        }
        return false;
    } 

    private Unit getUnit(Integer costCenterCode) {
        Collection<Unit> allUnits = RootDomainObject.readAllDomainObjects(Unit.class);
        for (Unit unit : allUnits) {
            if(unit.getCostCenterCode() != null && unit.getCostCenterCode().equals(costCenterCode)){
                return unit;
            }
        }
        return null;
    }
}
