/*
 * Created on Jul 8, 2004
 *
 */
package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.BothAreasAreTheSameServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseEnrollmentType;
import net.sourceforge.fenixedu.domain.degree.enrollment.CurricularCourse2Enroll;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.tools.enrollment.AreaType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * @author Jo�o Mota
 */

public class StudentCurricularPlanLEIC extends StudentCurricularPlanLEIC_Base {

    public StudentCurricularPlanLEIC() {
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

    public boolean getCanChangeSpecializationArea() {
        return true;
    }

    public boolean areNewAreasCompatible(IBranch specializationArea, IBranch secundaryArea)
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
        
        List curricularCoursesFromSpecArea = getCurricularCoursesFromArea(specializationArea, AreaType.SPECIALIZATION);
        List curricularCoursesFromSecArea = getCurricularCoursesFromArea(secundaryArea, AreaType.SECONDARY);

        List curricularCoursesBelongingToAnySpecializationAndSecundaryArea = getCurricularCoursesBelongingToAnySpecializationAndSecundaryArea();

        List studentsAprovedEnrollments = getStudentEnrollmentsWithApprovedState();
        
        studentsAprovedEnrollments.addAll(getStudentEnrollmentsWithEnrolledState());
        
        int specCredits = 0;
        int secCredits = 0;

        Iterator iterator = studentsAprovedEnrollments.iterator();
        while (iterator.hasNext()) {
            IEnrolment enrolment = (IEnrolment) iterator.next();
            if (curricularCoursesBelongingToAnySpecializationAndSecundaryArea.contains(enrolment
                    .getCurricularCourse())){
                if(curricularCoursesFromSpecArea.contains(enrolment.getCurricularCourse())) {
                    specCredits += enrolment.getCurricularCourse().getCredits().intValue();
                }
                else if(curricularCoursesFromSecArea.contains(enrolment.getCurricularCourse())) {
                    secCredits += enrolment.getCurricularCourse().getCredits().intValue();
                }
                else {
                    return false;
                }
                
            }
        }
        return checkCredits(specializationArea.getSpecializationCredits().intValue(), specCredits, secundaryArea.getSecondaryCredits().intValue(), secCredits);
    }
    
 
    public CurricularCourseEnrollmentType getCurricularCourseEnrollmentType(
            ICurricularCourse curricularCourse, IExecutionPeriod currentExecutionPeriod) {

        if (!curricularCourse.hasActiveScopeInGivenSemester(currentExecutionPeriod.getSemester())) {
            return CurricularCourseEnrollmentType.NOT_ALLOWED;
        }

        if (isCurricularCourseApproved(curricularCourse)) {
            return CurricularCourseEnrollmentType.NOT_ALLOWED;
        }

        List enrollmentsWithEnrolledStateInCurrentExecutionPeriod = getAllStudentEnrolledEnrollmentsInExecutionPeriod(currentExecutionPeriod);

        for (int i = 0; i < enrollmentsWithEnrolledStateInCurrentExecutionPeriod.size(); i++) {
            IEnrolment enrollment = (IEnrolment) enrollmentsWithEnrolledStateInCurrentExecutionPeriod
                    .get(i);
            if (curricularCourse.equals(enrollment.getCurricularCourse())) {
                return CurricularCourseEnrollmentType.NOT_ALLOWED;
            }
        }

        List enrollmentsWithEnrolledStateInPreviousExecutionPeriod = getAllStudentEnrolledEnrollmentsInExecutionPeriod(currentExecutionPeriod
                .getPreviousExecutionPeriod());

        for (int i = 0; i < enrollmentsWithEnrolledStateInPreviousExecutionPeriod.size(); i++) {
            IEnrolment enrollment = (IEnrolment) enrollmentsWithEnrolledStateInPreviousExecutionPeriod
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
    
    private boolean checkCredits(int specAreaCredits, int specCredits, int secAreaCredits, int secCredits) {
        if((specCredits > specAreaCredits) || (secCredits > secAreaCredits))
            return false;
        return true;
    }

    protected List getCurricularCoursesFromArea(IBranch specializationArea, AreaType areaType) {
        List curricularCourses = new ArrayList();

        List groups = specializationArea.getAreaCurricularCourseGroups(areaType);

        Iterator iterator = groups.iterator();
        while (iterator.hasNext()) {
            ICurricularCourseGroup curricularCourseGroup = (ICurricularCourseGroup) iterator.next();
            curricularCourses.addAll(curricularCourseGroup
                    .getCurricularCourses());
        }

        return curricularCourses;
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
            IBranch area = (IBranch) iterator.next();
            List groups = area.getAreaCurricularCourseGroups(areaType);
            Iterator iterator2 = groups.iterator();
            while (iterator2.hasNext()) {
                ICurricularCourseGroup curricularCourseGroup = (ICurricularCourseGroup) iterator2.next();
                Iterator iterator3 = curricularCourseGroup.getCurricularCourses().iterator();
                while (iterator3.hasNext()) {
                    ICurricularCourse curricularCourse = (ICurricularCourse) iterator3.next();
                    if (!curricularCourses.contains(curricularCourse)) {
                        curricularCourses.add(curricularCourse);
                    }
                }
            }
        }
    }
    
    
    protected List getCommonBranchAndStudentBranchesCourses(IExecutionPeriod executionPeriod) {

        HashSet curricularCourses = new HashSet();
        IDegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan();
        List commonAreas = degreeCurricularPlan.getCommonAreas();
        int commonAreasSize = commonAreas.size();

        for (int i = 0; i < commonAreasSize; i++) {
            IBranch area = (IBranch) commonAreas.get(i);
            curricularCourses.addAll(degreeCurricularPlan.getCurricularCoursesFromArea(area,
                    AreaType.BASE));
        }

        if (getBranch() != null) {
            curricularCourses.addAll(degreeCurricularPlan.getCurricularCoursesFromArea(getBranch(),
                    AreaType.SPECIALIZATION));
        }

        if (getSecundaryBranch() != null) {
            curricularCourses.addAll(degreeCurricularPlan.getCurricularCoursesFromArea(
                    getSecundaryBranch(), AreaType.SECONDARY));
        }

        curricularCourses.addAll(degreeCurricularPlan.getTFCs());
        
        List<ICurricularCourseGroup> optionalCurricularCourseGroups = degreeCurricularPlan.getAllOptionalCurricularCourseGroups();
        for (ICurricularCourseGroup curricularCourseGroup : optionalCurricularCourseGroups) {
			List<ICurricularCourse> optionalCurricularCourses = curricularCourseGroup.getCurricularCourses();
			curricularCourses.addAll(optionalCurricularCourses);
		}

        List allCurricularCourses = new ArrayList(curricularCourses.size());
        allCurricularCourses.addAll(curricularCourses);

        List result = new ArrayList();
        int curricularCoursesSize = curricularCourses.size();

        for (int i = 0; i < curricularCoursesSize; i++) {
            ICurricularCourse curricularCourse = (ICurricularCourse) allCurricularCourses.get(i);
            result.add(transformToCurricularCourse2Enroll(curricularCourse, executionPeriod));
        }

        List elementsToRemove = (List) CollectionUtils.select(result, new Predicate() {
            public boolean evaluate(Object obj) {
                CurricularCourse2Enroll curricularCourse2Enroll = (CurricularCourse2Enroll) obj;
                return curricularCourse2Enroll.getEnrollmentType().equals(
                        CurricularCourseEnrollmentType.NOT_ALLOWED)
                        || !curricularCourse2Enroll.getCurricularCourse().getEnrollmentAllowed()
                                .booleanValue();
            }
        });

        result.removeAll(elementsToRemove);

        return result;
    }
    
    	
	public void delete() throws DomainException {
		removeSecundaryBranch();
		super.delete();
	}
    
}