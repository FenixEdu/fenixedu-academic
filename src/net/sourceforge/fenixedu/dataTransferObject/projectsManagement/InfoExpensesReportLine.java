/*
 * Created on Jan 12, 2005
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.projectsManagement;

import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;
import net.sourceforge.fenixedu.domain.projectsManagement.IExpensesReportLine;
import net.sourceforge.fenixedu.util.projectsManagement.ExcelStyle;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.util.CellReference;

/**
 * @author Susana Fernandes
 * 
 */
public class InfoExpensesReportLine extends DataTranferObject implements IReportLine {
    private Integer projectCode;

    private String movementId;

    private String member;

    private Integer rubric;

    private String type;

    private String date;

    private String description;

    private Double value;

    private Double tax;

    private Double total;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public String getMovementId() {
        return movementId;
    }

    public void setMovementId(String movementId) {
        this.movementId = movementId;
    }

    public Integer getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(Integer projectCode) {
        this.projectCode = projectCode;
    }

    public Integer getRubric() {
        return rubric;
    }

    public void setRubric(Integer rubric) {
        this.rubric = rubric;
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

    public void copyFromDomain(IExpensesReportLine expensesReportLine) {
        if (expensesReportLine != null) {
            setProjectCode(expensesReportLine.getProjectCode());
            setMovementId(expensesReportLine.getMovementId());
            setMember(expensesReportLine.getMember());
            setRubric(expensesReportLine.getRubric());
            setType(expensesReportLine.getType());
            setDate(expensesReportLine.getDate());
            setDescription(expensesReportLine.getDescription());
            setValue(expensesReportLine.getValue());
            setTax(expensesReportLine.getTax());
            setTotal(expensesReportLine.getTotal());
        }
    }

    public static InfoExpensesReportLine newInfoFromDomain(IExpensesReportLine expensesReportLine) {
        InfoExpensesReportLine infoExpensesReportLine = null;
        if (expensesReportLine != null) {
            infoExpensesReportLine = new InfoExpensesReportLine();
            infoExpensesReportLine.copyFromDomain(expensesReportLine);
        }
        return infoExpensesReportLine;
    }

    public int getNumberOfColumns() {
        return 8;
    }

    public void getHeaderToExcel(HSSFSheet sheet) {
        HSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
        HSSFCell cell = row.createCell((short) 0);
        cell.setCellValue("Id Mov");
        cell.setCellStyle(ExcelStyle.HEADER_STYLE);
        cell = row.createCell((short) 1);
        cell.setCellValue("Membro");
        cell.setCellStyle(ExcelStyle.HEADER_STYLE);
        cell = row.createCell((short) 2);
        cell.setCellValue("Rúbrica");
        cell.setCellStyle(ExcelStyle.HEADER_STYLE);
        cell = row.createCell((short) 3);
        cell.setCellValue("Tipo");
        cell.setCellStyle(ExcelStyle.HEADER_STYLE);
        cell = row.createCell((short) 4);
        cell.setCellValue("Data");
        cell.setCellStyle(ExcelStyle.HEADER_STYLE);
        cell = row.createCell((short) 5);
        cell.setCellValue("Descrição");
        cell.setCellStyle(ExcelStyle.HEADER_STYLE);
        cell = row.createCell((short) 6);
        cell.setCellValue("Valor");
        cell.setCellStyle(ExcelStyle.HEADER_STYLE);
        cell = row.createCell((short) 7);
        cell.setCellValue("Iva");
        cell.setCellStyle(ExcelStyle.HEADER_STYLE);
        cell = row.createCell((short) 8);
        cell.setCellValue("Total");
        cell.setCellStyle(ExcelStyle.HEADER_STYLE);
    }

    public void getLineToExcel(HSSFSheet sheet) {
        HSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
        HSSFCell cell = row.createCell((short) 0);
        cell.setCellValue(getMovementId());
        cell.setCellStyle(ExcelStyle.STRING_STYLE);
        cell = row.createCell((short) 1);
        cell.setCellValue(getMember());
        cell.setCellStyle(ExcelStyle.STRING_STYLE);
        cell = row.createCell((short) 2);
        cell.setCellValue(Double.parseDouble(getRubric().toString()));
        cell.setCellStyle(ExcelStyle.INTEGER_STYLE);
        cell = row.createCell((short) 3);
        cell.setCellValue(getType());
        cell.setCellStyle(ExcelStyle.STRING_STYLE);
        cell = row.createCell((short) 4);
        cell.setCellValue(getDate());
        cell.setCellStyle(ExcelStyle.STRING_STYLE);
        cell = row.createCell((short) 5);
        cell.setCellValue(getDescription());
        cell.setCellStyle(ExcelStyle.STRING_STYLE);
        cell = row.createCell((short) 6);
        cell.setCellValue(getValue().doubleValue());
        if (getValue().doubleValue() < 0)
            cell.setCellStyle(ExcelStyle.DOUBLE_NEGATIVE_STYLE);
        else
            cell.setCellStyle(ExcelStyle.DOUBLE_STYLE);
        cell = row.createCell((short) 7);
        cell.setCellValue(getTax().doubleValue());
        if (getTax().doubleValue() < 0)
            cell.setCellStyle(ExcelStyle.DOUBLE_NEGATIVE_STYLE);
        else
            cell.setCellStyle(ExcelStyle.DOUBLE_STYLE);
        cell = row.createCell((short) 8);
        cell.setCellValue(getTotal().doubleValue());
        if (getTotal().doubleValue() < 0)
            cell.setCellStyle(ExcelStyle.DOUBLE_NEGATIVE_STYLE);
        else
            cell.setCellStyle(ExcelStyle.DOUBLE_STYLE);
    }

    public void getTotalLineToExcel(HSSFSheet sheet) {
        HSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
        HSSFCell cell = row.createCell((short) 0);
        cell.setCellStyle(ExcelStyle.STRING_STYLE);
        cell.setCellValue("TOTAL");
        for (int i = 6; i <= 8; i++) {
            CellReference cellRef1 = new CellReference(1, i);
            CellReference cellRef2 = new CellReference(((short) row.getRowNum() - 1), i);
            cell = row.createCell((short) i);
            cell.setCellStyle(ExcelStyle.DOUBLE_STYLE);
            cell.setCellFormula("sum(" + cellRef1.toString() + ":" + cellRef2.toString() + ")");
        }
    }
  
    public Double getValue(int column) {
        switch (column) {
        case 6:
            return getValue();
        case 7:
            return getTax();
        case 8:
            return getTotal();
        default:
            return null;
        }
    }
}
