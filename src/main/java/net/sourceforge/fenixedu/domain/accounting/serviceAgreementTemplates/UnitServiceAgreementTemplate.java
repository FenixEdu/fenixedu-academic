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
package net.sourceforge.fenixedu.domain.accounting.serviceAgreementTemplates;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class UnitServiceAgreementTemplate extends UnitServiceAgreementTemplate_Base {

    private UnitServiceAgreementTemplate() {
        super();
    }

    public UnitServiceAgreementTemplate(final Unit unit) {
        this();
        init(unit);
    }

    private void init(Unit unit) {
        checkParameters(unit);
        super.setUnit(unit);

    }

    private void checkParameters(Unit unit) {
        if (unit == null) {
            throw new DomainException(
                    "error.accounting.serviceAgreementTemplates.UnitServiceAgreementTemplate.unit.cannot.be.null");
        }
    }

    @Override
    public void setUnit(Unit unit) {
        throw new DomainException("error.accounting.serviceAgreementTemplates.UnitServiceAgreementTemplate.cannot.modify.unit");
    }

    @Deprecated
    public boolean hasUnit() {
        return getUnit() != null;
    }

}
