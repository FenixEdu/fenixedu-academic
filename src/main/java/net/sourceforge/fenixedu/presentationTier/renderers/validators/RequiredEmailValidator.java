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
package net.sourceforge.fenixedu.presentationTier.renderers.validators;

import pt.ist.fenixWebFramework.renderers.validators.EmailValidator;
import pt.ist.fenixWebFramework.renderers.validators.HtmlChainValidator;

public class RequiredEmailValidator extends EmailValidator {

    public RequiredEmailValidator() {
        super();
    }

    public RequiredEmailValidator(HtmlChainValidator htmlChainValidator) {
        super(htmlChainValidator);
    }

    @Override
    public String getValue() {
        return super.getValue().toLowerCase();
    }

    @Override
    public void performValidation() {
        final String email = getComponent().getValue();
        if (email != null && email.length() > 0) {
            super.performValidation();
        } else {
            setValid(false);
            setMessage("renderers.validator.required");
        }
    }

}
