package net.sourceforge.fenixedu.applicationTier.Servico.commons.curriculumHistoric;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.commons.curriculumHistoric.InfoCurriculumHistoricReport;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class ReadCurriculumHistoricReport extends Service {

    public InfoCurriculumHistoricReport run(Integer curricularCourseID, Integer semester,
            Integer executionYearID) throws FenixServiceException {

        final ExecutionYear executionYear = rootDomainObject.readExecutionYearByOID(executionYearID);
        final ExecutionPeriod executionPeriod = executionYear.readExecutionPeriodForSemester(semester);

        final CurricularCourse curricularCourse = (CurricularCourse) rootDomainObject.readDegreeModuleByOID(curricularCourseID);
        final List<Enrolment> enrollments = curricularCourse.getEnrolmentsByExecutionPeriod(executionPeriod);

        InfoCurriculumHistoricReport infoCurriculumHistoricReport = createInfoCurriculumHistoricReport(enrollments);
        
        infoCurriculumHistoricReport.setInfoCurricularCourse(InfoCurricularCourse.newInfoFromDomain(curricularCourse));
        infoCurriculumHistoricReport.setInfoExecutionYear(InfoExecutionYear.newInfoFromDomain(executionYear));
        infoCurriculumHistoricReport.setSemester(semester);

        return infoCurriculumHistoricReport;
    }

    private InfoCurriculumHistoricReport createInfoCurriculumHistoricReport(List enrollments) {

        final List<Enrolment> notAnulledEnrollments = (List) CollectionUtils.select(enrollments, new Predicate() {

            public boolean evaluate(Object obj) {
                Enrolment enrollment = (Enrolment) obj;
                return !enrollment.getEnrollmentState().equals(EnrollmentState.ANNULED);
            }
        });

        final List<Enrolment> evaluatedEnrollments = (List) CollectionUtils.select(notAnulledEnrollments,
                new Predicate() {
                    public boolean evaluate(Object obj) {
                        Enrolment enrollment = (Enrolment) obj;
                        return (enrollment.getEnrollmentState().equals(EnrollmentState.APROVED)
                                || enrollment.getEnrollmentState().equals(EnrollmentState.NOT_APROVED));
                    }
                });

        final List<Enrolment> aprovedEnrollments = (List) CollectionUtils.select(evaluatedEnrollments, new Predicate() {

            public boolean evaluate(Object obj) {
                Enrolment enrollment = (Enrolment) obj;
                return enrollment.getEnrollmentState().equals(EnrollmentState.APROVED);
            }
        });

        InfoCurriculumHistoricReport infoCurriculumHistoricReport = new InfoCurriculumHistoricReport();
        infoCurriculumHistoricReport.setEnrolled(Integer.valueOf(notAnulledEnrollments.size()));
        infoCurriculumHistoricReport.setEvaluated(Integer.valueOf(evaluatedEnrollments.size()));
        infoCurriculumHistoricReport.setAproved(Integer.valueOf(aprovedEnrollments.size()));

        final List<InfoEnrolment> infoEnrollments = new ArrayList<InfoEnrolment>(notAnulledEnrollments.size());
        for (final Enrolment enrolment : notAnulledEnrollments) {
            infoEnrollments.add(InfoEnrolment.newInfoFromDomain(enrolment));
        }
        
        infoCurriculumHistoricReport.setEnrollments(infoEnrollments);

        return infoCurriculumHistoricReport;
    }

}
