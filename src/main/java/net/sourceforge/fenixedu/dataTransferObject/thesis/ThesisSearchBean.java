package net.sourceforge.fenixedu.dataTransferObject.thesis;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.PersonNamePart;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisLibraryState;
import pt.ist.bennu.core.domain.Bennu;

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
            for (String title : thesis.getFinalFullTitle().getAllContents()) {
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
        if (person.getPersonName().match(PersonNamePart.getNameParts(text))) {
            return true;
        }
        return false;
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
