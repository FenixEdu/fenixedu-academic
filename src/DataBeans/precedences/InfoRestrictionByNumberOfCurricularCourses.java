package DataBeans.precedences;

import Dominio.precedences.IRestrictionByNumberOfCurricularCourses;

/**
 * @author David Santos on Jul 27, 2004
 */

public class InfoRestrictionByNumberOfCurricularCourses extends InfoRestriction {

    protected Integer numberOfCurricularCourses;

    public Integer getNumberOfCurricularCourses() {
        return numberOfCurricularCourses;
    }

    public void setNumberOfCurricularCourses(Integer numberOfCurricularCourses) {
        this.numberOfCurricularCourses = numberOfCurricularCourses;
    }

    public void copyFromDomain(IRestrictionByNumberOfCurricularCourses restriction) {
        super.copyFromDomain(restriction);
        this.setNumberOfCurricularCourses(restriction.getNumberOfCurricularCourses());
    }

    public static InfoRestrictionByNumberOfCurricularCourses newInfoFromDomain(IRestrictionByNumberOfCurricularCourses restriction) {

        InfoRestrictionByNumberOfCurricularCourses infoRestriction = null;
        
        if (restriction != null) {
            infoRestriction = new InfoRestrictionByNumberOfCurricularCourses();
            infoRestriction.copyFromDomain(restriction);
        }
        
        return infoRestriction;
    }

    public String getArg() {
        return numberOfCurricularCourses.toString();
    }
}