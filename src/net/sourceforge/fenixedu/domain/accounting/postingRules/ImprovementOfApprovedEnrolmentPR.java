package net.sourceforge.fenixedu.domain.accounting.postingRules;

import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.serviceAgreementTemplates.AdministrativeOfficeServiceAgreementTemplate;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

public class ImprovementOfApprovedEnrolmentPR extends ImprovementOfApprovedEnrolmentPR_Base {
    
    public  ImprovementOfApprovedEnrolmentPR(EntryType type, EventType type2, DateTime time, DateTime time2, AdministrativeOfficeServiceAgreementTemplate serviceAgreementTemplate, Money money) {
        super();
    }

    @Override
    protected boolean hasPenalty(Event event, DateTime when) {
	// TODO Auto-generated method stub
	return false;
    }
    
}
