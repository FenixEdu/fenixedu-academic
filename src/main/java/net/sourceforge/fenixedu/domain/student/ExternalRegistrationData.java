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
package net.sourceforge.fenixedu.domain.student;

import net.sourceforge.fenixedu.dataTransferObject.student.ExternalRegistrationDataBean;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ExternalRegistrationData extends ExternalRegistrationData_Base {

    public ExternalRegistrationData(Registration registration) {
        super();
        setRootDomainObject(Bennu.getInstance());
        setRegistration(registration);
    }

    public void edit(ExternalRegistrationDataBean externalRegistrationDataBean) {

        Unit institution = externalRegistrationDataBean.getInstitution();
        if (institution == null && !StringUtils.isEmpty(externalRegistrationDataBean.getInstitutionName())) {
            institution = UnitUtils.readExternalInstitutionUnitByName(externalRegistrationDataBean.getInstitutionName());
            if (institution == null) {
                institution = Unit.createNewNoOfficialExternalInstitution(externalRegistrationDataBean.getInstitutionName());
            }
        }

        setInstitution(institution);
        setCoordinatorName(externalRegistrationDataBean.getCoordinatorName());
    }

    public void delete() {
        setRootDomainObject(null);
        setInstitution(null);
        setRegistration(null);
        super.deleteDomainObject();
    }

    @Deprecated
    public boolean hasRegistration() {
        return getRegistration() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasCoordinatorName() {
        return getCoordinatorName() != null;
    }

    @Deprecated
    public boolean hasInstitution() {
        return getInstitution() != null;
    }

}
