package ServidorAplicacao.Servico.commons;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoCurricularCourse;
import DataBeans.InfoCurricularCourseScopeWithBranchAndSemesterAndYear;
import DataBeans.InfoCurricularCourseWithInfoDegree;
import Dominio.ExecutionCourse;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.IExecutionCourse;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 */

public class ReadCurricularCourseScopesByExecutionCourseID implements IService {

    public List run(Integer executionCourseID) throws FenixServiceException {

        List infoCurricularCourses = new ArrayList();
        try {

            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            // Read The ExecutionCourse

            IExecutionCourse executionCourse = (IExecutionCourse) sp.getIPersistentExecutionCourse()
                    .readByOID(ExecutionCourse.class, executionCourseID);

            // For all associated Curricular Courses read the Scopes

            infoCurricularCourses = new ArrayList();
            Iterator iterator = executionCourse.getAssociatedCurricularCourses().iterator();
            while (iterator.hasNext()) {
                ICurricularCourse curricularCourse = (ICurricularCourse) iterator.next();

                List curricularCourseScopes = sp.getIPersistentCurricularCourseScope()
                        .readCurricularCourseScopesByCurricularCourseInExecutionPeriod(curricularCourse,
                                executionCourse.getExecutionPeriod());

                //CLONER
                //InfoCurricularCourse infoCurricularCourse = new
                // InfoCurricularCourse();
                //infoCurricularCourse = Cloner
                //        .copyCurricularCourse2InfoCurricularCourse(curricularCourse);
                InfoCurricularCourse infoCurricularCourse = InfoCurricularCourseWithInfoDegree
                        .newInfoFromDomain(curricularCourse);
                infoCurricularCourse.setInfoScopes(new ArrayList());

                Iterator scopeIterator = curricularCourseScopes.iterator();
                while (scopeIterator.hasNext()) {

                    infoCurricularCourse.getInfoScopes().add(
                            InfoCurricularCourseScopeWithBranchAndSemesterAndYear
                                    .newInfoFromDomain((ICurricularCourseScope) scopeIterator.next()));
                }
                infoCurricularCourses.add(infoCurricularCourse);
            }

        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex);
        }

        return infoCurricularCourses;
    }
}