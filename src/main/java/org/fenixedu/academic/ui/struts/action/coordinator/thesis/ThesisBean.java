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
package org.fenixedu.academic.ui.struts.action.coordinator.thesis;

import java.io.Serializable;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.organizationalStructure.UnitName;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.domain.thesis.Thesis;
import org.fenixedu.academic.domain.thesis.ThesisEvaluationParticipant;
import org.fenixedu.academic.service.services.thesis.ChangeThesisPerson.PersonTarget;
import org.fenixedu.academic.util.MultiLanguageString;
import org.joda.time.DateTime;

public class ThesisBean implements Serializable {

    /**
     * Serial version id.
     */
    private static final long serialVersionUID = 1L;

    private Student student;

    private PersonTarget targetType;
    private ThesisEvaluationParticipant target;

    private Degree degree;
    private String rawPersonName;
    private Person person;
    private UnitName unitName;
    private String rawUnitName;

    private MultiLanguageString title;
    private String comment;

    private String mark;
    private DateTime discussion;

    private Thesis thesis;

    private String externalName;
    private String externalEmail;

    public ThesisBean() {
        super();

        this.degree = null;
        this.student = null;
        this.person = null;
        this.unitName = null;
        this.target = null;
    }

    public ThesisBean(final Thesis thesis) {
        this();
        setThesis(thesis);
    }

    public String getExternalName() {
        return externalName;
    }

    public void setExternalName(String externalName) {
        this.externalName = externalName;
    }

    public String getExternalEmail() {
        return externalEmail;
    }

    public void setExternalEmail(String externalEmail) {
        this.externalEmail = externalEmail;
    }

    public Degree getDegree() {
        return this.degree;
    }

    public void setDegree(Degree degree) {
        this.degree = degree;
    }

    public Student getStudent() {
        return this.student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public PersonTarget getTargetType() {
        return this.targetType;
    }

    public void setTargetType(PersonTarget target) {
        this.targetType = target;
    }

    public ThesisEvaluationParticipant getTarget() {
        return this.target;
    }

    public void setTarget(ThesisEvaluationParticipant target) {
        this.target = target;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Unit getUnit() {
        UnitName unitName = getUnitName();

        if (unitName == null) {
            return null;
        } else {
            return unitName.getUnit();
        }
    }

    public UnitName getUnitName() {
        return this.unitName;
    }

    public void setUnitName(UnitName unitName) {
        this.unitName = unitName;
    }

    public String getRawPersonName() {
        return this.rawPersonName;
    }

    public void setRawPersonName(String rawPersonName) {
        this.rawPersonName = rawPersonName;
    }

    public String getRawUnitName() {
        return this.rawUnitName;
    }

    public void setRawUnitName(String rawUnitName) {
        this.rawUnitName = rawUnitName;
    }

    public MultiLanguageString getTitle() {
        return this.title;
    }

    public void setTitle(MultiLanguageString title) {
        this.title = title;
    }

    public MultiLanguageString getFinalTitle() {
        return getTitle();
    }

    public void setFinalTitle(MultiLanguageString title) {
        setTitle(title);
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public DateTime getDiscussion() {
        return this.discussion;
    }

    public void setDiscussion(DateTime discussion) {
        this.discussion = discussion;
    }

    public String getMark() {
        return this.mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public Thesis getThesis() {
        return thesis;
    }

    public void setThesis(final Thesis thesis) {
        this.thesis = thesis;
    }

}
