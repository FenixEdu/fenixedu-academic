package ServidorAplicacao.Servico.masterDegree.administrativeOffice;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoCurricularCourse;
import DataBeans.util.Cloner;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.ICursoExecucao;
import Dominio.IExecutionYear;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Fernanda Quitério 01/07/2003
 *  
 */
public class ReadCurricularCoursesByDegree implements IService {

    public List run(String executionYearString, String degreeName) throws FenixServiceException {
        List infoCurricularCourses = null;
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IExecutionYear executionYear = sp.getIPersistentExecutionYear().readExecutionYearByName(
                    executionYearString);

            // Read degree
            ICursoExecucao cursoExecucao = sp.getIPersistentExecutionDegree()
                    .readByDegreeCurricularPlanNameAndExecutionYear(degreeName, executionYear);

            if (cursoExecucao == null || cursoExecucao.getCurricularPlan() == null
                    || cursoExecucao.getCurricularPlan().getCurricularCourses() == null
                    || cursoExecucao.getCurricularPlan().getCurricularCourses().size() == 0) {
                throw new NonExistingServiceException();
            }

            infoCurricularCourses = new ArrayList();
            ListIterator iterator = cursoExecucao.getCurricularPlan().getCurricularCourses()
                    .listIterator();
            while (iterator.hasNext()) {
                ICurricularCourse curricularCourse = (ICurricularCourse) iterator.next();

                InfoCurricularCourse infoCurricularCourse = Cloner
                        .copyCurricularCourse2InfoCurricularCourse(curricularCourse);
                infoCurricularCourse.setInfoScopes((List) CollectionUtils.collect(curricularCourse
                        .getScopes(), new Transformer() {

                    public Object transform(Object arg0) {
                        ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) arg0;
                        return Cloner
                                .copyICurricularCourseScope2InfoCurricularCourseScope(curricularCourseScope);
                    }
                }));

                infoCurricularCourses.add(infoCurricularCourse);
            }

        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        return infoCurricularCourses;
    }

}