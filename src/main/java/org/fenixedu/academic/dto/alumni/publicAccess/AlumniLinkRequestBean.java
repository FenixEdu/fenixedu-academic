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
package net.sourceforge.fenixedu.dataTransferObject.alumni.publicAccess;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Alumni;

public class AlumniLinkRequestBean implements Serializable {

    private Alumni alumni;
    private String documentIdNumber;
    private Integer studentNumber;
    private String email;
    private Boolean privacyPolicy;

    public AlumniLinkRequestBean() {
    }

    public AlumniLinkRequestBean(Alumni alumni) {
        setAlumni(alumni);
    }

    public String getDocumentIdNumber() {
        return documentIdNumber;
    }

    public void setDocumentIdNumber(String documentIdNumber) {
        this.documentIdNumber = documentIdNumber;
    }

    public Integer getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(Integer studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Alumni getAlumni() {
        return this.alumni;
    }

    public void setAlumni(Alumni alumni) {
        this.alumni = alumni;
    }

    public Boolean getPrivacyPolicy() {
        return privacyPolicy;
    }

    public void setPrivacyPolicy(Boolean privacyPolicy) {
        this.privacyPolicy = privacyPolicy;
    }
}
