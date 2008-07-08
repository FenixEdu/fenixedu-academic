package net.sourceforge.fenixedu.domain.organizationalStructure;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.residence.ResidenceMonth;

public class ResidenceManagementUnit extends ResidenceManagementUnit_Base {
    
    public  ResidenceManagementUnit() {
        super();
    }
    
    public boolean isPaymentEventAvailable(Person person, ResidenceMonth month) {
	return month.isEventPresent(person);
    }
} 
