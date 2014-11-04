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
/*
 * Created on 12/Nov/2003
 * 
 */
package org.fenixedu.academic.dto.guide.reimbursementGuide;

import java.util.Calendar;

import org.fenixedu.academic.domain.gratuity.ReimbursementGuideState;
import org.fenixedu.academic.domain.reimbursementGuide.ReimbursementGuideSituation;
import org.fenixedu.academic.dto.InfoObject;
import org.fenixedu.academic.util.State;

/**
 * 
 * 
 * This class contains all the information regarding a Reimbursement Guide. <br/>
 * 
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota </a>
 * 
 */
public class InfoReimbursementGuideSituation extends InfoObject {

    protected State state;

    protected InfoReimbursementGuide infoReimbursementGuide;

    protected String remarks;

    protected Calendar modificationDate;

    protected Calendar officialDate;

    protected ReimbursementGuideState reimbursementGuideState;

    /**
     * @return
     */
    public InfoReimbursementGuide getInfoReimbursementGuide() {
        return infoReimbursementGuide;
    }

    /**
     * @param infoReimbursementGuide
     */
    public void setInfoReimbursementGuide(InfoReimbursementGuide infoReimbursementGuide) {
        this.infoReimbursementGuide = infoReimbursementGuide;
    }

    /**
     * @return
     */
    public Calendar getModificationDate() {
        return modificationDate;
    }

    /**
     * @param modificationDate
     */
    public void setModificationDate(Calendar modificationDate) {
        this.modificationDate = modificationDate;
    }

    /**
     * @return
     */
    public ReimbursementGuideState getReimbursementGuideState() {
        return reimbursementGuideState;
    }

    /**
     * @param reimbursementGuideState
     */
    public void setReimbursementGuideState(ReimbursementGuideState reimbursementGuideState) {
        this.reimbursementGuideState = reimbursementGuideState;
    }

    /**
     * @return
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * @param remarks
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     * @return
     */
    public State getState() {
        return state;
    }

    /**
     * @param state
     */
    public void setState(State state) {
        this.state = state;
    }

    /**
     * @return Returns the officialDate.
     */
    public Calendar getOfficialDate() {
        return officialDate;
    }

    /**
     * @param officialDate
     *            The officialDate to set.
     */
    public void setOfficialDate(Calendar officialDate) {
        this.officialDate = officialDate;
    }

    public void copyFromDomain(ReimbursementGuideSituation reimbursementGuideSituation) {
        super.copyFromDomain(reimbursementGuideSituation);
        if (reimbursementGuideSituation != null) {

            setModificationDate(reimbursementGuideSituation.getModificationDate());
            setOfficialDate(reimbursementGuideSituation.getOfficialDate());
            setReimbursementGuideState(reimbursementGuideSituation.getReimbursementGuideState());
            setRemarks(reimbursementGuideSituation.getRemarks());
            setState(reimbursementGuideSituation.getState());

        }
    }

    public static InfoReimbursementGuideSituation newInfoFromDomain(ReimbursementGuideSituation reimbursementGuideSituation) {
        InfoReimbursementGuideSituation infoReimbursementGuideSituation = null;
        if (reimbursementGuideSituation != null) {
            infoReimbursementGuideSituation = new InfoReimbursementGuideSituation();
            infoReimbursementGuideSituation.copyFromDomain(reimbursementGuideSituation);
        }

        return infoReimbursementGuideSituation;
    }

}