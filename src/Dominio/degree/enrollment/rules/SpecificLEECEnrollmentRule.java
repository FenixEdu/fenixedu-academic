package Dominio.degree.enrollment.rules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import Dominio.CreditsInAnySecundaryArea;
import Dominio.CreditsInScientificArea;
import Dominio.CurricularCourseGroup;
import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseGroup;
import Dominio.IScientificArea;
import Dominio.IStudentCurricularPlan;
import Dominio.ScientificArea;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCreditsInAnySecundaryArea;
import ServidorPersistente.IPersistentCreditsInSpecificScientificArea;
import ServidorPersistente.IPersistentCurricularCourseGroup;
import ServidorPersistente.IPersistentScientificArea;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.AreaType;

public class SpecificLEECEnrollmentRule implements IEnrollmentRule {
    private IStudentCurricularPlan studentCurricularPlan;

    private Integer creditsInSecundaryArea;

    private Integer creditsInSpecializationArea;

    public SpecificLEECEnrollmentRule(
            IStudentCurricularPlan studentCurricularPlan) {
        this.studentCurricularPlan = studentCurricularPlan;
    }

    public List apply(List curricularCoursesToBeEnrolledIn) {
        if ((studentCurricularPlan.getBranch() != null)
                && (studentCurricularPlan.getSecundaryBranch() != null)) {
            try {
                return specificAlgorithm(this.studentCurricularPlan,
                        curricularCoursesToBeEnrolledIn);
            } catch (ExcepcaoPersistencia e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

        return curricularCoursesToBeEnrolledIn;
    }

    public Integer getCreditsInSecundaryArea() {
        return creditsInSecundaryArea;
    }

    public Integer getCreditsInSpecializationArea() {
        return creditsInSpecializationArea;
    }

    private List specificAlgorithm(
            IStudentCurricularPlan studentCurricularPlan,
            List curricularCoursesToBeEnrolledIn) throws ExcepcaoPersistencia {
        HashMap creditsInScientificAreas = new HashMap();
        HashMap creditsInSpecializationAreaGroups = new HashMap();
        HashMap creditsInSecundaryAreaGroups = new HashMap();
        int creditsInAnySecundaryArea = 0;

        getGivenCreditsInScientificArea(studentCurricularPlan,
                creditsInScientificAreas);

        creditsInAnySecundaryArea = getGivenCreditsInAnySecundaryArea(studentCurricularPlan);

        List specializationAreaCurricularCourses = studentCurricularPlan
                .getDegreeCurricularPlan().getCurricularCoursesFromArea(
                        studentCurricularPlan.getBranch(),
                        AreaType.SPECIALIZATION_OBJ);
        List secundaryAreaCurricularCourses = studentCurricularPlan
                .getDegreeCurricularPlan().getCurricularCoursesFromArea(
                        studentCurricularPlan.getSecundaryBranch(),
                        AreaType.SECONDARY_OBJ);

        List allCurricularCourses = new ArrayList();
        allCurricularCourses.addAll(specializationAreaCurricularCourses);
        allCurricularCourses.addAll(secundaryAreaCurricularCourses);

        List specializationAndSecundaryAreaCurricularCoursesToCountForCredits = getSpecializationAndSecundaryAreaCurricularCoursesToCountForCredits(allCurricularCourses);

        calculateGroupsCreditsFromEnrollments(
                studentCurricularPlan,
                specializationAndSecundaryAreaCurricularCoursesToCountForCredits,
                creditsInScientificAreas, creditsInSpecializationAreaGroups,
                creditsInSecundaryAreaGroups);

        calculateGroupsCreditsFromScientificAreas(studentCurricularPlan,
                creditsInScientificAreas, creditsInSpecializationAreaGroups,
                creditsInSecundaryAreaGroups, creditsInAnySecundaryArea);

        this.creditsInSecundaryArea = calculateCreditsInSecundaryArea(
                studentCurricularPlan, creditsInSecundaryAreaGroups,
                creditsInAnySecundaryArea);
        this.creditsInSpecializationArea = calculateCreditsInSpecializationArea(
                studentCurricularPlan, creditsInSpecializationAreaGroups);

        return selectCurricularCourses(
                studentCurricularPlan,
                creditsInSpecializationAreaGroups,
                creditsInSecundaryAreaGroups,
                creditsInAnySecundaryArea,
                specializationAndSecundaryAreaCurricularCoursesToCountForCredits,
                curricularCoursesToBeEnrolledIn);
    }

    private void getGivenCreditsInScientificArea(
            IStudentCurricularPlan studentCurricularPlan,
            HashMap creditsInScientificAreas) throws ExcepcaoPersistencia {
        ISuportePersistente persistentSuport = SuportePersistenteOJB
                .getInstance();
        IPersistentCreditsInSpecificScientificArea creditsInScientificAreaDAO = persistentSuport
                .getIPersistentCreditsInSpecificScientificArea();

        List givenCreditsInScientificAreas = creditsInScientificAreaDAO
                .readAllByStudentCurricularPlan(studentCurricularPlan);

        if (givenCreditsInScientificAreas != null
                && !givenCreditsInScientificAreas.isEmpty()) {
            int size = givenCreditsInScientificAreas.size();
            for (int i = 0; i < size; i++) {
                CreditsInScientificArea creditsInScientificArea = (CreditsInScientificArea) givenCreditsInScientificAreas
                        .get(i);
                sumInHashMap(creditsInScientificAreas, creditsInScientificArea
                        .getScientificArea().getIdInternal(),
                        creditsInScientificArea.getGivenCredits());
            }
        }
    }

    private int getGivenCreditsInAnySecundaryArea(
            IStudentCurricularPlan studentCurricularPlan)
            throws ExcepcaoPersistencia {
        ISuportePersistente persistentSuport = SuportePersistenteOJB
                .getInstance();
        IPersistentCreditsInAnySecundaryArea creditsInAnySecundaryAreaDAO = persistentSuport
                .getIPersistentCreditsInAnySecundaryArea();
        int creditsInAnySecundaryAreas = 0;

        List givenCreditsInAnySecundaryAreas = creditsInAnySecundaryAreaDAO
                .readAllByStudentCurricularPlan(studentCurricularPlan);

        if (givenCreditsInAnySecundaryAreas != null
                && !givenCreditsInAnySecundaryAreas.isEmpty()) {
            int size = givenCreditsInAnySecundaryAreas.size();
            for (int i = 0; i < size; i++) {
                CreditsInAnySecundaryArea creditsInAnySecundaryArea = (CreditsInAnySecundaryArea) givenCreditsInAnySecundaryAreas
                        .get(i);
                creditsInAnySecundaryAreas += creditsInAnySecundaryArea
                        .getGivenCredits().intValue();
            }
        }

        return creditsInAnySecundaryAreas;
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

    private void cleanHashMap(HashMap map, List keysOfItemsToRemove) {
        int size = keysOfItemsToRemove.size();
        for (int i = 0; i < size; i++) {
            Integer key = (Integer) keysOfItemsToRemove.get(i);
            map.remove(key);
        }
    }

    private List getSpecializationAndSecundaryAreaCurricularCoursesToCountForCredits(
            List allCurricularCourses) {
        return (List) CollectionUtils.select(allCurricularCourses,
                new Predicate() {
                    public boolean evaluate(Object obj) {
                        ICurricularCourse curricularCourse = (ICurricularCourse) obj;
                        return (studentCurricularPlan
                                .isCurricularCourseApproved(curricularCourse) || studentCurricularPlan
                                .isCurricularCourseEnrolled(curricularCourse));
                    }
                });
    }

    private void calculateGroupsCreditsFromEnrollments(
            IStudentCurricularPlan studentCurricularPlan,
            List specializationAndSecundaryAreaCurricularCoursesToCountForCredits,
            HashMap creditsInScientificAreas,
            HashMap creditsInSpecializationAreaGroups,
            HashMap creditsInSecundaryAreaGroups) throws ExcepcaoPersistencia {
        int size = specializationAndSecundaryAreaCurricularCoursesToCountForCredits
                .size();
        for (int i = 0; i < size; i++) {
            ICurricularCourse curricularCourse = (ICurricularCourse) specializationAndSecundaryAreaCurricularCoursesToCountForCredits
                    .get(i);

            if (curricularCourseBelongsToAScientificAreaPresentInMoreThanOneBranch(
                    curricularCourse, studentCurricularPlan)) {
                sumCreditsInScientificArea(curricularCourse,
                        creditsInScientificAreas);
            } else {
                sumCreditsInAreasGroups(curricularCourse,
                        studentCurricularPlan, creditsInSecundaryAreaGroups,
                        creditsInSpecializationAreaGroups);
            }
        }
    }

    private boolean curricularCourseBelongsToAScientificAreaPresentInMoreThanOneBranch(
            ICurricularCourse curricularCourse,
            IStudentCurricularPlan studentCurricularPlan)
            throws ExcepcaoPersistencia {
        return (curricularCourseBelongsToSpecializationArea(curricularCourse,
                studentCurricularPlan) && curricularCourseBelongsToSecundaryArea(
                curricularCourse, studentCurricularPlan));
    }

    private void sumCreditsInScientificArea(ICurricularCourse curricularCourse,
            HashMap creditsInScientificAreas) {
        IScientificArea scientificArea = curricularCourse.getScientificArea();
        Integer curricularCourseCredits = new Integer(curricularCourse
                .getCredits().intValue());

        sumInHashMap(creditsInScientificAreas, scientificArea.getIdInternal(),
                curricularCourseCredits);
    }

    private void sumCreditsInAreasGroups(ICurricularCourse curricularCourse,
            IStudentCurricularPlan studentCurricularPlan,
            HashMap creditsInSecundaryAreaGroups,
            HashMap creditsInSpecializationAreaGroups)
            throws ExcepcaoPersistencia {
        ISuportePersistente persistentSuport = SuportePersistenteOJB
                .getInstance();
        IPersistentCurricularCourseGroup curricularCourseGroupDAO = persistentSuport
                .getIPersistentCurricularCourseGroup();

        Integer curricularCourseCredits = new Integer(curricularCourse
                .getCredits().intValue());

        HashMap creditsInAreaGroups = null;
        IBranch branch = null;
        AreaType areaType = null;

        if (curricularCourseBelongsToSpecializationArea(curricularCourse,
                studentCurricularPlan)) {
            // Sum credits in specialization area groups
            creditsInAreaGroups = creditsInSpecializationAreaGroups;
            branch = studentCurricularPlan.getBranch();
            areaType = AreaType.SPECIALIZATION_OBJ;
        } else if (curricularCourseBelongsToSecundaryArea(curricularCourse,
                studentCurricularPlan)) {
            // Sum credits in secundary area groups
            creditsInAreaGroups = creditsInSecundaryAreaGroups;
            branch = studentCurricularPlan.getSecundaryBranch();
            areaType = AreaType.SECONDARY_OBJ;
        }

        if (creditsInAreaGroups != null && branch != null && areaType != null) {
            ICurricularCourseGroup curricularCourseGroup = curricularCourseGroupDAO
                    .readByBranchAndCurricularCourseAndAreaType(branch,
                            curricularCourse, areaType);

            sumInHashMap(creditsInAreaGroups, curricularCourseGroup
                    .getIdInternal(), curricularCourseCredits);
        }
    }

    private boolean curricularCourseBelongsToSpecializationArea(
            ICurricularCourse curricularCourse,
            IStudentCurricularPlan studentCurricularPlan)
            throws ExcepcaoPersistencia {
        ISuportePersistente persistentSuport = SuportePersistenteOJB
                .getInstance();
        IPersistentCurricularCourseGroup curricularCourseGroupDAO = persistentSuport
                .getIPersistentCurricularCourseGroup();

        ICurricularCourseGroup curricularCourseGroup = curricularCourseGroupDAO
                .readByBranchAndCurricularCourseAndAreaType(
                        studentCurricularPlan.getBranch(), curricularCourse,
                        AreaType.SPECIALIZATION_OBJ);

        return curricularCourseGroup != null;
    }

    private boolean curricularCourseBelongsToSecundaryArea(
            ICurricularCourse curricularCourse,
            IStudentCurricularPlan studentCurricularPlan)
            throws ExcepcaoPersistencia {
        ISuportePersistente persistentSuport = SuportePersistenteOJB
                .getInstance();
        IPersistentCurricularCourseGroup curricularCourseGroupDAO = persistentSuport
                .getIPersistentCurricularCourseGroup();

        ICurricularCourseGroup curricularCourseGroup = curricularCourseGroupDAO
                .readByBranchAndCurricularCourseAndAreaType(
                        studentCurricularPlan.getSecundaryBranch(),
                        curricularCourse, AreaType.SECONDARY_OBJ);

        return curricularCourseGroup != null;
    }

    private void calculateGroupsCreditsFromScientificAreas(
            IStudentCurricularPlan studentCurricularPlan,
            HashMap creditsInScientificAreas,
            HashMap creditsInSpecializationAreaGroups,
            HashMap creditsInSecundaryAreaGroups, int creditsInAnySecundaryArea)
            throws ExcepcaoPersistencia {
        HashMap clashingGroups = new HashMap();

        calculateNonClashingScientificAreas(studentCurricularPlan,
                creditsInScientificAreas, creditsInSpecializationAreaGroups,
                creditsInSecundaryAreaGroups, clashingGroups);

        calculateClashingScientificAreas(creditsInScientificAreas,
                creditsInSpecializationAreaGroups,
                creditsInSecundaryAreaGroups, creditsInAnySecundaryArea,
                clashingGroups);
    }

    private void calculateNonClashingScientificAreas(
            IStudentCurricularPlan studentCurricularPlan,
            HashMap creditsInScientificAreas,
            HashMap creditsInSpecializationAreaGroups,
            HashMap creditsInSecundaryAreaGroups, HashMap clashingGroups)
            throws ExcepcaoPersistencia {
        ISuportePersistente persistentSuport = SuportePersistenteOJB
                .getInstance();
        IPersistentScientificArea scientificAreaDAO = persistentSuport
                .getIPersistentScientificArea();
        IPersistentCurricularCourseGroup curricularCourseGroupDAO = persistentSuport
                .getIPersistentCurricularCourseGroup();

        if (!creditsInScientificAreas.entrySet().isEmpty()) {
            List entriesInHashMapToRemove = new ArrayList();

            Iterator iterator = creditsInScientificAreas.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry mapEntry = (Map.Entry) iterator.next();
                Integer credits = (Integer) mapEntry.getValue();
                Integer scientificAreaID = (Integer) mapEntry.getKey();

                IScientificArea scientificArea = (IScientificArea) scientificAreaDAO
                        .readByOID(ScientificArea.class, scientificAreaID);

                ICurricularCourseGroup specializationGroup = curricularCourseGroupDAO
                        .readByBranchAndScientificAreaAndAreaType(
                                studentCurricularPlan.getBranch(),
                                scientificArea, AreaType.SPECIALIZATION_OBJ);

                ICurricularCourseGroup secundaryGroup = curricularCourseGroupDAO
                        .readByBranchAndScientificAreaAndAreaType(
                                studentCurricularPlan.getSecundaryBranch(),
                                scientificArea, AreaType.SECONDARY_OBJ);

                if ((specializationGroup != null) && (secundaryGroup != null)) {
                    List groups = new ArrayList();
                    groups.add(0, specializationGroup);
                    groups.add(1, secundaryGroup);
                    groups.add(2, scientificArea);
                    clashingGroups.put(scientificArea.getIdInternal(), groups);
                } else if (specializationGroup != null) {
                    sumInHashMap(creditsInSpecializationAreaGroups,
                            specializationGroup.getIdInternal(), credits);
                    entriesInHashMapToRemove.add(scientificAreaID);
                } else if (secundaryGroup != null) {
                    sumInHashMap(creditsInSecundaryAreaGroups, secundaryGroup
                            .getIdInternal(), credits);
                    entriesInHashMapToRemove.add(scientificAreaID);
                }
            }

            cleanHashMap(creditsInScientificAreas, entriesInHashMapToRemove);
        }
    }

    private void calculateClashingScientificAreas(
            HashMap creditsInScientificAreas,
            HashMap creditsInSpecializationAreaGroups,
            HashMap creditsInSecundaryAreaGroups,
            int creditsInAnySecundaryArea, HashMap clashingGroups) {
        if (!clashingGroups.entrySet().isEmpty()) {
            Iterator iterator = clashingGroups.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry mapEntry = (Map.Entry) iterator.next();
                List objects = (List) mapEntry.getValue();
                Integer scientificAreaID = (Integer) mapEntry.getKey();

                ICurricularCourseGroup specializationGroup = (ICurricularCourseGroup) objects
                        .get(0);
                ICurricularCourseGroup secundaryGroup = (ICurricularCourseGroup) objects
                        .get(1);

                distributeCreditsInScientificAreaByTheTwoGroups(
                        creditsInSpecializationAreaGroups,
                        creditsInSecundaryAreaGroups, creditsInScientificAreas,
                        creditsInAnySecundaryArea, specializationGroup,
                        secundaryGroup, scientificAreaID);
            }
        }
    }

    private void distributeCreditsInScientificAreaByTheTwoGroups(
            HashMap creditsInSpecializationAreaGroups,
            HashMap creditsInSecundaryAreaGroups,
            HashMap creditsInScientificAreas, int creditsInAnySecundaryArea,
            ICurricularCourseGroup specializationGroup,
            ICurricularCourseGroup secundaryGroup, Integer scientificAreaID) {
        Integer aux = (Integer) creditsInSpecializationAreaGroups
                .get(specializationGroup.getIdInternal());
        Integer bux = (Integer) creditsInSecundaryAreaGroups.get(secundaryGroup
                .getIdInternal());
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
        int creditsInSecundaryGroup = bux.intValue()
                + creditsInAnySecundaryArea;
        int creditsInScientificArea = cux.intValue();
        int maxCreditsInSpecializationGroup = specializationGroup
                .getMaximumCredits().intValue();
        int minCreditsInSpecializationGroup = specializationGroup
                .getMinimumCredits().intValue();
        int maxCreditsInSecundaryGroup = secundaryGroup.getMaximumCredits()
                .intValue();
        int minCreditsInSecundaryGroup = secundaryGroup.getMinimumCredits()
                .intValue();

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
            }
        }

