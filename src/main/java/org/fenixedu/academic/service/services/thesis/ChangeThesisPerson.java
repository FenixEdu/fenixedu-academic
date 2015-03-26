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
        orientator, coorientator, president, vowel
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
        Person person = change.person;

        switch (change.type) {
        case orientator:
            thesis.setOrientator(person);
            break;
        case coorientator:
            thesis.setCoorientator(person);
            break;
        case president:
            thesis.setPresident(person);
            break;
        case vowel:
            if (change.target != null) {
                change.target.delete();
                if (person != null) {
                    thesis.addVowel(person);
                }
            } else {
                if (person != null) {
                    thesis.addVowel(person);
                }
            }

            break;
        }
    }

    private static ThesisParticipationType getThesisEvaluationParticipantType(PersonTarget type) {
        if (type.equals(PersonTarget.orientator)) {
            return ThesisParticipationType.ORIENTATOR;
        }

        if (type.equals(PersonTarget.coorientator)) {
            return ThesisParticipationType.COORIENTATOR;
        }

        if (type.equals(PersonTarget.president)) {
            return ThesisParticipationType.PRESIDENT;
        }

        if (type.equals(PersonTarget.vowel)) {
            return ThesisParticipationType.VOWEL;
        }
        return null;
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
        thesis.addExternal(getThesisEvaluationParticipantType(targetType), externalName, externalEmail);
    }

    @Atomic
    public static void addExternal(Thesis thesis, ThesisParticipationType thesisParticipationType, String externalName,
            String externalEmail) {
        thesis.addExternal(thesisParticipationType, externalName, externalEmail);
    }
}