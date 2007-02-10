/*
 * Created on Jan 17, 2005
 */
package net.sourceforge.fenixedu.domain.degree.enrollment.rules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseGroup;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseEnrollmentType;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.domain.degree.enrollment.CurricularCourse2Enroll;
import net.sourceforge.fenixedu.domain.exceptions.EnrolmentRuleDomainException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * @author nmgo
 */
public class SpecificLEICEnrollmentRule extends SpecificEnrolmentRule implements IEnrollmentRule {

    private final static String PORTFOLIO_V_CODE = "B3K";
    
    private final static String PORTFOLIO_VI_CODE = "B3O";
    
    private final static String[] NOT_OPTIONAL_5 = {"B3K", "B3O", "B63", "B64", "BAI"};

    protected Integer optionalCourses = 0;

    protected boolean isSpecAreaDone = false;

    protected boolean isSecAreaDone = false;

    protected List optionalCoursesList = new ArrayList();

    protected List specializationAndSecundaryAreaCurricularCoursesToCountForCredits = null;

    public SpecificLEICEnrollmentRule(StudentCurricularPlan studentCurricularPlan,
	    ExecutionPeriod executionPeriod) {
	this.studentCurricularPlan = studentCurricularPlan;
	this.executionPeriod = executionPeriod;
    }

    protected Integer getOptionalCourses() {
	return optionalCourses;
    }

    protected void setOptionalCourses(Integer optionalCourses) {
	this.optionalCourses = optionalCourses;
    }

    protected List specificAlgorithm(StudentCurricularPlan studentCurricularPlan) {

	HashMap creditsInScientificAreas = new HashMap();
	HashMap creditsInSpecializationAreaGroups = new HashMap();
	HashMap creditsInSecundaryAreaGroups = new HashMap();
	int creditsInAnySecundaryArea = 0;

	Collection allCurricularCourses = getSpecializationAndSecundaryAreaCurricularCourses(studentCurricularPlan);

	specializationAndSecundaryAreaCurricularCoursesToCountForCredits = getSpecializationAndSecundaryAreaCurricularCoursesToCountForCredits(allCurricularCourses);

	calculateGroupsCreditsFromEnrollments(studentCurricularPlan,
		specializationAndSecundaryAreaCurricularCoursesToCountForCredits,
		creditsInScientificAreas, creditsInSpecializationAreaGroups,
		creditsInSecundaryAreaGroups);

	/*this.creditsInSecundaryArea = calculateCredits(studentCurricularPlan.getSecundaryBranch().getSecondaryCredits(), creditsInSecundaryAreaGroups);
	 this.creditsInSpecializationArea = calculateCredits(studentCurricularPlan.getBranch().getSpecializationCredits(),
	 creditsInSpecializationAreaGroups);*/

	return selectCurricularCourses(studentCurricularPlan, creditsInSpecializationAreaGroups,
		creditsInSecundaryAreaGroups, creditsInAnySecundaryArea,
		specializationAndSecundaryAreaCurricularCoursesToCountForCredits);
    }

    /**
     * @param maxCredits
     * @param studentCurricularPlan2
     * @param creditsInSecundaryAreaGroups
     * @return
     */
    protected Integer calculateCredits(Integer maxCredits, HashMap creditsAreaGroups) {

	int credits = 0;
	for (Iterator iter = creditsAreaGroups.values().iterator(); iter.hasNext();) {
	    Integer value = (Integer) iter.next();
	    credits += value.intValue();
	}

	if (credits >= maxCredits.intValue()) {
	    credits = maxCredits.intValue();
	}

	return new Integer(credits);
    }

