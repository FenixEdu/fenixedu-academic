package net.sourceforge.fenixedu.domain.accounting.report.events;

public interface InstallmentWrapper {

    public String getExpirationDateLabel();

    public String getAmountToPayLabel();

    public String getRemainingAmountLabel();

    public String getExpirationDate();

    public String getAmountToPay();

    public String getRemainingAmount();

}
