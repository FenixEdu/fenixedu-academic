/*
 * Created on Jan 12, 2005
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.projectsManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;
import net.sourceforge.fenixedu.domain.projectsManagement.IMovementReport;
import net.sourceforge.fenixedu.domain.projectsManagement.IMovementReportLine;
import net.sourceforge.fenixedu.util.projectsManagement.ExcelStyle;
import net.sourceforge.fenixedu.util.projectsManagement.FormatDouble;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.hssf.util.Region;

/**
 * @author Susana Fernandes
 * 
 */
public class InfoMovementReport extends DataTranferObject implements IReportLine {

    private String parentMovementId;

    private Integer parentProjectCode;

    private Integer parentRubricId;

    private String parentType;

    private String parentDate;

    private String parentDescription;

    private Double parentValue;

    private List movements;

    public List getMovements() {
        return movements;
    }

    public void setMovements(List movements) {
        this.movements = movements;
    }

    public String getParentDate() {
        return parentDate;
    }

    public void setParentDate(String parentDate) {
        this.parentDate = parentDate;
    }

    public String getParentDescription() {
        return parentDescription;
    }

    public void setParentDescription(String parentDescription) {
        this.parentDescription = parentDescription;
    }

    public String getParentMovementId() {
        return parentMovementId;
    }

    public void setParentMovementId(String parentMovementId) {
        this.parentMovementId = parentMovementId;
    }

    public Integer getParentProjectCode() {
        return parentProjectCode;
    }

    public void setParentProjectCode(Integer parentProjectCode) {
        this.parentProjectCode = parentProjectCode;
    }

    public Integer getParentRubricId() {
        return parentRubricId;
    }

    public void setParentRubricId(Integer parentRubricId) {
        this.parentRubricId = parentRubricId;
    }

    public String getParentType() {
        return parentType;
    }

    public void setParentType(String parentType) {
        this.parentType = parentType;
    }

    public Double getParentValue() {
        return parentValue;
    }

    public void setParentValue(Double parentValue) {
        this.parentValue = parentValue;
    }

    public void copyFromDomain(IMovementReport movementReport) {
        if (movementReport != null) {
            setParentMovementId(movementReport.getParentMovementId());
            setParentProjectCode(movementReport.getParentProjectCode());
            setParentRubricId(movementReport.getParentRubricId());
            setParentType(movementReport.getParentType());
            setParentDate(movementReport.getParentDate());
            setParentDescription(movementReport.getParentDescription());
            setParentValue(movementReport.getParentValue());
            if (movementReport.getMovements() != null) {
                List infoMovementResportLineList = new ArrayList();
                for (int i = 0; i < movementReport.getMovements().size(); i++)
                    infoMovementResportLineList.add(InfoMovementReportLine.newInfoFromDomain((IMovementReportLine) movementReport.getMovements().get(
                            i)));
                setMovements(infoMovementResportLineList);
            }
        }
    }

    public static InfoMovementReport newInfoFromDomain(IMovementReport movementReport) {
        InfoMovementReport infoMovementReport = null;
        if (movementReport != null) {
            infoMovementReport = new InfoMovementReport();
            infoMovementReport.copyFromDomain(movementReport);
        }
        return infoMovementReport;
    }

    public Double getValue(int column) {
        return null;
    }

    public void getHeaderToExcel(HSSFSheet sheet) {
    }

