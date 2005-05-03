/*
 * Created on Jan 12, 2005
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.projectsManagement;

import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;
import net.sourceforge.fenixedu.domain.projectsManagement.ISummaryReportLine;
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
public class InfoSummaryReportLine extends DataTranferObject implements IReportLine {
    private Integer coordinatorCode;

    private Integer projectCode;

    private String acronym;

    private Integer explorationUnit;

    private String type;

    private Double budget;

    private Double maxFinance;

    private Double revenue;

    private Double expense;

    private Double adiantamentosPorJustificar;

    private Double treasuryBalance;

    private Double cabimentoPorExecutar;

    private Double budgetBalance;

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public Double getAdiantamentosPorJustificar() {
        return adiantamentosPorJustificar;
    }

    public void setAdiantamentosPorJustificar(Double adiantamentosPorJustificar) {
        this.adiantamentosPorJustificar = adiantamentosPorJustificar;
    }

    public Double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }

    public Double getBudgetBalance() {
        return budgetBalance;
    }

    public void setBudgetBalance(Double budgetBalance) {
        this.budgetBalance = budgetBalance;
    }

    public Double getCabimentoPorExecutar() {
        return cabimentoPorExecutar;
    }

    public void setCabimentoPorExecutar(Double cabimentoPorExecutar) {
        this.cabimentoPorExecutar = cabimentoPorExecutar;
    }

    public Integer getCoordinatorCode() {
        return coordinatorCode;
    }

    public void setCoordinatorCode(Integer coordinatorCode) {
        this.coordinatorCode = coordinatorCode;
    }

    public Double getExpense() {
        return expense;
    }

    public void setExpense(Double expense) {
        this.expense = expense;
    }

    public Integer getExplorationUnit() {
        return explorationUnit;
    }

    public void setExplorationUnit(Integer explorationUnit) {
        this.explorationUnit = explorationUnit;
    }

    public Double getMaxFinance() {
        return maxFinance;
    }

    public void setMaxFinance(Double maxFinance) {
        this.maxFinance = maxFinance;
    }

    public Integer getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(Integer projectCode) {
        this.projectCode = projectCode;
    }

    public Double getRevenue() {
        return revenue;
    }

    public void setRevenue(Double revenue) {
        this.revenue = revenue;
    }

    public Double getTreasuryBalance() {
        return treasuryBalance;
    }

    public void setTreasuryBalance(Double treasuryBalance) {
        this.treasuryBalance = treasuryBalance;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void copyFromDomain(ISummaryReportLine summaryReportLine) {
        if (summaryReportLine != null) {
            setCoordinatorCode(summaryReportLine.getCoordinatorCode());
            setProjectCode(summaryReportLine.getProjectCode());
            setAcronym(summaryReportLine.getAcronym());
            setExplorationUnit(summaryReportLine.getExplorationUnit());
            setType(summaryReportLine.getType());
            setBudget(summaryReportLine.getBudget());
            setMaxFinance(summaryReportLine.getMaxFinance());
            setRevenue(summaryReportLine.getRevenue());
            setExpense(summaryReportLine.getExpense());
            setAdiantamentosPorJustificar(summaryReportLine.getAdiantamentosPorJustificar());
            setTreasuryBalance(summaryReportLine.getTreasuryBalance());
            setCabimentoPorExecutar(summaryReportLine.getCabimentoPorExecutar());
            setBudgetBalance(summaryReportLine.getBudgetBalance());
        }
    }

    public static InfoSummaryReportLine newInfoFromDomain(ISummaryReportLine summaryReportLine) {
        InfoSummaryReportLine infoSummaryReportLine = null;
        if (summaryReportLine != null) {
            infoSummaryReportLine = new InfoSummaryReportLine();
            infoSummaryReportLine.copyFromDomain(summaryReportLine);
        }
        return infoSummaryReportLine;
    }

    public int getNumberOfColumns() {
        return 11;
    }

    public void getHeaderToExcel(HSSFSheet sheet, ExcelStyle excelStyle, ReportType reportType) {
        HSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
        HSSFCell cell = row.createCell((short) 0);
        cell.setCellValue("NºProj");
        cell.setCellStyle(excelStyle.getHeaderStyle());
        cell = row.createCell((short) 1);
        cell.setCellValue("Acrónimo");
        cell.setCellStyle(excelStyle.getHeaderStyle());
        cell = row.createCell((short) 2);
        cell.setCellValue("Unid Expl");
        cell.setCellStyle(excelStyle.getHeaderStyle());
        cell = row.createCell((short) 3);
        cell.setCellValue("Tipo");
        cell.setCellStyle(excelStyle.getHeaderStyle());
        cell = row.createCell((short) 4);
        cell.setCellValue("Orçamento");
        cell.setCellStyle(excelStyle.getHeaderStyle());
        cell = row.createCell((short) 5);
        cell.setCellValue("Máximo Financiável");
        cell.setCellStyle(excelStyle.getHeaderStyle());
        cell = row.createCell((short) 6);
        cell.setCellValue("Receita");
        cell.setCellStyle(excelStyle.getHeaderStyle());
        cell = row.createCell((short) 7);
        cell.setCellValue("Despesa");
        cell.setCellStyle(excelStyle.getHeaderStyle());
        cell = row.createCell((short) 8);
        cell.setCellValue("Adiantamentos por Justificar");
        cell.setCellStyle(excelStyle.getHeaderStyle());
        cell = row.createCell((short) 9);
        cell.setCellValue("Saldo Tesouraria");
        cell.setCellStyle(excelStyle.getHeaderStyle());
        cell = row.createCell((short) 10);
        cell.setCellValue("Cabimentos por Executar");
        cell.setCellStyle(excelStyle.getHeaderStyle());
        cell = row.createCell((short) 11);
        cell.setCellValue("Saldo Orçamental (*)");
        cell.setCellStyle(excelStyle.getHeaderStyle());
    }

    public void getLineToExcel(HSSFSheet sheet, ExcelStyle excelStyle, ReportType reportType) {
        HSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
        HSSFCell cell = row.createCell((short) 0);
        cell.setCellValue(Double.parseDouble(getProjectCode().toString()));
        cell.setCellStyle(excelStyle.getIntegerStyle());
        cell = row.createCell((short) 1);
        cell.setCellValue(getAcronym());
        cell.setCellStyle(excelStyle.getStringStyle());
        cell = row.createCell((short) 2);
        cell.setCellValue(Double.parseDouble(getExplorationUnit().toString()));
        cell.setCellStyle(excelStyle.getIntegerStyle());
        cell = row.createCell((short) 3);
        cell.setCellValue(getType());
        cell.setCellStyle(excelStyle.getStringStyle());
        cell = row.createCell((short) 4);
        cell.setCellValue(getBudget().doubleValue());
        if (getBudget().doubleValue() < 0)
            cell.setCellStyle(excelStyle.getDoubleNegativeStyle());
        else
            cell.setCellStyle(excelStyle.getDoubleStyle());
        cell = row.createCell((short) 5);
        cell.setCellValue(getMaxFinance().doubleValue());
        if (getMaxFinance().doubleValue() < 0)
            cell.setCellStyle(excelStyle.getDoubleNegativeStyle());
        else
            cell.setCellStyle(excelStyle.getDoubleStyle());
        cell = row.createCell((short) 6);
        cell.setCellValue(getRevenue().doubleValue());
        if (getRevenue().doubleValue() < 0)
            cell.setCellStyle(excelStyle.getDoubleNegativeStyle());
        else
            cell.setCellStyle(excelStyle.getDoubleStyle());
        cell = row.createCell((short) 7);
        cell.setCellValue(getExpense().doubleValue());
        if (getExpense().doubleValue() < 0)
            cell.setCellStyle(excelStyle.getDoubleNegativeStyle());
        else
            cell.setCellStyle(excelStyle.getDoubleStyle());
        cell = row.createCell((short) 8);
        cell.setCellValue(getAdiantamentosPorJustificar().doubleValue());
        if (getAdiantamentosPorJustificar().doubleValue() < 0)
            cell.setCellStyle(excelStyle.getDoubleNegativeStyle());
        else
            cell.setCellStyle(excelStyle.getDoubleStyle());
        cell = row.createCell((short) 9);
        cell.setCellValue(getTreasuryBalance().doubleValue());
        if (getTreasuryBalance().doubleValue() < 0)
            cell.setCellStyle(excelStyle.getDoubleNegativeStyle());
        else
            cell.setCellStyle(excelStyle.getDoubleStyle());
        cell = row.createCell((short) 10);
        cell.setCellValue(getCabimentoPorExecutar().doubleValue());
        if (getCabimentoPorExecutar().doubleValue() < 0)
            cell.setCellStyle(excelStyle.getDoubleNegativeStyle());
        else
            cell.setCellStyle(excelStyle.getDoubleStyle());
        cell = row.createCell((short) 11);
        cell.setCellValue(getBudgetBalance().doubleValue());
        if (getBudgetBalance().doubleValue() < 0)
            cell.setCellStyle(excelStyle.getDoubleNegativeStyle());
        else
            cell.setCellStyle(excelStyle.getDoubleStyle());
    }

    public void getTotalLineToExcel(HSSFSheet sheet, ExcelStyle excelStyle, ReportType reportType) {
        HSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
        HSSFCell cell = row.createCell((short) 0);
        cell.setCellStyle(excelStyle.getStringStyle());
        cell.setCellValue("TOTAL");
        for (int i = 4; i <= 11; i++) {
            CellReference cellRef1 = new CellReference(1, i);
            CellReference cellRef2 = new CellReference(((short) row.getRowNum() - 1), i);
            cell = row.createCell((short) i);
            cell.setCellStyle(excelStyle.getDoubleStyle());
            cell.setCellFormula("sum(" + cellRef1.toString() + ":" + cellRef2.toString() + ")");
        }
    }

    public Double getValue(int column) {
        switch (column) {
        case 4:
            return getBudget();
        case 5:
            return getMaxFinance();
        case 6:
            return getRevenue();
        case 7:
            return getExpense();
        case 8:
            return getAdiantamentosPorJustificar();
        case 9:
            return getTreasuryBalance();
        case 10:
            return getCabimentoPorExecutar();
        case 11:
            return getBudgetBalance();
        default:
            return null;
        }
    }
}
