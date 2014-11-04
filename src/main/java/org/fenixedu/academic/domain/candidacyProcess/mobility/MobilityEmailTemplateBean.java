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
package net.sourceforge.fenixedu.domain.candidacyProcess.mobility;

import java.io.Serializable;

public class MobilityEmailTemplateBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private MobilityEmailTemplateType type;
    private String subject;
    private String body;
    private MobilityProgram mobilityProgram;

    public MobilityEmailTemplateBean() {

    }

    public MobilityEmailTemplateBean(final MobilityEmailTemplateType type) {
        setType(type);
    }

    public MobilityEmailTemplateType getType() {
        return type;
    }

    public void setType(MobilityEmailTemplateType type) {
        this.type = type;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public MobilityProgram getMobilityProgram() {
        return mobilityProgram;
    }

    public void setMobilityProgram(MobilityProgram mobilityProgram) {
        this.mobilityProgram = mobilityProgram;
    }

}
