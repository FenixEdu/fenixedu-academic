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
package org.fenixedu.academic.domain.degreeStructure;

import java.io.Serializable;

import org.fenixedu.academic.domain.CurricularYear;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.LocalizedString;

import pt.ist.fenixframework.DomainObject;

public class NullEctsConversionTable implements IEctsConversionTable, Serializable {
    private static final long serialVersionUID = 1612946655483538738L;

    private final DomainObject target;

    private CycleType cycle;

    private CurricularYear curricularYear;

    public NullEctsConversionTable(DomainObject target) {
        this.target = target;
    }

    public NullEctsConversionTable(DomainObject target, CycleType cycle) {
        this(target);
        this.cycle = cycle;
    }

    public NullEctsConversionTable(DomainObject target, CurricularYear curricularYear) {
        this(target);
        this.curricularYear = curricularYear;
    }

    public NullEctsConversionTable(DomainObject target, CycleType cycle, CurricularYear curricularYear) {
        this(target);
        this.cycle = cycle;
        this.curricularYear = curricularYear;
    }

    @Override
    public DomainObject getTargetEntity() {
        return target;
    }

    @Override
    public EctsComparabilityTable getEctsTable() {
        return new NullEctsComparabilityTable();
    }

    @Override
    public EctsComparabilityPercentages getPercentages() {
        return new NullEctsComparabilityPercentages();
    }

    @Override
    public CurricularYear getCurricularYear() {
        return curricularYear;
    }

    @Override
    public CycleType getCycle() {
        return cycle;
    }

    @Override
    public LocalizedString getPresentationName() {
        return BundleUtil.getLocalizedString(Bundle.ENUMERATION, getClass().getSimpleName() + "presentation.name");
    }
}
