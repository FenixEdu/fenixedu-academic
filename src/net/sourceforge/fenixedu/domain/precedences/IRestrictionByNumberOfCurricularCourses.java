package net.sourceforge.fenixedu.domain.precedences;

/**
 * @author David Santos in Jun 9, 2004
 */

public interface IRestrictionByNumberOfCurricularCourses extends IRestriction {
    public Integer getNumberOfCurricularCourses();

    public void setNumberOfCurricularCourses(Integer numberOfCurricularCourses);
}