package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.Date;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.DateFormatUtil;

public class Function extends Function_Base {

    public boolean isInherentFunction() {
        return (this.getParentInherentFunction() != null);
    }

    public void edit(String functionName, Date beginDate, Date endDate, FunctionType type, Unit unit,
            Function parentInherentFunction) {       
        this.setName(functionName);
        this.setBeginDate(beginDate);
        if(endDate != null && endDate.before(beginDate)){
            throw new DomainException("error.endDateBeforeBeginDate");
        }
        this.setEndDate(endDate);
        this.setType(type);
        this.setUnit(unit);
        this.setParentInherentFunction(parentInherentFunction);
    }

    public boolean isActive(Date currentDate) {
        return (this.getEndDate() == null
                || (DateFormatUtil.equalDates("yyyyMMdd", this.getEndDate(), currentDate) || this
                        .getEndDate().after(currentDate)));            
    }
    
    public boolean belongsToPeriod(Date beginDate, Date endDate) {
        return (!this.getBeginDate().after(endDate)
                && (this.getEndDate() == null || !this.getEndDate().before(beginDate)));            
    }

    public void delete() {
        if (!hasAnyPersonFunctions() && !hasAnyInherentFunctions()) {            
            removeParentInherentFunction();
            removeUnit();
            removeRootDomainObject();
        super.deleteDomainObject();
        } else {
            throw new DomainException("error.delete.function");
        }
    }
}
