package net.sourceforge.fenixedu.domain.projectsManagement;

import java.io.Serializable;
import java.util.List;

public interface IProjectMemberBudget extends Serializable {

    public abstract Integer getFinancingPercentage();

    public abstract void setFinancingPercentage(Integer financingPercentage);

    public abstract String getInstitutionCode();

    public abstract void setInstitutionCode(String institutionCode);

    public abstract String getInstitutionName();

    public abstract void setInstitutionName(String institutionName);

    public abstract String getOverheads();

    public abstract void setOverheads(String overheads);

    public abstract Integer getProjectCode();

    public abstract void setProjectCode(Integer projectCode);

    public abstract List getRubricBudget();

    public abstract void setRubricBudget(List rubricBudget);

    public abstract String getTransferences();

    public abstract void setTransferences(String transferences);

    public abstract String getType();

    public abstract void setType(String type);

}