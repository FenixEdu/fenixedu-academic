package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourseScope;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;

/**
 * @author tfc130
 */
public class ReadCurricularCourseListOfExecutionCourse implements IServico {

    private static ReadCurricularCourseListOfExecutionCourse _servico = new ReadCurricularCourseListOfExecutionCourse();

    /**
     * The actor of this class.
     */

    private ReadCurricularCourseListOfExecutionCourse() {

    }

    /**
     * Returns Service Name
     */
    public String getNome() {
        return "ReadCurricularCourseListOfExecutionCourse";
    }

    /**
     * Returns the _servico.
     * 
     * @return ReadExecutionCourse
     */
    public static ReadCurricularCourseListOfExecutionCourse getService() {
        return _servico;
    }

    public Object run(InfoExecutionCourse infoExecCourse) throws ExcepcaoInexistente,
            FenixServiceException {

        List infoCurricularCourseList = new ArrayList();
        List infoCurricularCourseScopeList = null;

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentExecutionCourse executionCourseDAO = sp.getIPersistentExecutionCourse();
            IExecutionCourse executionCourse = Cloner
                    .copyInfoExecutionCourse2ExecutionCourse(infoExecCourse);
            executionCourse = executionCourseDAO.readByExecutionCourseInitialsAndExecutionPeriod(
                    executionCourse.getSigla(), executionCourse.getExecutionPeriod());
            if (executionCourse != null && executionCourse.getAssociatedCurricularCourses() != null)
                for (int i = 0; i < executionCourse.getAssociatedCurricularCourses().size(); i++) {
                    ICurricularCourse curricularCourse = (ICurricularCourse) executionCourse
                            .getAssociatedCurricularCourses().get(i);
                    InfoCurricularCourse infoCurricularCourse = Cloner
                            .copyCurricularCourse2InfoCurricularCourse(curricularCourse);
                    infoCurricularCourseScopeList = new ArrayList();
                    for (int j = 0; j < curricularCourse.getScopes().size(); j++) {
                        ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) curricularCourse
                                .getScopes().get(j);
                        InfoCurricularCourseScope infoCurricularCourseScope = Cloner
                                .copyICurricularCourseScope2InfoCurricularCourseScope(curricularCourseScope);
                        infoCurricularCourseScopeList.add(infoCurricularCourseScope);
                    }

                    infoCurricularCourse.setInfoScopes(infoCurricularCourseScopeList);
                    infoCurricularCourseList.add(infoCurricularCourse);

                }
        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
            FenixServiceException newEx = new FenixServiceException("");
            newEx.fillInStackTrace();
            throw newEx;
        }

        return infoCurricularCourseList;
    }

}