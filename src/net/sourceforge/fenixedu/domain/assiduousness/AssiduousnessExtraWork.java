package net.sourceforge.fenixedu.domain.assiduousness;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.Duration;

public class AssiduousnessExtraWork extends AssiduousnessExtraWork_Base {

    public AssiduousnessExtraWork(AssiduousnessClosedMonth assiduousnessClosedMonth,
	    WorkScheduleType workScheduleType, Duration nightBalance, Duration firstLevelBalance,
	    Duration secondLevelBalance, Duration unjustified) {
	setRootDomainObject(RootDomainObject.getInstance());
	setAssiduousnessClosedMonth(assiduousnessClosedMonth);
	setWorkScheduleType(workScheduleType);
	setNightBalance(nightBalance);
	setFirstLevelBalance(firstLevelBalance);
	setSecondLevelBalance(secondLevelBalance);
	setUnjustified(unjustified);
    }

    public void delete() {
	removeRootDomainObject();
	removeWorkScheduleType();
	deleteDomainObject();
    }

}
