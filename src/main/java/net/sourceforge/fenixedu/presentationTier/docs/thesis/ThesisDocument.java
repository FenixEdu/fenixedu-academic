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
package net.sourceforge.fenixedu.presentationTier.docs.thesis;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisEvaluationParticipant;
import net.sourceforge.fenixedu.presentationTier.docs.FenixReport;

import org.fenixedu.bennu.core.domain.Bennu;

/**
 * Base document for Thesis related reports. This document tries to setup the
 * basic parameters for the student information, thesis title and the jury
 * elements. Subdocuments can then add or remove parameters to generate a
 * specific template.
 * 
 * @author cfgi
 */
public abstract class ThesisDocument extends FenixReport {

    private Thesis thesis;

    public ThesisDocument(Thesis thesis) {
        super();
        this.thesis = thesis;
        fillReport();
    }

    protected Thesis getThesis() {
        return this.thesis;
    }

    @Override
    protected void fillReport() {
        fillGeneric();
        fillInstitution();
        fillDegree();
        fillStudent();
        fillOrientation();
        fillThesisInfo();
        fillJury();
    }

    protected void fillGeneric() {
    }

    protected void fillInstitution() {
        addParameter("institutionName", neverNull(Bennu.getInstance().getInstitutionUnit().getName()).toUpperCase());
    }

    protected void fillDegree() {
        final Degree degree = thesis.getDegree();
        addParameter("studentDegreeName", neverNull(degree.getNameI18N(thesis.getExecutionYear()).toString()));
    }

    protected void fillStudent() {
        final Student student = thesis.getStudent();
        addParameter("studentNumber", student.getNumber());

        final Person person = student.getPerson();
        addParameter("studentName", person.getName());
    }

    protected void fillThesisInfo() {
        addParameter("thesisTitle", thesis.getTitle().getContent());
    }

    protected void fillOrientation() {
        final ThesisEvaluationParticipant orientator = thesis.getOrientator();
        addParameter("orientatorName", orientator.getPersonName());
        addParameter("orientatorCategory", participantCategoryName(orientator));
        addParameter("orientatorAffiliation", neverNull(orientator.getAffiliation()));

        final ThesisEvaluationParticipant coorientator = thesis.getCoorientator();
        if (coorientator != null) {
            addParameter("coorientatorName", coorientator.getPersonName());
            addParameter("coorientatorCategory", participantCategoryName(coorientator));
            addParameter("coorientatorAffiliation", neverNull(coorientator.getAffiliation()));
        } else {
            addParameter("coorientatorName", EMPTY_STR);
            addParameter("coorientatorCategory", EMPTY_STR);
            addParameter("coorientatorAffiliation", EMPTY_STR);
        }
    }

    protected void fillJury() {
        final ThesisEvaluationParticipant juryPresident = thesis.getPresident();
        addParameter("juryPresidentName", juryPresident.getPersonName());
        addParameter("juryPresidentCategory", participantCategoryName(juryPresident));
        addParameter("juryPresidentAffiliation", neverNull(juryPresident.getAffiliation()));

        final Set<ThesisEvaluationParticipant> vowels =
                new TreeSet<ThesisEvaluationParticipant>(ThesisEvaluationParticipant.COMPARATOR_BY_PERSON_NAME);
        vowels.addAll(thesis.getVowels());

        Iterator<ThesisEvaluationParticipant> iterator = vowels.iterator();
        int guidanceVowel = 0;
        for (int i = 1; i <= 4; i++) {
            final String vowelPrefix = "vowel" + i;

            if (iterator.hasNext()) {
                ThesisEvaluationParticipant vowel = iterator.next();
                if (guidanceVowel == 0 && isGuidanceVowel(vowel)) {
                    guidanceVowel = i;
                }
                addParameter(vowelPrefix + "Name", vowel.getPersonName());
                addParameter(vowelPrefix + "Category", participantCategoryName(vowel));
                addParameter(vowelPrefix + "Affiliation", neverNull(vowel.getAffiliation()));
            } else {
                addParameter(vowelPrefix + "Name", EMPTY_STR);
                addParameter(vowelPrefix + "Category", EMPTY_STR);
                addParameter(vowelPrefix + "Affiliation", EMPTY_STR);
            }
        }
        addParameter("guidanceVowel", guidanceVowel);
    }

    protected String neverNull(String value) {
        return value == null ? EMPTY_STR : value;
    }

    private boolean isGuidanceVowel(ThesisEvaluationParticipant vowel) {
        Person vowelPerson = vowel.getPerson();
        Person orientatorPerson = (thesis.getOrientator() != null) ? thesis.getOrientator().getPerson() : null;
        Person coorientatorPerson = (thesis.getCoorientator() != null) ? thesis.getCoorientator().getPerson() : null;
        return vowelPerson.equals(orientatorPerson) || vowelPerson.equals(coorientatorPerson);
    }

    private String participantCategoryName(ThesisEvaluationParticipant participant) {
        if (participant == null) {
            return EMPTY_STR;
        } else if (participant.getCategory() == null) {
            return EMPTY_STR;
        } else {
            return participant.getCategory();
        }
    }
}
