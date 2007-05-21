package net.sourceforge.fenixedu.applicationTier.Servico.assiduousness;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.AssiduousnessExportChoices;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeWorkSheet;
import net.sourceforge.fenixedu.domain.assiduousness.Assiduousness;

public class ReadAllAssiduousnessWorkSheets extends Service {

    public List<EmployeeWorkSheet> run(AssiduousnessExportChoices assiduousnessExportChoices) {
	final List<EmployeeWorkSheet> employeeWorkSheetList = new ArrayList<EmployeeWorkSheet>();
	ReadAssiduousnessWorkSheet workSheet = new ReadAssiduousnessWorkSheet();
	for (Assiduousness assiduousness : assiduousnessExportChoices.getAssiduousnesses()) {
	    employeeWorkSheetList.add(workSheet.run(assiduousness, assiduousnessExportChoices
		    .getBeginDate(), assiduousnessExportChoices.getEndDate()));
	}
	return employeeWorkSheetList;
    }

}
