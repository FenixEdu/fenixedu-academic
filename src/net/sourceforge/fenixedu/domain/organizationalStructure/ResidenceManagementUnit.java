package net.sourceforge.fenixedu.domain.organizationalStructure;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.residence.ResidenceMonth;
import net.sourceforge.fenixedu.util.Money;

public class ResidenceManagementUnit extends ResidenceManagementUnit_Base {
    
    public  ResidenceManagementUnit() {
        super();
    }
    
    public boolean isPaymentEventAvailable(Person person, ResidenceMonth month) {
	return month.isEventPresent(person);
    }

    public Integer getCurrentPaymentLimitDay() {
	return getResidencePriceTable().getPaymentLimitDay();
    }

    public Money getCurrentSingleRoomValue() {
	return getResidencePriceTable().getSingleRoomValue();
    }
    
    public Money getCurrentDoubleRoomValue() {
	return getResidencePriceTable().getDoubleRoomValue();
    }
    
    public boolean isResidencePriceTableConfigured() {
	return getResidencePriceTable().isConfigured();
    }
} 
