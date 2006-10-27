package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Factory.TeacherAdministrationSiteComponentBuilder;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudents;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.TeacherAdministrationSiteView;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourseSite;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author Fernanda Quitério
 * @author Tânia Pousão
 * @author Ângela
 *  
 */
public class ReadStudentsByCurricularCourse extends Service {

    public Object run(Integer executionCourseCode, Integer courseCode) throws ExcepcaoInexistente,
            FenixServiceException, ExcepcaoPersistencia {

        List infoStudentList = null;
        CurricularCourse curricularCourse = null;

    	final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID( executionCourseCode);
        final ExecutionCourseSite site = executionCourse.getSite();

        if (courseCode == null) {
            infoStudentList = getAllAttendingStudents(site);
        } else {
            curricularCourse = (CurricularCourse) rootDomainObject.readDegreeModuleByOID(courseCode);

            infoStudentList = getCurricularCourseStudents(curricularCourse);

        }

        TeacherAdministrationSiteView siteView = createSiteView(infoStudentList, site, curricularCourse);
        return siteView;
    }

    private List getCurricularCourseStudents(CurricularCourse curricularCourse)
            throws ExcepcaoPersistencia {
        List infoStudentList;

        List enrolments = curricularCourse.getCurriculumModules();

        infoStudentList = (List) CollectionUtils.collect(enrolments, new Transformer() {
            public Object transform(Object input) {
                Enrolment enrolment = (Enrolment) input;
                Registration registration = enrolment.getStudentCurricularPlan().getRegistration();

                InfoStudent infoStudent = InfoStudent.newInfoFromDomain(registration);
                return infoStudent;
            }
        });
        return infoStudentList;
    }

    private List getAllAttendingStudents(final ExecutionCourseSite site) throws ExcepcaoPersistencia {
        final List<Attends> attendList = site.getExecutionCourse().getAttends();
        final List<InfoStudent> infoStudentList = new ArrayList<InfoStudent>();
        for (final Attends attends : attendList) {
            final Registration registration = attends.getAluno();
            infoStudentList.add(InfoStudent.newInfoFromDomain(registration));
        }
        return infoStudentList;
    }

    private TeacherAdministrationSiteView createSiteView(List infoStudentList, ExecutionCourseSite site,
            CurricularCourse curricularCourse) throws FenixServiceException, ExcepcaoPersistencia {
        InfoSiteStudents infoSiteStudents = new InfoSiteStudents();
        infoSiteStudents.setStudents(infoStudentList);

        if (curricularCourse != null) {
            infoSiteStudents.setInfoCurricularCourse(InfoCurricularCourse
                    .newInfoFromDomain(curricularCourse));
        }

        TeacherAdministrationSiteComponentBuilder componentBuilder = new TeacherAdministrationSiteComponentBuilder();
        ISiteComponent commonComponent = componentBuilder.getComponent(new InfoSiteCommon(), site, null,
                null, null);

        TeacherAdministrationSiteView siteView = new TeacherAdministrationSiteView(commonComponent,
                infoSiteStudents);
        return siteView;
    }
}