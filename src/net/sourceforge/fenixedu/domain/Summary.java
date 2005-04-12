/*
 * Created on 21/Jul/2003
 *
 * 
 */
package net.sourceforge.fenixedu.domain;

import java.util.Calendar;

import net.sourceforge.fenixedu.util.TipoAula;

/**
 * @author João Mota
 * @author Susana Fernandes
 * 
 * 21/Jul/2003 fenix-head Dominio
 *  
 */
public class Summary extends Summary_Base {
    private Calendar summaryDate;
    private Calendar summaryHour;
    private TipoAula summaryType;

    public Summary() {
    }

    public Summary(Integer summaryId) {
        setIdInternal(summaryId);
    }

    public Calendar getSummaryHour() {
        return summaryHour;
    }

    public void setSummaryHour(Calendar summaryHour) {
        this.summaryHour = summaryHour;
    }

    public TipoAula getSummaryType() {
        return summaryType;
    }

    public void setSummaryType(TipoAula summaryType) {
        this.summaryType = summaryType;
    }

    public Calendar getSummaryDate() {
        return summaryDate;
    }

    public void setSummaryDate(Calendar summaryDate) {
        this.summaryDate = summaryDate;
    }

    public boolean compareTo(Object obj) {
        boolean resultado = false;
        if (obj instanceof ISummary) {
            ISummary summary = (ISummary) obj;

            resultado = (summary != null) && this.getShift().equals(summary.getShift())
                    && this.getSummaryDate().equals(summary.getSummaryDate())
                    && this.getSummaryHour().equals(summary.getSummaryHour())
                    && this.getSummaryText().equals(summary.getSummaryText())
                    && this.getTitle().equals(summary.getTitle());
        }
        return resultado;
    }

}