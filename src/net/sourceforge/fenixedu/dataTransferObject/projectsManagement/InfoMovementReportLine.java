/*
 * Created on Jan 12, 2005
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.projectsManagement;

import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;
import net.sourceforge.fenixedu.domain.projectsManagement.IMovementReportLine;
import net.sourceforge.fenixedu.util.projectsManagement.ExcelStyle;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

/**
 * @author Susana Fernandes
 * 
 */
public class InfoMovementReportLine extends DataTranferObject implements IReportLine {

    private String movementId;

    private Integer rubricId;

    private String type;

    private String date;

    private String description;

    private Double value;

    private Double tax;

    private Double total;

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMovementId() {
        return movementId;
    }

    public void setMovementId(String movementId) {
        this.movementId = movementId;
    }

    public Integer getRubricId() {
        return rubricId;
    }

    public void setRubricId(Integer rubricId) {
        this.rubricId = rubricId;
    }

    public Double getTax() {
        return tax;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void copyFromDomain(IMovementReportLine movementReportLine) {
        if (movementReportLine != null) {
            setMovementId(movementReportLine.getMovementId());
            setRubricId(movementReportLine.getRubricId());
            setType(movementReportLine.getType());
            setDate(movementReportLine.getDate());
            setDescription(movementReportLine.getDescription());
            setValue(movementReportLine.getValue());
            setTax(movementReportLine.getTax());
            setTotal(new Double(getValue().doubleValue() + getTax().doubleValue()));
        }
    }

    public static InfoMovementReportLine newInfoFromDomain(IMovementReportLine movementReportLine) {
        InfoMovementReportLine infoMovementReportLine = null;
        if (movementReportLine != null) {
            infoMovementReportLine = new InfoMovementReportLine();
            infoMovementReportLine.copyFromDomain(movementReportLine);
        }
        return infoMovementReportLine;
    }

    public Double getValue(int column) {
        switch (column) {
        case 5:
            return getValue();
        case 6:
            return getTax();
        case 7:
            return getTotal();
        default:
            return null;
        }
    }

    public void getHeaderToExcel(HSSFSheet sheet, ExcelStyle excelStyle) {
        HSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
        HSSFCell cell = row.createCell((short) 0);
        cell.setCellValue("Id Mov");
        cell.setCellStyle(excelStyle.getHeaderStyle());
        cell = row.createCell((short) 1);
        cell.setCellValue("Rúbrica");
        cell.setCellStyle(excelStyle.getHeaderStyle());
        cell = row.createCell((short) 2);
        cell.setCellValue("Tipo");
        cell.setCellStyle(excelStyle.getHeaderStyle());
        cell = row.createCell((short) 3);
        cell.setCellValue("Data");
        cell.setCellStyle(excelStyle.getHeaderStyle());
        cell = row.createCell((short) 4);
        cell.setCellValue("Descrição");
        cell.setCellStyle(excelStyle.getHeaderStyle());
        cell = row.createCell((short) 5);
        cell.setCellValue("Valor");
        cell.setCellStyle(excelStyle.getHeaderStyle());
        cell = row.createCell((short) 6);
        cell.setCellValue("Iva");
        cell.setCellStyle(excelStyle.getHeaderStyle());
        cell = row.createCell((short) 7);
        cell.setCellValue("Total");
        cell.setCellStyle(excelStyle.getHeaderStyle());
    }

    public void getLineToExcel(HSSFSheet sheet, ExcelStyle excelStyle) {
        HSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
        HSSFCell cell = row.createCell((short) 0);
        cell.setCellValue(getMovementId());
        cell.setCellStyle(excelStyle.getStringStyle());
        cell = row.createCell((short) 1);
        cell.setCellValue(Double.parseDouble(getRubricId().toString()));
        cell.setCellStyle(excelStyle.getIntegerStyle());
        cell = row.createCell((short) 2);
        cell.setCellValue(getType());
        cell.setCellStyle(excelStyle.getStringStyle());
        cell = row.createCell((short) 3);
        cell.setCellValue(getDate());
        cell.setCellStyle(excelStyle.getStringStyle());
        cell = row.createCell((short) 4);
        cell.setCellValue(getDescription());
        cell.setCellStyle(excelStyle.getStringStyle());
        cell = row.createCell((short) 5);
        cell.setCellValue(getValue().doubleValue());
        if (getValue().doubleValue() < 0)
            cell.setCellStyle(excelStyle.getDoubleNegativeStyle());
        else
            cell.setCellStyle(excelStyle.getDoubleStyle());
        cell = row.createCell((short) 6);
        cell.setCellValue(getTax().doubleValue());
        if (getTax().doubleValue() < 0)
            cell.setCellStyle(excelStyle.getDoubleNegativeStyle());
        else
            cell.setCellStyle(excelStyle.getDoubleStyle());
        cell = row.createCell((short) 7);
        double total = getValue().doubleValue() + getTax().doubleValue();
        cell.setCellValue(total);
        if (total < 0)
            cell.setCellStyle(excelStyle.getDoubleNegativeStyle());
        else
            cell.setCellStyle(excelStyle.getDoubleStyle());
    }

    public void getTotalLineToExcel(HSSFSheet sheet, ExcelStyle excelStyle) {
    }

    public int getNumberOfColumns() {
        return 7;
    }

}
