/*
 * Created on Jul 8, 2004
 *
 */
package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.BothAreasAreTheSameServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseEnrollmentType;
import net.sourceforge.fenixedu.util.AreaType;

/**
 * @author Jo�o Mota
 */

public class StudentCurricularPlanLEIC extends StudentCurricularPlanLEIC_Base implements IStudentCurricularPlan {

    protected Integer secundaryBranchKey;

    protected IBranch secundaryBranch;

    protected Integer creditsInSpecializationArea;

    protected Integer creditsInSecundaryArea;

    public StudentCurricularPlanLEIC() {
        setOjbConcreteClass(getClass().getName());
    }

    public IBranch getSecundaryBranch() {
        return secundaryBranch;
    }

    public Integer getSecundaryBranchKey() {
        return secundaryBranchKey;
    }

    public void setSecundaryBranch(IBranch secundaryBranch) {
        this.secundaryBranch = secundaryBranch;
    }

    public void setSecundaryBranchKey(Integer secundaryBranchKey) {
        this.secundaryBranchKey = secundaryBranchKey;
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
        
        List curricularCoursesFromSpecArea = getCurricularCoursesFromArea(specializationArea, AreaType.SPECIALIZATION_OBJ);
        List curricularCoursesFromSecArea = getCurricularCoursesFromArea(secundaryArea, AreaType.SECONDARY_OBJ);

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
        //        List result = (List) CollectionUtils.collect(
        //                enrollmentsWithEnrolledStateInCurrentExecutionPeriod, new Transformer() {
        //                    public Object transform(Object obj) {
        //                        IEnrolment enrollment = (IEnrolment) obj;
        //                        return enrollment.getCurricularCourse();
        //                    }
        //                });
        //        if (result.contains(curricularCourse)) {
        //            return CurricularCourseEnrollmentType.NOT_ALLOWED;
        //        }

        List enrollmentsWithEnrolledStateInPreviousExecutionPeriod = getAllStudentEnrolledEnrollmentsInExecutionPeriod(currentExecutionPeriod
                .getPreviousExecutionPeriod());

        //        List result = (List)
        // CollectionUtils.collect(enrollmentsWithEnrolledStateInPreviousExecutionPeriod,
        //                new Transformer() {
        //                    public Object transform(Object obj) {
        //                        IEnrolment enrollment = (IEnrolment) obj;
        //                        return enrollment.getCurricularCourse();
        //                    }
        //                });
        //
        //        if (result.contains(curricularCourse)) {
        //            return CurricularCourseEnrollmentType.TEMPORARY;
        //        }
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
                AreaType.SPECIALIZATION_OBJ);
        addAreasCurricularCoursesWithoutRepetitions(curricularCourses, secundaryAreas,
                AreaType.SECONDARY_OBJ);

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
}