/*
 * Created on Jan 12, 2005
 *
 */
package net.sourceforge.fenixedu.domain.projectsManagement;

import java.io.Serializable;

/**
 * @author Susana Fernandes
 * 
 */
public class SummaryReportLine implements Serializable, ISummaryReportLine {

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

    @Override
    public String getAcronym() {
        return acronym;
    }

    @Override
    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    @Override
    public Double getAdiantamentosPorJustificar() {
        return adiantamentosPorJustificar;
    }

    @Override
    public void setAdiantamentosPorJustificar(Double adiantamentosPorJustificar) {
        this.adiantamentosPorJustificar = adiantamentosPorJustificar;
    }

    @Override
    public Double getBudget() {
        return budget;
    }

    @Override
    public void setBudget(Double budget) {
        this.budget = budget;
    }

    @Override
    public Double getBudgetBalance() {
        return budgetBalance;
    }

    @Override
    public void setBudgetBalance(Double budgetBalance) {
        this.budgetBalance = budgetBalance;
    }

    @Override
    public Double getCabimentoPorExecutar() {
        return cabimentoPorExecutar;
    }

    @Override
    public void setCabimentoPorExecutar(Double cabimentoPorExecutar) {
        this.cabimentoPorExecutar = cabimentoPorExecutar;
    }

    @Override
    public Integer getCoordinatorCode() {
        return coordinatorCode;
    }

    @Override
    public void setCoordinatorCode(Integer coordinatorCode) {
        this.coordinatorCode = coordinatorCode;
    }

    @Override
    public Double getExpense() {
        return expense;
    }

    @Override
    public void setExpense(Double expense) {
        this.expense = expense;
    }

    @Override
    public Integer getExplorationUnit() {
        return explorationUnit;
    }

    @Override
    public void setExplorationUnit(Integer explorationUnit) {
        this.explorationUnit = explorationUnit;
    }

    @Override
    public Double getMaxFinance() {
        return maxFinance;
    }

    @Override
    public void setMaxFinance(Double maxFinance) {
        this.maxFinance = maxFinance;
    }

    @Override
    public String getProjectCode() {
        return projectCode;
    }

    @Override
    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    @Override
    public Double getRevenue() {
        return revenue;
    }

    @Override
    public void setRevenue(Double revenue) {
        this.revenue = revenue;
    }

    @Override
    public Double getTreasuryBalance() {
        return treasuryBalance;
    }

    @Override
    public void setTreasuryBalance(Double treasuryBalance) {
        this.treasuryBalance = treasuryBalance;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public Double getPartnersTransfers() {
        return partnersTransfers;
    }

    @Override
    public void setPartnersTransfers(Double partnersTransfers) {
        this.partnersTransfers = partnersTransfers;
    }
}
