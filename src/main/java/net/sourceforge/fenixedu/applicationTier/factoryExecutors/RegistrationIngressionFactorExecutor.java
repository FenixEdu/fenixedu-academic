/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.applicationTier.factoryExecutors;

import java.io.Serializable;

import net.sourceforge.fenixedu.dataTransferObject.candidacy.IngressionInformationBean;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;

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
                setRegistrationAgreement(getRegistration().getRegistrationAgreement());
                setAgreementInformation(getRegistration().getAgreementInformation());
                setIngression(getRegistration().getIngression());
                setEntryPhase(getRegistration().getEntryPhase());
            }
        }

        @Override
        public Object execute() {
            getRegistration().setRegistrationAgreement(getRegistrationAgreement());
            getRegistration().setAgreementInformation(getAgreementInformation());
            getRegistration().setIngression(getIngression());
            getRegistration().setEntryPhase(getEntryPhase());
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
