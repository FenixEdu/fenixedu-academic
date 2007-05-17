package net.sourceforge.fenixedu.domain.assiduousness;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTime;

public class AssiduousnessVacations extends AssiduousnessVacations_Base {

    public AssiduousnessVacations(Assiduousness assiduousness, Integer year, Double normalDays,
	    Double normalWithLeavesDiscount, Double antiquityDays, Double ageDays,
	    Double accumulatedDays, Double bonusDays, Double lowTimeVacationsDays, Double article17Days,
	    Double extraWorkDays, Double lastYearExtraWorkDays, DateTime lastModifiedDate) {
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
	return getNormalWithLeavesDiscount() + getAntiquityDays() + getAgeDays() + getAccumulatedDays()
		+ getBonusDays() + getLowTimeVacationsDays() + getArticle17Days() + getExtraWorkDays()
		+ getLastYearExtraWorkDays();
    }
}
