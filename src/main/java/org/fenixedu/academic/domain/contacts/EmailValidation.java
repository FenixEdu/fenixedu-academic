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
package org.fenixedu.academic.domain.contacts;

import java.util.UUID;

import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.bennu.core.util.CoreConfiguration;

import org.fenixedu.messaging.core.domain.Message;
import pt.ist.fenixframework.Atomic;

public class EmailValidation extends EmailValidation_Base {

    public EmailValidation(PartyContact contact) {
        super();
        super.init(contact);
    }

    public String getEmailValue() {
        return ((EmailAddress) getPartyContact()).getValue();
    }

    public void generateToken() {
        if (getToken() == null) {
            setToken(UUID.randomUUID().toString());
        }
    }

    @Override
    @Atomic
    public void triggerValidationProcess() {
        if (!isValid()) {
            generateToken();
            sendValidationEmail();
        }
    }

    private void sendValidationEmail() {
        final String token = getToken();
        final String URL = String.format(CoreConfiguration.getConfiguration().applicationUrl()
                        + "/external/partyContactValidation.do?method=validate&validationOID=%s&token=%s", getExternalId(), token);

        final String subject = "Sistema Fénix @ " + Unit.getInstitutionAcronym() + " : Validação de Email";
        final String body_format =
                "Caro Utilizador\n Deverá validar o seu email introduzindo o código %s na página de verificação ou \n carregar no seguinte link : \n %s \n Os melhores cumprimentos,\n A equipa Fénix";
        final String body = String.format(body_format, token, URL);
        Message.fromSystem().singleBcc(getEmailValue())
                .subject(subject).textBody(body)
                .send();
    }
}
