/*
 * Created on Jan 12, 2005
 *
 */
package Dominio.projectsManagement;

import java.io.Serializable;

/**
 * @author Susana Fernandes
 * 
 */
public class SummaryReportLine implements Serializable, ISummaryReportLine {

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
}
