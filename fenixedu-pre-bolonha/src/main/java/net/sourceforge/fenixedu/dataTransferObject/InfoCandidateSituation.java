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
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Date;

import net.sourceforge.fenixedu.domain.CandidateSituation;
import net.sourceforge.fenixedu.util.SituationName;
import net.sourceforge.fenixedu.util.State;

/*
 * InfoCandidateSituation.java
 *
 * Created on 29 de Novembro de 2002, 15:57
 */

/**
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 * 
 */

public class InfoCandidateSituation extends InfoObject {

    private Date date = null; // Situation Date

    private String remarks = null; // Remarks

    private SituationName situation = null; // Situation

    private State validation = null;

    public InfoCandidateSituation() {
        situation = null;
        date = null;
        remarks = null;
        validation = null;
    }

    public InfoCandidateSituation(Date date, String remarks, String situation) {
        setSituation(new SituationName(situation));
        setDate(date);
        setRemarks(remarks);
    }

    public InfoCandidateSituation(CandidateSituation candidateSituation) {
        setSituation(candidateSituation.getSituation());
        setDate(candidateSituation.getDate());
        setRemarks(candidateSituation.getRemarks());
    }

    @Override
    public String toString() {
        String result = "DataBean Situacao do Candidato:\n";
        result += "\n  - Data : " + date;
        result += "\n  - Observacoes : " + remarks;
        result += "\n  - Situacao : " + situation;

        return result;
    }

    /**
     * Returns the date.
     * 
     * @return Date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Returns the remarks.
     * 
     * @return String
     */
    public String getRemarks() {
        return remarks;
    }

    public SituationName getSituation() {
        return situation;
    }

    /**
     * Sets the date.
     * 
     * @param date
     *            The date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Sets the remarks.
     * 
     * @param remarks
     *            The remarks to set
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     * Sets the situation.
     * 
     * @param situation
     *            The situation to set
     */
    public void setSituation(SituationName situation) {
        this.situation = situation;
    }

    /**
     * @return
     */
    public State getValidation() {
        return validation;
    }

    /**
     * @param validation
     */
    public void setValidation(State validation) {
        this.validation = validation;
    }

    public void copyFromDomain(CandidateSituation candidateSituation) {
        super.copyFromDomain(candidateSituation);
        if (candidateSituation != null) {
            setDate(candidateSituation.getDate());
            setRemarks(candidateSituation.getRemarks());
            setSituation(candidateSituation.getSituation());
            setValidation(candidateSituation.getValidation());
        }
    }

    public static InfoCandidateSituation newInfoFromDomain(CandidateSituation candidateSituation) {
        InfoCandidateSituation infoCandidateSituation = null;
        if (candidateSituation != null) {
            infoCandidateSituation = new InfoCandidateSituation();
            infoCandidateSituation.copyFromDomain(candidateSituation);
        }
        return infoCandidateSituation;
    }
}