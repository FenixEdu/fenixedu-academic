package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.BothAreasAreTheSameServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseEnrollmentType;
import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import net.sourceforge.fenixedu.domain.degree.enrollment.NotNeedToEnrollInCurricularCourse;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.tools.enrollment.AreaType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * @author David Santos in Jun 24, 2004
 */

public class StudentCurricularPlanLEEC extends StudentCurricularPlanLEEC_Base {

    public StudentCurricularPlanLEEC() {
    	super();
        setOjbConcreteClass(getClass().getName());
    }

    @Override
    public Integer getCreditsInSecundaryArea() {
        return this.creditsInSecundaryArea;
    }

    @Override
    public void setCreditsInSecundaryArea(Integer creditsInSecundaryArea) {
        this.creditsInSecundaryArea = creditsInSecundaryArea;
    }

    @Override
    public Integer getCreditsInSpecializationArea() {
        return this.creditsInSpecializationArea;
    }

    @Override
    public void setCreditsInSpecializationArea(Integer creditsInSpecializationArea) {
        this.creditsInSpecializationArea = creditsInSpecializationArea;
    }
    
    @Override
    public List getAllEnrollments() {
        return super.getEnrolments();
    }
    
    @Override
    public int getNumberOfApprovedCurricularCourses() {
    	int counter = 0;
        List<Enrolment> aprovedEnrolments = getAprovedEnrolments();
    	List<NotNeedToEnrollInCurricularCourse> notNeedToEnroll = getNotNeedToEnrollCurricularCourses();
    	
    	for (NotNeedToEnrollInCurricularCourse notNeedToEnrollInCurricularCourse : notNeedToEnroll) {
			if(notNeedToEnrollInCurricularCourse.getCurricularCourse().getDegreeCurricularPlan().equals(getDegreeCurricularPlan())) {
				counter++;
			}
		}
    	
    	counter += aprovedEnrolments.size();
        return counter;
    }

    @Override
    public boolean areNewAreasCompatible(Branch specializationArea, Branch secundaryArea)
            throws BothAreasAreTheSameServiceException,
            InvalidArgumentsServiceException {
        if (specializationArea == null && secundaryArea == null) {
            return true;
        }
        if (specializationArea == null || secundaryArea == null) {
            throw new InvalidArgumentsServiceException();
        }
        if (specializationArea.equals(secundaryArea)) {
            throw new BothAreasAreTheSameServiceException();
        }
        List<CurricularCourse> curricularCoursesFromGivenAreas = getCurricularCoursesFromGivenAreas(specializationArea,
                secundaryArea);

        List curricularCoursesBelongingToAnySpecializationAndSecundaryArea = getCurricularCoursesBelongingToAnySpecializationAndSecundaryArea();

        List studentsAprovedEnrollments = getStudentEnrollmentsWithApprovedState();

        Iterator iterator = studentsAprovedEnrollments.iterator();
        while (iterator.hasNext()) {
            Enrolment enrolment = (Enrolment) iterator.next();
            if (curricularCoursesBelongingToAnySpecializationAndSecundaryArea.contains(enrolment
                    .getCurricularCourse())
                    && !curricularCoursesFromGivenAreas.contains(enrolment.getCurricularCourse())) {
                return false;
            }
        }

        return true;
    }

    /**
     * @param studentCurricularPlan
     * @return CurricularCoursesBelongingToAnySpecializationAndSecundaryArea
     */
    private List getCurricularCoursesBelongingToAnySpecializationAndSecundaryArea() {
        List<CurricularCourse> curricularCourses = new ArrayList<CurricularCourse>();
        List specializationAreas = getDegreeCurricularPlan().getSpecializationAreas();

        List secundaryAreas = getDegreeCurricularPlan().getSecundaryAreas();

        addAreasCurricularCoursesWithoutRepetitions(curricularCourses, specializationAreas,
                AreaType.SPECIALIZATION);
        addAreasCurricularCoursesWithoutRepetitions(curricularCourses, secundaryAreas,
                AreaType.SECONDARY);

        return curricularCourses;
    }

    /**
     * @param curricularCourses
     * @param specializationAreas
     */
    private void addAreasCurricularCoursesWithoutRepetitions(List<CurricularCourse> curricularCourses, List areas,
            AreaType areaType) {
        Iterator iterator = areas.iterator();
        while (iterator.hasNext()) {
            Branch area = (Branch) iterator.next();
            List groups = area.getAreaCurricularCourseGroups(areaType);
            Iterator iterator2 = groups.iterator();
            while (iterator2.hasNext()) {
                CurricularCourseGroup curricularCourseGroup = (CurricularCourseGroup) iterator2.next();
                Iterator iterator3 = curricularCourseGroup.getCurricularCourses().iterator();
                while (iterator3.hasNext()) {
                    CurricularCourse curricularCourse = (CurricularCourse) iterator3.next();
                    if (!curricularCourses.contains(curricularCourse)) {
                        curricularCourses.add(curricularCourse);
                    }
                }
            }
        }
    }

