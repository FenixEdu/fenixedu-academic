package net.sourceforge.fenixedu.applicationTier.Servico.coordinator.tutor;

import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Option;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.Tutorship;
import net.sourceforge.fenixedu.domain.TutorshipLog;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public abstract class TutorshipManagement {

    private boolean verifyIfBelongsToDegree(Registration registration, Degree degree) {
        if (registration == null) {
            return false;
        }

        StudentCurricularPlan studentCurricularPlan = registration.getActiveStudentCurricularPlan();
        if (studentCurricularPlan == null) {
            return false;
        }

        return studentCurricularPlan.getDegreeCurricularPlan().getDegree().equals(degree);
    }

    protected void validateTeacher(Teacher teacher, ExecutionDegree executionDegree) throws FenixServiceException {
        List<Teacher> possibleTutorsForExecutionDegree = executionDegree.getPossibleTutorsFromExecutionDegreeDepartments();

        if (!possibleTutorsForExecutionDegree.contains(teacher)) {
            throw new FenixServiceException("error.tutor.cannotBeTutorOfExecutionDegree");
        }
    }

    protected void validateStudentRegistration(Registration registration, ExecutionDegree executionDegree,
            DegreeCurricularPlan degreeCurricularPlan, Integer studentNumber) throws FenixServiceException {

        if (!verifyIfBelongsToDegree(registration, degreeCurricularPlan.getDegree())) {
            // student doesn't belong to this degree
            ResourceBundle bundle = ResourceBundle.getBundle("resources.EnumerationResources", Language.getLocale());
            String degreeType = bundle.getString(executionDegree.getDegree().getDegreeType().getName());

            throw new FenixServiceException("error.tutor.studentNoDegree", new String[] { studentNumber.toString(), degreeType,
                    executionDegree.getDegree().getNameFor(registration.getStartExecutionYear()).getContent() });
        }
    }

    protected void validateTutorship(Registration registration) throws FenixServiceException {
        if (registration.getActiveTutorship() != null) {
            // student already with tutor
            throw new FenixServiceException("error.tutor.studentAlreadyHasTutor", new String[] { registration.getNumber()
                    .toString() });
        }
    }

    protected void createTutorship(Teacher teacher, StudentCurricularPlan scp, Integer endMonth, Integer endYear) {
        YearMonthDay currentDate = new YearMonthDay();

        Partial tutorshipStartDate =
                new Partial(new DateTimeFieldType[] { DateTimeFieldType.year(), DateTimeFieldType.monthOfYear() }, new int[] {
                        currentDate.year().get(), currentDate.monthOfYear().get() });

        Partial tutorshipEndDate =
                new Partial(new DateTimeFieldType[] { DateTimeFieldType.year(), DateTimeFieldType.monthOfYear() }, new int[] {
                        endYear, endMonth });
        Tutorship tutorship = new Tutorship(teacher, tutorshipStartDate, tutorshipEndDate);
        scp.addTutorships(tutorship);

        TutorshipLog tutorshipLog = new TutorshipLog();
        if (scp.getRegistration() != null && scp.getRegistration().getStudentCandidacy() != null
                && scp.getRegistration().getStudentCandidacy().getPlacingOption() != null) {
            switch (scp.getRegistration().getStudentCandidacy().getPlacingOption()) {
            case 1: {
                tutorshipLog.setOptionNumberDegree(Option.ONE);
                break;
            }
            case 2: {
                tutorshipLog.setOptionNumberDegree(Option.TWO);
                break;
            }
            case 3: {
                tutorshipLog.setOptionNumberDegree(Option.THREE);
                break;
            }
            case 4: {
                tutorshipLog.setOptionNumberDegree(Option.FOUR);
                break;
            }
            case 5: {
                tutorshipLog.setOptionNumberDegree(Option.FIVE);
                break;
            }
            case 6: {
                tutorshipLog.setOptionNumberDegree(Option.SIX);
                break;
            }
            default: {
                tutorshipLog.setOptionNumberDegree(null);
            }
            }
        }
        tutorship.setTutorshipLog(tutorshipLog);
    }
}
