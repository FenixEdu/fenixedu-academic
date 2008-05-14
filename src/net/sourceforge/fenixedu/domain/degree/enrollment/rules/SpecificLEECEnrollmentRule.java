package net.sourceforge.fenixedu.domain.degree.enrollment.rules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.CreditsInAnySecundaryArea;
import net.sourceforge.fenixedu.domain.CreditsInScientificArea;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseGroup;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.ScientificArea;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.enrollment.CurricularCourse2Enroll;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.tools.enrollment.AreaType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class SpecificLEECEnrollmentRule extends SpecificEnrolmentRule implements IEnrollmentRule {


    public SpecificLEECEnrollmentRule(StudentCurricularPlan studentCurricularPlan,
            ExecutionSemester executionSemester) {
        this.studentCurricularPlan = studentCurricularPlan;
        this.executionSemester = executionSemester;
    }

    protected List specificAlgorithm(StudentCurricularPlan studentCurricularPlan)
            throws ExcepcaoPersistencia {

        HashMap creditsInScientificAreas = new HashMap();
        HashMap creditsInSpecializationAreaGroups = new HashMap();
        HashMap creditsInSecundaryAreaGroups = new HashMap();
        int creditsInAnySecundaryArea = 0;

        getGivenCreditsInScientificAreas(studentCurricularPlan, creditsInScientificAreas);

        creditsInAnySecundaryArea = getGivenCreditsInAnySecundaryArea(studentCurricularPlan);

        Collection allCurricularCourses = getSpecializationAndSecundaryAreaCurricularCourses(studentCurricularPlan);

        List specializationAndSecundaryAreaCurricularCoursesToCountForCredits = getSpecializationAndSecundaryAreaCurricularCoursesToCountForCredits(allCurricularCourses);

        calculateGroupsCreditsFromEnrollments(studentCurricularPlan,
                specializationAndSecundaryAreaCurricularCoursesToCountForCredits,
                creditsInScientificAreas, creditsInSpecializationAreaGroups,
                creditsInSecundaryAreaGroups);

        calculateGroupsCreditsFromScientificAreas(studentCurricularPlan, creditsInScientificAreas,
                creditsInSpecializationAreaGroups, creditsInSecundaryAreaGroups,
                creditsInAnySecundaryArea);

        this.creditsInSecundaryArea = calculateCreditsInSecundaryArea(studentCurricularPlan,
                creditsInSecundaryAreaGroups, creditsInAnySecundaryArea);
        this.creditsInSpecializationArea = calculateCreditsInSpecializationArea(studentCurricularPlan,
                creditsInSpecializationAreaGroups);

        return selectCurricularCourses(studentCurricularPlan, creditsInSpecializationAreaGroups,
                creditsInSecundaryAreaGroups, creditsInAnySecundaryArea,
                specializationAndSecundaryAreaCurricularCoursesToCountForCredits);
    }

    private void getGivenCreditsInScientificAreas(StudentCurricularPlan studentCurricularPlan,
            HashMap creditsInScientificAreas) throws ExcepcaoPersistencia {

        List givenCreditsInScientificAreas = studentCurricularPlan.getCreditsInScientificAreas();

        if (givenCreditsInScientificAreas != null && !givenCreditsInScientificAreas.isEmpty()) {
            int size = givenCreditsInScientificAreas.size();
            for (int i = 0; i < size; i++) {
                CreditsInScientificArea creditsInScientificArea = (CreditsInScientificArea) givenCreditsInScientificAreas
                        .get(i);
                sumInHashMap(creditsInScientificAreas, creditsInScientificArea.getScientificArea()
                        .getIdInternal(), creditsInScientificArea.getGivenCredits());
            }
        }
    }

    private int getGivenCreditsInAnySecundaryArea(StudentCurricularPlan studentCurricularPlan) throws ExcepcaoPersistencia {
        int creditsInAnySecundaryAreas = 0;

        List<CreditsInAnySecundaryArea> givenCreditsInAnySecundaryAreas = studentCurricularPlan.getCreditsInAnySecundaryAreas();

        if (givenCreditsInAnySecundaryAreas != null && !givenCreditsInAnySecundaryAreas.isEmpty()) {
            int size = givenCreditsInAnySecundaryAreas.size();
            for (int i = 0; i < size; i++) {
                CreditsInAnySecundaryArea creditsInAnySecundaryArea = (CreditsInAnySecundaryArea) givenCreditsInAnySecundaryAreas
                        .get(i);
                creditsInAnySecundaryAreas += creditsInAnySecundaryArea.getGivenCredits().intValue();
            }
        }

        return creditsInAnySecundaryAreas;
    }

    private void calculateGroupsCreditsFromEnrollments(StudentCurricularPlan studentCurricularPlan,
            List specializationAndSecundaryAreaCurricularCoursesToCountForCredits,
            HashMap creditsInScientificAreas, HashMap creditsInSpecializationAreaGroups,
            HashMap creditsInSecundaryAreaGroups) throws ExcepcaoPersistencia {

        int size = specializationAndSecundaryAreaCurricularCoursesToCountForCredits.size();
        for (int i = 0; i < size; i++) {
            CurricularCourse curricularCourse = (CurricularCourse) specializationAndSecundaryAreaCurricularCoursesToCountForCredits
                    .get(i);

            if (curricularCourseBelongsToAScientificAreaPresentInMoreThanOneBranch(curricularCourse,
                    studentCurricularPlan)) {
                sumCreditsInScientificArea(curricularCourse, creditsInScientificAreas);
            } else {
                sumCreditsInAreasGroups(curricularCourse, studentCurricularPlan,
                        creditsInSecundaryAreaGroups, creditsInSpecializationAreaGroups);
            }
        }
    }

    private boolean curricularCourseBelongsToAScientificAreaPresentInMoreThanOneBranch(
            CurricularCourse curricularCourse, StudentCurricularPlan studentCurricularPlan)
            throws ExcepcaoPersistencia {
        return (curricularCourseBelongsToSpecializationArea(curricularCourse, studentCurricularPlan) && curricularCourseBelongsToSecundaryArea(
                curricularCourse, studentCurricularPlan));
    }

    private void sumCreditsInScientificArea(CurricularCourse curricularCourse,
            HashMap creditsInScientificAreas) {

        ScientificArea scientificArea = curricularCourse.getScientificArea();
        Integer curricularCourseCredits = new Integer(curricularCourse.getCredits().intValue());

        sumInHashMap(creditsInScientificAreas, scientificArea.getIdInternal(), curricularCourseCredits);
    }

    private void sumCreditsInAreasGroups(CurricularCourse curricularCourse,
            StudentCurricularPlan studentCurricularPlan, HashMap creditsInSecundaryAreaGroups,
            HashMap creditsInSpecializationAreaGroups) throws ExcepcaoPersistencia {

        Integer curricularCourseCredits = new Integer(curricularCourse.getCredits().intValue());

        HashMap creditsInAreaGroups = null;
        Branch branch = null;
        AreaType areaType = null;

        if (curricularCourseBelongsToSpecializationArea(curricularCourse, studentCurricularPlan)) {
            // Sum credits in specialization area groups
            creditsInAreaGroups = creditsInSpecializationAreaGroups;
            branch = studentCurricularPlan.getBranch();
            areaType = AreaType.SPECIALIZATION;
        } else if (curricularCourseBelongsToSecundaryArea(curricularCourse, studentCurricularPlan)) {
            // Sum credits in secundary area groups
            creditsInAreaGroups = creditsInSecundaryAreaGroups;
            branch = studentCurricularPlan.getSecundaryBranch();
            areaType = AreaType.SECONDARY;
        }

        if (creditsInAreaGroups != null && branch != null && areaType != null) {
        	CurricularCourseGroup curricularCourseGroup = branch.readCurricularCourseGroupByCurricularCourseAndAreaType(curricularCourse, areaType);

            sumInHashMap(creditsInAreaGroups, curricularCourseGroup.getIdInternal(),
                    curricularCourseCredits);
        }
    }

    private boolean curricularCourseBelongsToSpecializationArea(CurricularCourse curricularCourse,
            StudentCurricularPlan studentCurricularPlan) throws ExcepcaoPersistencia {

        CurricularCourseGroup curricularCourseGroup = studentCurricularPlan.getBranch().readCurricularCourseGroupByCurricularCourseAndAreaType(curricularCourse, AreaType.SPECIALIZATION);
        
        return curricularCourseGroup != null;
    }

    private boolean curricularCourseBelongsToSecundaryArea(CurricularCourse curricularCourse,
            StudentCurricularPlan studentCurricularPlan) throws ExcepcaoPersistencia {
        CurricularCourseGroup curricularCourseGroup = studentCurricularPlan.getSecundaryBranch().readCurricularCourseGroupByCurricularCourseAndAreaType(curricularCourse, AreaType.SECONDARY);
        
        return curricularCourseGroup != null;
    }

    private void calculateGroupsCreditsFromScientificAreas(StudentCurricularPlan studentCurricularPlan,
            HashMap creditsInScientificAreas, HashMap creditsInSpecializationAreaGroups,
            HashMap creditsInSecundaryAreaGroups, int creditsInAnySecundaryArea)
            throws ExcepcaoPersistencia {

        HashMap clashingGroups = new HashMap();

        calculateNonClashingScientificAreas(studentCurricularPlan, creditsInScientificAreas,
                creditsInSpecializationAreaGroups, creditsInSecundaryAreaGroups, clashingGroups);

        calculateClashingScientificAreas(creditsInScientificAreas, creditsInSpecializationAreaGroups,
                creditsInSecundaryAreaGroups, creditsInAnySecundaryArea, clashingGroups);
    }

    private void calculateNonClashingScientificAreas(StudentCurricularPlan studentCurricularPlan,
            HashMap creditsInScientificAreas, HashMap creditsInSpecializationAreaGroups,
            HashMap creditsInSecundaryAreaGroups, HashMap clashingGroups) throws ExcepcaoPersistencia {
        
        if (!creditsInScientificAreas.entrySet().isEmpty()) {
            List entriesInHashMapToRemove = new ArrayList();

            Iterator iterator = creditsInScientificAreas.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry mapEntry = (Map.Entry) iterator.next();
                Integer credits = (Integer) mapEntry.getValue();
                Integer scientificAreaID = (Integer) mapEntry.getKey();

                ScientificArea scientificArea = RootDomainObject.getInstance().readScientificAreaByOID(scientificAreaID);

                CurricularCourseGroup specializationGroup = studentCurricularPlan.getBranch().readCurricularCourseGroupByScientificAreaAndAreaType(scientificArea, AreaType.SPECIALIZATION);
                
                CurricularCourseGroup secundaryGroup = studentCurricularPlan.getSecundaryBranch().readCurricularCourseGroupByScientificAreaAndAreaType(scientificArea, AreaType.SECONDARY);

                if ((specializationGroup != null) && (secundaryGroup != null)) {
                    List groups = new ArrayList();
                    groups.add(0, specializationGroup);
                    groups.add(1, secundaryGroup);
                    groups.add(2, scientificArea);
                    clashingGroups.put(scientificArea.getIdInternal(), groups);
                } else if (specializationGroup != null) {
                    sumInHashMap(creditsInSpecializationAreaGroups, specializationGroup.getIdInternal(),
                            credits);
                    entriesInHashMapToRemove.add(scientificAreaID);
                } else if (secundaryGroup != null) {
                    sumInHashMap(creditsInSecundaryAreaGroups, secundaryGroup.getIdInternal(), credits);
                    entriesInHashMapToRemove.add(scientificAreaID);
                }
            }

            cleanHashMap(creditsInScientificAreas, entriesInHashMapToRemove);
        }
    }

    private void calculateClashingScientificAreas(HashMap creditsInScientificAreas,
            HashMap creditsInSpecializationAreaGroups, HashMap creditsInSecundaryAreaGroups,
            int creditsInAnySecundaryArea, HashMap clashingGroups) {
        if (!clashingGroups.entrySet().isEmpty()) {
            Iterator iterator = clashingGroups.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry mapEntry = (Map.Entry) iterator.next();
                List objects = (List) mapEntry.getValue();
                Integer scientificAreaID = (Integer) mapEntry.getKey();

                CurricularCourseGroup specializationGroup = (CurricularCourseGroup) objects.get(0);
                CurricularCourseGroup secundaryGroup = (CurricularCourseGroup) objects.get(1);

                distributeCreditsInScientificAreaByTheTwoGroups(creditsInSpecializationAreaGroups,
                        creditsInSecundaryAreaGroups, creditsInScientificAreas,
                        creditsInAnySecundaryArea, specializationGroup, secundaryGroup, scientificAreaID);
            }
        }
    }

    private void distributeCreditsInScientificAreaByTheTwoGroups(
            HashMap creditsInSpecializationAreaGroups, HashMap creditsInSecundaryAreaGroups,
            HashMap creditsInScientificAreas, int creditsInAnySecundaryArea,
            CurricularCourseGroup specializationGroup, CurricularCourseGroup secundaryGroup,
            Integer scientificAreaID) {
        Integer aux = (Integer) creditsInSpecializationAreaGroups.get(specializationGroup
                .getIdInternal());
        Integer bux = (Integer) creditsInSecundaryAreaGroups.get(secundaryGroup.getIdInternal());
        Integer cux = (Integer) creditsInScientificAreas.get(scientificAreaID);

        if (aux == null) {
            aux = new Integer(0);
        }

        if (bux == null) {
            bux = new Integer(0);
        }

        if (cux == null) {
            cux = new Integer(0);
        }

        int creditsInSpecializationGroup = aux.intValue();
        int creditsInSecundaryGroup = bux.intValue() + creditsInAnySecundaryArea;
        int creditsInScientificArea = cux.intValue();
        int maxCreditsInSpecializationGroup = specializationGroup.getMaximumCredits().intValue();
        int minCreditsInSpecializationGroup = specializationGroup.getMinimumCredits().intValue();
        int maxCreditsInSecundaryGroup = secundaryGroup.getMaximumCredits().intValue();
        int minCreditsInSecundaryGroup = secundaryGroup.getMinimumCredits().intValue();

        while (creditsInScientificArea > 0) {
            if (creditsInSpecializationGroup < (minCreditsInSpecializationGroup - 1)) {
                creditsInSpecializationGroup++;
                creditsInScientificArea--;
            } else if (creditsInSecundaryGroup < (minCreditsInSecundaryGroup - 1)) {
                creditsInSecundaryGroup++;
                creditsInScientificArea--;
            } else if ((creditsInSpecializationGroup == (minCreditsInSpecializationGroup - 1))
                    && (maxCreditsInSpecializationGroup != minCreditsInSpecializationGroup)) {
                creditsInSpecializationGroup++;
                creditsInScientificArea--;
            } else if ((creditsInSecundaryGroup == (minCreditsInSpecializationGroup - 1))
                    && (maxCreditsInSecundaryGroup != minCreditsInSecundaryGroup)) {
                creditsInSecundaryGroup++;
                creditsInScientificArea--;
            } else if (creditsInSpecializationGroup < (maxCreditsInSpecializationGroup - 1)) {
                creditsInSpecializationGroup++;
                creditsInScientificArea--;
            } else if (creditsInSecundaryGroup < (maxCreditsInSecundaryGroup - 1)) {
                creditsInSecundaryGroup++;
                creditsInScientificArea--;
            } else if (creditsInSpecializationGroup == (maxCreditsInSpecializationGroup - 1)) {
                creditsInSpecializationGroup++;
                creditsInScientificArea--;
            } else if (creditsInSecundaryGroup == (maxCreditsInSecundaryGroup - 1)) {
                creditsInSecundaryGroup++;
                creditsInScientificArea--;
            } else {
                creditsInSpecializationGroup += creditsInScientificArea;
                creditsInScientificArea = 0;
            }
        }

        sumInHashMap(creditsInSpecializationAreaGroups, specializationGroup.getIdInternal(),
                new Integer(String.valueOf(creditsInSpecializationGroup - aux.intValue())));

        sumInHashMap(creditsInSecundaryAreaGroups, secundaryGroup.getIdInternal(), new Integer(String
                .valueOf(creditsInSecundaryGroup - creditsInAnySecundaryArea - bux.intValue())));
    }

    private Integer calculateCreditsInSecundaryArea(StudentCurricularPlan studentCurricularPlan,
            HashMap creditsInSecundaryAreaGroups, int creditsInAnySecundaryArea)
            throws ExcepcaoPersistencia {
        int areaCredits = 0;

        if (!creditsInSecundaryAreaGroups.entrySet().isEmpty()) {
            Iterator iterator = creditsInSecundaryAreaGroups.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry mapEntry = (Map.Entry) iterator.next();
                Integer credits = (Integer) mapEntry.getValue();
                Integer groupID = (Integer) mapEntry.getKey();

                CurricularCourseGroup group = RootDomainObject.getInstance().readCurricularCourseGroupByOID(groupID);

                if (credits.intValue() > group.getMaximumCredits().intValue()) {
                    areaCredits += group.getMaximumCredits().intValue();
                } else {
                    areaCredits += credits.intValue();
                }
            }
        }

        areaCredits += creditsInAnySecundaryArea;
        if (areaCredits >= studentCurricularPlan.getSecundaryBranch().getSecondaryCredits().intValue()) {
            areaCredits = studentCurricularPlan.getSecundaryBranch().getSecondaryCredits().intValue();
        }

        return new Integer(areaCredits);
    }

    private Integer calculateCreditsInSpecializationArea(StudentCurricularPlan studentCurricularPlan,
            HashMap creditsInSpecializationAreaGroups) throws ExcepcaoPersistencia {
        int areaCredits = 0;

        if (!creditsInSpecializationAreaGroups.entrySet().isEmpty()) {
            Iterator iterator = creditsInSpecializationAreaGroups.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry mapEntry = (Map.Entry) iterator.next();
                Integer credits = (Integer) mapEntry.getValue();
                Integer groupID = (Integer) mapEntry.getKey();

                CurricularCourseGroup group = RootDomainObject.getInstance().readCurricularCourseGroupByOID(groupID);

                if (credits.intValue() > group.getMaximumCredits().intValue()) {
                    areaCredits += group.getMaximumCredits().intValue();
                } else {
                    areaCredits += credits.intValue();
                }
            }

            if (areaCredits >= studentCurricularPlan.getBranch().getSpecializationCredits().intValue()) {
                areaCredits = studentCurricularPlan.getBranch().getSpecializationCredits().intValue();
            }
        }

        return new Integer(areaCredits);
    }

    private List selectCurricularCourses(StudentCurricularPlan studentCurricularPlan,
            HashMap creditsInSpecializationAreaGroups, HashMap creditsInSecundaryAreaGroups,
            int creditsInAnySecundaryArea,
            List specializationAndSecundaryAreaCurricularCoursesToCountForCredits)
            throws ExcepcaoPersistencia {

        boolean isSpecializationAreaDone = isSpecializationAreaDone(studentCurricularPlan,
                creditsInSpecializationAreaGroups);
        boolean isSecundaryAreaDone = isSecundaryAreaDone(studentCurricularPlan,
                creditsInSecundaryAreaGroups, creditsInAnySecundaryArea);

        List finalListOfCurricularCourses = null;

        if (isSpecializationAreaDone && isSecundaryAreaDone) {
            finalListOfCurricularCourses = new ArrayList();
        } else if (!isSpecializationAreaDone && isSecundaryAreaDone) {
            finalListOfCurricularCourses = selectSpecializationAreaCurricularCoursesFromGroupsNotFull(
                    studentCurricularPlan, creditsInSpecializationAreaGroups);
        } else if (isSpecializationAreaDone && !isSecundaryAreaDone) {
            finalListOfCurricularCourses = selectSecundaryAreaCurricularCoursesFromGroupsNotFull(
                    studentCurricularPlan, creditsInSecundaryAreaGroups, creditsInAnySecundaryArea);
        } else {
            List specializationAreaCurricularCourses = selectSpecializationAreaCurricularCoursesFromGroupsNotFull(
                    studentCurricularPlan, creditsInSpecializationAreaGroups);

            List secundaryAreaCurricularCourses = selectSecundaryAreaCurricularCoursesFromGroupsNotFull(
                    studentCurricularPlan, creditsInSecundaryAreaGroups, creditsInAnySecundaryArea);

            List disjunction = (List) CollectionUtils.disjunction(specializationAreaCurricularCourses,
                    secundaryAreaCurricularCourses);
            List intersection = (List) CollectionUtils.intersection(specializationAreaCurricularCourses,
                    secundaryAreaCurricularCourses);

            finalListOfCurricularCourses = new ArrayList();
            finalListOfCurricularCourses.addAll(disjunction);
            finalListOfCurricularCourses.addAll(intersection);
        }

        finalListOfCurricularCourses
                .removeAll(specializationAndSecundaryAreaCurricularCoursesToCountForCredits);

        return finalListOfCurricularCourses;
    }

    private boolean isSpecializationAreaDone(StudentCurricularPlan studentCurricularPlan,
            HashMap creditsInSpecializationAreaGroups) throws ExcepcaoPersistencia {

        if (!creditsInSpecializationAreaGroups.entrySet().isEmpty()) {
            int areaCredits = 0;

            Iterator iterator = creditsInSpecializationAreaGroups.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry mapEntry = (Map.Entry) iterator.next();
                Integer otherCredits = (Integer) mapEntry.getValue();
                Integer groupID = (Integer) mapEntry.getKey();

                CurricularCourseGroup otherGroup = RootDomainObject.getInstance().readCurricularCourseGroupByOID(groupID);

                if (otherCredits.intValue() < otherGroup.getMinimumCredits().intValue()) {
                    return false;
                }
                areaCredits += otherCredits.intValue();

            }

            if (areaCredits >= studentCurricularPlan.getBranch().getSpecializationCredits().intValue()) {
                return true;
            }
            return false;

        }

        return false;
    }

    private boolean isSecundaryAreaDone(StudentCurricularPlan studentCurricularPlan,
            HashMap creditsInSecundaryAreaGroups, int creditsInAnySecundaryArea)
            throws ExcepcaoPersistencia {

        if (!creditsInSecundaryAreaGroups.entrySet().isEmpty()) {
            int areaCredits = 0;

            Iterator iterator = creditsInSecundaryAreaGroups.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry mapEntry = (Map.Entry) iterator.next();
                Integer otherCredits = (Integer) mapEntry.getValue();
                Integer groupID = (Integer) mapEntry.getKey();

                CurricularCourseGroup otherGroup = RootDomainObject.getInstance().readCurricularCourseGroupByOID(groupID);
                
                if (otherCredits.intValue() >= otherGroup.getMaximumCredits().intValue()) {
                    return true;
                }
                areaCredits += otherCredits.intValue();

            }

            areaCredits += creditsInAnySecundaryArea;

            if (areaCredits >= studentCurricularPlan.getSecundaryBranch().getSecondaryCredits()
                    .intValue()) {
                return true;
            }
            return false;

        }

        return false;
    }

    private List selectSecundaryAreaCurricularCoursesFromGroupsNotFull(
            StudentCurricularPlan studentCurricularPlan, HashMap creditsInSecundaryAreaGroups,
            int creditsInAnySecundaryArea) throws ExcepcaoPersistencia {
        
        List<CurricularCourse> secundaryAreaCurricularCourses = new ArrayList<CurricularCourse>();

        List<CurricularCourseGroup> groups = studentCurricularPlan.getSecundaryBranch().readCurricularCourseGroupsByAreaType(AreaType.SECONDARY);

        int size = groups.size();
        for (int i = 0; i < size; i++) {
            CurricularCourseGroup group = (CurricularCourseGroup) groups.get(i);

            if (!isSecundaryGroupDone(studentCurricularPlan, group, creditsInSecundaryAreaGroups,
                    creditsInAnySecundaryArea)) {
                secundaryAreaCurricularCourses.addAll(group.getCurricularCourses());
            }
        }
        return secundaryAreaCurricularCourses;
    }

    private List selectSpecializationAreaCurricularCoursesFromGroupsNotFull(
            StudentCurricularPlan studentCurricularPlan, HashMap creditsInSpecializationAreaGroups)
            throws ExcepcaoPersistencia {
        List<CurricularCourse> specializationAreaCurricularCourses = new ArrayList<CurricularCourse>();

        List<CurricularCourseGroup> groups = studentCurricularPlan.getBranch().readCurricularCourseGroupsByAreaType(AreaType.SPECIALIZATION); 

        int size = groups.size();
        for (int i = 0; i < size; i++) {
            CurricularCourseGroup group = (CurricularCourseGroup) groups.get(i);

            if (!isSpecializationGroupDone(studentCurricularPlan, group,
                    creditsInSpecializationAreaGroups)) {
                specializationAreaCurricularCourses.addAll(group.getCurricularCourses());
            }
        }

        return specializationAreaCurricularCourses;
    }

    private boolean isSpecializationGroupDone(StudentCurricularPlan studentCurricularPlan,
            CurricularCourseGroup group, HashMap creditsInSpecializationAreaGroups)
            throws ExcepcaoPersistencia {
        Integer credits = (Integer) creditsInSpecializationAreaGroups.get(group.getIdInternal());

        if (credits != null) {
            if (credits.intValue() >= group.getMaximumCredits().intValue()) {
                return true;
            }

            if (credits.intValue() < group.getMinimumCredits().intValue()) {
                return false;
            }

            return isSpecializationAreaDone(studentCurricularPlan, creditsInSpecializationAreaGroups);
        }
        return false;

    }

    private boolean isSecundaryGroupDone(StudentCurricularPlan studentCurricularPlan,
            CurricularCourseGroup group, HashMap creditsInSecundaryAreaGroups,
            int creditsInAnySecundaryArea) throws ExcepcaoPersistencia {
        Integer credits = (Integer) creditsInSecundaryAreaGroups.get(group.getIdInternal());

        if (credits != null) {
            if (credits.intValue() >= group.getMaximumCredits().intValue()) {
                return true;
            }

            if (credits.intValue() < group.getMinimumCredits().intValue()) {
                return false;
            }

            return isSecundaryAreaDone(studentCurricularPlan, creditsInSecundaryAreaGroups,
                    creditsInAnySecundaryArea);
        }
        return false;

    }

    protected List filter(StudentCurricularPlan studentCurricularPlan, ExecutionSemester executionSemester,
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
                                || SpecificLEECEnrollmentRule.this.studentCurricularPlan
                                        .getDegreeCurricularPlan().getTFCs().contains(
                                                curricularCourse2Enroll.getCurricularCourse());
                    }
                });

        List specializationAreaCurricularCourses = getSpecializationAreaCurricularCourses(studentCurricularPlan);
        List secundaryAreaCurricularCourses = getSecundaryAreaCurricularCourses(studentCurricularPlan);

        if (thereIsAnyTemporaryCurricularCourse(studentCurricularPlan,
                executionSemester, specializationAreaCurricularCourses)) {
            markCurricularCourses(curricularCoursesFromOtherAreasToMantain,
                    specializationAreaCurricularCourses);
        }

        if (thereIsAnyTemporaryCurricularCourse(studentCurricularPlan, executionSemester,
                secundaryAreaCurricularCourses)) {
            markCurricularCourses(curricularCoursesFromOtherAreasToMantain,
                    secundaryAreaCurricularCourses);
        }

        result.addAll(curricularCoursesFromOtherAreasToMantain);

        return result;
    }

}