    protected List filter(StudentCurricularPlan studentCurricularPlan, ExecutionPeriod executionPeriod,
	    List curricularCoursesToBeEnrolledIn,
	    final List selectedCurricularCoursesFromSpecializationAndSecundaryAreas) {

	final List curricularCoursesFromCommonAreas = getCommonAreasCurricularCourses(studentCurricularPlan);

	List result = (List) CollectionUtils.select(curricularCoursesToBeEnrolledIn, new Predicate() {
	    public boolean evaluate(Object obj) {
		CurricularCourse2Enroll curricularCourse2Enroll = (CurricularCourse2Enroll) obj;
		return curricularCoursesFromCommonAreas.contains(curricularCourse2Enroll
			.getCurricularCourse());
	    }
	});

	List curricularCoursesFromOtherAreasToMantain = (List) CollectionUtils.select(
		curricularCoursesToBeEnrolledIn, new Predicate() {
		    public boolean evaluate(Object obj) {
			CurricularCourse2Enroll curricularCourse2Enroll = (CurricularCourse2Enroll) obj;
			return selectedCurricularCoursesFromSpecializationAndSecundaryAreas
				.contains(curricularCourse2Enroll.getCurricularCourse())
				|| SpecificLEICEnrollmentRule.this.studentCurricularPlan
					.getDegreeCurricularPlan().getTFCs().contains(
						curricularCourse2Enroll.getCurricularCourse());
		    }
		});

	List specializationAreaCurricularCourses = getSpecializationAreaCurricularCourses(studentCurricularPlan);
	List secundaryAreaCurricularCourses = getSecundaryAreaCurricularCourses(studentCurricularPlan);

	if (thereIsAnyTemporaryCurricularCourse(studentCurricularPlan, executionPeriod,
		specializationAreaCurricularCourses)) {
	    markCurricularCourses(curricularCoursesFromOtherAreasToMantain,
		    specializationAreaCurricularCourses);
	}

	if (thereIsAnyTemporaryCurricularCourse(studentCurricularPlan, executionPeriod,
		secundaryAreaCurricularCourses)) {
	    markCurricularCourses(curricularCoursesFromOtherAreasToMantain,
		    secundaryAreaCurricularCourses);
	}

	result.addAll(curricularCoursesFromOtherAreasToMantain);
	//FIXME
	if (result.isEmpty() || allTemporaryOrNotOptional(result)) {
	    result.addAll(getOptionalCourses(result));
	    /*IEnrollmentRule enrollmentRule = new MaximumNumberEctsCreditsEnrolmentRule(studentCurricularPlan, executionPeriod);
	    try {
		result = enrollmentRule.apply(result);
	    } catch (EnrolmentRuleDomainException e) {
	    }*/
	} else {
	    IEnrollmentRule enrollmentRule = new PreviousYearsCurricularCourseEnrollmentRule(
		    studentCurricularPlan, executionPeriod);
	    try {
		result = enrollmentRule.apply(result);
	    } catch (EnrolmentRuleDomainException e) {
	    }
	}
	return result;
    }


    protected void calculateGroupsCreditsFromEnrollments(StudentCurricularPlan studentCurricularPlan,
	    List specializationAndSecundaryAreaCurricularCoursesToCountForCredits,
	    HashMap creditsInScientificAreas, HashMap creditsInSpecializationAreaGroups,
	    HashMap creditsInSecundaryAreaGroups) {

	Integer specCredits = 0;
	Integer secCredits = 0;

	List specCurricularCourses = getSpecializationAreaCurricularCourses(studentCurricularPlan);
	List secCurricularCourses = getSecundaryAreaCurricularCourses(studentCurricularPlan);

	optionalCoursesList.addAll(specializationAndSecundaryAreaCurricularCoursesToCountForCredits);

	int size = specializationAndSecundaryAreaCurricularCoursesToCountForCredits.size();
	for (int i = 0; i < size; i++) {
	    CurricularCourse curricularCourse = (CurricularCourse) specializationAndSecundaryAreaCurricularCoursesToCountForCredits
		    .get(i);

	    Integer curricularCourseCredits = new Integer(curricularCourse.getCredits().intValue());

	    if (specCurricularCourses.contains(curricularCourse)) {
		if (!isSpecAreaDone) {
		    specCredits = specCredits + curricularCourseCredits;
		    optionalCoursesList.remove(curricularCourse);
		    if (isAreaDone(studentCurricularPlan.getBranch().getSpecializationCredits(),
			    specCredits)) {
			isSpecAreaDone = true;
		    }
		}
		//sumInHashMap(creditsInSpecializationAreaGroups, studentCurricularPlan.getBranch().getIdInternal(), curricularCourseCredits);
	    } else if (secCurricularCourses.contains(curricularCourse)) {
		if (!isSecAreaDone) {
		    secCredits = secCredits + curricularCourseCredits;
		    optionalCoursesList.remove(curricularCourse);
		    if (isAreaDone(studentCurricularPlan.getSecundaryBranch().getSecondaryCredits(),
			    secCredits)) {
			isSecAreaDone = true;
		    }
		}
		//sumInHashMap(creditsInSecundaryAreaGroups, studentCurricularPlan.getSecundaryBranch().getIdInternal(), curricularCourseCredits);
	    }
	}

	this.creditsInSecundaryArea = secCredits;
	this.creditsInSpecializationArea = specCredits;
    }

