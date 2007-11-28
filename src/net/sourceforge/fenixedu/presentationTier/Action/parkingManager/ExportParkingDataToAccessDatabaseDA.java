package net.sourceforge.fenixedu.presentationTier.Action.parkingManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.research.result.OpenFileBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.parking.ParkingGroup;
import net.sourceforge.fenixedu.domain.parking.ParkingParty;
import net.sourceforge.fenixedu.domain.parking.Vehicle;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.DateFormatUtil;
import net.sourceforge.fenixedu.util.projectsManagement.ExcelStyle;
import net.sourceforge.fenixedu.util.report.Spreadsheet;
import net.sourceforge.fenixedu.util.report.Spreadsheet.Row;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.FileUtils;

import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.Table;

public class ExportParkingDataToAccessDatabaseDA extends FenixDispatchAction {

    public ActionForward prepareExportFile(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("openFileBean", new OpenFileBean());

	return mapping.findForward("exportFile");
    }

    public ActionForward mergeFilesAndExportToExcel(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	OpenFileBean openFileBean = (OpenFileBean) getRenderedObject();
	if (openFileBean != null) {

	    File dbFile = FileUtils.copyToTemporaryFile(openFileBean.getInputStream());
	    Database database = Database.open(dbFile, Boolean.FALSE, Boolean.TRUE);
	    Table table = database.getTable("XML");

	    List<ParkingParty> parkingParties = getValidParkingParties();
	    final HSSFWorkbook workbook = new HSSFWorkbook();
	    final ExcelStyle excelStyle = new ExcelStyle(workbook);

	    final Spreadsheet parkingBDSpreadsheet = new Spreadsheet("BD Fénix Parque");
	    setHeaders(parkingBDSpreadsheet);

	    for (Map<String, Object> row = table.getNextRow(); row != null; row = table.getNextRow()) {
		Long cardNumber = new Long((String) row.get("Card"));
		ParkingParty parkingParty = getParkingPartyCardNumber(cardNumber, parkingParties);
		if (parkingParty != null) {
		    addRow(parkingBDSpreadsheet, parkingParty, row);
		}
	    }

	    for (ParkingParty parkingParty : parkingParties) {
		addNewRow(parkingBDSpreadsheet, parkingParty);
	    }

	    response.setContentType("text/plain");
	    response.setHeader("Content-disposition", "attachment; filename=parkingDB_merge.xls");
	    final ServletOutputStream writer = response.getOutputStream();
	    parkingBDSpreadsheet.exportToXLSSheet(workbook, excelStyle.getHeaderStyle(), excelStyle
		    .getStringStyle());
	    workbook.write(writer);
	    writer.flush();

	    response.flushBuffer();
	}
	return null;
    }
    
