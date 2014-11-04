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
package net.sourceforge.fenixedu.dataTransferObject.documents;

import java.io.Serializable;

import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.documents.GeneratedDocumentType;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;

/**
 * @author Pedro Santos (pmrsa)
 */
public class DocumentSearchBean implements Serializable {
    private static final long serialVersionUID = 1360357466011500899L;

    private PersonBean addressee = new PersonBean();

    private PersonBean operator = new PersonBean();

    private GeneratedDocumentType type;

    private LocalDate uploadTime = new LocalDate();

    public PersonBean getAddressee() {
        return addressee;
    }

    public void setAddressee(PersonBean addressee) {
        this.addressee = addressee;
    }

    public boolean hasAddressee() {
        return StringUtils.isNotEmpty(getAddressee().getUsername()) || StringUtils.isNotEmpty(getAddressee().getName())
                || StringUtils.isNotEmpty(getAddressee().getDocumentIdNumber());
    }

    public PersonBean getOperator() {
        return operator;
    }

    public void setOperator(PersonBean operator) {
        this.operator = operator;
    }

    public boolean hasOperator() {
        return StringUtils.isNotEmpty(getOperator().getUsername()) || StringUtils.isNotEmpty(getOperator().getName())
                || StringUtils.isNotEmpty(getOperator().getDocumentIdNumber());
    }

    public GeneratedDocumentType getType() {
        return type;
    }

    public void setType(GeneratedDocumentType type) {
        this.type = type;
    }

    public LocalDate getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(LocalDate uploadTime) {
        this.uploadTime = uploadTime;
    }
}
