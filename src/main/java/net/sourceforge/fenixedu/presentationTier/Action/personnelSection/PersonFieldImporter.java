package net.sourceforge.fenixedu.presentationTier.Action.personnelSection;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.person.GenderHelper;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.google.common.collect.Maps;

public abstract class PersonFieldImporter {
    final String columnName;
    final boolean isMandatory;
    private Map<String, Integer> columnsNumbers;
    final static Locale PT = new Locale("pt");

    public PersonFieldImporter(String columnName, boolean isMandatory) {
        this.columnName = columnName;
        this.isMandatory = isMandatory;
    }

    public String getColumnName() {
        return columnName;
    }

    public abstract void apply(Row row, PersonBean personBean);

    public boolean isMandatory() {
        return isMandatory;
    }

    public Gender parseGender(String genderValue) {
        for (Gender gender : Gender.values()) {
            if (StringUtils.equalsIgnoreCase(GenderHelper.toLocalizedString(gender, PT), genderValue)) {
                return gender;
            }
        }
        return null;
    }

    public Date getCellDate(Row row, String columnName) {
        Cell cell = getCell(row);
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

    public boolean hasColumn(Row row) {
        return getColumnNumber(row.getSheet(), getColumnName()) != null;
    }

    public Cell getCell(Row row) {
        return row.getCell(getColumnNumber(row.getSheet(), columnName));
    }

    public Integer getColumnNumber(Sheet sheet, String columnName) {
        return getColumnsNumbers(sheet).get(columnName);
    }

    public Map<String, Integer> getColumnsNumbers(Sheet sheet) {
        if (columnsNumbers == null) {
            columnsNumbers = Maps.newHashMap();
            Row firstRow = sheet.getRow(0);
            for (int columnNumber = firstRow.getFirstCellNum(); columnNumber < firstRow.getLastCellNum(); ++columnNumber) {
                columnsNumbers.put(firstRow.getCell(columnNumber).getStringCellValue(), columnNumber);
            }
        }
        return columnsNumbers;
    }

    public void secureApply(Row row, PersonBean personBean) throws Exception {
        if (isMandatory && !hasColumn(row)) {
            throw new Exception("Column named " + getColumnName() + "is mandatory and is missing");
        }
        try {
            apply(row, personBean);
        } catch (Exception e) {
            //if the value is not mandatory we don't care
            if (isMandatory()) {
                e.printStackTrace();
                throw e;
            }
        }

    }
}