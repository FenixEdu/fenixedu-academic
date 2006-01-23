package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import net.sourceforge.fenixedu.applicationTier.Servico.coordinator.ReadDegreeCurricularPlanBaseService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoBranch;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

/**
 * @author Fernanda Quitério 5/Nov/2003
 * 
 */
public class ReadActiveDegreeCurricularPlanByID extends ReadDegreeCurricularPlanBaseService {

    public List run(Integer degreeCurricularPlanId, Integer executionPeriodId, String language,
            String arg) throws FenixServiceException, ExcepcaoPersistencia {

        if (degreeCurricularPlanId == null) {
            throw new FenixServiceException("null degreeCurricularPlanId");
        }

        if (executionPeriodId == null) {
            return groupScopesByCurricularYearAndCurricularCourseAndBranch(super
                    .readActiveCurricularCourseScopes(degreeCurricularPlanId), language);
        }

        ExecutionPeriod executionPeriod = (ExecutionPeriod) persistentObject.readByOID(
                ExecutionPeriod.class, executionPeriodId);
        if (executionPeriod == null || executionPeriod.getExecutionYear() == null) {
            throw new FenixServiceException("null executionPeriod");
        }

        return groupScopesByCurricularYearAndCurricularCourseAndBranch(super
                .readActiveCurricularCourseScopesInExecutionYear(degreeCurricularPlanId, executionPeriod
                        .getExecutionYear()), language);
    }


    public List run(InfoExecutionDegree infoExecutionDegree, InfoExecutionPeriod infoExecutionPeriod,
            Integer curricularYear, String language) throws FenixServiceException, ExcepcaoPersistencia {
        final ExecutionDegree executionDegree = (ExecutionDegree) persistentObject.readByOID(
                ExecutionDegree.class, infoExecutionDegree.getIdInternal());
        final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
        if (degreeCurricularPlan == null) {
            throw new FenixServiceException("null degreeCurricularPlan");
        }

        if (infoExecutionPeriod == null) {
            return groupScopesByCurricularYearAndCurricularCourseAndBranch(super
                    .readActiveCurricularCourseScopes(degreeCurricularPlan.getIdInternal()), language);
        }
        ExecutionPeriod executionPeriod = (ExecutionPeriod) persistentObject.readByOID(
                ExecutionPeriod.class, infoExecutionPeriod.getIdInternal());
        if (executionPeriod == null || executionPeriod.getExecutionYear() == null) {
            throw new FenixServiceException("nullDegree");
        }

        return groupScopesByCurricularYearAndCurricularCourseAndBranch(super
                .readActiveCurricularCourseScopesInCurricularYearAndExecutionPeriodAndExecutionDegree(
                        executionPeriod, executionDegree, curricularYear), language);
    }

    
    private List groupScopesByCurricularYearAndCurricularCourseAndBranch(List scopes, String language) {
        List result = new ArrayList();
        List temp = new ArrayList();

        ComparatorChain comparatorChain = new ComparatorChain();

        comparatorChain.addComparator(new BeanComparator(
                "infoCurricularSemester.infoCurricularYear.year"));
        comparatorChain.addComparator(new BeanComparator("infoBranch.name"));
        comparatorChain.addComparator(new BeanComparator("infoCurricularSemester.semester"));
        comparatorChain.addComparator(new BeanComparator("infoCurricularCourse.name"));

        List scopesAux = new ArrayList(scopes);
        Collections.sort(scopesAux, comparatorChain);

        if (scopesAux != null && scopesAux.size() > 0) {
            ListIterator iter = scopesAux.listIterator();
            InfoCurricularYear year = null;
            InfoCurricularCourse curricularCourse = null;

            while (iter.hasNext()) {
                InfoCurricularCourseScope scope = (InfoCurricularCourseScope) iter.next();
                InfoCurricularYear scopeYear = scope.getInfoCurricularSemester().getInfoCurricularYear();
                InfoCurricularCourse scopeCurricularCourse = scope.getInfoCurricularCourse();
                InfoBranch infoBranch = scope.getInfoBranch();
                infoBranch.prepareEnglishPresentation(language);
                scope.setInfoBranch(infoBranch);
                scopeCurricularCourse.prepareEnglishPresentation(language);
                if (year == null) {
                    year = scopeYear;
                }
                if (curricularCourse == null) {
                    curricularCourse = scopeCurricularCourse;
                }

                if (scopeYear.equals(year) && scopeCurricularCourse.equals(curricularCourse)) {
                    temp.add(scope);
                } else {
                    result.add(temp);
                    temp = new ArrayList();
                    year = scopeYear;
                    curricularCourse = scopeCurricularCourse;
                    temp.add(scope);
                }

                if (!iter.hasNext()) {
                    result.add(temp);
                }
            }
        }

        return result;
    }

    
    public InfoDegreeCurricularPlan run(Integer degreeCurricularPlanId, Integer executionYear, String arg)
            throws FenixServiceException, ExcepcaoPersistencia {
        final DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) persistentObject
                .readByOID(DegreeCurricularPlan.class, degreeCurricularPlanId);

        InfoDegreeCurricularPlan infoDegreeCurricularPlan = new InfoDegreeCurricularPlan();
        if (degreeCurricularPlan != null) {
            infoDegreeCurricularPlan = InfoDegreeCurricularPlan.newInfoFromDomain(degreeCurricularPlan);
        }
        if (degreeCurricularPlan == null) {
            throw new FenixServiceException("nullDegree");
        }

        return infoDegreeCurricularPlan;
    }

}