    protected List selectCurricularCourses(StudentCurricularPlan studentCurricularPlan,
	    HashMap creditsInSpecializationAreaGroups, HashMap creditsInSecundaryAreaGroups,
	    int creditsInAnySecundaryArea,
	    List specializationAndSecundaryAreaCurricularCoursesToCountForCredits) {

	/*boolean isSpecializationAreaDone = isAreaDone(studentCurricularPlan.getBranch().getSpecializationCredits(),
	 creditsInSpecializationArea);
	 boolean isSecundaryAreaDone = isAreaDone(studentCurricularPlan.getSecundaryBranch().getSecondaryCredits(),
	 creditsInSecundaryArea);*/

	Set finalListOfCurricularCourses = new HashSet();

	if (!isSpecAreaDone) {
	    finalListOfCurricularCourses
		    .addAll(getSpecializationAreaCurricularCourses(studentCurricularPlan));
	}

	if (!isSecAreaDone) {
	    finalListOfCurricularCourses
		    .addAll(getSecundaryAreaCurricularCourses(studentCurricularPlan));
	}

	finalListOfCurricularCourses
		.removeAll(specializationAndSecundaryAreaCurricularCoursesToCountForCredits);

	List result = new ArrayList();
	result.addAll(finalListOfCurricularCourses);
	return result;
    }

    protected boolean isAreaDone(Integer maxCredits, Integer credits) {
	if (credits.intValue() >= maxCredits.intValue())
	    return true;
	return false;
    }

    //FIXME
    protected boolean allTemporaryOrNotOptional(List<CurricularCourse2Enroll> result) {
	List<String> notOptional5Courses = getNotOptional5Courses();
	List<CurricularCourse> secundaryCourses = getSecundaryAreaCurricularCourses(studentCurricularPlan);
	List<CurricularCourse> specializationCourses = getSpecializationAreaCurricularCourses(studentCurricularPlan);
	for (CurricularCourse2Enroll enroll : result) {
	    if ((!secundaryCourses.contains(enroll.getCurricularCourse()) || !specializationCourses.contains(enroll.getCurricularCourse())) && !notOptional5Courses.contains(enroll.getCurricularCourse().getCode()) && !enroll.getEnrollmentType().equals(CurricularCourseEnrollmentType.TEMPORARY)) {
		return false;
	    }
	}
	return true;
    }
    
    private List<String> getNotOptional5Courses(){
	return Arrays.asList(NOT_OPTIONAL_5);
    }

    protected List<CurricularCourse2Enroll> getOptionalCourses(List<CurricularCourse2Enroll> result) {

	optionalCourses = optionalCoursesList.size();
	List<CurricularCourse> curricularCourses = getOptionalCurricularCourses();
	List<CurricularCourse2Enroll> curricularCoursesToEnroll = new ArrayList<CurricularCourse2Enroll>();

	if (optionalCourses.intValue() < 2) {
	    if (!hasSpecializationCourse(result)) {
		curricularCoursesToEnroll.addAll(transformToCurricularCoursesToEnroll(curricularCourses,
			false));
	    } else {
		curricularCoursesToEnroll.addAll(transformToCurricularCoursesToEnroll(curricularCourses,
			true));
	    }

	    curricularCoursesToEnroll = studentCurricularPlan
		    .initAcumulatedEnrollments(curricularCoursesToEnroll);
	}
	studentCurricularPlan.initEctsCreditsToEnrol(curricularCoursesToEnroll, executionPeriod);
	
	return curricularCoursesToEnroll;
    }

