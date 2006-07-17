package net.sourceforge.fenixedu.domain.accounting.postingRules;

import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

public abstract class GenericSingleEntryTypePR extends GenericSingleEntryTypePR_Base {

    protected GenericSingleEntryTypePR() {
        super();
    }

    protected void init(EntryType entryType, EventType eventType, DateTime startDate, DateTime endDate,
            ServiceAgreementTemplate serviceAgreementTemplate) {
        super.init(eventType, startDate, endDate, serviceAgreementTemplate);
        checkParameters(entryType);
        super.setEntryType(entryType);
    }

    private void checkParameters(EntryType entryType) {
        if (entryType == null) {
            throw new DomainException(
                    "error.accounting.postingRules.GenericSingleEntryTypePR.entryType.cannot.be.null");
        }
    }

    @Override
    public void setEntryType(EntryType entryType) {
        throw new DomainException(
                "error.accounting.postingRules.GenericSingleEntryTypePR.cannot.modify.entryType");
    }

}
