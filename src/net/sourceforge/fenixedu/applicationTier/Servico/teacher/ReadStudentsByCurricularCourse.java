package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.List;

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
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.ISite;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSite;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Fernanda Quitério
 * @author Tânia Pousão
 * @author Ângela
 *  
 */
public class ReadStudentsByCurricularCourse implements IService {

    public Object run(Integer executionCourseCode, Integer courseCode) throws ExcepcaoInexistente,
            FenixServiceException, ExcepcaoPersistencia {
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentSite persistentSite = sp.getIPersistentSite();

        List infoStudentList = null;
        ICurricularCourse curricularCourse = null;

        final ISite site = persistentSite.readByExecutionCourse(executionCourseCode);

        if (courseCode == null) {
            infoStudentList = getAllAttendingStudents(sp, site);
        } else {
            IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();
            curricularCourse = (ICurricularCourse) persistentCurricularCourse.readByOID(
                    CurricularCourse.class, courseCode);

            infoStudentList = getCurricularCourseStudents(curricularCourse, sp);

        }

        TeacherAdministrationSiteView siteView = createSiteView(infoStudentList, site, curricularCourse);
        return siteView;
    }

    private List getCurricularCourseStudents(ICurricularCourse curricularCourse, ISuportePersistente sp)
            throws ExcepcaoPersistencia {
        List infoStudentList;

        List enrolments = curricularCourse.getEnrolments();

        infoStudentList = (List) CollectionUtils.collect(enrolments, new Transformer() {
            public Object transform(Object input) {
                IEnrolment enrolment = (IEnrolment) input;
                IStudent student = enrolment.getStudentCurricularPlan().getStudent();

                InfoStudent infoStudent = InfoStudentWithInfoPerson.newInfoFromDomain(student);
                return infoStudent;
            }
        });
        return infoStudentList;
    }

    private List getAllAttendingStudents(final ISuportePersistente sp, final ISite site) throws ExcepcaoPersistencia {
        final List<IAttends> attendList = site.getExecutionCourse().getAttends();
        final List<InfoStudent> infoStudentList = new ArrayList<InfoStudent>();
        for (final IAttends attends : attendList) {
            final IStudent student = attends.getAluno();
            infoStudentList.add(InfoStudentWithInfoPerson.newInfoFromDomain(student));
        }
        return infoStudentList;
    }

    private TeacherAdministrationSiteView createSiteView(List infoStudentList, ISite site,
            ICurricularCourse curricularCourse) throws FenixServiceException, ExcepcaoPersistencia {
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