/*
 * Created on Jan 17, 2005
 */
package Dominio.degree.enrollment.rules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.IDegreeCurricularPlan;
import Dominio.IEnrollment;
import Dominio.IExecutionPeriod;
import Dominio.IStudentCurricularPlan;
import Dominio.degree.enrollment.CurricularCourse2Enroll;
import ServidorPersistente.ExcepcaoPersistencia;
import Util.AreaType;
import Util.enrollment.CurricularCourseEnrollmentType;

/**
 * @author nmgo
 */
public class SpecificLEICEnrollmentRule implements IEnrollmentRule {

    private IStudentCurricularPlan studentCurricularPlan;

    private IExecutionPeriod executionPeriod;

    private Integer creditsInSecundaryArea;

    private Integer creditsInSpecializationArea;

    public SpecificLEICEnrollmentRule(IStudentCurricularPlan studentCurricularPlan,
            IExecutionPeriod executionPeriod) {
        this.studentCurricularPlan = studentCurricularPlan;
        this.executionPeriod = executionPeriod;
    }

    public List apply(List curricularCoursesToBeEnrolledIn) {

        if ((this.studentCurricularPlan.getBranch() != null)
                && (this.studentCurricularPlan.getSecundaryBranch() != null)) {
            try {
                List result = specificAlgorithm(this.studentCurricularPlan);
                studentCurricularPlan.setCreditsInSpecializationArea(this
                        .getCreditsInSpecializationArea());
                studentCurricularPlan.setCreditsInSecundaryArea(this.getCreditsInSecundaryArea());
                return filter(this.studentCurricularPlan, this.executionPeriod,
                        curricularCoursesToBeEnrolledIn, result);
            } catch (ExcepcaoPersistencia e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

        return curricularCoursesToBeEnrolledIn;
    }

    private List specificAlgorithm(IStudentCurricularPlan studentCurricularPlan)
            throws ExcepcaoPersistencia {

        HashMap creditsInScientificAreas = new HashMap();
        HashMap creditsInSpecializationAreaGroups = new HashMap();
        HashMap creditsInSecundaryAreaGroups = new HashMap();
        int creditsInAnySecundaryArea = 0;

        Collection allCurricularCourses = getSpecializationAndSecundaryAreaCurricularCourses(studentCurricularPlan);

        List specializationAndSecundaryAreaCurricularCoursesToCountForCredits = getSpecializationAndSecundaryAreaCurricularCoursesToCountForCredits(allCurricularCourses);

        calculateGroupsCreditsFromEnrollments(studentCurricularPlan,
                specializationAndSecundaryAreaCurricularCoursesToCountForCredits,
                creditsInScientificAreas, creditsInSpecializationAreaGroups,
                creditsInSecundaryAreaGroups);

        this.creditsInSecundaryArea = calculateCredits(studentCurricularPlan.getSecundaryBranch().getSecondaryCredits(), creditsInSecundaryAreaGroups);
        this.creditsInSpecializationArea = calculateCredits(studentCurricularPlan.getBranch().getSpecializationCredits(),
                creditsInSpecializationAreaGroups);

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
    private Integer calculateCredits(Integer maxCredits, HashMap creditsAreaGroups) {
        
        int credits = 0;
        for(Iterator iter = creditsAreaGroups.values().iterator(); iter.hasNext(); ) {
            Integer value = (Integer) iter.next();
            credits += value.intValue();
        }
        
        if (credits >= maxCredits.intValue()) {
            credits = maxCredits.intValue();
        }
        
        return new Integer(credits);
    }

    /**
     * @param curricularCoursesToBeEnrolledIn
     * @param curricularCoursesFromAreasNotClosed
     * @return
     */
    private List filter(List curricularCoursesToBeEnrolledIn,
            final Collection curricularCoursesFromAreasNotClosed) {
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
                        return curricularCoursesFromAreasNotClosed.contains(curricularCourse2Enroll
                                .getCurricularCourse());
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

        return result;
    }
    
    private List filter(IStudentCurricularPlan studentCurricularPlan, IExecutionPeriod executionPeriod,
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

        List curricularCoursesFromOtherAreasToMantain = (List) CollectionUtils.select(curricularCoursesToBeEnrolledIn,
                new Predicate() {
                    public boolean evaluate(Object obj) {
                        CurricularCourse2Enroll curricularCourse2Enroll = (CurricularCourse2Enroll) obj;
                        return selectedCurricularCoursesFromSpecializationAndSecundaryAreas.contains(curricularCourse2Enroll
                                .getCurricularCourse())
                                || SpecificLEICEnrollmentRule.this.studentCurricularPlan.getDegreeCurricularPlan().getTFCs()
                                        .contains(curricularCourse2Enroll.getCurricularCourse());
                    }
                });

        List specializationAreaCurricularCourses = getSpecializationAreaCurricularCourses(studentCurricularPlan);
        List secundaryAreaCurricularCourses = getSecundaryAreaCurricularCourses(studentCurricularPlan);

        if (thereIsAnyTemporaryCurricularCourse(studentCurricularPlan,
                executionPeriod, specializationAreaCurricularCourses)) {
            markCurricularCourses(curricularCoursesFromOtherAreasToMantain,
                    specializationAreaCurricularCourses);
        }

        if (thereIsAnyTemporaryCurricularCourse(studentCurricularPlan, executionPeriod,
                secundaryAreaCurricularCourses)) {
            markCurricularCourses(curricularCoursesFromOtherAreasToMantain,
                    secundaryAreaCurricularCourses);
        }

        result.addAll(curricularCoursesFromOtherAreasToMantain);

        return result;
    }

    /**
     * @return
     */
/*    private Collection specificAlgorithm() {

        Double specializationAreaCredits = null;
        Double secundaryAreaCredits = null;

        List specializationAreaCurricularCourses = getSpecializationAreaCurricularCourses(studentCurricularPlan);
        List secundaryAreaCurricularCourses = getSecundaryAreaCurricularCourses(studentCurricularPlan);

        Collection specCurricularCoursesToCountForCredits = new ArrayList();
        Collection secCurricularCoursesToCountForCredits = new ArrayList();

        calculateCredits(specializationAreaCredits, secundaryAreaCredits,
                specCurricularCoursesToCountForCredits, secCurricularCoursesToCountForCredits,
                specializationAreaCurricularCourses, secundaryAreaCurricularCourses);

        isSpecificAreaDone = isSpecAreaDone(specializationAreaCredits, studentCurricularPlan.getBranch());

        isSecundaryAreaDone = isSecAreaDone(secundaryAreaCredits, studentCurricularPlan
                .getSecundaryBranch());

        return selectCurricularCourses(specCurricularCoursesToCountForCredits,
                secCurricularCoursesToCountForCredits, specializationAreaCurricularCourses,
                secundaryAreaCurricularCourses);
    }*/

    /**
     * @param secCurricularCoursesToCountForCredits
     * @param specCurricularCoursesToCountForCredits
     * @param secundaryAreaCurricularCourses
     * @param specializationAreaCurricularCourses
     * @return
     */
    /*private Collection selectCurricularCourses(Collection specCurricularCoursesToCountForCredits,
            Collection secCurricularCoursesToCountForCredits,
            Collection specializationAreaCurricularCourses, Collection secundaryAreaCurricularCourses) {
        Collection finalListOfCurricularCourses = new HashSet();

        if (!isSpecificAreaDone) {
            finalListOfCurricularCourses.addAll(specializationAreaCurricularCourses);
            finalListOfCurricularCourses.removeAll(specCurricularCoursesToCountForCredits);
        }

        if (!isSecundaryAreaDone) {
            secundaryAreaCurricularCourses.removeAll(secCurricularCoursesToCountForCredits);
            finalListOfCurricularCourses.addAll(secundaryAreaCurricularCourses);
        }

        return finalListOfCurricularCourses;
    }*/

    /**
     * @param secundaryAreaCredits
     * @param secundaryBranch
     * @return
     */
    /*private boolean isSecAreaDone(Double secundaryAreaCredits, IBranch branch) {
        int credits = branch.getSecondaryCredits().intValue();

        if (secundaryAreaCredits.intValue() >= credits) {
            return true;
        }
        return false;
    }*/

    /**
     * @param specializationAreaCredits
     * @param branch
     * @return
     */
    /*private boolean isSpecAreaDone(Double specializationAreaCredits, IBranch branch) {
        int credits = branch.getSpecializationCredits().intValue();

        if (specializationAreaCredits.intValue() >= credits) {
            return true;
        }
        return false;
    }*/

    /**
     * List
     * 
     * @param specializationAreaCredits
     * @param secundaryAreaCredits
     * @param secCurricularCoursesToCountForCredits
     * @param specCurricularCoursesToCountForCredits
     * @param secundaryAreaCurricularCourses
     * @param specializationAreaCurricularCourses
     */
    /*private void calculateCredits(Double specializationAreaCredits, Double secundaryAreaCredits,
            Collection specCurricularCoursesToCountForCredits,
            Collection secCurricularCoursesToCountForCredits,
            Collection specializationAreaCurricularCourses, Collection secundaryAreaCurricularCourses) {

        List enrolments = studentCurricularPlan.getEnrolments();

        double specCredits = 0;
        double secCredits = 0;

        for (Iterator iter = enrolments.iterator(); iter.hasNext();) {
            IEnrollment enrollment = (IEnrollment) iter.next();
            if (studentCurricularPlan.isCurricularCourseApproved(enrollment.getCurricularCourse())
                    || studentCurricularPlan.isCurricularCourseEnrolledInExecutionPeriod(enrollment
                            .getCurricularCourse(), executionPeriod)) {
                if (specializationAreaCurricularCourses.contains(enrollment.getCurricularCourse())) {
                    specCredits += enrollment.getCurricularCourse().getCredits().doubleValue();
                    specCurricularCoursesToCountForCredits.add(enrollment.getCurricularCourse());
                } else if (secundaryAreaCurricularCourses.contains(enrollment.getCurricularCourse())) {
                    secCredits += enrollment.getCurricularCourse().getCredits().doubleValue();
                    secCurricularCoursesToCountForCredits.add(enrollment.getCurricularCourse());
                }
            }
        }
        specializationAreaCredits = new Double(specCredits);
        secundaryAreaCredits = new Double(secCredits);
    }*/

    /**
     * @param specializationAreaCurricularCoursesToCountForCredits
     * @param secundaryAreaCurricularCoursesToCountForCredits
     */
    /*private void calculateCurricularCoursesToCountForCredits(
            Collection specializationAreaCurricularCoursesToCountForCredits,
            Collection secundaryAreaCurricularCoursesToCountForCredits) {
        List enrolments = studentCurricularPlan.getEnrolments();
        List specializationAreaCurricularCourses = getSpecializationAreaCurricularCourses(studentCurricularPlan);
        List secundaryAreaCurricularCourses = getSecundaryAreaCurricularCourses(studentCurricularPlan);

        for (Iterator iter = enrolments.iterator(); iter.hasNext();) {
            IEnrollment enrollment = (IEnrollment) iter.next();
            if (studentCurricularPlan.isCurricularCourseApproved(enrollment.getCurricularCourse())
                    || studentCurricularPlan.isCurricularCourseEnrolledInExecutionPeriod(enrollment
                            .getCurricularCourse(), executionPeriod)) {
                if (specializationAreaCurricularCourses.contains(enrollment.getCurricularCourse())) {
                    specializationAreaCurricularCoursesToCountForCredits.add(enrollment
                            .getCurricularCourse());
                } else if (secundaryAreaCurricularCourses.contains(enrollment.getCurricularCourse())) {
                    secundaryAreaCurricularCoursesToCountForCredits
                            .add(enrollment.getCurricularCourse());
                }
            }

        }

    }*/
    /**
     * @return
     */
 /*   private Collection getCurricularCoursesFromSpecializationArea() {
        List allCurricularCoursesFromSpecializationArea = getSpecializationAreaCurricularCourses(studentCurricularPlan);

        return null;
    }*/

    private List getSpecializationAreaCurricularCourses(IStudentCurricularPlan studentCurricularPlan) {

        return studentCurricularPlan.getDegreeCurricularPlan().getCurricularCoursesFromArea(
                studentCurricularPlan.getBranch(), AreaType.SPECIALIZATION_OBJ);
    }

    private List getSecundaryAreaCurricularCourses(IStudentCurricularPlan studentCurricularPlan) {

        return studentCurricularPlan.getDegreeCurricularPlan().getCurricularCoursesFromArea(
                studentCurricularPlan.getSecundaryBranch(), AreaType.SECONDARY_OBJ);
    }

    private List getCommonAreasCurricularCourses(IStudentCurricularPlan studentCurricularPlan) {

        IDegreeCurricularPlan degreeCurricularPlan = studentCurricularPlan.getDegreeCurricularPlan();

        List curricularCoursesFromCommonAreas = new ArrayList();
        List commonAreas = degreeCurricularPlan.getCommonAreas();
        int commonAreasSize = commonAreas.size();
        for (int i = 0; i < commonAreasSize; i++) {
            IBranch area = (IBranch) commonAreas.get(i);
            curricularCoursesFromCommonAreas.addAll(degreeCurricularPlan.getCurricularCoursesFromArea(
                    area, AreaType.BASE_OBJ));
        }

        return curricularCoursesFromCommonAreas;
    }



    private boolean thereIsAnyTemporaryCurricularCourse(IStudentCurricularPlan studentCurricularPlan,
            IExecutionPeriod executionPeriod, final List areaCurricularCourses) {

        List enrolledEnrollments = studentCurricularPlan
                .getAllStudentEnrolledEnrollmentsInExecutionPeriod(executionPeriod
                        .getPreviousExecutionPeriod());

        List result = (List) CollectionUtils.select(enrolledEnrollments, new Predicate() {
            public boolean evaluate(Object obj) {
                IEnrollment enrollment = (IEnrollment) obj;
                return areaCurricularCourses.contains(enrollment.getCurricularCourse());
            }
        });

        return !result.isEmpty();
    }

    private void markCurricularCourses(List curricularCoursesToBeEnrolledIn,
            List curricularCoursesFromOneArea) {

        int size = curricularCoursesToBeEnrolledIn.size();
        for (int i = 0; i < size; i++) {
            CurricularCourse2Enroll curricularCourse2Enroll = (CurricularCourse2Enroll) curricularCoursesToBeEnrolledIn
                    .get(i);
            if (curricularCoursesFromOneArea.contains(curricularCourse2Enroll.getCurricularCourse())) {
                curricularCourse2Enroll.setEnrollmentType(CurricularCourseEnrollmentType.TEMPORARY);
            }
        }
    }
    
    
    private Collection getSpecializationAndSecundaryAreaCurricularCourses(
            IStudentCurricularPlan studentCurricularPlan) {

        List specializationAreaCurricularCourses = getSpecializationAreaCurricularCourses(studentCurricularPlan);
        List secundaryAreaCurricularCourses = getSecundaryAreaCurricularCourses(studentCurricularPlan);

        Set set = new HashSet();
        set.addAll(specializationAreaCurricularCourses);
        set.addAll(secundaryAreaCurricularCourses);

        return set;
    }
    
    private List getSpecializationAndSecundaryAreaCurricularCoursesToCountForCredits(
            Collection allCurricularCourses) {

        return (List) CollectionUtils.select(allCurricularCourses, new Predicate() {
            public boolean evaluate(Object obj) {
                ICurricularCourse curricularCourse = (ICurricularCourse) obj;
                return (studentCurricularPlan.isCurricularCourseApproved(curricularCourse) || studentCurricularPlan
                        .isCurricularCourseEnrolledInExecutionPeriod(curricularCourse, executionPeriod));
            }
        });
    }
    
    private void calculateGroupsCreditsFromEnrollments(IStudentCurricularPlan studentCurricularPlan,
            List specializationAndSecundaryAreaCurricularCoursesToCountForCredits,
            HashMap creditsInScientificAreas, HashMap creditsInSpecializationAreaGroups,
            HashMap creditsInSecundaryAreaGroups) throws ExcepcaoPersistencia {
        
        List specCurricularCourses = getSpecializationAreaCurricularCourses(studentCurricularPlan);
        List secCurricularCourses = getSecundaryAreaCurricularCourses(studentCurricularPlan);

        int size = specializationAndSecundaryAreaCurricularCoursesToCountForCredits.size();
        for (int i = 0; i < size; i++) {
            ICurricularCourse curricularCourse = (ICurricularCourse) specializationAndSecundaryAreaCurricularCoursesToCountForCredits
                    .get(i);
            
            Integer curricularCourseCredits = new Integer(curricularCourse.getCredits().intValue());

            if(specCurricularCourses.contains(curricularCourse)) {
                sumInHashMap(creditsInSpecializationAreaGroups, studentCurricularPlan.getBranch().getIdInternal(), curricularCourseCredits);
            } else if(secCurricularCourses.contains(curricularCourse)) {
                sumInHashMap(creditsInSecundaryAreaGroups, studentCurricularPlan.getSecundaryBranch().getIdInternal(), curricularCourseCredits);
            }            
        }
    }
    
    private List selectCurricularCourses(IStudentCurricularPlan studentCurricularPlan,
            HashMap creditsInSpecializationAreaGroups, HashMap creditsInSecundaryAreaGroups,
            int creditsInAnySecundaryArea,
            List specializationAndSecundaryAreaCurricularCoursesToCountForCredits)
            throws ExcepcaoPersistencia {

        boolean isSpecializationAreaDone = isAreaDone(studentCurricularPlan.getBranch().getSpecializationCredits(),
                creditsInSpecializationArea);
        boolean isSecundaryAreaDone = isAreaDone(studentCurricularPlan.getSecundaryBranch().getSecondaryCredits(),
                creditsInSecundaryArea);

        List finalListOfCurricularCourses = new ArrayList();

        if (!isSpecializationAreaDone) {
            finalListOfCurricularCourses.addAll(getSpecializationAreaCurricularCourses(studentCurricularPlan));
        }

        if (!isSecundaryAreaDone) {
            finalListOfCurricularCourses.addAll(getSecundaryAreaCurricularCourses(studentCurricularPlan));
        }

        finalListOfCurricularCourses
                .removeAll(specializationAndSecundaryAreaCurricularCoursesToCountForCredits);

        return finalListOfCurricularCourses;
    }

    
    private boolean isAreaDone(Integer maxCredits, Integer credits) {
        if(credits.intValue() >= maxCredits.intValue())
            return true;
        return false;
    }

    private void sumInHashMap(HashMap map, Integer key, Integer value) {
        if (!map.containsKey(key)) {
            map.put(key, value);
        } else {
            Integer oldValue = (Integer) map.get(key);
            int result = oldValue.intValue() + value.intValue();
            map.put(key, new Integer(result));
        }
    }

    /**
     * @return Returns the creditsInSecundaryArea.
     */
    public Integer getCreditsInSecundaryArea() {
        return creditsInSecundaryArea;
    }
    /**
     * @param creditsInSecundaryArea The creditsInSecundaryArea to set.
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
     * @param creditsInSpecializationArea The creditsInSpecializationArea to set.
     */
    public void setCreditsInSpecializationArea(Integer creditsInSpecializationArea) {
        this.creditsInSpecializationArea = creditsInSpecializationArea;
    }
}
