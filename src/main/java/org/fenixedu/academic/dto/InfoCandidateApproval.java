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

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class InfoCandidateApproval extends InfoObject {

    protected String externalId;

    protected Integer orderPosition;

    protected String candidateName;

    protected String remarks;

    protected String situationName;

    public InfoCandidateApproval() {
    }

    /**
     * @return
     */
    public String getCandidateName() {
        return candidateName;
    }

    /**
     * @return
     */
    @Override
    public String getExternalId() {
        return externalId;
    }

    /**
     * @return
     */
    public Integer getOrderPosition() {
        return orderPosition;
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
    public String getSituationName() {
        return situationName;
    }

    /**
     * @param string
     */
    public void setCandidateName(String string) {
        candidateName = string;
    }

    /**
     * @param integer
     */
    @Override
    public void setExternalId(String integer) {
        externalId = integer;
    }

    /**
     * @param integer
     */
    public void setOrderPosition(Integer integer) {
        orderPosition = integer;
    }

    /**
     * @param string
     */
    public void setRemarks(String string) {
        remarks = string;
    }

    /**
     * @param string
     */
    public void setSituationName(String string) {
        situationName = string;
    }

}