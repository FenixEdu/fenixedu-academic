package net.sourceforge.fenixedu.dataTransferObject.accounting;

import java.io.Serializable;
import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;

public class ReimbursementEntryDTO extends EntryDTO implements Serializable {

    private BigDecimal amountToReimburse;

    public ReimbursementEntryDTO(EntryType entryType, BigDecimal totalAmount, BigDecimal payedAmount,
            Event event, BigDecimal amountToReimburse) {
        super(entryType, totalAmount, payedAmount, event);
        setAmountToReimburse(amountToReimburse);
    }

    public BigDecimal getAmountToReimburse() {
        return amountToReimburse;
    }

    public void setAmountToReimburse(BigDecimal amountToReimburse) {
        this.amountToReimburse = amountToReimburse;
    }

}