    public void getLineToExcel(HSSFSheet sheet) {
        HSSFRow row = sheet.createRow(sheet.getLastRowNum() + 2);
        HSSFCell cell = row.createCell((short) 0);
        cell.setCellValue("Id Mov");
        cell.setCellStyle(ExcelStyle.HEADER_STYLE);
        cell = row.createCell((short) 1);
        cell.setCellValue("Rúbrica");
        cell.setCellStyle(ExcelStyle.HEADER_STYLE);
        cell = row.createCell((short) 2);
        cell.setCellValue("Tipo");
        cell.setCellStyle(ExcelStyle.HEADER_STYLE);
        cell = row.createCell((short) 3);
        cell.setCellValue("Data");
        cell.setCellStyle(ExcelStyle.HEADER_STYLE);
        cell = row.createCell((short) 4);
        cell.setCellValue("Descrição");
        cell.setCellStyle(ExcelStyle.HEADER_STYLE);
        cell = row.createCell((short) 5);
        cell.setCellStyle(ExcelStyle.HEADER_STYLE);
        cell = row.createCell((short) 6);
        cell.setCellStyle(ExcelStyle.HEADER_STYLE);
        cell = row.createCell((short) 7);
        cell.setCellStyle(ExcelStyle.HEADER_STYLE);
        sheet.addMergedRegion(new Region(row.getRowNum(), (short) 4, row.getRowNum(), (short) 7));
        cell = row.createCell((short) 8);
        cell.setCellValue("Total");
        cell.setCellStyle(ExcelStyle.HEADER_STYLE);

        row = sheet.createRow(sheet.getLastRowNum() + 1);
        cell = row.createCell((short) 0);
        cell.setCellValue(getParentMovementId());
        cell.setCellStyle(ExcelStyle.STRING_STYLE);
        cell = row.createCell((short) 1);
        cell.setCellValue(Double.parseDouble(getParentRubricId().toString()));
        cell.setCellStyle(ExcelStyle.INTEGER_STYLE);
        cell = row.createCell((short) 2);
        cell.setCellValue(getParentType());
        cell.setCellStyle(ExcelStyle.STRING_STYLE);
        cell = row.createCell((short) 3);
        cell.setCellValue(getParentDate());
        cell.setCellStyle(ExcelStyle.STRING_STYLE);
        cell = row.createCell((short) 4);
        cell.setCellValue(getParentDescription());
        cell.setCellStyle(ExcelStyle.STRING_STYLE);
        cell = row.createCell((short) 5);
        cell.setCellStyle(ExcelStyle.STRING_STYLE);
        cell = row.createCell((short) 6);
        cell.setCellStyle(ExcelStyle.STRING_STYLE);
        cell = row.createCell((short) 7);
        cell.setCellStyle(ExcelStyle.STRING_STYLE);
        sheet.addMergedRegion(new Region(row.getRowNum(), (short) 4, row.getRowNum(), (short) 7));
        cell = row.createCell((short) 8);
        cell.setCellValue(getParentValue().doubleValue());
        if (getParentValue().doubleValue() < 0)
            cell.setCellStyle(ExcelStyle.DOUBLE_NEGATIVE_STYLE);
        else
            cell.setCellStyle(ExcelStyle.DOUBLE_STYLE);
        double totalJustified = 0;
        if (movements.size() != 0) {
            row = sheet.createRow(sheet.getLastRowNum() + 1);
            ((IReportLine) movements.get(0)).getHeaderToExcel(sheet);
            for (int i = 0; i < movements.size(); i++) {
                ((IReportLine) movements.get(i)).getLineToExcel(sheet);
                totalJustified = FormatDouble.round(totalJustified + ((InfoMovementReportLine) movements.get(i)).getTotal().doubleValue());
            }
        }
        row = sheet.createRow(sheet.getLastRowNum() + 2);
        cell = row.createCell((short) 0);
        cell.setCellValue("Total:");
        cell.setCellStyle(ExcelStyle.LABEL_STYLE);
        sheet.addMergedRegion(new Region(row.getRowNum(), (short) 0, row.getRowNum(), (short) 2));
        cell = row.createCell((short) 3);
        cell.setCellValue(getParentValue().doubleValue());
        if (getParentValue().doubleValue() <= 0)
            cell.setCellStyle(ExcelStyle.DOUBLE_NEGATIVE_STYLE);
        else
            cell.setCellStyle(ExcelStyle.DOUBLE_STYLE);
        row = sheet.createRow(sheet.getLastRowNum() + 1);
        cell = row.createCell((short) 0);
        cell.setCellValue("Total Executado/Justificado:");
        cell.setCellStyle(ExcelStyle.LABEL_STYLE);
        sheet.addMergedRegion(new Region(row.getRowNum(), (short) 0, row.getRowNum(), (short) 2));
        cell = row.createCell((short) 3);
        cell.setCellValue(totalJustified);
        if (totalJustified < 0)
            cell.setCellStyle(ExcelStyle.DOUBLE_NEGATIVE_STYLE);
        else
            cell.setCellStyle(ExcelStyle.DOUBLE_STYLE);
        row = sheet.createRow(sheet.getLastRowNum() + 1);
        cell = row.createCell((short) 0);
        cell.setCellValue("Por Executar/Justificar:");
        cell.setCellStyle(ExcelStyle.LABEL_STYLE);
        sheet.addMergedRegion(new Region(row.getRowNum(), (short) 0, row.getRowNum(), (short) 2));
        cell = row.createCell((short) 3);
        double forJustify = FormatDouble.round(getParentValue().doubleValue() - totalJustified);
        cell.setCellValue(forJustify);
        if (forJustify < 0)
            cell.setCellStyle(ExcelStyle.DOUBLE_NEGATIVE_STYLE);
        else
            cell.setCellStyle(ExcelStyle.DOUBLE_STYLE);
        row = sheet.createRow(sheet.getLastRowNum() + 1);
    }

