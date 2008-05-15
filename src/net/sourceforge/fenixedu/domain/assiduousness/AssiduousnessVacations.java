package net.sourceforge.fenixedu.domain.assiduousness;

import java.text.DecimalFormat;
import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.util.JustificationGroup;
import net.sourceforge.fenixedu.domain.assiduousness.util.JustificationType;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.EmployeeContract;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Duration;
import org.joda.time.YearMonthDay;

public class AssiduousnessVacations extends AssiduousnessVacations_Base {

    protected static final int ART_17_MAXIMUM = 12;

    public AssiduousnessVacations(Assiduousness assiduousness, Integer year, Double normalDays, Double normalWithLeavesDiscount,
	    Double antiquityDays, Double ageDays, Double accumulatedDays, Double bonusDays, Double lowTimeVacationsDays,
	    Double accumulatedArticle17Days, Double extraWorkDays, Double lastYearExtraWorkDays, DateTime lastModifiedDate) {
	setRootDomainObject(RootDomainObject.getInstance());
	setAssiduousness(assiduousness);
	setYear(year);
	setNormalDays(normalDays);
	setNormalWithLeavesDiscount(normalWithLeavesDiscount);
	setAntiquityDays(antiquityDays);
	setAgeDays(ageDays);
	setAccumulatedDays(accumulatedDays);
	setBonusDays(bonusDays);
	setLowTimeVacationsDays(lowTimeVacationsDays);
	setAccumulatedArticle17Days(accumulatedArticle17Days);
	setExtraWorkDays(extraWorkDays);
	setLastYearExtraWorkDays(lastYearExtraWorkDays);
	setLastModifiedDate(lastModifiedDate);
	setEfectiveWorkDays(0);
    }

    public Double getTotalDays() {
	return getNormalWithLeavesDiscount() + getAntiquityDays() + getAgeDays() + getAccumulatedDays() + getBonusDays()
		+ getLowTimeVacationsDays() + getAccumulatedArticle17Days() + getExtraWorkDays() + getLastYearExtraWorkDays();
    }

    public void edit(Double normalDays, Double normalWithLeavesDiscount, Double antiquityDays, Double ageDays,
	    Double accumulatedDays, Double lowTimeVacationsDays, Double accumulatedArticle17Days, DateTime lastModifiedDate) {
	setNormalDays(normalDays);
	setNormalWithLeavesDiscount(normalWithLeavesDiscount);
	setAntiquityDays(antiquityDays);
	setAgeDays(ageDays);
	setAccumulatedDays(accumulatedDays);
	setLowTimeVacationsDays(lowTimeVacationsDays);
	setAccumulatedArticle17Days(accumulatedArticle17Days);
	setLastModifiedDate(lastModifiedDate);
    }

    public Double getUsedVacations() {
	Double vacationsDays = 0.0;
	List<Leave> leaves = getAssiduousness().getLeavesByYear(getYear());
	for (Leave leave : leaves) {
	    JustificationGroup justificationGroup = leave.getJustificationMotive().getJustificationGroup();
	    if (justificationGroup != null && justificationGroup.isVacation()) {
		JustificationType justificationType = leave.getJustificationMotive().getJustificationType();
		if (justificationType == JustificationType.HALF_OCCURRENCE) {
		    vacationsDays += (leave.getWorkDaysBetween(getYear()) / 2);
		} else if (justificationType == JustificationType.OCCURRENCE) {
		    vacationsDays += leave.getWorkDaysBetween(getYear());
		}
	    }
	}
	return vacationsDays;
    }

    public Double getUnusedVacations() {
	return getTotalDays() - getUsedVacations();
    }

