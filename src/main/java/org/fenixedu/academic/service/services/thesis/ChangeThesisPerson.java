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
package org.fenixedu.academic.service.services.thesis;

import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.thesis.Thesis;
import org.fenixedu.academic.domain.thesis.ThesisEvaluationParticipant;
import org.fenixedu.academic.domain.thesis.ThesisParticipationType;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;

import pt.ist.fenixframework.Atomic;

public class ChangeThesisPerson {

    public static enum PersonTarget {
        orientator(ThesisParticipationType.ORIENTATOR), coorientator(ThesisParticipationType.COORIENTATOR), president(
                ThesisParticipationType.PRESIDENT), vowel(ThesisParticipationType.VOWEL);

        private ThesisParticipationType type;

        PersonTarget(ThesisParticipationType type) {
            this.type = type;
        }

        public ThesisParticipationType getType() {
            return type;
        }
    };

    public static class PersonChange {
        PersonTarget type;
        Person person;
        ThesisEvaluationParticipant target;

        public PersonChange(PersonTarget type, Person person, ThesisEvaluationParticipant target) {
            super();

            this.type = type;
            this.person = person;
            this.target = target;
        }
    }

    @Atomic
    public static void run(DegreeCurricularPlan degreeCurricularPlan, Thesis thesis, PersonChange change)
            throws FenixServiceException {
        ThesisParticipationType type = change.type.getType();

        if (type.isSingle()) {
            thesis.setParticipation(change.person, type);
        } else {
            if (change.target != null) {
                change.target.delete();
            }
            if (change.person != null) {
                thesis.addParticipant(change.person, type);
            }
        }
    }

    @Atomic
    public static void remove(final ThesisEvaluationParticipant thesisEvaluationParticipant) {
        thesisEvaluationParticipant.delete();
    }

    @Atomic
    public static void add(final Thesis thesis, final ThesisParticipationType thesisParticipationType, final Person person) {
        if (person != null) {
            new ThesisEvaluationParticipant(thesis, person, thesisParticipationType);
        }
    }

    @Atomic
    public static void addExternal(Thesis thesis, PersonTarget targetType, String externalName, String externalEmail) {
        thesis.addExternal(targetType.getType(), externalName, externalEmail);
    }

    @Atomic
    public static void addExternal(Thesis thesis, ThesisParticipationType thesisParticipationType, String externalName,
            String externalEmail) {
        thesis.addExternal(thesisParticipationType, externalName, externalEmail);
    }
}