    public void getTotalLineToExcel(HSSFSheet sheet) {
        HSSFRow row = sheet.createRow(sheet.getLastRowNum() + 2);
        HSSFCell cell = row.createCell((short) 0);
        cell.setCellValue("Resumo");
        cell.setCellStyle(ExcelStyle.TITLE_STYLE);
        sheet.addMergedRegion(new Region(row.getRowNum(), (short) 0, row.getRowNum(), (short) getNumberOfColumns()));

        row = sheet.createRow(sheet.getLastRowNum() + 1);
        cell = row.createCell((short) 0);
        cell.setCellValue("Total:");
        cell.setCellStyle(ExcelStyle.LABEL_STYLE);
        sheet.addMergedRegion(new Region(row.getRowNum(), (short) 0, row.getRowNum(), (short) 2));
        cell = row.createCell((short) 3);
        CellReference cellRef1 = new CellReference(1, 8);
        CellReference cellRef2 = new CellReference(((short) row.getRowNum() - 1), 8);
        cell.setCellStyle(ExcelStyle.DOUBLE_STYLE);
        cell.setCellFormula("sum(" + cellRef1.toString() + ":" + cellRef2.toString() + ")");

        row = sheet.createRow(sheet.getLastRowNum() + 1);
        cell = row.createCell((short) 0);
        cell.setCellValue("Executados/Justificados:");
        cell.setCellStyle(ExcelStyle.LABEL_STYLE);
        sheet.addMergedRegion(new Region(row.getRowNum(), (short) 0, row.getRowNum(), (short) 2));
        cell = row.createCell((short) 3);
        cellRef1 = new CellReference(1, 7);
        cellRef2 = new CellReference(((short) row.getRowNum() - 1), 7);
        cell.setCellStyle(ExcelStyle.DOUBLE_STYLE);
        cell.setCellFormula("sum(" + cellRef1.toString() + ":" + cellRef2.toString() + ")");

        row = sheet.createRow(sheet.getLastRowNum() + 1);
        cell = row.createCell((short) 0);
        cell.setCellValue("Por Executar/Justificar:");
        cell.setCellStyle(ExcelStyle.LABEL_STYLE);
        sheet.addMergedRegion(new Region(row.getRowNum(), (short) 0, row.getRowNum(), (short) 2));
        cell = row.createCell((short) 3);
        cellRef1 = new CellReference(sheet.getLastRowNum() - 2, 3);
        cellRef2 = new CellReference(sheet.getLastRowNum() - 1, 3);
        cell.setCellStyle(ExcelStyle.DOUBLE_STYLE);
        cell.setCellFormula(cellRef1.toString() + "-" + cellRef2.toString());

    }

    public int getNumberOfColumns() {
        return 8;
    }

}
