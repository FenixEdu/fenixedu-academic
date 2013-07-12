package net.sourceforge.fenixedu.applicationTier.Servico.Seminaries;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.SeminaryCoordinatorOrStudentFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.CandidacyDTO;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoClassification;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoSeminary;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoSeminaryWithEquivalencies;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.SelectCandidaciesDTO;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.Grade;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Seminaries.Seminary;
import net.sourceforge.fenixedu.domain.Seminaries.SeminaryCandidacy;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.ist.fenixframework.Atomic;

public class SelectCandidaciesService {

    protected SelectCandidaciesDTO run(Boolean inEnrollmentPeriod, String seminaryID) throws FenixServiceException {
        SelectCandidaciesDTO result = new SelectCandidaciesDTO();

        Collection<Seminary> seminaries = RootDomainObject.getInstance().getSeminarys();
        List infoSeminaries = getSeminaries(inEnrollmentPeriod, seminaries);
        result.setSeminaries(infoSeminaries);

        List<CandidacyDTO> infoCandidacies = new ArrayList<CandidacyDTO>();
        for (SeminaryCandidacy candidacy : getCandidacies(seminaryID, seminaries)) {
            Registration registration = candidacy.getStudent();
            StudentCurricularPlan studentCurricularPlan = getStudentCurricularPlan(registration);

            CandidacyDTO candidacyDTO = new CandidacyDTO();
            candidacyDTO.setNumber(registration.getNumber());
            candidacyDTO.setName(registration.getPerson().getName());
            candidacyDTO.setUsername(registration.getPerson().getUsername());
            candidacyDTO.setEmail(registration.getPerson().getName());
            candidacyDTO.setInfoClassification(getInfoClassification(studentCurricularPlan.getEnrolments()));
            candidacyDTO.setCandidacyId(candidacy.getExternalId());
            if (candidacy.getApproved() != null) {
                candidacyDTO.setApproved(candidacy.getApproved());
            } else {
                candidacyDTO.setApproved(Boolean.FALSE);
            }
            infoCandidacies.add(candidacyDTO);
        }
        result.setCandidacies(infoCandidacies);

        return result;
    }

    private InfoClassification getInfoClassification(List<Enrolment> enrollments) {
        InfoClassification infoClassification = new InfoClassification();
        int auxInt = 0;
        float acc = 0;
        for (final Enrolment enrollment : enrollments) {
            final Grade grade = enrollment.getGrade();

            if (grade.isApproved() && grade.isNumeric()) {
                acc += Float.valueOf(grade.getValue()).floatValue();
                auxInt++;
            }
        }
        if (auxInt != 0) {
            String value = new DecimalFormat("#0.0").format(acc / auxInt);
            infoClassification.setAritmeticClassification(value);
        }
        infoClassification.setCompletedCourses(Integer.valueOf(auxInt).toString());
        return infoClassification;
    }

    private StudentCurricularPlan getStudentCurricularPlan(Registration registration) {
        long startDate = Long.MAX_VALUE;
        StudentCurricularPlan selectedSCP = null;
        for (StudentCurricularPlan studentCurricularPlan : registration.getStudentCurricularPlans()) {
            if (studentCurricularPlan.getStartDate().getTime() < startDate) {
                startDate = studentCurricularPlan.getStartDate().getTime();
                selectedSCP = studentCurricularPlan;
            }
        }
        return selectedSCP;
    }

    private Collection<SeminaryCandidacy> getCandidacies(final String seminaryID, Collection seminaries) {
        Seminary seminary = (Seminary) CollectionUtils.find(seminaries, new Predicate() {

            @Override
            public boolean evaluate(Object arg0) {
                Seminary seminary = (Seminary) arg0;
                return seminary.getExternalId().equals(seminaryID);
            }
        });

        return seminary.getCandidacies();
    }

    private List<InfoSeminary> getSeminaries(Boolean inEnrollmentPeriod, Collection<Seminary> seminaries) {
        List<InfoSeminary> result = new ArrayList<InfoSeminary>();

        for (Seminary seminary : seminaries) {
            Calendar now = new GregorianCalendar();

            Calendar beginDate = new GregorianCalendar();
            beginDate.setTimeInMillis(seminary.getEnrollmentBeginTime().getTimeInMillis()
                    + seminary.getEnrollmentBeginDate().getTimeInMillis());

            Calendar endDate = new GregorianCalendar();
            endDate.setTimeInMillis(seminary.getEnrollmentEndTime().getTimeInMillis()
                    + seminary.getEnrollmentEndDate().getTimeInMillis());

            if (!inEnrollmentPeriod || (endDate.after(now) && beginDate.before(now))) {
                result.add(InfoSeminaryWithEquivalencies.newInfoFromDomain(seminary));
            }
        }
        return result;
    }

    // Service Invokers migrated from Berserk

    private static final SelectCandidaciesService serviceInstance = new SelectCandidaciesService();

    @Atomic
    public static SelectCandidaciesDTO runSelectCandidaciesService(Boolean inEnrollmentPeriod, String seminaryID)
            throws FenixServiceException, NotAuthorizedException {
        SeminaryCoordinatorOrStudentFilter.instance.execute();
        return serviceInstance.run(inEnrollmentPeriod, seminaryID);
    }

}