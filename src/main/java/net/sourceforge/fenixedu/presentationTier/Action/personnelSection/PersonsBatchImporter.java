package net.sourceforge.fenixedu.presentationTier.Action.personnelSection;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.person.GenderHelper;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.person.MaritalStatus;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;

public class PersonsBatchImporter {

    private Workbook workbook;
    private Set<Person> persons;
    private InputStream inputStream;
    final static Locale PT = new Locale("pt");

    public PersonsBatchImporter(InputStream inputStream) {
        this.inputStream = inputStream;
        try {
            workbook = WorkbookFactory.create(inputStream);
        } catch (InvalidFormatException | IOException e) {
            e.printStackTrace();
        }
    }

    public PersonBean createPersonBean(Row row) {

        String name = getCell(row, "nome").getStringCellValue();
        Preconditions.checkNotNull(name, "nome is required and is empty for row " + row.getRowNum());

        int startFamilyName = name.lastIndexOf(" ");
        Preconditions.checkArgument(startFamilyName > 0, "full name is required");

        String givenNames = name.substring(0, startFamilyName);
        String familyNames = name.substring(startFamilyName + 1);

        String identificationNumber = getCell(row, "docum_num").getStringCellValue();
        Preconditions.checkNotNull(identificationNumber, "docum_num is required and is empty for row " + row.getRowNum());

        Date dateOfBirth = getCellDate(row, "data_nascimento");
        Preconditions.checkNotNull(dateOfBirth, "data_nascimento is required and is empty for row " + row.getRowNum());

        String idDocumentTypeString = getCell(row, "docum").getStringCellValue();
        IDDocumentType idDocumentType = IDDocumentType.parse(idDocumentTypeString, PT);
        Preconditions.checkNotNull(idDocumentType, "docum is required and is empty for row " + row.getRowNum());

        String genderString = getCell(row, "sexo").getStringCellValue();
        Gender gender = parseGender(genderString);
        Preconditions.checkNotNull(gender, "gender is required and is empty for row " + row.getRowNum());

        String maritialStatusString = getCell(row, "estado_civil").getStringCellValue();
        MaritalStatus maritialStatus = MaritalStatus.parse(maritialStatusString, PT);
        Preconditions.checkNotNull(gender, "estado_civil is required and is empty for row " + row.getRowNum());

        PersonBean personBean =
                new PersonBean(name, identificationNumber, idDocumentType, new YearMonthDay(dateOfBirth.getTime()));
        personBean.setGivenNames(givenNames);
        personBean.setFamilyNames(familyNames);
        personBean.setGender(gender);
        personBean.setMaritalStatus(maritialStatus);

        //TODO - all the remaining fiels

        return personBean;
    }

    private Gender parseGender(String genderValue) {
        for (Gender gender : Gender.values()) {
            if (StringUtils.equalsIgnoreCase(GenderHelper.toLocalizedString(gender, PT), genderValue)) {
                return gender;
            }
        }
        return null;
    }

    public Set<PersonBean> createPersonBeans(Sheet sheet) {
        Set<PersonBean> personsBeans = Sets.newHashSet();
        Iterator<Row> iterator = sheet.rowIterator();
        iterator.next();
        while (iterator.hasNext()) {
            Row row = iterator.next();
            if (!isEmptyRow(row)) {
                personsBeans.add(createPersonBean(row));
            }
        }
        return personsBeans;
    }

    @Atomic(mode = TxMode.WRITE)
    public void createPersons() {
        Set<Person> persons = Sets.newHashSet();
        for (PersonBean personBean : createPersonBeans(getSheet())) {
            persons.add(new Person(personBean));
        }
        setPersons(persons);
    }

    public static boolean hasColumnNamed(Sheet sheet, String columnName) {
        return getColumnNumber(sheet, columnName) != null;
    }

    public static Integer getColumnNumber(Sheet sheet, String columnName) {
        Row firstRow = sheet.getRow(0);
        Iterator<Cell> iterator = firstRow.cellIterator();
        while (iterator.hasNext()) {
            Cell cell = iterator.next();
            if (StringUtils.equals(cell.getStringCellValue(), columnName)) {
                return cell.getColumnIndex();
            }
        }
        return null;
    }

    public static Date getCellDate(Row row, String columnName) {
        Cell cell = getCell(row, "data_nascimento");
        if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            return cell.getDateCellValue();
        } else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
            try {
                return new SimpleDateFormat("dd/M/yyyy", Locale.getDefault()).parse(cell.getStringCellValue());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private boolean isEmptyRow(Row row) {
        for (int c = row.getFirstCellNum(); c <= row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);
            if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK) {
                return false;
            }
        }
        return true;
    }

    public Workbook getWorkbook() {
        return workbook;
    }

    public static Cell getCell(Row row, String columnName) {
        return row.getCell(getColumnNumber(row.getSheet(), columnName));
    }

    public Sheet getSheet() {
        return getWorkbook().getSheetAt(0);
    }

    public Set<Person> getPersons() {
        return persons;
    }

    public void setPersons(Set<Person> persons) {
        this.persons = persons;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
}
