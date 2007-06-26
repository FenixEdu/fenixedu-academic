package net.sourceforge.fenixedu.domain.assiduousness;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeExtraWorkAuthorizationBean;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.Month;
import net.sourceforge.fenixedu.util.report.StyledExcelSpreadsheet;

import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;

public class EmployeeExtraWorkAuthorization extends EmployeeExtraWorkAuthorization_Base {

    public EmployeeExtraWorkAuthorization(ExtraWorkAuthorization extraWorkAuthorization,
	    EmployeeExtraWorkAuthorizationBean employeeExtraWorkAuthorizationBean) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setExtraWorkAuthorization(extraWorkAuthorization);
	setAssiduousness(employeeExtraWorkAuthorizationBean.getEmployee().getAssiduousness());
	setModifiedBy(employeeExtraWorkAuthorizationBean.getModifiedBy());
	if (!employeeExtraWorkAuthorizationBean.getAuxiliarPersonel()
		&& !employeeExtraWorkAuthorizationBean.getExecutiveAuxiliarPersonel()
		&& !employeeExtraWorkAuthorizationBean.getNightExtraWork()
		&& !employeeExtraWorkAuthorizationBean.getNormalExtraWork()
		&& !employeeExtraWorkAuthorizationBean.getNormalExtraWorkPlusOneHundredHours()
		&& !employeeExtraWorkAuthorizationBean.getNormalExtraWorkPlusTwoHours()
		&& !employeeExtraWorkAuthorizationBean.getWeeklyRestExtraWork()) {
	    throw new DomainException("error.extraWorkAuthorization.options");
	}
	setAuxiliarPersonel(employeeExtraWorkAuthorizationBean.getAuxiliarPersonel());
	setExecutiveAuxiliarPersonel(employeeExtraWorkAuthorizationBean.getExecutiveAuxiliarPersonel());
	setNightExtraWork(employeeExtraWorkAuthorizationBean.getNightExtraWork());
	setNormalExtraWork(employeeExtraWorkAuthorizationBean.getNormalExtraWork());
	setNormalExtraWorkPlusOneHundredHours(employeeExtraWorkAuthorizationBean
		.getNormalExtraWorkPlusOneHundredHours());
	setNormalExtraWorkPlusTwoHours(employeeExtraWorkAuthorizationBean
		.getNormalExtraWorkPlusTwoHours());
	setWeeklyRestExtraWork(employeeExtraWorkAuthorizationBean.getWeeklyRestExtraWork());
	setLastModifiedDate(new DateTime());
    }

    public void edit(EmployeeExtraWorkAuthorizationBean employeeExtraWorkAuthorizationBean) {
	if (!employeeExtraWorkAuthorizationBean.getAuxiliarPersonel()
		&& !employeeExtraWorkAuthorizationBean.getExecutiveAuxiliarPersonel()
		&& !employeeExtraWorkAuthorizationBean.getNightExtraWork()
		&& !employeeExtraWorkAuthorizationBean.getNormalExtraWork()
		&& !employeeExtraWorkAuthorizationBean.getNormalExtraWorkPlusOneHundredHours()
		&& !employeeExtraWorkAuthorizationBean.getNormalExtraWorkPlusTwoHours()
		&& !employeeExtraWorkAuthorizationBean.getWeeklyRestExtraWork()) {
	    throw new DomainException("error.extraWorkAuthorization.options");
	}
	setModifiedBy(employeeExtraWorkAuthorizationBean.getModifiedBy());
	setAuxiliarPersonel(employeeExtraWorkAuthorizationBean.getAuxiliarPersonel());
	setExecutiveAuxiliarPersonel(employeeExtraWorkAuthorizationBean.getExecutiveAuxiliarPersonel());
	setNightExtraWork(employeeExtraWorkAuthorizationBean.getNightExtraWork());
	setNormalExtraWork(employeeExtraWorkAuthorizationBean.getNormalExtraWork());
	setNormalExtraWorkPlusOneHundredHours(employeeExtraWorkAuthorizationBean
		.getNormalExtraWorkPlusOneHundredHours());
	setNormalExtraWorkPlusTwoHours(employeeExtraWorkAuthorizationBean
		.getNormalExtraWorkPlusTwoHours());
	setWeeklyRestExtraWork(employeeExtraWorkAuthorizationBean.getWeeklyRestExtraWork());
	setLastModifiedDate(new DateTime());
    }

    public void delete() {
	removeRootDomainObject();
	removeAssiduousness();
	removeExtraWorkAuthorization();
	removeModifiedBy();
	deleteDomainObject();
    }

    public static void getExcelHeader(StyledExcelSpreadsheet spreadsheet, ResourceBundle bundle,
	    ResourceBundle enumBundle) {
	spreadsheet.newHeaderRow();
	spreadsheet.addHeader(bundle.getString("label.number"));
	spreadsheet.addHeader(bundle.getString("label.employee.name"), 10000);
	for (Month month : Month.values()) {
	    spreadsheet.addHeader(enumBundle.getString(month.getName()));
	}
	spreadsheet.addHeader(bundle.getString("label.total"));
    }

    public static void getExcelFooter(StyledExcelSpreadsheet spreadsheet, ResourceBundle bundle) {
	int lastRow = spreadsheet.getSheet().getLastRowNum();
	int lastColumn = spreadsheet.getSheet().getRow(7).getLastCellNum() - 1;
	spreadsheet.newRow();
	spreadsheet.newRow();
	spreadsheet.addCell(bundle.getString("label.total").toUpperCase());
	spreadsheet.sumColumn(8, lastRow, 2, lastColumn, spreadsheet.getExcelStyle().getDoubleStyle());
	spreadsheet.newRow();
	spreadsheet.newRow();
	DecimalFormat decimalFormat = new DecimalFormat("0.00");
	DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
	decimalFormatSymbols.setDecimalSeparator('.');
	decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
	spreadsheet.sumRows(8, lastRow, 2, lastColumn, spreadsheet.getExcelStyle().getDoubleStyle());
    }

    public void getExcelRow(StyledExcelSpreadsheet spreadsheet, int year) {
	DecimalFormat decimalFormat = new DecimalFormat("0.00");
	DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
	decimalFormatSymbols.setDecimalSeparator('.');
	decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
	spreadsheet.newRow();
	spreadsheet.addCell(getAssiduousness().getEmployee().getEmployeeNumber().toString());
	spreadsheet.addCell(getAssiduousness().getEmployee().getPerson().getName());
	List<ExtraWorkRequest> extraWorkRequests = getAssiduousness().getExtraWorkRequestsByUnit(
		getExtraWorkAuthorization().getPayingUnit(), year);
	for (ExtraWorkRequest extraWorkRequest : extraWorkRequests) {
	    spreadsheet.addCell(new Double(decimalFormat.format(extraWorkRequest.getAmount())),
		    spreadsheet.getExcelStyle().getDoubleStyle(), extraWorkRequest
			    .getPartialPayingDate().get(DateTimeFieldType.monthOfYear()) + 2);
	}
    }
}
