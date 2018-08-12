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

import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.phd.serviceRequests.PhdDocumentRequestCreateBean;
import org.fenixedu.academic.domain.phd.serviceRequests.documentRequests.PhdDiplomaRequest;
import org.fenixedu.academic.domain.phd.serviceRequests.documentRequests.PhdRegistryDiplomaRequest;
import org.fenixedu.academic.domain.serviceRequests.RegistryCode;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

import com.google.common.collect.Lists;

public class AssociatePhdRegistryCodesProvider implements DataProvider {

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

    @Override
    public Object provide(Object source, Object currentValue) {
        PhdDocumentRequestCreateBean bean = (PhdDocumentRequestCreateBean) source;
        switch (bean.getDocumentRequestType()) {
        case PHD_FINALIZATION_CERTIFICATE:
            return getRegistryDiplomaCodes(bean);
        case DIPLOMA_REQUEST:
            return getPossibleCodesForDiploma(bean);
        case DIPLOMA_SUPPLEMENT_REQUEST:
            return getPossibleCodesForDiplomaSupplement(bean);
        case REGISTRY_DIPLOMA_REQUEST:
            return getPossibleCodesForRegistryDiploma(bean);
        default:
            return Lists.newArrayList();
        }
    }

    private Set<RegistryCode> getRegistryDiplomaCodes(PhdDocumentRequestCreateBean bean) {
        return bean.getPhdIndividualProgramProcess().getRegistryDiplomaRequests().stream()
                .map(PhdRegistryDiplomaRequest::getRegistryCode).collect(Collectors.toSet());
    }

    private Set<RegistryCode> getPossibleCodesForDiploma(PhdDocumentRequestCreateBean bean) {
        return bean.getPhdIndividualProgramProcess().getRegistryDiplomaRequests().stream()
                .map(PhdRegistryDiplomaRequest::getRegistryCode).filter(rc -> rc.getPhdDiploma() == null).collect(Collectors
                        .toSet());
    }

    private Set<RegistryCode> getPossibleCodesForRegistryDiploma(PhdDocumentRequestCreateBean bean) {
        return bean.getPhdIndividualProgramProcess().getDiplomaRequests().stream()
                .map(PhdDiplomaRequest::getRegistryCode).filter(rc -> rc.getPhdRegistryDiploma() == null).collect(Collectors
                        .toSet());
    }

    private Set<RegistryCode> getPossibleCodesForDiplomaSupplement(PhdDocumentRequestCreateBean bean) {
        PhdIndividualProgramProcess process = bean.getPhdIndividualProgramProcess();
        Set<RegistryCode> codes =
                process.getRegistryDiplomaRequests().stream().map(PhdRegistryDiplomaRequest::getRegistryCode)
                        .collect(Collectors.toSet());
        process.getDiplomaRequests().stream().map(PhdDiplomaRequest::getRegistryCode).forEach(codes::add);
        return codes.stream().filter(rc -> rc.getPhdDiplomaSupplement() == null).collect(Collectors.toSet());
    }
}