    private void addRow(final Spreadsheet parkingBDSpreadsheet, ParkingParty parkingParty,
	    Map<String, Object> accessTableRow) throws FenixServiceException {
	long thisInstant = Calendar.getInstance().getTimeInMillis();
	DateTime dateTime = new DateTime(thisInstant);
	final Row row = parkingBDSpreadsheet.addRow();
	Person person = null;
	if (parkingParty.getParty().isPerson()) {
	    person = (Person) parkingParty.getParty();
	}

	row.setCell(((Short) accessTableRow.get("Garage")).toString());
	row.setCell(parkingParty.getCardNumber().toString()); // cardNumber
	row.setCell(((Short) accessTableRow.get("Type")).toString());
	row.setCell(getBooleanString(accessTableRow.get("Access"))); // if the card is active or not
	row.setCell(convertParkingGroupToAccessDB(parkingParty.getParkingGroup())); // accessGroup
	row.setCell(((Short) accessTableRow.get("Fee")).toString());
	row.setCell(((Short) accessTableRow.get("SAC")).toString());
	row
		.setCell(DateFormatUtil.format("dd/MM/yyyy HH:mm:ss", (Date) accessTableRow
			.get("AlterDate")));
	row.setCell(DateFormatUtil.format("dd/MM/yyyy HH:mm:ss", (Date) accessTableRow
		.get("CreatedDate")));
	row.setCell(dateTime.toString("dd/MM/yyyy HH:mm:ss")); // editedDate
	row.setCell(person != null ? getName(person.getNickname()) : getName(parkingParty.getParty()
		.getName())); // name
	row.setCell((String) accessTableRow.get("Address"));
	String vehicle1PlateNumber = "";
	String vehicle2PlateNumber = "";
	int counter = 1;
	for (Vehicle vehicle : parkingParty.getVehicles()) {
	    if (counter == 1) {
		vehicle1PlateNumber = vehicle.getPlateNumber();
	    } else if (counter == 2) {
		vehicle2PlateNumber = vehicle.getPlateNumber();
	    } else {
		break;
	    }
	    counter++;
	}
	row.setCell(vehicle1PlateNumber); // license
	row.setCell(vehicle2PlateNumber); // licenseAlt	

	row.setCell(person != null && person.getWorkPhone() != null ? person.getWorkPhone() : ""); // registration
	row.setCell(person != null && person.getMobile() != null ? person.getMobile() : ""); // registrationAlt
	row.setCell((String) accessTableRow.get("ClientRef"));
	row.setCell((String) accessTableRow.get("Comment"));
	row.setCell(((Integer) accessTableRow.get("Price")).toString());

	String endValidityDate = parkingParty.getCardEndDate() == null ? "" : parkingParty
		.getCardEndDate().toString("dd/MM/yyyy HH:mm:ss");
	row.setCell(endValidityDate);
	row.setCell(DateFormatUtil.format("dd/MM/yyyy HH:mm:ss", (Date) accessTableRow
		.get("LastUsedDate")));
	row.setCell(getBooleanString(accessTableRow.get("Invoice")));
	row.setCell(parkingParty.getCardEndDate() != null ? "FALSE" : "TRUE"); // if true, start and end validity dates are ignored
	row.setCell(getBooleanString(accessTableRow.get("Present")));
	row.setCell(getBooleanString(accessTableRow.get("PayDirect")));
	row.setCell(getBooleanString(accessTableRow.get("APBCorrect"))); // if it's already in the park
	String startValidityDate = parkingParty.getCardStartDate() == null ? "" : parkingParty
		.getCardStartDate().toString("dd/MM/yyyy HH:mm:ss");
	row.setCell(startValidityDate);
	row.setCell(getBooleanString(accessTableRow.get("NoFee")));
    }

    private void addNewRow(final Spreadsheet parkingBDSpreadsheet, ParkingParty parkingParty)
	    throws FenixServiceException {
	DateTime dateTime = new DateTime();
	final Row row = parkingBDSpreadsheet.addRow();
	Person person = null;
	if (parkingParty.getParty().isPerson()) {
	    person = (Person) parkingParty.getParty();
	}
	row.setCell("0"); // garage
	row.setCell(parkingParty.getCardNumber().toString()); // cardNumber
	row.setCell("3"); // type Mirafe
	row.setCell("TRUE"); // access (if the card is active or not)
	row.setCell(convertParkingGroupToAccessDB(parkingParty.getParkingGroup()));
	row.setCell("1"); // fee
	row.setCell("0"); // SAC
	row.setCell("1/1/2000"); // alterDate
	row.setCell(dateTime.toString("dd/MM/yyyy HH:mm:ss")); // createdDate
	row.setCell(dateTime.toString("dd/MM/yyyy HH:mm:ss")); // editedDate
	row.setCell(person != null ? getName(person.getNickname()) : getName(parkingParty.getParty()
		.getName())); // name
	row.setCell(""); // address         
	String vehicle1PlateNumber = "";
	String vehicle2PlateNumber = "";
	int counter = 1;
	for (Vehicle vehicle : parkingParty.getVehicles()) {
	    if (counter == 1) {
		vehicle1PlateNumber = vehicle.getPlateNumber();
	    } else if (counter == 2) {
		vehicle2PlateNumber = vehicle.getPlateNumber();
	    } else {
		break;
	    }
	    counter++;
	}
	row.setCell(vehicle1PlateNumber); // license
	row.setCell(vehicle2PlateNumber); // licenseAlt
	row.setCell(person != null && person.getWorkPhone() != null ? getString(person.getWorkPhone(),
		19) : ""); // registration
	row.setCell(person != null && person.getMobile() != null ? getString(person.getMobile(), 19)
		: ""); // registrationAlt
	row.setCell(""); // clientRef
	row.setCell(""); // comment
	row.setCell("0"); // price
	String endValidityDate = parkingParty.getCardEndDate() == null ? "" : parkingParty
		.getCardEndDate().toString("dd/MM/yyyy HH:mm:ss");
	row.setCell(endValidityDate);
	row.setCell("1/1/2000"); // lastUsedDate
	row.setCell("FALSE"); // invoice
	row.setCell(parkingParty.getCardStartDate() != null ? "FALSE" : "TRUE"); // if true, start and end validity dates are ignored
	row.setCell("FALSE"); // present
	row.setCell("FALSE"); // payDirect
	row.setCell("FALSE"); // apbCorrect (if it's already in the park)
	String startValidityDate = parkingParty.getCardStartDate() == null ? "" : parkingParty
		.getCardStartDate().toString("dd/MM/yyyy HH:mm:ss");
	row.setCell(startValidityDate);
	row.setCell("TRUE"); // noFee
    }

