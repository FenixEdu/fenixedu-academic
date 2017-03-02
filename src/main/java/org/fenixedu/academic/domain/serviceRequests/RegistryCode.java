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
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.academic.domain.DomainObjectUtil;
import org.fenixedu.academic.domain.phd.serviceRequests.documentRequests.PhdDiplomaRequest;
import org.fenixedu.academic.domain.phd.serviceRequests.documentRequests.PhdDiplomaSupplementRequest;
import org.fenixedu.academic.domain.phd.serviceRequests.documentRequests.PhdRegistryDiplomaRequest;
import org.fenixedu.academic.domain.phd.serviceRequests.documentRequests.certificates.PhdFinalizationCertificateRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DegreeFinalizationCertificateRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DiplomaRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DiplomaSupplementRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.RegistryDiplomaRequest;
import org.fenixedu.bennu.core.domain.Bennu;

public class RegistryCode extends RegistryCode_Base {
    public static Comparator<RegistryCode> COMPARATOR_BY_CODE = Comparator
                .nullsLast(Comparator.comparing(RegistryCode::getCode).thenComparing(DomainObjectUtil.COMPARATOR_BY_ID));

    protected RegistryCode(InstitutionRegistryCodeGenerator generator, AcademicServiceRequest request) {
        setRegistryCodeGenerator(generator);
        addDocumentRequest(request);
        setCode(generator.getCode(request));
    }

    protected Bennu getRootDomainObject() {
        return getRegistryCodeGenerator().getRootDomainObject();
    }

    //XXX Registry code is either linked to a set of documents for either phd or other degrees, not a mix of both
    //XXX Registry code is to be linked at most to 1 of each of the following documents

    public DiplomaSupplementRequest getDiplomaSupplement() {
        return getRequestsOfClass(DiplomaSupplementRequest.class).findAny().orElse(null);
    }

    public DiplomaRequest getDiploma() {
        return getRequestsOfClass(DiplomaRequest.class).findAny().orElse(null);
    }

    public RegistryDiplomaRequest getRegistryDiploma() {
        return getRequestsOfClass(RegistryDiplomaRequest.class).findAny().orElse(null);
    }

    public PhdDiplomaRequest getPhdDiploma() {
        return getRequestsOfClass(PhdDiplomaRequest.class).findAny().orElse(null);
    }

    public PhdRegistryDiplomaRequest getPhdRegistryDiploma() {
        return getRequestsOfClass(PhdRegistryDiplomaRequest.class).findAny().orElse(null);
    }

    public PhdDiplomaSupplementRequest getPhdDiplomaSupplement() {
        return getRequestsOfClass(PhdDiplomaSupplementRequest.class).findAny().orElse(null);
    }

    //XXX Registry code may have multiple of the following documents

    public Set<DegreeFinalizationCertificateRequest> getDegreeFinalizationCertificates() {
        return getRequestsOfClass(DegreeFinalizationCertificateRequest.class).collect(Collectors.toSet());
    }

    public Set<PhdFinalizationCertificateRequest> getPhdFinalizationCertificates() {
        return getRequestsOfClass(PhdFinalizationCertificateRequest.class).collect(Collectors.toSet());
    }

    public boolean safeDelete() {
        if (getDocumentRequestSet().isEmpty()) {
            setRegistryCodeGenerator(null);
            deleteDomainObject();
            return true;
        }
        return false;
    }

    public String getDescription() {
        String code = getCode();
        Optional<String> mainRequestDescription =
                Stream.of(RegistryDiplomaRequest.class, PhdRegistryDiplomaRequest.class, DiplomaRequest.class,
                        PhdDiplomaRequest.class, DegreeFinalizationCertificateRequest.class,
                        PhdFinalizationCertificateRequest.class).flatMap(c -> getRequestsOfClass(c)).findFirst()
                        .map(asr -> asr.getDescription() + " (" + asr.getLanguage().getLanguage().toUpperCase() + ")");
        return mainRequestDescription.isPresent() ? code + " - " + mainRequestDescription.get() : code;
    }

    private <T extends AcademicServiceRequest> Stream<T> getRequestsOfClass(Class<T> c) {
        return getDocumentRequestSet().stream().filter(c::isInstance).map(c::cast);
    }
}
