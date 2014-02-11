package net.sourceforge.fenixedu.presentationTier.Action.personnelSection;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

public abstract class PersonFieldImporter {
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    final String columnName;
    final boolean isMandatory;
    private Map<String, Integer> columnsNumbers;

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

    public Date getCellDate(Row row, String columnName) {
        Cell cell = getCell(row);
        if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            return cell.getDateCellValue();
        } else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
            try {
                return new SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).parse(cell.getStringCellValue());
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
        Preconditions.checkState(!isMandatory || isMandatory && hasColumn(row), "Column named " + getColumnName()
                + "is mandatory and is missing");
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