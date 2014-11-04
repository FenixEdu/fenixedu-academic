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
package org.fenixedu.academic.ui.renderers.providers.candidacy;

import java.util.ArrayList;

import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.dto.administrativeOffice.ExecutionDegreeBean;
import org.fenixedu.academic.service.factoryExecutors.StudentCurricularPlanFactoryExecutor.StudentCurricularPlanCreator;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class CycleTypeProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {

        if (source instanceof ExecutionDegreeBean) {
            ExecutionDegreeBean executionDegreeBean = (ExecutionDegreeBean) source;
            if (executionDegreeBean.getDegree() != null) {
                return executionDegreeBean.getDegree().getDegreeType().getCycleTypes();
            }
        } else if (source instanceof StudentCurricularPlanCreator) {
            StudentCurricularPlanCreator studentCurricularPlanCreator = (StudentCurricularPlanCreator) source;
            if (studentCurricularPlanCreator.getDegree() != null) {
                return studentCurricularPlanCreator.getDegree().getDegreeType().getCycleTypes();
            }
        }

        return new ArrayList<CycleType>();
    }

    @Override
    public Converter getConverter() {
        return null;
    }

}
