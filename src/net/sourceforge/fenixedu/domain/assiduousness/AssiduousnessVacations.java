package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.util.JustificationGroup;
import net.sourceforge.fenixedu.domain.assiduousness.util.JustificationType;

import org.joda.time.DateTime;

public class AssiduousnessVacations extends AssiduousnessVacations_Base {

    public AssiduousnessVacations(Assiduousness assiduousness, Integer year, Double normalDays, Double normalWithLeavesDiscount,
	    Double antiquityDays, Double ageDays, Double accumulatedDays, Double bonusDays, Double lowTimeVacationsDays,
	    Double article17Days, Double extraWorkDays, Double lastYearExtraWorkDays, DateTime lastModifiedDate) {
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
	setArticle17Days(article17Days);
	setExtraWorkDays(extraWorkDays);
	setLastYearExtraWorkDays(lastYearExtraWorkDays);
	setLastModifiedDate(lastModifiedDate);
    }

    public Double getTotalDays() {
	return getNormalWithLeavesDiscount() + getAntiquityDays() + getAgeDays() + getAccumulatedDays() + getBonusDays()
		+ getLowTimeVacationsDays() + getArticle17Days() + getExtraWorkDays() + getLastYearExtraWorkDays();
    }

    public void edit(Double normalDays, Double normalWithLeavesDiscount, Double antiquityDays, Double ageDays,
	    Double accumulatedDays, Double lowTimeVacationsDays, Double article17Days, DateTime lastModifiedDate) {
	setNormalDays(normalDays);
	setNormalWithLeavesDiscount(normalWithLeavesDiscount);
	setAntiquityDays(antiquityDays);
	setAgeDays(ageDays);
	setAccumulatedDays(accumulatedDays);
	setLowTimeVacationsDays(lowTimeVacationsDays);
	setArticle17Days(article17Days);
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

    public void delete() {
	removeAssiduousness();
	removeRootDomainObject();
	deleteDomainObject();
    }
}
