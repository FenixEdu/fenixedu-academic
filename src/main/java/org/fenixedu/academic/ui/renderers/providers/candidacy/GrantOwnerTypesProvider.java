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

import java.util.Arrays;

import org.fenixedu.academic.domain.GrantOwnerType;
import org.fenixedu.academic.domain.candidacy.PersonalInformationBean;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.converters.EnumConverter;

public class GrantOwnerTypesProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        if (source instanceof PersonalInformationBean) {
            PersonalInformationBean personalInformationBean = (PersonalInformationBean) source;
            Student student = personalInformationBean.getStudent();
            boolean isThirdCycle = false;
            boolean isFirstOrSecondCycle = false;

            isThirdCycle = student.hasActivePhdProgramProcess();
            for (Registration registration : student.getActiveRegistrations()) {
                isThirdCycle |= registration.isDEA();
                isFirstOrSecondCycle |= registration.isDegreeOrBolonhaDegreeOrBolonhaIntegratedMasterDegree();
                isFirstOrSecondCycle |= registration.isMasterDegreeOrBolonhaMasterDegree();
            }
            if (isThirdCycle) {
                return GrantOwnerType.getTypesForPhDStudent();
            } else if (isFirstOrSecondCycle) {
                return GrantOwnerType.getTypesForFirstOrSecondCycle();
            }
        }
        return Arrays.asList(GrantOwnerType.values());
    }

    @Override
    public Converter getConverter() {
        return new EnumConverter();
    }

}
