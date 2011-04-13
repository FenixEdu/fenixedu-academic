package net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.server;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import pt.ist.fenixframework.pstm.AbstractDomainObject;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Grade;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.presentationTier.Action.coordinator.xviews.Inar;
import net.sourceforge.fenixedu.presentationTier.Action.coordinator.xviews.YearViewBean;
import net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.InarService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class InarServiceImpl extends RemoteServiceServlet implements InarService {

    public String getExecutionYear(String eyId) {
	ExecutionYear executionYear = AbstractDomainObject.fromExternalId(eyId);
	String shortPre = executionYear.getQualifiedName().substring(2, 4);
	String shortPost = executionYear.getQualifiedName().substring(7, 9);
	String yearAcronym = shortPre + "/" + shortPost;

	return yearAcronym;
    }

    public int[] getInar(String eyId, String dcpId) {
	ExecutionYear executionYear = AbstractDomainObject.fromExternalId(eyId);
	DegreeCurricularPlan degreeCurricularPlan = AbstractDomainObject.fromExternalId(dcpId);

	YearViewBean yearviewBean = new YearViewBean(degreeCurricularPlan);
	yearviewBean.setExecutionYear(executionYear);
	yearviewBean.setEnrolments();

	Inar inar = new Inar();

	for (Enrolment enrol : yearviewBean.getEnrolments()) {
	    inar.incEnrolled();
	    Grade grade = enrol.getGrade();
	    if (grade == null || grade.isEmpty())
		inar.incFrequenting();
	    else if (grade.isApproved())
		inar.incApproved();
	    else if (grade.isNotEvaluated())
		inar.incNotEvaluated();
	    else if (grade.isNotApproved())
		inar.incFlunked();
	}

	return inar.exportAsArray();
    }

    public int[][] getInarByCurricularYears(String eyId, String dcpId) {
	ExecutionYear executionYear = AbstractDomainObject.fromExternalId(eyId);
	DegreeCurricularPlan degreeCurricularPlan = AbstractDomainObject.fromExternalId(dcpId);

	YearViewBean yearviewBean = new YearViewBean(degreeCurricularPlan);
	yearviewBean.setExecutionYear(executionYear);
	yearviewBean.setEnrolments();
	int totalYears = degreeCurricularPlan.getDegree().getDegreeType().getYears();

	Inar[] inarArray = new Inar[totalYears];
	for (int i = 0; i < totalYears; i++) {
	    inarArray[i] = new Inar();
	}

	for (Enrolment enrol : yearviewBean.getEnrolments()) {
	    CurricularYear year = CurricularYear.readByYear(enrol.getRegistration().getCurricularYear());
	    inarArray[year.getYear() - 1].incEnrolled();
	    Grade grade = enrol.getGrade();
	    if (grade == null || grade.isEmpty())
		inarArray[year.getYear() - 1].incFrequenting();
	    else if (grade.isApproved())
		inarArray[year.getYear() - 1].incApproved();
	    else if (grade.isNotEvaluated())
		inarArray[year.getYear() - 1].incNotEvaluated();
	    else if (grade.isNotApproved())
		inarArray[year.getYear() - 1].incFlunked();
	}

	int[][] result = new int[totalYears][5];
	for (int i = 0; i < totalYears; i++) {
	    result[i] = inarArray[i].exportAsArray();
	}
	return result;
    }

    public int getNumberOfCurricularYears(String dcpId) {
	DegreeCurricularPlan degreeCurricularPlan = AbstractDomainObject.fromExternalId(dcpId);
	return degreeCurricularPlan.getDegree().getDegreeType().getYears();
    }

    public double[] getAverageByCurricularYears(String eyId, String dcpId) {
	ExecutionYear executionYear = AbstractDomainObject.fromExternalId(eyId);
	DegreeCurricularPlan degreeCurricularPlan = AbstractDomainObject.fromExternalId(dcpId);

	YearViewBean yearviewBean = new YearViewBean(degreeCurricularPlan);
	yearviewBean.setExecutionYear(executionYear);
	yearviewBean.setEnrolments();

	int years = yearviewBean.getDegreeCurricularPlan().getDegree().getDegreeType().getYears();
	double[] result = new double[years];

	BigDecimal[] sigma = new BigDecimal[years + 1];
	BigDecimal[] cardinality = new BigDecimal[years + 1];
	BigDecimal[] average = new BigDecimal[years + 1];

	for (int i = 1; i <= years; i++) {
	    sigma[i] = new BigDecimal(0);
	    cardinality[i] = new BigDecimal(0);
	    average[i] = new BigDecimal(0);
	}

	for (Enrolment enrol : yearviewBean.getEnrolments()) {
	    CurricularYear year = CurricularYear.readByYear(enrol.getRegistration().getCurricularYear());
	    Grade grade = enrol.getGrade();
	    if (grade.isApproved() && grade.getGradeScale() == GradeScale.TYPE20) {
		BigDecimal biggy = sigma[year.getYear()];
		BigDecimal smalls = biggy.add(grade.getNumericValue());
		sigma[year.getYear()] = smalls;

		BigDecimal notorious = cardinality[year.getYear()];
		BigDecimal big = notorious.add(BigDecimal.ONE);
		cardinality[year.getYear()] = big;
	    }
	}

	for (int i = 1; i <= years; i++) {
	    if (cardinality[i].compareTo(BigDecimal.ZERO) == 0) {
		result[i - 1] = BigDecimal.ZERO.doubleValue();
	    } else {
		result[i - 1] = sigma[i].divide(cardinality[i], 2, RoundingMode.HALF_EVEN).doubleValue();
	    }
	}

	return result;
    }

    public Map<Integer, Map<Integer, List<String>>> getProblematicCourses(String eyId, String dcpId) {
	ExecutionYear executionYear = AbstractDomainObject.fromExternalId(eyId);
	DegreeCurricularPlan degreeCurricularPlan = AbstractDomainObject.fromExternalId(dcpId);

	Map<Integer, Map<Integer, List<String>>> harvester = new HashMap<Integer, Map<Integer, List<String>>>();

	List<ExecutionCourse> courses;
	ExecutionSemester referenceSemester = executionYear.getFirstExecutionPeriod();
	int years = degreeCurricularPlan.getDegree().getDegreeType().getYears();
	ExecutionSemester actualSemester = ExecutionSemester.readActualExecutionSemester();
	if (referenceSemester.isBefore(actualSemester)) {
	    for (int y = 1; y <= years; y++) {
		courses = degreeCurricularPlan.getExecutionCoursesByExecutionPeriodAndSemesterAndYear(referenceSemester, y, 1);
		List<String> situations = new ArrayList<String>();
		Map<Integer, List<String>> pairs = new HashMap<Integer, List<String>>();
		pairs.put(1, situations);
		harvester.put(y, pairs);
		for (ExecutionCourse executionCourse : courses) {
		    if (evaluateExecutionCourse(executionCourse)) {
			harvester.get(y).get(1).add(executionCourse.getExternalId());
		    }
		}
	    }
	}

	referenceSemester = executionYear.getLastExecutionPeriod();
	if (referenceSemester.isBefore(actualSemester)) {
	    for (int y = 1; y <= years; y++) {
		courses = degreeCurricularPlan.getExecutionCoursesByExecutionPeriodAndSemesterAndYear(referenceSemester, y, 2);
		List<String> situations = new ArrayList<String>();
		Map<Integer, List<String>> pairs = harvester.get(y);
		pairs.put(2, situations);
		for (ExecutionCourse executionCourse : courses) {
		    if (evaluateExecutionCourse(executionCourse)) {
			harvester.get(y).get(2).add(executionCourse.getExternalId());
		    }
		}
	    }
	}

	return harvester;
    }

    private boolean evaluateExecutionCourse(ExecutionCourse executionCourse) {
	Inar inar = new Inar();

	for (Enrolment enrol : executionCourse.getActiveEnrollments()) {
	    inar.incEnrolled();
	    Grade grade = enrol.getGrade();
	    if (grade == null || grade.isEmpty())
		inar.incFrequenting();
	    else if (grade.isApproved())
		inar.incApproved();
	    else if (grade.isNotEvaluated())
		inar.incNotEvaluated();
	    else if (grade.isNotApproved())
		inar.incFlunked();
	}

	return inar.getAB50Heuristic();
    }
    
    public String getCourseName(String ecId) {
	ExecutionCourse executionCourse = AbstractDomainObject.fromExternalId(ecId);
	return executionCourse.getName();
    }
    
    public String[] getCourseInarLabel(String ecId) {
	String[] result = new String[2];
	ExecutionCourse executionCourse = AbstractDomainObject.fromExternalId(ecId);
	String shortPre = executionCourse.getExecutionYear().getQualifiedName().substring(2, 4);
	String shortPost = executionCourse.getExecutionYear().getQualifiedName().substring(7, 9);
	result[0] = executionCourse.getSigla();
	result[1] = shortPre + "/" + shortPost;
	return result;
    }
    
    public int[] getInarByExecutionCourse(String ecId) {
	ExecutionCourse executionCourse = AbstractDomainObject.fromExternalId(ecId);

	Inar inar = new Inar();

	for (Enrolment enrol : executionCourse.getActiveEnrollments()) {
	    inar.incEnrolled();
	    Grade grade = enrol.getGrade();
	    if (grade == null || grade.isEmpty())
		inar.incFrequenting();
	    else if (grade.isApproved())
		inar.incApproved();
	    else if (grade.isNotEvaluated())
		inar.incNotEvaluated();
	    else if (grade.isNotApproved())
		inar.incFlunked();
	}

	return inar.exportAsArray();
    }
    
    public int[] getGradesDistribution(String ecId) {
	ExecutionCourse executionCourse = AbstractDomainObject.fromExternalId(ecId);
	int[] results;
	int padding;
	switch(executionCourse.getActiveEnrollments().get(0).getGradeScale()) {
	case TYPE20:
	    results = new int[11];
	    padding = 10;
	    break;
	case TYPE5:
	    results = new int[3];
	    padding = 3;
	    break;
	default:
	    return null;
	}
	for (Enrolment enrol : executionCourse.getActiveEnrollments()) {
	    if(enrol.getGrade().isNumeric())
		results[enrol.getGrade().getIntegerValue() - padding]++;
	}
	return results;
    }

}
