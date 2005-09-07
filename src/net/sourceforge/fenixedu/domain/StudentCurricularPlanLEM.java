package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class StudentCurricularPlanLEM extends StudentCurricularPlanLEM_Base {

    private static final String METROLOGIA_INDUSTRIAL_CODE = "A8Z";

    private static final String PLACAS_E_CASCAS_CODE = "6Q";

    private static final String RAMO_PRODUCAO = "Ramo de Produção";

    private static final String FITH_YEAR_1SEM_OPTIONAL_GROUP = "Opções 5ºAno 1ºSem";

    private static final String FOURTH_YEAR_1SEM_OPTIONAL_GROUP = "Opções 4ºAno 1ºSem";

    public StudentCurricularPlanLEM() {
        setOjbConcreteClass(getClass().getName());
    }

    /**
     * @param curricularCoursesToRemove
     * @param curricularCoursesToKeep
     * @param optionalCurricularCourseGroup
     */
    protected void selectOptionalCoursesToBeRemoved(List curricularCoursesToRemove,
            List curricularCoursesToKeep, ICurricularCourseGroup optionalCurricularCourseGroup, IExecutionPeriod executionPeriod) {
        int count = 0;

        ICurricularCourseGroup ccgProd5Year1Sem = getCurricularCourseGroup(FITH_YEAR_1SEM_OPTIONAL_GROUP);
        ICurricularCourseGroup ccgProd4Year1Sem = getCurricularCourseGroup(FOURTH_YEAR_1SEM_OPTIONAL_GROUP);

        int size2 = optionalCurricularCourseGroup.getCurricularCourses().size();
        for (int j = 0; j < size2; j++) {
            ICurricularCourse curricularCourse = optionalCurricularCourseGroup.getCurricularCourses()
                    .get(j);
            if (isCurricularCourseEnrolledInExecutionPeriod(curricularCourse,executionPeriod)
                    || isCurricularCourseApproved(curricularCourse)) {
                if (getBranch().getName().equalsIgnoreCase(RAMO_PRODUCAO)) {
                    if (curricularCourse.getCode().equals(PLACAS_E_CASCAS_CODE)
                            || curricularCourse.getCode().equals(METROLOGIA_INDUSTRIAL_CODE)) {
                        if (optionalCurricularCourseGroup.getName().equalsIgnoreCase(
                                FITH_YEAR_1SEM_OPTIONAL_GROUP)) {
                            List<ICurricularCourse> curricularCoursesToIgnore = new ArrayList();
                            curricularCoursesToIgnore.add(curricularCourse);
                            int done4year1sem = countDoneOrEnrolledCurricularCoursesExcept(ccgProd4Year1Sem,
                                    curricularCoursesToIgnore);
                            int done5year1sem = countDoneOrEnrolledCurricularCoursesExcept(ccgProd5Year1Sem,
                                    ccgProd4Year1Sem.getCurricularCourses());
                            if (done4year1sem >= 1 && done5year1sem >= 1) {
                                count++;
                            }
                        } else 
                            if (optionalCurricularCourseGroup.getName().equalsIgnoreCase(
                                FOURTH_YEAR_1SEM_OPTIONAL_GROUP)) {
                            int number = countDoneOrEnrolledCurricularCoursesExcept(ccgProd5Year1Sem,
                                    optionalCurricularCourseGroup.getCurricularCourses());
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
    }

    /**
     * @param optionalGroupName
     * @return
     */
    private ICurricularCourseGroup getCurricularCourseGroup(final String optionalGroupName) {
        List optionalCurricularCourseGroups = (List) CollectionUtils.select(getDegreeCurricularPlan()
                .getAllOptionalCurricularCourseGroups(), new Predicate() {

            public boolean evaluate(Object arg0) {
                ICurricularCourseGroup curricularCourseGroup = (ICurricularCourseGroup) arg0;
                return curricularCourseGroup.getName().equalsIgnoreCase(optionalGroupName)
                        && curricularCourseGroup.getBranch().getName().equalsIgnoreCase(RAMO_PRODUCAO);
            }
        });
        return (ICurricularCourseGroup) optionalCurricularCourseGroups.get(0);
    }

    private int countDoneOrEnrolledCurricularCoursesExcept(
            ICurricularCourseGroup optionalCurricularCourseGroup,
            List<ICurricularCourse> curricularCoursesNotToCount) {
        int count = 0;
        int size2 = optionalCurricularCourseGroup.getCurricularCourses().size();
        for (int j = 0; j < size2; j++) {
            ICurricularCourse curricularCourse = optionalCurricularCourseGroup.getCurricularCourses()
                    .get(j);
            if (!curricularCoursesNotToCount.contains(curricularCourse)) {

                if (isCurricularCourseEnrolled(curricularCourse)
                        || isCurricularCourseApproved(curricularCourse)) {
                    count++;
                }
            }
        }
        return count;
    }
}
