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
package org.fenixedu.academic.dto.thesis;

import java.io.Serializable;
import java.util.Locale;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.person.HumanName;
import org.fenixedu.academic.domain.thesis.Thesis;
import org.fenixedu.academic.domain.thesis.ThesisLibraryState;
import org.fenixedu.bennu.core.domain.Bennu;

/**
 * Bean with fields to search by author, title, library reference, state or
 * execution year.
 * 
 * @author Pedro Santos (pmrsa)
 */
public class ThesisSearchBean implements Serializable {
    private static final long serialVersionUID = 4695377851319085267L;

    private String text;

    private ThesisLibraryState state;

    private ExecutionYear year;

    public ThesisSearchBean() {
        ExecutionYear last = null;
        for (Thesis thesis : Bennu.getInstance().getThesesSet()) {
            if (last == null || thesis.getEnrolment().getExecutionYear().isAfter(last)) {
                last = thesis.getEnrolment().getExecutionYear();
            }
        }
        setYear(last);
    }

    public ThesisSearchBean(String text, String state, String year) {
        this.text = text;
        if (state != null) {
            this.state = ThesisLibraryState.valueOf(state);
        }
        if (year != null) {
            setYear(ExecutionYear.readExecutionYearByName(year));
        }
    }

    public boolean isMatch(Thesis thesis) {
        if (state != null && state != thesis.getLibraryState()) {
            return false;
        }
        if (getYear() != null && !thesis.getEnrolment().getExecutionYear().equals(getYear())) {
            return false;
        }
        if (text != null && !text.isEmpty()) {
            if (thesis.getStudent().getNumber().toString().equals(text)) {
                return true;
            }
            if (isMatchPerson(thesis.getStudent().getPerson(), text)) {
                return true;
            }
            for (final Locale l : thesis.getFinalFullTitle().getLocales()) {
                final String title = thesis.getFinalFullTitle().getContent(l);
                if (title.toLowerCase().contains(text.toLowerCase())) {
                    return true;
                }
            }
            if (thesis.getLibraryReference() != null && thesis.getLibraryReference().contains(text)) {
                return true;
            }
            if (thesis.getLibraryOperationPerformer() != null && isMatchPerson(thesis.getLibraryOperationPerformer(), text)) {
                return true;
            }
            return false;
        }
        return true;
    }

    private boolean isMatchPerson(Person person, String text) {
        if (person.getUsername().equals(text)) {
            return true;
        }
        return HumanName.namesMatch(person.getName(), text);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ThesisLibraryState getState() {
        return state;
    }

    public void setState(ThesisLibraryState state) {
        this.state = state;
    }

    public ExecutionYear getYear() {
        return this.year;
    }

    public void setYear(ExecutionYear year) {
        this.year = year;
    }
}
