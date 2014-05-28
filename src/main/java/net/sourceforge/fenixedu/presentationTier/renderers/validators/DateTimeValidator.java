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

import pt.ist.fenixWebFramework.rendererExtensions.DateTimeInputRenderer.DateTimeConverter;
import pt.ist.fenixWebFramework.renderers.components.HtmlSimpleValueComponent;
import pt.ist.fenixWebFramework.renderers.validators.HtmlChainValidator;
import pt.ist.fenixWebFramework.renderers.validators.HtmlValidator;

public class DateTimeValidator extends HtmlValidator {

    private boolean required;

    public DateTimeValidator() {
        super();
        setKey(true);
    }

    public DateTimeValidator(HtmlChainValidator htmlChainValidator) {
        super(htmlChainValidator);
        setKey(true);
    }

    public boolean isRequired() {
        return this.required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    @Override
    public void performValidation() {
        HtmlSimpleValueComponent component = (HtmlSimpleValueComponent) getComponent();

        String value = component.getValue();

        if (value == null || value.length() == 0) {
            setMessage("renderers.validator.dateTime.required");
            setValid(!isRequired());
        } else {
            if (value.equals(DateTimeConverter.INVALID)) {
                setMessage("renderers.validator.dateTime.invalid");
                setValid(false);
            } else if (value.equals(DateTimeConverter.INCOMPLETE)) {
                setMessage("renderers.validator.dateTime.incomplete");
                setValid(false);
            } else {
                int indexOfT = value.indexOf('T');
                String[] dateParts = value.substring(0, indexOfT).split("-");
                String[] timeParts = value.substring(indexOfT + 1).split(":");

                try {
                    int year = Integer.valueOf(dateParts[0]);
                    if (year < 1000 || year > 9999) {
                        setMessage("renderers.validator.dateTime.invalid");
                        setValid(false);
                    } else {
                        int hours = Integer.parseInt(timeParts[0]);
                        int minutes = Integer.parseInt(timeParts[1]);
                        if (inRange(hours, 0, 23) && inRange(minutes, 0, 59)) {
                            setValid(true);
                        } else {
                            setMessage("renderers.validator.dateTime.notInRange");
                            setValid(false);
                        }
                    }
                } catch (NumberFormatException e) {
                    setMessage("renderers.validator.dateTime.notNumbers");
                    setValid(false);
                }
            }
        }
    }

    private boolean inRange(int value, int min, int max) {
        return value >= min && value <= max;
    }

}
