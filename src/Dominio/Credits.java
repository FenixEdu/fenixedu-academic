package Dominio;

/**
 * @author Alexandra Alves
 */
public class Credits extends DomainObject implements ICredits
{
    private Double degreeFinalProjectStudents;

    private IExecutionPeriod executionPeriod;

    private Double institutionWorkTime;

    private Integer keyExecutionPeriod;

    private Integer keyTeacher;

    private Double lessons;

    private Double supportLessons;

    private ITeacher teacher;

    public boolean equals( Object obj )
    {
        boolean result = false;
        if (obj instanceof ICredits)
        {
            ICredits credits = (ICredits) obj;
            result = this.getExecutionPeriod().equals(credits.getExecutionPeriod())
                            && this.getTeacher().equals(credits.getTeacher());
        }
        return result;
    }

    /**
     * @return Returns the degreeFinalProjectStudents.
     */
    public Double getDegreeFinalProjectStudents()
    {
        return this.degreeFinalProjectStudents;
    }

    /**
     * @return Returns the executionPeriod.
     */
    public IExecutionPeriod getExecutionPeriod()
    {
        return this.executionPeriod;
    }

    /**
     * @return Returns the institutionWorkTime.
     */
    public Double getInstitutionWorkTime()
    {
        return this.institutionWorkTime;
    }

    /**
     * @return Returns the keyExecutionPeriod.
     */
    public Integer getKeyExecutionPeriod()
    {
        return this.keyExecutionPeriod;
    }

    /**
     * @return Returns the keyTeacher.
     */
    public Integer getKeyTeacher()
    {
        return this.keyTeacher;
    }

    /**
     * @return Returns the lessons.
     */
    public Double getLessons()
    {
        return this.lessons;
    }

    /**
     * @return Returns the supportLessons.
     */
    public Double getSupportLessons()
    {
        return this.supportLessons;
    }

    /**
     * @return Returns the teacher.
     */
    public ITeacher getTeacher()
    {
        return this.teacher;
    }

    /**
     * @param degreeFinalProjectStudents
     *                   The degreeFinalProjectStudents to set.
     */
    public void setDegreeFinalProjectStudents( Double degreeFinalProjectStudents )
    {
        this.degreeFinalProjectStudents = degreeFinalProjectStudents;
    }

    /**
     * @param executionPeriod
     *                   The executionPeriod to set.
     */
    public void setExecutionPeriod( IExecutionPeriod executionPeriod )
    {
        this.executionPeriod = executionPeriod;
    }

    /**
     * @param institutionWorkTime
     *                   The institutionWorkTime to set.
     */
    public void setInstitutionWorkTime( Double institutionWorkTime )
    {
        this.institutionWorkTime = institutionWorkTime;
    }

    /**
     * @param keyExecutionPeriod
     *                   The keyExecutionPeriod to set.
     */
    public void setKeyExecutionPeriod( Integer keyExecutionPeriod )
    {
        this.keyExecutionPeriod = keyExecutionPeriod;
    }

    /**
     * @param keyTeacher
     *                   The keyTeacher to set.
     */
    public void setKeyTeacher( Integer keyTeacher )
    {
        this.keyTeacher = keyTeacher;
    }

    /**
     * @param lessons
     *                   The lessons to set.
     */
    public void setLessons( Double lessons )
    {
        this.lessons = lessons;
    }

    /**
     * @param supportLessons
     *                   The supportLessons to set.
     */
    public void setSupportLessons( Double supportLessons )
    {
        this.supportLessons = supportLessons;
    }

    /**
     * @param teacher
     *                   The teacher to set.
     */
    public void setTeacher( ITeacher teacher )
    {
        this.teacher = teacher;
    }
}
