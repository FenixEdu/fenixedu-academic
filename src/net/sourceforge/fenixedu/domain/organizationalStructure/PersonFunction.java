package net.sourceforge.fenixedu.domain.organizationalStructure;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.YearMonthDay;

public class PersonFunction extends PersonFunction_Base {

    public PersonFunction(Party parentParty, Party childParty, Function function, 
            YearMonthDay begin, YearMonthDay end, Double credits) {
        
        super();
        setParentParty(parentParty);
        setChildParty(childParty);
        edit(function, begin, end, credits);         
    }

    public void edit(Function function, YearMonthDay begin, YearMonthDay end, Double credits) {        
        setAccountabilityType(function);
        setCredits(credits);
        setBeginDate(begin);
        if (end != null && end.isBefore(begin)) {
            throw new DomainException("error.endDateBeforeBeginDate");
        }
        setEndDate(end);
    }
    
    public Person getPerson(){
        return (Person) this.getChildParty();
    }
    
    public Unit getUnit() {
        return (Unit) this.getParentParty();
    }
    
    public Function getFunction(){
        return (Function) this.getAccountabilityType();
    } 
}
