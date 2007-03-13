package net.sourceforge.fenixedu.domain.assiduousness;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.Duration;

public class ClosedMonthJustification extends ClosedMonthJustification_Base {

    public ClosedMonthJustification(AssiduousnessClosedMonth assiduousnessClosedMonth,
	    JustificationMotive justificationMotive, Duration duration) {
	setRootDomainObject(RootDomainObject.getInstance());
	setAssiduousnessClosedMonth(assiduousnessClosedMonth);
	setJustificationMotive(justificationMotive);
	setJustificationDuration(duration);
    }

    public void delete() {
	removeRootDomainObject();
	removeJustificationMotive();
	deleteDomainObject();
    }

}
