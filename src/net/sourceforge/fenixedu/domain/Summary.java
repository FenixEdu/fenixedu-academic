/*
 * Created on 21/Jul/2003
 *
 * 
 */
package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.ShiftType;

/**
 * @author João Mota
 * @author Susana Fernandes
 * 
 * 21/Jul/2003 fenix-head Dominio
 *  
 */
public class Summary extends Summary_Base {
    private ShiftType summaryType;

    public Summary() {
    }

    public Summary(Integer summaryId) {
        setIdInternal(summaryId);
    }

    public ShiftType getSummaryType() {
        return summaryType;
    }

    public void setSummaryType(ShiftType summaryType) {
        this.summaryType = summaryType;
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