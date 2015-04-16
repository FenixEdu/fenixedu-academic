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
package org.fenixedu.academic.domain.serviceRequests;

import java.util.Comparator;

import org.fenixedu.academic.domain.DomainObjectUtil;
import org.fenixedu.bennu.core.domain.Bennu;

public class RegistryCode extends RegistryCode_Base {
    public static Comparator<RegistryCode> COMPARATOR_BY_CODE = new Comparator<RegistryCode>() {
        @Override
        public int compare(RegistryCode o1, RegistryCode o2) {
            if (o1.getCode().compareTo(o2.getCode()) != 0) {
                return o1.getCode().compareTo(o2.getCode());
            }
            return DomainObjectUtil.COMPARATOR_BY_ID.compare(o1, o2);
        }
    };

    protected RegistryCode(InstitutionRegistryCodeGenerator generator, AcademicServiceRequest request) {
        setRegistryCodeGenerator(generator);
        addDocumentRequest(request);
        setCode(generator.getCode(request));
    }

    protected Bennu getRootDomainObject() {
        return getRegistryCodeGenerator().getRootDomainObject();
    }
}
