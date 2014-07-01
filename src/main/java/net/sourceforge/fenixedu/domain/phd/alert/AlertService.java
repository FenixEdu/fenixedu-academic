/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.domain.phd.alert;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.phd.InternalPhdParticipant;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdParticipant;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.domain.util.email.ReplyTo;
import net.sourceforge.fenixedu.domain.util.email.UnitBasedSender;
import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.groups.UserGroup;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.LocalDate;

import pt.ist.fenixframework.DomainObject;

public class AlertService {

    static private final String PREFIX_PHD_LABEL = "label.phds";

    public static String getSubjectPrefixed(PhdIndividualProgramProcess process, String subjectKey) {
        return getProcessNumberPrefix(process) + getMessageFromResource(subjectKey);
    }

    static public String getProcessNumberPrefix(PhdIndividualProgramProcess process) {
        return "[" + getMessageFromResource(PREFIX_PHD_LABEL) + " - " + process.getProcessNumber() + "] ";
    }

    static public String getMessageFromResource(String key) {
        return BundleUtil.getString(Bundle.PHD, key);
    }

    static private String getBodyCommonText(final PhdIndividualProgramProcess process) {
        final StringBuilder builder = new StringBuilder();

        builder.append("------------------------------------------------------\n");

        if (process.getPerson().hasStudent()) {
            builder.append(getSlotLabel(PhdIndividualProgramProcess.class, "student.number"));
            builder.append(": ").append(process.getPerson().getStudent().getNumber());
        }
        builder.append("\n");

        builder.append(getSlotLabel(PhdIndividualProgramProcess.class, "processNumber"));
        builder.append(": ").append(process.getPhdIndividualProcessNumber().getFullProcessNumber()).append("\n");

        builder.append(getSlotLabel(PhdIndividualProgramProcess.class, "person.name"));
        builder.append(": ").append(process.getPerson().getName()).append("\n");

        builder.append(getSlotLabel(PhdIndividualProgramProcess.class, "phdProgram"));
        if (process.hasPhdProgram()) {
            builder.append(": ").append(process.getPhdProgram().getName());
        }
        builder.append("\n");

        builder.append(getSlotLabel(PhdIndividualProgramProcess.class, "activeState"));
        builder.append(": ").append(process.getActiveState().getLocalizedName()).append("\n");

        if (process.hasCandidacyProcess()) {
            builder.append(getMessageFromResource("label.phd.candidacy")).append(": ");
            builder.append(process.getCandidacyProcess().getActiveState().getLocalizedName()).append("\n");
        }

        if (process.hasSeminarProcess()) {
            builder.append(getMessageFromResource("label.phd.publicPresentationSeminar")).append(": ");
            builder.append(process.getSeminarProcess().getActiveState().getLocalizedName()).append("\n");
        }

        if (process.hasThesisProcess()) {
            builder.append(getMessageFromResource("label.phd.thesis")).append(": ");
            builder.append(process.getThesisProcess().getActiveState().getLocalizedName()).append("\n");
        }

        builder.append(getSlotLabel(PhdIndividualProgramProcess.class, "executionYear"));
        builder.append(": ").append(process.getExecutionYear().getQualifiedName()).append("\n");

        builder.append("------------------------------------------------------\n\n");

        return builder.toString();
    }

    static public String getBodyText(PhdIndividualProgramProcess process, String bodyText) {
        return getBodyCommonText(process) + getMessageFromResource(bodyText);
    }

    static private String getSlotLabel(Class<? extends DomainObject> clazz, String slotName) {
        return getMessageFromResource("label." + clazz.getName() + "." + slotName);
    }

    static public void alertStudent(PhdIndividualProgramProcess process, String subjectKey, String bodyKey) {
        final PhdCustomAlertBean alertBean = new PhdCustomAlertBean(process, true, false, false);

        alertBean.setSubject(getSubjectPrefixed(process, subjectKey));
        alertBean.setBody(getBodyText(process, bodyKey));
        alertBean.setFireDate(new LocalDate());
        alertBean.setTargetGroup(UserGroup.of(process.getPerson().getUser()));

        new PhdCustomAlert(alertBean);
    }

    static public void alertStudent(PhdIndividualProgramProcess process, AlertMessage subject, AlertMessage body) {
        final PhdCustomAlertBean alertBean = new PhdCustomAlertBean(process, true, false, false);

        alertBean.setSubject(getSubjectPrefixed(process, subject));
        alertBean.setBody(getBodyText(process, body));
        alertBean.setFireDate(new LocalDate());
        alertBean.setTargetGroup(UserGroup.of(process.getPerson().getUser()));

        new PhdCustomAlert(alertBean);
    }

