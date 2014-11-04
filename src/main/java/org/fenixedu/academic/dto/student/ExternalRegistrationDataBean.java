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
package net.sourceforge.fenixedu.dataTransferObject.student;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitName;
import net.sourceforge.fenixedu.domain.student.ExternalRegistrationData;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ExternalRegistrationDataBean implements Serializable {

    private Unit institution;

    private ExternalRegistrationData externalRegistrationData;

    private String institutionName;

    private String coordinatorName;

    public ExternalRegistrationDataBean(ExternalRegistrationData externalRegistrationData) {
        super();
        this.externalRegistrationData = externalRegistrationData;
        this.institution = (externalRegistrationData.getInstitution() == null) ? null : externalRegistrationData.getInstitution();
        setCoordinatorName(externalRegistrationData.getCoordinatorName());
    }

    public String getCoordinatorName() {
        return coordinatorName;
    }

    public void setCoordinatorName(String coordinatorName) {
        this.coordinatorName = coordinatorName;
    }

    public Unit getInstitution() {
        return institution;
    }

    public void setInstitution(Unit institution) {
        this.institution = institution;
    }

    public UnitName getInstitutionUnitName() {
        return (institution == null) ? null : institution.getUnitName();
    }

    public void setInstitutionUnitName(UnitName institutionUnitName) {
        this.institution = (institution == null) ? null : institutionUnitName.getUnit();
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public ExternalRegistrationData getExternalRegistrationData() {
        return externalRegistrationData;
    }

}
