package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Factory.TeacherAdministrationSiteComponentBuilder;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteAssociatedCurricularCourses;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon;
import net.sourceforge.fenixedu.dataTransferObject.TeacherAdministrationSiteView;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourseScope;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.ISite;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSite;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author Tânia Pousão
 * @author Ângela
 *  
 */
public class ReadCurricularCourseListByExecutionCourseCode implements IServico {
    private static ReadCurricularCourseListByExecutionCourseCode _servico = new ReadCurricularCourseListByExecutionCourseCode();

    /**
     * The actor of this class.
     */
    private ReadCurricularCourseListByExecutionCourseCode() {

    }

    /**
     * Returns Service Name
     */
    public String getNome() {
        return "ReadCurricularCourseListByExecutionCourseCode";
    }

    /**
     * Returns the _servico.
     * 
     * @return ReadExecutionCourse
     */
    public static ReadCurricularCourseListByExecutionCourseCode getService() {
        return _servico;
    }

    public Object run(Integer executionCourseCode) throws ExcepcaoInexistente, FenixServiceException {

        List infoCurricularCourseList = new ArrayList();
        ISite site = null;
        try {

            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentExecutionCourse executionCourseDAO = sp.getIPersistentExecutionCourse();
            IExecutionCourse executionCourse = (IExecutionCourse) executionCourseDAO.readByOID(
                    ExecutionCourse.class, executionCourseCode);

            if (executionCourse != null && executionCourse.getAssociatedCurricularCourses() != null) {
                for (int i = 0; i < executionCourse.getAssociatedCurricularCourses().size(); i++) {
                    ICurricularCourse curricularCourse = (ICurricularCourse) executionCourse
                            .getAssociatedCurricularCourses().get(i);

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

                    Iterator iterador = infoCurricularCourse.getInfoScopes().listIterator();
                    while (iterador.hasNext()) {
                        InfoCurricularCourseScope infoCurricularCourseScope = (InfoCurricularCourseScope) iterador
                                .next();

                        if (infoCurricularCourseScope.getInfoCurricularSemester().getSemester().equals(
                                executionCourse.getExecutionPeriod().getSemester())) {
                            if (!infoCurricularCourseList.contains(infoCurricularCourse)) {
                                infoCurricularCourseList.add(infoCurricularCourse);
                            }
                        }
                    }
                }
            }

            IPersistentSite persistentSite = sp.getIPersistentSite();
            site = persistentSite.readByExecutionCourse(executionCourse);
        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
            FenixServiceException newEx = new FenixServiceException("");
            newEx.fillInStackTrace();
            throw newEx;
        }

        InfoSiteAssociatedCurricularCourses infoSiteAssociatedCurricularCourses = new InfoSiteAssociatedCurricularCourses();
        infoSiteAssociatedCurricularCourses.setAssociatedCurricularCourses(infoCurricularCourseList);

        TeacherAdministrationSiteComponentBuilder componentBuilder = new TeacherAdministrationSiteComponentBuilder();
        ISiteComponent commonComponent = componentBuilder.getComponent(new InfoSiteCommon(), site, null,
                null, null);

        TeacherAdministrationSiteView siteView = new TeacherAdministrationSiteView(commonComponent,
                infoSiteAssociatedCurricularCourses);
        return siteView;
    }
}