    static public void alertGuiders(PhdIndividualProgramProcess process, String subjectKey, String bodyKey) {

        final Set<Person> toNotify = new HashSet<Person>();
        for (final PhdParticipant guiding : process.getGuidingsAndAssistantGuidings()) {
            if (guiding.isInternal()) {
                toNotify.add(((InternalPhdParticipant) guiding).getPerson());
            } else {
                guiding.ensureExternalAccess();
                new Message(Bennu.getInstance().getSystemSender(), Collections.<ReplyTo> emptyList(),
                        Collections.<Recipient> emptyList(), getSubjectPrefixed(process, subjectKey), getBodyText(process,
                                bodyKey), Collections.singleton(guiding.getEmail()));
            }
        }

        final PhdCustomAlertBean alertBean = new PhdCustomAlertBean(process, true, false, false);
        alertBean.setSubject(getSubjectPrefixed(process, subjectKey));
        alertBean.setBody(getBodyText(process, bodyKey));
        alertBean.setFireDate(new LocalDate());
        alertBean.setTargetGroup(UserGroup.of(Person.convertToUsers(toNotify)));

        new PhdCustomAlert(alertBean);

    }

    static public void alertGuiders(PhdIndividualProgramProcess process, AlertMessage subjectMessage, AlertMessage bodyMessage) {

        final Set<Person> toNotify = new HashSet<Person>();

        for (final PhdParticipant guiding : process.getGuidingsAndAssistantGuidings()) {
            if (guiding.isInternal()) {
                toNotify.add(((InternalPhdParticipant) guiding).getPerson());
            } else {
                guiding.ensureExternalAccess();
                new Message(Bennu.getInstance().getSystemSender(), Collections.<ReplyTo> emptyList(),
                        Collections.<Recipient> emptyList(), getSubjectPrefixed(process, subjectMessage), getBodyText(process,
                                bodyMessage), Collections.singleton(guiding.getEmail()));
            }
        }

        final PhdCustomAlertBean alertBean = new PhdCustomAlertBean(process, true, false, false);
        alertBean.setSubject(getSubjectPrefixed(process, subjectMessage));
        alertBean.setBody(getBodyText(process, bodyMessage));
        alertBean.setFireDate(new LocalDate());
        alertBean.setTargetGroup(UserGroup.of(Person.convertToUsers(toNotify)));

        new PhdCustomAlert(alertBean);

    }

    static public void alertAcademicOffice(PhdIndividualProgramProcess process, String subjectKey, String bodyKey) {
        final PhdCustomAlertBean alertBean = new PhdCustomAlertBean(process, true, false, true);
        alertBean.setSubject(getSubjectPrefixed(process, subjectKey));
        alertBean.setBody(getBodyText(process, bodyKey));
        alertBean.setFireDate(new LocalDate());
        alertBean.setTargetGroup(AcademicAuthorizationGroup.get(AcademicOperationType.MANAGE_PHD_PROCESSES, process.getPhdProgram()));

        new PhdCustomAlert(alertBean);
    }

    static public void alertAcademicOffice(PhdIndividualProgramProcess process, AcademicOperationType permissionType,
            String subjectKey, String bodyKey) {
        final PhdCustomAlertBean alertBean = new PhdCustomAlertBean(process, true, false, true);
        alertBean.setSubject(getSubjectPrefixed(process, subjectKey));
        alertBean.setBody(getBodyText(process, bodyKey));
        alertBean.setFireDate(new LocalDate());
        alertBean.setTargetGroup(getTargetGroup(permissionType, process.getPhdProgram()));

        new PhdCustomAlert(alertBean);
    }

    static public void alertAcademicOffice(PhdIndividualProgramProcess process, AcademicOperationType permissionType,
            AlertMessage subjectMessage, AlertMessage bodyMessage) {
        final PhdCustomAlertBean alertBean = new PhdCustomAlertBean(process, true, false, true);
        alertBean.setSubject(getSubjectPrefixed(process, subjectMessage));
        alertBean.setBody(getBodyText(process, bodyMessage));
        alertBean.setFireDate(new LocalDate());
        alertBean.setTargetGroup(getTargetGroup(permissionType, process.getPhdProgram()));

        new PhdCustomAlert(alertBean);
    }

    static private Group getTargetGroup(AcademicOperationType permissionType, PhdProgram program) {
        return AcademicAuthorizationGroup.get(permissionType, program);
    }

