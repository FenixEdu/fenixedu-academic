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
/*
 * Created on Feb 10, 2006
 *	by mrsp
 */
package org.fenixedu.academic.domain.organizationalStructure;

import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.ist.fenixWebFramework.rendererExtensions.util.IPresentableEnum;

public enum AccountabilityTypeEnum implements IPresentableEnum {

    MANAGEMENT_FUNCTION /*deprecated*/,

    ORGANIZATIONAL_STRUCTURE, ACADEMIC_STRUCTURE, ADMINISTRATIVE_STRUCTURE /*deprecated*/,

    GEOGRAPHIC,

    // Contracts
    WORKING_CONTRACT, MAILING_CONTRACT /*deprecated*/, INVITATION /*deprecated*/,

    RESEARCH_CONTRACT /*deprecated*/,

    ASSIDUOUSNESS_STRUCTURE /*deprecated*/;

    public String getName() {
        return name();
    }

    public String getQualifiedName() {
        return AccountabilityTypeEnum.class.getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
        return AccountabilityTypeEnum.class.getName() + "." + name();
    }

    @Override
    public String getLocalizedName() {
        return BundleUtil.getString(Bundle.ENUMERATION, getQualifiedName());
    }
}