    private String getName(String name) {
	if (name.length() > 59) { //max size of the other parking application DB
	    StringBuilder resultName = new StringBuilder();
	    resultName = new StringBuilder();
	    String[] names = name.split("\\p{Space}+");
	    for (int iter = 1; iter < names.length - 1; iter++) {
		if (names[iter].length() > 5) {
		    names[iter] = names[iter].substring(0, 5) + ".";
		}
	    }
	    for (int iter = 0; iter < names.length; iter++) {
		resultName.append(names[iter]).append(" ");
	    }
	    if (resultName.length() > 59) {
		resultName = new StringBuilder(names[0]).append(" ").append(names[names.length - 1]);
	    }
	    return resultName.toString().trim();
	} else {
	    return name;
	}

    }

    private void setHeaders(final Spreadsheet spreadsheet) {
	spreadsheet.setHeader("Garage");
	spreadsheet.setHeader("Card");
	spreadsheet.setHeader("Type");
	spreadsheet.setHeader("Access");
	spreadsheet.setHeader("AccessGroup");
	spreadsheet.setHeader("Fee");
	spreadsheet.setHeader("SAC");
	spreadsheet.setHeader("AlterDate");
	spreadsheet.setHeader("CreatedDate");
	spreadsheet.setHeader("EditedDate");
	spreadsheet.setHeader("Name");
	spreadsheet.setHeader("Address");
	spreadsheet.setHeader("License");
	spreadsheet.setHeader("LicenseAlt");
	spreadsheet.setHeader("Registration");
	spreadsheet.setHeader("RegistrationAlt");
	spreadsheet.setHeader("ClientRef");
	spreadsheet.setHeader("Comment");
	spreadsheet.setHeader("Price");
	spreadsheet.setHeader("EndValidityDate");
	spreadsheet.setHeader("LastUsedDate");
	spreadsheet.setHeader("Invoice");
	spreadsheet.setHeader("Unlimited");
	spreadsheet.setHeader("Present");
	spreadsheet.setHeader("PayDirect");
	spreadsheet.setHeader("APBCorrect");
	spreadsheet.setHeader("StartValidityDate");
	spreadsheet.setHeader("NoFee");
    }

    public ActionForward mergeFilesAndExport(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	OpenFileBean openFileBean = (OpenFileBean) getRenderedObject();
	if (openFileBean != null) {

	    File dbFile = FileUtils.copyToTemporaryFile(openFileBean.getInputStream());
	    Database database = Database.open(dbFile, Boolean.FALSE, Boolean.TRUE);
	    Table table = database.getTable("XML");

	    List<ParkingParty> parkingParties = getValidParkingParties();
	    List<Object[]> rows = new ArrayList<Object[]>();
	    for (Map<String, Object> row = table.getNextRow(); row != null; row = table.getNextRow()) {
		Long cardNumber = new Long((String) row.get("Card"));
		ParkingParty parkingParty = getParkingPartyCardNumber(cardNumber, parkingParties);
		if (parkingParty != null) {
		    table.deleteCurrentRow();
		    rows.add(updateRow(parkingParty, row));
		}
	    }
	    table.addRows(rows);

	    for (ParkingParty parkingParty : parkingParties) {
		Object[] newRow = new Object[28];
		fillInRow(parkingParty, newRow);
		table.addRow(newRow);
	    }
	    database.flush();
	    database.close();

	    response.setContentType("application/vnd.ms-access");
	    response.setHeader("Content-disposition", "attachment; filename=Cartões_XML.mdb");
	    final ServletOutputStream writer = response.getOutputStream();
	    writer.write(pt.utl.ist.fenix.tools.file.utils.FileUtils.readByteArray(dbFile));
	    writer.flush();
	    writer.close();

	    response.flushBuffer();
	}
	return null;
    }

