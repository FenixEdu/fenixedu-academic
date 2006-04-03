package net.sourceforge.fenixedu.domain.organizationalStructure;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.YearMonthDay;

public class PersonFunction extends PersonFunction_Base {

    public PersonFunction(Party parentParty, Party childParty, AccountabilityType accountabilityType) {        
        super();
        setParentParty(parentParty);
        setChildParty(childParty);
        setAccountabilityType(accountabilityType);        
    }

    public void edit(Function function, YearMonthDay begin, YearMonthDay end, Double credits) {
        this.setFunction(function);
        this.setCredits(credits);
        this.setBeginDate(begin);
        if (end != null && end.isBefore(begin)) {
            throw new DomainException("error.endDateBeforeBeginDate");
        }
        this.setEndDate(end);
    }
    
    public Person getPerson(){
        return (Person) this.getChildParty();
    }
    
    public void delete(){
        removeFunction();
        super.delete();
    }
}
