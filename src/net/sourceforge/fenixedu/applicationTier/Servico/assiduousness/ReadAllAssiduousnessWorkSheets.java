package net.sourceforge.fenixedu.applicationTier.Servico.assiduousness;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.AssiduousnessExportChoices;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeWorkSheet;
import net.sourceforge.fenixedu.domain.assiduousness.Assiduousness;

import org.joda.time.LocalDate;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class ReadAllAssiduousnessWorkSheets extends FenixService {

    @Checked("RolePredicates.PERSONNEL_SECTION_PREDICATE")
    @Service
    public static List<EmployeeWorkSheet> run(AssiduousnessExportChoices assiduousnessExportChoices) {
	final List<EmployeeWorkSheet> employeeWorkSheetList = new ArrayList<EmployeeWorkSheet>();
	for (Assiduousness assiduousness : assiduousnessExportChoices.getAssiduousnesses()) {

	    LocalDate thisDate = new LocalDate(assiduousnessExportChoices.getBeginDate().getYear(), assiduousnessExportChoices
		    .getBeginDate().getMonthOfYear(), 1);

	    for (; !thisDate.isAfter(assiduousnessExportChoices.getEndDate()); thisDate = thisDate.plusMonths(1)) {
		LocalDate beginDate = thisDate;
		if (thisDate.getYear() == assiduousnessExportChoices.getBeginDate().getYear()
			&& thisDate.getMonthOfYear() == assiduousnessExportChoices.getBeginDate().getMonthOfYear()) {
		    beginDate = assiduousnessExportChoices.getBeginDate();
		}
		LocalDate endDate = new LocalDate(beginDate.getYear(), beginDate.getMonthOfYear(), beginDate.dayOfMonth()
			.getMaximumValue());
		if (endDate.isAfter(assiduousnessExportChoices.getEndDate())) {
		    endDate = assiduousnessExportChoices.getEndDate();
		}

		EmployeeWorkSheet employeeWorkSheet = ReadAssiduousnessWorkSheet.run(assiduousness, beginDate, endDate);
		if (!employeeWorkSheet.getWorkDaySheetList().isEmpty()) {
		    employeeWorkSheetList.add(employeeWorkSheet);
		}
	    }
	}
	return employeeWorkSheetList;
    }
}