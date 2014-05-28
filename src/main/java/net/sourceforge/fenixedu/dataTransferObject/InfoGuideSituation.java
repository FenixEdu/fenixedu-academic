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

import net.sourceforge.fenixedu.domain.GuideSituation;
import net.sourceforge.fenixedu.domain.GuideState;
import net.sourceforge.fenixedu.util.State;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class InfoGuideSituation extends InfoObject {

    protected String remarks;

    protected GuideState situation;

    protected Date date;

    protected State state;

    protected InfoGuide infoGuide;

    public InfoGuideSituation() {
    }

    public InfoGuideSituation(String remarks, GuideState situationOfGuide, Date date, State state, InfoGuide infoGuide) {
        this.remarks = remarks;
        this.situation = situationOfGuide;
        this.date = date;
        this.state = state;
        this.infoGuide = infoGuide;
    }

    @Override
    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof InfoGuideSituation) {
            InfoGuideSituation infoGuideSituation = (InfoGuideSituation) obj;

            resultado =
                    getInfoGuide().equals(infoGuideSituation.getInfoGuide())
                            && getSituation().equals(infoGuideSituation.getSituation());
        }

        return resultado;
    }

    @Override
    public String toString() {
        String result = "[GUIDE SITUATION";
        result += ", remarks=" + remarks;
        result += ", guide=" + infoGuide;
        result += ", guide Situtation=" + situation;
        result += ", date=" + date;
        result += ", state=" + state;
        result += "]";
        return result;
    }

    /**
     * @return
     */
    public Date getDate() {
        return date;
    }

    /**
     * @return
     */
    public InfoGuide getInfoGuide() {
        return infoGuide;
    }

    /**
     * @return
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * @return
     */
    public GuideState getSituation() {
        return situation;
    }

    /**
     * @return
     */
    public State getState() {
        return state;
    }

    /**
     * @param date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @param guide
     */
    public void setInfoGuide(InfoGuide guide) {
        infoGuide = guide;
    }

    /**
     * @param string
     */
    public void setRemarks(String string) {
        remarks = string;
    }

    /**
     * @param guide
     */
    public void setSituation(GuideState guide) {
        situation = guide;
    }

    /**
     * @param state
     */
    public void setState(State state) {
        this.state = state;
    }

    public void copyFromDomain(GuideSituation guideSituation) {
        super.copyFromDomain(guideSituation);
        if (guideSituation != null) {
            setDate(guideSituation.getDate());
            setRemarks(guideSituation.getRemarks());
            setSituation(guideSituation.getSituation());
            setState(guideSituation.getState());
        }
    }

    public static InfoGuideSituation newInfoFromDomain(GuideSituation guideSituation) {
        InfoGuideSituation infoGuideSituation = null;
        if (guideSituation != null) {
            infoGuideSituation = new InfoGuideSituation();
            infoGuideSituation.copyFromDomain(guideSituation);
        }

        return infoGuideSituation;
    }

}