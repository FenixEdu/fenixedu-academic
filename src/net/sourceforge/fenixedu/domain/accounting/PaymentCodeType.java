package net.sourceforge.fenixedu.domain.accounting;

public enum PaymentCodeType {

    TOTAL_GRATUITY(0),
    GRATUITY_FIRST_INSTALLMENT(1),
    GRATUITY_SECOND_INSTALLMENT(2),
    ADMINISTRATIVE_OFFICE_FEE_AND_INSURANCE(3);

    private int typeDigit;
   
    private PaymentCodeType(int typeDigit) {
	this.typeDigit = typeDigit;
    }

    public String getName() {
	return name();
    }

    public String getQualifiedName() {
	return PaymentCodeType.class.getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
	return PaymentCodeType.class.getName() + "." + name();
    }

    public int getTypeDigit() {
	return typeDigit;
    }
    
}
