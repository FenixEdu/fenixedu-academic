/*
 * Created on Jan 12, 2005
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.projectsManagement;

import net.sourceforge.fenixedu.domain.projectsManagement.ISummaryReportLine;
import net.sourceforge.fenixedu.util.projectsManagement.ReportType;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.util.CellReference;

import pt.utl.ist.fenix.tools.util.excel.ExcelStyle;

/**
 * @author Susana Fernandes
 * 
 */
public class InfoSummaryReportLine extends InfoReportLine {
    private Integer coordinatorCode;

    private String projectCode;

    private String acronym;

    private Integer explorationUnit;

    private String type;

    private Double budget;

    private Double maxFinance;

    private Double revenue;

    private Double partnersTransfers;

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

    public String getProjectCode() {
	return projectCode;
    }

    public void setProjectCode(String projectCode) {
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

    public Double getPartnersTransfers() {
	return partnersTransfers;
    }

    public void setPartnersTransfers(Double partnersTransfers) {
	this.partnersTransfers = partnersTransfers;
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
	    setPartnersTransfers(summaryReportLine.getPartnersTransfers());
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

    @Override
    public int getNumberOfColumns() {
	return 11;
    }

    @Override
    public void getHeaderToExcel(HSSFSheet sheet, ExcelStyle excelStyle, ReportType reportType) {
	HSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
	HSSFCell cell = row.createCell((short) 0);
	cell.setCellValue(getString("label.projNum"));
	cell.setCellStyle(excelStyle.getHeaderStyle());
	cell = row.createCell((short) 1);
	cell.setCellValue(getString("label.acronym"));
	cell.setCellStyle(excelStyle.getHeaderStyle());
	cell = row.createCell((short) 2);
	cell.setCellValue(getString("label.explorUnit"));
	cell.setCellStyle(excelStyle.getHeaderStyle());
	cell = row.createCell((short) 3);
	cell.setCellValue(getString("label.type"));
	cell.setCellStyle(excelStyle.getHeaderStyle());
	cell = row.createCell((short) 4);
	cell.setCellValue(getString("label.budget"));
	cell.setCellStyle(excelStyle.getHeaderStyle());
	cell = row.createCell((short) 5);
	cell.setCellValue(getString("label.maxFinance"));
	cell.setCellStyle(excelStyle.getHeaderStyle());
	cell = row.createCell((short) 6);
	cell.setCellValue(getString("link.revenue"));
	cell.setCellStyle(excelStyle.getHeaderStyle());
	cell = row.createCell((short) 7);
	cell.setCellValue(getString("label.partnersTransfers"));
	cell.setCellStyle(excelStyle.getHeaderStyle());
	cell = row.createCell((short) 8);
	cell.setCellValue(getString("link.expenses"));
	cell.setCellStyle(excelStyle.getHeaderStyle());
	cell = row.createCell((short) 9);
	cell.setCellValue(getString("label.toExecute.adiantamentosReport"));
	cell.setCellStyle(excelStyle.getHeaderStyle());
	cell = row.createCell((short) 10);
	cell.setCellValue(getString("label.treasuryBalance"));
	cell.setCellStyle(excelStyle.getHeaderStyle());
	cell = row.createCell((short) 11);
	cell.setCellValue(getString("label.toExecute.cabimentosReport"));
	cell.setCellStyle(excelStyle.getHeaderStyle());
	cell = row.createCell((short) 12);
	cell.setCellValue(getString("label.budgetBalance"));
	cell.setCellStyle(excelStyle.getHeaderStyle());
    }

    @Override
    public void getLineToExcel(HSSFSheet sheet, ExcelStyle excelStyle, ReportType reportType) {
	HSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
	HSSFCell cell = row.createCell((short) 0);
	cell.setCellValue(getProjectCode().toString());
	cell.setCellStyle(excelStyle.getIntegerStyle());
	cell = row.createCell((short) 1);
	cell.setCellValue(getAcronym());
	cell.setCellStyle(excelStyle.getStringStyle());
	cell = row.createCell((short) 2);
	cell.setCellValue(getExplorationUnit().toString());
	cell.setCellStyle(excelStyle.getIntegerStyle());
	cell = row.createCell((short) 3);
	cell.setCellValue(getType());
	cell.setCellStyle(excelStyle.getStringStyle());
	cell = row.createCell((short) 4);
	cell.setCellValue(getBudget().doubleValue());
	cell.setCellStyle(getBudget().doubleValue() < 0 ? excelStyle.getDoubleNegativeStyle() : excelStyle.getDoubleStyle());
	cell = row.createCell((short) 5);
	cell.setCellValue(getMaxFinance().doubleValue());
	cell.setCellStyle(getMaxFinance().doubleValue() < 0 ? excelStyle.getDoubleNegativeStyle() : excelStyle.getDoubleStyle());
	cell = row.createCell((short) 6);
	cell.setCellValue(getRevenue().doubleValue());
	cell.setCellStyle(getRevenue().doubleValue() < 0 ? excelStyle.getDoubleNegativeStyle() : excelStyle.getDoubleStyle());
	cell = row.createCell((short) 7);
	cell.setCellValue(getPartnersTransfers().doubleValue());
	cell.setCellStyle(getPartnersTransfers().doubleValue() < 0 ? excelStyle.getDoubleNegativeStyle() : excelStyle
		.getDoubleStyle());
	cell = row.createCell((short) 8);
	cell.setCellValue(getExpense().doubleValue());
	cell.setCellStyle(getExpense().doubleValue() < 0 ? excelStyle.getDoubleNegativeStyle() : excelStyle.getDoubleStyle());
	cell = row.createCell((short) 9);
	cell.setCellValue(getAdiantamentosPorJustificar().doubleValue());
	cell.setCellStyle(getAdiantamentosPorJustificar().doubleValue() < 0 ? excelStyle.getDoubleNegativeStyle() : excelStyle
		.getDoubleStyle());
	cell = row.createCell((short) 10);
	cell.setCellValue(getTreasuryBalance().doubleValue());
	cell.setCellStyle(getTreasuryBalance().doubleValue() < 0 ? excelStyle.getDoubleNegativeStyle() : excelStyle
		.getDoubleStyle());
	cell = row.createCell((short) 11);
	cell.setCellValue(getCabimentoPorExecutar().doubleValue());
	cell.setCellStyle(getCabimentoPorExecutar().doubleValue() < 0 ? excelStyle.getDoubleNegativeStyle() : excelStyle
		.getDoubleStyle());
	cell = row.createCell((short) 12);
	cell.setCellValue(getBudgetBalance().doubleValue());
	cell.setCellStyle(getBudgetBalance().doubleValue() < 0 ? excelStyle.getDoubleNegativeStyle() : excelStyle
		.getDoubleStyle());
    }

    @Override
    public void getTotalLineToExcel(HSSFSheet sheet, ExcelStyle excelStyle, ReportType reportType) {
	HSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
	HSSFCell cell = row.createCell((short) 0);
	cell.setCellStyle(excelStyle.getStringStyle());
	cell.setCellValue(getString("label.total"));
	for (int i = 4; i <= 12; i++) {
	    CellReference cellRef1 = new CellReference(1, i);
	    CellReference cellRef2 = new CellReference(((short) row.getRowNum() - 1), i);
	    cell = row.createCell((short) i);
	    cell.setCellStyle(excelStyle.getDoubleStyle());
	    cell.setCellFormula("sum(" + cellRef1.formatAsString() + ":" + cellRef2.formatAsString() + ")");
	}
    }

    @Override
    public Double getValue(int column) {
	switch (column) {
	case 4:
	    return getBudget();
	case 5:
	    return getMaxFinance();
	case 6:
	    return getRevenue();
	case 7:
	    return getPartnersTransfers();
	case 8:
	    return getExpense();
	case 9:
	    return getAdiantamentosPorJustificar();
	case 10:
	    return getTreasuryBalance();
	case 11:
	    return getCabimentoPorExecutar();
	case 12:
	    return getBudgetBalance();
	default:
	    return null;
	}
    }
}