    private boolean hasSpecializationCourse(List<CurricularCourse2Enroll> result) {
	List<CurricularCourse> specializationCourses = getSpecializationAreaCurricularCourses(studentCurricularPlan);
	for (CurricularCourse2Enroll curricularCourse2Enroll : result) {
	    if(specializationCourses.contains(curricularCourse2Enroll.getCurricularCourse())) {
		return true;
	    }
	}
	return false;
    }

    protected List<CurricularCourse> getOptionalCurricularCourses() {
	List<CurricularCourse> result = new ArrayList<CurricularCourse>();
	List<CurricularCourse> tagusCourses = getTagus4And5Courses();
	List<CurricularCourse> leicCourses = getLEIC4And5Courses();
	result.addAll(tagusCourses);
	result.addAll(leicCourses);
	return result;
    }

    protected List<CurricularCourse2Enroll> transformToCurricularCoursesToEnroll(
	    List<CurricularCourse> curricularCourses, boolean temporary) {
	List<CurricularCourse2Enroll> result = new ArrayList<CurricularCourse2Enroll>();
	for (CurricularCourse curricularCourse : curricularCourses) {
	    CurricularCourse2Enroll course2Enroll = new CurricularCourse2Enroll();
	    course2Enroll.setCurricularCourse(curricularCourse);
	    course2Enroll.setOptionalCurricularCourse(Boolean.TRUE);
	    course2Enroll.setCurricularYear(curricularCourse.getCurricularYearByBranchAndSemester(null,
		    executionPeriod.getSemester()));
	    if (temporary) {
		course2Enroll.setEnrollmentType(CurricularCourseEnrollmentType.TEMPORARY);
	    } else {
		course2Enroll.setEnrollmentType(CurricularCourseEnrollmentType.DEFINITIVE);
	    }
	    result.add(course2Enroll);
	}

	return result;
    }

    protected List<CurricularCourse> getLEIC4And5Courses() {
	Set<CurricularCourse> allCurricularCourses = new HashSet<CurricularCourse>();

	Set<CurricularCourse> areaCourses = new HashSet<CurricularCourse>();

	for (Branch branch : (List<Branch>) studentCurricularPlan.getDegreeCurricularPlan()
		.getSpecializationAreas()) {
	    addOldCurricularCourses(allCurricularCourses, branch.getCurricularCourseGroups());
	}
	for (Branch branch : (List<Branch>) studentCurricularPlan.getDegreeCurricularPlan()
		.getSecundaryAreas()) {
	    addOldCurricularCourses(allCurricularCourses, branch.getCurricularCourseGroups());
	}
	allCurricularCourses.removeAll(specializationAndSecundaryAreaCurricularCoursesToCountForCredits);
	if(!isSecAreaDone) {
	    allCurricularCourses.removeAll(getSecundaryAreaCurricularCourses(studentCurricularPlan));
	}
	addOldCurricularCourses(allCurricularCourses, studentCurricularPlan.getDegreeCurricularPlan()
		.getAllOptionalCurricularCourseGroups());

	for (CurricularCourse course : allCurricularCourses) {
	    if (!studentCurricularPlan.isCurricularCourseApproved(course)
		    && !studentCurricularPlan.isCurricularCourseEnrolledInExecutionPeriod(course,
			    executionPeriod)) {
		if (isAnyScopeActive(course.getScopes())) {
		    areaCourses.add(course);
		}
	    } else {
		if (isAnyScopeActive(course.getScopes())) {
		    optionalCourses++;
		}
	    }
	}

	return new ArrayList<CurricularCourse>(areaCourses);
    }

    protected boolean isAnyScopeActive(List<CurricularCourseScope> scopes) {
	for (CurricularCourseScope curricularCourseScope : scopes) {
	    if (curricularCourseScope.isActive()
		    && curricularCourseScope.getCurricularSemester().getSemester().equals(
			    executionPeriod.getSemester())) {
		return true;
	    }
	}
	return false;
    }

