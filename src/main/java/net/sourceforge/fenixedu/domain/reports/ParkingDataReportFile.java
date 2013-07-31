package net.sourceforge.fenixedu.domain.reports;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.ist.bennu.core.util.ConfigurationManager;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.QueueJobResult;
import net.sourceforge.fenixedu.domain.parking.ParkingParty;
import net.sourceforge.fenixedu.domain.parking.Vehicle;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.util.FileUtils;

import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.Table;

public class ParkingDataReportFile extends ParkingDataReportFile_Base {

    private static final String FILE_NAME = "Cartoes_XML.mdb";

    private static final int _3 = 3;
    private static final int _1 = 1;
    private static final int _0 = 0;
    private static final Date DATE = new LocalDate(2000, 1, 1).toDateTimeAtStartOfDay().toDate();

    public ParkingDataReportFile() {
        super();
    }

    @Override
    public String getFilename() {
        return FILE_NAME;
    }

    @Override
    public QueueJobResult execute() throws Exception {
        QueueJobResult queueJobResult = null;

        final String inputFilename = ConfigurationManager.getProperty("export.parking.data.report.input.file");
        if (inputFilename != null) {
            File parkingDataFile = FileUtils.copyToTemporaryFile(new FileInputStream(inputFilename));
            renderReport(parkingDataFile);

            queueJobResult = new QueueJobResult();
            queueJobResult.setContentType("application/vnd.ms-access");
            queueJobResult.setContent(pt.utl.ist.fenix.tools.file.utils.FileUtils.readByteArray(parkingDataFile));

            System.out.println("Job " + getFilename() + " completed");
        }
        return queueJobResult;
    }

    public void renderReport(File parkingDataFile) throws Exception {
        Database db = Database.open(parkingDataFile, Boolean.FALSE, Boolean.TRUE);
        Table xml = db.getTable("XML");
        List<ParkingParty> parkingParties = getValidParkingParties();
        final Date now = new DateTime().toDate();
        final Map<String, Integer> parkingGroupCodes = getParkingGroupCodes();
        for (ParkingParty parkingParty : parkingParties) {
            if (parkingParty.getCardNumber() != 0) {
                Object[] newRow = new Object[28];
                fillInRow(parkingParty, newRow, parkingGroupCodes, now);
                xml.addRow(newRow);
            }
        }
    }

    private Map<String, Integer> getParkingGroupCodes() {
        Map<String, Integer> codes = new HashMap<String, Integer>();
        codes.put("Docentes", 1);
        codes.put("Não Docentes", 2);
        codes.put("Especiais", 3);
        codes.put("Bolseiros", 4);
        codes.put("Investigadores", 5);
        codes.put("3º ciclo", 6);
        codes.put("2º ciclo", 7);
        codes.put("IPSFL", 8);
        codes.put("Jubilados", 9);
        codes.put("Limitados", 10);
        codes.put("Limitado1", 11);
        codes.put("Limitado2", 12);
        codes.put("Limitado3", 13);
        codes.put("Limitado4", 14);
        codes.put("Limitado5", 15);
        return codes;
    }

    private void fillInRow(ParkingParty parkingParty, Object[] newRow, Map<String, Integer> parkingGroupCodes, Date now)
            throws Exception {

        Person person = null;
        if (parkingParty.getParty().isPerson()) {
            person = (Person) parkingParty.getParty();
        }

        newRow[0] = _0;
        newRow[1] = parkingParty.getCardNumber().toString();
        newRow[2] = _3;
        newRow[3] = Boolean.TRUE;
        newRow[4] = parkingGroupCodes.get(parkingParty.getParkingGroup().getGroupName()).intValue();
        newRow[5] = _1;
        newRow[6] = _0;
        newRow[7] = DATE;
        newRow[8] = now;
        newRow[9] = now;
        newRow[10] = person != null ? getName(person.getNickname()) : getName(parkingParty.getParty().getName());
        newRow[11] = StringUtils.EMPTY;
        String vehicle1PlateNumber = StringUtils.EMPTY;
        String vehicle2PlateNumber = StringUtils.EMPTY;
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
        newRow[14] =
                person != null && person.getPersonWorkPhone() != null ? getString(person.getPersonWorkPhone().getNumber(), 19) : StringUtils.EMPTY;
        newRow[15] =
                person != null && person.getDefaultMobilePhone() != null ? getString(person.getDefaultMobilePhone().getNumber(),
                        19) : StringUtils.EMPTY;
        newRow[16] = StringUtils.EMPTY;
        newRow[17] = StringUtils.EMPTY;
        newRow[18] = _0;
        newRow[19] = parkingParty.getCardEndDate() == null ? null : parkingParty.getCardEndDate().toDate();
        newRow[20] = DATE;
        newRow[21] = Boolean.FALSE;
        newRow[22] = parkingParty.getCardStartDate() != null ? Boolean.FALSE : Boolean.TRUE;
        newRow[23] = Boolean.FALSE;
        newRow[24] = Boolean.FALSE;
        newRow[25] = Boolean.FALSE;
        newRow[26] = Boolean.TRUE;
        newRow[27] = parkingParty.getCardStartDate() == null ? null : parkingParty.getCardStartDate().toDate();
    }

    private String getName(String name) {
        if (name.length() > 59) { // max size of the other parking application
            // DB
            StringBuilder resultName = new StringBuilder();
            resultName = new StringBuilder();
            String[] names = name.split("\\p{Space}+");
            for (int iter = 1; iter < names.length - 1; iter++) {
                if (names[iter].length() > 5) {
                    names[iter] = names[iter].substring(0, 5) + ".";
                }
            }
            for (String name2 : names) {
                resultName.append(name2).append(" ");
            }
            if (resultName.length() > 59) {
                resultName = new StringBuilder(names[0]).append(" ").append(names[names.length - 1]);
            }
            return resultName.toString().trim();
        } else {
            return name;
        }

    }

    private List<ParkingParty> getValidParkingParties() {
        List<ParkingParty> parkingParties = new ArrayList<ParkingParty>();
        for (ParkingParty parkingParty : ParkingParty.getAll()) {
            if (parkingParty.getParkingGroup() != null && parkingParty.getCardNumber() != null
                    && parkingParty.getCardNumber() != 0) {
                parkingParties.add(parkingParty);
            }
        }
        return parkingParties;
    }

    private String getString(String string, int maxSize) {
        if (string == null) {
            return StringUtils.EMPTY;
        }
        if (string.length() > maxSize) {
            return string.substring(0, maxSize - 1);
        } else {
            return string;
        }
    }

    @Override
    public Priority getPriority() {
        return Priority.HIGH;
    }

}
