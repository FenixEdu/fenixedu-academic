/*
 * Created on 14/Mai/2003 by jpvl
 *  
 */
package DataBeans;

/**
 * @author jpvl
 */
public class InfoProfessorShip extends InfoObject
{
    protected InfoTeacher infoTeacher;
    protected InfoExecutionCourse infoExecutionCourse;
    private Float credits;
    /**
	 * @return
	 */
    public InfoExecutionCourse getInfoExecutionCourse()
    {
        return infoExecutionCourse;
    }

    /**
	 * @return
	 */
    public InfoTeacher getInfoTeacher()
    {
        return infoTeacher;
    }

    /**
	 * @param course
	 */
    public void setInfoExecutionCourse(InfoExecutionCourse course)
    {
        infoExecutionCourse = course;
    }

    /**
	 * @param teacher
	 */
    public void setInfoTeacher(InfoTeacher teacher)
    {
        infoTeacher = teacher;
    }

    /**
	 * @return Returns the credits.
	 */
    public Float getCredits()
    {
        return this.credits;
    }

    /**
	 * @param credits
	 *                   The credits to set.
	 */
    public void setCredits(Float credits)
    {
        this.credits = credits;
    }

}
