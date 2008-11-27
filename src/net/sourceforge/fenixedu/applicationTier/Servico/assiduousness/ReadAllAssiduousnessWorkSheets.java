package net.sourceforge.fenixedu.applicationTier.Servico.assiduousness;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.AssiduousnessExportChoices;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeWorkSheet;
import net.sourceforge.fenixedu.domain.assiduousness.Assiduousness;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class ReadAllAssiduousnessWorkSheets extends FenixService {

    @Checked("RolePredicates.PERSONNEL_SECTION_PREDICATE")
    @Service
    public static List<EmployeeWorkSheet> run(AssiduousnessExportChoices assiduousnessExportChoices) {
	final List<EmployeeWorkSheet> employeeWorkSheetList = new ArrayList<EmployeeWorkSheet>();
	ReadAssiduousnessWorkSheet workSheet = new ReadAssiduousnessWorkSheet();
	for (Assiduousness assiduousness : assiduousnessExportChoices.getAssiduousnesses()) {
	    employeeWorkSheetList.add(workSheet.run(assiduousness, assiduousnessExportChoices.getBeginDate(),
		    assiduousnessExportChoices.getEndDate()));
	}
	return employeeWorkSheetList;
    }

}