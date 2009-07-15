package net.sourceforge.fenixedu.applicationTier.strategy.assiduousness.strategys;

import net.sourceforge.fenixedu.dataTransferObject.assiduousness.WorkDaySheet;
import net.sourceforge.fenixedu.domain.assiduousness.Assiduousness;

public interface ICalculateDailyWorkSheetStrategy {

    public WorkDaySheet calculateDailyBalance(Assiduousness assiduousness, WorkDaySheet workDaySheet, boolean isDayHoliday);

    public WorkDaySheet calculateDailyBalance(Assiduousness assiduousness, WorkDaySheet workDaySheet, boolean isDayHoliday,
	    boolean closingMonth);

}