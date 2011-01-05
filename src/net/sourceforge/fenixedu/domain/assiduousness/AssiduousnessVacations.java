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
import org.joda.time.YearMonthDay;

public class AssiduousnessVacations extends AssiduousnessVacations_Base {

    protected static final int ART_17_MAXIMUM = 12;

    public AssiduousnessVacations(Assiduousness assiduousness, Integer year, Double normalDays, Double normalWithLeavesDiscount,
	    Double antiquityDays, Double ageDays, Double accumulatedDays, Double imprescriptibleAccumulatedDays,
	    Double bonusDays, Double lowTimeVacationsDays, Double accumulatedArticle17Days, Double extraWorkDays,
	    Double lastYearExtraWorkDays, DateTime lastModifiedDate) {
	setRootDomainObject(RootDomainObject.getInstance());
	setAssiduousness(assiduousness);
	setYear(year);
	setNormalDays(normalDays);
	setNormalWithLeavesDiscount(normalWithLeavesDiscount);
	setAntiquityDays(antiquityDays);
	setAgeDays(ageDays);
	setAccumulatedDays(accumulatedDays);
	setImprescriptibleAccumulatedDays(imprescriptibleAccumulatedDays);
	setBonusDays(bonusDays);
	setLowTimeVacationsDays(lowTimeVacationsDays);
	setAccumulatedArticle17Days(accumulatedArticle17Days);
	setExtraWorkDays(extraWorkDays);
	setLastYearExtraWorkDays(lastYearExtraWorkDays);
	setLastModifiedDate(lastModifiedDate);
	// setNoWorkDaysAndArticles();
    }

    public Double getTotalDays() {
	return getNormalWithLeavesDiscount() + getAntiquityDays() + getAgeDays() + getAccumulatedDays()
		+ getImprescriptibleAccumulatedDays() + getBonusDays() + getLowTimeVacationsDays()
		+ getAccumulatedArticle17Days() + getExtraWorkDays() + getLastYearExtraWorkDays();
    }

    public void edit(Double normalDays, Double normalWithLeavesDiscount, Double antiquityDays, Double ageDays,
	    Double accumulatedDays, Double imprescriptibleAccumulatedDays, Double bonusDays, Double lowTimeVacationsDays,
	    Double accumulatedArticle17Days, Double extraWorkDays, Double lastYearExtraWorkDays, DateTime lastModifiedDate) {
	setNormalDays(normalDays);
	setNormalWithLeavesDiscount(normalWithLeavesDiscount);
	setAntiquityDays(antiquityDays);
	setAgeDays(ageDays);
	setAccumulatedDays(accumulatedDays);
	setImprescriptibleAccumulatedDays(imprescriptibleAccumulatedDays);
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

    public void delete() {
	removeAssiduousness();
	removeRootDomainObject();
	deleteDomainObject();
    }
}
