/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.candidacy.erasmus;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DegreeCourseInformationBean implements java.io.Serializable, DataProvider {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    Degree chosenDegree;
    CurricularCourse chosenCourse;
    ExecutionYear executionYear;

    public DegreeCourseInformationBean(final ExecutionYear executionYear) {
        setExecutionYear(executionYear);
    }

    public DegreeCourseInformationBean() {
    }

    public Degree getChosenDegree() {
        return chosenDegree;
    }

    public void setChosenDegree(Degree chosenDegree) {
        this.chosenDegree = chosenDegree;
    }

    public CurricularCourse getChosenCourse() {
        return chosenCourse;
    }

    public void setChosenCourse(CurricularCourse chosenCourse) {
        this.chosenCourse = chosenCourse;
    }

    public ExecutionYear getExecutionYear() {
        return executionYear;
    }

    public void setExecutionYear(ExecutionYear executionYear) {
        this.executionYear = executionYear;
    }

    private List<DegreeCurricularPlan> getChosenDegreeCurricularPlans() {
        if (getChosenDegree() != null) {
    	return getChosenDegree().getDegreeCurricularPlansForYear(getExecutionYear());
        }

        return new ArrayList<DegreeCurricularPlan>();
    }

    private SortedSet<CurricularCourse> getCurricularCoursesForChosenDegree() {
        final SortedSet<CurricularCourse> result = new TreeSet<CurricularCourse>(CurricularCourse.COMPARATOR_BY_NAME);

        for (DegreeCurricularPlan degreeCurricularPlan : getChosenDegreeCurricularPlans()) {
    	result.addAll(degreeCurricularPlan.getCurricularCourses());
        }

        return result;
    }

    public Object provide(Object source, Object currentValue) {
        final DegreeCourseInformationBean chooseDegreeBean = (DegreeCourseInformationBean) source;

        return chooseDegreeBean.getCurricularCoursesForChosenDegree();
    }

    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }
}