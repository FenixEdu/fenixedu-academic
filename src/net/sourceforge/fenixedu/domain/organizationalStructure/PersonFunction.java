package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.Date;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.DateFormatUtil;

public class PersonFunction extends PersonFunction_Base {

    public void edit(IFunction function, Date beginDate, Date endDate, Double credits) {
        this.setFunction(function);
        this.setCredits(credits);        
        this.setBeginDate(beginDate);
        if(endDate != null && endDate.before(beginDate)){            
            throw new DomainException("error.endDateBeforeBeginDate");
        }
        this.setEndDate(endDate);
    }
    
    public void delete(){
        removeFunction();
        removePerson();
        deleteDomainObject();
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
}
