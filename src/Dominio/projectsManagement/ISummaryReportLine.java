/*
 * Created on Jan 27, 2005
 *
 */
package Dominio.projectsManagement;

/**
 * @author Susana Fernandes
 * 
 */
public interface ISummaryReportLine {

    public abstract String getAcronym();

    public abstract void setAcronym(String acronym);

    public abstract Double getAdiantamentosPorJustificar();

    public abstract void setAdiantamentosPorJustificar(Double adiantamentosPorJustificar);

    public abstract Double getBudget();

    public abstract void setBudget(Double budget);

    public abstract Double getBudgetBalance();

    public abstract void setBudgetBalance(Double budgetBalance);

    public abstract Double getCabimentoPorExecutar();

    public abstract void setCabimentoPorExecutar(Double cabimentoPorExecutar);

    public abstract Integer getCoordinatorCode();

    public abstract void setCoordinatorCode(Integer coordinatorCode);

    public abstract Double getExpense();

    public abstract void setExpense(Double expense);

    public abstract Integer getExplorationUnit();

    public abstract void setExplorationUnit(Integer explorationUnit);

    public abstract Double getMaxFinance();

    public abstract void setMaxFinance(Double maxFinance);

    public abstract Integer getProjectCode();

    public abstract void setProjectCode(Integer projectCode);

    public abstract Double getRevenue();

    public abstract void setRevenue(Double revenue);

    public abstract Double getTreasuryBalance();

    public abstract void setTreasuryBalance(Double treasuryBalance);

    public abstract String getType();

    public abstract void setType(String type);
}