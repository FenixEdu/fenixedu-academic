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
package net.sourceforge.fenixedu.domain.phd.access;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.phd.PhdParticipant;
import net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean;

public class PhdExternalOperationBean implements Serializable {

    static private final long serialVersionUID = 1L;

    private PhdParticipant participant;
    private PhdProgramDocumentUploadBean documentBean;

    private String email;
    private String password;

    private PhdProcessAccessType accessType;

    public PhdExternalOperationBean(PhdParticipant participant, PhdProcessAccessType accessType) {
        setParticipant(participant);
        setAccessType(accessType);
    }

    public PhdParticipant getParticipant() {
        return participant;
    }

    public void setParticipant(PhdParticipant participant) {
        this.participant = participant;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public PhdProgramDocumentUploadBean getDocumentBean() {
        return documentBean;
    }

    public void setDocumentBean(PhdProgramDocumentUploadBean documentBean) {
        this.documentBean = documentBean;
    }

    public PhdProcessAccessType getAccessType() {
        return accessType;
    }

    public PhdExternalOperationBean setAccessType(final PhdProcessAccessType accessType) {
        this.accessType = accessType;
        return this;
    }

}
