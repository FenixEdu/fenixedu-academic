package net.sourceforge.fenixedu.domain.material;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.YearMonthDay;

public class PersonExtension extends PersonExtension_Base {
    
    public PersonExtension(Person person, Extension extension, YearMonthDay begin, YearMonthDay end) {
        super();
        checkParameters(extension, begin, end, person);
        checkExtensionSpaceOccupationIntersection(begin, end, extension, person);       
        setPerson(person);
        setExtension(extension);          
        super.setBegin(begin);
        super.setEnd(end);
    }
    
    private void checkParameters(Extension extension, YearMonthDay begin, YearMonthDay end, Person person) {
        if(extension == null) {
            throw new DomainException("error.personExtension.no.extension");
        }  
        if(person == null) {
            throw new DomainException("error.personExtension.no.space");
        }  
        if(begin == null) {
            throw new DomainException("error.personExtension.no.beginDate");
        }
        if(end != null && end.isBefore(begin)) {
            throw new DomainException("error.personExtension.endDateBeforeBeginDate");
        }              
    }
           
    @Override
    public void setBegin(YearMonthDay begin) {
        checkExtensionSpaceOccupationIntersection(begin, getEnd(), getExtension(), getPerson());
        super.setBegin(begin);
    }

    @Override
    public void setEnd(YearMonthDay end) {
        checkExtensionSpaceOccupationIntersection(getBegin(), end, getExtension(), getPerson());
        super.setEnd(end);
    }

    private void checkExtensionSpaceOccupationIntersection(YearMonthDay begin, YearMonthDay end, Extension extension, Person person) {
        for (PersonExtension personExtension : extension.getPersons()) {
            if (!personExtension.equals(this)
                    && personExtension.getPerson().equals(person)
                    && personExtension.checkIntersections(begin, end)) {
                throw new DomainException("error.personExtension.intersection");
            }
        }
    }      
    
    private boolean checkIntersections(YearMonthDay begin, YearMonthDay end) {
        return ((end == null || !this.getBegin().isAfter(end))
                && (this.getEnd() == null || !this.getEnd().isBefore(begin)));
    }  
}