    private Object[] updateRow(ParkingParty parkingParty, Map<String, Object> accessTableRow)
	    throws FenixServiceException {
	long thisInstant = Calendar.getInstance().getTimeInMillis();
	DateTime dateTime = new DateTime(thisInstant);
	Person person = null;
	if (parkingParty.getParty().isPerson()) {
	    person = (Person) parkingParty.getParty();
	}

	Object[] newRow = new Object[28];
	newRow[0] = accessTableRow.get("Garage");
	newRow[1] = parkingParty.getCardNumber().toString();
	newRow[2] = accessTableRow.get("Type");
	newRow[3] = accessTableRow.get("Access");
	newRow[4] = Integer.valueOf(convertParkingGroupToAccessDB(parkingParty.getParkingGroup()))
		.intValue();
	newRow[5] = accessTableRow.get("Fee");
	newRow[6] = accessTableRow.get("SAC");
	newRow[7] = accessTableRow.get("AlterDate");
	newRow[8] = dateTime.toDate();
	newRow[9] = dateTime.toDate();
	newRow[10] = person != null ? getName(person.getNickname()) : getName(parkingParty.getParty()
		.getName());
	newRow[11] = accessTableRow.get("Address");
	String vehicle1PlateNumber = "";
	String vehicle2PlateNumber = "";
	int counter = 1;
	for (Vehicle vehicle : parkingParty.getVehicles()) {
	    if (counter == 1) {
		vehicle1PlateNumber = vehicle.getPlateNumber();
	    } else if (counter == 2) {
		vehicle2PlateNumber = vehicle.getPlateNumber();
	    } else {
		break;
	    }
	    counter++;
	}
	newRow[12] = vehicle1PlateNumber;
	newRow[13] = vehicle2PlateNumber;
	newRow[14] = person != null && person.getWorkPhone() != null ? getString(person.getWorkPhone(),
		19) : "";
	newRow[15] = person != null && person.getMobile() != null ? getString(person.getMobile(), 19)
		: "";
	newRow[16] = accessTableRow.get("ClientRef");
	newRow[17] = accessTableRow.get("Comment");
	newRow[18] = accessTableRow.get("Price");
	newRow[19] = parkingParty.getCardEndDate() == null ? null : parkingParty.getCardEndDate()
		.toDate();
	newRow[20] = accessTableRow.get("LastUsedDate");
	newRow[21] = accessTableRow.get("Invoice");
	newRow[22] = parkingParty.getCardStartDate() != null ? Boolean.FALSE : Boolean.TRUE;
	newRow[23] = accessTableRow.get("Present");
	newRow[24] = accessTableRow.get("PayDirect");
	newRow[25] = accessTableRow.get("APBCorrect");
	newRow[26] = accessTableRow.get("NoFee");
	newRow[27] = parkingParty.getCardStartDate() == null ? null : parkingParty.getCardStartDate()
		.toDate();
	return newRow;
    }

