package net.sourceforge.fenixedu.domain.projectsManagement;

import java.io.Serializable;

public interface IProjectBudgetaryBalanceReportLine extends Serializable {

    public abstract Double getBalance();

    public abstract void setBalance(Double balance);

    public abstract Double getBudget();

    public abstract void setBudget(Double budget);

    public abstract Double getExecuted();

    public abstract void setExecuted(Double executed);

    public abstract Integer getRubric();

    public abstract void setRubric(Integer rubric);

    public abstract String getRubricDescription();

    public abstract void setRubricDescription(String rubricDescription);

}