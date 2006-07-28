/*
 * Created on Sep 26, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.domain;

import java.util.Comparator;
import java.util.Date;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.util.ContractType;

import org.apache.commons.beanutils.BeanComparator;
import org.joda.time.YearMonthDay;

public class Contract extends Contract_Base {

    public static final Comparator CONTRACT_COMPARATOR_BY_BEGIN_DATE = new BeanComparator("beginDateYearMonthDay");
    
    public Contract(Employee employee, Date beginDate, Date endDate, Unit unit, ContractType type) {
		super();
        checkParameters(employee, beginDate, endDate, unit);        
		setRootDomainObject(RootDomainObject.getInstance());
        setEmployee(employee);        
        setWorkingUnit(unit);
        setContractType(type);
        setBeginDate(beginDate);
        setEndDate(endDate);
	}

    private void checkParameters(Employee employee, Date beginDate, Date endDate, Unit unit) {       
        if(unit == null) {
            throw new DomainException("error.contract.no.unit");
        }
        if(employee == null) {
            throw new DomainException("error.contract.no.employee");
        }
        if(beginDate == null) {
            throw new DomainException("error.contract.no.beginDate");
        }
        if (endDate != null && endDate.before(beginDate)) {
            throw new DomainException("error.contract.endDateBeforeBeginDate");
        }
        //checkContractDatesIntersection(employee, beginDate, endDate);
    }

	public boolean belongsToPeriod(YearMonthDay beginDate, YearMonthDay endDate) {
        return (!this.getBeginDateYearMonthDay().isAfter(endDate)
                && (this.getEndDateYearMonthDay() == null || !this.getEndDateYearMonthDay().isBefore(beginDate)));            
    }

    public boolean isActive(YearMonthDay currentDate) {
        return (!this.getBeginDateYearMonthDay().isAfter(currentDate) && (this.getEndDateYearMonthDay() == null || !this
                .getEndDateYearMonthDay().isBefore(currentDate)));
    }

//    private void checkContractDatesIntersection(Employee employee, Date begin, Date end) {
//        for (Contract contract : employee.getContracts()) {
//            if (contract.checkDatesIntersections(begin, end)) {
//                throw new DomainException("error.employee.contract.dates.intersection");
//            }
//        }        
//    }
//    
//    private boolean checkDatesIntersections(Date begin, Date end) {
//        return ((end == null || !this.getBeginDate().after(end))
//                && (this.getEndDate() == null || !this.getEndDate().before(begin)));
//    } 

    public void delete() {
        removeEmployee();
        removeMailingUnit();
        removeSalaryUnit();
        removeWorkingUnit();
        removeRootDomainObject();
        deleteDomainObject();
    }
}
