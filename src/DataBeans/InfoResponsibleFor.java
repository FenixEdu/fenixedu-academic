/*
 * Created on 14/Mai/2003 by jpvl
 *  
 */
package DataBeans;

/**
 * @author Fernanda Quitério 05/Dez/2003
 */
public class InfoResponsibleFor extends InfoObject
{
    protected InfoTeacher infoTeacher;
    protected InfoExecutionCourse infoExecutionCourse;

	private String toDelete;
	
    public InfoResponsibleFor()
    {
    }

    public InfoExecutionCourse getInfoExecutionCourse()
    {
        return infoExecutionCourse;
    }

    public InfoTeacher getInfoTeacher()
    {
        return infoTeacher;
    }

    public void setInfoExecutionCourse(InfoExecutionCourse course)
    {
        infoExecutionCourse = course;
    }

    public void setInfoTeacher(InfoTeacher teacher)
    {
        infoTeacher = teacher;
    }
	/**
	 * @return Returns the toDelete.
	 */
	public String getToDelete()
	{
		return toDelete;
	}

	/**
	 * @param toDelete The toDelete to set.
	 */
	public void setToDelete(String toDelete)
	{
		this.toDelete = toDelete;
	}

}
