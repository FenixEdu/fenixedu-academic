package org.fenixedu.academic.ui.struts.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.Tutorship;
import org.fenixedu.academic.domain.TutorshipIntention;
import org.fenixedu.academic.domain.candidacy.StudentCandidacy;
import org.fenixedu.academic.domain.candidacy.workflow.RegistrationOperation.RegistrationCreatedByCandidacy;
import org.fenixedu.academic.domain.student.Registration;

import org.fenixedu.bennu.signals.Signal;
import org.joda.time.LocalDate;

import pt.ist.fenixframework.FenixFramework;

@WebListener
public class FenixEduTutorshipContextListener implements ServletContextListener {
    static private final List<String> DEGREES_WITHOUT_AUTOMATIC_TUTOR_DISTRIBUTION = new ArrayList<String>();

    static {
        DEGREES_WITHOUT_AUTOMATIC_TUTOR_DISTRIBUTION.add("MEMec");
        DEGREES_WITHOUT_AUTOMATIC_TUTOR_DISTRIBUTION.add("MEC");
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        FenixFramework.getDomainModel().registerDeletionListener(
                StudentCurricularPlan.class,
                (studentCurricularPlan) -> {
                    for (; !studentCurricularPlan.getTutorshipsSet().isEmpty(); studentCurricularPlan.getTutorshipsSet()
                            .iterator().next().delete()) {
                        ;
                    }
                });

        Signal.register("academic.candidacy.registration.created", FenixEduTutorshipContextListener::associateTutor);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

    private static void associateTutor(RegistrationCreatedByCandidacy event) {
        Registration registration = event.getInstance();
        if (!DEGREES_WITHOUT_AUTOMATIC_TUTOR_DISTRIBUTION.contains(registration.getDegree().getSigla())) {
            Teacher teacher = getAvailableTutorTeacher(event.getCandidacy());
            if (teacher != null) {
                StudentCurricularPlan scp = registration.getActiveStudentCurricularPlan();
                Tutorship.createTutorship(teacher, scp, new LocalDate().getMonthOfYear(),
                        Tutorship.getLastPossibleTutorshipYear());
            }
        }
    }

    private static Teacher getAvailableTutorTeacher(StudentCandidacy studentCandidacy) {
        for (TutorshipIntention tutorshipIntention : TutorshipIntention.getTutorshipIntentions(studentCandidacy
                .getExecutionDegree())) {
            if (tutorshipIntention.getMaxStudentsToTutor() != null && tutorshipIntention.getMaxStudentsToTutor() > 0) {
                tutorshipIntention.setMaxStudentsToTutor(tutorshipIntention.getMaxStudentsToTutor() - 1);
                return tutorshipIntention.getTeacher();
            }
        }
        // when all tutors are full start distributing equally among them
        TutorshipIntention tutorshipIntention = getLeastOverloadedTutor(studentCandidacy);
        if (tutorshipIntention != null) {
            tutorshipIntention.setMaxStudentsToTutor(tutorshipIntention.getMaxStudentsToTutor() - 1);
            return tutorshipIntention.getTeacher();
        }
        return null;
    }

    private static TutorshipIntention getLeastOverloadedTutor(StudentCandidacy studentCandidacy) {
        TutorshipIntention chosenTutor = null;
        for (TutorshipIntention tutorshipIntention : TutorshipIntention.getTutorshipIntentions(studentCandidacy
                .getExecutionDegree())) {
            if (tutorshipIntention.getMaxStudentsToTutor() != null) {
                if (chosenTutor == null) {
                    chosenTutor = tutorshipIntention;
                } else if (Math.abs(tutorshipIntention.getMaxStudentsToTutor()) < Math.abs(chosenTutor.getMaxStudentsToTutor())) {
                    chosenTutor = tutorshipIntention;
                }
            }
        }
        return chosenTutor;
    }

}
