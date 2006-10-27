package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Factory.TeacherAdministrationSiteComponentBuilder;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteAssociatedCurricularCourses;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon;
import net.sourceforge.fenixedu.dataTransferObject.TeacherAdministrationSiteView;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourseSite;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author Tânia Pousão
 * @author Ângela
 *  
 */
public class ReadCurricularCourseListByExecutionCourseCode extends Service {

    public Object run(Integer executionCourseCode) throws ExcepcaoInexistente, FenixServiceException,
            ExcepcaoPersistencia {

        List infoCurricularCourseList = new ArrayList();
        ExecutionCourseSite site = null;
        ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseCode);

        if (executionCourse != null && executionCourse.getAssociatedCurricularCourses() != null) {
            for (int i = 0; i < executionCourse.getAssociatedCurricularCourses().size(); i++) {
                CurricularCourse curricularCourse = executionCourse
                        .getAssociatedCurricularCourses().get(i);

                InfoCurricularCourse infoCurricularCourse = InfoCurricularCourse.newInfoFromDomain(curricularCourse);
                infoCurricularCourse.setInfoScopes((List) CollectionUtils.collect(curricularCourse
                        .getScopes(), new Transformer() {

                    public Object transform(Object arg0) {
                        CurricularCourseScope curricularCourseScope = (CurricularCourseScope) arg0;
                        return InfoCurricularCourseScope.newInfoFromDomain(curricularCourseScope);
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

        site = executionCourse.getSite();

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