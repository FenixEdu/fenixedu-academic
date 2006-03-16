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
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentWithInfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.TeacherAdministrationSiteView;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;

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

    	final ExecutionCourse executionCourse = (ExecutionCourse) persistentObject.readByOID(ExecutionCourse.class, executionCourseCode);
        final Site site = executionCourse.getSite();

        if (courseCode == null) {
            infoStudentList = getAllAttendingStudents(site);
        } else {
            curricularCourse = (CurricularCourse) persistentObject.readByOID(
                    CurricularCourse.class, courseCode);

            infoStudentList = getCurricularCourseStudents(curricularCourse, persistentSupport);

        }

        TeacherAdministrationSiteView siteView = createSiteView(infoStudentList, site, curricularCourse);
        return siteView;
    }

    private List getCurricularCourseStudents(CurricularCourse curricularCourse, ISuportePersistente persistentSupport)
            throws ExcepcaoPersistencia {
        List infoStudentList;

        List enrolments = curricularCourse.getCurriculumModules();

        infoStudentList = (List) CollectionUtils.collect(enrolments, new Transformer() {
            public Object transform(Object input) {
                Enrolment enrolment = (Enrolment) input;
                Student student = enrolment.getStudentCurricularPlan().getStudent();

                InfoStudent infoStudent = InfoStudentWithInfoPerson.newInfoFromDomain(student);
                return infoStudent;
            }
        });
        return infoStudentList;
    }

    private List getAllAttendingStudents(final Site site) throws ExcepcaoPersistencia {
        final List<Attends> attendList = site.getExecutionCourse().getAttends();
        final List<InfoStudent> infoStudentList = new ArrayList<InfoStudent>();
        for (final Attends attends : attendList) {
            final Student student = attends.getAluno();
            infoStudentList.add(InfoStudentWithInfoPerson.newInfoFromDomain(student));
        }
        return infoStudentList;
    }

    private TeacherAdministrationSiteView createSiteView(List infoStudentList, Site site,
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