package net.sourceforge.fenixedu.dataTransferObject.accounting.administrativeOffice;

import net.sourceforge.fenixedu.domain.accounting.Installment;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.YearMonthDay;

public class InstallmentDebtDTO extends DebtDTO {

    private Installment installment;

    public InstallmentDebtDTO(final YearMonthDay limitDate, final String code, final Money amount,
	    final Installment installment) {
	super(limitDate, code, amount);
	this.installment = installment;
    }

    public Installment getInstallment() {
	return installment;
    }


}
