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
/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.candidacy;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.candidacy.CandidacySituationType;
import net.sourceforge.fenixedu.domain.candidacy.DFACandidacy;
import net.sourceforge.fenixedu.domain.util.workflow.StateBean;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class SelectDFACandidacyBean extends StateBean implements Serializable {

    private DFACandidacy candidacy;

    private String remarks;

    private Integer order;

    public SelectDFACandidacyBean(DFACandidacy candidacy) {
        super();
        if (candidacy != null) {
            this.candidacy = candidacy;
            if (candidacy.getActiveCandidacySituation().getCandidacySituationType().equals(CandidacySituationType.SUBSTITUTE)) {
                setSelectionSituation(CandidacySituationType.SUBSTITUTE);
            }
            if (candidacy.getActiveCandidacySituation().getCandidacySituationType().equals(CandidacySituationType.ADMITTED)) {
                setSelectionSituation(CandidacySituationType.ADMITTED);
            }
            if (candidacy.getActiveCandidacySituation().getCandidacySituationType().equals(CandidacySituationType.NOT_ADMITTED)) {
                setSelectionSituation(CandidacySituationType.NOT_ADMITTED);

            }
            this.remarks = candidacy.getActiveCandidacySituation().getRemarks();
        }
    }

    public DFACandidacy getCandidacy() {
        return this.candidacy;
    }

    public void setCandidacy(DFACandidacy candidacy) {
        this.candidacy = candidacy;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public CandidacySituationType getSelectionSituation() {
        return getNextState() == null ? null : CandidacySituationType.valueOf(getNextState());
    }

    public void setSelectionSituation(final CandidacySituationType selectionSituation) {
        setNextState(selectionSituation == null ? null : selectionSituation.name());
    }

}
