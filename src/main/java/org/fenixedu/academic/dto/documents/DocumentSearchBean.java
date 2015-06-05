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
package org.fenixedu.academic.dto.documents;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.documents.GeneratedDocumentType;
import org.fenixedu.academic.dto.person.PersonBean;
import org.joda.time.LocalDate;

/**
 * @author Pedro Santos (pmrsa)
 */
public class DocumentSearchBean implements Serializable {
    private static final long serialVersionUID = 1360357466011500899L;

    private PersonBean addressee = new PersonBean();

    private PersonBean operator = new PersonBean();

    private GeneratedDocumentType type;

    private LocalDate creationDate = new LocalDate();

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

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }
}
