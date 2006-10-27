package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Factory.TeacherAdministrationSiteComponentBuilder;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteTeacherStudentsEnrolledList;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.InfoWrittenEvaluationEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.InfoWrittenEvaluationEnrolmentWithInfoStudentAndInfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoWrittenTest;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.dataTransferObject.TeacherAdministrationSiteView;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourseSite;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.WrittenEvaluationEnrolment;
import net.sourceforge.fenixedu.domain.WrittenTest;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadStudentsEnrolledInWrittenEvaluation extends Service {

    public SiteView run(Integer executionCourseID, Integer writtenEvaluationID)
            throws FenixServiceException, ExcepcaoPersistencia {

        final WrittenEvaluation writtenEvaluation = (WrittenEvaluation) rootDomainObject.readEvaluationByOID(writtenEvaluationID);
        if (writtenEvaluation == null) {
            throw new FenixServiceException("error.noWrittenEvaluation");
        }

    	final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseID);
        final ExecutionCourseSite site = executionCourse.getSite();
        if (site == null) {
            throw new FenixServiceException("error.noSite");
        }

        final List<WrittenEvaluationEnrolment> writtenEvaluationEnrolmentList = writtenEvaluation
                .getWrittenEvaluationEnrolments();

        final List<InfoStudent> infoStudents = new ArrayList<InfoStudent>(writtenEvaluationEnrolmentList
                .size());
        final List<InfoWrittenEvaluationEnrolment> infoWrittenEvaluationEnrolments = new ArrayList<InfoWrittenEvaluationEnrolment>(
                writtenEvaluationEnrolmentList.size());

        for (final WrittenEvaluationEnrolment writtenEvaluationEnrolment : writtenEvaluationEnrolmentList) {
            infoStudents.add(InfoStudent.newInfoFromDomain(writtenEvaluationEnrolment.getStudent()));
            infoWrittenEvaluationEnrolments.add(InfoWrittenEvaluationEnrolmentWithInfoStudentAndInfoRoom
                    .newInfoFromDomain(writtenEvaluationEnrolment));
        }

        ISiteComponent component = null;
        if (writtenEvaluation instanceof Exam) {
            final InfoExam infoExam = InfoExam.newInfoFromDomain((Exam) writtenEvaluation);
            component = new InfoSiteTeacherStudentsEnrolledList(infoStudents, infoExam,
                    infoWrittenEvaluationEnrolments);
        } else if (writtenEvaluation instanceof WrittenTest) {
            final InfoWrittenTest infoWrittenTest = InfoWrittenTest
                    .newInfoFromDomain((WrittenTest) writtenEvaluation);
            component = new InfoSiteTeacherStudentsEnrolledList(infoStudents, infoWrittenTest,
                    infoWrittenEvaluationEnrolments);
        } else {
            throw new FenixServiceException("error.noWrittenEvaluation");
        }

        final TeacherAdministrationSiteComponentBuilder componentBuilder = TeacherAdministrationSiteComponentBuilder
                .getInstance();
        final ISiteComponent commonComponent = componentBuilder.getComponent(new InfoSiteCommon(), site,
                null, null, null);
        final TeacherAdministrationSiteView siteView = new TeacherAdministrationSiteView(
                commonComponent, component);

        return siteView;
    }
}