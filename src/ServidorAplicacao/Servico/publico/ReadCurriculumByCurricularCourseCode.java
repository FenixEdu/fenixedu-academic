package ServidorAplicacao.Servico.publico;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import DataBeans.InfoCurricularCourseScope;
import DataBeans.InfoCurriculum;
import DataBeans.InfoExecutionCourse;
import DataBeans.util.Cloner;
import Dominio.CurricularCourse;
import Dominio.ExecutionPeriod;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.ICurriculum;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentCurricularCourseScope;
import ServidorPersistente.IPersistentCurriculum;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Fernanda Quitério 13/Nov/2003
 */
public class ReadCurriculumByCurricularCourseCode implements IServico
{

    private static ReadCurriculumByCurricularCourseCode service =
        new ReadCurriculumByCurricularCourseCode();

    public static ReadCurriculumByCurricularCourseCode getService()
    {

        return service;
    }

    private ReadCurriculumByCurricularCourseCode()
    {

    }

    public final String getNome()
    {

        return "ReadCurriculumByCurricularCourseCode";
    }

    public InfoCurriculum run(Integer curricularCourseCode, Integer executionPeriodId)
        throws FenixServiceException
    {

        InfoCurriculum infoCurriculum = null;
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();
            IPersistentCurriculum persistentCurriculum = sp.getIPersistentCurriculum();
            IPersistentCurricularCourseScope persistentCurricularCourseScope =
                sp.getIPersistentCurricularCourseScope();
            IDisciplinaExecucaoPersistente persistentExecutionCourse =
                sp.getIDisciplinaExecucaoPersistente();
            IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();

            if (curricularCourseCode == null)
            {
                throw new FenixServiceException("nullCurricularCourse");
            }

            ICurricularCourse curricularCourse = new CurricularCourse();
            curricularCourse.setIdInternal(curricularCourseCode);

            curricularCourse =
                (ICurricularCourse) persistentCurricularCourse.readByOId(curricularCourse, false);
            if (curricularCourse == null)
            {
                throw new NonExistingServiceException();
            }

            ICurriculum curriculum =
                persistentCurriculum.readCurriculumByCurricularCourse(curricularCourse);
            if (curriculum != null)
            {
                //selects active curricular course scopes
                List activeCurricularCourseScopes =
                    persistentCurricularCourseScope.readActiveCurricularCourseScopesByCurricularCourse(
                        curricularCourse);

                activeCurricularCourseScopes =
                    (List) CollectionUtils.select(activeCurricularCourseScopes, new Predicate()
                {
                    public boolean evaluate(Object arg0)
                    {
                        ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) arg0;
                        if (curricularCourseScope.isActive().booleanValue())
                        {
                            return true;
                        }
                        return false;
                    }
                });
                curriculum.getCurricularCourse().setScopes(activeCurricularCourseScopes);

                //selects execution courses for current execution period
                IExecutionPeriod executionPeriod = null;
                if (executionPeriodId == null)
                {
                    executionPeriod = persistentExecutionPeriod.readActualExecutionPeriod();
                } else
                {
                    executionPeriod = new ExecutionPeriod();
                    executionPeriod.setIdInternal(executionPeriodId);

                    executionPeriod =
                        (IExecutionPeriod) persistentExecutionPeriod.readByOId(executionPeriod, false);
                }

                List associatedExecutionCourses =
                    persistentExecutionCourse.readListbyCurricularCourseAndExecutionPeriod(
                        curricularCourse,
                        executionPeriod);
                curriculum.getCurricularCourse().setAssociatedExecutionCourses(
                    associatedExecutionCourses);

                infoCurriculum = createInfoCurriculum(curriculum, persistentExecutionCourse);
            }
        } catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        }
        return infoCurriculum;
    }

    private InfoCurriculum createInfoCurriculum(
        ICurriculum curriculum,
        IDisciplinaExecucaoPersistente persistentExecutionCourse)
        throws ExcepcaoPersistencia
    {
        InfoCurriculum infoCurriculum;
        infoCurriculum = Cloner.copyICurriculum2InfoCurriculum(curriculum);

        List scopes = curriculum.getCurricularCourse().getScopes();
        CollectionUtils.transform(scopes, new Transformer()
        {
            public Object transform(Object arg0)
            {
                ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) arg0;
                InfoCurricularCourseScope infoCurricularCourseScope =
                    Cloner.copyICurricularCourseScope2InfoCurricularCourseScope(curricularCourseScope);
                return infoCurricularCourseScope;
            }
        });
        infoCurriculum.getInfoCurricularCourse().setInfoScopes(scopes);

        List infoExecutionCourses = new ArrayList();
        List executionCourses = curriculum.getCurricularCourse().getAssociatedExecutionCourses();
        Iterator iterExecutionCourses = executionCourses.iterator();
        while (iterExecutionCourses.hasNext())
        {
            IDisciplinaExecucao executionCourse = (IDisciplinaExecucao) iterExecutionCourses.next();
            InfoExecutionCourse infoExecutionCourse =
                Cloner.copyIExecutionCourse2InfoExecutionCourse(executionCourse);
            infoExecutionCourse.setHasSite(
                persistentExecutionCourse.readSite(executionCourse.getIdInternal()));
            infoExecutionCourses.add(infoExecutionCourse);
        }
        infoCurriculum.getInfoCurricularCourse().setInfoAssociatedExecutionCourses(infoExecutionCourses);
        return infoCurriculum;
    }
}