        sumInHashMap(creditsInSpecializationAreaGroups, specializationGroup
                .getIdInternal(), new Integer(String
                .valueOf(creditsInSpecializationGroup - aux.intValue())));

        sumInHashMap(creditsInSecundaryAreaGroups, secundaryGroup
                .getIdInternal(), new Integer(String
                .valueOf(creditsInSecundaryGroup - creditsInAnySecundaryArea
                        - bux.intValue())));
    }

    private Integer calculateCreditsInSecundaryArea(
            IStudentCurricularPlan studentCurricularPlan,
            HashMap creditsInSecundaryAreaGroups, int creditsInAnySecundaryArea)
            throws ExcepcaoPersistencia {
        ISuportePersistente persistentSuport = SuportePersistenteOJB
                .getInstance();
        IPersistentCurricularCourseGroup curricularCourseGroupDAO = persistentSuport
                .getIPersistentCurricularCourseGroup();
        int areaCredits = 0;

        if (!creditsInSecundaryAreaGroups.entrySet().isEmpty()) {
            Iterator iterator = creditsInSecundaryAreaGroups.entrySet()
                    .iterator();
            while (iterator.hasNext()) {
                Map.Entry mapEntry = (Map.Entry) iterator.next();
                Integer credits = (Integer) mapEntry.getValue();
                Integer groupID = (Integer) mapEntry.getKey();

                ICurricularCourseGroup group = (ICurricularCourseGroup) curricularCourseGroupDAO
                        .readByOID(CurricularCourseGroup.class, groupID);

                if (credits.intValue() > group.getMaximumCredits().intValue()) {
                    areaCredits += group.getMaximumCredits().intValue();
                } else {
                    areaCredits += credits.intValue();
                }
            }
        }

        areaCredits += creditsInAnySecundaryArea;
        if (areaCredits >= studentCurricularPlan.getSecundaryBranch()
                .getSecondaryCredits().intValue()) {
            areaCredits = studentCurricularPlan.getSecundaryBranch()
                    .getSecondaryCredits().intValue();
        }

        return new Integer(areaCredits);
    }

    private Integer calculateCreditsInSpecializationArea(
            IStudentCurricularPlan studentCurricularPlan,
            HashMap creditsInSpecializationAreaGroups)
            throws ExcepcaoPersistencia {
        ISuportePersistente persistentSuport = SuportePersistenteOJB
                .getInstance();
        IPersistentCurricularCourseGroup curricularCourseGroupDAO = persistentSuport
                .getIPersistentCurricularCourseGroup();
        int areaCredits = 0;

        if (!creditsInSpecializationAreaGroups.entrySet().isEmpty()) {
            Iterator iterator = creditsInSpecializationAreaGroups.entrySet()
                    .iterator();
            while (iterator.hasNext()) {
                Map.Entry mapEntry = (Map.Entry) iterator.next();
                Integer credits = (Integer) mapEntry.getValue();
                Integer groupID = (Integer) mapEntry.getKey();

                ICurricularCourseGroup group = (ICurricularCourseGroup) curricularCourseGroupDAO
                        .readByOID(CurricularCourseGroup.class, groupID);

                if (credits.intValue() > group.getMaximumCredits().intValue()) {
                    areaCredits += group.getMaximumCredits().intValue();
                } else {
                    areaCredits += credits.intValue();
                }
            }

            if (areaCredits >= studentCurricularPlan.getBranch()
                    .getSpecializationCredits().intValue()) {
                areaCredits = studentCurricularPlan.getBranch()
                        .getSpecializationCredits().intValue();
            }
        }

        return new Integer(areaCredits);
    }

    private List selectCurricularCourses(
            IStudentCurricularPlan studentCurricularPlan,
            HashMap creditsInSpecializationAreaGroups,
            HashMap creditsInSecundaryAreaGroups,
            int creditsInAnySecundaryArea,
            List specializationAndSecundaryAreaCurricularCoursesToCountForCredits,
            List curricularCoursesToBeEnrolledIn) throws ExcepcaoPersistencia {
        boolean isSpecializationAreaDone = isSpecializationAreaDone(
                studentCurricularPlan, creditsInSpecializationAreaGroups);
        boolean isSecundaryAreaDone = isSecundaryAreaDone(
                studentCurricularPlan, creditsInSecundaryAreaGroups,
                creditsInAnySecundaryArea);

        List finalListOfCurricularCourses = null;

        if (isSpecializationAreaDone && isSecundaryAreaDone) {
            finalListOfCurricularCourses = new ArrayList();
        } else if (!isSpecializationAreaDone && isSecundaryAreaDone) {
            finalListOfCurricularCourses = selectSpecializationAreaCurricularCoursesFromGroupsNotFull(
                    studentCurricularPlan, creditsInSpecializationAreaGroups);
        } else if (isSpecializationAreaDone && !isSecundaryAreaDone) {
            finalListOfCurricularCourses = selectSecundaryAreaCurricularCoursesFromGroupsNotFull(
                    studentCurricularPlan, creditsInSecundaryAreaGroups,
                    creditsInAnySecundaryArea);
        } else {
            List specializationAreaCurricularCourses = selectSpecializationAreaCurricularCoursesFromGroupsNotFull(
                    studentCurricularPlan, creditsInSpecializationAreaGroups);

            List secundaryAreaCurricularCourses = selectSecundaryAreaCurricularCoursesFromGroupsNotFull(
                    studentCurricularPlan, creditsInSecundaryAreaGroups,
                    creditsInAnySecundaryArea);

            List disjunction = (List) CollectionUtils.disjunction(
                    specializationAreaCurricularCourses,
                    secundaryAreaCurricularCourses);
            List intersection = (List) CollectionUtils.intersection(
                    specializationAreaCurricularCourses,
                    secundaryAreaCurricularCourses);

            finalListOfCurricularCourses = new ArrayList();
            finalListOfCurricularCourses.addAll(disjunction);
            finalListOfCurricularCourses.addAll(intersection);
        }

        finalListOfCurricularCourses
                .removeAll(specializationAndSecundaryAreaCurricularCoursesToCountForCredits);

        return (List) CollectionUtils.intersection(
                finalListOfCurricularCourses, curricularCoursesToBeEnrolledIn);
    }

    private boolean isSpecializationAreaDone(
            IStudentCurricularPlan studentCurricularPlan,
            HashMap creditsInSpecializationAreaGroups)
            throws ExcepcaoPersistencia {
        ISuportePersistente persistentSuport = SuportePersistenteOJB
                .getInstance();
        IPersistentCurricularCourseGroup curricularCourseGroupDAO = persistentSuport
                .getIPersistentCurricularCourseGroup();

        if (!creditsInSpecializationAreaGroups.entrySet().isEmpty()) {
            int areaCredits = 0;

            Iterator iterator = creditsInSpecializationAreaGroups.entrySet()
                    .iterator();
            while (iterator.hasNext()) {
                Map.Entry mapEntry = (Map.Entry) iterator.next();
                Integer otherCredits = (Integer) mapEntry.getValue();
                Integer groupID = (Integer) mapEntry.getKey();

                ICurricularCourseGroup otherGroup = (ICurricularCourseGroup) curricularCourseGroupDAO
                        .readByOID(CurricularCourseGroup.class, groupID);

                if (otherCredits.intValue() < otherGroup.getMinimumCredits()
                        .intValue()) {
                    return false;
                }
                areaCredits += otherCredits.intValue();

            }

            if (areaCredits >= studentCurricularPlan.getBranch()
                    .getSpecializationCredits().intValue()) {
                return true;
            }
            return false;

        }

        return false;
    }

    private boolean isSecundaryAreaDone(
            IStudentCurricularPlan studentCurricularPlan,
            HashMap creditsInSecundaryAreaGroups, int creditsInAnySecundaryArea)
            throws ExcepcaoPersistencia {
        ISuportePersistente persistentSuport = SuportePersistenteOJB
                .getInstance();
        IPersistentCurricularCourseGroup curricularCourseGroupDAO = persistentSuport
                .getIPersistentCurricularCourseGroup();

        if (!creditsInSecundaryAreaGroups.entrySet().isEmpty()) {
            int areaCredits = 0;

            Iterator iterator = creditsInSecundaryAreaGroups.entrySet()
                    .iterator();
            while (iterator.hasNext()) {
                Map.Entry mapEntry = (Map.Entry) iterator.next();
                Integer otherCredits = (Integer) mapEntry.getValue();
                Integer groupID = (Integer) mapEntry.getKey();

                ICurricularCourseGroup otherGroup = (ICurricularCourseGroup) curricularCourseGroupDAO
                        .readByOID(CurricularCourseGroup.class, groupID);

                if (otherCredits.intValue() >= otherGroup.getMaximumCredits()
                        .intValue()) {
                    return true;
                }
                areaCredits += otherCredits.intValue();

            }

            areaCredits += creditsInAnySecundaryArea;

            if (areaCredits >= studentCurricularPlan.getSecundaryBranch()
                    .getSecondaryCredits().intValue()) {
                return true;
            }
            return false;

        }

        return false;
    }

    private List selectSecundaryAreaCurricularCoursesFromGroupsNotFull(
            IStudentCurricularPlan studentCurricularPlan,
            HashMap creditsInSecundaryAreaGroups, int creditsInAnySecundaryArea)
            throws ExcepcaoPersistencia {
        ISuportePersistente persistentSuport = SuportePersistenteOJB
                .getInstance();
        IPersistentCurricularCourseGroup curricularCourseGroupDAO = persistentSuport
                .getIPersistentCurricularCourseGroup();

        List secundaryAreaCurricularCourses = new ArrayList();

        List groups = curricularCourseGroupDAO.readByBranchAndAreaType(
                studentCurricularPlan.getSecundaryBranch(),
                AreaType.SECONDARY_OBJ);

        int size = groups.size();
        for (int i = 0; i < size; i++) {
            ICurricularCourseGroup group = (ICurricularCourseGroup) groups
                    .get(i);

            if (!isSecundaryGroupDone(studentCurricularPlan, group,
                    creditsInSecundaryAreaGroups, creditsInAnySecundaryArea)) {
                secundaryAreaCurricularCourses.addAll(group
                        .getCurricularCourses());
            }
        }
        return secundaryAreaCurricularCourses;
    }

    private List selectSpecializationAreaCurricularCoursesFromGroupsNotFull(
            IStudentCurricularPlan studentCurricularPlan,
            HashMap creditsInSpecializationAreaGroups)
            throws ExcepcaoPersistencia {
        ISuportePersistente persistentSuport = SuportePersistenteOJB
                .getInstance();
        IPersistentCurricularCourseGroup curricularCourseGroupDAO = persistentSuport
                .getIPersistentCurricularCourseGroup();

        List specializationAreaCurricularCourses = new ArrayList();

        List groups = curricularCourseGroupDAO.readByBranchAndAreaType(
                studentCurricularPlan.getBranch(), AreaType.SPECIALIZATION_OBJ);

        int size = groups.size();
        for (int i = 0; i < size; i++) {
            ICurricularCourseGroup group = (ICurricularCourseGroup) groups
                    .get(i);

            if (!isSpecializationGroupDone(studentCurricularPlan, group,
                    creditsInSpecializationAreaGroups)) {
                specializationAreaCurricularCourses.addAll(group
                        .getCurricularCourses());
            }
        }

        return specializationAreaCurricularCourses;
    }

    private boolean isSpecializationGroupDone(
            IStudentCurricularPlan studentCurricularPlan,
            ICurricularCourseGroup group,
            HashMap creditsInSpecializationAreaGroups)
            throws ExcepcaoPersistencia {
        Integer credits = (Integer) creditsInSpecializationAreaGroups.get(group
                .getIdInternal());

        if (credits != null) {
            if (credits.intValue() >= group.getMaximumCredits().intValue()) {
                return true;
            }

            if (credits.intValue() < group.getMinimumCredits().intValue()) {
                return false;
            }

            return isSpecializationAreaDone(studentCurricularPlan,
                    creditsInSpecializationAreaGroups);
        }
        return false;

    }

    private boolean isSecundaryGroupDone(
            IStudentCurricularPlan studentCurricularPlan,
            ICurricularCourseGroup group, HashMap creditsInSecundaryAreaGroups,
            int creditsInAnySecundaryArea) throws ExcepcaoPersistencia {
        Integer credits = (Integer) creditsInSecundaryAreaGroups.get(group
                .getIdInternal());

        if (credits != null) {
            if (credits.intValue() >= group.getMaximumCredits().intValue()) {
                return true;
            }

            if (credits.intValue() < group.getMinimumCredits().intValue()) {
                return false;
            }

            return isSecundaryAreaDone(studentCurricularPlan,
                    creditsInSecundaryAreaGroups, creditsInAnySecundaryArea);
        }
        return false;

    }

}