package net.sourceforge.fenixedu.domain.organizationalStructure;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.YearMonthDay;

public class PersonFunction extends PersonFunction_Base {
       
    public PersonFunction(Party parentParty, Party childParty, Function function, YearMonthDay begin, YearMonthDay end, Double credits) {        
        super();
        checkParameters(parentParty, childParty, function, begin, end);                    
        setParentParty(parentParty);
        setChildParty(childParty);
        setAccountabilityType(function);
        setCredits(credits);        
        setBeginDate(begin);        
        setEndDate(end);
    }
    
    public void edit(YearMonthDay begin, YearMonthDay end, Double credits) {                
        checkPersonFunctionDates(begin, end);        
        setCredits(credits);
        setBeginDate(begin);        
        setEndDate(end);
    }
    
    private void checkParameters(Party parentParty, Party childParty, Function function, YearMonthDay begin, YearMonthDay end) {
        if(parentParty == null) {
            throw new DomainException("error.personFunction.no.unit");
        }
        if(childParty == null) {
            throw new DomainException("error.personFunction.no.person");
        }
        if(function == null) {
            throw new DomainException("error.personFunction.no.function");
        }
        checkPersonFunctionDates(begin, end);  
    }    

    private void checkPersonFunctionDates(YearMonthDay begin, YearMonthDay end) {
        if (end != null && end.isBefore(begin)) {
            throw new DomainException("error.endDateBeforeBeginDate");
        }
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
