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

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.renderers.validators.HtmlChainValidator;
import pt.ist.fenixWebFramework.renderers.validators.HtmlValidator;

public class LongRangeValidator extends HtmlValidator {

    private boolean isNumber;

    private Long upperBound;

    private Long lowerBound;

    public LongRangeValidator() {
        super();

        upperBound = null;
        lowerBound = null;
    }

    public LongRangeValidator(HtmlChainValidator htmlChainValidator) {
        super(htmlChainValidator);

        upperBound = null;
        lowerBound = null;
    }

    public long getLowerBound() {
        return lowerBound;
    }

    public void setLowerBound(long lowerBound) {
        this.lowerBound = lowerBound;
    }

    public long getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(long upperBound) {
        this.upperBound = upperBound;
    }

    @Override
    public void performValidation() {

        try {
            long number = Long.parseLong(getComponent().getValue().trim());

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