    private void fillInRow(ParkingParty parkingParty, Object[] newRow) throws Exception {
	DateTime dateTime = new DateTime();
	Person person = null;
	if (parkingParty.getParty().isPerson()) {
	    person = (Person) parkingParty.getParty();
	}

	newRow[0] = 0;
	newRow[1] = parkingParty.getCardNumber().toString();
	newRow[2] = 3;
	newRow[3] = Boolean.TRUE;
	newRow[4] = Integer.valueOf(convertParkingGroupToAccessDB(parkingParty.getParkingGroup()))
		.intValue();
	newRow[5] = 1;
	newRow[6] = 0;
	newRow[7] = new YearMonthDay(2000, 1, 1).toDateTimeAtMidnight().toDate();
	newRow[8] = dateTime.toDate();
	newRow[9] = dateTime.toDate();
	newRow[10] = person != null ? getName(person.getNickname()) : getName(parkingParty.getParty()
		.getName());
	newRow[11] = "";
	String vehicle1PlateNumber = "";
	String vehicle2PlateNumber = "";
	int counter = 1;
	for (Vehicle vehicle : parkingParty.getVehicles()) {
	    if (counter == 1) {
		vehicle1PlateNumber = vehicle.getPlateNumber();
	    } else if (counter == 2) {
		vehicle2PlateNumber = vehicle.getPlateNumber();
	    } else {
		break;
	    }
	    counter++;
	}
	newRow[12] = vehicle1PlateNumber;
	newRow[13] = vehicle2PlateNumber;
	newRow[14] = person != null && person.getWorkPhone() != null ? getString(person.getWorkPhone(),
		19) : "";
	newRow[15] = person != null && person.getMobile() != null ? getString(person.getMobile(), 19)
		: "";
	newRow[16] = "";
	newRow[17] = "";
	newRow[18] = 0;
	newRow[19] = parkingParty.getCardEndDate() == null ? null : parkingParty.getCardEndDate()
		.toDate();
	newRow[20] = new YearMonthDay(2000, 1, 1).toDateTimeAtMidnight().toDate();
	newRow[21] = Boolean.FALSE;
	newRow[22] = parkingParty.getCardStartDate() != null ? Boolean.FALSE : Boolean.TRUE;
	newRow[23] = Boolean.FALSE;
	newRow[24] = Boolean.FALSE;
	newRow[25] = Boolean.FALSE;
	newRow[26] = Boolean.TRUE;
	newRow[27] = parkingParty.getCardStartDate() == null ? null : parkingParty.getCardStartDate()
		.toDate();
    }

    private String convertParkingGroupToAccessDB(ParkingGroup parkingGroup) throws FenixServiceException {
	if (parkingGroup.getGroupName().equalsIgnoreCase("Docentes")) {
	    return "1";
	} else if (parkingGroup.getGroupName().equalsIgnoreCase("Não Docentes")) {
	    return "2";
	} else if (parkingGroup.getGroupName().equalsIgnoreCase("Especiais")) {
	    return "3";
	} else if (parkingGroup.getGroupName().equalsIgnoreCase("Bolseiros")) {
	    return "4";
	} else if (parkingGroup.getGroupName().equalsIgnoreCase("Investigadores")) {
	    return "5";
	} else if (parkingGroup.getGroupName().equalsIgnoreCase("3º ciclo")) {
	    return "6";
	} else if (parkingGroup.getGroupName().equalsIgnoreCase("2º ciclo")) {
	    return "7";
	} else if (parkingGroup.getGroupName().equalsIgnoreCase("IPSFL")) {
	    return "8";
	} else if (parkingGroup.getGroupName().equalsIgnoreCase("Jubilados")) {
	    return "9";
	} else if (parkingGroup.getGroupName().equalsIgnoreCase("Limitados")) {
	    return "10";
	} else if (parkingGroup.getGroupName().equalsIgnoreCase("Limitado1")) {
	    return "11";
	} else if (parkingGroup.getGroupName().equalsIgnoreCase("Limitado2")) {
	    return "12";
	} else if (parkingGroup.getGroupName().equalsIgnoreCase("Limitado3")) {
	    return "13";
	} else if (parkingGroup.getGroupName().equalsIgnoreCase("Limitado4")) {
	    return "14";
	} else if (parkingGroup.getGroupName().equalsIgnoreCase("Limitado5")) {
	    return "15";
	}
	throw new FenixServiceException();
    }
    
    private List<ParkingParty> getValidParkingParties() {
	List<ParkingParty> parkingParties = new ArrayList<ParkingParty>();
	for (ParkingParty parkingParty : ParkingParty.getAll()) {
	    if (parkingParty.getCardNumber() != null) {
		parkingParties.add(parkingParty);
	    }
	}
	return parkingParties;
    }

    private ParkingParty getParkingPartyCardNumber(Long cardNumber, List<ParkingParty> parkingParties) {
	for (ParkingParty parkingParty : parkingParties) {
	    if (parkingParty.getCardNumber().equals(cardNumber)) {
		parkingParties.remove(parkingParty);
		return parkingParty;
	    }
	}
	return null;
    }

    private String getBooleanString(Object object) {
	Boolean booleanNumber = (Boolean) object;
	if (booleanNumber) {
	    return "TRUE";
	} else {
	    return "FALSE";
	}
    }
    
    private String getString(String string, int maxSize) {
	if (string.length() > maxSize) {
	    return string.substring(0, maxSize - 1);
	} else {
	    return string;
	}
    }

}