    /**
     * @param studentCurricularPlan
     * @param specializationArea
     * @param secundaryArea
     * @return CurricularCoursesFromGivenAreas
     */
    private List<CurricularCourse> getCurricularCoursesFromGivenAreas(Branch specializationArea, Branch secundaryArea) {
        List<CurricularCourse> curricularCoursesFromNewSpecializationArea = new ArrayList<CurricularCourse>();
        List<CurricularCourse> curricularCoursesFromNewSecundaryArea = new ArrayList<CurricularCourse>();

        List groups = specializationArea.getAreaCurricularCourseGroups(AreaType.SPECIALIZATION);

        Iterator iterator = groups.iterator();
        while (iterator.hasNext()) {
            CurricularCourseGroup curricularCourseGroup = (CurricularCourseGroup) iterator.next();
            curricularCoursesFromNewSpecializationArea.addAll(curricularCourseGroup
                    .getCurricularCourses());
        }

        groups = secundaryArea.getAreaCurricularCourseGroups(AreaType.SECONDARY);
        iterator = groups.iterator();
        while (iterator.hasNext()) {
            CurricularCourseGroup curricularCourseGroup = (CurricularCourseGroup) iterator.next();
            curricularCoursesFromNewSecundaryArea.addAll(curricularCourseGroup.getCurricularCourses());
        }

        List<CurricularCourse> newCurricularCourses = new ArrayList<CurricularCourse>();
        newCurricularCourses.addAll(curricularCoursesFromNewSpecializationArea);
        newCurricularCourses.addAll(curricularCoursesFromNewSecundaryArea);

        return newCurricularCourses;
    }

    @Override
    public CurricularCourseEnrollmentType getCurricularCourseEnrollmentType(
            CurricularCourse curricularCourse, ExecutionPeriod currentExecutionPeriod) {

        if (!curricularCourse.hasActiveScopeInGivenSemester(currentExecutionPeriod.getSemester())) {
            return CurricularCourseEnrollmentType.NOT_ALLOWED;
        }

        if (isCurricularCourseApproved(curricularCourse)) {
            return CurricularCourseEnrollmentType.NOT_ALLOWED;
        }

        List enrollmentsWithEnrolledStateInCurrentExecutionPeriod = getAllStudentEnrolledEnrollmentsInExecutionPeriod(currentExecutionPeriod);

        for (int i = 0; i < enrollmentsWithEnrolledStateInCurrentExecutionPeriod.size(); i++) {
            Enrolment enrollment = (Enrolment) enrollmentsWithEnrolledStateInCurrentExecutionPeriod
                    .get(i);
            if (curricularCourse.equals(enrollment.getCurricularCourse())) {
                return CurricularCourseEnrollmentType.NOT_ALLOWED;
            }
        }

        List<Enrolment> enrollmentsWithEnrolledStateInPreviousExecutionPeriod = getAllStudentEnrolledEnrollmentsInExecutionPeriod(currentExecutionPeriod
                .getPreviousExecutionPeriod());
      
        for (int i = 0; i < enrollmentsWithEnrolledStateInPreviousExecutionPeriod.size(); i++) {
            Enrolment enrollment = (Enrolment) enrollmentsWithEnrolledStateInPreviousExecutionPeriod
                    .get(i);
            if (curricularCourse.equals(enrollment.getCurricularCourse())) {
                return CurricularCourseEnrollmentType.TEMPORARY;
            }
        }
        
        CurricularCourseEnrollmentType courseEnrollmentType = CurricularCourseEnrollmentType.DEFINITIVE;
        for (CurricularCourseEquivalence curricularCourseEquivalence : curricularCourse.getCurricularCourseEquivalencesSet()) {
	    for (CurricularCourse eqCurricularCourse : curricularCourseEquivalence.getOldCurricularCoursesSet()) {
		if(this.isEquivalentAproved(eqCurricularCourse)) {
		    courseEnrollmentType = courseEnrollmentType.and(CurricularCourseEnrollmentType.DEFINITIVE);
		} else if(hasEnrolledStateInPreviousExecutionPerdiod(eqCurricularCourse, enrollmentsWithEnrolledStateInPreviousExecutionPeriod)) {
		    courseEnrollmentType = courseEnrollmentType.and(CurricularCourseEnrollmentType.TEMPORARY);
		} else {
		    courseEnrollmentType = courseEnrollmentType.and(CurricularCourseEnrollmentType.NOT_ALLOWED);
		}
	    }
	    if(courseEnrollmentType.equals(CurricularCourseEnrollmentType.TEMPORARY)) {
		return CurricularCourseEnrollmentType.TEMPORARY;
	    }
	}


        if (isMathematicalCourse(curricularCourse)) {
            if (hasCurricularCourseEquivalenceIn(curricularCourse,
                    enrollmentsWithEnrolledStateInPreviousExecutionPeriod))
                return CurricularCourseEnrollmentType.TEMPORARY;
        }

        return CurricularCourseEnrollmentType.DEFINITIVE;
    }

    @Override
    public boolean getCanChangeSpecializationArea() {
        return true;
    }

    @Override
    public List<Enrolment> getStudentEnrollmentsWithApprovedState() {
        return (List<Enrolment>) CollectionUtils.select(getAllEnrollments(), new Predicate() {
            public boolean evaluate(Object obj) {
                Enrolment enrollment = (Enrolment) obj;
                return enrollment.isEnrolmentStateApproved()
                && !enrollment.getStudentCurricularPlan().getDegreeCurricularPlan().getName().startsWith("PAST");
            }
        });
    }

	public void delete() throws DomainException {
		removeSecundaryBranch();
		super.delete();
	}

	public int numberCompletedCoursesForSpecifiedDegrees(final Set<Degree> degrees) {
		return getRegistration().countCompletedCoursesForActiveUndergraduateCurricularPlan();
	}

	@Override
	public void addApprovedEnrolments(final Collection<Enrolment> enrolments) {
	    final DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan();
	    final DegreeCurricularPlanState degreeCurricularPlanState = degreeCurricularPlan.getState();
	    if (degreeCurricularPlanState != DegreeCurricularPlanState.PAST) {
		super.addApprovedEnrolments(enrolments);
	    }
	}

}
