package net.sourceforge.fenixedu.applicationTier.Servico.assiduousness;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeWorkSheet;
import net.sourceforge.fenixedu.domain.assiduousness.Assiduousness;

import org.joda.time.YearMonthDay;

public class ReadAllAssiduousnessWorkSheets extends Service {

    public List<EmployeeWorkSheet> run(YearMonthDay beginDate, YearMonthDay endDate) {
        final List<EmployeeWorkSheet> employeeWorkSheetList = new ArrayList<EmployeeWorkSheet>();        

        ReadAssiduousnessWorkSheet workSheet = new ReadAssiduousnessWorkSheet();
        for (Assiduousness assiduousness : rootDomainObject.getAssiduousnesss()) {
            if (assiduousness.isStatusActive(beginDate, endDate)) {
                EmployeeWorkSheet employeeWorkSheet = workSheet.run(assiduousness, beginDate,
                        endDate);
                employeeWorkSheetList.add(employeeWorkSheet);
            }
        }
        return employeeWorkSheetList;
    } 
}
