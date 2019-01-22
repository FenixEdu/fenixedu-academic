package org.fenixedu.academic.task;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.calculator.DebtInterestCalculator;
import org.fenixedu.academic.domain.organizationalStructure.Party;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.scheduler.CronTask;
import org.fenixedu.bennu.scheduler.annotation.Task;
import org.fenixedu.messaging.core.domain.Message;
import org.fenixedu.messaging.core.domain.MessageTemplate;
import org.fenixedu.messaging.core.template.DeclareMessageTemplate;
import org.fenixedu.messaging.core.template.TemplateParameter;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.ist.fenixframework.FenixFramework;

@DeclareMessageTemplate(
        id = "event.notification.new.event",
        description = "event.notification.new.event.description",
        subject = "event.notification.new.event.subject",
        text = "event.notification.new.event.body",
        parameters = {
                @TemplateParameter(id = "eventDescription", description = "event.notification.new.event.param.eventDescription"),
                @TemplateParameter(id = "debtAmount", description = "event.notification.new.event.param.debtAmount")
        },
        bundle = "resources.AccountingResources"
)
@DeclareMessageTemplate(
        id = "event.notification.due.event",
        description = "event.notification.due.event.description",
        subject = "event.notification.due.event.subject",
        text = "event.notification.due.event.body",
        parameters = {
                @TemplateParameter(id = "eventDescription", description = "event.notification.due.event.param.eventDescription"),
                @TemplateParameter(id = "dueAmount", description = "event.notification.due.event.param.dueAmount")
        },
        bundle = "resources.AccountingResources"
)
@DeclareMessageTemplate(
        id = "event.notification.unused.amount",
        description = "event.notification.unused.amount.description",
        subject = "event.notification.unused.amount.subject",
        text = "event.notification.unused.amount.body",
        parameters = {
                @TemplateParameter(id = "eventDescription", description = "event.notification.unused.amount.param.eventDescription"),
                @TemplateParameter(id = "totalUnusedAmount", description = "event.notification.unused.amount.param.totalUnusedAmount")
        },
        bundle = "resources.AccountingResources"
)
@DeclareMessageTemplate(
        id = "event.notifications",
        description = "event.notifications.description",
        subject = "event.notifications.subject",
        text = "event.notifications.body",
        parameters = {
                @TemplateParameter(id = "notificationMessage", description = "event.notifications.notificationMessage")
        },
        bundle = "resources.AccountingResources"
)
@Task(englishTitle = "Send notifications regarding events (new, close to due date and excess paynebts.", readOnly = true)
public class EventNotificationTask extends CronTask {

    private Map<String, Object> paramMap(final Event event, final String key, final String value) {
        final Map<String, Object> params = new HashMap<>();
        params.put("eventDescription", event.getDescription());
        params.put(key, value);
        return params;
    }

    private String createMessage(final String templateKey, final Event event, final String paramKey, final String paramValue) {
        return MessageTemplate.get(templateKey).getCompiledTextBody(paramMap(event, paramKey, paramValue)).getContent();
    }

    @Override
    public void runTask() throws Exception {
        final DateTime now = new DateTime();
        final LocalDate afterTomorrow = new LocalDate().plusDays(2);
        final LocalDate yesterday = new LocalDate().minusDays(1);

        Bennu.getInstance().getPartysSet().stream()
            .parallel()
            .forEach(p -> process(p, now, afterTomorrow, yesterday));
    }

    private void process(final Party party, final DateTime now, final LocalDate afterTomorrow, final LocalDate yesterday) {
        FenixFramework.atomic(() -> {
            if (party.isPerson()) {
                final Person person = (Person) party;

                final String message = person.getEventsSet().stream()
                    .flatMap(e -> generateNotifications(e, now, afterTomorrow, yesterday))
                    .reduce("", String::concat);
                if (!message.isEmpty()) {
                    Message.fromSystem()
                        .to(Group.users(person.getUser()))
                        .template("event.notifications")
                        .parameter("notificationMessage", message)
                        .and()
                        .wrapped()
                        .send();
                }
            }
        });
    }

    private Stream<String> generateNotifications(final Event event, final DateTime now, final LocalDate afterTomorrow, final LocalDate yesterday) {
        try {
            final DebtInterestCalculator calculator = event.getDebtInterestCalculator(now);
            if (event.getWhenOccured().toLocalDate().equals(yesterday) && calculator.getDueAmount().signum() == 1) {
                return Stream.of(createMessage("event.notification.new.event", event, "debtAmount", calculator.getDebtAmount().toPlainString()));
            }
            
            final Stream<String> dueAmountNotifications = calculator.getDebtsOrderedByDueDate().stream()
                    .filter(d -> d.isOpen() && d.getDueDate().equals(afterTomorrow))
                    .map(d -> createMessage("event.notification.due.event", event, "dueAmount", d.getAmount().toPlainString()));
            
            final BigDecimal totalUnusedAmount = calculator.getTotalUnusedAmount();
            if (totalUnusedAmount.signum() == 1) {
                final DateTime lastPaymentDate = calculator.getPayments()
                        .map(p -> p.getCreated())
                        .max((dt1, dt2) -> dt1.compareTo(dt2))
                        .orElse(null);
                if (lastPaymentDate != null && lastPaymentDate.toLocalDate().equals(yesterday)) {
                    return Stream.concat(dueAmountNotifications, Stream.of(createMessage("event.notification.unused.amount", event, "totalUnusedAmount", totalUnusedAmount.toPlainString())));
                }
            }
            return dueAmountNotifications;
        } catch (final Throwable t) {
            taskLog("Unable to process event: %s - %s%n", event.getExternalId(), t.getMessage());
            return Stream.empty();
        }
    }

}
