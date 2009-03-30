package net.sourceforge.fenixedu.domain.assiduousness;

import java.text.DecimalFormat;
import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.util.AssiduousnessState;
import net.sourceforge.fenixedu.domain.assiduousness.util.JustificationGroup;
import net.sourceforge.fenixedu.domain.assiduousness.util.JustificationType;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.EmployeeContract;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Duration;
import org.joda.time.LocalDate;
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
	setNoWorkDaysAndArticles();
    }

    public Double getTotalDays() {
	return getNormalWithLeavesDiscount() + getAntiquityDays() + getAgeDays() + getAccumulatedDays() + getBonusDays()
		+ getLowTimeVacationsDays() + getAccumulatedArticle17Days() + getExtraWorkDays() + getLastYearExtraWorkDays();
    }

    public void edit(Double normalDays, Double normalWithLeavesDiscount, Double antiquityDays, Double ageDays,
	    Double accumulatedDays, Double bonusDays, Double lowTimeVacationsDays, Double accumulatedArticle17Days,
	    Double extraWorkDays, Double lastYearExtraWorkDays, DateTime lastModifiedDate) {
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
	LocalDate thisYearBeginDate = new LocalDate(getYear(), 1, 1);
	LocalDate thisYearEndDate = new LocalDate(getYear(), 12, 31);
	if (getAssiduousness().isStatusActive(thisYearBeginDate, thisYearEndDate)) {
	    LocalDate beginDate = new LocalDate((getYear() - 1), 1, 1);
	    LocalDate endDate = new LocalDate((getYear() - 1), 12, 31);
	    List<AssiduousnessStatusHistory> assiduousnessStatusHistories = getAssiduousness().getStatusBetween(beginDate,
		    endDate);
	    if (hasAnyAssiduousnessStatusHistoryBefore(assiduousnessStatusHistories, thisYearBeginDate.minusMonths(6))) {
		int efectiveWorkDays = 0;
		for (AssiduousnessStatusHistory assiduousnessStatusHistory : assiduousnessStatusHistories) {
		    efectiveWorkDays = efectiveWorkDays
			    + calculateEfectiveWorkDays(beginDate, endDate, assiduousnessStatusHistory);
		}
		setEfectiveWorkDays(efectiveWorkDays);
		setEfectiveWorkYear(getYear() - 1);
		if (efectiveWorkDays != 0) {
		    int exemptionDaysQuantity = AssiduousnessExemption.getAssiduousnessExemptionDaysQuantity(getYear());
		    int maxDays = Math.round((float) (getEfectiveWorkDays() * new Double(0.08)));
		    setArticlesValues(exemptionDaysQuantity, maxDays);
		}
	    } else {
		// menos de 6 meses de contrato

		assiduousnessStatusHistories = getAssiduousness().getStatusBetween(thisYearBeginDate, thisYearEndDate);
		int efectiveWorkDays = 0;
		LocalDate begin = thisYearBeginDate;
		for (AssiduousnessStatusHistory assiduousnessStatusHistory : assiduousnessStatusHistories) {
		    if (assiduousnessStatusHistory.getBeginDate().isAfter(thisYearBeginDate)) {
			begin = assiduousnessStatusHistory.getBeginDate();
		    }
		    efectiveWorkDays = efectiveWorkDays
			    + calculateEfectiveWorkDays(thisYearBeginDate, thisYearEndDate, assiduousnessStatusHistory);
		}
		setEfectiveWorkDays(0);
		setEfectiveWorkYear(getYear());
		if (efectiveWorkDays != 0) {
		    int exemptionDaysQuantity = AssiduousnessExemption.getAssiduousnessExemptionDaysQuantityByDate(begin);
		    int maxDays = (int) (efectiveWorkDays / 22 * 1.5);
		    setArticlesValues(exemptionDaysQuantity, maxDays);
		}
	    }
	} else {
	    setNoWorkDaysAndArticles();
	}
    }

    private boolean hasAnyAssiduousnessStatusHistoryBefore(List<AssiduousnessStatusHistory> assiduousnessStatusHistories,
	    LocalDate date) {
	for (AssiduousnessStatusHistory assiduousnessStatusHistory : assiduousnessStatusHistories) {
	    if (assiduousnessStatusHistory.getAssiduousnessStatus().getState() == AssiduousnessState.ACTIVE
		    && assiduousnessStatusHistory.getBeginDate().isBefore(date)) {
		return true;
	    }
	}
	return false;
    }

    private void setArticlesValues(int exemptionDaysQuantity, int maxDays) {
	setArt17And18MaximumLimitDays(Math.max(maxDays, exemptionDaysQuantity));
	setNumberOfArt18(Math.min(exemptionDaysQuantity, getArt17And18MaximumLimitDays()));
	setArt17And18LimitDays(Math.min((getNumberOfArt18() + ART_17_MAXIMUM), getArt17And18MaximumLimitDays()));
	setNumberOfArt17(Math.max((getArt17And18LimitDays() - getNumberOfArt18()), 0));
    }

    private int calculateEfectiveWorkDays(LocalDate beginDate, LocalDate endDate,
	    AssiduousnessStatusHistory assiduousnessStatusHistory) {
	Duration totalWorkedTime = getTotalWorkedTime(assiduousnessStatusHistory, beginDate.getYear());
	int efectiveWorkDays = 0;
	if (!totalWorkedTime.equals(Duration.ZERO)) {
	    Duration averageWorkPeriodDuration = assiduousnessStatusHistory.getSheculeWeightedAverage(beginDate, endDate);
	    efectiveWorkDays = Math.round((float) (totalWorkedTime.getMillis() * Math.pow(averageWorkPeriodDuration.getMillis(),
		    -1)));
	} else {
	    setNoWorkDaysAndArticles();
	}
	return efectiveWorkDays;
    }

    private Duration getTotalWorkedTime(AssiduousnessStatusHistory assiduousnessStatusHistory, int year) {
	Duration totalWorkedTime = Duration.ZERO;
	for (AssiduousnessClosedMonth assiduousnessClosedMonth : assiduousnessStatusHistory.getAssiduousnessClosedMonths()) {
	    if (assiduousnessClosedMonth.getClosedMonth() != null
		    && assiduousnessClosedMonth.getClosedMonth().getClosedYearMonth().get(DateTimeFieldType.year()) == year) {
		totalWorkedTime = totalWorkedTime.plus(assiduousnessClosedMonth.getTotalWorkedTime());
	    }
	}
	return totalWorkedTime;
    }

    private void setNoWorkDaysAndArticles() {
	setEfectiveWorkDays(0);
	setEfectiveWorkYear(getYear());
	setArt17And18MaximumLimitDays(0);
	setNumberOfArt18(0);
	setArt17And18LimitDays(0);
	setNumberOfArt17(0);
    }

    public void delete() {
	removeAssiduousness();
	removeRootDomainObject();
	deleteDomainObject();
    }
}
