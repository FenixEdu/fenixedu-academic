/*
 * Created on Jan 12, 2005
 *
 */
package DataBeans.projectsManagement;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.util.CellReference;

import DataBeans.DataTranferObject;
import Dominio.projectsManagement.IRevenueReportLine;
import Util.projectsManagement.ExcelStyle;

/**
 * @author Susana Fernandes
 * 
 */
public class InfoRevenueReportLine extends DataTranferObject implements IReportLine {
    private Integer projectCode;

    private String movementId;

    private String financialEntity;

    private Integer rubric;

    private String date;

    private String description;

    private Double value;

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

    public String getFinancialEntity() {
        return financialEntity;
    }

    public void setFinancialEntity(String financialEntity) {
        this.financialEntity = financialEntity;
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

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public void copyFromDomain(IRevenueReportLine revenueReportLine) {
        if (revenueReportLine != null) {
            setProjectCode(revenueReportLine.getProjectCode());
            setMovementId(revenueReportLine.getMovementId());
            setFinancialEntity(revenueReportLine.getFinancialEntity());
            setRubric(revenueReportLine.getRubric());
            setDate(revenueReportLine.getDate());
            setDescription(revenueReportLine.getDescription());
            setValue(revenueReportLine.getValue());
        }
    }

    public static InfoRevenueReportLine newInfoFromDomain(IRevenueReportLine revenueReportLine) {
        InfoRevenueReportLine infoRevenueReportLine = null;
        if (revenueReportLine != null) {
            infoRevenueReportLine = new InfoRevenueReportLine();
            infoRevenueReportLine.copyFromDomain(revenueReportLine);
        }
        return infoRevenueReportLine;
    }

    public int getNumberOfColumns() {
        return 5;
    }

    public HSSFRow getHeaderToExcel(HSSFRow row) {
        HSSFCell cell = row.createCell((short) 0);
        cell.setCellValue("Id Mov");
        cell.setCellStyle(ExcelStyle.HEADER_STYLE);
        cell = row.createCell((short) 1);
        cell.setCellValue("Ent. Financ.");
        cell.setCellStyle(ExcelStyle.HEADER_STYLE);
        cell = row.createCell((short) 2);
        cell.setCellValue("Rúbrica");
        cell.setCellStyle(ExcelStyle.HEADER_STYLE);
        cell = row.createCell((short) 3);
        cell.setCellValue("Data");
        cell.setCellStyle(ExcelStyle.HEADER_STYLE);
        cell = row.createCell((short) 4);
        cell.setCellValue("Descrição");
        cell.setCellStyle(ExcelStyle.HEADER_STYLE);
        cell = row.createCell((short) 5);
        cell.setCellValue("Valor");
        cell.setCellStyle(ExcelStyle.HEADER_STYLE);
        return row;
    }

    public HSSFRow getLineToExcel(HSSFRow row) {
        HSSFCell cell = row.createCell((short) 0);
        cell.setCellValue(getMovementId());
        cell.setCellStyle(ExcelStyle.STRING_STYLE);
        cell = row.createCell((short) 1);
        cell.setCellValue(getFinancialEntity());
        cell.setCellStyle(ExcelStyle.STRING_STYLE);
        cell = row.createCell((short) 2);
        cell.setCellValue(Double.parseDouble(getRubric().toString()));
        cell.setCellStyle(ExcelStyle.INTEGER_STYLE);
        cell = row.createCell((short) 3);
        cell.setCellValue(getDate());
        cell.setCellStyle(ExcelStyle.STRING_STYLE);
        cell = row.createCell((short) 4);
        cell.setCellValue(getDescription());
        cell.setCellStyle(ExcelStyle.STRING_STYLE);
        cell = row.createCell((short) 5);
        cell.setCellValue(getValue().doubleValue());
        if (getValue().doubleValue() < 0)
            cell.setCellStyle(ExcelStyle.DOUBLE_NEGATIVE_STYLE);
        else
            cell.setCellStyle(ExcelStyle.DOUBLE_STYLE);
        return row;
    }

    public HSSFRow getTotalLineToExcel(HSSFRow row) {
        HSSFCell cell = row.createCell((short) 0);
        cell.setCellStyle(ExcelStyle.STRING_STYLE);
        cell.setCellValue("TOTAL");
        CellReference cellRef1 = new CellReference(1, 5);
        CellReference cellRef2 = new CellReference(((short) row.getRowNum() - 1), 5);
        cell = row.createCell((short) 5);
        cell.setCellStyle(ExcelStyle.DOUBLE_STYLE);
        cell.setCellFormula("sum(" + cellRef1.toString() + ":" + cellRef2.toString() + ")");
        return row;
    }

    public Double getValue(int column) {
        switch (column) {
        case 5:
            return getValue();
        default:
            return null;
        }
    }
}
