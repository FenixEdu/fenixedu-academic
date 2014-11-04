package org.fenixedu.academic.ui.struts.action;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.fenixedu.academic.service.services.manager.MergeExecutionCourses;
import org.fenixedu.academic.service.services.resourceAllocationManager.exams.EditWrittenEvaluation.EditWrittenEvaluationEvent;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.WrittenEvaluation;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.util.email.ConcreteReplyTo;
import org.fenixedu.academic.domain.util.email.Message;
import org.fenixedu.academic.domain.util.email.Recipient;
import org.fenixedu.academic.domain.util.email.Sender;
import org.fenixedu.academic.domain.vigilancy.Vigilancy;
import org.fenixedu.academic.domain.vigilancy.VigilantGroup;
import org.fenixedu.academic.util.Bundle;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.groups.UserGroup;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.signals.DomainObjectEvent;
import org.fenixedu.bennu.signals.Signal;
import org.joda.time.DateTime;

import pt.ist.fenixframework.FenixFramework;

@WebListener
public class FenixEduVigilanciesContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        FenixFramework.getDomainModel().registerDeletionListener(WrittenEvaluation.class, (writtenEvaluation) -> {
            for (Vigilancy vigilancy : writtenEvaluation.getVigilanciesSet()) {
                vigilancy.delete();
            }
        });
        FenixFramework.getDomainModel().registerDeletionListener(ExecutionCourse.class, (executionCourse) -> {
            executionCourse.setVigilantGroup(null);
        });
        FenixFramework.getDomainModel().registerDeletionBlockerListener(Unit.class, (unit, blockers) -> {
            if (!(unit.getVigilantGroupsSet().isEmpty() && unit.getExamCoordinatorsSet().isEmpty())) {
                blockers.add(BundleUtil.getString(Bundle.APPLICATION, "error.unit.cannot.be.deleted"));
            }
        });
        MergeExecutionCourses.registerMergeHandler(FenixEduVigilanciesContextListener::copyVigilantGroups);
        Signal.register("academic.writtenevaluation.deleted",
                FenixEduVigilanciesContextListener::notifyVigilantsOfDeletedEvaluation);
        Signal.register("academic.writtenevaluation.deleted",
                FenixEduVigilanciesContextListener::notifyVigilantsOfEditedEvaluation);
    }

    private static void copyVigilantGroups(ExecutionCourse executionCourseFrom, ExecutionCourse executionCourseTo) {
        if (executionCourseTo.getVigilantGroup() == null) {
            executionCourseTo.setVigilantGroup(executionCourseFrom.getVigilantGroup());
        }
    }

    private static void notifyVigilantsOfDeletedEvaluation(DomainObjectEvent<WrittenEvaluation> event) {
        WrittenEvaluation writtenEvaluation = event.getInstance();
        if (!writtenEvaluation.getVigilanciesSet().isEmpty()) {
            final Set<Person> tos = new HashSet<Person>();

            for (VigilantGroup group : VigilantGroup.getAssociatedVigilantGroups(writtenEvaluation)) {
                tos.clear();
                DateTime date = writtenEvaluation.getBeginningDateTime();
                String time = writtenEvaluation.getBeginningDateHourMinuteSecond().toString();
                String beginDateString = date.getDayOfMonth() + "-" + date.getMonthOfYear() + "-" + date.getYear();

                String subject =
                        BundleUtil.getString("resources.VigilancyResources", "email.convoke.subject", new String[] {
                                writtenEvaluation.getName(), group.getName(), beginDateString, time });
                String body =
                        BundleUtil.getString("resources.VigilancyResources", "label.writtenEvaluationDeletedMessage",
                                new String[] { writtenEvaluation.getName(), beginDateString, time });
                for (Vigilancy vigilancy : writtenEvaluation.getVigilanciesSet()) {
                    Person person = vigilancy.getVigilantWrapper().getPerson();
                    tos.add(person);
                }
                Sender sender = Bennu.getInstance().getSystemSender();
                new Message(sender, new ConcreteReplyTo(group.getContactEmail()).asCollection(), new Recipient(
                        UserGroup.of(Person.convertToUsers(tos))).asCollection(), subject, body, "");

            }
        }
    }

    private static void notifyVigilantsOfEditedEvaluation(EditWrittenEvaluationEvent event) {
        WrittenEvaluation writtenEvaluation = event.getInstance();
        Date dayDate = event.getDayDate();
        Date beginDate = event.getBeginDate();
        if (!writtenEvaluation.getVigilanciesSet().isEmpty()
                && (dayDate != writtenEvaluation.getDayDate() || timeModificationIsBiggerThanFiveMinutes(beginDate,
                        writtenEvaluation.getBeginningDate()))) {
            final HashSet<Person> tos = new HashSet<Person>();

            // VigilantGroup group =
            // writtenEvaluation.getAssociatedVigilantGroups().iterator().next();
            for (VigilantGroup group : VigilantGroup.getAssociatedVigilantGroups(writtenEvaluation)) {
                tos.clear();
                DateTime date = writtenEvaluation.getBeginningDateTime();
                String time = writtenEvaluation.getBeginningDateHourMinuteSecond().toString();
                String beginDateString = date.getDayOfMonth() + "-" + date.getMonthOfYear() + "-" + date.getYear();

                String subject =
                        String.format("[ %s - %s - %s %s ]", new Object[] { writtenEvaluation.getName(), group.getName(),
                                beginDateString, time });
                String body =
                        String.format(
                                "Caro Vigilante,\n\nA prova de avaliação: %1$s %2$s - %3$s foi alterada para  %4$td-%4$tm-%4$tY - %5$tH:%5$tM.",
                                new Object[] { writtenEvaluation.getName(), beginDateString, time, dayDate, beginDate });

                for (Vigilancy vigilancy : writtenEvaluation.getVigilanciesSet()) {
                    Person person = vigilancy.getVigilantWrapper().getPerson();
                    tos.add(person);
                }
                Sender sender = Bennu.getInstance().getSystemSender();
                new Message(sender, new ConcreteReplyTo(group.getContactEmail()).asCollection(), new Recipient(
                        UserGroup.of(Person.convertToUsers(tos))).asCollection(), subject, body, "");
            }
        }
    }

    private static boolean timeModificationIsBiggerThanFiveMinutes(Date writtenEvaluationStartTime, Date beginningDate) {
        int hourDiference = Math.abs(writtenEvaluationStartTime.getHours() - beginningDate.getHours());
        int minuteDifference = Math.abs(writtenEvaluationStartTime.getMinutes() - beginningDate.getMinutes());

        return hourDiference > 0 || minuteDifference > 5;
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
