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
public class ProjectBudgetaryBalanceReportLine implements Serializable, IProjectBudgetaryBalanceReportLine {

    private Integer rubric;

    private String rubricDescription;

    private Double budget;

    private Double executed;

    private Double balance;

    @Override
    public Double getBalance() {
        return balance;
    }

    @Override
    public void setBalance(Double balance) {
        this.balance = balance;
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
    public Double getExecuted() {
        return executed;
    }

    @Override
    public void setExecuted(Double executed) {
        this.executed = executed;
    }

    @Override
    public Integer getRubric() {
        return rubric;
    }

    @Override
    public void setRubric(Integer rubric) {
        this.rubric = rubric;
    }

    @Override
    public String getRubricDescription() {
        return rubricDescription;
    }

    @Override
    public void setRubricDescription(String rubricDescription) {
        this.rubricDescription = rubricDescription;
    }

}
