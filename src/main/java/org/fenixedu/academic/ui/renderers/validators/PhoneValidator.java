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
package org.fenixedu.academic.ui.renderers.validators;

import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.PhoneUtil;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.ist.fenixWebFramework.renderers.validators.HtmlChainValidator;
import pt.ist.fenixWebFramework.renderers.validators.HtmlValidator;

@SuppressWarnings("serial")
public class PhoneValidator extends HtmlValidator {

    public PhoneValidator() {
        super();
    }

    public PhoneValidator(HtmlChainValidator htmlChainValidator) {
        super(htmlChainValidator);
    }

    @Override
    public void performValidation() {
        final String value = getComponent().getValue();
        // let the RequiredValidator do the work if this slot is actually required
        final boolean isEmpty = value == null || value.trim().isEmpty();
        setValid(isEmpty || PhoneUtil.isValidNumber(value));
    }

    @Override
    public String getErrorMessage() {
        return BundleUtil.getString(Bundle.RENDERER, "renderers.validator.phone.number");
    }

}
