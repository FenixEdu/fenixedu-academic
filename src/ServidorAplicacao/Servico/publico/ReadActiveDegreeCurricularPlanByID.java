package ServidorAplicacao.Servico.publico;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import DataBeans.InfoCurricularCourse;
import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.util.Cloner;
import Dominio.DegreeCurricularPlan;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.IDegreeCurricularPlan;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Fernanda Quitério 5/Nov/2003
 *  
 */
public class ReadActiveDegreeCurricularPlanByID implements IServico
{

    private static ReadActiveDegreeCurricularPlanByID service = new ReadActiveDegreeCurricularPlanByID();

    public static ReadActiveDegreeCurricularPlanByID getService()
    {

        return service;
    }

    private ReadActiveDegreeCurricularPlanByID()
    {

    }

    public final String getNome()
    {

        return "ReadActiveDegreeCurricularPlanByID";
    }

    public InfoDegreeCurricularPlan run(Integer degreeCurricularPlanCode) throws FenixServiceException
    {

        InfoDegreeCurricularPlan infoDegreeCurricularPlan = null;
        try
        {
            if (degreeCurricularPlanCode == null)
            {
                throw new FenixServiceException("nullDegree");
            }

            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
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

            List allCurricularCourses =
                sp.getIPersistentCurricularCourse().readCurricularCoursesByDegreeCurricularPlan(
                    degreeCurricularPlan);

            if (allCurricularCourses != null && !allCurricularCourses.isEmpty())
            {

                Iterator iterator = allCurricularCourses.iterator();
                while (iterator.hasNext())
                {
                    ICurricularCourse curricularCourse = (ICurricularCourse) iterator.next();

                    List curricularCourseScopes =
                        sp
                            .getIPersistentCurricularCourseScope()
                            .readActiveCurricularCourseScopesByCurricularCourse(
                            curricularCourse);

                    if (curricularCourseScopes != null)
                    {
                        curricularCourse.setScopes(curricularCourseScopes);
                    }
                }
                infoDegreeCurricularPlan =
                    createInfoDegreeCurricularPlan(degreeCurricularPlan, allCurricularCourses);
            }
        } catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        }
        return infoDegreeCurricularPlan;
    }

    private InfoDegreeCurricularPlan createInfoDegreeCurricularPlan(
        IDegreeCurricularPlan degreeCurricularPlan,
        List allCurricularCourses)
    {
        InfoDegreeCurricularPlan infoDegreeCurricularPlan;
        infoDegreeCurricularPlan =
            Cloner.copyIDegreeCurricularPlan2InfoDegreeCurricularPlan(degreeCurricularPlan);

        CollectionUtils.transform(allCurricularCourses, new Transformer()
        {
            public Object transform(Object arg0)
            {
                ICurricularCourse curricularCourse = (ICurricularCourse) arg0;
                CollectionUtils.transform(curricularCourse.getScopes(), new Transformer()
                {
                    public Object transform(Object arg0)
                    {
                        ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) arg0;
                        return Cloner.copyICurricularCourseScope2InfoCurricularCourseScope(
                            curricularCourseScope);
                    }
                });

                InfoCurricularCourse infoCurricularCourse =
                    Cloner.copyCurricularCourse2InfoCurricularCourse(curricularCourse);
                infoCurricularCourse.setInfoScopes(curricularCourse.getScopes());
                return infoCurricularCourse;
            }
        });

        infoDegreeCurricularPlan.setCurricularCourses(allCurricularCourses);
        return infoDegreeCurricularPlan;
    }
}