package Dominio;


/**
 * @author David Santos in Jun 24, 2004
 */

public class StudentCurricularPlanLEEC extends StudentCurricularPlan implements IStudentCurricularPlan
{
	protected Integer secundaryBranchKey;
	protected IBranch secundaryBranch;

	public StudentCurricularPlanLEEC()
	{
		ojbConcreteClass = getClass().getName();
	}

	public IBranch getSecundaryBranch()
	{
		return secundaryBranch;
	}
	
	public Integer getSecundaryBranchKey()
	{
		return secundaryBranchKey;
	}
	
	public void setSecundaryBranch(IBranch secundaryBranch)
	{
		this.secundaryBranch = secundaryBranch;
	}
	
	public void setSecundaryBranchKey(Integer secundaryBranchKey)
	{
		this.secundaryBranchKey = secundaryBranchKey;
	}
	
	public String toString()
	{
		return super.toString();
	}
}