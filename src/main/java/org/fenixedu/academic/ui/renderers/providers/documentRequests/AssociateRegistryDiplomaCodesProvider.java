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
package org.fenixedu.academic.ui.renderers.providers.documentRequests;

import java.util.Set;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.degreeStructure.ProgramConclusion;
import org.fenixedu.academic.domain.serviceRequests.RegistryCode;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DiplomaRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.RegistryDiplomaRequest;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.dto.serviceRequests.DocumentRequestCreateBean;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

import com.google.common.collect.Lists;

public class AssociateRegistryDiplomaCodesProvider implements DataProvider {

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

    @Override
    public Object provide(Object source, Object currentValue) {
        DocumentRequestCreateBean bean = ((DocumentRequestCreateBean) source);
        switch (bean.getChosenDocumentRequestType()) {
        case DEGREE_FINALIZATION_CERTIFICATE:
            return getRegistryDiplomaCodes(bean);
        case DIPLOMA_REQUEST:
            return getPossibleCodesForDiploma(bean);
        case DIPLOMA_SUPPLEMENT_REQUEST:
            return getPossibleCodesForDiplomaSupplement(bean);
        default:
            return Lists.newArrayList();
        }
    }

    private Set<RegistryCode> getRegistryDiplomaCodes(DocumentRequestCreateBean bean) {
        return bean.getRegistration().getRegistryDiplomaRequests(bean.getProgramConclusion()).stream()
                .map(RegistryDiplomaRequest::getRegistryCode).collect(Collectors.toSet());
    }

    private Set<RegistryCode> getPossibleCodesForDiploma(DocumentRequestCreateBean bean) {
        return bean.getRegistration().getRegistryDiplomaRequests(bean.getProgramConclusion()).stream()
                .map(RegistryDiplomaRequest::getRegistryCode).filter(rc -> rc.getDiploma() == null).collect(Collectors.toSet());
    }

    private Set<RegistryCode> getPossibleCodesForDiplomaSupplement(DocumentRequestCreateBean bean) {
        Registration registration = bean.getRegistration();
        ProgramConclusion programConclusion = bean.getProgramConclusion();
        Set<RegistryCode> codes =
                registration.getRegistryDiplomaRequests(programConclusion).stream().map(RegistryDiplomaRequest::getRegistryCode)
                        .collect(Collectors.toSet());
        registration.getDiplomaRequests(programConclusion).stream().map(DiplomaRequest::getRegistryCode).forEach(codes::add);
        return codes.stream().filter(rc -> rc.getDiplomaSupplement() == null).collect(Collectors.toSet());
    }
}
