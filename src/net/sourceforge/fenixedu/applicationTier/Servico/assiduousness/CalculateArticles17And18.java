package net.sourceforge.fenixedu.applicationTier.Servico.assiduousness;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessStatusHistory;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessVacations;

import org.joda.time.YearMonthDay;

public class CalculateArticles17And18 extends Service {

    public void run(int year) {
	YearMonthDay beginDate = new YearMonthDay(year, 1, 1);
	YearMonthDay endDate = new YearMonthDay(year, 12, 31);
	for (AssiduousnessVacations assiduousnessVacations : rootDomainObject.getAssiduousnessVacations()) {
	    if (assiduousnessVacations.getYear().equals(year)) {
		AssiduousnessStatusHistory assiduousnessStatusHistory = assiduousnessVacations.getAssiduousness()
			.getLastAssiduousnessStatusHistoryBetween(beginDate, endDate);
		if (assiduousnessStatusHistory != null) {
		    assiduousnessVacations.calculateArticles17And18();
		}
	    }
	}
	return;
    }
}
