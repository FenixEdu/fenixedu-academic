package ServidorAplicacao.Servico.publico;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

import DataBeans.InfoCurricularCourse;
import DataBeans.InfoCurricularCourseScope;
import DataBeans.InfoCurricularYear;
import Dominio.DegreeCurricularPlan;
import Dominio.ExecutionPeriod;
import Dominio.IDegreeCurricularPlan;
import Dominio.IExecutionPeriod;
import ServidorAplicacao.Servico.coordinator.ReadDegreeCurricularPlanBaseService;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Fernanda Quitério 5/Nov/2003
 *  
 */
public class ReadActiveDegreeCurricularPlanByID extends ReadDegreeCurricularPlanBaseService
{

    public ReadActiveDegreeCurricularPlanByID()
    {

    }

    public List run(Integer degreeCurricularPlanCode, Integer executionPeriodCode)
        throws FenixServiceException
    {
        if (degreeCurricularPlanCode == null)
        {
            throw new FenixServiceException("nullDegree");
        }

        ISuportePersistente sp;
        try
        {
            sp = SuportePersistenteOJB.getInstance();
        }
        catch (ExcepcaoPersistencia e)
        {
            e.printStackTrace(System.out);
            throw new FenixServiceException("Problems on database!");
        }

        IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan =
            sp.getIPersistentDegreeCurricularPlan();

        IDegreeCurricularPlan degreeCurricularPlan = new DegreeCurricularPlan();
        degreeCurricularPlan.setIdInternal(degreeCurricularPlanCode);
        degreeCurricularPlan =
            (IDegreeCurricularPlan) persistentDegreeCurricularPlan.readByOId(
                degreeCurricularPlan,
                false);
        if (degreeCurricularPlan == null)
        {
            throw new FenixServiceException("nullDegree");
        }

        if (executionPeriodCode == null)
        {
            return groupScopesByCurricularYearAndCurricularCourse(
                super.readActiveCurricularCourseScopes(degreeCurricularPlan, sp));
        }
        else
        {
            IExecutionPeriod executionPeriod = new ExecutionPeriod();
            executionPeriod.setIdInternal(executionPeriodCode);

            IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();
            executionPeriod =
                (IExecutionPeriod) persistentExecutionPeriod.readByOId(executionPeriod, false);
            if (executionPeriod == null || executionPeriod.getExecutionYear() == null)
            {
                throw new FenixServiceException("nullDegree");
            }

            return groupScopesByCurricularYearAndCurricularCourse(
                super.readActiveCurricularCourseScopesInExecutionYear(
                    degreeCurricularPlan,
                    executionPeriod.getExecutionYear(),
                    sp));
        }
    }

    private List groupScopesByCurricularYearAndCurricularCourse(List scopes)
    {
        List result = new ArrayList();
        List temp = new ArrayList();
        ComparatorChain comparatorChain = new ComparatorChain();
        comparatorChain.addComparator(
                new BeanComparator("infoCurricularSemester.infoCurricularYear.year"));
        comparatorChain.addComparator(new BeanComparator("infoCurricularSemester.semester"));
        comparatorChain.addComparator(new BeanComparator("infoCurricularCourse.name"));
        Collections.sort(scopes, comparatorChain);
        Iterator iter = scopes.iterator();
        InfoCurricularYear year = null;
        InfoCurricularCourse curricularCourse = null;
        while (iter.hasNext())
        {
            InfoCurricularCourseScope scope = (InfoCurricularCourseScope) iter.next();
            InfoCurricularYear scopeYear = scope.getInfoCurricularSemester().getInfoCurricularYear();
            InfoCurricularCourse scopeCurricularCourse = scope.getInfoCurricularCourse();
            if (year == null)
            {
                year = scopeYear;
            }
            if (curricularCourse == null)
            {
                curricularCourse = scopeCurricularCourse;
            }
            if (scopeYear.equals(year) && scopeCurricularCourse.equals(curricularCourse))
            {
                temp.add(scope);
            }
            else
            {
                result.add(temp);
                temp = new ArrayList();
                year = scopeYear;
                curricularCourse = scopeCurricularCourse;
                temp.add(scope);
            }

        }

        return result;
    }

}