    static public void alertCoordinators(PhdIndividualProgramProcess process, String subjectKey, String bodyKey) {
        alertCoordinators(process, process.getCoordinatorsFor(ExecutionYear.readCurrentExecutionYear()), subjectKey, bodyKey);
    }

    static private void alertCoordinators(PhdIndividualProgramProcess process, Set<Person> persons, String subjectKey,
            String bodyKey) {
        final PhdCustomAlertBean alertBean = new PhdCustomAlertBean(process, true, false, false);
        alertBean.setSubject(getSubjectPrefixed(process, subjectKey));
        alertBean.setBody(getBodyText(process, bodyKey));
        alertBean.setTargetGroup(UserGroup.of(Person.convertToUsers(persons)));
        alertBean.setFireDate(new LocalDate());

        new PhdCustomAlert(alertBean);
    }

    static public void alertCoordinators(PhdIndividualProgramProcess process, AlertMessage subject, AlertMessage body) {
        alertCoordinators(process, process.getCoordinatorsFor(ExecutionYear.readCurrentExecutionYear()), subject, body);
    }

    static public void alertResponsibleCoordinators(PhdIndividualProgramProcess process, AlertMessage subject, AlertMessage body) {
        alertCoordinators(process, process.getResponsibleCoordinatorsFor(ExecutionYear.readCurrentExecutionYear()), subject, body);
    }

    static private void alertCoordinators(PhdIndividualProgramProcess process, Set<Person> persons, AlertMessage subject,
            AlertMessage body) {
        final PhdCustomAlertBean alertBean = new PhdCustomAlertBean(process, true, false, false);
        alertBean.setSubject(getSubjectPrefixed(process, subject));
        alertBean.setBody(getBodyText(process, body));
        alertBean.setTargetGroup(UserGroup.of(Person.convertToUsers(persons)));
        alertBean.setFireDate(new LocalDate());
        new PhdCustomAlert(alertBean);
    }

    static public String getSubjectPrefixed(PhdIndividualProgramProcess process, AlertMessage message) {
        return (message.withPrefix() ? getProcessNumberPrefix(process) : "") + message.getMessage();
    }

    static public String getBodyText(PhdIndividualProgramProcess process, AlertMessage body) {
        return (body.withPrefix() ? getBodyCommonText(process) : "") + body.getMessage();
    }

    static public void alertParticipants(PhdIndividualProgramProcess process, AlertMessage subject, AlertMessage body,
            PhdParticipant... participants) {

        final Set<Person> toNotify = new HashSet<Person>();
        for (final PhdParticipant participant : participants) {
            if (participant.isInternal()) {
                toNotify.add(((InternalPhdParticipant) participant).getPerson());
            } else {
                Unit unit = process.getAdministrativeOffice().getUnit();
                UnitBasedSender sender = unit.getUnitBasedSender().iterator().next();
                new Message(sender, Collections.<ReplyTo> emptyList(), Collections.<Recipient> emptyList(), getSubjectPrefixed(
                        process, subject), getBodyText(process, body), Collections.singleton(participant.getEmail()));
            }
        }

        if (!toNotify.isEmpty()) {
            final PhdCustomAlertBean alertBean = new PhdCustomAlertBean(process, true, false, false);
            alertBean.setSubject(getSubjectPrefixed(process, subject));
            alertBean.setBody(getBodyText(process, body));
            alertBean.setTargetGroup(UserGroup.of(Person.convertToUsers(toNotify)));
            alertBean.setFireDate(new LocalDate());
            new PhdCustomAlert(alertBean);
        }
    }

    static public class AlertMessage {
        private String label;
        private Object[] args;
        private boolean isKey = true;
        private boolean withPrefix = true;

        public AlertMessage label(String label) {
            this.label = label;
            return this;
        }

        public AlertMessage args(Object... args) {
            this.args = args;
            return this;
        }

        public AlertMessage isKey(boolean value) {
            this.isKey = value;
            return this;
        }

        protected boolean withPrefix() {
            return withPrefix;
        }

        public AlertMessage withPrefix(boolean value) {
            withPrefix = value;
            return this;
        }

        public String getMessage() {
            return isKey ? MessageFormat.format(getMessageFromResource(label), args) : label;
        }

        static public AlertMessage create(String label, Object... args) {
            return new AlertMessage().label(label).args(args);
        }

        static public String get(String label, Object... args) {
            return new AlertMessage().label(label).args(args).getMessage();
        }
    }
}
