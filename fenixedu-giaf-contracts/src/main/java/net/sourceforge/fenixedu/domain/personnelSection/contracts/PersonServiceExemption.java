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
package org.fenixedu.academic.domain.personnelSection.contracts;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class PersonServiceExemption extends PersonServiceExemption_Base {

    public PersonServiceExemption(final GiafProfessionalData giafProfessionalData, final LocalDate beginDate,
            final LocalDate endDate, final ServiceExemption serviceExemption, final String serviceExemptionGiafId,
            final DateTime creationDate, final DateTime modifiedDate) {
        super();
        setRootDomainObject(Bennu.getInstance());
        setGiafProfessionalData(giafProfessionalData);
        setBeginDate(beginDate);
        setEndDate(endDate);
        setServiceExemption(serviceExemption);
        setServiceExemptionGiafId(serviceExemptionGiafId);
        setCreationDate(creationDate);
        setModifiedDate(modifiedDate);
        setImportationDate(new DateTime());
    }

    @Override
    public boolean isValid() {
        return getServiceExemption() != null && getBeginDate() != null && getEndDate() != null
                && !getBeginDate().isAfter(getEndDate());
    }

    @Override
    public boolean getIsSabaticalOrEquivalent() {
        return getServiceExemption().getIsSabaticalOrEquivalent();
    }

    @Override
    public boolean getHasMandatoryCredits() {
        return getServiceExemption().getHasMandatoryCredits();
    }

    @Override
    public boolean getGiveCredits() {
        return getServiceExemption().getGiveCredits();
    }

}
