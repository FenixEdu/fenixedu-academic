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
/*
 * Created on Oct 14, 2003
 *  
 */
package org.fenixedu.academic.dto;

import org.fenixedu.academic.domain.organizationalStructure.ExternalContract;

/**
 * @author Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author Nadir Tarmahomed (naat@mega.ist.utl.pt)
 * 
 */
public class InfoExternalPerson extends InfoObject {

    private final ExternalContract externalPersonDomainReference;

    public InfoExternalPerson(final ExternalContract externalPerson) {
        externalPersonDomainReference = externalPerson;
    }

    public static InfoExternalPerson newInfoFromDomain(final ExternalContract externalPerson) {
        return externalPerson == null ? null : new InfoExternalPerson(externalPerson);
    }

    private ExternalContract getExternalPerson() {
        return externalPersonDomainReference;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof InfoExternalPerson && getExternalPerson() == ((InfoExternalPerson) obj).getExternalPerson();
    }

    @Override
    public String getExternalId() {
        return getExternalPerson().getExternalId();
    }

    @Override
    public void setExternalId(String integer) {
        throw new Error("Method should not be called!");
    }

    /**
     * @return Returns the infoPerson.
     */
    public InfoPerson getInfoPerson() {
        return InfoPerson.newInfoFromDomain(getExternalPerson().getPerson());
    }

    /**
     * @return Returns the infoInstitution.
     */
    public InfoInstitution getInfoInstitution() {
        return InfoInstitution.newInfoFromDomain(getExternalPerson().getInstitutionUnit());
    }

}
