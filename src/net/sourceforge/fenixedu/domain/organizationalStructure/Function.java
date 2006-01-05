package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.Date;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.DateFormatUtil;

public class Function extends Function_Base {

    public boolean isInherentFunction() {
        if (this.getParentInherentFunction() != null) {
            return true;
        }
        return false;
    }

    public void edit(String functionName, Date beginDate, Date endDate, FunctionType type, IUnit unit,
            IFunction parentInherentFunction) {       
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
        if (this.getEndDate() == null
                || (DateFormatUtil.equalDates("yyyyMMdd", this.getEndDate(), currentDate) || this
                        .getEndDate().after(currentDate))) {
            return true;
        }
        return false;
    }
    
    public boolean belongsToPeriod(Date beginDate, Date endDate) {
        if (this.getBeginDate().before(endDate)
                && (this.getEndDate() == null || ((this.getEndDate().before(endDate) && this
                        .getEndDate().after(beginDate))
                        || this.getEndDate().after(endDate) || this.getEndDate().equals(endDate)))) {
            return true;
        }
        return false;
    }

    public void delete() {
        if (!hasAnyPersonFunctions() && !hasAnyInherentFunctions()) {            
            removeParentInherentFunction();
            removeUnit();
            super.deleteDomainObject();
        } else {
            throw new DomainException("error.delete.function");
        }
    }
}
