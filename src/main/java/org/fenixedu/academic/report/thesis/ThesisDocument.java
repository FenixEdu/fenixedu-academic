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
package org.fenixedu.academic.report.thesis;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.domain.thesis.Thesis;
import org.fenixedu.academic.domain.thesis.ThesisEvaluationParticipant;
import org.fenixedu.academic.report.FenixReport;
import org.fenixedu.bennu.core.domain.Bennu;

import java.util.Optional;
import java.util.Set;

/**
 * Base document for Thesis related reports. This document tries to setup the
 * basic parameters for the student information, thesis title and the jury
 * elements. Subdocuments can then add or remove parameters to generate a
 * specific template.
 * 
 * @author cfgi
 */
public abstract class ThesisDocument extends FenixReport {

    private final Thesis thesis;

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
        getPayload().addProperty("institutionName", Optional.ofNullable(Bennu.getInstance().getInstitutionUnit().getName())
                .orElse(EMPTY_STR));
    }

    protected void fillDegree() {
        final Degree degree = thesis.getDegree();
        getPayload().addProperty("studentDegreeName",Optional.ofNullable(degree.getNameI18N(thesis.getExecutionYear()).getContent())
                .orElse(EMPTY_STR));
    }

    protected void fillStudent() {
        final Student student = thesis.getStudent();
        getPayload().addProperty("studentNumber", student.getNumber());

        final Person person = student.getPerson();
        getPayload().addProperty("studentName", person.getName());
    }

    protected void fillThesisInfo() {
        getPayload().addProperty("thesisTitle", thesis.getTitle().getContent());
    }

    protected void fillOrientation() {
        JsonArray result = new JsonArray();

        thesis.getOrientation().stream().map(o -> {
            JsonObject advisor = new JsonObject();
            advisor.addProperty("name", o.getName());
            advisor.addProperty("category", o.getCategory());
            advisor.addProperty("affiliation", o.getAffiliation());
            return advisor;
        }).forEach(result::add);

        getPayload().add("advisors", result);
    }

    protected void fillJury() {
        final ThesisEvaluationParticipant juryPresident = thesis.getPresident();
        getPayload().addProperty("juryPresidentName", juryPresident.getName());
        getPayload().addProperty("juryPresidentCategory", participantCategoryName(juryPresident));
        getPayload().addProperty("juryPresidentAffiliation", Optional.ofNullable(juryPresident.getAffiliation()).orElse(EMPTY_STR));

        JsonArray result = new JsonArray();

        boolean hasAdvisor = false;
        thesis.getVowels().stream().sorted(ThesisEvaluationParticipant.COMPARATOR_BY_PERSON_NAME).map(jm -> {
            JsonObject juryMember = new JsonObject();
            juryMember.addProperty("name", jm.getName());
            juryMember.addProperty("category", participantCategoryName(jm));
            juryMember.addProperty("affiliation", Optional.ofNullable(jm.getAffiliation()).orElse(EMPTY_STR));
            juryMember.addProperty("isAdvisor", !hasAdvisor && isAdvisor(jm));
            return juryMember;
        }).forEach(result::add);

        getPayload().add("members", result);
    }

    private boolean isAdvisor(ThesisEvaluationParticipant juryMember) {
        Person juryPerson = juryMember.getPerson();

        Set<Person> orientationPersons = thesis.getOrientationPersons();

        return juryPerson != null && orientationPersons.contains(juryPerson);
    }

    private String participantCategoryName(ThesisEvaluationParticipant participant) {
        if (participant == null || participant.getCategory() == null) {
            return EMPTY_STR;
        } else {
            return participant.getCategory();
        }
    }
}
