/*
 * Shift.java
 *
 * Created on 17 de Outubro de 2002, 19:28
 */

package net.sourceforge.fenixedu.domain;

/**
 * 
 * @author tfc130
 */
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class Shift extends Shift_Base {
    protected Integer ocupation;

    protected Double percentage;

    public Integer getOcupation() {
        return ocupation;
    }

    public void setOcupation(Integer ocupation) {
        this.ocupation = ocupation;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public String toString() {
        String result = "[TURNO";
        result += ", codigoInterno=" + this.getIdInternal();
        result += ", nome=" + getNome();
        result += ", tipo=" + getTipo();
        result += ", lotacao=" + getLotacao();
        result += ", chaveDisciplinaExecucao=" + getChaveDisciplinaExecucao();
        result += "]";
        return result;
    }

    public double hours() {
        double hours = 0;
        List lessons = this.getAssociatedLessons();
        for (int i = 0; i < lessons.size(); i++) {
            ILesson lesson = (ILesson) lessons.get(i);
            hours += lesson.hours();
        }
        return hours;
    }

    public void associateSchoolClass(ISchoolClass schoolClass) {
        if (schoolClass == null) {
            throw new NullPointerException();
        }
        if (!this.getAssociatedClasses().contains(schoolClass)) {
            this.getAssociatedClasses().add(schoolClass);
        }
        if (!schoolClass.getAssociatedShifts().contains(this)) {
            schoolClass.getAssociatedShifts().add(this);
        }
    }

    public void transferSummary(ISummary summary, Date summaryDate, Date summaryHour, IRoom room) {
        checkIfSummaryExistFor(summaryDate, summaryHour);
        summary.modifyShift(this, summaryDate, summaryHour, room);
    }

    private void checkIfSummaryExistFor(final Date summaryDate, final Date summaryHour) {
        final Iterator associatedSummaries = getAssociatedSummariesIterator();
        while (associatedSummaries.hasNext()) {
            ISummary summary = (ISummary) associatedSummaries.next();
            if (summary.getSummaryDate().equals(summaryDate)
                    && summary.getSummaryHour().equals(summaryHour)) {
                throw new DomainException(this.getClass().getName(), "error.summary.already.exists");
            }
        }
        return;
    }

}
