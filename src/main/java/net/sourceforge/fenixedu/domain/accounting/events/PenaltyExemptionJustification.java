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

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.fenixedu.bennu.core.domain.Bennu;

import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public class PenaltyExemptionJustification extends PenaltyExemptionJustification_Base {

    protected PenaltyExemptionJustification() {
        super();
        super.setRootDomainObject(Bennu.getInstance());
    }

    public PenaltyExemptionJustification(final PenaltyExemption penaltyExemption,
            final PenaltyExemptionJustificationType justificationType, final String reason) {
        this();
        init(penaltyExemption, justificationType, reason);
    }

    protected void init(PenaltyExemption penaltyExemption, PenaltyExemptionJustificationType justificationType, String reason) {
        checkParameters(justificationType);
        super.init(penaltyExemption, reason);
        super.setPenaltyExemptionJustificationType(justificationType);
    }

    private void checkParameters(PenaltyExemptionJustificationType justificationType) {
        if (justificationType == null) {
            throw new DomainException(
                    "error.net.sourceforge.fenixedu.domain.accounting.events.PenaltyExemptionJustification.justificationType.cannot.be.null");
        }

    }

    @Override
    public void setPenaltyExemptionJustificationType(PenaltyExemptionJustificationType penaltyExemptionJustificationType) {
        throw new DomainException(
                "error.net.sourceforge.fenixedu.domain.accounting.events.PenaltyExemptionJustification.cannot.modify.penaltyExemptionJustificationType");
    }

    @Override
    public LabelFormatter getDescription() {
        final LabelFormatter labelFormatter = new LabelFormatter();
        labelFormatter.appendLabel(getPenaltyExemptionJustificationType().getQualifiedName(),
                LabelFormatter.ENUMERATION_RESOURCES);

        return labelFormatter;

    }

    @Deprecated
    public boolean hasPenaltyExemptionJustificationType() {
        return getPenaltyExemptionJustificationType() != null;
    }

}
