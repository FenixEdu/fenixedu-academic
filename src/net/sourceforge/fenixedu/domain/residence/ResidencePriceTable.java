package net.sourceforge.fenixedu.domain.residence;

public class ResidencePriceTable extends ResidencePriceTable_Base {
    
    public  ResidencePriceTable() {
        super();
    }

    public boolean isConfigured() {
	return getDoubleRoomValue() != null && getSingleRoomValue() != null && getPaymentLimitDay() != null;
    }
    
}
