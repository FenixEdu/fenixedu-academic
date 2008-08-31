package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.Duration;

public class AssiduousnessExtraWork extends AssiduousnessExtraWork_Base {

    public static final Comparator<AssiduousnessExtraWork> COMPARATOR_BY_WORK_SCHEDULE_TYPE_ACRONYM = new Comparator<AssiduousnessExtraWork>() {

	@Override
	public int compare(AssiduousnessExtraWork o1, AssiduousnessExtraWork o2) {
	    return o1.getWorkScheduleType().getAcronym().compareTo(o2.getWorkScheduleType().getAcronym());
	}
	
    };

    public AssiduousnessExtraWork(AssiduousnessClosedMonth assiduousnessClosedMonth, WorkScheduleType workScheduleType,
	    Duration nightBalance, Duration firstLevelBalance, Duration secondLevelBalance, Duration secondLevelBalanceWithLimit,
	    Duration unjustified) {
	setRootDomainObject(RootDomainObject.getInstance());
	setAssiduousnessClosedMonth(assiduousnessClosedMonth);
	setWorkScheduleType(workScheduleType);
	setNightBalance(nightBalance);
	setFirstLevelBalance(firstLevelBalance);
	setSecondLevelBalance(secondLevelBalance);
	setSecondLevelBalanceWithLimit(secondLevelBalanceWithLimit);
	setUnjustified(unjustified);
    }

    public void delete() {
	removeRootDomainObject();
	removeWorkScheduleType();
	deleteDomainObject();
    }

}
