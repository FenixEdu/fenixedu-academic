package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.Comparator;

import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeWorkSheet;
import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTime;
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
	    Duration firstLevelNightBalance, Duration secondLevelNightBalance, Duration secondLevelNightBalanceWithLimit,
	    Integer nightExtraWorkDays, Duration unjustified) {
	setRootDomainObject(RootDomainObject.getInstance());
	setAssiduousnessClosedMonth(assiduousnessClosedMonth);
	setWorkScheduleType(workScheduleType);
	setNightBalance(nightBalance);
	setFirstLevelBalance(firstLevelBalance);
	setSecondLevelBalance(secondLevelBalance);
	setSecondLevelBalanceWithLimit(secondLevelBalanceWithLimit);
	setFirstLevelNightBalance(firstLevelNightBalance);
	setSecondLevelNightBalance(secondLevelNightBalance);
	setSecondLevelNightBalanceWithLimit(secondLevelNightBalanceWithLimit);
	setNightExtraWorkDays(nightExtraWorkDays);
	setUnjustified(unjustified);
	setIsCorrection(Boolean.FALSE);
	setLastModifiedDate(new DateTime());
    }

    public AssiduousnessExtraWork(EmployeeWorkSheet employeeWorkSheet, ClosedMonth correctionClosedMonth,
	    AssiduousnessClosedMonth assiduousnessClosedMonth, WorkScheduleType workScheduleType) {
	setRootDomainObject(RootDomainObject.getInstance());
	setWorkScheduleType(workScheduleType);
	setCorrectedOnClosedMonth(correctionClosedMonth);
	setAssiduousnessClosedMonth(assiduousnessClosedMonth);
	setIsCorrection(Boolean.TRUE);
	correct(employeeWorkSheet);
	setLastModifiedDate(new DateTime());
    }

    public void delete() {
	removeRootDomainObject();
	removeWorkScheduleType();
	removeAssiduousnessClosedMonth();
	removeCorrectedOnClosedMonth();
	deleteDomainObject();
    }

    public boolean hasEqualValues(EmployeeWorkSheet employeeWorkSheet) {
	return (getFirstLevelBalance().equals(employeeWorkSheet.getExtra125Map().get(getWorkScheduleType()))
		&& getSecondLevelBalance().equals(employeeWorkSheet.getExtra150Map().get(getWorkScheduleType()))
		&& getSecondLevelBalanceWithLimit().equals(
			employeeWorkSheet.getExtra150WithLimitsMap().get(getWorkScheduleType()))
		&& getFirstLevelNightBalance().equals(employeeWorkSheet.getExtraNight160Map().get(getWorkScheduleType()))
		&& getSecondLevelNightBalance().equals(employeeWorkSheet.getExtraNight190Map().get(getWorkScheduleType()))
		&& getSecondLevelNightBalanceWithLimit().equals(
			employeeWorkSheet.getExtraNight190WithLimitsMap().get(getWorkScheduleType()))
		&& getNightBalance().equals(employeeWorkSheet.getExtra25Map().get(getWorkScheduleType()))
		&& getUnjustified().equals(employeeWorkSheet.getUnjustifiedMap().get(getWorkScheduleType())) && getNightExtraWorkDays()
		.equals(employeeWorkSheet.getExtraWorkNightsMap().get(getWorkScheduleType())));
    }

    public void correct(EmployeeWorkSheet employeeWorkSheet) {
	setNightBalance(employeeWorkSheet.getExtra25Map().get(getWorkScheduleType()) == null ? Duration.ZERO : employeeWorkSheet
		.getExtra25Map().get(getWorkScheduleType()));
	setFirstLevelBalance(employeeWorkSheet.getExtra125Map().get(getWorkScheduleType()) == null ? Duration.ZERO
		: employeeWorkSheet.getExtra125Map().get(getWorkScheduleType()));
	setSecondLevelBalance(employeeWorkSheet.getExtra150Map().get(getWorkScheduleType()) == null ? Duration.ZERO
		: employeeWorkSheet.getExtra150Map().get(getWorkScheduleType()));
	setSecondLevelBalanceWithLimit(employeeWorkSheet.getExtra150WithLimitsMap().get(getWorkScheduleType()) == null ? Duration.ZERO
		: employeeWorkSheet.getExtra150WithLimitsMap().get(getWorkScheduleType()));
	setFirstLevelNightBalance(employeeWorkSheet.getExtraNight160Map().get(getWorkScheduleType()) == null ? Duration.ZERO
		: employeeWorkSheet.getExtraNight160Map().get(getWorkScheduleType()));
	setSecondLevelNightBalance(employeeWorkSheet.getExtraNight190Map().get(getWorkScheduleType()) == null ? Duration.ZERO
		: employeeWorkSheet.getExtraNight190Map().get(getWorkScheduleType()));
	setSecondLevelNightBalanceWithLimit(employeeWorkSheet.getExtraNight190WithLimitsMap().get(getWorkScheduleType()) == null ? Duration.ZERO
		: employeeWorkSheet.getExtraNight190WithLimitsMap().get(getWorkScheduleType()));
	setNightExtraWorkDays(employeeWorkSheet.getExtraWorkNightsMap().get(getWorkScheduleType()) == null ? 0
		: employeeWorkSheet.getExtraWorkNightsMap().get(getWorkScheduleType()));
	setUnjustified(employeeWorkSheet.getUnjustifiedMap().get(getWorkScheduleType()) == null ? Duration.ZERO
		: employeeWorkSheet.getUnjustifiedMap().get(getWorkScheduleType()));
	setLastModifiedDate(new DateTime());
    }
}
