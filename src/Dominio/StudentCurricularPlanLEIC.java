/*
 * Created on Jul 8, 2004
 *
 */
package Dominio;

/**
 * @author João Mota
 */

public class StudentCurricularPlanLEIC extends StudentCurricularPlan implements IStudentCurricularPlan {

    protected Integer secundaryBranchKey;

    protected IBranch secundaryBranch;

    protected Integer creditsInSpecializationArea;

    protected Integer creditsInSecundaryArea;

    public StudentCurricularPlanLEIC() {
        ojbConcreteClass = getClass().getName();
    }

    public IBranch getSecundaryBranch() {
        return secundaryBranch;
    }

    public Integer getSecundaryBranchKey() {
        return secundaryBranchKey;
    }

    public void setSecundaryBranch(IBranch secundaryBranch) {
        this.secundaryBranch = secundaryBranch;
    }

    public void setSecundaryBranchKey(Integer secundaryBranchKey) {
        this.secundaryBranchKey = secundaryBranchKey;
    }

    public boolean getCanChangeSpecializationArea() {
        return false;
    }
}