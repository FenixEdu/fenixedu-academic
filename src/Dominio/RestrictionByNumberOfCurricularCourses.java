package Dominio;

/**
 * @author David Santos
 */

public abstract class RestrictionByNumberOfCurricularCourses
    extends Restriction
    implements IRestrictionByNumberOfCurricularCourses
{

    protected Integer numberOfCurricularCourses;

    public Integer getNumberOfCurricularCourses()
    {
        return numberOfCurricularCourses;
    }

    public void setNumberOfCurricularCourses(Integer numberOfCurricularCourseDone)
    {
        this.numberOfCurricularCourses = numberOfCurricularCourseDone;
    }

    public boolean equals(Object obj)
    {
        boolean result = super.equals(obj);
        if ((result) && (obj instanceof IRestrictionByNumberOfCurricularCourses))
        {
            IRestrictionByNumberOfCurricularCourses restrictionByNumberOfCurricularCourses =
                (IRestrictionByNumberOfCurricularCourses) obj;
            result =
                restrictionByNumberOfCurricularCourses.getNumberOfCurricularCourses().equals(
                    this.getNumberOfCurricularCourses())
                    && this.getClass().getName().equals(
                        restrictionByNumberOfCurricularCourses.getClass().getName());
        }
        return result;
    }
}