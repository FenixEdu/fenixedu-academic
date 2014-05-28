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
package net.sourceforge.fenixedu.presentationTier.Action.coordinator.thesis;

import java.io.Serializable;

import net.sourceforge.fenixedu.applicationTier.Servico.thesis.ChangeThesisPerson.PersonTarget;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitName;
import net.sourceforge.fenixedu.domain.person.PersonName;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisEvaluationParticipant;

import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ThesisBean implements Serializable {

    /**
     * Serial version id.
     */
    private static final long serialVersionUID = 1L;

    private Student student;

    private PersonTarget targetType;
    private ThesisEvaluationParticipant target;

    private Degree degree;
    private boolean internal;
    private String rawPersonName;
    private PersonName personName;
    private UnitName unitName;
    private String rawUnitName;

    private MultiLanguageString title;
    private String comment;

    private String mark;
    private DateTime discussion;

    private Thesis thesis;

    public ThesisBean() {
        super();

        this.degree = null;
        this.student = null;
        this.personName = null;
        this.unitName = null;
        this.target = null;

        this.internal = true;
    }

    public ThesisBean(final Thesis thesis) {
        this();
        setThesis(thesis);
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
        PersonName personName = getPersonName();

        if (personName == null) {
            return null;
        } else {
            return personName.getPerson();
        }
    }

    public PersonName getPersonName() {
        return this.personName;
    }

    public boolean isInternal() {
        return this.internal;
    }

    public void setInternal(boolean internal) {
        this.internal = internal;
    }

    public void setPersonName(PersonName personName) {
        this.personName = personName;
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
