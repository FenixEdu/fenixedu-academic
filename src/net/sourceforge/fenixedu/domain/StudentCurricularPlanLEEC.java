package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.BothAreasAreTheSameServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.util.AreaType;
import net.sourceforge.fenixedu.util.EnrollmentState;
import net.sourceforge.fenixedu.util.enrollment.CurricularCourseEnrollmentType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * @author David Santos in Jun 24, 2004
 */

public class StudentCurricularPlanLEEC extends StudentCurricularPlan implements IStudentCurricularPlan {
    protected Integer secundaryBranchKey;

    protected IBranch secundaryBranch;

    protected Integer creditsInSpecializationArea;

    protected Integer creditsInSecundaryArea;

    public StudentCurricularPlanLEEC() {
        ojbConcreteClass = getClass().getName();
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

    public List getAllEnrollments() {
        return super.getEnrolments();
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
        List curricularCoursesFromGivenAreas = getCurricularCoursesFromGivenAreas(specializationArea,
                secundaryArea);

        List curricularCoursesBelongingToAnySpecializationAndSecundaryArea = getCurricularCoursesBelongingToAnySpecializationAndSecundaryArea();

        List studentsAprovedEnrollments = getStudentEnrollmentsWithApprovedState();

        Iterator iterator = studentsAprovedEnrollments.iterator();
        while (iterator.hasNext()) {
            IEnrollment enrolment = (IEnrollment) iterator.next();
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

    /**
     * @param studentCurricularPlan
     * @param specializationArea
     * @param secundaryArea
     * @return CurricularCoursesFromGivenAreas
     */
    protected List getCurricularCoursesFromGivenAreas(IBranch specializationArea, IBranch secundaryArea) {
        List curricularCoursesFromNewSpecializationArea = new ArrayList();
        List curricularCoursesFromNewSecundaryArea = new ArrayList();

        List groups = specializationArea.getAreaCurricularCourseGroups(AreaType.SPECIALIZATION_OBJ);

        Iterator iterator = groups.iterator();
        while (iterator.hasNext()) {
            ICurricularCourseGroup curricularCourseGroup = (ICurricularCourseGroup) iterator.next();
            curricularCoursesFromNewSpecializationArea.addAll(curricularCourseGroup
                    .getCurricularCourses());
        }

        groups = secundaryArea.getAreaCurricularCourseGroups(AreaType.SECONDARY_OBJ);
        iterator = groups.iterator();
        while (iterator.hasNext()) {
            ICurricularCourseGroup curricularCourseGroup = (ICurricularCourseGroup) iterator.next();
            curricularCoursesFromNewSecundaryArea.addAll(curricularCourseGroup.getCurricularCourses());
        }

        List newCurricularCourses = new ArrayList();
        newCurricularCourses.addAll(curricularCoursesFromNewSpecializationArea);
        newCurricularCourses.addAll(curricularCoursesFromNewSecundaryArea);

        return newCurricularCourses;
    }
    
    public boolean isCurricularCourseApproved(ICurricularCourse curricularCourse) {

        final List studentApprovedEnrollments = getStudentEnrollmentsWithApprovedState();

        final List result = new ArrayList(studentApprovedEnrollments.size());
        for (int i = 0; i < studentApprovedEnrollments.size(); i++) {
            final IEnrollment enrollment = (IEnrollment) studentApprovedEnrollments.get(i);
            final ICurricularCourse curricularCourseFromEnrollment = enrollment.getCurricularCourse();
            if (curricularCourse.getCurricularCourseUniqueKeyForEnrollment().equals(
                    curricularCourseFromEnrollment.getCurricularCourseUniqueKeyForEnrollment())) {
                return true;
            }
            result.add(curricularCourseFromEnrollment);
        }
//        List result = (List) CollectionUtils.collect(studentApprovedEnrollments, new Transformer() {
//            public Object transform(Object obj) {
//                IEnrollment enrollment = (IEnrollment) obj;
//                return enrollment.getCurricularCourse();
//            }
//        });
//
//        if (isThisCurricularCoursesInTheList(curricularCourse, result)) {
//            return true;
//        }

        int size = result.size();
        for (int i = 0; i < size; i++) {
            ICurricularCourse curricularCourseDoneByStudent = (ICurricularCourse) result.get(i);
            List curricularCourseEquivalences = getCurricularCoursesInCurricularCourseEquivalences(curricularCourseDoneByStudent);
            if (isThisCurricularCoursesInTheList(curricularCourse, curricularCourseEquivalences)) {
                return true;
            }
        }
        
        return false;
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
            IEnrollment enrollment = (IEnrollment) enrollmentsWithEnrolledStateInCurrentExecutionPeriod
                    .get(i);
            if (curricularCourse.equals(enrollment.getCurricularCourse())) {
                return CurricularCourseEnrollmentType.NOT_ALLOWED;
            }
        }
        //        List result = (List) CollectionUtils.collect(
        //                enrollmentsWithEnrolledStateInCurrentExecutionPeriod, new Transformer() {
        //                    public Object transform(Object obj) {
        //                        IEnrollment enrollment = (IEnrollment) obj;
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
        //                        IEnrollment enrollment = (IEnrollment) obj;
        //                        return enrollment.getCurricularCourse();
        //                    }
        //                });
        //
        //        if (result.contains(curricularCourse)) {
        //            return CurricularCourseEnrollmentType.TEMPORARY;
        //        }
        for (int i = 0; i < enrollmentsWithEnrolledStateInPreviousExecutionPeriod.size(); i++) {
            IEnrollment enrollment = (IEnrollment) enrollmentsWithEnrolledStateInPreviousExecutionPeriod
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

    /**
     * @return Returns the creditsInSecundaryArea.
     */
    public Integer getCreditsInSecundaryArea() {
        return creditsInSecundaryArea;
    }

    /**
     * @param creditsInSecundaryArea
     *            The creditsInSecundaryArea to set.
     */
    public void setCreditsInSecundaryArea(Integer creditsInSecundaryArea) {
        this.creditsInSecundaryArea = creditsInSecundaryArea;
    }

    /**
     * @return Returns the creditsInSpecializationArea.
     */
    public Integer getCreditsInSpecializationArea() {
        return creditsInSpecializationArea;
    }

    /**
     * @param creditsInSpecializationArea
     *            The creditsInSpecializationArea to set.
     */
    public void setCreditsInSpecializationArea(Integer creditsInSpecializationArea) {
        this.creditsInSpecializationArea = creditsInSpecializationArea;
    }

    protected List getStudentEnrollmentsWithApprovedState() {
        return (List) CollectionUtils.select(getAllEnrollments(), new Predicate() {
            public boolean evaluate(Object obj) {
                IEnrollment enrollment = (IEnrollment) obj;
                return enrollment.getEnrollmentState().equals(EnrollmentState.APROVED)
                && !enrollment.getStudentCurricularPlan().getDegreeCurricularPlan().getName().startsWith("PAST");
            }
        });
    }

}