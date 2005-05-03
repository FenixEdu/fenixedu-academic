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

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }

    public Double getExecuted() {
        return executed;
    }

    public void setExecuted(Double executed) {
        this.executed = executed;
    }

    public Integer getRubric() {
        return rubric;
    }

    public void setRubric(Integer rubric) {
        this.rubric = rubric;
    }

    public String getRubricDescription() {
        return rubricDescription;
    }

    public void setRubricDescription(String rubricDescription) {
        this.rubricDescription = rubricDescription;
    }

}
