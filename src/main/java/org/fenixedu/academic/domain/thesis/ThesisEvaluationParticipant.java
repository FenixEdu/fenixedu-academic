/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain.thesis;

import java.text.Collator;
import java.util.Comparator;
import java.util.Objects;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.fenixedu.academic.domain.DomainObjectUtil;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.signals.DomainObjectEvent;
import org.fenixedu.bennu.signals.Signal;

import pt.ist.fenixframework.dml.runtime.RelationAdapter;

public class ThesisEvaluationParticipant extends ThesisEvaluationParticipant_Base {

    private static KeepParticipationNumberAdapter KEEP_PARTICIPATION_NUMBER_ADAPTER = new KeepParticipationNumberAdapter();
    static {
        getRelationThesisHasParticipations().addListener(KEEP_PARTICIPATION_NUMBER_ADAPTER);
    }

    public final static Comparator<ThesisEvaluationParticipant> COMPARATOR_BY_PERSON_NAME = new ComparatorChain();
    static {
        ((ComparatorChain) COMPARATOR_BY_PERSON_NAME).addComparator(new BeanComparator("person.name", Collator.getInstance()));
        ((ComparatorChain) COMPARATOR_BY_PERSON_NAME).addComparator(DomainObjectUtil.COMPARATOR_BY_ID);
    }

    public final static Comparator<ThesisEvaluationParticipant> COMPARATOR_BY_STUDENT_NUMBER = new BeanComparator(
            "thesis.student.number");

    public ThesisEvaluationParticipant(Thesis thesis, Person person, ThesisParticipationType type) {
        super();
        Objects.requireNonNull(person);

        setRootDomainObject(Bennu.getInstance());

        setType(type);
        setThesis(thesis);
        setPerson(person);
        setPercentageDistribution(0);
        Signal.emit("academic.thesis.participant.created", new DomainObjectEvent<>(this));
    }

    public ThesisEvaluationParticipant(Thesis thesis, String name, String email, ThesisParticipationType type) {
        super();

        setRootDomainObject(Bennu.getInstance());

        setType(type);
        setThesis(thesis);
        setExternalPerson(new ThesisEvaluationExternalParticipant(name, email));
        setPercentageDistribution(0);
        Signal.emit("academic.thesis.participant.created", new DomainObjectEvent<>(this));
    }

    @Override
    public Person getPerson() {
        // FIXME remove when framework supports read-only slots
        return super.getPerson();
    }

    public void delete() {
        setRootDomainObject(null);
        setPerson(null);
        setThesis(null);
        setExternalPerson(null);

        deleteDomainObject();
    }

    @Override
    public void setType(ThesisParticipationType type) {
        super.setType(type);

        KEEP_PARTICIPATION_NUMBER_ADAPTER.changedType(this);
    }

    public static class KeepParticipationNumberAdapter extends RelationAdapter<Thesis, ThesisEvaluationParticipant> {

        @Override
        public void beforeAdd(Thesis o2, ThesisEvaluationParticipant o1) {
            super.beforeAdd(o2, o1);

            if (o1 != null && o2 != null) {
                keepTypeCount(o1, o2);
            }
        }

        public void changedType(ThesisEvaluationParticipant participant) {
            keepTypeCount(participant, participant.getThesis());
        }

        private void keepTypeCount(ThesisEvaluationParticipant participant, Thesis thesis) {
            if (thesis == null) {
                return;
            }

            ThesisParticipationType type = participant.getType();

            if (type == null) {
                return;
            }

            if (type.isSingle()) {
                ThesisEvaluationParticipant existing = thesis.getParticipant(type);

                if (existing != null && existing != participant) {
                    existing.delete();
                }
            }
        }

    }

    public boolean isExternal() {
        return getPerson() == null;
    }

    public String getName() {
        if (getPerson() != null) {
            return getPerson().getName();
        } else {
            return getExternalPerson() != null ? getExternalPerson().getName() : null;
        }
    }

    public String getEmail() {
        if (getPerson() != null) {
            return getPerson().getProfile().getEmail();
        } else {
            return getExternalPerson() != null ? getExternalPerson().getEmail() : null;
        }
    }
}
