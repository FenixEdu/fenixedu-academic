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
package org.fenixedu.academic.ui.renderers.providers;

import org.fenixedu.academic.domain.accounting.EventTemplate;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.RegistrationDataByExecutionYear;
import org.fenixedu.academic.ui.struts.action.administrativeOffice.studentEnrolment.ManageRegistrationDataByExecutionYearDA;
import org.fenixedu.bennu.core.domain.Bennu;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EventTemplateProviderProvider implements DataProvider {

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

    @Override
    public Object provide(final Object source, final Object currentValue) {
        Stream<EventTemplate> result = Bennu.getInstance().getEventTemplateSet().stream()
                .filter(eventTemplate -> eventTemplate.getEventTemplateFromAlternativeSet().isEmpty());
        if (source instanceof ManageRegistrationDataByExecutionYearDA.RegistrationDataByYearBean) {
            final ManageRegistrationDataByExecutionYearDA.RegistrationDataByYearBean bean
                    = (ManageRegistrationDataByExecutionYearDA.RegistrationDataByYearBean) source;
            final RegistrationDataByExecutionYear byExecutionYear = bean.getDataByExecutionYear();
            if (byExecutionYear != null) {
                final Registration registration = byExecutionYear.getRegistration();
                if (registration != null) {
                    final EventTemplate eventTemplate = registration.getEventTemplate();
                    if (eventTemplate != null) {
                        result = Stream.concat(Stream.of(eventTemplate), eventTemplate.getAlternativeEventTemplateSet().stream());
                    }
                }
            }
        }
        return result.distinct().sorted().collect(Collectors.toList());
    }

}
