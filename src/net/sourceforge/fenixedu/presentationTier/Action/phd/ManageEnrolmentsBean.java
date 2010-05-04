package net.sourceforge.fenixedu.presentationTier.Action.phd;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentPeriod;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInCurricularCourses;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyArrayConverter;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.presentationTier.renderers.providers.AbstractDomainObjectProvider;

import org.apache.commons.collections.comparators.ReverseComparator;
import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ManageEnrolmentsBean implements Serializable {

    static private final long serialVersionUID = 1L;

    private PhdIndividualProgramProcess process;

    private ExecutionSemester semester;

    private Collection<Enrolment> enrolmentsPerformedByStudent;

    private Collection<Enrolment> remainingEnrolments;

    private Collection<EnrolmentPeriod> enrolmentPeriods;

    private List<DegreeCurricularPlan> degreeCurricularPlans;

    private CurricularCourse curricularCourse;

    private DateTime startDate, endDate;

    public PhdIndividualProgramProcess getProcess() {
	return process;
    }

    public void setProcess(PhdIndividualProgramProcess process) {
	this.process = process;
    }

    public ExecutionSemester getSemester() {
	return semester;
    }

    public void setSemester(ExecutionSemester semester) {
	this.semester = semester;
    }

    public Collection<Enrolment> getEnrolmentsPerformedByStudent() {
	return enrolmentsPerformedByStudent;
    }

    public void setEnrolmentsPerformedByStudent(Collection<Enrolment> enrolmentsPerformedByStudent) {
	this.enrolmentsPerformedByStudent = enrolmentsPerformedByStudent;
    }

    public Collection<Enrolment> getRemainingEnrolments() {
	return remainingEnrolments;
    }

    public void setRemainingEnrolments(Collection<Enrolment> remainingEnrolments) {
	this.remainingEnrolments = remainingEnrolments;
    }

    public Collection<EnrolmentPeriod> getEnrolmentPeriods() {
	return enrolmentPeriods;
    }

    public void setEnrolmentPeriods(Collection<EnrolmentPeriod> enrolmentPeriods) {
	this.enrolmentPeriods = enrolmentPeriods;
    }

    public List<DegreeCurricularPlan> getDegreeCurricularPlans() {
	return degreeCurricularPlans;
    }

    public void setDegreeCurricularPlans(List<DegreeCurricularPlan> degreeCurricularPlans) {
	this.degreeCurricularPlans = degreeCurricularPlans;
    }

    public DateTime getStartDate() {
	return startDate;
    }

    public void setStartDate(DateTime startDate) {
	this.startDate = startDate;
    }

    public DateTime getEndDate() {
	return endDate;
    }

    public void setEndDate(DateTime endDate) {
	this.endDate = endDate;
    }

    public CurricularCourse getCurricularCourse() {
	return curricularCourse;
    }

    public void setCurricularCourse(final CurricularCourse curricularCourse) {
	this.curricularCourse = curricularCourse;
    }

    public String getCurricularCourseName() {
	return getCurricularCourse().getName(getSemester());
    }

    static public class PhdManageEnrolmentsExecutionSemestersProviders extends AbstractDomainObjectProvider {

	@Override
	public Object provide(Object source, Object obj) {
	    final ManageEnrolmentsBean bean = (ManageEnrolmentsBean) source;

	    final List<ExecutionSemester> result = new ArrayList<ExecutionSemester>();

	    ExecutionSemester each = bean.getProcess().getExecutionYear().getFirstExecutionPeriod();
	    while (each != null) {
		result.add(each);
		each = each.getNextExecutionPeriod();
	    }

	    Collections.sort(result, new ReverseComparator(ExecutionSemester.COMPARATOR_BY_SEMESTER_AND_YEAR));

	    return result;
	}
    }

    static public class PhdDegreeCurricularPlansProvider implements DataProvider {

	@Override
	public Converter getConverter() {
	    return new DomainObjectKeyArrayConverter();
	}

	@Override
	public Object provide(Object source, Object currentValue) {
	    final ManageEnrolmentsBean bean = (ManageEnrolmentsBean) source;

	    final List<DegreeCurricularPlan> result = new ArrayList<DegreeCurricularPlan>();
	    for (final ExecutionDegree executionDegree : bean.getSemester().getExecutionYear().getExecutionDegreesByType(
		    DegreeType.BOLONHA_ADVANCED_SPECIALIZATION_DIPLOMA)) {

		final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();

		if (!hasEnrolmentPeriod(degreeCurricularPlan, bean.getSemester())) {
		    result.add(degreeCurricularPlan);
		}

	    }
	    return result;
	}

	private boolean hasEnrolmentPeriod(DegreeCurricularPlan degreeCurricularPlan, ExecutionSemester semester) {
	    return semester.getEnrolmentPeriod(EnrolmentPeriodInCurricularCourses.class, degreeCurricularPlan) != null;
	}
    }

    static public class CurricularCourseDegreeExecutionSemesterProvider implements DataProvider {

	@Override
	public Converter getConverter() {
	    return new DomainObjectKeyConverter();
	}

	@Override
	public Object provide(Object source, Object currentValue) {
	    final ManageEnrolmentsBean bean = (ManageEnrolmentsBean) source;

	    final Collection<ExecutionSemester> result = new TreeSet<ExecutionSemester>(new ReverseComparator());

	    for (final ExecutionYear executionYear : bean.getCurricularCourse().getDegreeCurricularPlan().getExecutionYears()) {
		result.addAll(executionYear.getExecutionPeriods());
	    }

	    return result;
	}

    }

}