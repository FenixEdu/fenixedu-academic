package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class StudentCurricularPlanLEM extends StudentCurricularPlanLEM_Base {

    
	private static final String RAMO_PRODUCAO = "Ramo de Produção";
	
	//1 Semester
	private static final String METROLOGIA_INDUSTRIAL_CODE = "A8Z";

    private static final String PLACAS_E_CASCAS_CODE = "6Q";

    private static final String FITH_YEAR_1SEM_OPTIONAL_GROUP = "Opções 5ºAno 1ºSem";

    private static final String FOURTH_YEAR_1SEM_OPTIONAL_GROUP = "Opções 4ºAno 1ºSem";
    
    //2 Semester
    private static final String FITH_YEAR_2SEM_OPTIONAL_GROUP = "Opções 5ºAno 2ºSem";
    
    private static final String FOURTH_YEAR_2SEM_OPTIONAL_GROUP = "Opções 4ºAno 2ºSem";
    
    private static final String CALCULO_AUTOMATICO_ESTRUTURAS_CODE = "A6U";
    
    private static final String MAQUINAS_FERRAMENTAS_CODE = "A90";

    public StudentCurricularPlanLEM() {
        setOjbConcreteClass(getClass().getName());
    }

    /**
     * @param curricularCoursesToRemove
     * @param curricularCoursesToKeep
     * @param optionalCurricularCourseGroup
     */
    //  FIXME: MÉTODO PARA O 2º SEMSTERE
    protected void selectOptionalCoursesToBeRemoved(List curricularCoursesToRemove,
            List curricularCoursesToKeep, CurricularCourseGroup optionalCurricularCourseGroup, ExecutionPeriod executionPeriod) {
        int count = 0;

        CurricularCourseGroup ccgProd5Year1Sem = getCurricularCourseGroup(FITH_YEAR_2SEM_OPTIONAL_GROUP);
        CurricularCourseGroup ccgProd4Year1Sem = getCurricularCourseGroup(FOURTH_YEAR_2SEM_OPTIONAL_GROUP);

        int size2 = optionalCurricularCourseGroup.getCurricularCourses().size();
        for (int j = 0; j < size2; j++) {
            CurricularCourse curricularCourse = optionalCurricularCourseGroup.getCurricularCourses()
                    .get(j);
            if (isCurricularCourseEnrolledInExecutionPeriod(curricularCourse,executionPeriod)
                    || isCurricularCourseApproved(curricularCourse)) {
                if (getBranch().getName().equalsIgnoreCase(RAMO_PRODUCAO)) {
                    if (curricularCourse.getCode().equals(CALCULO_AUTOMATICO_ESTRUTURAS_CODE)
                            || curricularCourse.getCode().equals(MAQUINAS_FERRAMENTAS_CODE)) {
                        if (optionalCurricularCourseGroup.getName().equalsIgnoreCase(
                                FITH_YEAR_2SEM_OPTIONAL_GROUP)) {
                            List<CurricularCourse> curricularCoursesToIgnore = new ArrayList();
                            curricularCoursesToIgnore.add(curricularCourse);
                            int done4year1sem = countDoneOrEnrolledCurricularCoursesExcept(ccgProd4Year1Sem,
                                    curricularCoursesToIgnore, executionPeriod);
                            if (done4year1sem >= 1) {
                                count++;
                            }
                        } else 
                            if (optionalCurricularCourseGroup.getName().equalsIgnoreCase(
                                FOURTH_YEAR_2SEM_OPTIONAL_GROUP)) {
                            List<CurricularCourse> curricularCoursesToIgnore = new ArrayList();
                            curricularCoursesToIgnore.add(curricularCourse);
                            int number = countDoneOrEnrolledCurricularCoursesExcept(ccgProd5Year1Sem,
                                    curricularCoursesToIgnore, executionPeriod);
                            if (number >= 1) {
                                count++;
                            }
                        }
                    }
                    else {
                        count++;
                    }
                } else {
                    count++;
                }
            }
        }

        if (count >= optionalCurricularCourseGroup.getMaximumNumberOfOptionalCourses().intValue()) {
            curricularCoursesToRemove.addAll(optionalCurricularCourseGroup.getCurricularCourses());
        } else {
            curricularCoursesToKeep.addAll(optionalCurricularCourseGroup.getCurricularCourses());
        }
    }

    
    //FIXME: MÉTODO PARA O 1º SEMSTERE
   /* protected void selectOptionalCoursesToBeRemoved(List curricularCoursesToRemove,
            List curricularCoursesToKeep, CurricularCourseGroup optionalCurricularCourseGroup, ExecutionPeriod executionPeriod) {
        int count = 0;

        CurricularCourseGroup ccgProd5Year1Sem = getCurricularCourseGroup(FITH_YEAR_2SEM_OPTIONAL_GROUP);
        CurricularCourseGroup ccgProd4Year1Sem = getCurricularCourseGroup(FOURTH_YEAR_2SEM_OPTIONAL_GROUP);

        int size2 = optionalCurricularCourseGroup.getCurricularCourses().size();
        for (int j = 0; j < size2; j++) {
            CurricularCourse curricularCourse = optionalCurricularCourseGroup.getCurricularCourses()
                    .get(j);
            if (isCurricularCourseEnrolledInExecutionPeriod(curricularCourse,executionPeriod)
                    || isCurricularCourseApproved(curricularCourse)) {
                if (getBranch().getName().equalsIgnoreCase(RAMO_PRODUCAO)) {
                    if (curricularCourse.getCode().equals(CALCULO_AUTOMATICO_ESTRUTURAS_CODE)
                            || curricularCourse.getCode().equals(MAQUINAS_FERRAMENTAS_CODE)) {
                        if (optionalCurricularCourseGroup.getName().equalsIgnoreCase(
                                FITH_YEAR_2SEM_OPTIONAL_GROUP)) {
                            List<CurricularCourse> curricularCoursesToIgnore = new ArrayList<CurricularCourse>();
                            curricularCoursesToIgnore.add(curricularCourse);
                            int done4year1sem = countDoneOrEnrolledCurricularCoursesExcept(ccgProd4Year1Sem,
                                    curricularCoursesToIgnore, executionPeriod);
                            int done5year1sem = countDoneOrEnrolledCurricularCoursesExcept(ccgProd5Year1Sem,
                                    ccgProd4Year1Sem.getCurricularCourses(), executionPeriod);
                            if (done4year1sem >= 1 && done5year1sem >= 1) {
                                count++;
                            }
                        } else 
                            if (optionalCurricularCourseGroup.getName().equalsIgnoreCase(
                                FOURTH_YEAR_2SEM_OPTIONAL_GROUP)) {
                            int number = countDoneOrEnrolledCurricularCoursesExcept(ccgProd5Year1Sem,
                                    optionalCurricularCourseGroup.getCurricularCourses(), executionPeriod);
                            if (number >= 2) {
                                count++;
                            }
                        }
                    }
                    else {
                        count++;
                    }
                } else {
                    count++;
                }
            }
        }

        if (count >= optionalCurricularCourseGroup.getMaximumNumberOfOptionalCourses().intValue()) {
            curricularCoursesToRemove.addAll(optionalCurricularCourseGroup.getCurricularCourses());
        } else {
            curricularCoursesToKeep.addAll(optionalCurricularCourseGroup.getCurricularCourses());
        }
    }*/

    /**
     * @param optionalGroupName
     * @return
     */
    private CurricularCourseGroup getCurricularCourseGroup(final String optionalGroupName) {
        List optionalCurricularCourseGroups = (List) CollectionUtils.select(getDegreeCurricularPlan()
                .getAllOptionalCurricularCourseGroups(), new Predicate() {

            public boolean evaluate(Object arg0) {
                CurricularCourseGroup curricularCourseGroup = (CurricularCourseGroup) arg0;
                return curricularCourseGroup.getName().equalsIgnoreCase(optionalGroupName)
                        && curricularCourseGroup.getBranch().getName().equalsIgnoreCase(RAMO_PRODUCAO);
            }
        });
        return (CurricularCourseGroup) optionalCurricularCourseGroups.get(0);
    }

    private int countDoneOrEnrolledCurricularCoursesExcept(
            CurricularCourseGroup optionalCurricularCourseGroup,
            List<CurricularCourse> curricularCoursesNotToCount, ExecutionPeriod executionPeriod) {
        int count = 0;
        int size2 = optionalCurricularCourseGroup.getCurricularCourses().size();
        for (int j = 0; j < size2; j++) {
            CurricularCourse curricularCourse = optionalCurricularCourseGroup.getCurricularCourses()
                    .get(j);
            if (!curricularCoursesNotToCount.contains(curricularCourse)) {

                if (isCurricularCourseEnrolledInExecutionPeriod(curricularCourse, executionPeriod)
                        || isCurricularCourseApproved(curricularCourse)) {
                    count++;
                }
            }
        }
        return count;
    }
}