    protected List<CurricularCourse> getTagus4And5Courses() {
	DegreeCurricularPlan tagusCurricularPlan = RootDomainObject.getInstance()
		.readDegreeCurricularPlanByOID(89);
	Set<CurricularCourse> areaCourses = new HashSet<CurricularCourse>();
	Set<CurricularCourse> allCurricularCourses = new HashSet<CurricularCourse>();

	for (Branch branch : (List<Branch>) tagusCurricularPlan.getSpecializationAreas()) {
	    addOldCurricularCourses(allCurricularCourses, branch.getCurricularCourseGroups());
	}
	for (Branch branch : (List<Branch>) tagusCurricularPlan.getSecundaryAreas()) {
	    addOldCurricularCourses(allCurricularCourses, branch.getCurricularCourseGroups());
	}
	addOldCurricularCourses(allCurricularCourses, tagusCurricularPlan
		.getAllOptionalCurricularCourseGroups());

	for (CurricularCourse course : allCurricularCourses) {
	    if (!isApproved(course)
		    && !studentCurricularPlan.isCurricularCourseEnrolledInExecutionPeriod(course,
			    executionPeriod)) {
		if (!studentCurricularPlan.isCurricularCourseApproved(course)
			&& isAnyScopeActive(course.getScopes())) {
		    areaCourses.add(course);
		}
	    } else {
		if (isAnyScopeActive(course.getScopes())) {
		    optionalCourses++;
		}
	    }
	}

	return new ArrayList<CurricularCourse>(areaCourses);
    }

    private void addOldCurricularCourses(Set<CurricularCourse> allCurricularCourses,
	    List<CurricularCourseGroup> curricularCourseGroups) {
	for (CurricularCourseGroup curricularCourseGroup : curricularCourseGroups) {
	    for (CurricularCourse curricularCourse : curricularCourseGroup.getCurricularCourses()) {
		if (!curricularCourse.isBolonha()) {
		    allCurricularCourses.add(curricularCourse);
		}
	    }
	}
    }

    protected boolean isAnyScopeActive(List<CurricularCourseScope> scopes, List<Integer> years) {
	for (CurricularCourseScope curricularCourseScope : scopes) {
	    if (curricularCourseScope.isActive()
		    && years.contains(curricularCourseScope.getCurricularSemester().getCurricularYear()
			    .getYear())
		    && curricularCourseScope.getCurricularSemester().getSemester().equals(
			    executionPeriod.getSemester())) {
		return true;
	    }
	}
	return false;
    }

    protected boolean isApproved(CurricularCourse curricularCourse) {
	for (Enrolment enrolment : studentCurricularPlan.getEnrolments()) {
	    if (enrolment.getCurricularCourse().equals(curricularCourse)
		    && enrolment.getEnrollmentState().equals(EnrollmentState.APROVED)) {
		return true;
	    }
	}
	return false;
    }

    protected List removeAreaCurricularCourses(List curricularCoursesToBeEnrolledIn) {
	//remove all optional, area and Portfólio V

	final List areasCurricularCourses = studentCurricularPlan.getDegreeCurricularPlan()
		.getCurricularCoursesFromAnyArea();
	final List optionalCurricularCourses = new ArrayList();
	for (CurricularCourseGroup curricularCourseGroup : (List<CurricularCourseGroup>) studentCurricularPlan
		.getDegreeCurricularPlan().getAllOptionalCurricularCourseGroups()) {
	    optionalCurricularCourses.addAll(curricularCourseGroup.getCurricularCourses());
	}

	List result = (List) CollectionUtils.select(curricularCoursesToBeEnrolledIn, new Predicate() {

	    public boolean evaluate(Object arg0) {
		CurricularCourse2Enroll course2Enroll = (CurricularCourse2Enroll) arg0;
		return (!areasCurricularCourses.contains(course2Enroll.getCurricularCourse())
			&& !optionalCurricularCourses.contains(course2Enroll.getCurricularCourse())
			&& !course2Enroll.getCurricularCourse().getCode().equals(PORTFOLIO_V_CODE) 
			&& !course2Enroll.getCurricularCourse().getCode().equals(PORTFOLIO_VI_CODE)
			&& !studentCurricularPlan.getDegreeCurricularPlan().getTFCs().contains(
				course2Enroll.getCurricularCourse()));
	    }

	});
	return result;
    }
}
