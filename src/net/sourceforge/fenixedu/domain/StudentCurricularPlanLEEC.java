package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.BothAreasAreTheSameServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseEnrollmentType;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
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

    public Integer getCreditsInSecundaryArea() {
        return this.creditsInSecundaryArea;
    }

    public void setCreditsInSecundaryArea(Integer creditsInSecundaryArea) {
        this.creditsInSecundaryArea = creditsInSecundaryArea;
    }

    public Integer getCreditsInSpecializationArea() {
        return this.creditsInSpecializationArea;
    }

    public void setCreditsInSpecializationArea(Integer creditsInSpecializationArea) {
        this.creditsInSpecializationArea = creditsInSpecializationArea;
    }
    
    public List getAllEnrollments() {
        return super.getEnrolments();
    }
    
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
        List curricularCoursesFromGivenAreas = getCurricularCoursesFromGivenAreas(specializationArea,
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
    protected List getCurricularCoursesBelongingToAnySpecializationAndSecundaryArea() {
        List curricularCourses = new ArrayList();
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
    protected void addAreasCurricularCoursesWithoutRepetitions(List curricularCourses, List areas,
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
    protected List getCurricularCoursesFromGivenAreas(Branch specializationArea, Branch secundaryArea) {
        List curricularCoursesFromNewSpecializationArea = new ArrayList();
        List curricularCoursesFromNewSecundaryArea = new ArrayList();

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

        List newCurricularCourses = new ArrayList();
        newCurricularCourses.addAll(curricularCoursesFromNewSpecializationArea);
        newCurricularCourses.addAll(curricularCoursesFromNewSecundaryArea);

        return newCurricularCourses;
    }

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

        List enrollmentsWithEnrolledStateInPreviousExecutionPeriod = getAllStudentEnrolledEnrollmentsInExecutionPeriod(currentExecutionPeriod
                .getPreviousExecutionPeriod());
      
        for (int i = 0; i < enrollmentsWithEnrolledStateInPreviousExecutionPeriod.size(); i++) {
            Enrolment enrollment = (Enrolment) enrollmentsWithEnrolledStateInPreviousExecutionPeriod
                    .get(i);
            if (curricularCourse.equals(enrollment.getCurricularCourse())) {
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

    /*
     * (non-Javadoc)
     * 
     * @see Dominio.IStudentCurricularPlan#getCanChangeSpecializationArea()
     */
    public boolean getCanChangeSpecializationArea() {
        return true;
    }

    public List getStudentEnrollmentsWithApprovedState() {
        return (List) CollectionUtils.select(getAllEnrollments(), new Predicate() {
            public boolean evaluate(Object obj) {
                Enrolment enrollment = (Enrolment) obj;
                return enrollment.getEnrollmentState().equals(EnrollmentState.APROVED)
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

}