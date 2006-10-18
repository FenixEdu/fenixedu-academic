package net.sourceforge.fenixedu.presentationTier.Action.parkingManager;

import java.util.Calendar;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.parking.ParkingGroup;
import net.sourceforge.fenixedu.domain.parking.ParkingParty;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.util.projectsManagement.ExcelStyle;
import net.sourceforge.fenixedu.util.report.Spreadsheet;
import net.sourceforge.fenixedu.util.report.Spreadsheet.Row;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;

public class ExportParkingDataToAccessDatabaseAction extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            response.setContentType("text/plain");
            response.setHeader("Content-disposition", "attachment; filename=parkingDB.xls");
            //response.setContentType("application/vnd.ms-excel");
            
            final HSSFWorkbook workbook = new HSSFWorkbook();
            final ExcelStyle excelStyle = new ExcelStyle(workbook);
            final ServletOutputStream writer = response.getOutputStream();

            final Spreadsheet parkingBDSpreadsheet = new Spreadsheet("BD Fénix Parque");
            setHeaders(parkingBDSpreadsheet);
            for (ParkingParty parkingParty : ParkingParty.getAll()) {
                if (parkingParty.getCardNumber() != null) {
                    fillInRow(parkingBDSpreadsheet, parkingParty);
                }
            }
            parkingBDSpreadsheet.exportToXLSSheet(workbook, excelStyle.getHeaderStyle(), excelStyle
                    .getStringStyle());
            workbook.write(writer);
            writer.flush();
            response.flushBuffer();

        } catch (Exception e) {
            throw new FenixServiceException();
        }
        return null;
    }

    private void fillInRow(final Spreadsheet parkingBDSpreadsheet, ParkingParty parkingParty)
            throws FenixServiceException {
        long thisInstant = Calendar.getInstance().getTimeInMillis();
        DateTime dateTime = new DateTime(thisInstant);
        final Row row = parkingBDSpreadsheet.addRow();
        Person person = null;
        if (parkingParty.getParty().isPerson()) {
            person = (Person) parkingParty.getParty();
        }
        row.setCell("0"); // garage
        row.setCell(parkingParty.getCardNumber().toString()); // cardNumber
        row.setCell("3"); // type Mirafe confirm this in the real aplication
        row.setCell("TRUE"); // access (if the card is active or not)
        row.setCell(convertParkingGroupToAccessDB(parkingParty.getParkingGroup()));
        row.setCell("1"); // fee
        row.setCell("0"); // SAC
        row.setCell("1/1/2000"); // alterDate
        row.setCell(dateTime.toString("dd/MM/yyyy hh:mm:ss")); // createdDate
        row.setCell(dateTime.toString("dd/MM/yyyy hh:mm:ss")); // editedDate
        row.setCell(parkingParty.getParty().getName()); // name
        row.setCell(person != null && person.getAddress() != null ? person.getAddress() : ""); // address         
        row.setCell(parkingParty.getFirstCarPlateNumber() != null ? parkingParty.getFirstCarPlateNumber() : ""); // license
        row.setCell(parkingParty.getSecondCarPlateNumber() != null ? parkingParty.getSecondCarPlateNumber() : ""); // licenseAlt
        row.setCell(person != null && person.getWorkPhone() != null ? person.getWorkPhone() : ""); // registration
        row.setCell(person != null && person.getMobile() != null ? person.getMobile() : ""); // registrationAlt
        row.setCell(""); // clientRef
        row.setCell(""); // comment
        row.setCell("0"); // price
        row.setCell("3/10/2007"); // endValidityDate*
        row.setCell("1/1/2000"); // lastUsedDate
        row.setCell("FALSE"); // invoice
        row.setCell("TRUE"); // unlimited (if true, start and end validity dates are ignored
        row.setCell("FALSE"); // present
        row.setCell("FALSE"); // payDirect
        row.setCell("FALSE"); // apbCorrect (if it's already in the park)
        row.setCell("TRUE"); // noFee
        row.setCell("3/10/2006"); // startValidityDate*
        
        //* what values to put here ? maybe we should have them in our side too
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
        spreadsheet.setHeader("NoFee");
        spreadsheet.setHeader("StartValidityDate");
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
        }

        throw new FenixServiceException();
    }
}