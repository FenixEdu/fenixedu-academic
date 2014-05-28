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

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.renderers.components.HtmlSimpleValueComponent;
import pt.ist.fenixWebFramework.renderers.validators.HtmlChainValidator;

public class FutureDateTimeValidator extends DateTimeValidator {

    public FutureDateTimeValidator() {
        super();
    }

    public FutureDateTimeValidator(HtmlChainValidator htmlChainValidator) {
        super(htmlChainValidator);
    }

    @Override
    public void performValidation() {
        HtmlSimpleValueComponent component = (HtmlSimpleValueComponent) getComponent();

        String value = component.getValue();

        if (value == null || value.length() == 0) {
            setMessage("renderers.validator.dateTime.required");
            setValid(!isRequired());
        } else {
            super.performValidation();

            if (isValid()) {
                DateTime dateTime = new DateTime(value);

                if (dateTime.isBeforeNow()) {
                    setMessage("renderers.validator.dateTime.beforeNow");
                    setValid(false);
                }
            }
        }
    }

}
