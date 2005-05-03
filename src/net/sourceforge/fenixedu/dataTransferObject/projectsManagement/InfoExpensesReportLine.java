/*
 * Created on Jan 12, 2005
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.projectsManagement;

import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;
import net.sourceforge.fenixedu.domain.projectsManagement.IExpensesReportLine;
import net.sourceforge.fenixedu.util.projectsManagement.ExcelStyle;
import net.sourceforge.fenixedu.util.projectsManagement.ReportType;

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

    private String supplier;

    private String supplierDescription;

    private String documentType;

    private String documentNumber;

    private String financingSource;

    private Integer rubric;

    private String movementType;

    private Double ivaPercentage;

    private String date;

    private String description;

    private Double value;

    private Double tax;

    private Double total;

    private Double imputedPercentage;

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

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getFinancingSource() {
        return financingSource;
    }

    public void setFinancingSource(String financingSource) {
        this.financingSource = financingSource;
    }

    public Double getImputedPercentage() {
        return imputedPercentage;
    }

    public void setImputedPercentage(Double imputedPercentage) {
        this.imputedPercentage = imputedPercentage;
    }

    public Double getIvaPercentage() {
        return ivaPercentage;
    }

    public void setIvaPercentage(Double ivaPercentage) {
        this.ivaPercentage = ivaPercentage;
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

    public String getMovementType() {
        return movementType;
    }

    public void setMovementType(String movementType) {
        this.movementType = movementType;
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

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getSupplierDescription() {
        return supplierDescription;
    }

    public void setSupplierDescription(String supplierDescription) {
        this.supplierDescription = supplierDescription;
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
            setSupplier(expensesReportLine.getSupplier());
            setSupplierDescription(expensesReportLine.getSupplierDescription());
            setDocumentType(expensesReportLine.getDocumentType());
            setDocumentNumber(expensesReportLine.getDocumentNumber());
            setFinancingSource(expensesReportLine.getFinancingSource());
            setRubric(expensesReportLine.getRubric());
            setMovementType(expensesReportLine.getMovementType());
            setIvaPercentage(expensesReportLine.getIvaPercentage());
            setDate(expensesReportLine.getDate());
            setDescription(expensesReportLine.getDescription());
            setValue(expensesReportLine.getValue());
            setTax(expensesReportLine.getTax());
            setTotal(expensesReportLine.getTotal());
            setImputedPercentage(expensesReportLine.getImputedPercentage());
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

    public void getHeaderToExcel(HSSFSheet sheet, ExcelStyle excelStyle, ReportType reportType) {
        HSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
        int column = 0;
        HSSFCell cell = row.createCell((short) column++);
        cell.setCellValue("Id Mov");
        cell.setCellStyle(excelStyle.getHeaderStyle());
        cell = row.createCell((short) column++);
        cell.setCellValue("Membro");
        cell.setCellStyle(excelStyle.getHeaderStyle());
        cell = row.createCell((short) column++);

        if (reportType.equals(ReportType.COMPLETE_EXPENSES)) {
            cell.setCellValue("Fornecedor");
            cell.setCellStyle(excelStyle.getHeaderStyle());
            cell = row.createCell((short) column++);
            cell.setCellValue("Tipo Doc");
            cell.setCellStyle(excelStyle.getHeaderStyle());
            cell = row.createCell((short) column++);
            cell.setCellValue("Num Doc");
            cell.setCellStyle(excelStyle.getHeaderStyle());
            cell = row.createCell((short) column++);
            cell.setCellValue("Fonte financ");
            cell.setCellStyle(excelStyle.getHeaderStyle());
            cell = row.createCell((short) column++);

        }
        cell.setCellValue("Rúbrica");
        cell.setCellStyle(excelStyle.getHeaderStyle());
        cell = row.createCell((short) column++);
        cell.setCellValue("Tipo");
        cell.setCellStyle(excelStyle.getHeaderStyle());
        cell = row.createCell((short) column++);
        cell.setCellValue("Data");
        cell.setCellStyle(excelStyle.getHeaderStyle());
        cell = row.createCell((short) column++);
        cell.setCellValue("Descrição");
        cell.setCellStyle(excelStyle.getHeaderStyle());
        cell = row.createCell((short) column++);
        if (reportType.equals(ReportType.COMPLETE_EXPENSES)) {
            cell.setCellValue("% IVA");
            cell.setCellStyle(excelStyle.getHeaderStyle());
            cell = row.createCell((short) column++);
        }

        cell.setCellValue("Valor");
        cell.setCellStyle(excelStyle.getHeaderStyle());
        cell = row.createCell((short) column++);
        cell.setCellValue("Iva");
        cell.setCellStyle(excelStyle.getHeaderStyle());
        cell = row.createCell((short) column++);
        cell.setCellValue("Total");
        cell.setCellStyle(excelStyle.getHeaderStyle());
        if (reportType.equals(ReportType.COMPLETE_EXPENSES)) {
            cell.setCellValue("% Imput");
            cell.setCellStyle(excelStyle.getHeaderStyle());
            cell = row.createCell((short) column++);
        }
    }

    public void getLineToExcel(HSSFSheet sheet, ExcelStyle excelStyle, ReportType reportType) {
        HSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
        int column = 0;
        HSSFCell cell = row.createCell((short) column++);
        cell.setCellValue(getMovementId());
        cell.setCellStyle(excelStyle.getStringStyle());
        cell = row.createCell((short) column++);
        cell.setCellValue(getMember());
        cell.setCellStyle(excelStyle.getStringStyle());
        cell = row.createCell((short) column++);
        if (reportType.equals(ReportType.COMPLETE_EXPENSES)) {
            cell.setCellValue(getSupplierDescription());
            cell.setCellStyle(excelStyle.getStringStyle());
            cell = row.createCell((short) column++);

            cell.setCellValue(getDocumentType());
            cell.setCellStyle(excelStyle.getStringStyle());
            cell = row.createCell((short) column++);
            cell.setCellValue(getDocumentNumber());
            cell.setCellStyle(excelStyle.getStringStyle());
            cell = row.createCell((short) column++);
            cell.setCellValue(getFinancingSource());
            cell.setCellStyle(excelStyle.getStringStyle());
            cell = row.createCell((short) column++);
        }
        cell.setCellValue(Double.parseDouble(getRubric().toString()));
        cell.setCellStyle(excelStyle.getIntegerStyle());
        cell = row.createCell((short) column++);
        cell.setCellValue(getMovementType());
        cell.setCellStyle(excelStyle.getStringStyle());
        cell = row.createCell((short) column++);
        cell.setCellValue(getDate());
        cell.setCellStyle(excelStyle.getStringStyle());
        cell = row.createCell((short) column++);
        cell.setCellValue(getDescription());
        cell.setCellStyle(excelStyle.getStringStyle());
        cell = row.createCell((short) column++);
        if (reportType.equals(ReportType.COMPLETE_EXPENSES)) {
            cell.setCellValue(getIvaPercentage().doubleValue());
            if (getValue().doubleValue() < 0)
                cell.setCellStyle(excelStyle.getDoubleNegativeStyle());
            else
                cell.setCellStyle(excelStyle.getDoubleStyle());
            cell = row.createCell((short) column++);
        }
        cell.setCellValue(getValue().doubleValue());
        if (getValue().doubleValue() < 0)
            cell.setCellStyle(excelStyle.getDoubleNegativeStyle());
        else
            cell.setCellStyle(excelStyle.getDoubleStyle());
        cell = row.createCell((short) column++);
        cell.setCellValue(getTax().doubleValue());
        if (getTax().doubleValue() < 0)
            cell.setCellStyle(excelStyle.getDoubleNegativeStyle());
        else
            cell.setCellStyle(excelStyle.getDoubleStyle());
        cell = row.createCell((short) column++);
        cell.setCellValue(getTotal().doubleValue());
        if (getTotal().doubleValue() < 0)
            cell.setCellStyle(excelStyle.getDoubleNegativeStyle());
        else
            cell.setCellStyle(excelStyle.getDoubleStyle());
        if (reportType.equals(ReportType.COMPLETE_EXPENSES)) {
            cell.setCellValue(getImputedPercentage().doubleValue());
            if (getValue().doubleValue() < 0)
                cell.setCellStyle(excelStyle.getDoubleNegativeStyle());
            else
                cell.setCellStyle(excelStyle.getDoubleStyle());
            cell = row.createCell((short) column++);
        }
    }

    public void getTotalLineToExcel(HSSFSheet sheet, ExcelStyle excelStyle, ReportType reportType) {
        HSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
        HSSFCell cell = row.createCell((short) 0);
        cell.setCellStyle(excelStyle.getStringStyle());
        cell.setCellValue("TOTAL");
        int first = 6, last = 8;
        if (reportType.equals(ReportType.COMPLETE_EXPENSES)) {
            first = 11;
            last = 13;
        }
        for (int i = first; i <= last; i++) {
            CellReference cellRef1 = new CellReference(1, i);
            CellReference cellRef2 = new CellReference(((short) row.getRowNum() - 1), i);
            cell = row.createCell((short) i);
            cell.setCellStyle(excelStyle.getDoubleStyle());
            cell.setCellFormula("sum(" + cellRef1.toString() + ":" + cellRef2.toString() + ")");
        }
    }

    public Double getValue(int column) {
        switch (column) {
        case 6:
        case 11:
            return getValue();
        case 7:
        case 12:
            return getTax();
        case 8:
        case 13:
            return getTotal();
        default:
            return null;
        }
    }
}