    public Unit getLastMailingUnitInYear() {
	YearMonthDay beginDate = new YearMonthDay(getYear(), 1, 1);
	YearMonthDay endDate = new YearMonthDay(getYear(), 12, 31);
	Unit unit = getAssiduousness().getEmployee().getLastWorkingPlace(beginDate, endDate);
	EmployeeContract lastMailingContract = (EmployeeContract) getAssiduousness().getEmployee().getLastContractByContractType(
		AccountabilityTypeEnum.MAILING_CONTRACT);
	if (lastMailingContract != null && lastMailingContract.getMailingUnit() != null) {
	    unit = lastMailingContract.getMailingUnit();
	}
	return unit;
    }

    public String getLastMailingUnitCodeInYear() {
	Unit unit = getLastMailingUnitInYear();
	if (unit != null) {
	    return new DecimalFormat("0000").format(unit.getCostCenterCode());
	}
	return "";
    }

    public void calculateArticles17And18() {
	YearMonthDay beginDate = new YearMonthDay(getYear(), 1, 1);
	YearMonthDay endDate = new YearMonthDay(getYear(), 12, 31);
	AssiduousnessStatusHistory assiduousnessStatusHistory = getAssiduousness().getLastAssiduousnessStatusHistoryBetween(
		beginDate, endDate);
	Duration totalWorkedTime = Duration.ZERO;
	if (assiduousnessStatusHistory == null || assiduousnessStatusHistory.getAssiduousnessClosedMonths().size() == 0) {
	    setEfectiveWorkDays(0);
	} else {
	    for (AssiduousnessClosedMonth assiduousnessClosedMonth : assiduousnessStatusHistory.getAssiduousnessClosedMonths()) {
		if (assiduousnessClosedMonth.getClosedMonth() != null
			&& assiduousnessClosedMonth.getClosedMonth().getClosedYearMonth().get(DateTimeFieldType.year()) == (getYear() - 1)) {
		    totalWorkedTime = totalWorkedTime.plus(assiduousnessClosedMonth.getTotalWorkedTime());
		}
	    }
	    if (!totalWorkedTime.equals(Duration.ZERO)) {
		if (beginDate.isBefore(assiduousnessStatusHistory.getBeginDate())) {
		    beginDate = assiduousnessStatusHistory.getBeginDate();
		}
		if (assiduousnessStatusHistory.getEndDate() != null && endDate.isAfter(assiduousnessStatusHistory.getEndDate())) {
		    endDate = assiduousnessStatusHistory.getEndDate();
		}
		List<Schedule> schedules = getAssiduousness().getSchedules(beginDate, endDate);
		Duration averageWorkPeriodDuration = Duration.ZERO;
		for (Schedule schedule : schedules) {
		    averageWorkPeriodDuration = averageWorkPeriodDuration.plus(schedule.getAverageWorkPeriodDuration());
		}
		averageWorkPeriodDuration = new Duration(averageWorkPeriodDuration.getMillis() / schedules.size());
		int efectiveWorkDays = (int) (totalWorkedTime.getMillis() / averageWorkPeriodDuration.getMillis());
		setEfectiveWorkDays(efectiveWorkDays);
	    } else {
		setEfectiveWorkDays(0);
	    }
	}
    }

    public Integer getArt17And18MaximumLimitDays() {
	return Math.max(Math.round((float) (getEfectiveWorkDays() * new Double(0.08))), AssiduousnessExemption
		.getAssiduousnessExemptionDaysQuantity(getYear()));
    }

    public Integer getNumberOfArt17() {
	return Math.max((getArt17And18LimitDays() - getNumberOfArt18()), 0);
    }

    public Integer getNumberOfArt18() {
	return Math.min(AssiduousnessExemption.getAssiduousnessExemptionDaysQuantity(getYear()), getArt17And18MaximumLimitDays());
    }

    public Integer getArt17And18LimitDays() {
	return Math.min((getNumberOfArt18() + ART_17_MAXIMUM), getArt17And18MaximumLimitDays());
    }

    public void delete() {
	removeAssiduousness();
	removeRootDomainObject();
	deleteDomainObject();
    }
}
