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
package net.sourceforge.fenixedu.domain.accounting.events;

import net.sourceforge.fenixedu.domain.accounting.Exemption;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.fenixedu.bennu.core.domain.Bennu;

import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public abstract class ExemptionJustification extends ExemptionJustification_Base {

    protected ExemptionJustification() {
        super();
        super.setRootDomainObject(Bennu.getInstance());
    }

    protected void init(final Exemption exemption, final String reason) {

        checkParameters(exemption);

        super.setExemption(exemption);
        super.setReason(reason);
    }

    private void checkParameters(Exemption exemption) {
        if (exemption == null) {
            throw new DomainException("error.accounting.events.ExemptionJustification.exemption.cannot.be.null");
        }
    }

    @Override
    public void setExemption(Exemption exemption) {
        throw new DomainException(
                "error.net.sourceforge.fenixedu.domain.accounting.events.ExemptionJustification.cannot.modify.exemption");
    }

    @Override
    public void setReason(String reason) {
        throw new DomainException(
                "error.net.sourceforge.fenixedu.domain.accounting.events.ExemptionJustification.cannot.modify.reason");
    }

    public void delete() {
        setRootDomainObject(null);
        super.setExemption(null);

        super.deleteDomainObject();
    }

    public void removeExemption() {
        super.setExemption(null);
    }

    abstract public LabelFormatter getDescription();

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasExemption() {
        return getExemption() != null;
    }

    @Deprecated
    public boolean hasReason() {
        return getReason() != null;
    }

}
