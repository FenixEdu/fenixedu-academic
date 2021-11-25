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
package org.fenixedu.academic.service.factoryExecutors;

import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.dto.candidacy.IngressionInformationBean;
import org.fenixedu.academic.service.services.commons.FactoryExecutor;

import java.io.Serializable;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class RegistrationIngressionFactorExecutor {

    @SuppressWarnings("serial")
    public static class RegistrationIngressionEditor extends IngressionInformationBean implements FactoryExecutor, Serializable {

        private Registration registration;

        public RegistrationIngressionEditor(Registration registration) {
            super();
            setRegistration(registration);

            if (hasRegistration()) {
                setRegistrationProtocol(getRegistration().getRegistrationProtocol());
                setAgreementInformation(getRegistration().getAgreementInformation());
                setIngressionType(getRegistration().getIngressionType());
                setEntryPhase(getRegistration().getEntryPhase());
                setEventTemplate(getRegistration().getEventTemplate());
            }
        }

        @Override
        public Object execute() {
            getRegistration().setRegistrationProtocol(getRegistrationProtocol());
            getRegistration().setAgreementInformation(getAgreementInformation());
            getRegistration().setIngressionType(getIngressionType());
            getRegistration().setEntryPhase(getEntryPhase());
            getRegistration().setEventTemplate(getEventTemplate());
            return getRegistration();
        }

        public Registration getRegistration() {
            return this.registration;
        }

        public boolean hasRegistration() {
            return getRegistration() != null;
        }

        public void setRegistration(Registration registration) {
            this.registration = registration;
        }
    }
}
