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

import java.util.Collections;

import org.fenixedu.academic.domain.JobBean;
import org.fenixedu.academic.dto.alumni.AlumniJobBean;
import org.fenixedu.academic.dto.alumni.publicAccess.AlumniPublicAccessBean;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class BusinessAreaChildProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {

        if (source.getClass().equals(AlumniJobBean.class)) {
            final AlumniJobBean jobBean = (AlumniJobBean) source;
            if (jobBean.getParentBusinessArea() != null) {
                return jobBean.getParentBusinessArea().getChildAreasSet();
            }

        } else if (source.getClass().equals(AlumniPublicAccessBean.class)) {
            final AlumniPublicAccessBean publicBean = (AlumniPublicAccessBean) source;
            if (publicBean.getJobBean().getParentBusinessArea() != null) {
                return publicBean.getJobBean().getParentBusinessArea().getChildAreasSet();
            }

        } else if (source.getClass().equals(JobBean.class)) {
            final JobBean jobBean = (JobBean) source;
            if (jobBean.hasParentBusinessArea()) {
                return jobBean.getParentBusinessArea().getChildAreasSet();
            }
        }

        return Collections.emptyList();
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
