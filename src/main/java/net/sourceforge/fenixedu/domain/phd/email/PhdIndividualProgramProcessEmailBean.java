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
package net.sourceforge.fenixedu.domain.phd.email;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdParticipant;
import net.sourceforge.fenixedu.domain.phd.alert.AlertService.AlertMessage;
import net.sourceforge.fenixedu.domain.phd.thesis.ThesisJuryElement;

import org.apache.commons.lang.StringUtils;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class PhdIndividualProgramProcessEmailBean extends PhdEmailBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -820152568474219538L;

    public static enum PhdEmailTemplate {
        FINAL_THESIS_DELIVERY("message.phd.template.final.thesis.delivery"),

        FINAL_THESIS_DELIVERY_WITH_CHANGES("message.phd.template.final.thesis.delivery.changes"),

        FINAL_THESIS_DELIVERY_AFTER_DISCUSSION("message.phd.template.final.thesis.delivery.after.discussion");

        private String label;

        private PhdEmailTemplate(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

        public String getLabelForSubject() {
            return label + ".subject";
        }

        public String getLabelForBody() {
            return label + ".body";
        }

        public String getTemplateSubject() {
            return AlertMessage.get(getLabelForSubject());
        }

        public String getTemplateBody() {
            return AlertMessage.get(getLabelForBody());
        }

        @Override
        public String toString() {
            return AlertMessage.get(getLabel() + ".label");
        }
    }

    public static abstract class PhdEmailParticipantsGroup implements Serializable, DataProvider {

        private static final long serialVersionUID = -8990666659412753954L;
        protected String label;

        public PhdEmailParticipantsGroup() {
        }

        public String getGroupLabel() {
            return AlertMessage.get(this.label);
        }

        public String getName() {
            return this.getClass().getSimpleName();
        }

        public abstract Collection<PhdParticipant> getGroupParticipants(PhdIndividualProgramProcess process);

        public String getEmailsAsBccs(PhdIndividualProgramProcess process) {
            StringBuilder bccs = new StringBuilder();
            Boolean hasParticipantsEmails = false;
            for (PhdParticipant participant : this.getGroupParticipants(process)) {
                String email = participant.getEmail();
                if (email == null) {
                    continue;
                }

                hasParticipantsEmails = true;
                bccs.append(email);
                bccs.append(",");
            }

            if (hasParticipantsEmails) {
                bccs.deleteCharAt(bccs.length() - 1);
            }

            return bccs.toString();
        }

        @Override
        public Converter getConverter() {
            return null;
        }

        @Override
        public Object provide(final Object source, final Object currentValue) {
            final PhdIndividualProgramProcessEmailBean emailBean = (PhdIndividualProgramProcessEmailBean) source;
            return this.getGroupParticipants(emailBean.getProcess());
        }

    }

    public static class PhdEmailParticipantsCoordinatorsGroup extends PhdEmailParticipantsGroup {

        private static final long serialVersionUID = 4961478244113914645L;

        public PhdEmailParticipantsCoordinatorsGroup() {
            super();
            super.label = "label.phd.email.group.coordinators";
        }

        @Override
        public List<PhdParticipant> getGroupParticipants(PhdIndividualProgramProcess process) {
            List<PhdParticipant> participants = new ArrayList<PhdParticipant>();
            for (Person person : process.getCoordinatorsFor(ExecutionYear.readCurrentExecutionYear())) {
                participants.add(process.getParticipant(person));
            }

            return participants;
        }

    }

    public static class PhdEmailParticipantsGuidersGroup extends PhdEmailParticipantsGroup {

        private static final long serialVersionUID = -3022014810736464210L;

        public PhdEmailParticipantsGuidersGroup() {
            super();
            super.label = "label.phd.email.group.guiders";
        }

        @Override
        public List<PhdParticipant> getGroupParticipants(PhdIndividualProgramProcess process) {
            List<PhdParticipant> participants = new ArrayList<PhdParticipant>();
            participants.addAll(process.getGuidingsAndAssistantGuidings());

            return participants;
        }

    }

    public static class PhdEmailParticipantsAllGroup extends PhdEmailParticipantsGroup {

        private static final long serialVersionUID = -6806003598437992476L;

        public PhdEmailParticipantsAllGroup() {
            super();
            super.label = "label.phd.email.group.all.participants";
        }

        @Override
        public Collection<PhdParticipant> getGroupParticipants(PhdIndividualProgramProcess process) {
            return process.getParticipants();
        }

    }

    public static class PhdEmailParticipantsJuryMembersGroup extends PhdEmailParticipantsGroup {

        private static final long serialVersionUID = 3417426578342610353L;

        public PhdEmailParticipantsJuryMembersGroup() {
            super();
            super.label = "label.phd.email.group.jury.members";
        }

        @Override
        public List<PhdParticipant> getGroupParticipants(PhdIndividualProgramProcess process) {
            List<PhdParticipant> participants = new ArrayList<PhdParticipant>();
            if (!process.hasThesisProcess()) {
                throw new DomainException("phd.individualProcess.does.not.have.thesisProcess");
            }

            if (process.getThesisProcess().hasPresidentJuryElement()) {
                participants.add(process.getThesisProcess().getPresidentJuryElement().getParticipant());
            }

            for (ThesisJuryElement element : process.getThesisProcess().getThesisJuryElements()) {
                participants.add(element.getParticipant());
            }

            return participants;
        }

    }

    private PhdIndividualProgramProcess process;

    private PhdEmailTemplate template;
    private Set<PhdEmailParticipantsGroup> participantsGroup;
    private Set<PhdParticipant> selectedParticipants;

    public PhdIndividualProgramProcessEmailBean() {
    }

    public PhdIndividualProgramProcessEmailBean(PhdIndividualProgramProcessEmail email) {
        this.subject = email.getFormattedSubject().getContent(MultiLanguageString.pt);
        this.message = email.getFormattedBody().getContent(MultiLanguageString.pt);
        this.bccs = email.getBccs();
        this.creationDate = email.getWhenCreated();
        this.creator = email.getPerson();
        this.process = email.getPhdIndividualProgramProcess();
    }

    public String getBccsWithSelectedParticipants() {
        String bccs = getBccs() == null ? null : getBccs().replace(" ", "");

        if (!StringUtils.isEmpty(bccs)) {
            bccs += ",";
        }

        for (PhdParticipant participant : getSelectedParticipants()) {
            bccs += participant.getEmail();
            bccs += ",";
        }
        if (bccs.endsWith(",")) {
            bccs = bccs.substring(0, bccs.length() - 1);
        }

        return bccs;
    }

    public PhdIndividualProgramProcess getProcess() {
        return process;
    }

    public void setProcess(PhdIndividualProgramProcess process) {
        this.process = process;
    }

    public PhdEmailTemplate getTemplate() {
        return template;
    }

    public void setTemplate(PhdEmailTemplate template) {
        this.template = template;
    }

    @Override
    public Person getCreator() {
        return creator;
    }

    @Override
    public void setCreator(Person creator) {
        this.creator = creator;
    }

    public Set<PhdEmailParticipantsGroup> getParticipantsGroup() {
        final Set<PhdEmailParticipantsGroup> result = new TreeSet<PhdEmailParticipantsGroup>(COMPARATOR_BY_NAME);
        if (this.participantsGroup != null) {
            for (final PhdEmailParticipantsGroup participantGroup : participantsGroup) {
                result.add(participantGroup);
            }
        }
        return result;
    }

    public void setParticipantsGroup(List<PhdEmailParticipantsGroup> participantsGroup) {
        if (participantsGroup == null) {
            this.participantsGroup = null;
        } else {
            this.participantsGroup = new HashSet<PhdEmailParticipantsGroup>();
            for (final PhdEmailParticipantsGroup participantGroup : participantsGroup) {
                this.participantsGroup.add(participantGroup);
            }
        }
    }

    public static final Comparator<PhdEmailParticipantsGroup> COMPARATOR_BY_NAME = new Comparator<PhdEmailParticipantsGroup>() {

        @Override
        public int compare(PhdEmailParticipantsGroup g1, PhdEmailParticipantsGroup g2) {
            return g1.getGroupLabel().compareTo(g2.getGroupLabel());
        }

    };

    public Set<PhdEmailParticipantsGroup> getPossibleParticipantsGroups() {
        final Set<PhdEmailParticipantsGroup> groups = new TreeSet<PhdEmailParticipantsGroup>(COMPARATOR_BY_NAME);

        groups.add(new PhdEmailParticipantsCoordinatorsGroup());
        if (getProcess().hasAnyGuidings()) {
            groups.add(new PhdEmailParticipantsGuidersGroup());
        }
        groups.add(new PhdEmailParticipantsAllGroup());
        if (getProcess().hasThesisProcess()) {
            groups.add(new PhdEmailParticipantsJuryMembersGroup());
        }

        return groups;
    }

    public List<PhdEmailParticipantsGroup> getPossibleParticipantsGroupsList() {
        List<PhdEmailParticipantsGroup> groups = new ArrayList<PhdEmailParticipantsGroup>();

        groups.add(new PhdEmailParticipantsCoordinatorsGroup());
        groups.add(new PhdEmailParticipantsGuidersGroup());
        groups.add(new PhdEmailParticipantsAllGroup());

        if (getProcess().hasThesisProcess()) {
            groups.add(new PhdEmailParticipantsJuryMembersGroup());
        }

        return groups;
    }

    public List<PhdParticipant> getSelectedParticipants() {
        final List<PhdParticipant> result = new ArrayList<PhdParticipant>();
        if (this.selectedParticipants != null) {
            for (final PhdParticipant participant : selectedParticipants) {
                result.add(participant);
            }
        }
        return result;
    }

    public void setSelectedParticipants(List<PhdParticipant> selectedParticipants) {
        if (selectedParticipants == null) {
            this.selectedParticipants = null;
        } else {
            this.selectedParticipants = new HashSet<PhdParticipant>();
            for (final PhdParticipant participant : selectedParticipants) {
                this.selectedParticipants.add(participant);
            }
        }
    }

    public void refreshTemplateInUse() {
        if (getTemplate() != null) {
            setSubject(getTemplate().getTemplateSubject());
            setMessage(getTemplate().getTemplateBody());
        } else {
            setSubject("");
            setMessage("");
        }
    }

}
