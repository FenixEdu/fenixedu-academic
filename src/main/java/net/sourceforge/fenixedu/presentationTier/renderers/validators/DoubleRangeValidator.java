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

import org.apache.commons.lang.StringUtils;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.renderers.validators.HtmlChainValidator;
import pt.ist.fenixWebFramework.renderers.validators.HtmlValidator;

public class DoubleRangeValidator extends HtmlValidator {

    private boolean isNumber;

    private Double upperBound;

    private Double lowerBound;

    public DoubleRangeValidator() {
        super();
        upperBound = null;
        lowerBound = null;
    }

    public DoubleRangeValidator(HtmlChainValidator htmlChainValidator) {
        super(htmlChainValidator);

        upperBound = null;
        lowerBound = null;
    }

    public double getLowerBound() {
        return lowerBound;
    }

    public void setLowerBound(double lowerBound) {
        this.lowerBound = lowerBound;
    }

    public double getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(double upperBound) {
        this.upperBound = upperBound;
    }

    @Override
    public void performValidation() {

        String numberText = getComponent().getValue();

        if (!StringUtils.isEmpty(numberText)) {
            try {
                double number = Double.parseDouble(numberText.trim());

                boolean inRange = true;
                isNumber = true;

                if (lowerBound != null) {
                    inRange &= lowerBound <= number;
                }

                if (upperBound != null) {
                    inRange &= upperBound >= number;
                }

                this.setValid(inRange);
            } catch (NumberFormatException e) {
                isNumber = false;
                setValid(false);
            }
        }
    }

    @Override
    public String getErrorMessage() {
        if (!isNumber) {
            return RenderUtils.getResourceString("renderers.validator.number");
        }

        if (lowerBound != null && upperBound != null) {
            return RenderUtils.getFormatedResourceString("renderers.validator.number.range.both", lowerBound, upperBound);
        }

        if (lowerBound != null) {
            return RenderUtils.getFormatedResourceString("renderers.validator.number.range.lower", lowerBound);
        }

        return RenderUtils.getFormatedResourceString("renderers.validator.number.range.upper", upperBound);
    }

}
