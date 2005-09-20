package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Factory.TeacherAdministrationSiteComponentBuilder;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteTeacherStudentsEnrolledList;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.InfoWrittenEvaluationEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.InfoWrittenEvaluationEnrolmentWithInfoStudentAndInfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.TeacherAdministrationSiteView;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.IExam;
import net.sourceforge.fenixedu.domain.ISite;
import net.sourceforge.fenixedu.domain.IWrittenEvaluationEnrolment;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExam;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSite;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadStudentsEnrolledInExam implements IService {

    public Object run(Integer executionCourseID, Integer examID) throws FenixServiceException,
            ExcepcaoPersistencia {

        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();

        final IPersistentExam persistentExam = persistentSupport.getIPersistentExam();
        final IExam exam = (IExam) persistentExam.readByOID(Exam.class, examID);
        if (examID == null) {
            throw new FenixServiceException("error.noExam");
        }

        final IPersistentSite persistentSite = persistentSupport.getIPersistentSite();
        final ISite site = persistentSite.readByExecutionCourse(executionCourseID);
        if (site == null) {
            throw new FenixServiceException("error.noSite");
        }

        final List<IWrittenEvaluationEnrolment> writtenEvaluationEnrolmentList = exam
                .getWrittenEvaluationEnrolments();

        final List<InfoStudent> infoStudents = new ArrayList<InfoStudent>(writtenEvaluationEnrolmentList
                .size());
        final List<InfoWrittenEvaluationEnrolment> infoWrittenEvaluationEnrolments = new ArrayList<InfoWrittenEvaluationEnrolment>(
                writtenEvaluationEnrolmentList.size());

        for (final IWrittenEvaluationEnrolment writtenEvaluationEnrolment : writtenEvaluationEnrolmentList) {
            infoStudents.add(InfoStudent.newInfoFromDomain(writtenEvaluationEnrolment.getStudent()));
            infoWrittenEvaluationEnrolments.add(InfoWrittenEvaluationEnrolmentWithInfoStudentAndInfoRoom
                    .newInfoFromDomain(writtenEvaluationEnrolment));
        }

        final InfoExam infoExam = InfoExam.newInfoFromDomain(exam);
        final ISiteComponent component = new InfoSiteTeacherStudentsEnrolledList(infoStudents, infoExam,
                infoWrittenEvaluationEnrolments);
        final TeacherAdministrationSiteComponentBuilder componentBuilder = TeacherAdministrationSiteComponentBuilder
                .getInstance();
        final ISiteComponent commonComponent = componentBuilder.getComponent(new InfoSiteCommon(), site,
                null, null, null);
        final TeacherAdministrationSiteView siteView = new TeacherAdministrationSiteView(
                commonComponent, component);

        return siteView;
    }
}